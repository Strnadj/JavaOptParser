# Java Option Parser

[![Coverage Status](https://coveralls.io/repos/Strnadj/JavaOptParser/badge.png)](https://coveralls.io/r/Strnadj/JavaOptParser)
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

### Get help?

```java
System.out.println(options.getHelp());
```


```bash

```

