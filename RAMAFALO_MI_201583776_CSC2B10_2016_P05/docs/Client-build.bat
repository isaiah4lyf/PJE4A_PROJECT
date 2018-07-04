@echo off
cls
echo Setup JDK bin path before this script
echo Build script set to run in Project folder like eclipse
cd ..
set PRAC_BIN=./bin
set PRAC_DOCS=./docs
set PRAC_LIB=./lib/*
set PRAC_SRC=./src
echo *** Compiling ***
javac -sourcepath %PRAC_SRC% -cp "%PRAC_BIN%;%PRAC_LIB%" -d %PRAC_BIN% %PRAC_SRC%/ClientMain.java
echo *** Building JavaDoc ***

IF %ERRORLEVEL% GTR 0 GOTO ERROR
echo *** Running application ***
echo *** Running application ***
java -cp %PRAC_BIN%;%PRAC_LIB% ClientMain
IF %ERRORLEVEL% GTR 0 GOTO ERROR
GOTO END
:ERROR
echo !!! An error has occured !!!
echo Error number is %ERRORLEVEL%
:END
echo *** Completed ***
cd %PRAC_DOCS%
pause
