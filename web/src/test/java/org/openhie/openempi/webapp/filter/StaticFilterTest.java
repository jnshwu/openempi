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
package org.openhie.openempi.webapp.filter;

import junit.framework.TestCase;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class StaticFilterTest extends TestCase {
    private StaticFilter filter = null;

    protected void setUp() throws Exception {
        filter = new StaticFilter();
        MockFilterConfig config = new MockFilterConfig();
        config.addInitParameter("includes", "/scripts/*");
        filter.init(config);
    }

    public void testFilterDoesntForwardWhenPathMatches() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/scripts/dojo/test.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNull(chain.getForwardURL());
    }

    public void testFilterForwardsWhenPathDoesntMatch() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/editProfile.html");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertNotNull(chain.getForwardURL());
    }
}

