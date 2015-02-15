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
package com.zeropush.device;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class DevicesEndpointProxy
{
    /**
     *
     * @return all devices
     */
    public GetAllDevices get()
    {
        return new GetAllDevices();
    }

    /**
     *
     * @param deviceToken device token to get a device of
     * @return device of specified {@code deviceToken}
     */
    public GetDevice get(String deviceToken)
    {
        return new GetDevice(deviceToken);
    }

    /**
     * Replaces channels for some device of {@code deviceToken}
     *
     * @param deviceToken device token of a device to replace all channels of
     * @param channels channels to set for a device of specified {@code deviceToken}
     * @return {@link PutDevice}
     */
    public PutDevice replaceChannels(String deviceToken, String... channels)
    {
        return replaceChannels(deviceToken, Arrays.asList(channels));
    }

    /**
     * Replaces channels for some device of {@code deviceToken}
     *
     * @param deviceToken device token of a device to replace all channels of
     * @param channels channels to set for a device of specified {@code deviceToken}
     * @return {@code PutDevice}
     */
    public PutDevice replaceChannels(String deviceToken, List<String> channels)
    {
        return new PutDevice(deviceToken, channels);
    }

    /**
     * Add channels for some device of {@code deviceToken} to already existing channels.
     *
     * @param deviceToken device token of a device to replace all channels of
     * @param channels channels to append for a device of specified {@code deviceToken}
     * @return {@code PatchDevice}
     */
    public PatchDevice appendChannels(String deviceToken, String... channels)
    {
        return appendChannels(deviceToken, Arrays.asList(channels));
    }

    /**
     * Add channels for some device of {@code deviceToken} to already existing channels.
     *
     * @param deviceToken device token of a device to replace all channels of
     * @param channels channels to append for a device of specified {@code deviceToken}
     * @return {@link PatchDevice}
     */
    public PatchDevice appendChannels(String deviceToken, List<String> channels)
    {
        return new PatchDevice(deviceToken, channels);
    }
}
