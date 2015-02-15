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
package com.zeropush.response;

/**
 * Generic response from ZeroPush REST API.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public abstract class ZeroPushResponse
{
    private int statusCode = -1;

    private ZeroPushResponseError responseError = new ZeroPushResponseError();

    /**
     *
     * @param statusCode status code to set, has to be bigger then or equal to 100.
     */
    public void setStatusCode(int statusCode)
    {
        if (statusCode >= 100)
        {
            this.statusCode = statusCode;
        }
    }

    /**
     *
     * @return status code of REST endpoint call. Returned value "-1" means that status
     *         code was not set by {@link #setStatusCode(int)}.
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * In case some response does not have status code 200, it means there was some error. This method gets underlying error
     * model class.
     *
     * @return {@link ZeroPushResponseError}
     */
    public ZeroPushResponseError getResponseError()
    {
        return responseError;
    }

    public void setResponseError(ZeroPushResponseError responseError)
    {
        this.responseError = responseError;
    }
}
