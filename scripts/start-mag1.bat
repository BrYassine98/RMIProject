@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag1" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag1 data\Mag1.txt localhost 1099"
