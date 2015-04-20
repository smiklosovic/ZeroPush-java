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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import com.google.gson.Gson;
import com.zeropush.exception.ZeroPushEndpointException;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class HttpResponseUtil
{
    private final HttpResponse response;

    /**
     *
     * @param response reponse to operate upon
     * @throws IllegalStateException if {@code response} is a null object
     */
    public HttpResponseUtil(HttpResponse response)
    {
        if (response == null)
        {
            throw new IllegalStateException("HttpResponse must be specified.");
        }

        this.response = response;
    }

    /**
     *
     * @param deserializationClass class to which a body of request will be deserialized to
     * @param <T> type of class to deserialize the body of the request to
     * @return deserialized body of a request
     * @throws ZeroPushEndpointException when deserialization was unsuccessful
     */
    public <T> T getBodyAs(Class<T> deserializationClass)
    {
        HttpEntity entity = response.getEntity();

        if (entity == null)
        {
            throw new IllegalStateException("Entity is a null object.");
        }

        String bodyString = null;

        T deserializedBody = null;

        try
        {
            bodyString = getBodyAsString(entity);
            deserializedBody = new Gson().fromJson(bodyString, deserializationClass);
        } catch (Exception e)
        {
            throw new ZeroPushEndpointException("Unable to get deserialized body as " + deserializationClass.getName());
        }

        return deserializedBody;
    }

    /**
     *
     * @return status code of a response
     * @throws ZeroPushEndpointException when status line is a null object
     */
    public int getStatusCode()
    {
        StatusLine statusLine = response.getStatusLine();

        if (statusLine == null)
        {
            throw new ZeroPushEndpointException("Status line to get status code from is a null object.");
        }

        return statusLine.getStatusCode();
    }

    /**
     *
     * @return headers of a response
     */
    public Header[] getHeaders()
    {
        return response.getAllHeaders();
    }

    private String getBodyAsString(HttpEntity entity) {

        StringBuilder sb = new StringBuilder();

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(entity.getContent()));

            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append("\n");
            }
        } catch (Exception ex)
        {
        } finally
        {
            if (reader != null)
            {
                try {
                    reader.close();
                } catch (IOException ex)
                {

                } finally {
                    reader = null;
                }
            }
        }

        return sb.toString();
    }
}
