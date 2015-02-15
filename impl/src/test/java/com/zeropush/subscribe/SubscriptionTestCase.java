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
package com.zeropush.subscribe;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.subscribe.SubscriptionResponse;
import com.zeropush.test.AbstractZeroPushTestCase;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class SubscriptionTestCase extends AbstractZeroPushTestCase
{
    private static final String CUSTOM_SUBSCRIPTION_CHANNEL = "custom_channel";

    @Test
    public void subscribeAndUnSubscribeTest()
    {
        // created device is by default in one channel

        // subscription
        SubscriptionResponse subscriptionResponse = ZeroPush.subscription().subscribe(DEVICE_TOKEN, CUSTOM_SUBSCRIPTION_CHANNEL).execute();

        Assert.assertEquals(HttpStatus.SC_OK, subscriptionResponse.getStatusCode());
        Assert.assertThat(subscriptionResponse.getSubscription(), is(not(nullValue())));

        Assert.assertEquals(DEVICE_TOKEN, subscriptionResponse.getSubscription().getDeviceToken());
        Assert.assertEquals(2, subscriptionResponse.getSubscription().getChannels().size());
        Assert.assertTrue(subscriptionResponse.getSubscription().getChannels().contains(CUSTOM_SUBSCRIPTION_CHANNEL));

        // unsubscription
        subscriptionResponse = ZeroPush.subscription().unsubscribe(DEVICE_TOKEN, CUSTOM_SUBSCRIPTION_CHANNEL).execute();
        Assert.assertEquals(HttpStatus.SC_OK, subscriptionResponse.getStatusCode());
        Assert.assertThat(subscriptionResponse.getSubscription(), is(not(nullValue())));

        Assert.assertEquals(DEVICE_TOKEN, subscriptionResponse.getSubscription().getDeviceToken());
        Assert.assertEquals(1, subscriptionResponse.getSubscription().getChannels().size());
        Assert.assertFalse(subscriptionResponse.getSubscription().getChannels().contains(CUSTOM_SUBSCRIPTION_CHANNEL));
        Assert.assertTrue(subscriptionResponse.getSubscription().getChannels().contains(DEVICE_CHANNEL));
    }
}
