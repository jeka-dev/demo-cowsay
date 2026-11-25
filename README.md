# Cowsay Demo for JeKa

This repository shows how JeKa runs, installs and packages a Java application directly from its Git repository.
It demonstrates remote execution (`-r`, `-p`), Docker images, native executables, application installation and
command shorthands.

The application is a [Cowsay](https://en.wikipedia.org/wiki/Cowsay) Java port, forked from
[ricksbrown/cowsay](https://github.com/ricksbrown/cowsay/tree/master).

## Prerequisites

- [JeKa](https://jeka-dev.github.io/jeka/installation/), to run commands starting with `jeka`. Commands starting with `./jeka` don't need it.
- Git and network access.
- Docker, only to [build a Docker image](#build-a-docker-image).

You don't need a JDK: JeKa downloads it.

## Run It

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p "Hello JeKa"
```

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

On the first run, JeKa:

  - Clones the repo in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay*
  - Downloads the required JDK and JeKa versions, if they are missing
  - Builds the fat jar, without running the tests
  - Runs the jar with the arguments following `-p`

Next runs only execute the last step. Add `-u` to get the last commit and rebuild:

```shell
jeka -ru https://github.com/jeka-dev/demo-cowsay -p "Hello JeKa"
```

### Run a Specific Version

Add a tag name after `#`:

```shell
jeka -r "https://github.com/jeka-dev/demo-cowsay#0.0.10" -p "Hello JeKa"
```

Each tag has its own clone, here in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay#0.0.10*.

### Run from a Local Clone

```shell
git clone https://github.com/jeka-dev/demo-cowsay
cd demo-cowsay
./jeka -p "Hello JeKa"
```

Run the tests and create the jar in *jeka-output*:

```shell
./jeka project: test pack
```

## Build a Docker Image

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: build
```

Then run it:

```shell
docker run --rm github.com_jeka-dev_demo-cowsay:latest "Hello Docker"
```

## Build a Native Executable

This takes a few minutes. JeKa downloads GraalVM if needed.

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay native: compile
```

Now the same run command uses the native executable, which starts in a few milliseconds:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon "Hello Native"
```

```
 ______________
< Hello Native >
 --------------
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

## Install as a Command

`cowsay@demo` refers to this repository in the JeKa *demo* catalog. JeKa asks which version to install.

```shell
jeka app: install repo=cowsay@demo
```

Then, run:

```shell
cowsay "Hello JeKa"
```

For a faster startup, install the native version instead. Remove the previous one first:

```shell
jeka app: uninstall name=cowsay
jeka app: install repo=cowsay@demo runtime=NATIVE
```

## Use Shorthands

A shorthand `jeka.cmd.xxx` replaces `::xxx` in the command line. It is read from the current directory or from the global
properties, not from the remote repository.

From a local clone, use `hi`, defined in [jeka.properties](jeka.properties):

```shell
./jeka -p ::hi
```

To run cowsay from any directory, add this line to *[USER HOME]/.jeka/global.properties*:

```properties
jeka.cmd.cowsay=-r https://github.com/jeka-dev/demo-cowsay -p
```

Then, execute:

```shell
jeka ::cowsay "Hello JeKa"
```

## How It Works

- [jeka.properties](jeka.properties): JeKa and Java versions, fat jar with its main class detected, resources included in the native executable, and shorthand.
- [jeka.project.deps](jeka.project.deps): compile and test dependencies.
- `jeka` and `jeka.ps1`: scripts that run JeKa without installing it.

## Learn More

- [Source-runnable applications](https://jeka-dev.github.io/jeka/tutorials/source-runnable-apps/)
- [App](https://jeka-dev.github.io/jeka/reference/kbeans-app/), [Docker](https://jeka-dev.github.io/jeka/reference/kbeans-docker/) and [Native](https://jeka-dev.github.io/jeka/reference/kbeans-native/) KBeans
- [Properties](https://jeka-dev.github.io/jeka/reference/properties/)
