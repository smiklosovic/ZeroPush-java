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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushRequestBuilderException;
import com.zeropush.model.NotificationSender;
import com.zeropush.model.ZeroPushNotificationResponse;
import com.zeropush.model.notification.AndroidPushNotification;
import com.zeropush.request.RequestType;
import com.zeropush.request.ZeroPushRequestBuilder;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class AndroidNotificationSender implements NotificationSender<AndroidPushNotification>
{
    private static final Logger logger = Logger.getLogger(AndroidNotificationSender.class.getName());

    private boolean broadcast = false;

    private String channel = null;

    @Override
    public ZeroPushNotificationResponse send(AndroidPushNotification pushNotification)
    {
        ZeroPushRequestBuilder requestBuilder = getRequestBuilder(pushNotification);

        if (logger.isLoggable(Level.INFO))
        {
            logger.log(Level.INFO, String.format("sending notification - '%s'", pushNotification.toString()));
        }

        ZeroPushNotificationResponse response = new NotificationRestSender().send(requestBuilder, broadcast);

        return response;
    }

    public AndroidNotificationSender broadcast(boolean broadcast)
    {
        this.broadcast = broadcast;
        return this;
    }

    public AndroidNotificationSender channel(String channel)
    {
        this.channel = channel;
        return this;
    }

    private ZeroPushRequestBuilder getRequestBuilder(AndroidPushNotification pushNotification)
    {
        final RequestBuilderFactory requestBuilderFactory = new RequestBuilderFactory(pushNotification.getData())
            .collapseKey(pushNotification.getCollapseKey())
            .delayWhileIdle(pushNotification.isDelayWhileIdle())
            .timeToLive(pushNotification.getTimeToLive());

        if (!broadcast)
        {
            requestBuilderFactory.deviceTokens(pushNotification.getDeviceTokens());
        }
        else
        {
            requestBuilderFactory.broadcast(true);
            requestBuilderFactory.channel(channel);
        }

        return requestBuilderFactory.build();
    }

    private class RequestBuilderFactory
    {
        // required

        private List<String> deviceTokens; // required only when we do not broadcast

        private Map<String, String> data;

        // optional

        private String channel;

        private String collapseKey;

        private boolean delayWhileIdle = false;

        private int timeToLive;

        // internal

        private boolean broadcast = false;

        public RequestBuilderFactory(Map<String, String> data)
        {
            if (data == null || data.isEmpty())
            {
                throw new ZeroPushRequestBuilderException("Data given to RequestBuilderFactory must be specified and must not be empty.");
            }
            this.data = data;
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

        public RequestBuilderFactory collapseKey(String collapseKey)
        {
            this.collapseKey = collapseKey;
            return this;
        }

        public RequestBuilderFactory delayWhileIdle(boolean delayWhileIdle)
        {
            this.delayWhileIdle = delayWhileIdle;
            return this;
        }

        public RequestBuilderFactory timeToLive(int timeToLive)
        {
            this.timeToLive = timeToLive;
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

            // device tokens are sent only when we do not broadcast
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

            for (final Map.Entry<String, String> datum : data.entrySet())
            {
                if (datum.getKey() != null && datum.getKey().length() != 0 && datum.getValue() != null)
                {
                    requestBuilder.withParameter("data[" + datum.getKey() + "]", datum.getValue());
                }
            }

            if (collapseKey != null && collapseKey.length() != 0)
            {
                requestBuilder.withParameter("collape_key", collapseKey);
            }

            if (delayWhileIdle)
            {
                requestBuilder.withParameter("delay_while_idle", "true");
            }

            if (timeToLive > 0)
            {
                requestBuilder.withParameter("time_to_live", timeToLive);
            }

            return requestBuilder;
        }
    }
}
