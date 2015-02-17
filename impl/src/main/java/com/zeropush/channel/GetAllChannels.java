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
package com.zeropush.channel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;

import com.zeropush.ZeroPush;
import com.zeropush.model.Channel;
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
public class GetAllChannels implements Endpoint<ChannelsResponse>
{
    private int page = 0;

    private int perPage = 25;

    @Override
    public ChannelsResponse execute()
    {
        ZeroPushRequestBuilder requestBuilder = new ZeroPushRequestBuilder(RequestType.GET, ZeroPush.getConfiguration())
            .withPath("/channels")
            .withAuthToken(ZeroPush.getConfiguration().getServerToken());

        if (page > 0)
        {
            requestBuilder.withParameter("page", page);
        }

        if (perPage > 0)
        {
            requestBuilder.withParameter("perPage", perPage);
        }

        HttpUriRequest request = requestBuilder.build();

        HttpResponse response = new ZeroPushRequestExecutor().execute(request);

        ChannelsResponse channelsResponse = new ChannelsResponse();

        HttpResponseUtil httpResponseUtil = new HttpResponseUtil(response);

        channelsResponse.setStatusCode(httpResponseUtil.getStatusCode());

        if (channelsResponse.getStatusCode() != HttpStatus.SC_OK)
        {
            channelsResponse.setResponseError(httpResponseUtil.getBodyAs(ZeroPushResponseError.class));
        }
        else
        {
            final String[] channelNames = httpResponseUtil.getBodyAs(String[].class);

            final List<Channel> channels = new ArrayList<Channel>();

            for (final String channelName : channelNames)
            {
                channels.add(new Channel(channelName));
            }

            channelsResponse.setChannels(channels);
        }

        return channelsResponse;
    }

    public GetAllChannels page(int page)
    {
        if (page > 0)
        {
            this.page = page;
        }

        return this;
    }

    public GetAllChannels perPage(int perPage)
    {
        if (perPage > 0 && perPage <= 100)
        {
            this.perPage = perPage;
        }

        return this;
    }
}
