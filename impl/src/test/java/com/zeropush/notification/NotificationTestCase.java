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
package com.zeropush.notification;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushRequestBuilderException;
import com.zeropush.model.ZeroPushNotificationResponse;
import com.zeropush.model.notification.AndroidPushNotification;
import com.zeropush.model.notification.IOSPushNotification;
import com.zeropush.model.notification.ZeroPushNotification;
import com.zeropush.model.notification.exception.InvalidDeviceTokenException;
import com.zeropush.model.notification.exception.ZeroPushNotificationValidationException;
import com.zeropush.test.AbstractZeroPushTestCase;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class NotificationTestCase extends AbstractZeroPushTestCase
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void androidNotificationTest()
    {
        ZeroPushNotification boardingNotification = new AndroidPushNotification.Builder()
            .addDeviceToken(DEVICE_TOKEN)
            .addDatum("alert", "Now Boarding")
            .addDatum("username", "fred.droid")
            .build();

        Assert.assertEquals(1, boardingNotification.getDeviceTokens().size());

        ZeroPushNotificationResponse response = ZeroPush.notification(boardingNotification).send();

        Assert.assertEquals(200, response.getStatusCode());

        Assert.assertEquals(1, response.getSenderResponse().getSentCount());

        Assert.assertThat(response.getResponseError(), not(nullValue()));
        Assert.assertThat(response.getResponseError().getError(), nullValue());
        Assert.assertThat(response.getResponseError().getMessage(), nullValue());
        Assert.assertThat(response.getResponseError().getReferenceUrl(), nullValue());
    }

    @Test
    public void invalidTokenExceptionTest()
    {
        exception.expect(InvalidDeviceTokenException.class);

        new AndroidPushNotification.Builder()
            .addDatum("a", "b")
            .addDeviceTokens(" ")
            .build();
    }

    @Test
    @Ignore
    public void iOSNotificationTest()
    {
        ZeroPushNotification boardingNotification = new IOSPushNotification.Builder()
            .addDeviceToken(DEVICE_TOKEN)
            .alert("Now Boarding")
            .badge(1)
            .category("category1")
            .contentAvailable()
            .build();

        Assert.assertEquals(1, boardingNotification.getDeviceTokens().size());

        ZeroPushNotificationResponse response = ZeroPush.notification(boardingNotification).send();

        System.out.println(response.getResponseError().getError());
        System.out.println(response.getResponseError().getMessage());
        System.out.println(response.getResponseError().getReferenceUrl());

        Assert.assertEquals(200, response.getStatusCode());

        Assert.assertEquals(1, response.getSenderResponse().getSentCount());

        Assert.assertThat(response.getResponseError(), not(nullValue()));
        Assert.assertThat(response.getResponseError().getError(), nullValue());
        Assert.assertThat(response.getResponseError().getMessage(), nullValue());
        Assert.assertThat(response.getResponseError().getReferenceUrl(), nullValue());
    }

    @Test
    public void broadcastNotificationTest()
    {
        ZeroPushNotification boardingNotification = new AndroidPushNotification.Builder()
            .addDatum("alert", "Now Boarding Broadcast")
            .addDatum("username", "fred.droid")
            .build();

        ZeroPush.notification(boardingNotification).broadcast(DEVICE_CHANNEL).send();
    }

    @Test
    public void emptyChannelWhenBroadcastingTest()
    {
        exception.expect(ZeroPushRequestBuilderException.class);
        exception.expectMessage("Channel to broadcast a push notification can not be empty String.");

        ZeroPushNotification boardingNotification = new AndroidPushNotification.Builder()
            .addDatum("alert", "Now Boarding Broadcast")
            .addDatum("username", "fred.droid")
            .build();

        ZeroPush.notification(boardingNotification).broadcast("").send();
    }

    @Test
    public void emptyDataNotificationTest()
    {
        exception.expect(ZeroPushNotificationValidationException.class);
        exception.expectMessage("No data to send.");

        ZeroPushNotification boardingNotification = new AndroidPushNotification.Builder().build();

        ZeroPush.notification(boardingNotification).send();
    }

    @Test
    public void notBroadcastingEmptyDeviceTokensNotificationTest()
    {
        exception.expect(ZeroPushRequestBuilderException.class);
        exception.expectMessage("You do not broadcast and you have supplied either empty array or device tokens which are null objects or empty Strings.");

        ZeroPushNotification boardingNotification = new AndroidPushNotification.Builder()
            .addDatum("alert", "Now Boarding")
            .addDatum("username", "fred.droid")
            .build();

        ZeroPush.notification(boardingNotification).send();
    }
}
