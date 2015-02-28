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
package com.zeropush.example.ee;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class ZeroPushPropertiesLoader
{
    private static final Logger logger = Logger.getLogger(ZeroPushPropertiesLoader.class.getName());

    private static final String PROPERTIES_FILE_NAME = "zeropush.properties";

    private static final Properties properties;

    static
    {
        properties = getProperties();
    }

    public String getProperty(String propertyKey)
    {
        return properties.getProperty(propertyKey);
    }

    private static Properties getProperties()
    {
        InputStream is = ZeroPushPropertiesLoader.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);

        if (is == null)
        {
            throw new IllegalStateException(String.format("Unable to load property file %s.", PROPERTIES_FILE_NAME));
        }

        Properties properties = null;

        try
        {
            properties = new Properties();
            properties.load(is);
        } catch (IOException ex)
        {
            throw new IllegalStateException(String.format("Unable to load property file %s.", PROPERTIES_FILE_NAME));
        }

        logger.log(Level.INFO, "ZeroPush properties has been loaded.");

        return properties;
    }

}
