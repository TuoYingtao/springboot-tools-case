:: 页码更改为Unicode(UTF-8)
chcp 65001
@echo off
echo.
echo [信息] 打包Web工程，生成war/jar包文件。
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true -Pprod

pause
