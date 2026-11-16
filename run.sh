#!/bin/bash
set -e

# Discover Java compiler and runtime
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
    JAVAC_BIN="$JAVA_HOME/bin/javac"
    JAVA_BIN="$JAVA_HOME/bin/java"
elif [ -x "/Library/Java/JavaVirtualMachines/jdk-27.jdk/Contents/Home/bin/javac" ]; then
    JAVAC_BIN="/Library/Java/JavaVirtualMachines/jdk-27.jdk/Contents/Home/bin/javac"
    JAVA_BIN="/Library/Java/JavaVirtualMachines/jdk-27.jdk/Contents/Home/bin/java"
elif command -v javac >/dev/null 2>&1; then
    JAVAC_BIN="javac"
    JAVA_BIN="java"
else
    echo "Error: No Java Development Kit (JDK) found."
    exit 1
fi

BIN_DIR="bin"
SRC_DIR="src/main/java"

mkdir -p "$BIN_DIR"

echo "Compiling Java sources..."
find "$SRC_DIR" -name "*.java" > sources.txt
"$JAVAC_BIN" -d "$BIN_DIR" @sources.txt
rm sources.txt
echo "Compilation successful!"

echo "Running application with arguments: $@"
"$JAVA_BIN" -cp "$BIN_DIR" com.pathfinding.Main "$@"
