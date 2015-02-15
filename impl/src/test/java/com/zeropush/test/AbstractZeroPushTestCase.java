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
package com.zeropush.test;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.device.DeviceResponse;
import com.zeropush.register.RegisterResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public abstract class AbstractZeroPushTestCase extends LoadingPropertiesTestCase
{
    protected static final String DEVICE_TOKEN = "1234567890";

    protected static final String DEVICE_CHANNEL = "testing_channel";

    @BeforeClass
    public static void setup() throws Exception
    {
        loadProperties();
        ZeroPush.setConfiguration(new ZeroPushConfiguration()); // only for testing purposes
        ZeroPush.getConfiguration().setServerToken(System.getProperty("zeropush.token.server", getProperty("zeropush.token.server")));
        ZeroPush.getConfiguration().setApplicationToken(System.getProperty("zeropush.token.app", getProperty("zeropush.token.app")));
    }

    @Before
    public void registerDevice()
    {
        // register device
        RegisterResponse registerResponse = ZeroPush.registration().register(DEVICE_TOKEN, DEVICE_CHANNEL).execute();
        Assert.assertEquals(HttpStatus.SC_OK, registerResponse.getStatusCode());
        Assert.assertEquals("ok", registerResponse.getMessage().getMessage());
    }

    @After
    public void unregisterDevice()
    {
        // unregister device
        RegisterResponse unRegisterResponse = ZeroPush.registration().unregister(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, unRegisterResponse.getStatusCode());
        Assert.assertEquals("ok", unRegisterResponse.getMessage().getMessage());

        // make sure it is unregistered
        DeviceResponse getUnregisteredDeviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_NOT_FOUND, getUnregisteredDeviceResponse.getStatusCode());
        Assert.assertEquals("record not found", getUnregisteredDeviceResponse.getResponseError().getError());
    }
}
