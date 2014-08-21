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
package org.openhie.openempi.openpixpdqadapter;


public abstract class AbstractPixPdqTest extends BasePixPdqTest
{
	protected String pdqHostname = "localhost";
	protected String pixHostname = "localhost";
	protected int pdqPort = 3700;
	protected int pixPort = 3600;
	
	protected String getHostname(Profile profile) {
		if (profile.equals(Profile.PDQ)) {
			return pdqHostname;
		}
		if (profile.equals(Profile.PIX)) {
			return pixHostname;
		}
		throw new RuntimeException("Profile " + profile + " is not supported.");
	}


	@Override
	protected boolean supportsPdqProfile() {
		return true;
	}

	@Override
	protected boolean supportsPixProfile() {
		return true;
	}

	@Override
	protected int getPort(Profile profile) {
		if (profile.equals(Profile.PDQ)) {
			return pdqPort;
		}
		if (profile.equals(Profile.PIX)) {
			return pixPort;
		}
		throw new RuntimeException("Profile " + profile + " is not supported.");		
	}
}
