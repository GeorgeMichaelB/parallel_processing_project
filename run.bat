@echo off
setlocal

:: Set JAVA_HOME if not already set
if "%JAVA_HOME%"=="" (
    if exist "C:\Program Files\Android\Android Studio\jbr\bin\java.exe" (
        set "JAVA_HOME=C:\Program Files\Android\Android Studio\jbr"
    )
)

if not "%JAVA_HOME%"=="" (
    set "JAVA_BIN=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_BIN=java"
)

if exist "C:\tools\apache-maven-3.9.9\bin\mvn.cmd" (
    set "MVN_CMD=C:\tools\apache-maven-3.9.9\bin\mvn.cmd"
) else (
    set "MVN_CMD=mvn"
)

:: Compile classes if not already compiled
if not exist "target\classes\com\pathfinding\Main.class" (
    echo Compiling project with Maven...
    call "%MVN_CMD%" compile -q
)

"%JAVA_BIN%" -cp "target\classes" com.pathfinding.Main %*
