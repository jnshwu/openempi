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
package org.openhie.openempi.matching;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractMatchingLifecycleObserver implements MatchingLifecycleObserver
{
	protected final Log log = LogFactory.getLog(getClass());
	
	private boolean ready;
	private boolean down;
	
	// Always initialize the service as being down
	// when the system is starting up but before the
	// startup operation has been invoked.
	//
	public AbstractMatchingLifecycleObserver() {
		this.ready = false;
		this.down = true;
	}
	
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public boolean isReady() {
		return ready;
	}

	public boolean isDown() {
		return down;
	}
}
