@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag3" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag3 data\Mag3.txt localhost 1099"
