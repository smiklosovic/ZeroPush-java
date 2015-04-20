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

import java.util.Collections;
import java.util.List;

import com.zeropush.exception.ZeroPushNotificationValidationException;

/**
 * Represents base of all possible notification types.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public abstract class ZeroPushNotification
{
    private List<String> deviceTokens;

    public List<String> getDeviceTokens()
    {
        return Collections.unmodifiableList(deviceTokens);
    }

    protected void setDeviceTokens(List<String> deviceTokens)
    {
        this.deviceTokens = deviceTokens;
    }

    /**
     * Tells which platform this notification targets.
     *
     * @return platfrom which this notification is sent to
     */
    public abstract Platform provides();

    /**
     * Validates built notification.
     *
     * @throws ZeroPushNotificationValidationException in case notification is not valid
     */
    public abstract void validate() throws ZeroPushNotificationValidationException;
}
