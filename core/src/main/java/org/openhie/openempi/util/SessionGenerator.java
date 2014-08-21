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
package org.openhie.openempi.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Generates unique, secure session IDs. This class is based on the algorithm used to generate session IDs in the Apache Tomcat server
 * with small modifications.
 * 
 * @author <a href="mailto:odysseas@sysnetint.com">Odysseas Pentakalos</a>
 */
public class SessionGenerator
{
	protected static final Log log = LogFactory.getLog(SessionGenerator.class);

	protected static final String DEFAULT_ALGORITHM = "MD5";
	protected static int sessionIdLength = 16;
	protected static int duplicates = 0;
	protected static MessageDigest digest;
	protected static Random random;
	protected static java.util.Map<String, String> sessions = new ConcurrentHashMap<String, String>();

	public static synchronized String generateSessionId() {

		byte random[] = new byte[16];
		String result = null;

		StringBuffer buffer = new StringBuffer();
		do {
			int resultLenBytes = 0;
			if (result != null) {
				buffer = new StringBuffer();
				duplicates++;
			}

			while (resultLenBytes < SessionGenerator.sessionIdLength) {
				getRandomBytes(random);
				random = getDigest().digest(random);
				for (int j = 0; j < random.length
						&& resultLenBytes < SessionGenerator.sessionIdLength; j++) {
					byte b1 = (byte) ((random[j] & 0xf0) >> 4);
					byte b2 = (byte) (random[j] & 0x0f);
					if (b1 < 10)
						buffer.append((char) ('0' + b1));
					else
						buffer.append((char) ('A' + (b1 - 10)));
					if (b2 < 10)
						buffer.append((char) ('0' + b2));
					else
						buffer.append((char) ('A' + (b2 - 10)));
					resultLenBytes++;
				}
			}

			result = buffer.toString();
		} while (sessions.containsKey(result));
		return (result);
	}

	protected static synchronized MessageDigest getDigest() {

		if (SessionGenerator.digest == null) {
			try {
				digest = MessageDigest.getInstance(DEFAULT_ALGORITHM);
			} catch (NoSuchAlgorithmException e) {
				log.error("Unable to obtain a message digest algorithm: " + e,
						e);
				throw new RuntimeException(
						"Unable to obtain a message digest algorithm: "
								+ e.getMessage());
			}
		}
		return digest;
	}

	protected static void getRandomBytes(byte bytes[]) {
		getRandom().nextBytes(bytes);
	}

	public static Random getRandom() {
		if (random == null) {
			// Calculate the new random number generator seed
			long seed = System.currentTimeMillis();

			random = new java.util.Random();
			random.setSeed(seed);
		}
		return (SessionGenerator.random);
	}
}
