@echo off
setlocal
cd /d %~dp0.. 
javac -d bin src\rmi\*.java
start "Mag2" cmd /k "cd /d %~dp0.. && java -cp bin rmi.StoreManager Mag2 data\Mag2.txt localhost 1099"
