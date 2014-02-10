package com.strnadj.OptParser;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import com.strnadj.OptParser.exceptions.*;

/**
 * Generall option parser for definition and parsing command line arguments!
 * 
 * @author strnadj <jan.strnadek@gmail.com>
 * 
 * You can specify variable options, for presents or for boolean and default values!
 * equal inputs are: -v,--verbose
 * Inputs and factory method returns itself for fluent interfaces using it is recommended.
 * Inspired by ruby-stdlib opt parser class
 * (http://ruby-doc.org/stdlib-2.0.0/libdoc/optparse/rdoc/OptionParser.html)
 */
public class OptParser {
	/** Store options definitions in list. */
	private Set<Option> optionsContainer = new TreeSet<Option>();
	
	/** Store object map for quicker searching (after parsing arguments!). */
	private Map<String, Option> optionsValues = new HashMap<String, Option>();
	
	/** Private data for string help methods etc.. */
	private int maxFullNameLength  = 0;
	
	/** Private variable for save option (path|expr) order. */
	private int expressionOrder = 0;
	
	/** Help output optional string. */
	private String exprHelpStringOptional = "";
	
	/** Help output required string. */
	private String exprHelpStringRequired = "";
	
	/** List of required options in order row. */
	private List<Option> exprRequiredOrder = new LinkedList<Option>();
	
	/** List of optional options in order row. */
	private List<Option> exprOptionalOrder = new LinkedList<Option>();
	
	/** Command name for help method. */
	private String commandName;
	
	/** Command description. */
	private String commandDescription;
	
	/** Constants for optional type. */
	public final static int OPTIONAL = 1;
	
	/** Constants for required type. */
	public final static int REQUIRED = 2;
	
	/** Option - no value. */
	public final static int OPTION_NO_VALUE = 1;
	
	/** Option - value is required. */
	public final static int OPTION_VALUE_IS_REQUIRED = 2;
	
	/** Debug mode. */
	public final static boolean DEBUG = false;

	/**
	 * Factory method returns instance of OptParser.
	 * 
	 * @param cmdName Command name
	 * @param cmdDesc Command description
	 * 
	 * @return Instance of option parser.
	 */
	public static OptParser createOptionParser(String cmdName, String cmdDesc) {
		return new OptParser(cmdName, cmdDesc);
	}
	
	
	/**
	 * Default constructor for option parser.
	 * 
	 * @param name Command name
	 * @param desc Command description
	 */
	public OptParser(String name, String desc) {
		this.commandName = name;
		this.commandDescription = desc;
	}
	
	/**
	 * Add option to option parser, return self for fluent interface.
	 * 
	 * @param shortName Shortcut
	 * @param fullName Full name
	 * @param type Optional or required?
	 * @param defaultValue Default value
	 * @param description Description
	 * 
	 * @return Option parser
	 */
	public OptParser addOption(char shortName, String fullName, int type, String defaultValue, String description) {
		optionsContainer.add(
			new Option(shortName, fullName, defaultValue, type, description)
		);
		
		// Count variables of lenght for output
		if (fullName.length() > maxFullNameLength) {
			maxFullNameLength = fullName.length();
		}
		
		return this;
	}
	
	/**
	 * Add option.
	 * 
	 * @param shortName Shortcut
	 * @param fullName Full name
	 * @param type Optional or required?
	 * @param defaultValue Default value
	 * @param description Description
	 * @param valueType Value type
	 * 
	 * @return Option parser
	 */
	public OptParser addOption(char shortName, String fullName, int type, String defaultValue, String description, int valueType) {
		optionsContainer.add(
				new Option(shortName, fullName, defaultValue, type, description, valueType)
			);
			
			// Count variables of lenght for output
			if (fullName.length() > maxFullNameLength) {
				maxFullNameLength = fullName.length();
			}
			
			return this;		
	}
	
	/**
	 * Can add path or expression for example ls [path] or cp [path1] [path2].
	 * 
	 * @param fullName Full name
	 * @param type Optional or required
	 * @param defaultValue Default value
	 * @param description Description
	 * 
	 * @return Option parser instance
	 */
	public OptParser addPathOrExpression(String fullName, int type, String defaultValue, String description) {
		Option o = new Option(fullName, type, defaultValue, expressionOrder++, description);
		optionsContainer.add(o);
		
		// Helper string?!
		if (type == OptParser.REQUIRED) {
			exprRequiredOrder.add(o);
			exprHelpStringRequired += "\"" + fullName + "\" ";
		} else {
			exprOptionalOrder.add(o);
			exprHelpStringOptional += "[" + fullName + "] ";
		}
		
		return this;
	}
	
	
	/**
	 * Return command help created from options.
	 * 
	 * @return Help string
	 */
	public String getHelp() {
		String ret = "";
		
		// name + description
		ret += String.format("Command: %s - %s\n", this.commandName, this.commandDescription);
		
		// Usage?!
		// Better formmating
		if (exprHelpStringRequired.length() > 0) {
			exprHelpStringRequired += " ";
		}
		ret += String.format("Usage: %s [options] %s%s \n\n", this.commandName, exprHelpStringRequired, exprHelpStringOptional);
		
		// Required parameters
		String required = "";
		
		// Optional parameters
		String optional = "";
		
		for (Option o : optionsContainer) {
			// Params
			if(o.POSITION != -1) {
				continue;
			}
			
			// Count necessary spaces for pretty output!
			// -(char), --(string){spaces} - 3 + 3 + fullLenght
			// {spaces} is variable maxFullNameLength + 4
			int spaces = (maxFullNameLength + 4) - o.getFullName().length();
			String freeSpaces = "";
			for (int i = 0; i < spaces; i++) {
				freeSpaces += " ";
			}
			
			// Prepare value is required?!
			String value = "";
			if (o.isValueRequired()) {
				value = "(Value is required!!)";
			}
			
			// Return
			if (o.getType() == OptParser.OPTIONAL) {
				optional += String.format("\t-%c, --%s%s%s %s\n", o.getShortName(), o.getFullName(), freeSpaces, o.getDescription(), value);
			} else {
				required += String.format("\t-%c, --%s%s%s %s\n", o.getShortName(), o.getFullName(), freeSpaces, o.getDescription(), value);
			}
		}
		
		// Merge with return string
		if (required.length() > 0) {
			ret += "Required options:\n"+required+"\n";
		}
		
		// Optional options
		if (optional.length() > 0) {
			ret += "Optional options:\n"+optional+"\n";
		}
		
		return ret;
	}

	/** Public method for parsing from collection of strings
	 * - String with parameters
	 * @param parameters String of parameters
	 * @throws Parsing exception
	 */
	public void parseArguments(String parameters) throws Exception {
		parseArguments(parameters.split(" "));
	}
	
	/** Public method for parsing from collection of strings
	 * - collection must be LIST, cause parameters must be ordered!
	 * @param parameters List of arguments
	 * @throws Parsing exception
	 */
	public void parseArguments(List<String>parameters) throws Exception {
		String [] arr = new String[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			arr[i] = parameters.get(i);
		}
		parseArguments(arr);
	}
	
	/** Parse arguments from command line
	 *  - work with array is much more faster!
	 *  @params parameters Parameters
	 *  @throws Parsing exception
	 */
	public void parseArguments(String [] parameters) throws Exception {
		/** Concatenate parameters - single and double quoted spaces etc */
		parameters = concatenateParameters(parameters);
		
		// Method is designed to throw exception when is catch undefined attribute!
		// Non-optional items -  fill with required parameters, if at the end
		// of parsing there is some items - its bad and required parameters are missing
		Set<String> nonOptionalItems = this.getRequiredParameters();
		// Every time when parse some required option, this option are removed from set
		
		// Get count of parameters
		int size = parameters.length;
	
		// Is there HELP option?!
		boolean help = false;
		for (int i = 0; i < size; i++) {
			String parameter = parameters[i];
			
			// Choiced help option - ignore warnings about missing properties!
			if (parameter.equals("-h") || parameter.equals("--help")) {
				help = true;
				
				if (DEBUG) {
					System.out.println("DEBUG: Help parameter found!");
				}
			}
		}
		
		// Debug
		if (DEBUG) {
			System.out.println("OptParser debug:\nDEBUG: Parameters "+Arrays.toString(parameters));
		}
		
		// Path or expressions on the end!
		List<String> poe = new LinkedList<String>();
		
		// Start parsing!
		for (int i = 0; i < size; i++) {
			// Always start with empty option! (for save on method end)
			Option o = null;
			
			// Get parameter
			String parameter = parameters[i];
			
			// What kind of parameter is it?!
			if (isOption(parameter)) {
				// Get option by parameter!
				o = getOptionByParameter(parameter);
				
				// Throw exception of undefined option!
				if (o == null) {
					if (DEBUG) {
						System.out.println(String.format("ERROR: Command: %s unexcepted option %s", this.commandName, parameter ));
					}
					
					throw new UnexpectedOption(String.format("Command: %s unexcepted option %s", this.commandName, parameter ));
				}
				
				// Debug
				if (DEBUG) {
					System.out.println("DEBUG: found parameter "+o.getFullName());
				}
				
				// Set filled! 
				o.setFilled();				
				
				// Required value?!
				if (o.isValueRequired()) {
					// Now we've got option, is there a value? on next index?!
					String nextParameter = null;
					if ((i+1) < size) {
						nextParameter = parameters[i+1];
					} 
					
					
					// Throw new exception when next string is null (not exist) or next string is option!!!
					if (nextParameter == null || isOption(nextParameter)) {
						if (DEBUG) {
							System.out.println(String.format("DEBUG: Command: %s - value: %c(%s) is required!", this.commandName, o.getShortName(), o.getFullName()));
						}
						
						// End with exception
						throw new MissingOptionValue(String.format("Command: %s - value: %c(%s) is required!", this.commandName, o.getShortName(), o.getFullName()));
					}
					
					// Set next parameter as value!
					o.setValue(nextParameter);

					if (DEBUG) {
						System.out.println("DEBUG: Set value: "+nextParameter+" to command: "+o.getFullName());
					}					
					
					// Skip next parameter! (it is value for this option)
					i = i + 1;
				}
				
				// Now if option is required remove from required list!
				if (o.isRequired()) {
					nonOptionalItems.remove(o.getFullName());
				}
			} else {
				// It is path or expr option! Parse different!!
				poe.add(parameter);
				
				if (DEBUG) {
					System.out.println("DEBUG: Add: '"+parameter+"' to path or expression");
				}
			}
			
			// If there is an option save it!
			if (o != null) {
				optionsValues.put(o.getFullName(), o);
			}
		}
		
		if (DEBUG) {
			System.out.println("DEBUG: Merge POE size:"+poe.size());
		}
		
		// *************************************** //
		// Parse path or expressions parameters!!! //
		// *************************************** //
		// First concatenate parameters! (path \/s, ' and " open)
		// when there is opened " or ' - throws OptParser exception
		Map<Integer, String> poeParsed = new HashMap<Integer, String>();
		int actPoeIndex = 0;
				
		// Iterate through paths or expressions
		for (int i = 0; i < poe.size(); i++) {
			String pom = poe.get(i).trim();

			// Skip when there is no string!
			if (pom.length() == 0) {
				continue;
			}
			
			if (DEBUG) {
				System.out.println("Opt parser find POE: "+i+" - "+pom);
			}
			
			// Get param
			String param;
			if (pom.charAt(0) == '\'' || pom.charAt(0) == '"') {
				param = pom.substring(1, pom.length() - 1);
			} else {
				param = pom;
			}
			
			if (DEBUG) {
				System.out.println("Find parameters: "+param);
			}
			
			// Set
			poeParsed.put(actPoeIndex, param);
			actPoeIndex++;
		}
		
		if (DEBUG) {
			System.out.println("DEBUG: Merge POE END");
		}
				
		
		// Set for items!
		int posRequired = 0;
		int posOptional = 0;
		
		// Try to assing values to options!
		for(int i = 0; i < actPoeIndex; i++) {
			String param = (String) poeParsed.get(i);
			// Try required first
			if (posRequired < exprRequiredOrder.size()) {
				// Get option
				Option o = exprRequiredOrder.get(posRequired);
				
				// Set value
				o.setValue(param);
				o.setFilled();
				
				// Add to option values!
				optionsValues.put(o.getFullName(), o);
				
				// Remove from required items!
				nonOptionalItems.remove(o.getFullName());
				
				// Move pos required
				posRequired++;
			} else if(posOptional < exprOptionalOrder.size()) {
				// Get option
				Option o = exprOptionalOrder.get(posOptional);
				
				// Set value
				o.setValue(param);
				o.setFilled();
				
				// Add to option values
				optionsValues.put(o.getFullName(), o);
				
				// Inc
				posOptional++;
			} else {
				// Unknown attribute!
				throw new UnknownAttribute(String.format("Unknown attribute: \"%s\" for command: %s\n", param, this.commandName));
			}
		}
		
		// There is some required parameters left!
		if (nonOptionalItems.size() > 0) {
			String options = "";
			for(String s : nonOptionalItems) {
				options = s+", ";
			}
			
			options = options.substring(0, options.length() - 2);
			
			// Throw exception
			if (help) {
				throw new MissingOptionsHelp(this.getHelp());
			} else {
				throw new MissingOptions(String.format("Missing options for command: %s - %s\n%s", this.commandName, options, this.getHelp()));
			}
		}
	}
	
	/**
	 * Concatenate parameters quotes etc.
	 * 
	 * @param arr Parameters array
	 * 
	 * @throws Exception Parenthesses overleaps
	 * 
	 * @return Concatened array
	 */
	private String[] concatenateParameters(String [] params) throws Exception {
		// List of new strings
		List<String> parameters = new LinkedList<String>();
		
		// Marks
		boolean singleQuotedOpen = false;
		boolean doubleQuotedOpen = false;
		
		
		String test = "";
		for(int i = 0; i < params.length; i++) {
			if (i == 0) {
				test = params[i];
			} else {
				test += " "+params[i];
			}
		}
		
		// Char by char and count \\
		int length = test.length();
		int escapedCount = 0;
		String token = "";
		
		for (int i = 0; i < length; i++) {
			char c = test.charAt(i);
			if (c == ' ' && escapedCount % 2 == 0 && !singleQuotedOpen && !doubleQuotedOpen && token.length() > 0) {
				if (!token.isEmpty()) {
					parameters.add(token);
				}
				escapedCount = 0;
				token = "";
			} else if(c == '\\') {
				escapedCount++;
				token += "\\";
			} else if(c == '\'' && escapedCount % 2 == 0) {
				if (singleQuotedOpen == true) {
					token += "'";
					if (!token.isEmpty()) {
						parameters.add(token);
					}
					escapedCount = 0;
					singleQuotedOpen = false;
					token = "";
				} else if(doubleQuotedOpen) {
					throw new OverlapingBracketsException("Quoted \" overleaping with '!");
				} else {
					singleQuotedOpen = true;
					if (!token.isEmpty()) {
						parameters.add(token);
					}
					escapedCount = 0;
					token = "'";
				}
			} else if(c == '"' && escapedCount % 2 == 0) {
				if (doubleQuotedOpen) {
					token += "\"";
					if (!token.isEmpty()) {
						parameters.add(token);
					}
					escapedCount = 0;
					doubleQuotedOpen = false;
					token = "";
				} else if(singleQuotedOpen) {
					throw new OverlapingBracketsException("Quote ' overleaping with \"!");
				} else {
					doubleQuotedOpen = true;
					if (!token.isEmpty()) {
						parameters.add(token);
					}
					escapedCount = 0;
					token = "\"";
				}
			} else {
				token += c+"";
				escapedCount = 0;
			}
		}
		
		if (token.length() > 0) {
			parameters.add(token);
		}
		if (singleQuotedOpen) {
			throw new OverlapingBracketsException("Single quoted bracket not closed!");
		}
		
		if (doubleQuotedOpen) {
			throw new OverlapingBracketsException("Double quoted bracket not closed!");
		}
		
		String [] arr = new String[parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			arr[i] = parameters.get(i);
		}
		
		return arr;
	}
	
	/**
	 * Return true when parametr string is an option!
	 * 
	 * @param parameter String parameter
	 * 
	 * @return True if it is option.
	 */
	private boolean isOption(String parameter) {
		return (parameter.startsWith("-") || parameter.startsWith("--"));
	}
	
	
	/**
	 * Return option by parameter.
	 * 
	 * @param parameter Parameter name
	 * 
	 * @return Option instance
	 */
	private Option getOptionByParameter(String parameter) {
		// Compare with what?!
		if (parameter.startsWith("--")) {
			// Remove -- from parameter name
			parameter = parameter.substring(2);
			
			// Full
			for (Option o : optionsContainer) {
				if (o.getFullName().equals(parameter)) return o;
			}
		} else {
			// Short name!
			char param = parameter.charAt(1);
			
			// Compare
			for(Option o : optionsContainer) {
				if (o.getShortName() == param) return o;
			}
		}
		
		// Nothing was found!
		return null;
	}
	
	/**
	 * Return true when option is filled!
	 * 
	 * @param optName Option name
	 * 
	 * @return True when option is filled
	 */
	public boolean isOptionFilled(String optName) {
		if (optionsValues.containsKey(optName)) {
			Option o = optionsValues.get(optName);
			return o.isFilled();
		} else {
			return false;
		}
	}
	
	/** 
	 * Get option via full name. 
	 * @param optName Option name
	 * @return Option instance or null
	 */
	public Option getOption(String optName) {
		if (optionsValues.containsKey(optName)) {
			Option o = optionsValues.get(optName);
			return o;
		} else {
			return null;
		}
	}
	
	/** 
	 * Return all required parameters in set.
	 *  
	 * @return Set of required parameters
	 */
	public Set<String> getRequiredParameters() {
		Set<String> ret = new TreeSet<String>();
		
		// Iterate parameters
		for (Option o : optionsContainer) {
			if (o.isRequired()) {
				ret.add(o.getFullName());
			}
		}
		
		// Iterate required parameters (paths or expressions)
		for (Option o : exprRequiredOrder) {
			if (o.isRequired()) {
				ret.add(o.getFullName());
			}
		}
		
		// return
		return ret;
	}
}
