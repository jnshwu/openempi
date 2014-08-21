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
package org.openhie.openempi.service.impl;

import org.openhie.openempi.dao.UniversalDao;
import org.openhie.openempi.model.User;
import org.jmock.Expectations;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.AssertThrows;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class tests the generic UniversalManager and UniversalManagerImpl implementation.
 */
public class UniversalManagerTest extends BaseManagerMockTestCase {
    protected UniversalManagerImpl manager = new UniversalManagerImpl();
    protected UniversalDao dao;

    @Before
    public void setUp() throws Exception {
        dao = context.mock(UniversalDao.class);
        manager.setDao(dao);
    }

    @After
    public void tearDown() throws Exception {
        manager = null;
        dao = null;
    }

    /**
     * Simple test to verify BaseDao works.
     */
    @Test
    public void testCreate() {
        final User user = createUser();
        context.checking(new Expectations() {{
            one(dao).save(with(same(user)));
            will(returnValue(user));
        }});

        manager.save(user);
    }

    @Test
    public void testRetrieve() {
        final User user = createUser();
        context.checking(new Expectations() {{
            one(dao).get(User.class, "foo");
            will(returnValue(user));
        }});

        User user2 = (User) manager.get(User.class, user.getUsername());
        assertTrue(user2.getUsername().equals("foo"));
    }

    @Test
    public void testUpdate() {
        context.checking(new Expectations() {{
            one(dao).save(createUser());
        }});

        User user = createUser();
        user.getAddress().setCountry("USA");
        manager.save(user);
    }

    @Test
    public void testDelete() {
        final Exception ex = new ObjectRetrievalFailureException(User.class, "foo");

        context.checking(new Expectations() {{
            one(dao).remove(User.class, "foo");
            one(dao).get(User.class, "foo");
            will(throwException(ex));
        }});

        manager.remove(User.class, "foo");
        new AssertThrows(ObjectRetrievalFailureException.class) {
            public void test() {
                manager.get(User.class, "foo");
            }
        }.runTest();
    }
    
    private User createUser() {
        User user = new User();
        // set required fields
        user.setUsername("foo");
        return user;
    }
}
