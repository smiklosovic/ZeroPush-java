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
package com.zeropush.notify.notification;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.zeropush.exception.ZeroPushNotificationValidationException;
import com.zeropush.model.Platform;
import com.zeropush.model.ZeroPushNotification;

/**
 * Represents Safari push notification. You get reference to this object by {@link SafariPushNotification.Builder#build()}.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class SafariPushNotification extends ZeroPushNotification
{
    // required

    private String title;

    private String body;

    // optional parameters

    private List<String> urlArgs;

    private String label;

    private int expiry;

    private SafariPushNotification(Builder builder)
    {
        setDeviceTokens(builder.deviceTokens);
        title = builder.title;
        body = builder.body;
        urlArgs = builder.urlArgs;
        label = builder.label;
        expiry = builder.expiry;
    }

    @Override
    public void validate() throws ZeroPushNotificationValidationException
    {
        if (title == null || title.length() == 0)
        {
            throw new ZeroPushNotificationValidationException("Title for Safari push notification was not set.");
        }

        if (body == null || body.length() == 0)
        {
            throw new ZeroPushNotificationValidationException("Body for Safari push notification was not set.");
        }
    }

    @Override
    public Platform provides()
    {
        return Platform.SAFARI;
    }

    public String getTitle()
    {
        return title;
    }

    public String getBody()
    {
        return body;
    }

    public List<String> getUrlArgs()
    {
        return urlArgs;
    }

    public String getLabel()
    {
        return label;
    }

    public int getExpiry()
    {
        return expiry;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public static class Builder
    {
        final List<String> deviceTokens = new ArrayList<String>();

        String title;

        String body;

        final List<String> urlArgs = new ArrayList<String>();

        String label;

        int expiry = 0;

        public SafariPushNotification build()
        {
            return new SafariPushNotification(this);
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
         *
         * @param title The title of the notification to display. Only 1 line is visible and should be short.
         * @return
         */
        public Builder title(String title)
        {
            this.title = title;
            return this;
        }

        /**
         *
         * @param body The body of the notification. About 4 lines are visible.
         * @return
         */
        public Builder body(String body)
        {
            this.body = body;
            return this;
        }

        /**
         *
         * @param urlArg value to substitute placeholders of {@code urlFormatString} defined in {@code website.json}.
         * @return
         */
        public Builder addUrlArg(String urlArg)
        {
            if (urlArg != null && urlArg.length() != 0)
            {
                urlArgs.add(urlArg);
            }

            return this;
        }

        /**
         *
         * @param urlArg values to substitute placeholders of {@code urlFormatString} defined in {@code website.json}.
         * @return
         */
        public Builder addUrlArgs(String... urlArgs)
        {
            if (urlArgs != null)
            {
                for (String urlArg : urlArgs)
                {
                    addUrlArg(urlArg);
                }
            }

            return this;
        }

        /**
         *
         * @param urlArg list of values to substitute placeholders of {@code urlFormatString} defined in {@code website.json}.
         * @return
         */
        public Builder addUrlArgs(List<String> urlArgs)
        {
            if (urlArgs != null)
            {
                for (String urlArg : urlArgs)
                {
                    addUrlArg(urlArg);
                }
            }

            return this;
        }

        /**
         * The label of the action button, if the user sets the notifications to appear as alerts. This label should be
         * succinct, such as “Details” or “Read more”. If omitted, the default value is “Show”.
         *
         * @param label
         * @return
         */
        public Builder label(String label)
        {
            this.label = label;
            return this;
        }

        /**
         *
         * @param expiry A UNIX epoch date expressed in seconds (UTC) that identifies when the notification is no longer valid
         *        and can be discarded.
         * @return
         */
        public Builder expiry(int expiry)
        {
            if (expiry > 0)
            {
                this.expiry = expiry;
            }
            return this;
        }
    }

}
