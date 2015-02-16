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
package com.zeropush.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import com.zeropush.exception.ZeroPushConfigurationException;

/**
 * Single configuration class for ZeroPush.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class ZeroPushConfiguration
{
    private static final String DEFAULT_ZEROPUSH_SERVER = "https://api.zeropush.com";

    private static URL defaultServerUrl;

    private URL serverUrl;

    private Proxy proxy;

    private String serverToken;

    private String applicationToken;

    private boolean preferServerToken = true;

    static
    {
        try
        {
            defaultServerUrl = new URL(DEFAULT_ZEROPUSH_SERVER);
        }
        catch (MalformedURLException ex)
        {
            // intentionally empty
        }
    }

    public ZeroPushConfiguration()
    {
        serverUrl = defaultServerUrl;
    }

    /**
     * Sets URL for ZeroPush REST API. By default, all operations are executed against https://api.zeropush.com.
     *
     * @param serverUrl server for ZeroPush REST API.
     * @return this configuration
     * @throws ZeroPushConfigurationException when serverUrl is not a valid URL address.
     */
    public ZeroPushConfiguration setServer(String serverUrl) throws ZeroPushConfigurationException
    {
        Validate.notNullOrEmpty(serverUrl, "Server URL is a null object or an empty String.");

        try
        {
            URL url = new URL(serverUrl);
            this.serverUrl = url;
        }
        catch (MalformedURLException ex)
        {
            throw new ZeroPushConfigurationException(String.format("Specified serverUrl (%s) is not a valid URL address.", serverUrl));
        }

        return this;
    }

    /**
     * Sets URL for ZeroPush REST API. By default, all operations are executed against https://api.zeropush.com.
     *
     * @param serverUrl server for ZeroPush REST API.
     * @return this configuration
     * @throws ZeroPushConfigurationException when serverUrl is not a valid URL address.
     */
    public ZeroPushConfiguration setServer(URL serverUrl)
    {
        Validate.notNull(serverUrl, "Server URL is a null object.");

        this.serverUrl = serverUrl;

        return this;
    }

    /**
     *
     * @return server url, by default https://api.zeropush.com
     */
    public URL getServer()
    {
        return serverUrl;
    }

    /**
     * Gets authetication token to use while performing some operations. Preferred key to return can be set via
     * {@link #preferServerToken} and {@link #preferAppToken} respectively. If preference methods are not used, by default this
     * methods returns server API key.
     *
     * @return authentication token
     */
    public String getAuthToken()
    {
        return preferServerToken ? getServerToken() : getApplicationToken();
    }

    /**
     * By calling this method, {@link #getAuthToken()} will always return app_token.
     *
     * @return this configuration
     */
    public ZeroPushConfiguration preferAppToken()
    {
        preferServerToken = false;
        return this;
    }

    /**
     * By calling this method, {@link #getAuthToken()} will always return server_token.
     *
     * @return this configuration
     */
    public ZeroPushConfiguration preferServerToken()
    {
        preferServerToken = true;
        return this;
    }

    /**
     *
     * @param serverToken server token to set
     * @return this configuration
     * @throws ZeroPushConfigurationException when {@code serverToken} is null object or an empty String.
     */
    public ZeroPushConfiguration setServerToken(String serverToken)
    {
        Validate.notNullOrEmpty(serverToken, "Server token is a null object or an empty String.");
        this.serverToken = serverToken;
        return this;
    }

    /**
     *
     * @return server token
     */
    public String getServerToken()
    {
        return serverToken;
    }

    /**
     *
     * @param applicationToken application token to set
     * @return this configuration
     * @throws ZeroPushConfigurationException when {@code applicationToken} is null object or an empty String.
     */
    public ZeroPushConfiguration setApplicationToken(String applicationToken)
    {
        Validate.notNullOrEmpty(applicationToken, "Application token is a null object or an empty String.");
        this.applicationToken = applicationToken;
        return this;
    }

    /**
     *
     * @return application token
     */
    public String getApplicationToken()
    {
        return applicationToken;
    }

    /**
     *
     * @param serverToken server token to set
     * @param applicationToken application token to set
     * @return this configuration
     * @throws ZeroPushConfigurationException when either {@code serverToken} or {@code applicationToken} is a null object or an empty String.
     */
    public ZeroPushConfiguration setTokens(String serverToken, String applicationToken)
    {
        Validate.notNullOrEmpty(serverToken, "Server token is a null object or an empty String.");
        Validate.notNullOrEmpty(applicationToken, "Application token is a null object or an empty String.");

        this.serverToken = serverToken;
        this.applicationToken = applicationToken;

        return this;
    }

    /**
     * When set, all requests to ZeroPush server will go through this proxy server.
     *
     * @param proxy a proxy server to send all requests through
     * @return this configuration
     */
    public ZeroPushConfiguration setProxy(Proxy proxy)
    {
        Validate.notNull(proxy, "Proxy URI is a null object.");
        this.proxy = proxy;
        return this;
    }

    /**
     *
     * @return true if proxy was set via {@link #setProxy(Proxy)}
     */
    public boolean withProxy()
    {
        return proxy != null;
    }

    /**
     *
     * @return {@link Proxy}
     */
    public Proxy getProxy()
    {
        return proxy;
    }

    private final static class Validate
    {
        public static void notNull(Object object, String exceptionMessage)
        {
            if (object == null)
            {
                throw new ZeroPushConfigurationException(exceptionMessage);
            }
        }

        public static void notNullOrEmpty(String object, String exceptionMessage)
        {
            if (object == null || object.length() == 0)
            {
                throw new ZeroPushConfigurationException(exceptionMessage);
            }
        }
    }

    /**
     *
     * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
     *
     */
    public class Proxy
    {
        private String scheme;

        private String hostname;

        private int port;

        private Proxy(ProxyBuilder builder)
        {
            this.scheme = builder.scheme;
            this.hostname = builder.hostname;
            this.port = builder.port;
        }

        public String getScheme()
        {
            return scheme;
        }

        public String getHostname()
        {
            return hostname;
        }

        public int getPort()
        {
            return port;
        }
    }

    /**
     * Builds a {@link Proxy}
     *
     * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
     *
     */
    public static class ProxyBuilder
    {
        private String scheme = "http";

        private String hostname = "localhost";

        private int port = 8080;

        public ProxyBuilder withScheme(String scheme)
        {
            this.scheme = scheme;
            return this;
        }

        public ProxyBuilder withHostname(String hostname)
        {
            this.hostname = hostname;
            return this;
        }

        public ProxyBuilder withPort(int port)
        {
            this.port = port;
            return this;
        }

        /**
         *
         * @return build Proxy, by default scheme is "http", hostname "localhost" and port 8080.
         */
        public Proxy build()
        {
            return new ZeroPushConfiguration().new Proxy(this);
        }
    }
}
