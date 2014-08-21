/**
 *
 * Copyright (C) 2002-2012 "SYSNET International, Inc."
 * support@sysnetint.com [http://www.sysnetint.com]
 *
 * This file is part of OpenEMPI.
 *
 * OpenEMPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openempi.webapp.client.ui.widget;

import org.openempi.webapp.client.ClientState;
import org.openempi.webapp.client.ServiceRegistry;

import com.google.gwt.user.client.ui.Composite;

/**
 * base class for all Panes in our app
 * 
 * Borrowed from Beginning Google Web Toolkit book
 */
public abstract class Pane extends Composite {

    private final ServiceRegistry serviceRegistry;
    private final ClientState clientState;

    /**
     * @param serviceRegistry The service registry each pane has access to.
     */
    protected Pane(ClientState clientState, ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        this.clientState = clientState;
    }
    
    /**
     * 
     * @return The service registry associated with this pane.
     */
    protected ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            throw new RuntimeException("serviceRegistry state in uninitialized");
        }
        return serviceRegistry;
    }
    
    public ClientState getClientState() {
        if (clientState == null) {
            throw new RuntimeException("client state in uninitialized");
        }
        return clientState;
    }
}
