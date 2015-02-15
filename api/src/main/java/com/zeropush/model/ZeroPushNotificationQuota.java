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

/**
 * Default value "-1" for fields means it was not specified or it was unable to get.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushNotificationQuota
{
    private long XPushQuota = -1;

    private long XPushQuotaRemaining = -1;

    private long XPushQuotaOverage = -1;

    private long XPushQuotaReset = -1;

    public long getXPushQuota()
    {
        return XPushQuota;
    }

    public void setXPushQuota(long xPushQuota)
    {
        XPushQuota = xPushQuota;
    }

    public long getXPushQuotaRemaining()
    {
        return XPushQuotaRemaining;
    }

    public void setXPushQuotaRemaining(long xPushQuotaRemaining)
    {
        XPushQuotaRemaining = xPushQuotaRemaining;
    }

    public long getXPushQuotaOverage()
    {
        return XPushQuotaOverage;
    }

    public void setXPushQuotaOverage(long xPushQuotaOverage)
    {
        XPushQuotaOverage = xPushQuotaOverage;
    }

    public long getXPushQuotaReset()
    {
        return XPushQuotaReset;
    }

    public void setXPushQuotaReset(long xPushQuotaReset)
    {
        XPushQuotaReset = xPushQuotaReset;
    }
}
