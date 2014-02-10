package com.strnadj.OptParserTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.strnadj.OptParser.OptParser;
import com.strnadj.OptParser.exceptions.MissingOptions;
import com.strnadj.OptParser.exceptions.MissingOptionsHelp;
import com.strnadj.OptParser.exceptions.OverlapingBracketsException;
import com.strnadj.OptParser.exceptions.UnexpectedOption;

/**
 * Unit test for simple App.
 */
public class OptParserTest
{    
	
    /**
     * Test creating Parser with fluent interface
     */
	@Test
    public void testValidCountOfRequiredParameters()
    {
    	OptParser parser = OptParser.createOptionParser("test", "Test program")
    	                       .addOption('h', "help", OptParser.OPTIONAL, "false", "Show help")
    	                       .addOption('p', "parameter", OptParser.OPTIONAL, "false", "Test parameter")
    	                       .addPathOrExpression("path", OptParser.REQUIRED, "", "Params");
    	
    	assertEquals("Invalid optional parameters", 1, parser.getRequiredParameters().size());
    }
	
	/**
	 * Test exception when brackets overlaps.
	 */
	@Test(expected=OverlapingBracketsException.class)
	public void testOverleapingBrackets() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");
		
		// Assert exception
		String cmdLine = "--parameter \"test'test2\" -f pepa'";
		parser.parseArguments(cmdLine);		
	}
	
	/**
	 * Invalid count of exception (simple is not ended)
	 * @throws Exception
	 */
	@Test(expected=OverlapingBracketsException.class)
	public void testInvalidBracketCount() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");		
		
		// Assert invalid count of brackets
		String cmdLine = "--parameter \"test\" -f 'pepa";
		parser.parseArguments(cmdLine);
	}
	
	/**
	 * Unexcepted option from command line!
	 * @throws Exception
	 */
	@Test(expected=UnexpectedOption.class)
	public void testUnexpectedOption() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");		
		
		// Assert invalid count of brackets
		String cmdLine = "--invalidOption \"test1\" --parameter \"test\" -f 'pepa'";
		parser.parseArguments(cmdLine);		
	}
	
	/**
	 * Missing required option
	 * @throws Exception 
	 */
	@Test(expected=MissingOptions.class)
	public void testMissingRequiredOption() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('c', "help", OptParser.OPTIONAL, "", "Show this help", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('p', "parameter", OptParser.REQUIRED, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2", OptParser.OPTION_NO_VALUE);		
		
		// Assert invalid count of brackets
		String cmdLine = "-f";
		parser.parseArguments(cmdLine);
	}
	
	/**
	 * Missing required path or expression
	 */
	@Test(expected=MissingOptions.class)
	public void testMissingRequiredPOE() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2")
				.addPathOrExpression("testpoe", OptParser.REQUIRED, "", "Path for test");		
		
		// Assert invalid count of brackets
		String cmdLine = "-f";
		parser.parseArguments(cmdLine);
	}
	
	/**
	 * Missing required path or expression
	 */
	@Test(expected=MissingOptions.class)
	public void testMissingRequiredPOEAfterOptionWithValue() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2")
				.addPathOrExpression("testpoe", OptParser.REQUIRED, "", "Path for test");		
		
		// Assert invalid count of brackets
		String cmdLine = "-p \"test\"";
		parser.parseArguments(cmdLine);
	}	
	
	/**
	 * Test get help.
	 */
	@Test(expected=MissingOptionsHelp.class)
	public void testGetHelp() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2")
				.addPathOrExpression("testpoe", OptParser.REQUIRED, "", "Path for test");		
		
		// Assert invalid count of brackets
		String cmdLine = "-h";
		parser.parseArguments(cmdLine);
	}
	
	
	/**
	 * Test correct processing CMD line with optional parameters
	 */
	@Test
	public void testProcessingOptional() throws Exception
	{
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter", OptParser.OPTION_VALUE_IS_REQUIRED)
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");
		
		String cmdLine = "--parameter test2 -h test1";
		
		// Parse
		parser.parseArguments(cmdLine);
			
		// Try value
		assertEquals("Invalid parsing help parameter", "test1", parser.getOption("help").value());
		assertEquals("Invalid parsing first parameter", "test2", parser.getOption("parameter").value());
		assertEquals("Invalid parsing second parameter", null, parser.getOption("parameter2"));
	}
}
