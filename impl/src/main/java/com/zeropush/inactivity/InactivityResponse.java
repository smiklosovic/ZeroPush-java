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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zeropush.response.ZeroPushResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class InactivityResponse extends ZeroPushResponse
{
    private List<InactivityEntry> inactivities = new ArrayList<InactivityResponse.InactivityEntry>();

    void setInactivities(List<InactivityEntry> inactivities)
    {
        this.inactivities = inactivities;
    }

    public List<InactivityEntry> getInactivities()
    {
        return Collections.unmodifiableList(inactivities);
    }

    public class InactivityEntry
    {
        private String device_token;

        private String marked_inactive_at;

        private long marked_inactive;

        public String getDeviceToken()
        {
            return device_token;
        }

        public void setDeviceToken(String device_token)
        {
            this.device_token = device_token;
        }

        public String getMarkedInactiveAt()
        {
            return marked_inactive_at;
        }

        public void setMarkedInactiveAt(String marked_inactive_at)
        {
            this.marked_inactive_at = marked_inactive_at;
        }

        public long getMarkedInactive()
        {
            return marked_inactive;
        }

        public void setMarkedInactive(long marked_inactive)
        {
            this.marked_inactive = marked_inactive;
        }
    }
}