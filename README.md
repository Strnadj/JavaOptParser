# Java Option Parser

[![Coverage Status](https://coveralls.io/repos/Strnadj/JavaOptParser/badge.png?branch=master)](https://coveralls.io/r/Strnadj/JavaOptParser?branch=master)
[![Build Status](https://travis-ci.org/Strnadj/JavaOptParser.png?branch=master)](https://travis-ci.org/Strnadj/JavaOptParser)


## Info

Java Simple Option parser is inspired from ruby OptParser class
([rdoc/OptionParser](http://ruby-doc.org/stdlib-2.0.0/libdoc/optparse/rdoc/OptionParser.html)].

## Installation

Add java source files in to your project and import them in files in
which you want to use them.

## Usage

You can find usage in tests or in example bellow. JavaOpt class provides
basic *fluent interfaces* for creating option definitions.

### Difference between Option and Path/Expression

* *Option* - classic option (ex: -v, --verbose)
* *Path/Expression* - string or path after option fields

```cli
cd *path/expression*
cd ~/strnadj/Projects
cp *option* *path/expression* *path/expression*
cp -R ~/strnadj/Projects ~/strnadj/Projects_bacup/
```

## Example

We want basic *ls* command with options:

* Only files
* Only directories
* With hidden files
* Help
* And optional path

### Create parser definition

```java
OptParser options = OptParser.createOptionParser("ls", "Show directory
contents")
    .addOption('f', "files", OptParser.OPTIONAL, "", "Just files")
    .addOption('d', "directories", OptParser.OPTIONAL, "", "Just
directories")
    .addOption('h', "help", OptParser.OPTIONAL, "", "Show this help")
    .addPathOrExpression("path", OptParser.OPTIONAL, ".", "Directory to
listing");
```

### Create option with required value?

```java
.addOptionRequiredValue('t', "target", OptParser.OPTIONAL, null, "Target
folder")
```


### Get help?

```java
System.out.println(options.getHelp());
```


```bash
Usage: ls [options] "path"

Optional options:
	-f, --files         Just files
	-d, --directories   Just directories	
	-h, --help          Show this help
```

### Parse parameters

```java
try {
    options.parseArguments(args);
} catch(Exception e) {
    System.err.println(e.getMessage());
    System.exit(-1);
}
```

### Getting parameters

```java

// Get if parameter was set
if (options.getOption("help") != null) {
    // Parameter help was set
}

// Values?
if (options.getOption("directories") != null) {
    options.getOption("directories").value();
}

```

## How to contribute?

1. Fork it!
2. Do your changes!
3. Create pull-request and open issue!


Thanks Strnadj :)


