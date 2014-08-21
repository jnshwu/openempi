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

import org.openhie.openempi.InitializationException;

public interface MatchingLifecycleObserver
{
	/**
	 * The method is invoked when the system is initializing itself at startup. The method
	 * is useful if the matching algorithm uses a cache to speed up processing. It can make
	 * use of this method to warm-up in cache so that the system will operate faster once
	 * it has completed initialization.
	 * 
	 * If a problem comes up during initialization, the code should throw the
	 * InitializationException to notify the system that it should not go on with starting up
	 * normally because an error has occurred that will prevent the algorithm from working properly.
	 * If the error is not serious and the algorithm can operate, it should not throw this
	 * exception.
	 * 
	 */
	public void startup() throws InitializationException;
	
	/**
	 * The system uses the isReady method to confirm that the matching algorithm has finished its
	 * initialization and it can go on with processing requests. This polling mechanism is used
	 * because some matching algorithms may require considerable time to complete their
	 * initialization.
	 * 
	 */
	public boolean isReady();
	
	/**
	 * The shutdown method is invoked against the matching algorithm when the system starts to
	 * shutdown gracefully. It gives an opportunity to the matching algorithm to prepare its
	 * state for shutting down. If the matching algorithm caches write requests in memory then
	 * it needs this to synchronize its on-disk data structures with changes that have taken
	 * place in memory.
	 */
	public void shutdown();
	
	/**
	 * The isDown method is the mirror image of the isReady method and it is the polling
	 * mechanism used by the system to confirm that the system has shutdown before "pulling
	 * the plug". After a certain amount of time, it will force the shutdown.
	 */
	public boolean isDown();
}
