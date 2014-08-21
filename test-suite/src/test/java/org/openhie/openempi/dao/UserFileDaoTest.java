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
package org.openhie.openempi.dao;

import java.util.List;

import org.junit.Test;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.UserFile;

public class UserFileDaoTest extends BaseDaoTestCase
{
	private UserDao userDao;

	@Test
	public void testSaveUserFile() {
		try {
			User user = (User) userDao.loadUserByUsername("admin");
			
			for (int i=0; i < 10; i++) {
				UserFile aFile = new UserFile("test" + i, "/tmp/test.file" + i + ".test");
				aFile.setDateCreated(new java.util.Date());
				aFile.setOwner(user);			
				userDao.saveUserFile(aFile);				
			}
			
			List<UserFile> files = userDao.getUserFiles(user);
			for (UserFile file : files) {
				System.out.println("Found file: " + file);
			}
			
			for (UserFile file : files) {
				System.out.println("Removing file: " + file);
				userDao.removeUserFile(file);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
