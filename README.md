# Cowsay Demo for JeKa

This repository demonstrates the capability of JeKa for executing Java applications from source code hosted in a remote repository.

The application is a [Cowsay](https://en.wikipedia.org/wiki/Cowsay) Java port, built with JeKa.

The project is forked from [this repository](https://github.com/ricksbrown/cowsay/tree/master) and consists of many Java source and resource files.

## Prerequisites 

Install JeKa.
No need to have any JDK installed on your machine; JeKa manages it for you.

### Install



## Execute from Anywhere without Installing

You don't need to clone the repo yourself. Just execute the following command:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p "Hello JeKa"
```

Behind the scenes, this:
  - Clones the repo in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay*
  - Downloads the proper JDK (if needed)
  - Downloads the proper JeKa version (if needed)
  - Builds the fat jar silently (due to the `--quiet` option mentioned in the [jeka.properties file](jeka.properties))
  - Executes the built jar with the specified program arguments (after the `-p` option).

On subsequent runs, this will execute faster as only the last step will be performed.

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

## Execute a Specific Version of the Application

You can specify a particular tag for cloning the application using the hash notation like this:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay#0.0.8 -p "Hello JeKa"
```

This clones the repo from tag *0.0.8* in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay#0.0.8*.

## Execute in Docker

You need to have a Docker client running on the host machine (e.g., Docker Desktop).

First, build the Docker image:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: build
```

Then run the image:

```shell
docker run --rm github.com_jeka-dev_demo-cowsay:latest "Hello Docker"
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

## Native Executable

Create a native executable:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay native: compile 
```

Now, run again:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon "Hello Native"
```

The native executable will run directly in a few milliseconds.

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

## Use Shorthand Defined in jeka.properties

We can use predefined program arguments using the interpolation defined in the [jeka.properties file](jeka.properties).

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p ::hi
```

## Use Shorthand Defined in global.properties

By adding `jeka.cmd.cowsay=-r https://github.com/jeka-dev/demo-cowsay -p` to the *[USER HOME]/.jeka/global.properties* file, we can use a simpler command:

```shell
jeka ::cowsay
```

> [!TIP]
> You can display which shorthands are defined by executing `jeka --doc`.
