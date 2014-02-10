package com.strnadj.OptParser.exceptions;

/**
 * Exception class - overlaping brackets
 */
public class OverlapingBracketsException extends Exception
{
	public OverlapingBracketsException(String error) {
		super(error);
	}
}