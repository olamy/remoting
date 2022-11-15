/*
 * The MIT License
 *
 * Copyright (c) 2016, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.remoting.util;

import org.eclipse.jetty.io.ArrayByteBufferPool;

import java.nio.ByteBuffer;

/**
 * Wrapper on no pool
 */
public class JettyByteBufferPool implements ByteBufferPool {

    private ArrayByteBufferPool byteBufferPool;


    /**
     * Constructor.
     * @param minBufferSize the minimum size to create buffers.
     * @param maxPoolSize the maximum buffers to keep in the pool.
     */
    public JettyByteBufferPool(int minBufferSize, int maxPoolSize) {
        // int minCapacity, int factor, int maxCapacity, int maxBucketSize, long maxHeapMemory,
        // long maxDirectMemory, long retainedHeapMemory, long retainedDirectMemory
        this.byteBufferPool =
                new ArrayByteBufferPool(minBufferSize, 16, -1, -1, 0, 0, 0, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ByteBuffer acquire(int size) {
        return byteBufferPool.acquire(size, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release(ByteBuffer buffer) {
        byteBufferPool.release(buffer);
    }

}
