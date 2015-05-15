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
package com.zeropush.model.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.zeropush.model.Platform;
import com.zeropush.model.notification.exception.ZeroPushNotificationValidationException;

/**
 * Represents Android push notification. You get reference to this object by {@link AndroidPushNotification.Builder#build()}.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class AndroidPushNotification extends ZeroPushNotification
{
    // required

    private Map<String, String> data = new HashMap<String, String>();

    // optional

    private String collapseKey;

    private boolean delayWhileIdle = false;

    private int timeToLive;

    private AndroidPushNotification(Builder builder)
    {
        setDeviceTokens(builder.deviceTokens);
        data = builder.data;
        collapseKey = builder.collapseKey;
        delayWhileIdle = builder.delayWhileIdle;
        timeToLive = builder.timeToLive;
    }

    public Map<String, String> getData()
    {
        return data;
    }

    public String getCollapseKey()
    {
        return collapseKey;
    }

    public boolean isDelayWhileIdle()
    {
        return delayWhileIdle;
    }

    public int getTimeToLive()
    {
        return timeToLive;
    }

    @Override
    public Platform provides()
    {
        return Platform.ANDROID_GCM;
    }

    @Override
    public void validate() throws ZeroPushNotificationValidationException
    {
        if (data == null || data.isEmpty())
        {
            throw new ZeroPushNotificationValidationException("No data to send.");
        }
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public static class Builder
    {
        private static final Gson gson = new Gson();

        final List<String> deviceTokens = new ArrayList<String>();

        final Map<String, String> data = new HashMap<String, String>();

        String collapseKey;

        boolean delayWhileIdle = false;

        int timeToLive;

        public AndroidPushNotification build(String from)
        {
            return gson.fromJson(from, AndroidPushNotification.class);
        }

        public AndroidPushNotification build()
        {
            return new AndroidPushNotification(this);
        }

        public Builder deviceTokens(List<String> deviceTokens)
        {
            if (deviceTokens != null)
            {
                this.deviceTokens.clear();
                this.deviceTokens.addAll(deviceTokens);
            }

            return this;
        }

        public Builder addDeviceToken(String deviceToken)
        {
            if (deviceToken != null && deviceToken.length() != 0)
            {
                deviceTokens.add(deviceToken);
            }

            return this;
        }

        public Builder addDeviceTokens(String... deviceTokens)
        {
            if (deviceTokens != null)
            {
                for (String deviceToken : deviceTokens)
                {
                    addDeviceToken(deviceToken);
                }
            }

            return this;
        }

        public Builder addDeviceTokens(List<String> deviceTokens)
        {
            if (deviceTokens != null)
            {
                for (String deviceToken : deviceTokens)
                {
                    addDeviceToken(deviceToken);
                }
            }

            return this;
        }

        public Builder data(Map<String, String> data)
        {
            if (data != null)
            {
                this.data.clear();
                this.data.putAll(data);
            }

            return this;
        }

        public Builder addDatum(String key, String value)
        {
            this.data.put(key, value);
            return this;
        }

        public Builder addDatum(Map.Entry<String, String> entry)
        {
            if (entry.getKey() != null && entry.getKey().length() != 0 && entry.getValue() != null)
            {
                this.data.put(entry.getKey(), entry.getValue());
            }

            return this;
        }

        /**
         * This parameter specifies an arbitrary string (such as "Updates Available") that is used to collapse a group of like
         * messages when the device is offline, so that only the last message gets sent to the client. This is intended to avoid
         * sending too many messages to the phone when it comes back online. Note that since there is no guarantee of the order
         * in which messages get sent, the "last" message may not actually be the last message sent by the application server.
         * Messages with collapse keys are also called send-to-sync messages. <br>
         * <br>
         * Note: GCM allows a maximum of 4 different collapse keys to be used by the GCM server at any given time. In other
         * words, the GCM server can simultaneously store 4 different send-to-sync messages per device, each with a different
         * collapse key. If you exceed this number GCM will only keep 4 collapse keys, with no guarantees about which ones they
         * will be
         *
         * @param collapseKey collape key for push notification
         * @return this builder
         */
        public Builder collapseKey(String collapseKey)
        {
            this.collapseKey = collapseKey;
            return this;
        }

        /**
         * This parameter indicates that the message should not be sent immediately if the device is idle. The server will wait
         * for the device to become active, and then only the last message for each collapse_key value will be sent. The default
         * value is false.
         *
         * @return this builder
         */
        public Builder delayWhileIdle()
        {
            delayWhileIdle = true;
            return this;
        }

        /**
         * This parameter specifies how long (in seconds) the message should be kept on GCM storage if the device is offline.
         * Optional (default time-to-live is 4 weeks, and must be set as a JSON number).
         *
         * @param timeToLive number specifying how long the message should be kept on GCM storage in seconds
         * @return this builder
         */
        public Builder timeToLive(int timeToLive)
        {
            this.timeToLive = timeToLive;
            return this;
        }
    }
}
