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
package com.zeropush.register;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.model.token.TokenGenerator;
import com.zeropush.test.LoadingPropertiesTestCase;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class RegistrationTestCase extends LoadingPropertiesTestCase
{
    private static String DEVICE_TOKEN;

    @BeforeClass
    public static void setup() throws Exception
    {
        DEVICE_TOKEN = TokenGenerator.ANDROID.generate();

        loadProperties();
        ZeroPush.setConfiguration(new ZeroPushConfiguration()); // only for testing purposes
        ZeroPush.getConfiguration().setServerToken(System.getProperty("zeropush.token.server", getProperty("zeropush.token.server")));
        ZeroPush.getConfiguration().setApplicationToken(System.getProperty("zeropush.token.app", getProperty("zeropush.token.app")));
    }

    @Test
    public void registerAndUnRegisterDeviceTest()
    {
        RegisterResponse registerResponse = ZeroPush.registration().register(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, registerResponse.getStatusCode());
        Assert.assertEquals("ok", registerResponse.getMessage().getMessage());

        RegisterResponse unRegisterResponse = ZeroPush.registration().unregister(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, unRegisterResponse.getStatusCode());
        Assert.assertEquals("ok", unRegisterResponse.getMessage().getMessage());
    }
}
