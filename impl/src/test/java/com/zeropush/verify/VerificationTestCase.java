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

import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.zeropush.ZeroPush;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.response.ZeroPushResponseError;
import com.zeropush.test.LoadingPropertiesTestCase;
import com.zeropush.verify.VerifyCredentialsResponse;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(JUnit4.class)
public class VerificationTestCase extends LoadingPropertiesTestCase
{
    private static final String INVALID_CREDENTIALS = "gcmdev_2ixrMtr";

    private static String VALID_APP_TOKEN;

    private static String VALID_SERVER_TOKEN;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @BeforeClass
    public static void setup() throws Exception
    {
        loadProperties();
        ZeroPush.setConfiguration(new ZeroPushConfiguration()); // only for testing purposes
        ZeroPush.getConfiguration().setServerToken(System.getProperty("zeropush.token.server", getProperty("zeropush.token.server")));
        ZeroPush.getConfiguration().setApplicationToken(System.getProperty("zeropush.token.app", getProperty("zeropush.token.app")));

        VALID_APP_TOKEN = ZeroPush.getConfiguration().getApplicationToken();
        VALID_SERVER_TOKEN = ZeroPush.getConfiguration().getAuthToken();
    }

    @Test
    public void invalidCredentialsTest()
    {
        VerifyCredentialsResponse response = ZeroPush.verification().credentials(INVALID_CREDENTIALS).execute();
        Assert.assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusCode());

        ZeroPushResponseError error = response.getResponseError();

        Assert.assertEquals("authorization error", error.getError());
        Assert.assertEquals("Please provide a valid authentication token parameter or HTTP Authorization header.", error.getMessage());
        Assert.assertEquals("https://zeropush.com/documentation/api_reference", error.getReferenceUrl());
    }

    @Test
    public void validCredentialsTestAppToken()
    {
        VerifyCredentialsResponse response = ZeroPush.verification().credentials(VALID_APP_TOKEN).execute();
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

        Assert.assertEquals("authenticated", response.getMessage().getMessage());
        Assert.assertEquals("app_token", response.getMessage().getAuthTokenType());
    }

    @Test
    public void validCredentialsTestPreferingServerToken()
    {
        ZeroPush.getConfiguration().setServerToken(VALID_SERVER_TOKEN);
        ZeroPush.getConfiguration().preferServerToken(); // this is default

        VerifyCredentialsResponse response = ZeroPush.verification().credentials().execute();
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());

        Assert.assertEquals("authenticated", response.getMessage().getMessage());
        Assert.assertEquals("server_token", response.getMessage().getAuthTokenType());
    }

    @Test
    public void validCredentialsFromConfigurationTest()
    {
        ZeroPush.getConfiguration().setApplicationToken(VALID_APP_TOKEN);
        ZeroPush.getConfiguration().preferAppToken();

        VerifyCredentialsResponse response = ZeroPush.verification().credentials().execute();
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCode());
        Assert.assertEquals("app_token", response.getMessage().getAuthTokenType());
    }

}
