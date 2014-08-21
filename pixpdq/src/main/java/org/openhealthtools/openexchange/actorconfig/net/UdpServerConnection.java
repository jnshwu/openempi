/**
 *  Copyright (c) 2009-2011 Misys Open Source Solutions (MOSS) and others
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 *  Contributors:
 *    Misys Open Source Solutions - initial API and implementation
 *    -
 */

package org.openhealthtools.openexchange.actorconfig.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:wenzhi.li@misys.com">Wenzhi Li</a>
 */
public class UdpServerConnection implements IUdpServerConnection {

    private static final Log log = LogFactory.getLog(UdpServerConnection.class);
    private IConnectionDescription description;
    private DatagramSocket socket;
    private final int DEFAULT_MAX_TRANSMISSION_UNIT = 32786;

    public UdpServerConnection(IConnectionDescription description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IUdpServerConnection#isServerConnectionValid()
     */
    public boolean isServerConnectionValid() {
        return socket != null && socket.isBound();
    }


    /* (non-Javadoc)
    * @see org.openhealthtools.openatna.net.IUdpServerConnection#getServerSocket()
    */
    public DatagramSocket getServerSocket() {
        DatagramSocket returnVal = null;
        if (isServerConnectionValid()) {
            returnVal = socket;
        }

        return returnVal;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IUdpServerConnection#closeServerConnection()
     */
    public void closeServerConnection() {
        if (isServerConnectionValid()) {
            socket.close();
        }
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IUdpServerConnection#getMaxTransmissionUnit()
     */
    public int getMaxTransmissionUnit() {
        int ret = DEFAULT_MAX_TRANSMISSION_UNIT;

        PropertySet udpProps = description.getPropertySet("UdpProperties");
        if (udpProps == null) {
            return ret;
        }
        String mtu = udpProps.getValue("mtu");
        if (mtu != null) {
            try {
                ret = Integer.parseInt(mtu);
            } catch (NumberFormatException e) {
                log.warn("Cannot parse mtu in connection " + description.getName(), e);
            }
        }

        return ret;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IUdpServerConnection#getConnectionDescription()
     */
    public IConnectionDescription getConnectionDescription() {
        return description;
    }

    /* (non-Javadoc)
     * @see org.openhealthtools.openatna.net.IUdpServerConnection#connect()
     */
    public void connect() {
        int port = description.getPort();
        String addr = description.getHostname();
        try {
            socket = new DatagramSocket(port, InetAddress.getByName(addr));
        } catch (SocketException e) {
            log.error("UDP socket Connection Error", e);
        } catch (UnknownHostException e) {
            log.error("Unknown host for UDP socket", e);
        }

    }
}
