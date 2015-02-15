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
package com.zeropush.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class LoadingPropertiesTestCase
{
    private static final Logger logger = Logger.getLogger(LoadingPropertiesTestCase.class.getName());

    public static final String DEFAULT_PROPERTIES_FILE_NAME = "src/test/resources/zeropush.properties";

    private static Properties properties = new Properties();

    public static final void loadProperties(String fileName) throws Exception
    {
        String propertiesFileName = null;

        if (fileName == null || fileName.length() == 0)
        {
            propertiesFileName = DEFAULT_PROPERTIES_FILE_NAME;
        }
        else
        {
            propertiesFileName = fileName;
        }

        File propertiesFile = new File(propertiesFileName);

        if (!propertiesFile.exists())
        {
            logger.info(String.format("Specified properties file '%s' does not exist.", propertiesFile.getAbsolutePath()));
            return;
        }

        properties.load(new FileInputStream(propertiesFile));
    }

    public static final void loadProperties() throws Exception
    {
        loadProperties(DEFAULT_PROPERTIES_FILE_NAME);
    }

    public static String getProperty(String propertyKey)
    {
        return properties.getProperty(propertyKey);
    }
}
