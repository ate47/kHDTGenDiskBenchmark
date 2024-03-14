package com.the_qa_company.benchmark;

import java.io.IOException;

public abstract class Tool {
	private final String name;
	private final String usage;
	private final String desc;

	protected Tool(String name, String usage, String desc) {
		this.name = name;
		this.usage = usage;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public String getUsage() {
		return usage;
	}

	public String getDesc() {
		return desc;
	}

	public abstract void execute(CliMain main) throws Exception;
}
