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
package com.zeropush.device;

import java.util.Arrays;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.device.DeviceResponse;
import com.zeropush.model.Device;
import com.zeropush.test.AbstractZeroPushTestCase;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class DevicesTestCase extends AbstractZeroPushTestCase
{
    private static final String[] REPLACING_CHANNELS = new String[] { "replacing_channe1", "replacing_channel2", "replacing_channel3" };

    private static final String[] ADDING_CHANNELS = new String[] { "adding_channe1", "adding_channel2", "adding_channel3" };

    @Test
    public void getDeviceTest()
    {
        // get device

        DeviceResponse deviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, deviceResponse.getStatusCode());

        // make asserts

        Device device = deviceResponse.getDevice();
        Assert.assertEquals(DEVICE_TOKEN, device.getToken());
        Assert.assertEquals(1, device.getChannels().size());
        Assert.assertEquals(DEVICE_CHANNEL, device.getChannels().get(0));
    }

    @Test
    public void replaceChannelsForDeviceTest()
    {
        // get device

        DeviceResponse deviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, deviceResponse.getStatusCode());

        // make asserts

        Device device = deviceResponse.getDevice();
        Assert.assertEquals(DEVICE_TOKEN, device.getToken());
        Assert.assertEquals(1, device.getChannels().size());
        Assert.assertEquals(DEVICE_CHANNEL, device.getChannels().get(0));

        // replace channels

        ZeroPush.devices().replaceChannels(DEVICE_TOKEN, REPLACING_CHANNELS).execute();

        // assert there are new channels and only these new ones

        deviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, deviceResponse.getStatusCode());

        // we added 3 channels
        Assert.assertEquals(3, deviceResponse.getDevice().getChannels().size());
        Assert.assertTrue(deviceResponse.getDevice().getChannels().containsAll(Arrays.asList(REPLACING_CHANNELS)));

        // the old channel is not there anymore
        Assert.assertFalse(deviceResponse.getDevice().getChannels().contains(DEVICE_CHANNEL));
    }

    @Test
    public void addChannelsToDeviceTest()
    {
        // get device

        DeviceResponse deviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, deviceResponse.getStatusCode());

        // make asserts

        Device device = deviceResponse.getDevice();
        Assert.assertEquals(DEVICE_TOKEN, device.getToken());
        Assert.assertEquals(1, device.getChannels().size());
        Assert.assertEquals(DEVICE_CHANNEL, device.getChannels().get(0));

        // add channels

        ZeroPush.devices().appendChannels(DEVICE_TOKEN, ADDING_CHANNELS).execute();

        // assert there are new channels with the old one

        deviceResponse = ZeroPush.devices().get(DEVICE_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, deviceResponse.getStatusCode());

        // we have 4 channels in total
        Assert.assertEquals(4, deviceResponse.getDevice().getChannels().size());
        Assert.assertTrue(deviceResponse.getDevice().getChannels().containsAll(Arrays.asList(ADDING_CHANNELS)));
        Assert.assertTrue(deviceResponse.getDevice().getChannels().contains(DEVICE_CHANNEL));
    }
}
