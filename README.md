# Sample project

This project consists of a simple gradle config and some example source files
done for the SEP WS19/20 at LMU Munich.

## Dependencies

The project requires Java 11.
It was only tested on Ubuntu 18.04 with openjdk 11.0.3 2019-04-16,
so no other guarantees can be made.

The project is built with `gradle`, version 5.6.4. The provided `gradlew` wrapper automatically downloads and uses
the correct gradle version.


## Building the Project

On Linux and Mac OS, run the following command from the project's root directory to compile the program,
run all checks and create an executable jar:

```
./gradlew build jar
```

On Windows, run the following command from the project's root directory to compile the program,
run all checks and create an executable jar:

```
./gradlew.bat build jar
```

If the command succeeds, the jar is found in `build/libs/sample.jar`.
This jar can be executed with `java -jar build/libs/sample.jar`


## Running the Program

To run the program during development without any checks, run `./gradlew run` .
