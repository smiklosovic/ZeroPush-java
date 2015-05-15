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

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

/**
 * Represents Alert JSON for iOS and MacOS notifications. You instantiate this class by {@link AlertJSON.Builder#build()}.
 *
 * This class will be subsequently serialized to JSON string when build via {@link AlertJSON.Builder}. You can then pass this
 * built string to {@link IOSPushNotification.Builder#alert(String)} method.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class AlertJSON
{
    private String body;

    private String actionLocKey;

    private String locKey;

    private List<String> locArgs;

    private String launchImage;

    private AlertJSON(Builder builder)
    {
        body = builder.body;
        actionLocKey = builder.actionLocKey;
        locKey = builder.locKey;
        locArgs = builder.locArgs;
        launchImage = builder.launchImage;
    }

    public String getBody()
    {
        return body;
    }

    public String getActionLocKey()
    {
        return actionLocKey;
    }

    public String getLocKey()
    {
        return locKey;
    }

    public List<String> getLocArgs()
    {
        return locArgs;
    }

    public String getLaunchImage()
    {
        return launchImage;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public static class Builder
    {
        private static final Gson gson = new Gson();

        private String body;

        private String actionLocKey;

        private String locKey;

        private List<String> locArgs;

        private String launchImage;

        public AlertJSON build(String from)
        {
            return gson.fromJson(from, AlertJSON.class);
        }

        public String build()
        {
            return new Gson().toJson(new AlertJSON(this));
        }

        public Builder body(String body)
        {
            this.body = body;
            return this;
        }

        public Builder actionLocKey(String actionLocKey)
        {
            this.actionLocKey = actionLocKey;
            return this;
        }

        public Builder locKey(String locKey)
        {
            this.locKey = locKey;
            return this;
        }

        public Builder locArgs(String... locArgs)
        {
            if (locArgs != null)
            {
                locArgs(Arrays.asList(locArgs));
            }

            return this;
        }

        public Builder locArgs(List<String> locArgs)
        {
            this.locArgs = locArgs;
            return this;
        }

        public Builder launchImage(String launchImage)
        {
            this.launchImage = launchImage;
            return this;
        }
    }
}
