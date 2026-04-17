@echo off
setlocal
set PORT=1099
start "RMI Registry" /min rmiregistry %PORT%
echo RMI Registry started on port %PORT%
