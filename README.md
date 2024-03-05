# Cowsay demo for JeKa

Project forked from https://github.com/ricksbrown/cowsay/tree/master

## Prerequisite 

Install JeKa

## Execute from github source

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay project: runJar run.programArgs="Hello JeKa" -q
```

Form scratch, this will
  - download proper JDK (If needed)
  - download proper JeKa version (If needed)
  - Build the fat Jar silently (cause of -q option)
  - Execute the build Jar with the specified program args

In the subsequent runs, this will be faster as it will execute Jar file directly.

## Use shorthand defined in jeka.properties

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay project: ::hi -q
```