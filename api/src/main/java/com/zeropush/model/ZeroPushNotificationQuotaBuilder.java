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
package com.zeropush.model;

import org.apache.http.Header;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushNotificationQuotaBuilder
{
    private final Header[] headers;

    public ZeroPushNotificationQuotaBuilder(Header[] headers)
    {
        this.headers = headers;
    }

    public ZeroPushNotificationQuota build()
    {
        ZeroPushNotificationQuota quota = new ZeroPushNotificationQuota();

        if (headers == null || headers.length == 0)
        {
            return quota;
        }

        for (final Header header : headers)
        {
            if (header.getValue() == null || header.getValue().equals("null"))
            {
                continue;
            }

            final String headerValue = header.getValue();

            if (header.getName().equals("X-Push-Quota"))
            {
                quota.setXPushQuota(Long.parseLong(headerValue));
            }
            else if (header.getName().equals("X-Push-Quota-Overage"))
            {
                quota.setXPushQuotaOverage(Long.parseLong(headerValue));
            }
            else if (header.getName().equals("X-Push-Quota-Remaining"))
            {
                quota.setXPushQuotaRemaining(Long.parseLong(headerValue));
            }
            else if (header.getName().equals("X-Push-Quota-Reset"))
            {
                quota.setXPushQuotaReset(Long.parseLong(headerValue));
            }
        }

        return quota;
    }
}
