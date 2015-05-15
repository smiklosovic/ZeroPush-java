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
package com.zeropush.notify.sender;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushRequestBuilderException;
import com.zeropush.model.NotificationSender;
import com.zeropush.model.ZeroPushNotificationResponse;
import com.zeropush.model.notification.IOSPushNotification;
import com.zeropush.request.RequestType;
import com.zeropush.request.ZeroPushRequestBuilder;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class IOSNotificationSender implements NotificationSender<IOSPushNotification>
{
    private static final Logger logger = Logger.getLogger(IOSNotificationSender.class.getName());

    private boolean broadcast = false;

    private String channel = null;

    @Override
    public ZeroPushNotificationResponse send(IOSPushNotification pushNotification)
    {
        ZeroPushRequestBuilder requestBuilder = getRequestBuilder(pushNotification);

        if (logger.isLoggable(Level.INFO))
        {
            logger.log(Level.INFO, String.format("sending notification - '%s'", pushNotification.toString()));
        }

        ZeroPushNotificationResponse response = new NotificationRestSender().send(requestBuilder, broadcast);

        return response;
    }

    public IOSNotificationSender broadcast(boolean broadcast)
    {
        this.broadcast = broadcast;
        return this;
    }

    public IOSNotificationSender channel(String channel)
    {
        this.channel = channel;
        return this;
    }

    private ZeroPushRequestBuilder getRequestBuilder(IOSPushNotification pushNotification)
    {
        final RequestBuilderFactory builder = new RequestBuilderFactory()
            .alert(pushNotification.getAlert())
            .badge(pushNotification.getBadge())
            .sound(pushNotification.getSound())
            .info(pushNotification.getInfo())
            .expiry(pushNotification.getExpiry())
            .contentAvailable(pushNotification.isContentAvailable())
            .category(pushNotification.getCategory());

        if (!broadcast)
        {
            builder.deviceTokens(pushNotification.getDeviceTokens());
        }
        else
        {
            builder.broadcast(true);
            builder.channel(channel);
        }

        return builder.build();
    }

    private class RequestBuilderFactory
    {
        // optional

        private List<String> deviceTokens; // required only when we do not broadcast

        private String alert;

        private String badge;

        private String sound;

        private String info;

        private int expiry;

        private boolean contentAvailable;

        private String category;

        private String channel;

        // internal

        private boolean broadcast;

        // setters

        public RequestBuilderFactory deviceTokens(List<String> deviceTokens)
        {
            this.deviceTokens = deviceTokens;
            return this;
        }

        public RequestBuilderFactory category(String category)
        {
            this.category = category;
            return this;
        }

        public RequestBuilderFactory contentAvailable(boolean contentAvailable)
        {
            this.contentAvailable = contentAvailable;
            return this;
        }

        public RequestBuilderFactory expiry(int expiry)
        {
            this.expiry = expiry;
            return this;
        }

        public RequestBuilderFactory info(String info)
        {
            this.info = info;
            return this;
        }

        public RequestBuilderFactory sound(String sound)
        {
            this.sound = sound;
            return this;
        }

        public RequestBuilderFactory badge(String badge)
        {
            this.badge = badge;
            return this;
        }

        public RequestBuilderFactory alert(String alert)
        {
            this.alert = alert;
            return this;
        }

        public RequestBuilderFactory channel(String channel)
        {
            this.channel = channel;
            return this;
        }

        public RequestBuilderFactory broadcast(boolean broadcast)
        {
            this.broadcast = broadcast;
            return this;
        }

        public ZeroPushRequestBuilder build()
        {
            final ZeroPushRequestBuilder requestBuilder = new ZeroPushRequestBuilder(RequestType.POST, ZeroPush.getConfiguration())
                .withAuthToken(ZeroPush.getConfiguration().getServerToken());

            if (!broadcast)
            {
                if (deviceTokens == null)
                {
                    throw new ZeroPushRequestBuilderException("You do not broadcast and you have not specified device tokens.");
                }

                boolean addedDeviceToken = false;

                for (final String deviceToken : deviceTokens)
                {
                    if (deviceToken != null && deviceToken.length() != 0)
                    {
                        addedDeviceToken = true;
                        requestBuilder.withParameter("device_tokens[]", deviceToken);
                    }
                }

                if (!addedDeviceToken)
                {
                    throw new ZeroPushRequestBuilderException("You do not broadcast and you have "
                        + "supplied either empty array or device tokens which are null objects or empty Strings.");
                }
            }

            if (broadcast && channel != null)
            {
                if (channel.length() != 0)
                {
                    requestBuilder.withParameter("channel", channel);
                }
                else
                {
                    throw new ZeroPushRequestBuilderException("Channel to broadcast a push notification can not be empty String.");
                }
            }

            if (alert != null && alert.length() != 0)
            {
                requestBuilder.withParameter("alert", alert);
            }

            if (badge != null && badge.length() != 0)
            {
                requestBuilder.withParameter("badge", badge);
            }

            if (sound != null && sound.length() != 0)
            {
                requestBuilder.withParameter("sound", sound);
            }

            if (info != null && info.length() != 0)
            {
                requestBuilder.withParameter("info", info);
            }

            if (expiry > 0)
            {
                requestBuilder.withParameter("expiry", expiry);
            }

            if (contentAvailable)
            {
                requestBuilder.withParameter("content_available", "true");
            }

            if (category != null && category.length() != 0)
            {
                requestBuilder.withParameter("category", category);
            }

            return requestBuilder;
        }
    }
}
