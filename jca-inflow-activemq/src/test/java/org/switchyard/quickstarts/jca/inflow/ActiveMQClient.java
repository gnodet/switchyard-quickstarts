/*
 * JBoss, Home of Professional Open Source Copyright 2009, Red Hat Middleware
 * LLC, and individual contributors by the @authors tag. See the copyright.txt
 * in the distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.switchyard.quickstarts.jca.inflow;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.switchyard.component.test.mixins.activemq.ActiveMQMixIn;


/**
 * ActiveMQ client that uses JMS API to connect to a remote server and
 * send a message to a queue.
 *
 * @author Daniel Bevenius
 *
 */
public final class ActiveMQClient {
    
    private static final String QUEUE = "JCAInflowGreetingServiceQueue";

    /**
     * Private no-args constructor.
     */
    private ActiveMQClient() {
    }
    
    /**
     * Only execution point for this application.
     * @param ignored not used.
     * @throws Exception if something goes wrong.
     */
    public static void main(final String[] args) throws Exception {
        ActiveMQMixIn mixIn = new ActiveMQMixIn();
        if (args.length == 0) {
            System.err.println("ERROR: Use -Dexec.args to pass a name and language value, e.g. -Dexec.args=\"Skippy english\"");
            return;
        }

        try {
            final Session session = mixIn.getSession();
            final MessageProducer producer = session.createProducer(session.createQueue(QUEUE));
            final TextMessage message = session.createTextMessage();
            String payload = TEMPLATE.replace("@name@", args[0]);
            if (args.length == 2) {
                payload = payload.replace("@lang@", args[1]);
            } else {
                payload = payload.replace("@lang@", "english");
            }
            System.out.println(payload);
            message.setText(payload);
            producer.send(message);

            System.out.println("Sent message [" + message + "]");
            session.close();
        } finally {
            mixIn.uninitialize();
        }
    }
    
    private static final String TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<qs:person xmlns:qs=\"urn:switchyard-quickstart:jca-inflow-activemq:0.1.0\">\n"
            + "    <name>@name@</name>\n"
            + "    <language>@lang@</language>\n"
            + "</qs:person>\n";
    
}
