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
import com.zeropush.notify.notification.SafariPushNotification;
import com.zeropush.request.RequestType;
import com.zeropush.request.ZeroPushRequestBuilder;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class SafariNotificationSender implements NotificationSender<SafariPushNotification>
{
    private static final Logger logger = Logger.getLogger(SafariNotificationSender.class.getName());

    private boolean broadcast = false;

    private String channel = null;

    public ZeroPushNotificationResponse send(SafariPushNotification pushNotification)
    {
        ZeroPushRequestBuilder requestBuilder = getRequestBuilder(pushNotification);

        if (logger.isLoggable(Level.INFO))
        {
            logger.log(Level.INFO, String.format("sending notification - '%s'", pushNotification.toString()));
        }

        ZeroPushNotificationResponse response = new NotificationRestSender().send(requestBuilder, broadcast);

        return response;
    }

    public SafariNotificationSender broadcast(boolean broadcast)
    {
        this.broadcast = broadcast;
        return this;
    }

    public SafariNotificationSender channel(String channel)
    {
        this.channel = channel;
        return this;
    }

    private ZeroPushRequestBuilder getRequestBuilder(SafariPushNotification pushNotification)
    {
        final String title = pushNotification.getTitle();
        final String body = pushNotification.getBody();

        final RequestBuilderFactory builder = new RequestBuilderFactory(title, body)
            .urlArgs(pushNotification.getUrlArgs())
            .label(pushNotification.getLabel())
            .expiry(pushNotification.getExpiry());

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
        private List<String> deviceTokens; // required only when we do not broadcast

        // required

        private String title;

        private String body;

        // optional

        private String channel;

        private int expiry;

        private String label;

        private List<String> urlArgs;

        // internal

        private boolean broadcast;

        public RequestBuilderFactory(String title, String body)
        {
            if (title == null || title.length() == 0)
            {
                throw new ZeroPushRequestBuilderException("Title for Safari notification has to be specified.");
            }

            this.title = title;

            if (body == null || body.length() == 0)
            {
                throw new ZeroPushRequestBuilderException("Body for Safari notification has to be specified.");
            }

            this.body = body;
        }

        public RequestBuilderFactory expiry(int expiry)
        {
            this.expiry = expiry;
            return this;
        }

        public RequestBuilderFactory label(String label)
        {
            this.label = label;
            return this;
        }

        public RequestBuilderFactory urlArgs(List<String> urlArgs)
        {
            this.urlArgs = urlArgs;
            return this;
        }

        public RequestBuilderFactory deviceTokens(List<String> deviceTokens)
        {
            this.deviceTokens = deviceTokens;
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

            requestBuilder.withParameter("title", title);
            requestBuilder.withParameter("body", body);

            if (expiry > 0)
            {
                requestBuilder.withParameter("expiry", expiry);
            }

            if (label != null && label.length() != 0)
            {
                requestBuilder.withParameter("label", label);
            }

            if (urlArgs != null)
            {
                for (final String urlArg : urlArgs)
                {
                    if (urlArg != null && urlArg.length() != 0)
                    {
                        requestBuilder.withParameter("url_args[]", urlArg);
                    }
                }
            }

            return requestBuilder;
        }
    }
}
