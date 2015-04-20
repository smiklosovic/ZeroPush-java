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

import com.zeropush.model.ZeroPushNotification;
import com.zeropush.model.ZeroPushNotificationResponse;

/**
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class NotificationEndpointProxy
{
    private ZeroPushNotification pushNotification;

    private boolean broadcast = false;

    private String channel;

    /**
     * Sends specified push notification.
     *
     * @return {@link ZeroPushNotificationResponse}
     */
    public ZeroPushNotificationResponse send()
    {
        return new NotificationSenderExecutor(pushNotification, broadcast, channel).execute();
    }

    /**
     * Sets a notification to send.
     *
     * @param pushNotification push notification to sent
     * @return {@link NotificationEndpointProxy}
     */
    public NotificationEndpointProxy notification(ZeroPushNotification pushNotification)
    {
        this.pushNotification = pushNotification;
        return this;
    }

    /**
     * Flag method for broadcasting specified {@link ZeroPushNotification}.
     *
     * @return {@link NotificationEndpointProxy}
     */
    public NotificationEndpointProxy broadcast()
    {
        broadcast = true;
        return this;
    }

    /**
     * Flag method for broadcasting specified {@link ZeroPushNotification} only to specified channel.
     *
     * @param channel channel to which the push notification will be broadcast
     * @return {@link NotificationEndpointProxy}
     */
    public NotificationEndpointProxy broadcast(String channel)
    {
        broadcast = true;
        this.channel = channel;
        return this;
    }
}
