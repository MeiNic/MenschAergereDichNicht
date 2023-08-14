#!/bin/bash
# A small utility for running the game while developing. This script
# ensures that the program is recompiled completely by removing any
# .class files before starting javac.
cd src/

printf "Cleaning up... "
rm *.class
echo "done."

printf "Compiling... "
javac Main.java
echo "done."

echo "Starting game."
java Main

cd ../
