#!/bin/bash
set -e

# 查看mysql服务的状态，方便调试，这条语句可以删除
echo `service mysql status`

echo '1.启动mysql....'
# 启动mysql
service mysql start
sleep 3

echo `service mysql status`

# 建库、建表
echo '2.建库、建表....'
mysql < /db/mysql.sql
sleep 3
echo '2-1.建库、建表完毕....'

# sleep 3
echo `service mysql status`
echo `mysql容器启动完毕,且数据导入成功`

tail -f /dev/null
