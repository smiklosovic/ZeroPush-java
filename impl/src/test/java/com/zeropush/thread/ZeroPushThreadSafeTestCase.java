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
package com.zeropush.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.test.LoadingPropertiesTestCase;
import com.zeropush.verify.VerifyCredentialsResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class ZeroPushThreadSafeTestCase extends LoadingPropertiesTestCase
{
    private static String SERVER_TOKEN;
    private static String APP_TOKEN;

    @BeforeClass
    public static void setup() throws Exception
    {
        loadProperties();
        SERVER_TOKEN = System.getProperty("zeropush.token.server", getProperty("zeropush.token.server"));
        APP_TOKEN = System.getProperty("zeropush.token.app", getProperty("zeropush.token.app"));
    }

    @Test
    public void configurationsAreDifferentObjectsTest() throws InterruptedException, ExecutionException
    {
        Callable<ZeroPushConfiguration> callable1 = new Callable<ZeroPushConfiguration>()
        {
            public ZeroPushConfiguration call() throws Exception
            {
                return ZeroPush.getConfiguration();
            }
        };

        Callable<ZeroPushConfiguration> callable2 = new Callable<ZeroPushConfiguration>()
        {
            public ZeroPushConfiguration call() throws Exception
            {
                return ZeroPush.getConfiguration();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<ZeroPushConfiguration> future1 = executorService.submit(callable1);
        Future<ZeroPushConfiguration> future2 = executorService.submit(callable2);

        ZeroPushConfiguration configuration1 = future1.get();
        ZeroPushConfiguration configuration2 = future2.get();

        Assert.assertNotSame(configuration1, configuration2);
    }

    @Test
    public void testZeroPushThreadSafety() throws InterruptedException, ExecutionException
    {
        Callable<VerifyCredentialsResponse> callable1 = new Callable<VerifyCredentialsResponse>()
        {
            public VerifyCredentialsResponse call() throws Exception
            {
                ZeroPush.getConfiguration().setApplicationToken(APP_TOKEN);
                ZeroPush.getConfiguration().preferAppToken();
                return ZeroPush.verification().credentials().execute();
            }
        };

        Callable<VerifyCredentialsResponse> callable2 = new Callable<VerifyCredentialsResponse>()
        {
            public VerifyCredentialsResponse call() throws Exception
            {
                ZeroPush.getConfiguration().setServerToken(SERVER_TOKEN);
                ZeroPush.getConfiguration().preferServerToken();
                return ZeroPush.verification().credentials().execute();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<VerifyCredentialsResponse> futureResponse1 = executorService.submit(callable1);
        Future<VerifyCredentialsResponse> futureResponse2 = executorService.submit(callable2);

        VerifyCredentialsResponse response1 = futureResponse1.get();
        VerifyCredentialsResponse response2 = futureResponse2.get();

        Assert.assertEquals(200, response1.getStatusCode());
        Assert.assertEquals(200, response2.getStatusCode());

        Assert.assertEquals("app_token", response1.getMessage().getAuthTokenType());
        Assert.assertEquals("server_token", response2.getMessage().getAuthTokenType());

        Assert.assertEquals("authenticated", response1.getMessage().getMessage());
        Assert.assertEquals("authenticated", response2.getMessage().getMessage());
    }

}
