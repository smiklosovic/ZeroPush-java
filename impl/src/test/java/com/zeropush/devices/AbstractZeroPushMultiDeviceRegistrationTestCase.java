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
package com.zeropush.devices;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.model.token.TokenGenerator;
import com.zeropush.register.RegisterResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class AbstractZeroPushMultiDeviceRegistrationTestCase
{
    protected static String DEVICE_TOKEN;

    protected static final String DEVICE_CHANNEL = "testing_channel";

    private static final List<String> deviceTokens = new ArrayList<String>();

    static final int NUMBER_OF_REGISTRATIONS = 100;

    @BeforeClass
    public static void setup()
    {
        DEVICE_TOKEN = TokenGenerator.ANDROID.generate();

        ZeroPush.setConfiguration(new ZeroPushConfiguration()); // only for testing purposes
        ZeroPush.getConfiguration().setServerToken(System.getProperty("zeropush.token.server"));
        ZeroPush.getConfiguration().setApplicationToken(System.getProperty("zeropush.token.app"));

        deviceTokens.addAll(TokenGenerator.ANDROID.generate(NUMBER_OF_REGISTRATIONS));
    }

    @Before
    public void registerDevice()
    {
        for (int i = 0; i < deviceTokens.size(); i++)
        {
            RegisterResponse registerResponse = ZeroPush.registration().register(deviceTokens.get(0), DEVICE_CHANNEL).execute();
            Assert.assertEquals(HttpStatus.SC_OK, registerResponse.getStatusCode());
            Assert.assertEquals("ok", registerResponse.getMessage().getMessage());

            System.out.println("registered " + i);
        }
    }

    @After
    public void unregisterDevice()
    {
        for (int i = 0; i < deviceTokens.size(); i++)
        {
            RegisterResponse unRegisterResponse = ZeroPush.registration().unregister(deviceTokens.get(0)).execute();
            Assert.assertEquals(HttpStatus.SC_OK, unRegisterResponse.getStatusCode());
            Assert.assertEquals("ok", unRegisterResponse.getMessage().getMessage());

            System.out.println("unregistered " + i);
        }
    }
}
