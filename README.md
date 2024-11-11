# Cowsay CLI Demo with JeKa

This repository showcases [JeKa's](https://jeka.dev) ability to run Java applications directly from source code in a remote Git repository. 
Java sources can run as JVM or native executables, either on the host or within a Docker container.

The demo app is a [Java port](https://github.com/ricksbrown/cowsay/tree/master) of [Cowsay](https://en.wikipedia.org/wiki/Cowsay), built with JeKa.

The project includes [Java sources](src/main/java), [resources](src/main/resources), [library dependencies](dependencies.txt), and [build configuration](jeka.properties).

## Prerequisites 

- JeKa must be installed on your machine ([Installation guide](https://jeka-dev.github.io/jeka/installation/))
- Optionally, a Docker client(such as Docker Desktop) should be installed to create and run Docker images.

No JDK, JRE, or GraalVM is required, as JeKa will download the appropriate ones for you.

## Execute from Anywhere

You don't need to clone the repository yourself. Simply execute the following command:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p Hello JeKa
```

The command should display:
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
Command line explanation:
- `-r` stands for the 'remote' option, meaning that a Git repository URL is expected as the next argument.
- `https://github.com/jeka-dev/demo-cowsay` is the Git repository containing the Java application to build/run.
- `-p` stands for the 'program' option, indicating that the application will be run, and the following
  arguments will be passed to the application as program arguments.
- `Hello JeKa` are arguments passed to the Java application. In this case, we want the cow to say *Hello JeKa*.

Behind the scenes, the above command does the following:
- Clones the repository to *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay*.
- Downloads the appropriate JDK (if needed).
- Downloads the correct JeKa version (if needed).
- Silently builds the fat jar.
- Executes the application with the specified program arguments.

On subsequent runs, the command executes faster as only the last step is performed.



## Execute a Specific Version of the Application

You can specify a particular tag for cloning the application using the hash notation like this:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay#0.0.2 -p "Hello JeKa"
```

This clones the repo from tag *0.0.2* in *[USER HOME]/.jeka/cache/git/github.com_jeka-dev_demo-cowsay#0.0.2*.

## Use Shortcuts

To avoid retyping Git url of the application, you can leverage of the global substitution mechanism,
by adding `jeka.cmd.cowsay=-r https://github.com/jeka-dev/demo-cowsay` to the *[USER HOME]/.jeka/global.properties* file.

> **Note:** You can edit this file by executing `jeka admin: editGlobalProps`

Now, you can just execute :
```shell
jeka ::cowsay -p "Hello JeKa"
```

You can also combine with substitutions defined in the [jeka.properties file](jeka.properties).

```shell
jeka ::cowsay -p ::hi
```
## Create JVM Docker Images

You can directly create an efficient Docker image of this Java application by executing :
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: build
```
or
```shell
jeka ::cosway docker: build
```
This requires a Docker client installed on the host.

Then you can run the image by executing:
```shell
docker run --rm demo-cowsay:latest "Hello Docker"
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

## Create Native Images

JeKa can also compile an application to a native executable without any specific requirements; it will download GraalVM from the internet if it's missing.

To create a native executable, run:

```shell
jeka -r https://github.com/jeka-dev/demo-cowsay native: compile 
```
This creates a native executable under the jeka-output directory.

Since Jeka detects the presence of the executable, the following command will automatically run the executable instead of the Java JAR:
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay -p -f dragon "Hello Native"
```
The application now runs in a few milliseconds.

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

We can also create a Docker image running the native executable:
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay docker: buildNative
```
By default, this creates an image based on Ubuntu with dynamically linked libc.

We can create a smaller, distroless image using the following command:
```shell
jeka -r https://github.com/jeka-dev/demo-cowsay native: staticLink=MUSL docker: buildNative nativeBaseImage=gcr.io/distroless/static-debian12:nonroot 
```

To execute the Docker native image, run :
```shell
docker run --rm native-demo-cowsay:latest "Hello native docker"
```

## Command line help and documentation

Commands documentations are available from the command line: use `jeka --help` or `jeka xxx: --doc` 
to get a comprehensive list of available commands and options.

List the available KBeans (sets of commands/options):
```shell
jeka --doc
```

List the available commands and options provided by *docker* KBean:
```shell
jeka docker: --doc
```

List the available commands and options provided by *native* KBean:
```shell
jeka native: --doc
```

