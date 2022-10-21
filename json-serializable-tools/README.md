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

**1.1、JSON 数据转对象**

```json
{"name": "Alex","id": 1,"users":[{"name": "Alex","id": 1},{"name": "Brian","id": 2},{"name": "Charles","id": 3}]}
```

```java
User user = gson.fromJson(userJson, User.class);
```

**1.2、JSON Array 转数组**

```json
[{"name": "Alex","id": 1},{"name": "Brian","id": 2},{"name": "Charles","id": 3}]
```

```java
User[] userArray = gson.fromJson(userJson, User[].class);
```

**1.3、JSON Array 转 List**

```java
List<User> userList = gson.fromJson(userJson, new TypeToken<ArrayList<User>>() {
}.getType());
```

**1.4、Set 转 JSON**

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

**2.1、如何在序列化时允许空值**

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

**3.1、@Since注解**

在Gson中，可以使用@Since注释维护同一对象的多个版本。可以在类，字段以及将来的方法中使用此注释。

当我们为Gson实例配置版本号“ M.N”时，所有标记有版本大于M.N的类字段都将被忽略。

例如，如果我们将Gson配置为版本号“ 1.2”，则所有版本号更高的字段（1.3、1.4…）都将被忽略。

```java
@Since(1.2)
private String email;
```

**3.2、如何使用@Since注解编写版本化的类**

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

**3.3、创建具备版本支持的Gson实例**

要创建使用过@Since注解的Gson实例，需要使用GsonBuilder.setVersion()方法:

```java
Gson gson = new GsonBuilder()
    .setVersion(1.1)
    .create();
```

**3.4、实例**

**3.4.1、序列化**

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

**3.4.2、反序列化**

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

**4.1、@SerializedName注解**

```java
@SerializedName("user_name")
private String userName;
@SerializedName(value = "user_name", alternate = {"user_nameA", "user_nameB"})
private String userName;
```

* `value`序列化时字段名映射。

* `alternate`备用字段映射，在反序列化时不论JSON中字段是`value`还是`alternate`中的值都会被匹配上

**4.2、@Expose注解**

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

**4.2.1、用修饰符排除字段**

`transient`修饰符具有与@Expose相同的效果（serialize = false，deserialize = false）。

```java
private transient String emailAddress;
```

**4.2.2、其它修饰符排除字段**

通过使用GsonBuilder的excludeFieldsWithModifiers()方法，我们可以排除具有某些公共修饰符的字段。

```java
Gson gson = new GsonBuilder()
    .excludeFieldsWithModifiers(Modifier.STATIC)
    .create();
```

**4.2.3、自定义排除策略**

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

**6.1、FieldNamingPolicy.IDENTITY**

使用此命名策略字段名称不变。这个是默认的策略：

```json
{
  "id": 1,
  "first_Name": "Lokesh",
  "lastName": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

**6.2、FieldNamingPolicy.LOWER_CASE_WITH_DASHES**

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用破折号（-）分隔。

```json
{
  "id": 1,
  "first_-name": "Lokesh",
  "last-name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

**6.3、FieldNamingPolicy.LOWER_CASE_WITH_DOTS**

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用点（.）分隔:

```json
{
  "id": 1,
  "first_.name": "Lokesh",
  "last.name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

**6.4、FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES**

Gson会将Java字段名称从其驼峰大小写形式修改为小写的字段名称，其中每个单词都用下划线（_）分隔。

```json
{
  "id": 1,
  "first__name": "Lokesh",
  "last_name": "Gupta",
  "_email": "admin@howtodoinjava.com"
}
```

**6.5、FieldNamingPolicy.UPPER_CAMEL_CASE**

Gson将确保序列化为JSON格式的Java字段名称的第一个“字母”大写：

```json
{
  "Id": 1,
  "First_Name": "Lokesh",
  "LastName": "Gupta",
  "_Email": "admin@howtodoinjava.com"
}
```

**6.6、FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES**

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