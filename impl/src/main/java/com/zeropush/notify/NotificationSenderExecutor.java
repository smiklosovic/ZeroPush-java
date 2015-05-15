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
package com.zeropush.notify;

import com.zeropush.model.ZeroPushNotificationResponse;
import com.zeropush.model.notification.AndroidPushNotification;
import com.zeropush.model.notification.IOSPushNotification;
import com.zeropush.model.notification.SafariPushNotification;
import com.zeropush.model.notification.ZeroPushNotification;
import com.zeropush.model.notification.exception.ZeroPushNotificationValidationException;
import com.zeropush.notify.sender.AndroidNotificationSender;
import com.zeropush.notify.sender.IOSNotificationSender;
import com.zeropush.notify.sender.SafariNotificationSender;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class NotificationSenderExecutor
{
    private final ZeroPushNotification pushNotification;

    private final boolean broadcast;

    private final String channel;

    public NotificationSenderExecutor(ZeroPushNotification pushNotification, boolean broadcast, String channel)
    {
        this.pushNotification = pushNotification;
        this.broadcast = broadcast;
        this.channel = channel;
    }

    public ZeroPushNotificationResponse execute() throws ZeroPushNotificationValidationException
    {
        pushNotification.validate();

        ZeroPushNotificationResponse senderResponse = null;

        switch (pushNotification.provides())
        {
            case ANDROID_GCM:
                senderResponse = new AndroidNotificationSender()
                    .broadcast(broadcast)
                    .channel(channel)
                    .send((AndroidPushNotification) pushNotification);
                break;
            case IOS:
                senderResponse = new IOSNotificationSender()
                    .broadcast(broadcast)
                    .channel(channel)
                    .send((IOSPushNotification) pushNotification);
                break;
            case SAFARI:
                senderResponse = new SafariNotificationSender()
                    .broadcast(broadcast)
                    .channel(channel)
                    .send((SafariPushNotification) pushNotification);
                break;
            default:
                break;
        }

        return senderResponse;
    }
}
