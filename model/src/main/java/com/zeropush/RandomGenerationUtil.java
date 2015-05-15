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
package com.zeropush;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.zeropush.model.Platform;

/**
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class RandomGenerationUtil
{

    private static final Random r = new Random();

    /**
     * Generates random map.
     *
     * @param count number of entries generated map will contain
     * @return map of {@code count} entries, randomly filled with keys and values
     */
    public static Map<String, String> generateRandomMap(int count)
    {

        Map<String, String> randomData = new HashMap<String, String>();

        for (int i = 0; i < count; i++)
        {
            String randomString = UUID.randomUUID().toString().substring(0, 16);
            String randomKey = randomString.substring(0, 16);
            randomData.put(randomKey, randomString);
        }

        return randomData;
    }

    /**
     * Generates random boolean value.
     *
     * @return random boolean
     */
    public static boolean generateRandomBoolean()
    {
        return r.nextBoolean();
    }

    /**
     * Generates random number in given range.
     *
     * @param from lower bound of generated number
     * @param to upper bound of generated number
     * @return random number from given range
     */
    public static int generateRandomNumber(int from, int to)
    {
        return r.nextInt(to - from + 1) + from;
    }

    /**
     *
     * @return randomly generated platform
     * @see Platform
     */
    public static Platform generateRandomPlatform()
    {
        List<Platform> values = Collections.unmodifiableList(Arrays.asList(Platform.values()));
        int size = values.size();
        return values.get(r.nextInt(size));
    }

    /**
     * Generates random alpha-numeric string
     *
     * @param length length of randomly generated alpha-numeric string
     * @return random alpha-numeric string of given length
     */
    public static String randomAlphanumeric(int length)
    {
        StringBuilder builder = new StringBuilder();

        while (builder.length() < length) {
            builder.append(UUID.randomUUID().toString());
        }

        return builder.substring(0, length);
    }
}
