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
package com.zeropush.verify;

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

/**
 * Endpoint dealing with verification of credentials.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class VerifyCredentials extends Endpoint<VerifyCredentialsResponse>
{
    private String authToken;

    public VerifyCredentials()
    {
    }

    public VerifyCredentials(String authToken)
    {
        this.authToken = authToken;
    }

    @Override
    public VerifyCredentialsResponse execute() throws ZeroPushEndpointException
    {
        String authToken = resolveAuthToken();

        if (authToken == null)
        {
            throw new ZeroPushEndpointException("Unable to determine the authentication token to verify.");
        }

        HttpUriRequest request = new ZeroPushRequestBuilder(RequestType.GET, ZeroPush.getConfiguration())
            .withAuthToken(authToken)
            .withPath("/verify_credentials")
            .build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        VerifyCredentialsResponse verifyCredentialsResponse = new VerifyCredentialsResponse();

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        verifyCredentialsResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (verifyCredentialsResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            verifyCredentialsResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            verifyCredentialsResponse.setMessage(httpResponseUtil.getBodyAs(VerifyCredentialsResponse.Message.class));
        }

        return verifyCredentialsResponse;
    }

    // helpers

    private String resolveAuthToken()
    {
        String defaultAuthToken = this.authToken;
        String authToken = ZeroPush.getConfiguration().getAuthToken();

        if (defaultAuthToken != null && defaultAuthToken.length() != 0)
        {
            return defaultAuthToken;
        }
        else if (authToken != null && authToken.length() != 0)
        {
            return authToken;
        }

        return null;
    }
}
