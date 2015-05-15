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

import com.zeropush.model.notification.AndroidPushNotification;
import com.zeropush.model.notification.IOSPushNotification;
import com.zeropush.model.notification.SafariPushNotification;
import com.zeropush.model.notification.ZeroPushNotification;
import com.zeropush.badge.BadgeEndpointProxy;
import com.zeropush.channel.ChannelsEndpointProxy;
import com.zeropush.configuration.ZeroPushConfiguration;
import com.zeropush.device.DevicesEndpointProxy;
import com.zeropush.inactivity.InactivityEndpointProxy;
import com.zeropush.notify.NotificationEndpointProxy;
import com.zeropush.register.RegisterEndpointProxy;
import com.zeropush.subscribe.SubscriptionEndpointProxy;
import com.zeropush.verify.VerifyEndpointProxy;

/**
 * Single entry point to ZeroPush. ZeroPush is thread safe.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
public final class ZeroPush
{
    private static ThreadLocal<ZeroPushConfiguration> pushConfiguration = new ThreadLocal<ZeroPushConfiguration>() {

        @Override
        protected ZeroPushConfiguration initialValue()
        {
            return new ZeroPushConfiguration();
        }
    };

    /**
     * Gets and deletes a channel.
     *
     * @return proxy dealing with channel management
     */
    public static ChannelsEndpointProxy channels()
    {
        return new ChannelsEndpointProxy();
    }

    /**
     * Gets a single device or all devices.
     *
     * @return proxy dealing with devices management
     */
    public static DevicesEndpointProxy devices()
    {
        return new DevicesEndpointProxy();
    }

    /**
     * Verifies credentials.
     *
     * @return proxy dealing with verification of credentials
     */
    public static VerifyEndpointProxy verification()
    {
        return new VerifyEndpointProxy();
    }

    /**
     * Registers devices.
     *
     * @return proxy dealing with device registration
     */
    public static RegisterEndpointProxy registration()
    {
        return new RegisterEndpointProxy();
    }

    /**
     * Subscribes a device to some channel.
     *
     * @return proxy dealing with subscription
     */
    public static SubscriptionEndpointProxy subscription()
    {
        return new SubscriptionEndpointProxy();
    }

    /**
     * Sets a device's badge number to a given value.
     *
     * @return proxy dealing with badges
     */
    public static BadgeEndpointProxy badge()
    {
        return new BadgeEndpointProxy();
    }

    /**
     * Gets inactivities.
     *
     * @return inactivities proxy
     */
    public static InactivityEndpointProxy inactivity()
    {
        return new InactivityEndpointProxy();
    }

    /**
     * Sends notifications.
     *
     * @param pushNotification push notification to sent
     * @return {@link NotificationEndpointProxy}
     * @see AndroidPushNotification
     * @see SafariPushNotification
     * @see IOSPushNotification
     */
    public static NotificationEndpointProxy notification(ZeroPushNotification pushNotification)
    {
        return new NotificationEndpointProxy().notification(pushNotification);
    }

    /**
     * Flag for sending a notification by {@link #notification(ZeroPushNotification)} while broadcasting it.
     *
     * @return {@link NotificationEndpointProxy}
     */
    public static NotificationEndpointProxy broadcast()
    {
        return new NotificationEndpointProxy().broadcast();
    }

    /**
     * Flag for sending a notification by {@link #notification(ZeroPushNotification)} while broadcasting it to some channel.
     *
     * @param channel channel where broadcasting will be used
     * @return {@link NotificationEndpointProxy}
     */
    public static NotificationEndpointProxy broadcast(String channel)
    {
        return new NotificationEndpointProxy().broadcast(channel);
    }

    /**
     * Gets ZeroPush configuration. You get unique ZeroPush configuration object per thread, ZeroPush is thread safe.
     *
     * @return configuration
     */
    public static ZeroPushConfiguration getConfiguration()
    {
        return pushConfiguration.get();
    }

    /**
     *
     * @param configuration configuration to set for this ZeroPush instance
     * @throws IllegalArgumentException iff {@code configuration} is a null object
     */
    public static void setConfiguration(ZeroPushConfiguration configuration)
    {
        if (configuration == null)
        {
            throw new IllegalArgumentException("Configuration must be specified.");
        }

        pushConfiguration.set(configuration);
    }
}
