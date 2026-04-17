@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
java -cp bin rmi.StoreClient localhost 1099
