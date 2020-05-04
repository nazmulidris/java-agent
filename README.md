# java-instrumentation-agent

<!-- START doctoc generated TOC please keep comment here to allow auto update -->
<!-- DON'T EDIT THIS SECTION, INSTEAD RE-RUN doctoc TO UPDATE -->

- [Resources](#resources)
- [Running this on your machine](#running-this-on-your-machine)
- [Notes on building this project](#notes-on-building-this-project)
  - [What goes where](#what-goes-where)
  - [Building the agent using Maven](#building-the-agent-using-maven)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

This project shows how to use the Java instrumentation API to add some runtime monitoring to an already created and
running application using byte code generation.

## Resources

- [Guide to Java Instrumentation (lacking lots of details)](https://www.baeldung.com/java-instrumentation)
- [Repo of samples "core-java-jvm" on GitHub (lacking lots of details)](https://github.com/eugenp/tutorials/tree/master/core-java-modules/core-java-jvm)
- [Maven clean and install](https://stackoverflow.com/a/20122552/2085356)
- [Example of using maven to package an agent](https://dhruba.wordpress.com/2010/02/07/creation-dynamic-loading-and-instrumentation-with-javaagents/)
- [Example of using IDEA and maven to build an agent](https://stackify.com/what-are-java-agents-and-how-to-profile-with-them/)

## Running this on your machine

Open the project in IDEA, and then you will see the following run configurations.

1. `Main <no args>` - This simply starts the `application.Main` class and you will see usage instructions printed
   to the console.
2. `Main start 10000 10 1000` - This actually starts the main application which does the following:
   - Makes an ATM withdrawal for `10` units.
   - Sleeps for `10_000` ms (this is your chance to attach the agent to see it instrument the ATM).
   - Makes a 2nd ATM withdrawal for `1000` units.
3. `Main loadagent` - This actually starts the main application and tells it to load the agent, which will attach
   to an already existing running VM that has executed `start` (above).

So, to play with it, make sure that you build the entire project first. There's a maven task that runs everytime
the project is rebuilt called `install`. You can see this in the Maven tool window.

1. The first thing you can do is run the `Main <no args>` run configuration just to see the command line usage of the
   `application.Main` class.
2. Then you can run the `Main start...` configuration and it will perform one ATM
   withdrawal and wait for 10 sec.
3. Then you can run the `Main loadagent` configuration and it will attach to the VM started above, and will instrument
   the ATM code by adding some byte code generated using [`javassist`](https://www.javassist.org/). You can see the
   output of this byte code injection into your ATM class and it will dump some instrumentation output to the console.

## Notes on building this project

### What goes where

This project is really broken out into two main parts.

1. `application` package - this is the code for the application, the ATM, and the agent loader. The instrumentation code and byte code generation does not exist in the `application` package. You can run that
   indepedently of the instrumentation code, by simply running `App#main()`.
2. `agent` package - the code here is used to generate bytecode and use the instrumentation API. This code is what is
   built by Maven along w/ the `MANIFEST.MF` file into a JAR file (`target/java-instrumentation-agent-0.1.0-SNAPSHOT.jar`).
   This JAR file is loaded by the `application.AgentLoader` into the VM that is running the `Main start...` run
   configuration (the `application` package).

### Building the agent using Maven

There is a `MANIFEST.MF` file that is in the `resources/META-INF` folder which is used by Maven to generate the
agent JAR file. In IDEA, in the Maven tool window, you will find that the `install` task is run after a full rebuild
of the project in IDEA. Here's the code from the `pom.xml` that does this.

```xml
<build>
  <plugins>
    <!-- Agent manifest. -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <version>${maven.jar.plugin.version}</version>
      <configuration>
        <archive>
          <manifestFile>src/main/resources/META-INF/MANIFEST.MF</manifestFile>
        </archive>
      </configuration>
    </plugin>
   </plugins>
</build>
```

Also, it was useful to use the following command to debug the maven `install` task from the command line, which
generates some really helpful debug messages during compilation and packaging the agent JAR file.

```shell script
mvn clean install -X
```
