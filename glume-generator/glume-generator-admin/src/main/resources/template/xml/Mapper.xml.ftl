<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.${moduleName}.dao.${ClassName}Dao">

    <resultMap id="${className}Result" type="${package}.${moduleName}.domain.entity.${ClassName}Entity">
        <#list fieldList as field>
        <result property="${field.attrName}" column="${field.fieldName}" />
        </#list>
    </resultMap>

</mapper>
