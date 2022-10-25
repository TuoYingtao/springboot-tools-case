## 一、FastJson  的使用

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.76</version>
</dependency>
```

### 1、FastJson 常用API

`fastjson API` 入口类是`com.alibaba.fastjson.JSON`,常用的序列化操作都可以在`JSON`类上的静态方法直接完成。

```java
public static final Object parse(String text); // 把JSON文本parse为JSONObject或者JSONArray

public static final JSONObject parseObject(String text)； // 把JSON文本parse成JSONObject 
    
public static final <T> T parseObject(String text, Class<T> clazz); // 把JSON文本parse为JavaBean 

public static final JSONArray parseArray(String text); // 把JSON文本parse成JSONArray 

public static final <T> List<T> parseArray(String text, Class<T> clazz); //把JSON文本parse成JavaBean集合 

public static final String toJSONString(Object object); // 将JavaBean序列化为JSON文本 

public static final String toJSONString(Object object, boolean prettyFormat); // 将JavaBean序列化为带格式的JSON文本 

public static final Object toJSON(Object javaObject); // 将JavaBean转换为JSONObject或者JSONArray。
```

### 2、FastJSON 常用注解

```java
// 适用范围：属性、参数、方法
public @interface JSONField {
    // 配置序列化和反序列化的顺序，值越小越靠前
    int ordinal() default 0;

     // 指定字段的名称
    String name() default "";

    // 序列化和反序列使用yyyyMMdd日期格式，对日期格式有用
    String format() default "";

    // 是否序列化
    boolean serialize() default true;

    // 是否反序列化
    boolean deserialize() default true;
}
```

### 3、FastJson 还支持 BeanToArray 序列化功能

```java
private List<Person> listOfPersons = new ArrayList<Person>();
String jsonOutput= JSON.toJSONString(listOfPersons, SerializerFeature.BeanToArray);
```

输出结果为：

```javascript
[
    [
        15,
        1469003271063,
        "John Doe"
    ],
    [
        20,
        1469003271063,
        "Janette Doe"
    ]
]
```

### 4、使用 ContextValueFilter 配置 JSON 转换

在某些场景下，对Value做过滤，需要获得所属JavaBean的信息，包括类型、字段、方法等。在fastjson-1.2.9中，提供了ContextValueFilter，类似于之前版本提供的ValueFilter，只是多了BeanContext参数可用。

```java
@Test
public void givenContextFilter_whenJavaObject_thanJsonCorrect() {
    ContextValueFilter valueFilter = new ContextValueFilter () {
        public Object process(
          BeanContext context, Object object, String name, Object value) {
            if (name.equals("DATE OF BIRTH")) {
                return "NOT TO DISCLOSE";
            }
            if (value.equals("John")) {
                return ((String) value).toUpperCase();
            } else {
                return null;
            }
        }
    };
    String jsonOutput = JSON.toJSONString(listOfPersons, valueFilter);
}
```

以上实例中我们隐藏了 DATE OF BIRTH 字段，并过滤名字不包含 John 的字段：

```javascript
[
    {
        "FULL NAME":"JOHN DOE",
        "DATE OF BIRTH":"NOT TO DISCLOSE"
    }
]
```

### 5、使用 NameFilter 和 SerializeConfig

**NameFilter**: 序列化时修改 Key。

**SerializeConfig**：内部是个map容器主要功能是配置并记录每种Java类型对应的序列化类。

```java
@Test
public void givenSerializeConfig_whenJavaObject_thanJsonCorrect() {
    NameFilter formatName = new NameFilter() {
        public String process(Object object, String name, Object value) {
            return name.toLowerCase().replace(" ", "_");
        }
    };
     
    SerializeConfig.getGlobalInstance().addFilter(Person.class,  formatName);
    String jsonOutput =
      JSON.toJSONStringWithDateFormat(listOfPersons, "yyyy-MM-dd");
}
```

实例中我们声明了 formatName 过滤器使用 NameFilter 匿名类来处理字段名称。 新创建的过滤器与 Person 类相关联，然后添加到全局实例，它是 SerializeConfig 类中的静态属性。

现在我们可以轻松地将对象转换为JSON格式。

注意我们使用的是 toJSONStringWithDateFormat() 而不是 toJSONString() ，它可以更快速的格式化日期。

输出结果：

```javascript
[  
    {  
        "full_name":"John Doe",
        "date_of_birth":"2016-07-21"
    },
    {  
        "full_name":"Janette Doe",
        "date_of_birth":"2016-07-21"
    }
]
```

### 6、定制序列化

#### 6.1、通过SerializeFilter定制序列化

##### 6.1.1、Fastjson API SerializeFilter简介

SerializeFilter是通过编程扩展的方式定制序列化。fastjson支持6种SerializeFilter，用于不同场景的定制序列化。

1. PropertyPreFilter 根据PropertyName判断是否序列化
2. PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化
3. NameFilter 修改Key，如果需要修改Key,process返回值则可
4. ValueFilter 修改Value
5. BeforeFilter 序列化时在最前添加内容
6. AfterFilter 序列化时在最后添加内容

##### 6.1.1、PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化

```java
  public interface PropertyFilter extends SerializeFilter {
      boolean apply(Object object, String propertyName, Object propertyValue);
  }
```

可以通过扩展实现根据object或者属性名称或者属性值进行判断是否需要序列化。例如：

```java
    PropertyFilter filter = new PropertyFilter() {

        public boolean apply(Object source, String name, Object value) {
            if ("id".equals(name)) {
                int id = ((Integer) value).intValue();
                return id >= 100;
            }
            return false;
        }
    };

    JSON.toJSONString(obj, filter); // 序列化的时候传入filter
```

##### 6.1.2、PropertyPreFilter 根据PropertyName判断是否序列化

和PropertyFilter不同只根据object和name进行判断，在调用getter之前，这样避免了getter调用可能存在的异常。

```java
 public interface PropertyPreFilter extends SerializeFilter {
      boolean apply(JSONSerializer serializer, Object object, String name);
  }
```

##### 6.1.3、NameFilter 序列化时修改Key

如果需要修改Key,process返回值则可

```java
public interface NameFilter extends SerializeFilter {
    String process(Object object, String propertyName, Object propertyValue);
}
```

fastjson内置一个PascalNameFilter，用于输出将首字符大写的Pascal风格。 例如：

```java
import com.alibaba.fastjson.serializer.PascalNameFilter;

Object obj = ...;
String jsonStr = JSON.toJSONString(obj, new PascalNameFilter());
```

##### 6.1.4、ValueFilter 序列化是修改Value

```java
  public interface ValueFilter extends SerializeFilter {
      Object process(Object object, String propertyName, Object propertyValue);
  }
```

##### 6.1.5、BeforeFilter 序列化时在最前添加内容

> 在序列化对象的所有属性之前执行某些操作,例如调用 writeKeyValue 添加内容

```java
  public abstract class BeforeFilter implements SerializeFilter {
      protected final void writeKeyValue(String key, Object value) { ... }
      // 需要实现的抽象方法，在实现中调用writeKeyValue添加内容
      public abstract void writeBefore(Object object);
  }
```

##### 6.1.6、AfterFilter 序列化时在最后添加内容

> 在序列化对象的所有属性之后执行某些操作,例如调用 writeKeyValue 添加内容

```java
  public abstract class AfterFilter implements SerializeFilter {
      protected final void writeKeyValue(String key, Object value) { ... }
      // 需要实现的抽象方法，在实现中调用writeKeyValue添加内容
      public abstract void writeAfter(Object object);
  }java
```

#### 6.2、通过ParseProcess定制反序列化

##### 6.2.1. Fastjson API ParseProcess简介

ParseProcess是编程扩展定制反序列化的接口。fastjson支持如下ParseProcess：

- ExtraProcessor 用于处理多余的字段
- ExtraTypeProvider 用于处理多余字段时提供类型信息

##### 6.2.2. 使用ExtraProcessor 处理多余字段

```java
public static class VO {
    private int id;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    public int getId() { return id; }
    public void setId(int id) { this.id = id;}
    public Map<String, Object> getAttributes() { return attributes;}
}

ExtraProcessor processor = new ExtraProcessor() {
    public void processExtra(Object object, String key, Object value) {
        VO vo = (VO) object;
        vo.getAttributes().put(key, value);
    }
};

VO vo = JSON.parseObject("{\"id\":123,\"name\":\"abc\"}", VO.class, processor);
Assert.assertEquals(123, vo.getId());
Assert.assertEquals("abc", vo.getAttributes().get("name"));
```

##### 6.2.3. 使用ExtraTypeProvider 为多余的字段提供类型

```java
public static class VO {
    private int id;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    public int getId() { return id; }
    public void setId(int id) { this.id = id;}
    public Map<String, Object> getAttributes() { return attributes;}
}

class MyExtraProcessor implements ExtraProcessor, ExtraTypeProvider {
    public void processExtra(Object object, String key, Object value) {
        VO vo = (VO) object;
        vo.getAttributes().put(key, value);
    }

    public Type getExtraType(Object object, String key) {
        if ("value".equals(key)) {
            return int.class;
        }
        return null;
    }
};
ExtraProcessor processor = new MyExtraProcessor();

VO vo = JSON.parseObject("{\"id\":123,\"value\":\"123456\"}", VO.class, processor);
Assert.assertEquals(123, vo.getId());
Assert.assertEquals(123456, vo.getAttributes().get("value")); // value本应该是字符串类型的，通过getExtraType的处理变成Integer类型了。
```

### 7、参考文献

【1】[W3Cschool](https://www.w3cschool.cn/fastjson/fastjson-intro.html)

## 二、Gson 的使用

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.6</version>
</dependency>
```

**1、创建实例**

使用Gson的第一步是创建一个Gson对象，创建Gson对象有两种方式：

- 使用 new Gson()。（简单实例，标准配置就足够了）
- 创建GsonBuilder实例，使用 create() 方法返回一个Gson实例。（自定义Gson的配置行为）

```java
Gson gson = new Gson();
Gson gson = new GsonBuilder().create();
```

**2、Java 对象序列化**

```java
String jsonString = gson.toJson(employee);
```

**3、Json 数据转 Java 对象**

```java
Employee empObject = gson.fromJson(jsonString, Employee.class);
```

### 1、JSON 数据解析

#### 1.1、JSON 数据转对象

```json
{"name": "Alex","id": 1,"users":[{"name": "Alex","id": 1},{"name": "Brian","id": 2},{"name": "Charles","id": 3}]}
```

```java
User user = gson.fromJson(userJson, User.class);
```

#### 1.2、JSON Array 转数组

```json
[{"name": "Alex","id": 1},{"name": "Brian","id": 2},{"name": "Charles","id": 3}]
```

```java
User[] userArray = gson.fromJson(userJson, User[].class);
```

#### 1.3、JSON Array 转 List

```java
List<User> userList = gson.fromJson(userJson, new TypeToken<ArrayList<User>>() {
}.getType());
```

#### 1.4、Set 转 JSON

使用Gson.toJson()方法将HashSet序列化为JSON:

```java
Set<String> userSet = new HashSet<String>();
userSet.add("Alex");
userSet.add("Brian");
userSet.add("Charles");
String jsonString= gson.toJson(userSet); 
// 结果：["Alex","Brian","Charles"]
```

**1.5、JSON 转 Set**

```java
String jsonString = "['Alex','Brian','Charles','Alex']";
Set<String> userSet = gson.fromJson(jsonString, new TypeToken<HashSet<String>>(){
}.getType()); 
// 结果：[Alex, Brian, Charles]
```

### 2、Null值处理

Gson中实现的默认行为是忽略空对象字段。

例如，如果在Employee对象中未指定电子邮件（即email值为null），则电子邮件将不会被序列化JSON输出。Gson会忽略null字段，因为此行为允许使用更紧凑的JSON输出格式。

#### 2.1、如何在序列化时允许空值

要配置Gson实例以输出null，我们必须使用GsonBuilder对象的serializeNulls()。

```java
Gson gson = new GsonBuilder()
    .serializeNulls() // 当字段值为空或null时，依然对该字段进行序列化
    .create();
```

### 3、版本序列化控制注解

应用程序随着时间变化，模型类也随之变化。有时候更新/删除字段可能会被打断。

所有这些更改都可以使用@Since注释进行标记，以跟踪模型类，在这些系统使用反序列化JSON数据进行交换时，与其他系统的应用程序交互不会中断。

`@Since` 代表从某个版本启用
`@Until` 代表从某个版本弃用

#### 3.1、@Since注解

在Gson中，可以使用@Since注释维护同一对象的多个版本。可以在类，字段以及将来的方法中使用此注释。

当我们为Gson实例配置版本号“ M.N”时，所有标记有版本大于M.N的类字段都将被忽略。

例如，如果我们将Gson配置为版本号“ 1.2”，则所有版本号更高的字段（1.3、1.4…）都将被忽略。

```java
@Since(1.2)
private String email;
```

#### 3.2、如何使用@Since注解编写版本化的类

在Employee类下面，我们对三个字段进行了版本控制，即firstName，lastName和email。

```java
public class Employee {
    private Integer id;
 
    @Since(1.0) // 从1.0版本开始启用该字段
    private String firstName;
     
    @Since(1.1) // 从1.1版本开始启用该字段
    private String lastName;
     
    @Since(1.2) // 从1.2版本开始启用该字段
    private String email;
    
    @Until(1.0) // 从1.0版本开始弃用该字段
    private String sex;
}
```

#### 3.3、创建具备版本支持的Gson实例

要创建使用过@Since注解的Gson实例，需要使用GsonBuilder.setVersion()方法:

```java
Gson gson = new GsonBuilder()
    .setVersion(1.1)
    .create();
```

#### 3.4、实例

##### 3.4.1、序列化

```java
Gson gson = new GsonBuilder()
    .setVersion(1.1)
    .setPrettyPrinting()
    .create();
```

输出：

```json
{
  "id": 1,
  "firstName": "Lokesh",
  "lastName": "Gupta"
}
```

##### 3.4.2、反序列化

```java
String json = "{'id': 1001, "
                + "'firstName': 'Lokesh',"
                + "'lastName': 'Gupta',"
                + "'email': '123456@gmail.com'}";
Employee employeeObj = gson.fromJson(json, Employee.class);
```

输出：

```
Employee [id=1001, firstName=Lokesh, lastName=Gupta, email=null]
```

### 4、常用注解注解

#### 4.1、@SerializedName注解

```java
@SerializedName("user_name")
private String userName;
@SerializedName(value = "user_name", alternate = {"user_nameA", "user_nameB"})
private String userName;
```

* `value`序列化时字段名映射。

* `alternate`备用字段映射，在反序列化时不论JSON中字段是`value`还是`alternate`中的值都会被匹配上

#### 4.2、@Expose注解

GSON @Expose注解（com.google.gson.annotations.Expose）可用于标记对象序列化或反序列化时是否忽略的字段。

```java
public @interface Expose {
    // 序列化 true：带有的字段 false：忽略的字段
    boolean serialize() default true;
	// 反序列化 true：带有的字段 false：忽略的字段
    boolean deserialize() default true;
}
```

还必须使用GsonBuilder 来构建GSON对象，并且设置excludeFieldsWithoutExposeAnnotation()

```java
Gson gson = new GsonBuilder()
    .excludeFieldsWithoutExposeAnnotation()
    .create();
```

> 不加@Expose，意味着该字段不能正反序列化。

##### 4.2.1、用修饰符排除字段

`transient`修饰符具有与@Expose相同的效果（serialize = false，deserialize = false）。

```java
private transient String emailAddress;
```

##### 4.2.2、其它修饰符排除字段

通过使用GsonBuilder的excludeFieldsWithModifiers()方法，我们可以排除具有某些公共修饰符的字段。

```java
Gson gson = new GsonBuilder()
    .excludeFieldsWithModifiers(Modifier.STATIC)
    .create();
```

##### 4.2.3、自定义排除策略

如果以上任何一种技术都不适合我们，那么我们可以创建自己的策略。

ExclusionStrategy用于确定是否应将字段或顶级类作为JSON输出/输入的一部分进行序列化或反序列化。

- 对于序列化，如果shouldSkipClass（Class）或shouldSkipField（fieldAttributes）方法返回true，则该类或字段类型将不属于JSON输出。
- 对于反序列化，如果shouldSkipClass（Class）或shouldSkipField（fieldAttributes）方法返回true，则不会将其设置为Java对象结构的一部分。

例如，在ExclusionStrategy定义下面，将排除所有使用@Hidden注释注释的字段:

```java
//public @interface Hidden {
    // some implementation here
//}
 
// Excludes any field (or class) that is tagged with an "@Hidden"
public class HiddenAnnotationExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(Hidden.class) != null;
    }
 
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(Hidden.class) != null;
    }
}
```

要使用此排除策略，在GsonBuilder对象中进行设置:

```java
Gson gson = new GsonBuilder()
    .setExclusionStrategies(new HiddenAnnotationExclusionStrategy())
    .create();
```

### 5、Gson 的漂亮格式化

默认情况下，Gson将创建紧凑的JSON字符串。这对于减少通过网络传输的数据量非常有用。

```json
{"id":1,"firstName":"Lokesh","lastName":"Gupta","emailId":"howtogoinjava@gmail.com"}
```

但是，这种紧凑的JSON在开发/调试应用程序时不友好很难阅读。因此，GSON提供了一个漂亮的打印选项，可以在其中打印JSON，以便于更加方便阅读，只需要在GsonBuilder构建Gson对象时设置setPrettyPrinting()来格式化。

```java
Gson gson = new GsonBuilder()
    .setPrettyPrinting() // 自动格式化换行
    .create();
```

```json
{
    "id":1,
    "firstName":"Lokesh",
    "lastName":"Gupta",
    "emailId":"howtogoinjava@gmail.com"
}
```

### 6、FieldNamingPolicy 标准命名约定

FieldNamingPolicy枚举在序列化期间为JSON字段名称提供了几种标准命名约定。

它有助于Gson实例将Java字段名称正确转换为所需的JSON字段名称。

注意：以下任何命名约定均不会影响以@SerializedName注释的字段。我们将验证使用User类的每个策略生成的名称。

```java
public class User {
    private int id;
    private String first_Name;
    private String lastName;
    private String _email;
}
```

```java
Gson gson = new GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    .setPrettyPrinting().create();
```

以下几种不同的命名策略演示：

#### 6.1、FieldNamingPolicy.IDENTITY

使用此命名策略字段名称不变。这个是默认的策略：

```json
{
  "id": 1,
  "first_Name": "Lokesh",
  "lastName": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

#### 6.2、FieldNamingPolicy.LOWER_CASE_WITH_DASHES

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用破折号（-）分隔。

```json
{
  "id": 1,
  "first_-name": "Lokesh",
  "last-name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

#### 6.3、FieldNamingPolicy.LOWER_CASE_WITH_DOTS

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用点（.）分隔:

```json
{
  "id": 1,
  "first_.name": "Lokesh",
  "last.name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

#### 6.4、FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用下划线（_）分隔。

```json
{
  "id": 1,
  "first__name": "Lokesh",
  "last_name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

#### 6.5、FieldNamingPolicy.UPPER_CAMEL_CASE

Gson将确保序列化为JSON格式的Java字段名称的第一个“字母”大写：

```json
{
  "Id": 1,
  "First_Name": "Lokesh",
  "LastName": "Gupta",
  "_Email": "admin@howtodoinjava.com"
}
```

#### 6.6、FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES

Gson将确保在将Java字段名称的第一个“字母”序列化为JSON格式时将其大写，并且单词之间将使用空格分隔：

```json
{
  "Id": 1,
  "First_ Name": "Lokesh",
  "Last Name": "Gupta",
  "_Email": "admin@howtodoinjava.com"
}
```

### 7、参考文献

【1】：[Google Gson用法详解](https://cloud.tencent.com/developer/article/1662930)

【1】：[Gson](https://zh.wikipedia.org/wiki/Gson) 

【2】：[Gson – Introduction](https://howtodoinjava.com/learningpaths/gson/) 

【3】：[Gson – Installation](https://howtodoinjava.com/gson/gson-installation-maven-gradle-jar/) 

【4】：[GSON - Gson](http://tutorials.jenkov.com/java-json/gson.html) 

【5】：[GSON – Serialize and Deserialize JSON](https://howtodoinjava.com/gson/gson-serialize-deserialize-json/) 

【6】：[Gson – Pretty Printing for JSON Output](https://howtodoinjava.com/gson/pretty-print-json-output/) 

【7】：[GSON – Parse JSON array to Java array or list](https://howtodoinjava.com/gson/gson-parse-json-array/) 

【8】：[GSON – Serialize and deserialize JSON to Set](https://howtodoinjava.com/gson/gson-serialize-deserialize-set/) 

【9】：[Gson – GsonBuilder Configuration Examples](https://howtodoinjava.com/gson/gson-gsonbuilder-configuration/) 

【10】：[Gson @Since – Version Support](https://howtodoinjava.com/gson/gson-since-version-support/) 

【11】：[Gson @SerializedName](https://howtodoinjava.com/gson/gson-serializedname/) 

【12】：[Gson – JsonReader](https://howtodoinjava.com/gson/jsonreader-streaming-json-parser/) 

【13】：[Gson – JsonReader](https://howtodoinjava.com/gson/jsonreader-streaming-json-parser/) 

【14】：[Gson – JsonParser](https://howtodoinjava.com/gson/gson-jsonparser/) 

【15】：[GSON - JsonParser](http://tutorials.jenkov.com/java-json/gson-jsonparser.html)

## 三、Jackson 的使用

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-core</artifactId>
    <version>2.12.5</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.12.5</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-annotations</artifactId>
    <version>2.12.5</version>
</dependency>
```

### 1、Jackson 的核心模块。

`jackson-core`核心包，提供基于"流模式"解析的相关 API，它包括 JsonPaser 和 JsonGenerator。 Jackson 内部实现正是通过高性能的流模式 API 的 JsonGenerator 和 JsonParser 来生成和解析 json。

`jackson-annotations`注解包，提供标准注解功能；

`jackson-databind`数据绑定包， 提供基于"对象绑定" 解析的相关 API （ ObjectMapper ） 和"树模型" 解析的相关 API （JsonNode）；基于"对象绑定" 解析的 API 和"树模型"解析的 API 依赖基于"流模式"解析的 API。

源码地址：[FasterXML/jackson](https://link.juejin.cn/?target=https%3A%2F%2Fgithub.com%2FFasterXML%2Fjackson.git)

### 2、ObjectMapper 序列化对象

* ObjectMapper 主要用于对 Java 对象（比如 POJO、List、Set、Map 等等）进行序列化与反序列化。

* ObjectMapper 除了能在 json 字符串与 Java 对象之间进行转换，还能将 json 字符串与 JsonNode 进行转换。

```java
/**  Java 对象与 Json 字符串的转换   */
public String writeValueAsString(Object value); // 将任何Java对象序列化为json字符串，如果对象中某个属性的值为null，则默认也会序列化为null

public byte[] writeValueAsBytes(Object value); // 将Java对象序列化为字节数组

public void writeValue(File resultFile, Object value); // 将任何Java对象序列化为JSON输出，并写入File中。

public void writeValue(OutputStream out, Object value); // 将任何Java对象序列化并输出到指定字节输出流中

public void writeValue(Writer w, Object value); // 将任何Java对象序列化并输出到指定字符输出流中

public <T> T readValue(String content, Class<T> valueType); // 从给定的JSON内容字符串反序列化JSON内容，比如 POJO、List、Set、Map 等等

public <T> T readValue(byte[] src, Class<T> valueType); // 从给定的JSON内容的字节数组反序列化为java对象

public <T> T readValue(File src, Class<T> valueType); // 将JSON内容从给定的文件反序列化为给定的Java类型

public <T> T readValue(InputStream src, Class<T> valueType); // 将JSON内容从给定的字节流反序列化为给定的Java类型

 public <T> T readValue(Reader src, Class<T> valueType); // 将JSON内容从给定的字符流反序列化为给定的Java类型

public <T> T readValue(URL src, Class<T> valueType); // 将JSON内容从给定的网络资源反序列化为给定的Java类型

/**  Json 字符串内容反序列化为 Json 节点对象  */
public JsonNode readTree(String content); // 将JSON字符串反序列化为JsonNode对象，即JSON节点对象

public JsonNode readTree(URL source); // 将JSON内容从给定的网络资源反序列化为JsonNode对象

public JsonNode readTree(InputStream in); // 将JSON内容从给定的字节流反序列化为JsonNode对象

public JsonNode readTree(byte[] content); // 将JSON内容从给定的字节数组反序列化为JsonNode对象

public JsonNode readTree(File file); // 将JSON内容从给定的文件反序列化为JsonNode对象

/** Java 对象与 Json 节点对象的转换 */
public <T> T convertValue(Object fromValue, Class<T> toValueType); // 将Java对象（如 POJO、List、Map、Set 等）序列化为Json节点对象。

public <T> T treeToValue(TreeNode n, Class<T> valueType); // Json树节点对象转Java对象（如 POJO、List、Set、Map 等等）TreeNode树节点是整个Json节点对象模型的根接口。
```

### 3、Jackson 常用配置

#### 3.1、JSON 忽略未知字段

有时候从JSON反序列化为Java对象，JSON中的字段更多。 默认情况下，Jackson在这种情况下会抛出异常，报不知道XXX字段异常，因为在Java对象中找不到该字段。在这种情况下，可以使用Jackson配置忽略这些额外的字段。 

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
```

#### 3.2、不允许基本类型为null

当JSON数据中存在基本类型字段值为null，而在Java对象中是不允许基本类型的值为null，反序列化时Jackson会忽略该字段。我们也可以修改Jackson ObjectMapper 配置使它在反序列化过程中，该字段存在null值则抛出一个异常。

```java
objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
```

#### 3.3、序列化结果格式化

```java
objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
```

#### 3.4、空对象不要抛出异常

```java
mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
```

#### 3.5、Date、Calendar等序列化为时间格式的字符串(如果不执行以下设置，就会序列化成时间戳格式)

```java
mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
```

#### 3.6、反序列化时，空字符串对于的实例属性为null

```java
mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
```

#### 3.7、允许C和C++样式注释

```java
mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
```

#### 3.8、允许字段名没有引号（可以进一步减小json体积）

```java
mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
```

#### 3.9、允许单引号

```java
mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
```

#### 3.10、properties 配置

```yaml
spring:
  jackson:
    # 设置属性命名策略,对应jackson下PropertyNamingStrategy中的常量值，SNAKE_CASE-返回的json驼峰式转下划线，json body下划线传到后端自动转驼峰式
    property-naming-strategy: SNAKE_CASE
    # 全局设置@JsonFormat的格式pattern
    date-format: yyyy-MM-dd HH:mm:ss
    # 当地时区
    locale: zh_CN
    # 设置全局时区
    time-zone: GMT+8
    # 常用，全局设置pojo或被@JsonInclude注解的属性的序列化方式
    default-property-inclusion: NON_NULL #不为空的属性才会序列化,具体属性可看JsonInclude.Include
    # 常规默认,枚举类SerializationFeature中的枚举属性为key，值为boolean设置jackson序列化特性,具体key请看SerializationFeature源码
    visibility:
      #属性序列化的可见范围
      getter: non_private
      #属性反序列化的可见范围
      setter: protected_and_public
      #静态工厂方法的反序列化
      CREATOR: public_only
      #字段
      FIELD: public_only
      #布尔的序列化
      IS_GETTER: public_only
      #所有类型(即getter setter FIELD）不受影响,无意义
      NONE: public_only
      #所有类型(即getter setter FIELD）都受其影响（慎用）
      ALL: public_only
    serialization:
      #反序列化是否有根节点
      WRAP_ROOT_VALUE: false
      #是否使用缩进，格式化输出
      INDENT_OUTPUT: false
      FAIL_ON_EMPTY_BEANS: true # 对象不含任何字段时是否报错，默认true
      FAIL_ON_SELF_REFERENCES: true #循环引用报错
      WRAP_EXCEPTIONS: true #是否包装异常
      FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS: true #JsonUnwrapped标记的类有类型信息是否报错
      WRITE_SELF_REFERENCES_AS_NULL: false #循环引用返回null
      CLOSE_CLOSEABLE: true #若对象实现了CLOSEABLE接口，在序列化后是否调用Close方法
      FLUSH_AFTER_WRITE_VALUE: false #流对象序列化之后是否强制刷新
      WRITE_DATES_AS_TIMESTAMPS: true # 返回的java.util.date转换成时间戳
      WRITE_DATES_WITH_ZONE_ID: true #2011-12-03T10:15:30+01:00[Europe/Paris]带时区id
      WRITE_DURATIONS_AS_TIMESTAMPS: true #将DURATIONS转换成时间戳
      WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS: false #是否字符数组输出json数组 (false则输出字符串)
      WRITE_ENUMS_USING_TO_STRING: false # 将枚举输出toString
      WRITE_ENUMS_USING_INDEX: false #枚举下标
      WRITE_ENUM_KEYS_USING_INDEX: false #枚举key类似
      WRITE_NULL_MAP_VALUES: false #是否输出map中的空entry(此特性已过期，请使用JsonInclude注解)
      WRITE_EMPTY_JSON_ARRAYS: true # 对象属性值是空集合是否输出空json数组
      WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED: false #是否将单个元素的集合展开，（即：去除数组符号"[]"）
      WRITE_BIGDECIMAL_AS_PLAIN: false #是否调用BigDecimal#toPlainString()输出
      WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS: #将timestamp输出为纳秒
      ORDER_MAP_ENTRIES_BY_KEYS: false #map序列化后，是否用key对其排序
      EAGER_SERIALIZER_FETCH: true #是否马上获取序列化器
      USE_EQUALITY_FOR_OBJECT_ID: false #是否使用objectId比较是否相等（在ORM框架Hibernate中有应用）

    # 枚举类DeserializationFeature中的枚举属性为key，值为boolean设置jackson反序列化特性,具体key请看DeserializationFeature源码
    deserialization:
      USE_BIG_DECIMAL_FOR_FLOATS: false #将浮点数反序列化为BIG_DECIMAL
      USE_BIG_INTEGER_FOR_INTS: false #将整数反序列化为BIG_INTEGER
      USE_LONG_FOR_INTS: false #将整型反序列化为长整
      USE_JAVA_ARRAY_FOR_JSON_ARRAY: false #无明确类型时，是否将json数组反序列化为java数组（若是true，就对应Object[] ,反之就是List<?>）
      FAIL_ON_UNKNOWN_PROPERTIES: false # 常用,json中含pojo不存在属性时是否失败报错,默认true
      FAIL_ON_NULL_FOR_PRIMITIVES: false #将null反序列化为基本数据类型是否报错
      FAIL_ON_NUMBERS_FOR_ENUMS: false #用整数反序列化为枚举是否报错
      FAIL_ON_INVALID_SUBTYPE: false #找不至合适的子类否报错 （如注解JsonTypeInfo指定的子类型）
      FAIL_ON_READING_DUP_TREE_KEY: false #出现重复的json字段是否报错
      FAIL_ON_IGNORED_PROPERTIES: false #如果json中出现了java实体字段中已显式标记应当忽略的字段，是否报错
      FAIL_ON_UNRESOLVED_OBJECT_IDS: true #如果反序列化发生了不可解析的ObjectId是否报错
      FAIL_ON_MISSING_CREATOR_PROPERTIES: false #如果缺少静态工厂方法的参数是否报错（false,则使用null代替需要的参数）
      FAIL_ON_NULL_CREATOR_PROPERTIES: false #将空值绑定到构造方法或静态工厂方法的参数是否报错
      FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY: false #注解JsonTypeInfo.As#EXTERNAL_PROPERTY标记的属性缺失，是否报错
      FAIL_ON_TRAILING_TOKENS: false #出现尾随令牌是否报错（如果是true,则调用JsonParser#nextToken，检查json的完整性）
      WRAP_EXCEPTIONS: true #是否包装反序列化出现的异常
      ACCEPT_SINGLE_VALUE_AS_ARRAY: true #反序列化时是否将一个对象封装成单元素数组
      UNWRAP_SINGLE_VALUE_ARRAYS: false #反序列化时是否将单元素数组展开为一个对象
      UNWRAP_ROOT_VALUE: false #是否将取消根节点的包装
      ACCEPT_EMPTY_STRING_AS_NULL_OBJECT: false #是否将空字符("")串当作null对象
      ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT: false #是否接受将空数组("[]")作为null
      ACCEPT_FLOAT_AS_INT: true #是否接受将浮点数作为整数
      READ_ENUMS_USING_TO_STRING: false #按照枚举toString()方法读取，（false则按枚举的name()方法读取）
      READ_UNKNOWN_ENUM_VALUES_AS_NULL: false #读取到未知的枚举当作null
      READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE: false #读取到未知的枚举,将其当作被JsonEnumDefaultValue注解标记的枚举
      READ_DATE_TIMESTAMPS_AS_NANOSECONDS: true #将时间戳视为纳秒(false,则视为毫秒)
      ADJUST_DATES_TO_CONTEXT_TIME_ZONE: true #反序列化是否会适应DeserializationContext#getTimeZone()提供的时区 （此特性仅对java8的时间/日期有效）
      EAGER_DESERIALIZER_FETCH: true  #是否马上获取反序列化器
    # 枚举类MapperFeature中的枚举属性为key，值为boolean设置jackson ObjectMapper特性
    # ObjectMapper在jackson中负责json的读写、json与pojo的互转、json tree的互转,具体特性请看MapperFeature,常规默认即可
    mapper:
      USE_ANNOTATIONS: true #是否使用注解自省（检查JsonProperties这些）
      # 使用getter取代setter探测属性，这是针对集合类型，可以直接修改集合的属性
      USE_GETTERS_AS_SETTERS: true #默认false
      PROPAGATE_TRANSIENT_MARKER: false #如何处理transient字段，如果true(不能访问此属性) ，若是false则不能通过字段访问（还是可以使用getter和setter访问）
      AUTO_DETECT_CREATORS: true #是否自动检测构造方法或单参且名为valueOf的静态工厂方法
      AUTO_DETECT_FIELDS: true #是否自动检测字段 （若true,则将所有public实例字段视为为属性）
      AUTO_DETECT_GETTERS: true #确定是否根据标准 Bean 命名约定自动检测常规“getter”方法的（不包括is getter）
      AUTO_DETECT_IS_GETTERS: true #确定是否根据标准 Bean 命名约定自动检测“is getter”方法
      AUTO_DETECT_SETTERS: false # 确定是否根据标准 Bean 命名约定自动检测“setter”方法
      REQUIRE_SETTERS_FOR_GETTERS: false #getter方法必需要有对应的setter或字段或构造方法参数，才能视为一个属性
      ALLOW_FINAL_FIELDS_AS_MUTATORS: true #是否可以修改final成员字段
      INFER_PROPERTY_MUTATORS: true #是否能推断属性，(即使用字段和setter是不可见的，但getter可见即可推断属性)
      INFER_CREATOR_FROM_CONSTRUCTOR_PROPERTIES: true #是否自动推断ConstructorProperties注解
      CAN_OVERRIDE_ACCESS_MODIFIERS: true #调用AccessibleObject#setAccessible设为true .将原来不可见的属性，变为可见
      OVERRIDE_PUBLIC_ACCESS_MODIFIERS: true #对所有的属性调用AccessibleObject#setAccessible设为true .（即使用是公共的）
      USE_STATIC_TYPING: false #序列化使用声明的静态类型还是动态类型  JsonSerialize#typing注解可覆盖它
      USE_BASE_TYPE_AS_DEFAULT_IMPL: false # 反序列化是否使用基本类作为默实现 @JsonTypeInfo.defaultImpl
      DEFAULT_VIEW_INCLUSION: true #没有JsonView注解标记的属性是否会被包含在json序列化视图中
      SORT_PROPERTIES_ALPHABETICALLY: false #按字母表顺序序列化字段（若false，按字段声明的顺序）
      ACCEPT_CASE_INSENSITIVE_PROPERTIES: false #反序列化属性时不区分大小写 （true时，会影响性能）
      ACCEPT_CASE_INSENSITIVE_ENUMS: false #枚举反序列化不区别大小写
      ACCEPT_CASE_INSENSITIVE_VALUES: false #允许解析一些枚举的基于文本的值类型但忽略反序列化值的大小写 如日期/时间类型反序列化器
      USE_WRAPPER_NAME_AS_PROPERTY_NAME: false # 使用包装器名称覆盖属性名称 AnnotationIntrospector#findWrapperName指定的
      USE_STD_BEAN_NAMING: false # 是否以强制与 Bean 名称自省严格兼容的功能，若开启后（getURL()）变成URL （jackson默认false, url）
      ALLOW_EXPLICIT_PROPERTY_RENAMING: false #是否允许JsonProperty注解覆盖PropertyNamingStrategy
      ALLOW_COERCION_OF_SCALARS: true # 是否允许强制使用文本标题 ，即将字符串的"true"当作布尔的true ,字符串的"1.0"当作"double"
      IGNORE_DUPLICATE_MODULE_REGISTRATIONS: true #如果模块相同（Module#getTypeId()返回值相同），只有第一次能会真正调用注册方法
      IGNORE_MERGE_FOR_UNMERGEABLE: true #在合并不能合并的属性时是否忽略错误
      BLOCK_UNSAFE_POLYMORPHIC_BASE_TYPES: false #阻止不安全的基类（如Object Closeable Cloneable AutoCloseable Serializable）
    parser:
      AUTO_CLOSE_SOURCE: true #是否自动关闭不属于解析器的底层输入流
      ALLOW_COMMENTS: false #是否允许json注解（Json规范是不能加注释的，但这里可以配置）
      ALLOW_YAML_COMMENTS: false #是否允许出现yaml注释
      ALLOW_UNQUOTED_FIELD_NAMES: false #是否允许出现字段名不带引号
      ALLOW_SINGLE_QUOTES: false # 是否允许出现单引号,默认false
      ALLOW_UNQUOTED_CONTROL_CHARS: false #是否允许出现未加转义的控制字符
      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: false #是否允许对所有字符都可加反斜杠转义
      ALLOW_NUMERIC_LEADING_ZEROS: false #是否允许前导的零 000001
      ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS: false #是否允许前导的小点数 如 ".04314"会被解析成"0.04314"
      ALLOW_NON_NUMERIC_NUMBERS: false #是否允许NaN型的浮点数 （"INF"当作正无穷  "-INF"当作负无穷 "NaN"非数字，类型于除数为0）
      ALLOW_MISSING_VALUES: false # 是否允许json数组中出现缺失值 （如["value1",,"value3",]将被反序列化为["value1", null, "value3", null]）
      ALLOW_TRAILING_COMMA: false # 是否允许json尾部有逗号 （如{"a": true,}）
      STRICT_DUPLICATE_DETECTION: false #是否启用严格的字段名重复检查（开启后会增加20-30%左右的性能开销）
      IGNORE_UNDEFINED: false #属性定义未找到是否报错（这不是针对json,是针对Avro, protobuf等需要Schema的格式）
      INCLUDE_SOURCE_IN_LOCATION: false #是否包含其源信息（如总字节数，总字符数 行号 列号 ）
    generator:
      AUTO_CLOSE_TARGET: true #是否自动关闭不属于生成器的底层输出流
      AUTO_CLOSE_JSON_CONTENT: true #是否自动补全json(当有不匹配的JsonToken#START_ARRAY JsonToken#START_OBJECT时)
      FLUSH_PASSED_TO_STREAM: true #是否刷新generator
      QUOTE_FIELD_NAMES: true #是否为字段名添加引号
      QUOTE_NON_NUMERIC_NUMBERS: true #对于NaN浮点数是否加引号
      ESCAPE_NON_ASCII: false #非ASCII码是否需要转义
      WRITE_NUMBERS_AS_STRINGS: false #将数字当作字符串输出 （防止Javascript长度限制被截断）
      WRITE_BIGDECIMAL_AS_PLAIN: false #按BigDecimal的toPlainString()输出
      STRICT_DUPLICATE_DETECTION: false #是否启用严格的字段名重复检查
      IGNORE_UNKNOWN: false #属性定义未找到是否报错（这不是针对json,是针对Avro, protobuf等需要Schema的格式）
```

### 4、自定义序列化

#### 4.1、使用自定义JsonSerializer

Jackson库中有一个抽象类`JsonSerializer`，其中要实现一个抽象方法`serialize`。

```Java
public abstract void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException;
```

我们定义自己的`serialize`并继承`JsonSerializer`。

```Java
public class CarSerializer extends JsonSerializer<Car> {
    @Override
    public void serialize(Car car, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("producer", car.getBrand());
        jsonGenerator.writeNumberField("doorCount", car.getDoors());
        jsonGenerator.writeEndObject();
    }
}
```

#### 4.2、注册序列化器

1. 通过`SimpleModule`的`addSerializer`方法将`CarSerializer`进行注册。

   ```java
   //通过simpleModule进行注册
   SimpleModule simpleModule = new SimpleModule();
   simpleModule.addSerializer(Car.class, new CarSerializer());
   ObjectMapper objectMapper = new ObjectMapper();
   //注册simpleModule
   objectMapper.registerModule(simpleModule);
   Car car = objectMapper.writeValueAsString(json, Car.class);
   ```

2. 通过注解进行注册。

   ```Java
   @JsonSerialize(using = CarSerializer.class)
   public class Car {
       private String brand;
       private Integer doors;
       private Boolean isCheckout;
   }
   ```

   ```Java
   Car car = new Car();
   car.setBrand("Mercedes");
   car.setDoors(5);
   String carJson = objectMapper.writeValueAsString(car);
   ```

### 5、自定义反序列化

#### 5.1、使用自定义JsonDeserializer

Jackson库中有一个抽象类`JsonDeserializer`，其中要实现一个抽象方法`deserialize`。

```java
public abstract T deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JacksonException;
```

我们定义自己的`deserializer`并继承`JsonDeserializer`。

```Java
public class OrderStatusDeserializer extends JsonDeserializer<Order.OrderStatus> {
    @Override
    public Order.OrderStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        //解析Json
        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
        Set<String> fieldSet = new HashSet<>();
        //遍历Json字符串里面存在属性，并存在set中
        Iterator<String> iterator = treeNode.fieldNames();
        while(iterator.hasNext()) {
            fieldSet.add(iterator.next());
        }
        //创建实例
        Order.OrderStatus orderStatus = new Order.OrderStatus();
        //获取Class实例
        Class<Order.OrderStatus> orderStatusClass = Order.OrderStatus.class;
        //获取Class的所有属性
        Field[] declaredFields = orderStatusClass.getDeclaredFields();
        for(Field field : declaredFields) {
            //设置属性可写
            field.setAccessible(true);
            //如果JSON字符串存在该属性则设置true
            if(fieldSet.contains(field.getName())) {
                try {
                    //注入值
                    field.set(orderStatus,true);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            //否则设置false
            else {
                try {
                    //注入值
                    field.set(orderStatus,false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return orderStatus;
    }
}

```

#### 5.2、注册反序列化器

1. 通过`SimpleModule`的`addDeserializer`方法将`OrderStatusDeserializer `进行注册。

   ```Java
   //通过simpleModule进行注册
   SimpleModule simpleModule = new SimpleModule();
   simpleModule.addDeserializer(Order.OrderStatus.class, new OrderStatusDeserializer());
   ObjectMapper objectMapper = new ObjectMapper();
   //注册simpleModule
   objectMapper.registerModule(simpleModule);
   Order.OrderStatus orderStatus = objectMapper.readValue(json, Order.OrderStatus.class);
   ```

2. 通过注解进行注册。

   ```java
   @JsonDeserialize(using = OrderStatusDeserializer.class)
   public static class OrderStatus {
       private Boolean isDelayed;
       private Boolean isBulk;
       private Boolean isCheckout;
       private Boolean isAllocated;
   }
   ```

   ```java
   String json="{\"isAllocated\":true,\"isCheckout\":true}";
   ObjectMapper objectMapper = new ObjectMapper();
   //使用注解注册后直接调用即可
   Order.OrderStatus orderStatus = objectMapper.readValue(json, Order.OrderStatus.class);
   ```

### 6、Jackson日期序列化

默认情况下，Jackson会将`java.util.Date`对象序列化为其`long`型的值，该值是自1970年1月1日以来的毫秒数。

Jackson支持将日期格式化为字符串，通过在`ObjectMapper`上设置`SimpleDateFormat`来指定要使用的确切Jackson日期格式。

```java
SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
ObjectMapper objectMapper = new ObjectMapper();
objectMapper.setDateFormat(dateFormat);
String output2 = objectMapper.writeValueAsString(transaction);
```

### 7、Jackson JsonNode 树模型

Jackson具有内置的树模型，可用于表示JSON对象。 如果不知道接收到的JSON的格式，或者由于某种原因而不能创建一个类来表示它，那么就要用到Jackson的树模型。 如果需要在使用或转化JSON之前对其进行操作，也需要被用到Jackson树模型。 所有这些情况在数据流场景中都很常见。

#### 7.1、操作Jackson树模型访问JSON字段、数组和嵌套对象

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
        "  \"nestedObject\" : { \"field\" : \"value\" } }";

ObjectMapper objectMapper = new ObjectMapper();
try {
    JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);
    
    JsonNode brandNode = jsonNode.get("brand");
    String brand = brandNode.asText();
    System.out.println("brand = " + brand);

    JsonNode doorsNode = jsonNode.get("doors");
    int doors = doorsNode.asInt();
    System.out.println("doors = " + doors);

    JsonNode array = jsonNode.get("owners");
    JsonNode jsonNode = array.get(0);
    String john = jsonNode.asText();
    System.out.println("john  = " + john);

    JsonNode child = jsonNode.get("nestedObject");
    JsonNode childField = child.get("field");
    String field = childField.asText();
    System.out.println("field = " + field);
} catch (IOException e) {
    e.printStackTrace();
}
```

#### 7.2、Java 对象转 JsonNode

```java
ObjectMapper objectMapper = new ObjectMapper();
Car car = new Car();
JsonNode carJsonNode = objectMapper.valueToTree(car);
```

#### 7.3、JsonNode 转 Java对象

```java
ObjectMapper objectMapper = new ObjectMapper();
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
JsonNode carJsonNode = objectMapper.readTree(carJson);
Car car = objectMapper.treeToValue(carJsonNode);
```

#### 7.3、在路径中获取JsonNode字段

JsonNode有一个`at()`的特殊方法。 `at()`方法可以从给定的JsonNode为根的任何位置访问JSON字段。 假设JSON结构如下所示：

```json
{
  "identification" :  {
        "name" : "James",
        "ssn: "ABC123552"
    }
}
```

将此JSON解析为JsonNode，使用`at()`方法访问名称字段，请注意JSON路径表达式必须以斜杠字符（`/`字符）开头。返回一个JsonNode它表示请求的JSON字段，如果没有节点与给定的路径表达式匹配，则将返回null。另外你可以给他定义一个默认值`Default`如下所示：

```java
JsonNode nameNode = jsonNode.at("/identification/name");
String name = nameNode.get("name").asText("Default");
```

#### 7.4、遍历JsonNode字段

JsonNode类具有一个名为`fieldNames()`的方法，该方法返回一个`Iterator`，可以迭代JsonNode的所有字段名称。之后在使用字段名称来获取字段值。

```java
Iterator<String> fieldNames = jsonNode.fieldNames();
while(fieldNames.hasNext()) {
    String fieldName = fieldNames.next();
    JsonNode field = jsonNode.get(fieldName);
}
```

### 8、Jackson ObjectNode

JsonNode类是不可变的，如果要设置属性值和子JsonNode实例等操作，无法直接使用JsonNode来实现。而ObjectNode实例它提供了这些操作，该实例是JsonNode的子类。 

#### 8.1、创建ObjectNode

通过`createObjectNode()`方法创建ObjectNode的示例：

```java
ObjectMapper objectMapper = new ObjectMapper();
ObjectNode objectNode = objectMapper.createObjectNode();
```

#### 8.2、Set ObjectNode 字段

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
objectNode.set("name", objectMapper.readTree(carJson));
```

#### 8.3、Put ObjectNode 字段

```java
objectNode.put("name", "zhangsan");
```

#### 8.4、Remove ObjectNode 字段

```java
objectNode.remove("name");
```

### 9、Jackson JsonParser

使用JsonParser它来解析JSON，将JSON分解为一系列JsonToken，JsonToken它有一组常量令牌，可以使用这组令牌来判断当前的JsonToken是什么类型的令牌。

```java
START_OBJECT
END_OBJECT
START_ARRAY
END_ARRAY
FIELD_NAME
VALUE_EMBEDDED_OBJECT
VALUE_FALSE
VALUE_TRUE
VALUE_NULL
VALUE_STRING
VALUE_NUMBER_INT
VALUE_NUMBER_FLOAT
```

#### 9.1、创建JsonParser

首先需要一个JsonFactory用于创建JsonParser实例。 JsonFactory类包含几个`createParser()`方法，每个方法都使用不同的JSON源作为参数。

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
JsonFactory factory = new JsonFactory();
JsonParser parser = factory.createParser(carJson);
```

#### 9.2、遍历JsonParser

通过JsonParser的`isClosed()`方法来判断是否存在令牌，返回`false`代表JSON源中仍然存在令牌。使用JsonParser的`nextToken()`获取一个JsonToken。

```java
String carJson = "{ \"brand\" : \"Mercedes\", \"doors\" : 5 }";
JsonFactory factory = new JsonFactory();
JsonParser parser = factory.createParser(carJson);
Car car = new Car();
while(!parser.isClosed()) {
    JsonToken jsonToken = parser.nextToken();
    if(JsonToken.FIELD_NAME.equals(jsonToken)) {
        // 返回当前字段名称
        String fieldName = parser.getCurrentName();
        jsonToken = parser.nextToken();
        if("brand".equals(fieldName)) {
            car.setBrand = parser.getValueAsString();
        } else if ("doors".equals(fieldName)) {
            car.setDoors = parser.getValueAsInt();
        }
    }
}
```

### 10、JsonGenerator 生成 JSON 数据

首先创建JsonFactory实例，通过`createGenerator()`方法得到JsonGenerator的实例，提供了有关生成的JSON写入何处的参数，可以是File对象或是OutputStream流等。第二个参数则是生成JSON时使用的字符编码。最后用`close()`方法关闭资源。

```java
JsonFactory factory = new JsonFactory();
JsonGenerator generator = factory.createGenerator(new File("data/output.json"), JsonEncoding.UTF8);
generator.writeStartObject();
generator.writeStringField("brand", "Mercedes");
generator.writeNumberField("doors", 5);
generator.writeEndObject();
generator.close();
```



### 参考文献

【1】[Jackson API 详细汇总 与 使用介绍、格式化日期请求与响应](https://blog.csdn.net/wangmx1993328/article/details/88598625)

【2】[Jackson配置大全](https://blog.csdn.net/Xiaowu_First/article/details/123846121)

【3】[jackson学习之三：常用API操作](https://blog.csdn.net/boling_cavalry/article/details/108192174)

【4】[jackson自定义反序列化器JsonDeserializer](https://blog.csdn.net/weixin_43335392/article/details/124864390)





















































