# Java Option Parser

[![Build Status](https://travis-ci.org/Strnadj/JavaOptParser.png?branch=master)](https://travis-ci.org/Strnadj/JavaOptParser)
[![Coverage Status](https://coveralls.io/repos/Strnadj/JavaOptParser/badge.png?branch=master)](https://coveralls.io/r/Strnadj/JavaOptParser?branch=master)


## Info

Java Simple Option parser is inspired from ruby OptParser class
([rdoc/OptionParser](http://ruby-doc.org/stdlib-2.0.0/libdoc/optparse/rdoc/OptionParser.html)). This work has been created as part of Semester Work on University of West Bohemia ([zcu.cz](http://zcu.cz)).

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

## Licence (DWTFYWTPL)

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE, Version 2, December 1991

Copyright (C) 2014 Jan Strnadek <jan.strnadek@gmail.com>

Everyone is permitted to copy and distribute verbatim or modified
copies of this license document, and changing it is allowed as long
as the name is changed.

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

0. You just DO WHAT THE FUCK YOU WANT TO.

I renounce responsibility for any damage or whatever this library caused to who use it.

## How to contribute?

1. Fork it!
2. Do your changes!
3. Create pull-request and open issue!


Thanks Strnadj :)


