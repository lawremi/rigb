package com.gene.bcb.rigb;

public class DataAccessException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public DataAccessException(String message) {
		super(message);
	}
	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
