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
package com.zeropush.model;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.model.notification.AlertJSON;
import com.zeropush.model.notification.AndroidPushNotification;
import com.zeropush.model.notification.IOSPushNotification;
import com.zeropush.model.notification.SafariPushNotification;
import com.zeropush.model.notification.ZeroPushNotification;
import com.zeropush.model.token.TokenGenerator;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class NotificationMarshallingTest {

    @Test
    public void androidMarshallingTest() {
        ZeroPushNotification notification = new AndroidPushNotification.Builder()
            .addDeviceToken(TokenGenerator.ANDROID.generate())
            .addDatum("some", "data")
            .collapseKey("Updates Available")
            .build();

        String marshalled = notification.toString();
        ZeroPushNotification demarshalled = new AndroidPushNotification.Builder().build(marshalled);

        Assert.assertEquals("Marshalled and demarshalled Android notifications are not equal.", marshalled, demarshalled.toString());
    }

    @Test
    public void safariMarshallingTest() {
        ZeroPushNotification notification = new SafariPushNotification.Builder()
            .addDeviceToken(TokenGenerator.IOS.generate())
            .addUrlArgs("url args")
            .body("this is body")
            .expiry(1000)
            .label("this is label")
            .title("this is title")
            .build();

        String marshalled = notification.toString();
        ZeroPushNotification demarshalled = new SafariPushNotification.Builder().build(marshalled);

        Assert.assertEquals("Marshalled and demarshalled Safari notifications are not equal.", marshalled, demarshalled.toString());
    }

    @Test
    public void iosMarshallingTest() {

        String alertJson = new AlertJSON.Builder()
            .actionLocKey("key")
            .body("body")
            .launchImage("someImage.png")
            .locArgs("lock", "args")
            .locKey("lockKey")
            .build();

        ZeroPushNotification notification = new IOSPushNotification.Builder()
            .addDeviceToken(TokenGenerator.IOS.generate())
            .alert(alertJson)
            .badge(1)
            .category("")
            .contentAvailable()
            .info("this is info")
            .sound("sound.file")
            .build();

        String marshalled = notification.toString();
        ZeroPushNotification demarshalled = new IOSPushNotification.Builder().build(marshalled);

        Assert.assertEquals("Marshalled and demarshalled iOS notifications are not equal.", marshalled, demarshalled.toString());

        AlertJSON demarshalledAlertJson = new AlertJSON.Builder().build(((IOSPushNotification) demarshalled).getAlert());

        Assert.assertEquals("Marshalled and demarshalled alerts are not equal.", alertJson, demarshalledAlertJson.toString());
    }
}
