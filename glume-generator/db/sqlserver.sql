CREATE TABLE gen_datasource (
    id bigint NOT NULL IDENTITY(1,1),
    db_type varchar(200),
    conn_name varchar(200),
    conn_url varchar(500),
    username varchar(200),
    password varchar(200),
    create_time datetime,
    update_time datetime,
    primary key (id)
);


CREATE TABLE gen_field_type
(
    id           bigint NOT NULL IDENTITY(1,1),
    column_type  varchar(200),
    attr_type    varchar(200),
    package_name varchar(200),
    create_time  datetime,
    update_time  datetime,
    primary key (id)
);
CREATE UNIQUE INDEX gen_column_type on gen_field_type(column_type);


CREATE TABLE gen_base_class
(
    id           bigint NOT NULL IDENTITY(1,1),
    package_name varchar(200),
    code         varchar(200),
    fields       varchar(500),
    remark       varchar(200),
    create_time  datetime,
    update_time  datetime,
    primary key (id)
);


CREATE TABLE gen_table
(
    id             bigint NOT NULL IDENTITY(1,1),
    table_name     varchar(200),
    class_name     varchar(200),
    table_comment  varchar(200),
    author         varchar(200),
    email          varchar(200),
    package_name   varchar(200),
    version        varchar(200),
    generator_type int,
    backend_path   varchar(500),
    frontend_path  varchar(500),
    module_name    varchar(200),
    function_name  varchar(200),
    form_layout    int,
    datasource_id  bigint,
    baseclass_id   bigint,
    create_time    datetime,
    update_time    datetime,
    primary key (id)
);
CREATE UNIQUE INDEX gen_table_name on gen_table(table_name);


CREATE TABLE gen_table_field
(
    id              bigint NOT NULL IDENTITY(1,1),
    table_id        bigint,
    field_name      varchar(200),
    field_type      varchar(200),
    field_comment   varchar(200),
    attr_name       varchar(200),
    attr_type       varchar(200),
    package_name    varchar(200),
    sort            int,
    auto_fill       varchar(20),
    date_fill       varchar(20),
    date_format     varchar(20),
    time_zone       varchar(20),
    primary_pk      int,
    base_field      int,
    form_item       int,
    form_required   int,
    form_type       varchar(200),
    form_dict       varchar(200),
    form_validator  varchar(200),
    grid_item       int,
    grid_sort       int,
    query_item      int,
    query_type      varchar(200),
    query_form_type varchar(200),
    create_time     datetime,
    update_time     datetime,
    primary key (id)
);


CREATE TABLE gen_project_modify
(
    id                     bigint NOT NULL IDENTITY(1,1),
    project_name           varchar(100),
    project_code           varchar(100),
    project_package        varchar(100),
    project_path           varchar(200),
    modify_project_name    varchar(100),
    modify_project_code    varchar(100),
    modify_project_package varchar(100),
    exclusions             varchar(200),
    modify_suffix          varchar(200),
    modify_tmp_path        varchar(100),
    create_time            datetime,
    update_time            datetime,
    PRIMARY KEY (id)
);

INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('datetime', 'Date', 'java.util.Date', getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('date', 'Date', 'java.util.Date', getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('tinyint', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('smallint', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('mediumint', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('int', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('integer', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('bigint', 'Long', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('float', 'Float', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('double', 'Double', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('decimal', 'BigDecimal', 'java.math.BigDecimal', getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('bit', 'Boolean', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('char', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('varchar', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('tinytext', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('text', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('mediumtext', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('longtext', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('timestamp', 'Date', 'java.util.Date', getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('NUMBER', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('BINARY_INTEGER', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('BINARY_FLOAT', 'Float', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('BINARY_DOUBLE', 'Double', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('VARCHAR2', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('NVARCHAR', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('NVARCHAR2', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('CLOB', 'String', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('int8', 'Long', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('int4', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('int2', 'Integer', NULL, getdate(), getdate());
INSERT INTO gen_field_type (column_type, attr_type, package_name, create_time, update_time) VALUES ('numeric', 'BigDecimal', 'java.math.BigDecimal', getdate(), getdate());

INSERT INTO gen_base_class (package_name, code, fields, remark, create_time, update_time) VALUES ('com.glume.generator.service.base.entity', 'BaseEntity', 'id,creator,create_time,updater,update_time,version,deleted', '使用该基类，则需要表里有这些字段', getdate(), getdate());

INSERT INTO gen_project_modify (project_name, project_code, project_package, project_path, modify_project_name, modify_project_code, modify_project_package, exclusions, modify_suffix, create_time, update_time) VALUES ('glume-boot', 'glume', 'com.glume', 'D:/glume/glume-boot', 'baba-boot', 'baba', 'com.baba', '.git,.idea,target,logs', 'java,xml,yml,txt', getdate(), getdate());
INSERT INTO gen_project_modify (project_name, project_code, project_package, project_path, modify_project_name, modify_project_code, modify_project_package, exclusions, modify_suffix, create_time, update_time) VALUES ('glume-cloud', 'glume', 'com.glume', 'D:/glume/glume-cloud', 'baba-cloud', 'baba', 'com.baba', '.git,.idea,target,logs', 'java,xml,yml,txt', getdate(), getdate());
