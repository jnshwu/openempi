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
package org.openempi.webapp.client.mvc;

import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
//import org.openempi.webapp.client.ui.widget.Viewport;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.widget.LoginDialog;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Dispatcher;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class AppView extends View
{
  private Viewport viewport;
  private ContentPanel west;
  private LayoutContainer center;
  private LayoutContainer north;
  private LoginDialog dialog;
 
  public AppView(Controller controller) {
    super(controller);
  }

  protected void initialize() {
      
	initUI();
	  
    dialog = new LoginDialog(controller);
    dialog.setClosable(false);
    dialog.addListener(Events.Hide, new Listener<WindowEvent>() {
      public void handleEvent(WindowEvent be) {
        Dispatcher.forwardEvent(AppEvents.Init);
      }
    });
    dialog.show();
  }

  private void initUI() {
    viewport = new Viewport();
    viewport.setLayout(new BorderLayout());

    createNorth();
//    createWest();
    createCenter();

    // registry serves as a global context
    Registry.register(Constants.VIEWPORT, viewport);
    Registry.register(Constants.NORTH_PANEL, north);
    Registry.register(Constants.CENTER_PANEL, center);

    RootPanel.get().add(viewport);
  }

  private void createNorth() {
	  north = new LayoutContainer();
	  Html header = new Html("<div id='header'>" +
	  		"   <img src='images/openempi.jpg'/> " +
	  		"</div>");
	  
	  BorderLayoutData data = new BorderLayoutData(LayoutRegion.NORTH, 120);
	  data.setMargins(new Margins(0, 0, 0, 0));
	  header.setBorders(true);
	  header.setWidth("100%");
	  header.setHeight(70);
	  north.add(header);
	  viewport.add(north, data);
  }

  private void createWest() {
    BorderLayoutData data = new BorderLayoutData(LayoutRegion.WEST, 150, 150, 200);
    data.setMargins(new Margins(2, 0, 2, 2));

    west = new ContentPanel();
    west.setBodyBorder(true);
    west.setLayout(new AccordionLayout());
    west.setLayoutOnChange(true);
    west.setHeading("Navidation Panel");

    viewport.add(west, data);
  }

  private void createCenter() {
    center = new LayoutContainer();
    center.setLayout(new FitLayout());

    BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
    data.setMargins(new Margins(5, 5, 5, 5));

    viewport.add(center, data);
  }

  protected void handleEvent(AppEvent event) {
    if (event.getType() == AppEvents.Init) {
    	
        initUI();
        
    } else if (event.getType() == AppEvents.Logout) {
        	
    	showDefaultCursor();
    	getController().cleanRegistry();
    	
    	initUI();
        dialog.relogin();
    } else if (event.getType() == AppEvents.UserAuthenticateSuccess) {
    	
    	//UserWeb user = event.getData();
	    //Registry.register(Constants.LOGIN_USER, user);
    	Map<String,PermissionWeb> permissions = event.getData();
	    Registry.register(Constants.LOGIN_USER_PERMISSIONS, permissions);   
	    
//	    for (Object key: permissions.keySet()) {
//	    	Info.display("permissions:", "Key : " + key.toString() + " Value : " + permissions.get(key).getDescription());
//	    }
    	dialog.authenticateSuccess();
    } else if (event.getType() == AppEvents.UserAuthenticateFailure) {
    	String msg = event.getData();
    	
	    Registry.register(Constants.LOGIN_USER, null);
    	dialog.authenticateFailure(msg);
    }
  }

  public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
  }
}
