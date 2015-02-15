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
package com.zeropush.subscribe;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushEndpointException;
import com.zeropush.model.Endpoint;
import com.zeropush.request.RequestType;
import com.zeropush.request.ZeroPushRequestBuilder;
import com.zeropush.request.ZeroPushRequestExecutor;
import com.zeropush.response.HttpResponseUtil;
import com.zeropush.response.ZeroPushResponseError;
import com.zeropush.subscribe.SubscriptionResponse.SubscriptionEntry;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class Subscription extends Endpoint<SubscriptionResponse>
{
    private final String deviceToken;

    private final String channel;

    private boolean subscribe = true;

    public Subscription(String deviceToken, String channel)
    {
        if (deviceToken == null || deviceToken.length() == 0)
        {
            throw new ZeroPushEndpointException("Device token to subscribe a channel to is a null object or an empty String.");
        }

        if (channel == null || channel.length() == 0)
        {
            throw new ZeroPushEndpointException("Channel to subscribe a device to is a null object or an empty String.");
        }

        this.deviceToken = deviceToken;
        this.channel = channel;
    }

    @Override
    public SubscriptionResponse execute() throws ZeroPushEndpointException
    {
        ZeroPushRequestBuilder requestBuilder = null;

        if (subscribe)
        {
            requestBuilder = new ZeroPushRequestBuilder(RequestType.POST, ZeroPush.getConfiguration());
        }
        else
        {
            requestBuilder = new ZeroPushRequestBuilder(RequestType.DELETE, ZeroPush.getConfiguration());
        }

        HttpUriRequest request = requestBuilder.withAuthToken(ZeroPush.getConfiguration().getAuthToken())
            .withPath("/subscribe/" + channel)
            .withDeviceToken(deviceToken)
            .build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        subscriptionResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (subscriptionResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            subscriptionResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            subscriptionResponse.setSubscription(httpResponseUtil.getBodyAs(SubscriptionEntry.class));
        }

        return subscriptionResponse;
    }

    public Subscription subscribe()
    {
        subscribe = true;
        return this;
    }

    public Subscription unsubscribe()
    {
        subscribe = false;
        return this;
    }
}
