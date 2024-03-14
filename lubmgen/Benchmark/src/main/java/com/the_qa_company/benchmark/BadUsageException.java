package com.the_qa_company.benchmark;

public class BadUsageException extends RuntimeException {
	public BadUsageException() {
	}
	public BadUsageException(String message) {
		super(message);
	}

	public BadUsageException(String message, Throwable cause) {
		super(message, cause);
	}
}
