#!/bin/bash
set -e

# Discover Java compiler and runtime
if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
    JAVAC_BIN="$JAVA_HOME/bin/javac"
    JAVA_BIN="$JAVA_HOME/bin/java"
elif [ -x "/c/Program Files/Android/Android Studio/jbr/bin/javac.exe" ]; then
    JAVAC_BIN="/c/Program Files/Android/Android Studio/jbr/bin/javac.exe"
    JAVA_BIN="/c/Program Files/Android/Android Studio/jbr/bin/java.exe"
elif [ -x "C:/Program Files/Android/Android Studio/jbr/bin/javac.exe" ]; then
    JAVAC_BIN="C:/Program Files/Android/Android Studio/jbr/bin/javac.exe"
    JAVA_BIN="C:/Program Files/Android/Android Studio/jbr/bin/java.exe"
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

# If Maven is available, use target/classes if compiled, or compile via javac
BIN_DIR="target/classes"
SRC_DIR="src/main/java"

if [ ! -f "$BIN_DIR/com/pathfinding/Main.class" ]; then
    mkdir -p "$BIN_DIR"
    echo "Compiling Java sources..."
    # Use relative paths with quotes to handle spaces in directory names
    find "$SRC_DIR" -name "*.java" | sed 's/^/"/;s/$/"/' > sources.txt
    "$JAVAC_BIN" -d "$BIN_DIR" @sources.txt
    rm -f sources.txt
    echo "Compilation successful!"
fi

echo "Running application with arguments: $@"
"$JAVA_BIN" -cp "$BIN_DIR" com.pathfinding.Main "$@"
