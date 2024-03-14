package com.the_qa_company.benchmark.tools;

import com.the_qa_company.benchmark.CliMain;
import com.the_qa_company.benchmark.Tool;
import com.the_qa_company.qendpoint.core.enums.RDFNotation;
import com.the_qa_company.qendpoint.core.exceptions.ParserException;
import com.the_qa_company.qendpoint.core.iterator.utils.CombinedIterator;
import com.the_qa_company.qendpoint.core.listener.ProgressListener;
import com.the_qa_company.qendpoint.core.options.HDTOptions;
import com.the_qa_company.qendpoint.core.hdt.HDT;
import com.the_qa_company.qendpoint.core.hdt.HDTManager;
import com.the_qa_company.qendpoint.core.options.HDTOptionsKeys;
import com.the_qa_company.qendpoint.core.util.StopWatch;
import org.apache.commons.io.file.PathUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class IndexLubm extends Tool {
	public IndexLubm() {
		super("lubm", " [file]", "run lubm benchmark, index a file");
	}

	@Override
	public void execute(CliMain main) throws IOException, ParserException {
		if (main.parameters.size() < 3) {
			System.err.println("lubm [ds] [out]");
			return;
		}

		Path input = Path.of(main.parameters.get(1));
		Path output = Path.of(main.parameters.get(2));
		Path prof = output.resolveSibling(output.getFileName() + ".prof");
		Path work = output.resolveSibling("work");

		System.out.printf("gen %s->%s using (%s/%s)\n", input, output, prof, work);

		try {
			PathUtils.deleteDirectory(work);
		} catch (NoSuchFileException ignore) {}
		Files.createDirectories(work);

		HDTOptions spec = HDTOptions.of(
				HDTOptionsKeys.PROFILER_KEY, true,
				HDTOptionsKeys.PROFILER_OUTPUT_KEY, prof,
				HDTOptionsKeys.NT_SIMPLE_PARSER_KEY, true,
				HDTOptionsKeys.DICTIONARY_TYPE_KEY, HDTOptionsKeys.DICTIONARY_TYPE_VALUE_FOUR_SECTION,
				HDTOptionsKeys.LOADER_TYPE_KEY, HDTOptionsKeys.LOADER_TYPE_VALUE_CAT,
				HDTOptionsKeys.HDTCAT_LOCATION, work.resolve("cat"),
				HDTOptionsKeys.LOADER_CATTREE_LOADERTYPE_KEY, HDTOptionsKeys.LOADER_CATTREE_HDT_SUPPLIER_VALUE_DISK,
				HDTOptionsKeys.LOADER_CATTREE_FUTURE_HDT_LOCATION_KEY, output,
				HDTOptionsKeys.LOADER_DISK_FUTURE_HDT_LOCATION_KEY, work.resolve("gd.hdt"),
				HDTOptionsKeys.HDTCAT_FUTURE_LOCATION, output,
				HDTOptionsKeys.LOADER_CATTREE_LOCATION_KEY, work.resolve("ct"),
				HDTOptionsKeys.LOADER_CATTREE_KCAT, 20,
				HDTOptionsKeys.LOADER_DISK_LOCATION_KEY, work.resolve("gd")
		);

		StopWatch sw = new StopWatch();
		try (HDT hdt = HDTManager.generateHDT(input, "https://wdaqua.eu/lubm", RDFNotation.DIR, spec, ProgressListener.sout())){
			hdt.saveToHDT(output);
			System.out.printf("generated in %s\n", sw.stopAndShow());
		} finally {
			try {
				PathUtils.deleteDirectory(work);
			} catch (NoSuchFileException ignore) {}
		}
	}
}
