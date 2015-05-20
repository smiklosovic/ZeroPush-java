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
package com.zeropush.model.token;

import java.util.regex.Pattern;

import com.zeropush.model.Platform;
import com.zeropush.model.notification.exception.InvalidDeviceTokenException;

/**
 * Validates if a device token for some platform is valid or not.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public class TokenValidator
{
    private static final Pattern ANDROID_DEVICE_TOKEN = Pattern.compile("(?i)[0-9a-z\\-_]{100,}");

    private static final Pattern IOS_DEVICE_TOKEN = Pattern.compile("(?i)[a-f0-9 -]{64,}");

    /**
     * Validates a token for given platform.
     *
     * @param platform platform of a token to validate
     * @param token token to validate
     * @throws InvalidDeviceTokenException if token is not valid
     * @throws IllegalArgumentException if token is a null object or an empty String
     */
    public void validate(Platform platform, String token) throws InvalidDeviceTokenException
    {
        if (token == null || token.isEmpty())
        {
            throw new IllegalArgumentException("Token to validate is a null object or an empty String!");
        }

        boolean valid = false;

        switch (platform)
        {
            case ANDROID_GCM:
                valid = ANDROID_DEVICE_TOKEN.matcher(token).matches();
                break;
            case SAFARI:
            case IOS:
                valid = IOS_DEVICE_TOKEN.matcher(token).matches();
                break;
            default:
                throw new IllegalStateException(
                    String.format("Unable to determine platform for validation. You entered: %s" , platform.name()));
        }

        if (!valid)
        {
            throw new InvalidDeviceTokenException(String.format("Token %s is not valid", token));
        }
    }
}
