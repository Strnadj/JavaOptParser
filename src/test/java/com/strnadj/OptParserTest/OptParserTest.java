package com.strnadj.OptParserTest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Test;
import org.omg.CosNaming.NamingContextExtPackage.AddressHelper;

import com.strnadj.OptParser.OptParser;
import com.strnadj.OptParser.exceptions.MissingOptions;
import com.strnadj.OptParser.exceptions.MissingOptionsHelp;
import com.strnadj.OptParser.exceptions.OverlapingBracketsException;
import com.strnadj.OptParser.exceptions.UnexpectedOption;
import com.strnadj.OptParser.exceptions.MissingOptionValue;;

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
	 * Test exception when brackets overlaps.
	 */
	@Test(expected=OverlapingBracketsException.class)
	public void testOverleapingSingleOpen() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");
		
		// Assert exception
		String cmdLine = "--parameter \"test2\" -f 'pepa\"";
		parser.parseArguments(cmdLine);		
	}
		
	/**
	 * Test exception when brackets overlaps.
	 */
	@Test(expected=OverlapingBracketsException.class)
	public void testOverleapingDoubleNotClosed() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");
		
		// Assert exception
		String cmdLine = "--parameter \"test2\" -f \"pepa";
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
	 * Test of assigning required POE before optional POE
	 */
	@Test
	public void testOptionalPOE() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addPathOrExpression("testpoe2", OptParser.OPTIONAL, "test", "POE 2 for optional test")
				.addPathOrExpression("testpoe", OptParser.REQUIRED, "", "Path for test");
		
		// Assert invalid count of brackets
		String cmdLine = "testpoe1";
		parser.parseArguments(new LinkedList<String>(Arrays.asList(cmdLine.split(" "))));
		
		// Assert Required expression
		assertEquals("Invalid assign required POE", "testpoe1", parser.getOption("testpoe").getValue());
		assertEquals("Option poe filled", null, parser.getOption("testpoe2"));
		
		// But I can get default value from testpoe2
		assertEquals("Invalid assigning default string to Optional POE", "test", parser.getOptionValue("testpoe2"));
	}
	
	/** 
	 * Test missing otpion value
	 */
	@Test(expected=MissingOptionValue.class)
	public void testMissingOptionValue() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOptionRequiredValue('c', "help", OptParser.OPTIONAL, null, "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, null, "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, null, "Test parameter 2");		
		
		// Assert invalid count of brackets
		String cmdLine = "-c -p";
		parser.parseArguments(cmdLine);		
	}
	
	/** 
	 * Test missing otpion value
	 */
	@Test(expected=MissingOptionValue.class)
	public void testMissingOptionValueNothing() throws Exception {
		OptParser parser = OptParser.createOptionParser("test", "Test program")
				.addOptionRequiredValue('c', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOption('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");		
		
		// Assert invalid count of brackets
		String cmdLine = "-c";
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
				.addOptionRequiredValue('c', "help", OptParser.OPTIONAL, "", "Show this help")
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
				.addOptionRequiredValue('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
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
				.addOptionRequiredValue('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
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
				.addOptionRequiredValue('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
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
				.addOptionRequiredValue('h', "help", OptParser.OPTIONAL, "", "Show this help")
				.addOptionRequiredValue('p', "parameter", OptParser.OPTIONAL, "", "Test parameter")
				.addOption('f', "parameter2", OptParser.OPTIONAL, "", "Test parameter 2");
		
		String cmdLine = "--parameter test2 -h test1";
		
		// Parse
		parser.parseArguments(cmdLine);
			
		// Try value
		assertEquals("Invalid parsing help parameter", "test1", parser.getOption("help").value());
		assertEquals("Invalid parsing first parameter", "test2", parser.getOption("parameter").value());
		assertEquals("Invalid parsing second parameter", null, parser.getOption("parameter2"));
		
		// Test is filled?
		assertEquals("Invalid filed", true, parser.isOptionFilled("help"));
		assertEquals("Invalid non-filed", false, parser.isOptionFilled("parameter2"));
		
	}
}
