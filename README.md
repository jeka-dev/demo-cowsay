# Cowsay demo for JeKa

Project forked from https://github.com/ricksbrown/cowsay/tree/master

## Prerequisite 

Install JeKa

## Execute from anywhere

Don't need to clone the repo by your own. Just execute :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p Hello JeKa
```

Form scratch, this will:
  - clone the repo
  - download proper JDK (If needed)
  - download proper JeKa version (If needed)
  - Build the fat Jar silently (cause of -q option)
  - Execute the build Jar with the specified program args

In the subsequent runs, this will be faster as it will execute Jar file directly.

## Native image (experimental)

Create a native native executable :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay nativeImg
```

Now, run again :
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon Hello My Friends
```
The native image will be run directly in few milliseconds.


## Use shorthand defined in jeka.properties

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p ::hi
```