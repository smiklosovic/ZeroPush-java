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
import java.util.List;

import com.google.gson.Gson;
import com.zeropush.model.Platform;
import com.zeropush.model.notification.exception.ZeroPushNotificationValidationException;

/**
 * Represents iOS and MacOS push notification. You get reference to this object by {@link IOSPushNotification.Builder#build()}.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class IOSPushNotification extends ZeroPushNotification
{
    // optional paramters

    private String alert;

    private int badge;

    private String sound;

    private String info;

    private long expiry;

    private boolean contentAvailable;

    private String category;

    private IOSPushNotification()
    {
        // for serialization purposes
    }

    private IOSPushNotification(Builder builder)
    {
        setDeviceTokens(builder.deviceTokens);
        this.alert = builder.alert;
        this.badge = builder.badge;
        this.sound = builder.sound;
        this.info = builder.info;
        this.expiry = builder.expiry;
        this.contentAvailable = builder.contentAvailable;
        this.category = builder.category;
    }

    @Override
    public void validate() throws ZeroPushNotificationValidationException
    {
        if (getDeviceTokens() == null)
        {
            throw new ZeroPushNotificationValidationException("Device token list is a null object.");
        }
    }

    @Override
    public Platform provides()
    {
        return Platform.IOS;
    }

    public String getAlert()
    {
        return alert;
    }

    public int getBadge()
    {
        return badge;
    }

    public String getSound()
    {
        return sound;
    }

    public String getInfo()
    {
        return info;
    }

    public long getExpiry()
    {
        return expiry;
    }

    public boolean isContentAvailable()
    {
        return contentAvailable;
    }

    public String getCategory()
    {
        return category;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    /**
     * Builds {@link IOSPushNotification}
     *
     * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
     *
     */
    public static class Builder
    {
        private static final Gson gson = new Gson();

        final List<String> deviceTokens = new ArrayList<String>();

        private String alert;

        private int badge;

        private String sound;

        private String info;

        private long expiry;

        private boolean contentAvailable;

        private String category;

        public IOSPushNotification build(String from)
        {
            return gson.fromJson(from, IOSPushNotification.class);
        }

        public IOSPushNotification build()
        {
            return new IOSPushNotification(this);
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

        /**
         * The text of the notification to be displayed. If you want to customize the notification, you may pass here whatever
         * {@link AlertJSON.Builder#build()} returns.
         *
         * @param alert altert to set as string
         * @return this builder
         */
        public Builder alert(String alert)
        {
            this.alert = alert;
            return this;
        }

        /**
         * The number to display on the app's icon badge. badge can also auto-increment or auto-decrement by passing "+1" or
         * "-1" respectively.
         *
         * @param badge badge to set as string
         * @return this builder
         */
        public Builder badge(int badge)
        {
            if (badge >= 0) {
                this.badge = badge;
            }
            return this;
        }

        /**
         * A sound to play along with the notification
         *
         * @param sound sound to play when notification arrives
         * @return this builder
         */
        public Builder sound(String sound)
        {
            this.sound = sound;
            return this;
        }

        /**
         * A JSON dictionary of extra data to send with the notification
         *
         * @param info extra data to send with the notification as a JSON dictionary
         * @return this builder
         */
        public Builder info(String info)
        {
            this.info = info;
            return this;
        }

        /**
         * A UNIX epoch date expressed in seconds (UTC) that identifies when the notification is no longer valid and can be
         * discarded.
         *
         * @param expiry expiration date of the notification in seconds
         * @return this builder
         */
        public Builder expiry(long expiry)
        {
            if (System.currentTimeMillis()/1000 < expiry)
            {
                this.expiry = expiry;
            }
            return this;
        }

        /**
         * The presence of this parameter will cause the notification to include the content_available key in it's aps payload.
         * The only valid truthy value is 'true'. This is used to implement background fetch.
         *
         * @return this builder
         */
        public Builder contentAvailable()
        {
            this.contentAvailable = true;
            return this;
        }

        /**
         * A string matching the identifier of a registered interactive notification type.
         *
         * @param category string representing the identified of a registered interactive notification type
         * @return this builder
         */
        public Builder category(String category)
        {
            this.category = category;
            return this;
        }
    }
}
