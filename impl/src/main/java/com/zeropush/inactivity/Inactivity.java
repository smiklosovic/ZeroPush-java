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
package com.zeropush.inactivity;

import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import com.zeropush.ZeroPush;
import com.zeropush.exception.ZeroPushEndpointException;
import com.zeropush.inactivity.InactivityResponse.InactivityEntry;
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
public class Inactivity extends Endpoint<InactivityResponse>
{
    @Override
    public InactivityResponse execute() throws ZeroPushEndpointException
    {
        HttpUriRequest request = new ZeroPushRequestBuilder(RequestType.GET, ZeroPush.getConfiguration())
            .withAuthToken(ZeroPush.getConfiguration().getServerToken())
            .withPath("/inactive_tokens")
            .build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        InactivityResponse inactivityResponse = new InactivityResponse();
        inactivityResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (inactivityResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            inactivityResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            inactivityResponse.setInactivities(Arrays.asList(httpResponseUtil.getBodyAs(InactivityEntry[].class)));
        }

        return inactivityResponse;
    }
}
