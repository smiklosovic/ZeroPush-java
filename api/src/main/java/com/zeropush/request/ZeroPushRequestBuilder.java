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
package com.zeropush.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.configuration.ZeroPushConfiguration.Proxy;
import com.zeropush.exception.ZeroPushRequestBuilderException;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushRequestBuilder
{
    private final RequestType requestType;

    private final ZeroPushConfiguration configuration;

    private String path;

    private final List<NameValuePair> nvps = new ArrayList<NameValuePair>();

    public ZeroPushRequestBuilder(RequestType requestType, ZeroPushConfiguration configuration)
    {
        if (requestType == null)
        {
            throw new ZeroPushRequestBuilderException("Request type must be specified.");
        }

        if (configuration == null)
        {
            throw new ZeroPushRequestBuilderException("ZeroPushConfiguration must be specified.");
        }

        this.requestType = requestType;
        this.configuration = configuration;
    }

    public ZeroPushRequestBuilder withPath(String path)
    {
        this.path = path;
        return this;
    }

    public ZeroPushRequestBuilder withAuthToken(String authToken)
    {
        return withParameter("auth_token", authToken);
    }

    public ZeroPushRequestBuilder withDeviceToken(String deviceToken)
    {
        return withParameter("device_token", deviceToken);
    }

    public ZeroPushRequestBuilder withParameter(String name, int value)
    {
        return withParameter(name, Integer.toString(value));
    }

    public ZeroPushRequestBuilder withParameter(String name, String value)
    {
        if (name != null && value != null & name.length() != 0)
        {
            nvps.add(new BasicNameValuePair(name, value));
        }

        return this;
    }

    public HttpUriRequest build()
    {
        URI uri = null;

        try
        {
            uri = new URIBuilder(configuration.getServer().toURI())
                .setPath(path)
                .setParameters(nvps)
                .build();
        }
        catch (URISyntaxException e)
        {
            throw new ZeroPushRequestBuilderException("Unable to build URI.", e);
        }

        HttpRequestBase request = null;

        switch (requestType)
        {
            case GET:
                request = new HttpGet(uri);
                break;
            case POST:
                request = new HttpPost(uri);
                break;
            case DELETE:
                request = new HttpDelete(uri);
                break;
            case PUT:
                request = new HttpPut(uri);
                break;
            case PATCH:
                request = new HttpPatch(uri);
                break;
            default:
                throw new UnsupportedOperationException("Type of request to build is not supported yet.");
        }

        if (configuration.withProxy())
        {
            Proxy proxy = configuration.getProxy();
            HttpHost proxyHost = new HttpHost(proxy.getHostname(), proxy.getPort(), proxy.getScheme());
            RequestConfig config = RequestConfig.custom().setProxy(proxyHost).build();
            request.setConfig(config);
        }

        return request;
    }
}
