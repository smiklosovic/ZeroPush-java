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

import java.util.List;

/**
 * Generates device tokens.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public enum TokenGenerator
{

    ANDROID
    {
        @Override
        public List<String> generate(int count)
        {
            return new AndroidToken().generate(count);
        }

        @Override
        public String generate()
        {
            return new AndroidToken().generate(1).get(0);
        }
    },
    IOS
    {

        @Override
        public List<String> generate(int count)
        {
            return new IOSToken().generate(count);
        }

        @Override
        public String generate()
        {
            return new IOSToken().generate(1).get(0);
        }
    };

    /**
     *
     * @param count number of random tokens to generate
     * @return list of randomly generated tokens
     */
    public abstract List<String> generate(int count);

    /**
     *
     * @return random token
     */
    public abstract String generate();
}