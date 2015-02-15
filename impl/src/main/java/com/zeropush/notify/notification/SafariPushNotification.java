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

        public Builder title(String title)
        {
            this.title = title;
            return this;
        }

        public Builder body(String body)
        {
            this.body = body;
            return this;
        }

        public Builder addUrlArg(String urlArg)
        {
            if (urlArg != null && urlArg.length() != 0)
            {
                urlArgs.add(urlArg);
            }

            return this;
        }

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

        public Builder label(String label)
        {
            this.label = label;
            return this;
        }

        public Builder expiry(int expirty)
        {
            if (expirty > 0)
            {
                this.expiry = expirty;
            }
            return this;
        }
    }

}
