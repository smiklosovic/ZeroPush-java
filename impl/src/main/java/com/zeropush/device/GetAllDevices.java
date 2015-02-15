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

import java.util.Arrays;

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
public class GetAllDevices extends Endpoint<DevicesResponse>
{
    private int page = 0;

    private int perPage = 25;

    @Override
    public DevicesResponse execute() throws ZeroPushEndpointException
    {
        ZeroPushRequestBuilder requestBuilder = new ZeroPushRequestBuilder(RequestType.GET, ZeroPush.getConfiguration());

        requestBuilder.withAuthToken(ZeroPush.getConfiguration().getServerToken()).withPath("/devices");

        if (page > 0)
        {
            requestBuilder.withParameter("page", page);
        }

        if (perPage > 0)
        {
            requestBuilder.withParameter("perPage", page);
        }

        HttpUriRequest request = requestBuilder.build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        DevicesResponse devicesResponse = new DevicesResponse();
        devicesResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (devicesResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            devicesResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            devicesResponse.setDevices(Arrays.asList(httpResponseUtil.getBodyAs(Device[].class)));
        }

        return devicesResponse;
    }

    public GetAllDevices page(int page)
    {
        if (page > 0)
        {
            this.page = page;
        }

        return this;
    }

    public GetAllDevices perPage(int perPage)
    {
        if (perPage > 0 && page <= 100)
        {
            this.perPage = perPage;
        }

        return this;
    }
}
