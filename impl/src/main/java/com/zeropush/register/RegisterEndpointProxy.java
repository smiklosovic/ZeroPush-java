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
package com.zeropush.register;

/**
 * Registers a device and/or updates the devices active status.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class RegisterEndpointProxy
{
    /**
     * Register a device of some token.
     *
     * @param deviceToken token to register.
     * @return {@link Register}
     */
    public Register register(String deviceToken)
    {
        return new Register(deviceToken);
    }

    /**
     * Register a device of some token to some channel.
     *
     * @param deviceToken token to register
     * @param channel channel to register a device to
     * @return {@link Register}
     */
    public Register register(String deviceToken, String channel)
    {
        return new Register(deviceToken, channel);
    }

    /**
     * Unregisters a device of some device token.
     *
     * @param deviceToken device token to unregister.
     * @return {@link UnRegister}
     */
    public UnRegister unregister(String deviceToken)
    {
        return new UnRegister(deviceToken);
    }
}
