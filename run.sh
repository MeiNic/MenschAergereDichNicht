#!/usr/bin/env bash
# A small utility for running the game while developing. This script
# ensures that the program is recompiled completely by removing any
# .class files before starting javac.
printf "Cleaning up... "
rm src/*.class
echo "done."

printf "Compiling... "
javac src/Main.java
echo "done."

echo "Starting game."
java src.Main
