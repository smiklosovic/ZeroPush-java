/*
 * Copyright (c) 2015 Symmetric Infinity LLC
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.zeropush.configuration;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.configuration.ZeroPushConfiguration.Proxy;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class ZeroPushProxyTestCase
{
    @Test
    public void proxySettingPerThread() throws InterruptedException, ExecutionException
    {
        Callable<Boolean> callable1 = new Callable<Boolean>()
        {
            public Boolean call() throws Exception
            {
                Proxy proxy = new ZeroPushConfiguration.ProxyBuilder()
                    .withHostname("localhost")
                    .withPort(8080)
                    .build();

                ZeroPush.getConfiguration().setProxy(proxy);
                return ZeroPush.getConfiguration().withProxy();
            }
        };

        Callable<Boolean> callable2 = new Callable<Boolean>()
        {
            public Boolean call() throws Exception
            {
                return ZeroPush.getConfiguration().withProxy();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<Boolean> future1 = executorService.submit(callable1);
        Future<Boolean> future2 = executorService.submit(callable2);

        Boolean withProxy1 = future1.get();
        Boolean withProxy2 = future2.get();

        Assert.assertTrue(withProxy1);
        Assert.assertFalse(withProxy2);
    }
}
