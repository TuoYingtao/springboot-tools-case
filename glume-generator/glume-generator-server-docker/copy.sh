#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
echo "begin copy sql "
cp ../db/mysql.sql ./mysql/db

# copy jar
echo "begin copy glume-generator-admin "
cp ../glume-generator-admin/target/glume-generator-admin.jar ./glume/jar
