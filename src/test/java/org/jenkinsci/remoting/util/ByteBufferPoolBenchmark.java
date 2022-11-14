package org.jenkinsci.remoting.util;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.nio.ByteBuffer;

@State(Scope.Benchmark)
public class ByteBufferPoolBenchmark
{

    @Param({
            "array-global-lock-pool",
            "no-pool"
    })
    public static String POOL_TYPE;

    private ByteBufferPool pool;

    @Setup
    public void setUp() throws Exception
    {
        switch (POOL_TYPE) {
            case "array-global-lock-pool" :
                pool = new SimpleDirectByteBufferPool(16916, Runtime.getRuntime().availableProcessors() * 4);
                break;
            case "no-pool" :
                pool = new DirectByteBufferPool(16916, Runtime.getRuntime().availableProcessors() * 4);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @TearDown
    public void tearDown()
    {
        pool = null;
    }

    @Benchmark
    public void testAcquireRelease()
    {
        ByteBuffer buffer = pool.acquire(8192);
        pool.release(buffer);
    }

    @Benchmark
    public void testAcquireReleaseLargerBuffer()
    {
        ByteBuffer buffer = pool.acquire(8192 * 2);
        pool.release(buffer);
    }

    public static void main(String[] args) throws RunnerException
    {
        Options opt = new OptionsBuilder()
            .include(ByteBufferPoolBenchmark.class.getSimpleName())
            .warmupIterations(3)
            .measurementIterations(3)
            .forks(1)
            .threads(8)
            // .addProfiler(GCProfiler.class)
            .build();

        new Runner(opt).run();
    }
}
