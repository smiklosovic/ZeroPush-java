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

import java.io.File;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zeropush.model.ZeroPushNotification;
import com.zeropush.notify.notification.AndroidPushNotification;

/**
 * This test case runs effectively in embedded Weld EE container.
 *
 * Bean instances in test methods are injected there automatically.
 *
 * @author <a href="mailto:miklosovic@gmail.com">Stefan Miklosovic</a>
 *
 */
@RunWith(Arquillian.class)
public class ZeroPushTestCase
{
    @Deployment
    public static WebArchive getDeployment()
    {
        File[] zeropush = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .resolve("com.zeropush:zeropush")
            .withTransitivity()
            .asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
            .addClass(ZeroPushCredentialsVerifier.class)
            .addClass(ZeroPushPropertiesLoader.class)
            .addClass(ZeroPushNotificationSender.class)
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
            .addAsResource("zeropush.properties")
            .addAsLibraries(zeropush);

        System.out.println(war.toString(true));

        return war;
    }

    @Test
    public void verificationOfNonsenseTokenTest(ZeroPushCredentialsVerifier verifier)
    {
        Assert.assertFalse(verifier.verifyToken("nonsense"));
    }

    @Test
    public void verificationOfValidToken(ZeroPushCredentialsVerifier verifier)
    {
        Assert.assertTrue(verifier.verifyToken(System.getProperty("zeropush.token.server")));
    }

    @Test
    public void sendNotificationTest(ZeroPushNotificationSender sender)
    {
        ZeroPushNotification notification = new AndroidPushNotification.Builder()
            .addDeviceToken("123456789")
            .addDatum("alert", "Now Boarding")
            .addDatum("username", "fred.droid")
            .build();

        sender.sendNotification(notification);
    }
}
