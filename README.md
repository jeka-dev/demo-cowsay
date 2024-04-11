# Cowsay demo for JeKa

Project forked from https://github.com/ricksbrown/cowsay/tree/master

This repo demonstrates the capability of Jeka for executing Java applications from source code, hosted 
in a remote repository.

The application is a *cowsay* Java port, build with JeKa. 
It consists in many Java source and resources files.

## Prerequisite 

Install JeKa.
No need to have any JDK installed on your machine, JeKa manages it for you.

## Execute from anywhere

Don't need to clone the repo by your own. Just execute from a terminal :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p Hello JeKa
```

Form scratch, this will:
  - clone the repo
  - download proper JDK (If needed)
  - download proper JeKa version (If needed)
  - Build the fat Jar silently (cause of -q option)
  - Execute the build Jar with the specified program args

On the subsequent runs, this will run faster as it will execute the Jar file directly.

```
 ____________
< Hello JeKa >
 ------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
```

## Execute in Docker image
You need to have a Docker client running on the host machine (Docker Desktop).

First, build the Docker image
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: build
```

Then run the image
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: run programArgs="Hello Docker" --quiet
```

## Native image (experimental)

Create a native native executable :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay nativeImg 
```

Now, run again :
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon Hello Java-Native
```
The native image will be run directly in few milliseconds.


## Use shorthand defined in jeka.properties

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p ::hi
```