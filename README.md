# Cowsay demo for JeKa

This repo demonstrates the capability of Jeka for executing Java applications from source code, hosted 
in a remote repository.

The application is a *cowsay* Java port, build with JeKa. 

It is forked from https://github.com/ricksbrown/cowsay/tree/master
and consists in many Java source and resources files.

## Prerequisite 

Install JeKa.
No need to have any JDK installed on your machine, JeKa manages it for you.

## Execute from anywhere

Don't need to clone the repo by your own. Just execute from a terminal :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p Hello JeKa
```

Behind the scene, this :
  - Clones the repo
  - Downloads proper JDK (If needed)
  - Downloads proper JeKa version (If needed)
  - Builds the fat Jar silently (cause of -q option)
  - Executes the build Jar with the specified program arguments (next the '-p' option).

On the subsequent runs, this will run faster as only the last step will be executed.

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
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon Hello Mates
```
The native image will be run directly in few milliseconds.


## Use shorthand defined in jeka.properties

We can use predefined program args using interpolation defined in [jeka.properties file](jeka.properties)

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p ::hi
```

## Use shorthand defined in global.properties

By adding `jeka.cmd.cowsay=-r https://github.com/jeka-dev/demo-cowsay -p`

to the *[USER HOME]/.jeka/global.properties* file, we can use a simpler typing :

```shell
jeka ::cowsay ::hi
```

> [!TIP]
> You can display which shorthands are defined, by executing `jeka : --help`
