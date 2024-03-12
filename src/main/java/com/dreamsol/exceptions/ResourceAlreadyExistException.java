package com.dreamsol.exceptions;

public class ResourceAlreadyExistException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	String resourceName;
	String fieldName;
	public ResourceAlreadyExistException(String resourceName) {
		super(String.format("%s already exist!!",resourceName));
		this.resourceName = resourceName;
	}	
}
