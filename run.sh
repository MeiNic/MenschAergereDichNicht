#!/bin/bash
# A small utility for running the game while developing. This script
# ensures that the program is recompiled comletely by removing any
# .class files before starting javac.
printf "Cleaning up... "
rm *.class
echo "done."

printf "Compiling... "
javac Main.java
echo "done."

echo "Starting game."
java Main
