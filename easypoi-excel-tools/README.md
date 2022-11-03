# 一、EasyPoi 介绍

- 基于注解的导入导出，修改注解就可以修改Excel
- 支持常用的样式自定义
- 基于map可以灵活定义的表头字段
- 支持一堆多的导出、导入
- 支持模板的导出，一些常见的标签，自定义标签
- 支持HTML/Excel转换，如果模板还不能满足用户的变态需求，请用这个功能
- 支持word的导出、支持图片、Excel

> [EasyPoi 官方文档地址](http://doc.wupaas.com/docs/easypoi)

# 二、Maven 依赖

- 1.easypoi 父包–作用大家都懂得
- 2.easypoi-annotation 基础注解包，作用与实体对象上，拆分后方便maven多工程的依赖管理
- 3.easypoi-base 导入导出的工具包，可以完成Excel导出，导入，Word的导出，Excel的导出功能
- 4.easypoi-web 耦合了spring-mvc 基于AbstractView，极大的简化spring-mvc下的导出功能
- 5.sax 导入使用xercesImpl这个包(这个包可能造成奇怪的问题哈)，word导出使用poi-scratchpad，都作为可选包了

如果不使用spring mvc的便捷福利，直接引入easypoi-base 就可以了，easypoi-annotation

如果使用maven，请使用如下坐标

```xml
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-base</artifactId>
    <version>4.1.0</version>
</dependency>
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-web</artifactId>
    <version>4.1.0</version>
</dependency>
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-annotation</artifactId>
    <version>4.1.0</version>
</dependency>
```

随着spring boot的越来越流行，不可免俗的我们也推出了easypoi-spring-boot-starter，方便大家的引用和依赖

```xml
<dependency>
    <groupId>cn.afterturn</groupId>
    <artifactId>easypoi-spring-boot-starter</artifactId>
    <version>4.4.0</version>
</dependency>
```

> 加入其他不需要改动就可以完美的玩耍了，如果你不需要web服务，那只要依赖easypoi-base就可以

# 三、Excel 处理

Excel的导入导出是Easypoi的核心功能，前期基本也是围绕这个打造的，主要分为三种方式的处理，其中模板和Html目前只支持导出，因为支持Map.class其实导入应该是怎样都支持的

- 注解方式，注解变种方式
- 模板方式
- Html方式
- 流方式

下面分别就这四种方式进行讲解

## 1、注解

- [@Excel](https://github.com/Excel) 作用到Filed上面，是对Excel一列的一个描述
- [@ExcelCollection](https://github.com/ExcelCollection) 表示一个集合，主要针对一对多的导出，比如一个老师对应多个科目，科目就可以用集合表示
- [@ExcelEntity](https://github.com/ExcelEntity) 表示一个继续深入导出的实体，但他没有太多的实际意义，只是告诉系统这个对象里面同样有导出的字段
- [@ExcelIgnore](https://github.com/ExcelIgnore) 和名字一样表示这个字段被忽略跳过这个导出
- [@ExcelTarget](https://github.com/ExcelTarget) 这个是作用于最外层的对象，描述这个对象的id，以便支持一个对象可以针对不同导出做出不同处理

### 1.1、[@Excel](https://github.com/Excel)

这个是必须使用的注解，如果需求简单只使用这一个注解也是可以的，涵盖了常用的Excel需求，需要大家熟悉这个功能，主要分为基础、图片处理、时间处理、合并处理几块。

| 属性                | 类型     | 默认值           | 功能                                                         |
| :------------------ | :------- | :--------------- | :----------------------------------------------------------- |
| name                | String   | null             | 列名，支持name_id                                            |
| needMerge           | boolean  | fasle            | 是否需要纵向合并单元格(用于含有list中，单个的单元格，合并list创建的多个row) |
| orderNum            | String   | “0”              | 列的排序，支持name_id                                        |
| replace             | String[] | {}               | 值得替换 导出是{a_id，b_id} 导入反过来                       |
| savePath            | String   | “upload”         | 导入文件保存路径，如果是图片可以填写，默认是upload/className/ IconEntity这个类对应的就是upload/Icon/ |
| type                | int      | 1                | 导出类型 1 是文本 2 是图片，3 是函数，10 是数字 默认是文本   |
| width               | double   | 10               | 列宽                                                         |
| height              | double   | 10               | **列高，后期打算统一使用[@ExcelTarget](https://github.com/ExcelTarget)的height，这个会被废弃，注意** |
| isStatistics        | boolean  | fasle            | 自动统计数据，在追加一行统计，把所有数据都和输出[这个处理会吞没异常，请注意这一点 |
| isHyperlink         | boolean  | false            | 超链接，如果是需要实现接口返回对象                           |
| isImportField       | boolean  | true             | 校验字段，看看这个字段是不是导入的Excel中有，如果没有说明是错误的Excel，读取失败，支持name_id |
| exportFormat        | String   | “”               | 导出的时间格式，以这个是否为空来判断是否需要格式化日期       |
| importFormat        | String   | “”               | 导入的时间格式，以这个是否为空来判断是否需要格式化日期       |
| format              | String   | “”               | 时间格式，相当于同时设置了exportFormat 和 importFormat       |
| databaseFormat      | String   | “yyyyMMddHHmmss” | 导出时间设置，如果字段是Date类型则不需要设置 数据库如果是string 类型，这个需要设置这个数据库格式，用以转换时间格式输出 |
| numFormat           | String   | “”               | 数字格式化，参数是Pattern，使用的对象是DecimalFormat         |
| imageType           | int      | 1                | 导出类型 1 从file读取 2 是从数据库中读取 默认是文件 同样导入也是一样的 |
| suffix              | String   | “”               | 文字后缀，如% 90 变成90%                                     |
| isWrap              | boolean  | true             | 是否换行 即支持\n                                            |
| mergeRely           | int[]    | {}               | 合并单元格依赖关系，比如第二列合并是基于第一列 则{0}就可以了 |
| mergeVertical       | boolean  | fasle            | 纵向合并内容相同的单元格                                     |
| fixedIndex          | int      | -1               | 对应excel的列，忽略名字                                      |
| isColumnHidden      | boolean  | false            | 导出隐藏列                                                   |
| desensitizationRule | String   | “”               | 数据脱敏处理，`3_4`表示只显示字符串的前`3`位和后`4`位，其他为`*`号； |

*以上需要注意的是name_id，举一个例子*

```java
@ExcelTarget("teacherEntity")
public class TeacherEntity implements java.io.Serializable {
    /** name */
    @Excel(name = "主讲老师_teacherEntity,代课老师_absent", orderNum = "1", mergeVertical = true,needMerge=true,isImportField = "true_major,true_absent")
    private String name;
```

* 这里的[@ExcelTarget](https://github.com/ExcelTarget) 表示使用teacherEntity这个对象是可以针对不同字段做不同处理（同样的ExcelEntity 和 ExcelCollection 都支持这种方式）
* 当导出这对象时，name这一列对应的是主讲老师，而不是代课老师

### 1.2、[@ExcelCollection](https://github.com/ExcelCollection)

一对多的集合注解，用以标记集合是否被数据以及集合的整体排序

| 属性     | 类型     | 默认值          | 功能                      |
| :------- | :------- | :-------------- | :------------------------ |
| id       | String   | null            | 定义ID                    |
| name     | String   | null            | 定义集合列名，支持nanm_id |
| orderNum | int      | 0               | 排序，支持name_id         |
| type     | Class<?> | ArrayList.class | 导入时创建对象使用        |

### 1.3、[@ExcelEntity](https://github.com/ExcelEntity)

标记是不是导出 excel 标记为实体类，一遍是一个内部属性类，标记是否继续穿透，可以自定义内部id

| 属性 | 类型   | 默认值 | 功能   |
| :--- | :----- | :----- | :----- |
| id   | String | null   | 定义ID |

### 1.4、[@ExcelTarget](https://github.com/ExcelTarget)

限定一个到处实体的注解，以及一些通用设置，作用于最外面的实体

| 属性     | 类型   | 默认值 | 功能         |
| -------- | ------ | ------ | ------------ |
| value    | String | null   | 定义ID       |
| height   | double | 10     | 设置行高     |
| fontSize | short  | 11     | 设置文字大小 |

## 2、自定义数据处理器

EasyPoi 提供了一个`IExcelDataHandler<T>`接口供我们实现自定义数据处理。

```java
// Excel 导入导出 数据处理接口
public interface IExcelDataHandler<T> {
    /**
     * 导出处理方法
     * @param obj 当前对象
     * @param name 当前字段名称
     * @param value 当前值
     * @return
     */
    public Object exportHandler(T obj, String name, Object value);
    /**
     * 获取需要处理的字段,导入和导出统一处理了, 减少书写的字段
     * @return
     */
    public String[] getNeedHandlerFields();
    /**
     * 导入处理方法
     * @param obj 当前对象
     * @param name 当前字段名称
     * @param value 当前值
     * @return
     */
    public Object importHandler(T obj, String name, Object value);
    /**
     * 设置需要处理的属性列表
     * @param fields
     */
    public void setNeedHandlerFields(String[] fields);
    /**
     * 设置Map导入,自定义 put
     * @param map 数据对象
     * @param originKey 字段名
     * @param value 当前值
     */
    public void setMapValue(Map<String, Object> map, String originKey, Object value);
    /**
     * 获取这个字段的 Hyperlink ,07版本需要,03版本不需要
     * @param creationHelper
     * @param obj
     * @param name
     * @param value
     * @return
     */
    public Hyperlink getHyperlink(CreationHelper creationHelper, T obj, String name, Object value);
}
```

通常我们只需要继承`ExcelDataHandlerDefaultImpl`类，避免实现多余的接口。重写`setNeedHandlerFields`方法来完成自己的字段处理逻辑。

这里提供一下官方的Demo：将map导入的时候把map的key给转了，从中文转为习惯的英文

```java
public class MapImportHandler extends ExcelDataHandlerDefaultImpl<Map<String, Object>> {
    @Override
    public void setMapValue(Map<String, Object> map, String originKey, Object value) {
        if (value instanceof Double) {
            map.put(getRealKey(originKey), PoiPublicUtil.doubleToString((Double) value));
        } else {
            map.put(getRealKey(originKey), value != null ? value.toString() : null);
        }
    }
    private String getRealKey(String originKey) {
        if (originKey.equals("交易账户")) {
            return "accountNo";
        }
        if (originKey.equals("姓名")) {
            return "name";
        }
        if (originKey.equals("客户类型")) {
            return "type";
        }
        return originKey;
    }
}
```

## 3、Excel 导入校验

### 3.1 结合 JSR 303 进行验证

校验是一个不可或缺的功能，现在JAVA校验主要是`JSR 303`规范，实现方式主流的有两种

- Hibernate Validator
- Apache Commons Validator

这个EasyPoi没有限制，只要你防止一个实现丢到maven中就可以了，但是Hibernate Validator用的貌似多一些之前的版本EasyPoi有定义自己的实现，但是后来抛弃了，没有必要造这种轮子，这个了功能已经够丰富了。

EasyPoi的校验使用也很简单，对象上加上通用的校验规则或者这定义的这个看你用的哪个实现然后`params.setNeedVerfiy(true);`配置下需要校验就可以了。

```java
// 非空校验
@Excel(name = "name")
@NotEmpty(message = "姓名不能为空", groups = { ExcelVerify.class })
private String name;
```

```java
ImportParams params = new ImportParams();
// 开启验证
params.setNeedVerfiy(true);
// JSR 303 校验注解的分组
params.setVerfiyGroup(new Class[]{ ExcelVerify.class });
```

> 具体使用方法请看`EasyPoiImportController.class`里的`importMemberValidatorEasyPoi()`；

### 3.2 ExcelImportResult

```java
// importExcelMore 方法返回带有校验结果
ExcelImportResult<MemberVo> result = ExcelImportUtil.importExcelMore(file.getInputStream(), MemberVo.class, params);
```

注意在导入的时候请使用`ExcelImportUtil`中的`importExcelMore`方法，`importExcelMore`与`importExcel`的区别在：

```java
// 返回的是getList()，是ExcelImportResult对象的成功集合
public static <T> List<T> importExcel(InputStream inputstream, Class<?> pojoClass,ImportParams params) throws Exception {
    return new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, false).getList();
}
// 直接返回的是ExcelImportResult对象
public static <T> ExcelImportResult<T> importExcelMore(InputStream inputstream, Class<?> pojoClass, ImportParams params) throws Exception {
    return new ExcelImportService().importExcelByIs(inputstream, pojoClass, params, true);
}
```

ExcelImportResult 对象会比平时返回的List多了一些元素:

```java
// 结果集
private List<T>  list;
// 校验失败结果集
private List<T>  failList;
// 是否存在校验失败
private boolean  verfiyFail;
// 数据源
private Workbook workbook;
// 失败的数据源
private Workbook failWorkbook;
```

注意：这里的List属性，有两种返回结果

* 一种是只返回正确的数据
* 一种是只返回带有错误信息与错误行号的数据，要想拥有这种效果必须让Java Bean实现`IExcelModel`与`IExcelDataModel`接口。

### 3.3 IExcelModel

获取错误数据的信息

```java
public interface IExcelModel {
    // 获取错误数据
    public String getErrorMsg();
    // 设置错误信息
    public void setErrorMsg(String errorMsg);
}
```

### 3.4 IExcelDataModel

获取错误数据的行号

```java
public interface IExcelDataModel {
    // 获取行号
    public Integer getRowNum();
   	//设置行号
    public void setRowNum(Integer rowNum);
}
```

### 3.5 IExcelVerifyHandler 自定义校验规则

假如上面的不满足你，你可以用接口实现自己的校验规则，比如唯一性校验等，需要返回错误信息和成功与否

```java
public interface IExcelVerifyHandler<T> {
    /**
     * 导入校验方法
     * @param obj 当前对象
     * @return
     */
    public ExcelVerifyHandlerResult verifyHandler(T obj);
}
```

> 具体使用方法请看`EasyPoiImportController.class`里的`importOrderNestExcel()`；

### 3.6 验证导入嵌套集合以及对象数据（坑）

从Java Bean 的角度去思考，Excel 文件中的数据如果包含一个集合以及对象。在我们导入这个Excel 文件并且使用的是`ExcelImportUtil.importExcelMore`方法时会报错：

```
Cannot add merged region B6:B8 to sheet because it overlaps with an existing merged region (B4:B6).
无法将合并的区域B6:B8添加到工作表中，因为它与现有的合并区域(B4:B6)重叠。
```

解决办法：把导入文件分割属性设置为 `false`

```java
ImportParams params = new ImportParams();
params.setVerifyFileSplit(false);
```

### 3.7 验证导入嵌套集合以及对象数据，验证注解没有执行（坑）

验证导入嵌套集合以及对象数据时，Collection中的对象添加了Hibernate Validator 注解校验但是并未生效的问题。由于校验类会校验Excel中的每一条数据， 那我是不是可以直接在校验类中校验Collection中对象了呢？实践证明行不通，因为这个校验类的verifyHandler方法只会被调用一次，所以Collection中只有一条记录。既然这里行不通的话，就只能对导入结果再进行校验了。

因为Collection中的数据EasyPOI校验不到，所以有问题的数据也可能会被框架放到`result.getList()`中而不是`result.getFailList()`中，为了校验需要将两个集合合并为一个集合或者在IexcelVerifyHandler 校验器里，使用 EasyPOI 自带的工具类 PoiValidationUtil 进行校验 Collection 中的对象。

具体使用方法请看`EasyPoiImportController.class`里的`importOrderNestExcel()`；

















