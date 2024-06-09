package org.luvx.coding.common.concurrent;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Fork(1)
@org.openjdk.jmh.annotations.Threads(org.openjdk.jmh.annotations.Threads.MAX)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
public class SingleFlightJmhTest {
    final List<String>   keys = List.of("foo", "bar", "some", "other");
    final SingleFlight   sf1  = new SingleFlight();
    final SingleFlightV2 sf2  = new SingleFlightV2();
    final SingleFlightV3 sf3  = new SingleFlightV3();
    final SingleFlightV4 sf4  = new SingleFlightV4();

    final Supplier<String> a = () -> {
        try {
            // TimeUnit.MICROSECONDS.sleep(100);
        } catch (Throwable ignore) {
        }
        return "a";
    };

    public void exec(BiConsumer<String, Supplier<String>> consumer) {
        for (String key : keys) {
            consumer.accept(key, a);
        }
    }

    @Benchmark
    public void test01() {
        exec(sf1::execute);
    }

    @Benchmark
    public void test02() {
        exec(sf2::execute);
    }

    @Benchmark
    public void test03() {
        exec(sf3::execute);
    }

    @Benchmark
    public void test04() {
        exec(sf4::execute);
    }

    public static void main(String[] args) throws Exception {
        Options options = new OptionsBuilder()
                .include(SingleFlightJmhTest.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(options).run();
    }
}