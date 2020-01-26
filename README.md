# Reversi game project

This project consists of a early version of a reversi game and a simple gradle config for the SEP WS19/20 at LMU Munich.

## Dependencies

The project requires Java 11.
It was tested on Windows 10 version 1903 and Linux Mint 19.3 Cinnamon.

The project is built with `gradle`, version 5.6.4. The provided `gradlew` wrapper automatically downloads and uses
the correct gradle version.

## Opening the Project

The Project's jar files can be opened with `java -jar releases/ReversiMain.jar` and 'java -jar releases/Server.jar'

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

If the command succeeds, the jar is found in 'build/libs/ReversiMain.jar' and 'build/libs/Server.jar'.
This jar can be executed with `java -jar build/libs/ReversiMain.jar` and 'build/libs/Server.jar'.


## Running the Program

To run the program during development without any checks, run `./gradlew run` .
