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

import org.openempi.webapp.client.Constants;

import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.user.client.Window;

public class Viewport extends com.extjs.gxt.ui.client.widget.Viewport
{
	// This is plain ugly but the Viewport uses as the size of the application the size of
	// the browser window and if you are using part of that window for the header, and
	// loading the application on a div tag then the application size is larger than the
	// browser window without a scroll bar.
	//
	public void onAttach() {
		super.onAttach();
		GXT.hideLoadingPanel(getLoadingPanelId());
		setEnableScroll(getEnableScroll());
		setSize(Window.getClientWidth(), Window.getClientHeight() - Constants.HEADER_OFFSET);
	}

	@Override
	protected void onWindowResize(final int width, final int height) {
		setSize(Window.getClientWidth(), Window.getClientHeight() - Constants.HEADER_OFFSET);
	}
}
