package com.the_qa_company.benchmark;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import com.the_qa_company.benchmark.tools.IndexLubm;
import com.the_qa_company.qendpoint.core.util.StopWatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CliMain {
	public static void main(String[] args) throws Exception {
		CliMain cli = new CliMain();
		JCommander bld = JCommander.newBuilder().addObject(cli).build();
		bld.setProgramName("qepbench");
		bld.parse(args);

		if (cli.help) {
			bld.usage();
			return;
		}

		if (cli.parameters.isEmpty()) {
			System.out.println("-help for help");
			System.out.println("tools:");
			cli.tools.forEach((k, t) -> System.out.println(t.getName() + t.getUsage() + ": " + t.getDesc()));
			return;
		}

		/*
			1. Good value of k? (Ontoconst), maybe try on ASC42/NAM?
			2. LUBM 1->40k, ASC42->DS (Ontoconst)
			3. Index wikidata, (ASC42)
			4. k-hdtdiffcat update (Truthy+Wikidata delta) (NAM)
		 */

		String name = cli.parameters.get(0);
		Tool tool = cli.tools.get(name.toLowerCase());
		if (tool == null) {
			System.err.printf("Can't find tool '%s'\n", name);
			return;
		}
		StopWatch sw = new StopWatch();
		try {
			tool.execute(cli);
		} catch (BadUsageException e) {
			String message = e.getMessage();
			if (message != null) {
				System.err.println(message);
			}
			bld.usage();
		} finally {
			System.out.println("ended in " + sw.stopAndShow());
		}
	}

	@Parameter(names = "-help", description = "show help")
	public boolean help;

	@Parameter(description = "<tool>")
	public List<String> parameters = Lists.newArrayList();

	private final Map<String, Tool> tools = new TreeMap<>();

	public CliMain() {
		addTool(new IndexLubm());
	}

	public void addTool(Tool tool) {
		tools.put(tool.getName().toLowerCase(), tool);
	}

}
