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
package com.zeropush.proxy;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.configuration.ZeroPushConfiguration.Proxy;
import com.zeropush.test.LoadingPropertiesTestCase;
import com.zeropush.verify.VerifyCredentialsResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class ZeroPushProxyTestCase extends LoadingPropertiesTestCase
{
    private static String SERVER_TOKEN;

    private static HttpProxyServer proxyServer;

    private static int PROXY_SERVER_PORT = 8888;

    @BeforeClass
    public static void setup() throws Exception
    {
        ZeroPush.setConfiguration(new ZeroPushConfiguration()); // only for testing purposes

        loadProperties();
        SERVER_TOKEN = System.getProperty("zeropush.token.server", getProperty("zeropush.token.server"));

        proxyServer = DefaultHttpProxyServer.bootstrap().withPort(PROXY_SERVER_PORT).withAllowLocalOnly(true).start();
    }

    @AfterClass
    public static void teardown()
    {
        proxyServer.stop();
        proxyServer = null;
        FileUtils.deleteQuietly(new File("lib"));
    }

    @Test
    public void verificationViaProxyTest()
    {
        Proxy proxy = new ZeroPushConfiguration.ProxyBuilder()
            .withHostname("127.0.0.1")
            .withPort(8888)
            .build();

        ZeroPush.getConfiguration().setProxy(proxy);

        VerifyCredentialsResponse response = ZeroPush.verification().credentials(SERVER_TOKEN).execute();

        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("server_token", response.getMessage().getAuthTokenType());
        Assert.assertEquals("authenticated", response.getMessage().getMessage());
    }
}
