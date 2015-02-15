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

import java.util.List;

import com.zeropush.response.ZeroPushResponse;

/**
 * This is the complete answer from server with {@link SenderResponse} itself and meta information as error messages and quota
 * information after we sent a notification via {@code /notify} or {@code /broadcast} endpoints.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushNotificationResponse extends ZeroPushResponse
{
    // representation of JSON message sent back from server
    private SenderResponse senderResponse;

    // representation of headers which server sends us along the JSON senderResponse
    private ZeroPushNotificationQuota quota;

    public SenderResponse getSenderResponse()
    {
        return senderResponse;
    }

    public void setSenderResponse(SenderResponse senderResponse)
    {
        this.senderResponse = senderResponse;
    }

    public ZeroPushNotificationQuota getQuota()
    {
        return quota;
    }

    public void setQuota(ZeroPushNotificationQuota quota)
    {
        this.quota = quota;
    }

    public class SenderResponse
    {
        private int sent_count;

        private List<String> inactive_tokens;

        private List<String> unregistered_tokens;

        public int getSentCount()
        {
            return sent_count;
        }

        public void setSentCount(int setCount)
        {
            this.sent_count = setCount;
        }

        public List<String> getInactiveTokens()
        {
            return inactive_tokens;
        }

        public void setInactiveTokens(List<String> inactiveTokens)
        {
            this.inactive_tokens = inactiveTokens;
        }

        public List<String> getUnregisteredTokens()
        {
            return unregistered_tokens;
        }

        public void setUnregisteredTokens(List<String> unregisteredTokens)
        {
            this.unregistered_tokens = unregisteredTokens;
        }
    }
}
