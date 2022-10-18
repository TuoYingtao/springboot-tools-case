## JMH简介

[JMH(Java Microbenchmark Harness)](https://openjdk.org/projects/code-tools/jmh/)是[OpenJDK](https://openjdk.org/)团队开发的一款代码微基准测试的工具，主要是基于方法层面的基准测试，精度甚至可以达到纳秒级。适用于 JAVA 以及其他基于 JVM 的语言。与 Apache JMeter 不同，[JMH](https://openjdk.org/projects/code-tools/jmh/) 测试的对象可以是任一方法，颗粒度更小，而不仅限于 REST API，当你定位到热点方法，希望进一步优化方法性能的时候，就可以使用 [JMH](https://openjdk.org/projects/code-tools/jmh/) 对优化的结果进行量化的分析。[JMH](https://openjdk.org/projects/code-tools/jmh/) 比较典型的应用场景如下：

1. 想准确地知道某个方法需要执行多长时间，以及执行时间和输入之间的相关性
2. 对比接口不同实现在给定条件下的吞吐量
3. 查看多少百分比的请求在多长时间内完成

## Maven 依赖

```xml
<!-- JMH 基准测试 -->
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.35</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.35</version>
</dependency>
```

对于一些小测试，直接写一个 main 函数手动执行就好了。对于大型的测试，需要测试的时间比较久、线程数比较多，加上测试的服务器需要，一般要放在 Linux 服务器里去执行。JMH 官方提供了生成 jar 包的方式来执行，我们需要在 maven 里增加一个 plugin，具体配置如下：

```xml
<plugins>
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-shade-plugin</artifactId>
        <version>3.2.4</version>
        <executions>
            <execution>
                <phase>package</phase>
                <goals>
                    <goal>shade</goal>
                </goals>
                <configuration>
                    <finalName>jmh-benchmark-tools</finalName>
                    <transformers>
                        <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                            <mainClass>org.openjdk.jmh.Main</mainClass>
                        </transformer>
                        <transformer implementation="org.apache.maven.plugins.shade.resource.ServicesResourceTransformer"/>
                    </transformers>
                    <filters>
                        <filter>
                            <artifact>*:*</artifact>
                            <excludes>
                                <exclude>META-INF/*.SF</exclude>
                                <exclude>META-INF/*.DSA</exclude>
                                <exclude>META-INF/*.RSA</exclude>
                            </excludes>
                        </filter>
                    </filters>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

接着执行 maven 的命令生成可执行 jar 包并执行：

```shell
mvn clean install
java -jar target/jmh-benchmark-tools.jar ArrayListAndLinkedListBenchMark
```

## 注解的使用

```java
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 4, time = 100, timeUnit = TimeUnit.MILLISECONDS, batchSize = 10)
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS, batchSize = 10)
@Param({"10", "30", "60", "100"})
@Setup(Level.Trial)
@Benchmark
@TearDown(Level.Trial)
```

### @BenchmarkMode

用来配置 `Mode` 选项，可用于类或者方法上，这个注解的 value 是一个数组，可以把几种 `Mode` 集合在一起执行，如：`@BenchmarkMode({Mode.SampleTime, Mode.AverageTime})`，还可以设置为 `Mode.All`，即全部执行一遍。

1. `Throughput`：整体吞吐量，每秒执行了多少次调用，单位为 `ops/time`
2. `AverageTime`：用的平均时间，每次操作的平均时间，单位为 `time/op`
3. `SampleTime`：随机取样，统计每个响应时间范围内的响应次数，比如：`0-1ms  3次`；`1-2ms  5次`，最后输出取样结果的分布
4. `SingleShotTime`：跳过预热阶段，只运行一次，往往同时把 `Warmup` 次数设为 0，用于测试冷启动时的性能
5. `All`：上面的所有模式都执行一次

### @OutputTimeUnit(TimeUnit.MILLISECONDS)

为统计结果的时间单位，可用于类或者方法注解，默认以秒做单位，这里是以毫秒为单位。

### @State

可以指定一个对象的作用范围，JMH 根据 `Scope` 来进行实例化和共享操作，可以被继承使用，如果父类定义了该注解，子类则无需定义。`Scope` 取值范围：

1. `Scope.Thread`：默认的 State，每个测试线程分配一个实例。
2. `Scope.Benchmark`：所有测试线程共享一个实例，测试有状态实例在多线程共享下的性能
3. `Scope.Group`：同一个线程在同一个 group 里共享实例

### @Fork(2)

进行 fork 的次数，可用于类或者方法上。如果 fork 数是 2 的话，则 JMH 会 fork 出两个进程来进行测试。

### @Warmup

预热所需要配置的一些基本测试参数，可用于类或者方法上。一般前几次进行程序测试的时候都会比较慢，所以要让程序进行几轮预热，保证测试的准确性。参数如下所示：

1. `iterations`：预热的次数
2. `time`：每次预热的时间
3. `timeUnit`：时间的单位，默认秒
4. `batchSize`：批处理大小，每次操作调用几次方法

> **为什么需要预热？**因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译为机器码，从而提高执行速度，所以为了让 benchmark 的结果更加接近真实情况就需要进行预热。

### @Measurement

实际调用方法所需要配置的一些基本测试参数，可用于类或者方法上，参数和 `@Warmup` 相同。

### @Param

指定某项参数的多种情况，特别适合用来测试一个函数在不同的参数输入的情况下的性能，只能作用在字段上，使用该注解必须定义 `@State` 注解。

### @Benchmark

方法注解，表示该方法是需要进行 benchmark 的对象，运行在独立的进程中互不干涉。

### @Setup

方法注解，会在执行 benchmark 之前被执行，主要用于初始化。类似于 junit 的`@Before`，用于方法的调用时机。通过 Level 控制，`Level`取值范围如下：

1. `Trial`：默认配置。全部 @benchmark 运行(一组迭代)之前
2. `Iteration`：一次迭代之前
3. `Invocation`：每个方法调用之前

### @TearDown

方法注解，会在所有 benchmark 执行结束以后执行，主要用于资源的回收等。类似于 junit 的`@After`。Level 取值范围与 `@Setup` 相对的

## 无注解使用

使用注解与不使用注解其实都是一样，只不过使用注解更加方便。在运行时，注解配置被用于解析生成`BenchmarkListEntry`配置类实例，而在代码中使用`Options`配置也是被解析成一个个`BenchmarkListEntry`配置类实例（每个方法对应一个）。

非注解方式我们可以使用`OptionsBuilder`构造一个`Options`，例如，非注解方式实现上面的例子。

```java
public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
            .include(ArrayListAndLinkedListBenchMark.class.getSimpleName())
        	.exclude("testBenchMark")
            .forks(2)
            .threads(2)
            .timeUnit(TimeUnit.NANOSECONDS)
            .warmupIterations(5)
            .warmupTime(TimeValue.seconds(1))
            .measurementIterations(5)
            .measurementTime(TimeValue.seconds(1))
            .mode(Mode.AverageTime)
            .build();
    new Runner(opt).run();
}
```

- `include`：导入一个基准测试类。调用方法传递的是类的简单名称，不含包名。
- `exclude`：排除哪些方法。默认 JMH 会为 `include` 导入的类的每个`public`方法都生成一个`BenchmarkListEntry`配置类实例，也就是把每个`public`方法都当成是基准测试方法，这时我们就可以使用`exclude`排除不需要参与基准测试的方法。例如本例中使用`exclude`排除了`testBenchMark`方法。

## 日志的解读

```shell
// 开始测试
# JMH version: 1.35
# VM version: JDK 1.8.0_261, Java HotSpot(TM) 64-Bit Server VM, 25.261-b12
# VM invoker: D:\JDK\jdk-8u261\jre\bin\java.exe
# VM options: -javaagent:D:\Util\intelliJ IDEA\IntelliJ IDEA 2020.3\lib\idea_rt.jar=51799:D:\Util\intelliJ IDEA\IntelliJ IDEA 2020.3\bin -Dfile.encoding=UTF-8
# Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
// 预热运行四次
# Warmup: 4 iterations, 10 s each
// 性能测试10次 
# Measurement: 10 iterations, 10 s each
// 超时时间10分钟
# Timeout: 10 min per iteration
// 线程数量为1
# Threads: 1 thread, will synchronize iterations
// 统计方法调用整体吞吐量，每秒执行了多少次调用
# Benchmark mode: Throughput, ops/time
// 本次测试的方法名
# Benchmark: com.tuoyingtao.benchmark.ArrayListAndLinkedListBenchMark.arrayTraverse
// 测试参数
# Parameters: (num = 100)

# Run progress: 37.50% complete, ETA 00:23:27
// 第 1个 fork，总 2个 fork
# Fork: 1 of 2
// 第一次预热
# Warmup Iteration   1: 325276.372 ops/ms
// 第二次预热
# Warmup Iteration   2: 335194.323 ops/ms
// 第三次预热
# Warmup Iteration   3: 336195.489 ops/ms
// 第四次预热
# Warmup Iteration   4: 335373.143 ops/ms
// 执行10次的吞吐量
Iteration   1: 339979.444 ops/ms
Iteration   2: 334788.695 ops/ms
Iteration   3: 344005.606 ops/ms
Iteration   4: 344150.547 ops/ms
Iteration   5: 343698.807 ops/ms
Iteration   6: 341346.380 ops/ms
Iteration   7: 339597.449 ops/ms
Iteration   8: 340427.058 ops/ms
Iteration   9: 338298.379 ops/ms
Iteration  10: 339849.115 ops/ms

# Run progress: 43.75% complete, ETA 00:21:06
// 第 2个 fork，总 2个 fork
# Fork: 2 of 2
# Warmup Iteration   1: 347596.558 ops/ms
# Warmup Iteration   2: 335110.919 ops/ms
# Warmup Iteration   3: 321431.837 ops/ms
# Warmup Iteration   4: 326107.039 ops/ms
Iteration   1: 344451.064 ops/ms
Iteration   2: 339294.829 ops/ms
Iteration   3: 327920.802 ops/ms
Iteration   4: 340415.482 ops/ms
Iteration   5: 322474.845 ops/ms
Iteration   6: 327164.693 ops/ms
Iteration   7: 334366.627 ops/ms
Iteration   8: 337174.409 ops/ms
Iteration   9: 329190.367 ops/ms
Iteration  10: 335540.484 ops/ms


Result "com.tuoyingtao.benchmark.ArrayListAndLinkedListBenchMark.arrayTraverse":
  337206.754 ±(99.9%) 5411.385 ops/ms [Average]
  (min, avg, max) = (322474.845, 337206.754, 344451.064), stdev = 6231.759  // 执行的最小、平均、最大、误差值
  CI (99.9%): [331795.369, 342618.139] (assumes normal distribution)
  
  
  // .......省略其它的 Benchmark 日志


# Run complete. Total time: 00:37:30

REMEMBER: The numbers below are just data. To gain reusable insights, you need to follow up on
why the numbers are the way they are. Use profilers (see -prof, -lprof), design factorial
experiments, perform baseline and negative tests that provide experimental control, make sure
the benchmarking environment is safe on JVM/OS/HW level, ask for reviews from the domain experts.
Do not assume the numbers tell you what you want them to tell.

// 测试结果对比 (一般只需要关注这下面的东西)

Benchmark                                       (num)   Mode  Cnt       Score      Error   Units
ArrayListAndLinkedListBenchMark.arrayTraverse      10  thrpt   20  335352.531 ± 9754.832  ops/ms
ArrayListAndLinkedListBenchMark.arrayTraverse      30  thrpt   20  338728.462 ± 4322.489  ops/ms
ArrayListAndLinkedListBenchMark.arrayTraverse      60  thrpt   20  333603.292 ± 5564.698  ops/ms
ArrayListAndLinkedListBenchMark.arrayTraverse     100  thrpt   20  337206.754 ± 5411.385  ops/ms
ArrayListAndLinkedListBenchMark.linkedTraverse     10  thrpt   20   43430.608 ± 1147.719  ops/ms
ArrayListAndLinkedListBenchMark.linkedTraverse     30  thrpt   20   12068.251 ±  183.461  ops/ms
ArrayListAndLinkedListBenchMark.linkedTraverse     60  thrpt   20    2866.435 ±   59.392  ops/ms
ArrayListAndLinkedListBenchMark.linkedTraverse    100  thrpt   20     820.684 ±   11.399  ops/ms

Process finished with exit code 0

```

报告很长，因为这里的n有四种情况，然后有两个 `@Benchmark` 方法，因此会进行8次测试。大多数情况只需要关注最下面的结果。

可以结合 Score 和 Unit 这两列，看到方法的效率。这里显然 `arrayTraverse` 的效率比 `listTraverse` 的高很多，因为 `Unit` 单位是 `ops/ms`，即单位时间内执行的操作数。所以显然在遍历的时候，ArrayList 的效率是比 LinkedList 高的。

## JMH 陷阱

在使用 JMH 的过程中，一定要避免一些陷阱。比如 JIT 优化中的死码消除，比如以下代码：

```java
@Benchmark
public void testStringAdd(Blackhole blackhole) {
    String a = "";
    for (int i = 0; i < length; i++) {
        a += i;
    }
}
```

JVM 可能会认为变量 `a` 从来没有使用过，从而进行优化把整个方法内部代码移除掉，这就会影响测试结果。JMH 提供了两种方式避免这种问题，一种是将这个变量作为方法返回值 return a，一种是通过 Blackhole 的 consume 来避免 JIT 的优化消除。其他陷阱还有常量折叠与常量传播、永远不要在测试中写循环、使用 Fork 隔离多个测试方法、方法内联、伪共享与缓存行、分支预测、多线程测试等。[感兴趣的可以阅读了解全部的陷阱](https://github.com/lexburner/JMH-samples)。

## 参考文献

【1】[https://www.ibm.com/developerworks/cn/java/j-benchmark1.html](https://www.ibm.com/developerworks/cn/java/j-benchmark1.html?spm=a2c6h.12873639.article-detail.6.712b1077UpP4Fw)

【2】[http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/?spm=a2c6h.12873639.article-detail.7.712b1077UpP4Fw)

【3】[JMH - Java 代码性能测试的终极利器、必须掌握](https://developer.aliyun.com/article/885895#slide-5)

【4】[Java 性能调优必备利器—JMH ](https://www.cnblogs.com/msjhw/p/15718084.html)

【5】[使用JMH做Benchmark基准测试](https://www.cnblogs.com/fightfordream/p/9353002.html)

【6】[github openjdk jmh](https://github.com/openjdk/jmh)

【7】[基准测试框架JMH使用详解](https://blog.csdn.net/ZYC88888/article/details/113741316)



























