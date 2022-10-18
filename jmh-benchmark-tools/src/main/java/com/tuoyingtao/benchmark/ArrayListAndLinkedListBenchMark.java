package com.tuoyingtao.benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author tuoyingtao
 * @create 2022-10-11 11:24
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 结果所使用的时间单位
@State(Scope.Thread) // 每个测试线程 分配一个实例
@Fork(2) //Fork 进行的数目
@Warmup(iterations = 4, time = 100, timeUnit = TimeUnit.MILLISECONDS, batchSize = 10) // 预热4轮
@Measurement(iterations = 10, time = 100, timeUnit = TimeUnit.MILLISECONDS, batchSize = 10) // 进行10轮测试
public class ArrayListAndLinkedListBenchMark {

    @Param({"10", "30", "60", "100"}) // 定义四个参数，之后会分别对这四个参数进行测试
    private int num;

    private List<Integer> arrayList;
    private List<Integer> linkedList;

    @Setup(Level.Trial) // 初始化方法，在全部Benchmark运行之前进行
    public void init() {
        arrayList = new ArrayList<>(0);
        linkedList = new LinkedList<>();
        for (int i = 0; i < num; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
    }

    @Benchmark
    public void arrayTraverse() {
        for (int i = 0; i < num; i++) {
            arrayList.get(i);
        }
    }

    @Benchmark
    public void linkedTraverse() {
        for (int i = 0; i < num; i++) {
            linkedList.get(i);
        }
    }

    @TearDown(Level.Trial) // 结束方法，在全部Benchmark运行之后进行
    public void  arrayRemove() {
        for (int i = 0; i < num; i++) {
            arrayList.remove(0);
            linkedList.remove(0);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder().include(ArrayListAndLinkedListBenchMark.class.getSimpleName()).build();
        new Runner(options).run();
    }
}
