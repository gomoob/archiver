@echo off
set CLASSPATH="%~dp0/../conf/*"
set CLASSPATH="%~dp0/../lib/*"

java -cp %CLASSPATH% com.gomoob.archiver.Archiver %*
@echo on
