:: 页码更改为Unicode(UTF-8)
chcp 65001
@echo off
echo.
echo [信息] 使用Jar命令运行Web工程。
echo.

cd %~dp0
cd ../glume-generator-admin/target

set JAVA_OPTS=-Xms256m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m

set ENCODING=-Dfile.encoding=utf-8

java -jar %JAVA_OPTS% %ENCODING% glume-generator-admin.jar

cd bin
pause