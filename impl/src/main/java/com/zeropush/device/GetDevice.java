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
package com.zeropush.device;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushEndpointException;
import com.zeropush.model.Device;
import com.zeropush.model.Endpoint;
import com.zeropush.request.RequestType;
import com.zeropush.request.ZeroPushRequestBuilder;
import com.zeropush.request.ZeroPushRequestExecutor;
import com.zeropush.response.HttpResponseUtil;
import com.zeropush.response.ZeroPushResponseError;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class GetDevice implements Endpoint<DeviceResponse>
{
    private final String deviceToken;

    public GetDevice(String deviceToken)
    {
        if (deviceToken == null || deviceToken.length() == 0)
        {
            throw new ZeroPushEndpointException("The device token of a device to get is a null object or an empty String.");
        }

        this.deviceToken = deviceToken;
    }

    @Override
    public DeviceResponse execute() throws ZeroPushEndpointException
    {
        HttpUriRequest request = new ZeroPushRequestBuilder(RequestType.GET, ZeroPush.getConfiguration())
            .withAuthToken(ZeroPush.getConfiguration().getAuthToken())
            .withPath("/devices/" + deviceToken)
            .build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        DeviceResponse deviceResponse = new DeviceResponse();
        deviceResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (deviceResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            deviceResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            deviceResponse.setDevice(httpResponseUtil.getBodyAs(Device.class));
        }

        return deviceResponse;
    }
}
