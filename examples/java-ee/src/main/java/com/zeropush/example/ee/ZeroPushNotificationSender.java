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
package com.zeropush.example.ee;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.http.HttpStatus;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushEndpointException;
import com.zeropush.model.ZeroPushNotification;
import com.zeropush.model.ZeroPushNotificationQuota;
import com.zeropush.model.ZeroPushNotificationResponse;

/**
 * Bean which sends a notification.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushNotificationSender {

    @Inject
    private ZeroPushPropertiesLoader propertyLoader;

    @PostConstruct
    private void setupZeroPush()
    {
        String serverToken = propertyLoader.getProperty("zeropush.token.server");
        String applicationToken = propertyLoader.getProperty("zeropush.token.app");

        ZeroPush.getConfiguration().setTokens(serverToken, applicationToken);
    }

    /**
     * Sends a notification.
     *
     * @param notification notification to send
     * @return quotas
     * @throws ZeroPushEndpointException if something goes wrong (status code does not equals to 200)
     */
    public ZeroPushNotificationQuota sendNotification(ZeroPushNotification notification)
    {
        ZeroPushNotificationResponse response = ZeroPush.notification(notification).send();

        if (response.getStatusCode() != HttpStatus.SC_OK)
        {
            throw new ZeroPushEndpointException(
                String.format("Unable to send notification. Status code: '%s'. Response message: '%s'. Error cause: '%s'.",
                    response.getStatusCode(),
                    response.getResponseError().getMessage(),
                    response.getResponseError().getError()));
        }

        return response.getQuota();
    }
}
