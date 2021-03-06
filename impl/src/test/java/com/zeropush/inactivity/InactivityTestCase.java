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

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.inactivity.InactivityResponse;
import com.zeropush.test.AbstractZeroPushTestCase;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class InactivityTestCase extends AbstractZeroPushTestCase
{
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void inactivityTest()
    {
        InactivityResponse inactivityResponse = ZeroPush.inactivity().get().execute();

        Assert.assertEquals(HttpStatus.SC_OK, inactivityResponse.getStatusCode());
        Assert.assertTrue(inactivityResponse.getInactivities().isEmpty());
    }

    @Test
    public void inactivitySinceTest()
    {
        InactivityResponse inactivityResponse = ZeroPush.inactivity().since(1363033513).execute();

        Assert.assertEquals(HttpStatus.SC_OK, inactivityResponse.getStatusCode());
        Assert.assertTrue(inactivityResponse.getInactivities().isEmpty());
    }

    @Test
    public void inactivityWithNegativeSinceTest()
    {
        exception.expect(IllegalArgumentException.class);

        ZeroPush.inactivity().since(-1).execute();
    }
}
