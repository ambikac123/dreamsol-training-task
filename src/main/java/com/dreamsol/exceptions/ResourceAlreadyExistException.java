package com.dreamsol.exceptions;

public class ResourceAlreadyExistException extends RuntimeException
{
	String resourceName;
	public ResourceAlreadyExistException(String resourceName) {
		super(String.format("%s already exist!!",resourceName));
		this.resourceName = resourceName;
	}	
}
