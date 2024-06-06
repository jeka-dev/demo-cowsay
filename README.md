# Cowsay demo for JeKa

This repo demonstrates the capability of Jeka for executing Java applications from source code, hosted 
in a remote repository.

The application is a [cowsay](https://en.wikipedia.org/wiki/Cowsay) Java port, build with JeKa. 

The project is forked from https://github.com/ricksbrown/cowsay/tree/master
and consists in many Java source and resources files.

## Prerequisite 

Install JeKa.
No need to have any JDK installed on your machine, JeKa manages it for you.

## Execute from anywhere

You don't need to clone the repo by your own. Just execute the following command :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p Hello JeKa
```

Behind the scene, this :
  - Clones the repo in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay*
  - Downloads proper JDK (If needed)
  - Downloads proper JeKa version (If needed)
  - Builds the fat Jar silently (cause of --quiet option mentioned in [jeka.properties file](jeka.properties))
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

## Execute a specific version of the application

You can specify a particular tag for cloning the application using the hash notation like this: : 

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay#0.0.2 -p Hello JeKa
```
This Clones the repo from tag *0.0.1* in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay#0.0.1*

## Execute in Docker
You need to have a Docker client running on the host machine (Docker Desktop).

First, build the Docker image
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: build
```

Then run the image
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: run programArgs="Hello Docker" --quiet
```

```
 ____________
< Hello Docker >
 ------------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||
```

## Native image (experimental)

Create an executable native image :

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay native: build 
```

Now, run again :
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon Hello Native
```
The native image will be run directly in few milliseconds.
```
 _____________
< Hello Native >
 -------------
      \                    / \  //\
       \    |\___/|      /   \//  \\
            /0  0  \__  /    //  | \ \
           /     /  \/_/    //   |  \  \
           @_^_@'/   \/_   //    |   \   \
           //_^_/     \/_ //     |    \    \
        ( //) |        \///      |     \     \
      ( / /) _|_ /   )  //       |      \     _\
    ( // /) '/,_ _ _/  ( ; -.    |    _ _\.-~        .-~~~^-.
  (( / / )) ,-{        _      `-.|.-~-.           .~         `.
 (( // / ))  '/\      /                 ~-. _ .-~      .-~^-.  \
 (( /// ))      `.   {            }                   /      \  \
  (( / ))     .----~-.\        \-'                 .~         \  `. \^-.
             ///.----..>        \             _ -~             `.  ^-`  ^-_
               ///-._ _ _ _ _ _ _}^ - - - - ~                     ~-- ,.-~
                                                                  /.-~
```

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
