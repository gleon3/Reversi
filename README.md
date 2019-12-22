# Reversi game project

This project consists of a early version of a reversi game and a simple gradle config for the SEP WS19/20 at LMU Munich.

## Dependencies

The project requires Java 11.
It was tested on Windows 10 version 1903 and Linux Mint 19.2 Cinnamon.

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

If the command succeeds, the jar is found in `build/libs/Zwischenabgabe1.jar`.
This jar can be executed with `java -jar build/libs/Zwischenabgabe1.jar`


## Running the Program

To run the program during development without any checks, run `./gradlew run` .
