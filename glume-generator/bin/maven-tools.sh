#!/usr/bin/env sh

usage() {
	echo "Usage: sh maven.sh [options]"
	echo ""
	echo "options:"
	echo "* -h | --help                      帮助信息"
	echo "* -c | --clean                     清空编译"
	echo "* -p | --package                   打包开发环境"
	echo "* -pd | --package-dev              打包开发环境"
	echo "* -P | --package-prod              打包生产环境"
	exit 1
}

clean() {
  echo "begin mvn clean "
  cd ..
  mvn clean
}

# 生产环境打包
packageProd() {
  echo "begin mvn clean package -Dmaven.test.skip=true -Pprod "
  cd ..
  mvn clean package -Dmaven.test.skip=true -Pprod
}

# 开发环境打包
packageDeb() {
  echo "begin mvn clean package -Dmaven.test.skip=true -Pdev "
  cd ..
  mvn clean package -Dmaven.test.skip=true -Pdev
}

if [ -z $1 ]; then
    usage
fi

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case "$1" in
        -h|--help)
            usage
            shift
            ;;
        -c|--clean)
            clean
            shift
            ;;
        -p|--package|-pd|--package-dev)
            packageDeb
            shift
            ;;
        -P|--package-prod)
            packageProd
            shift
            ;;
        *)
            exit 1
            ;;
    esac
done
