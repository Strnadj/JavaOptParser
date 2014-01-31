package com.strnadj.OptParser;

/** 
 * Class for storing objects and values 
 * - contains shortcut, full, options and default values.
 * 
 * @author strnadj
 */
public class Option implements Comparable<Option> {
	/** Type - default is required. */
	private int type = OptParser.REQUIRED;
	
	/** Short option name. */
	private char shortName = 'a';
	
	/** Full option name. */
	private String fullName = "";
	
	/** Default value string. */
	private String defaultValue = "";
	
	/** Position in option list. */
	public final int POSITION;
	
	/** String description. */
	private String description;
	
	/** Is value required?! (no value) */
	private int requiredValue = OptParser.OPTION_NO_VALUE;
	
	/** Filled?! */
	private boolean filled = false;
	
	/** Value. */
	private String value = "";
	
	/**
	 * Default option with specification of required values.
	 * 
	 * @param shortName Shortcut
	 * @param fullName Full name 
	 * @param defaultValue Default value
	 * @param type Option type
	 * @param description Description
	 * @param requiredValue Required value?
	 */
	public Option(final char shortName, final String fullName, final String defaultValue, final int type, final String description, final int requiredValue) {
		this.shortName 	   = shortName;
		this.fullName  	   = fullName;
		this.defaultValue  = defaultValue;
		this.type          = type;
		this.POSITION 	   = -1;
		this.description   = description;
		this.requiredValue = requiredValue;
	}
	
	/**
	 * Default OPTIONAL option constructor.
	 * 
	 * @param shortName Shortcut
	 * @param fullName Full name 
	 * @param defaultValue Default value
	 * @param type Option type
	 * @param description Description
	 */
	public Option(char shortName, String fullName, String defaultValue, int type, String description) {
		this(shortName, fullName, defaultValue, type, description, OptParser.OPTION_NO_VALUE);
	}
	
	/**
	 * Path or expression option.
	 * 
	 * @param fullName Full name
	 * @param type Option type 
	 * @param defaultValue Default value
	 * @param position Position
	 * @param description Description
	 */
	public Option(String fullName, int type, String defaultValue, int position, String description) {
		this.fullName = fullName;
		this.defaultValue = defaultValue;
		this.POSITION = position;
		this.description  = description;
		this.type = type;
	}
	
	/**
	 * Is value required?
	 * 
	 * @return True if its
	 */
	public boolean isValueRequired() {
		return this.requiredValue == OptParser.OPTION_VALUE_IS_REQUIRED;
	}
	
	/** 
	 * Is option filled? 
	 * @return True if its
	 */
	public boolean isFilled() {
		return this.filled;
	}
	
	/**
	 * Set option as filled.
	 */
	public void setFilled() {
		this.filled = true;
	}
	
	/**
	 * Return value (if is not filled return default value!)
	 * 
	 * @return Value or default value
	 */
	public String value() {
		if (!isFilled()) {
			return this.defaultValue;
		}
		return this.value;
	}
	
	/** 
	 * Return actual value (always return value no default!).
	 * 
	 * @return String of actual value.
	 */
	public String getValue() {
		return value;
	}

	/** 
	 * Set new value. 
	 * @param value New value 
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Get type of option.
	 * @return Option type
	 */
	public int getType() {
		return this.type;
	}
	
	/**
	 * Return option shortcut.
	 * @return Option shortcut
	 */
	public char getShortName() {
		return this.shortName;
	}
	
	/**
	 * Return full name of option.
	 * @return full name of option
	 */
	public String getFullName() {
		return this.fullName;
	}

	/**
	 * Get description.
	 * 
	 * @return Option description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Is option required?
	 * @return True if its
	 */
	public boolean isRequired() {
		return this.type == OptParser.REQUIRED;
	}
	
	/** 
	 * Compare options for uniqueness in set.
	 * 
	 * @return True if options are same 
	 */
	public boolean equals(Option aOption) {
		if (this.shortName == aOption.getShortName() || this.fullName == aOption.getFullName()) {
			return true;
		}
		return false;
	}

	/** 
	 * Compare options.
	 * 
	 * @return Messure of comparing 
	 */
	public int compareTo(Option o) {
		// First required than optional, first short name
		if (this.type != o.getType()) {
			if(this.type == OptParser.REQUIRED) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.shortName != o.getShortName()) {
			return (int) this.shortName - (int) o.getShortName();
		} else {
			return this.fullName.compareTo(o.getFullName());
		}
	}	
}