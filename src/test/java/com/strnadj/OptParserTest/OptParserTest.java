package com.strnadj.OptParserTest;

import static org.junit.Assert.*;

import org.junit.Test;


import com.strnadj.OptParser.OptParser;

/**
 * Unit test for simple App.
 */
public class OptParserTest
{    
    /**
     * Test creating Parser with fluent interface
     */
	@Test
    public void testApp()
    {
    	OptParser parser = OptParser.createOptionParser("test", "Test program")
    	                       .addOption('h', "help", OptParser.OPTIONAL, "false", "Show help")
    	                       .addOption('p', "parameter", OptParser.OPTIONAL, "false", "Test parameter");
    	
    	assertEquals("Invalid optional parameters", 0, parser.getRequiredParameters().size());
    }
}
