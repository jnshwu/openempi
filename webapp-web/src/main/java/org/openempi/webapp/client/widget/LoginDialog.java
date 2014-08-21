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
package org.openempi.webapp.client.widget;


import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.model.UserWeb;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
import com.extjs.gxt.ui.client.mvc.Controller;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.google.gwt.user.client.Timer;

public class LoginDialog extends Dialog {

	protected TextField<String> userName;
	protected TextField<String> password;
	protected LabelField loginInfoLabel;
	protected Button reset;
	protected Button login;
	protected Status status;
	private Controller controller;

	public LoginDialog(Controller controller) {
		this.controller = controller;
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
		layout.setDefaultWidth(155);
		setLayout(layout);

		setButtonAlign(HorizontalAlignment.LEFT);
		setButtons("");
		setIcon(IconHelper.createStyle("user"));
		setHeading("OpenEMPI Manager Login");
		setModal(true);
		setBodyBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(300);
		setResizable(false);

		KeyListener keyListener = new KeyListener() {
			public void componentKeyUp(ComponentEvent event) {
				validate();
			}

		};

		userName = new TextField<String>();
		// userName.setMinLength(4);
		userName.setFieldLabel("Username");
		userName.addKeyListener(keyListener);
//		userName.setValue("admin");
		add(userName);

		password = new TextField<String>();
		// password.setMinLength(4);
		password.setPassword(true);
		password.setFieldLabel("Password");
		password.addKeyListener(keyListener);
//		password.setValue("admin");
		add(password);

		loginInfoLabel = new LabelField("");	
		loginInfoLabel.addStyleName("invalid-login");
		add(loginInfoLabel);	
		
		// setFocusWidget(userName);
		validate();
		setFocusWidget(login);

	}

	
	public void reset() {
		userName.reset();
		password.reset();
		loginInfoLabel.setText("");
		validate();
				
		userName.focus();
	}
	
	@Override
	protected void createButtons() {
		super.createButtons();
		status = new Status();
		status.setBusy("please wait...");
		status.hide();
		status.setAutoWidth(true);
		getButtonBar().add(status);

		getButtonBar().add(new FillToolItem());

		reset = new Button("Reset");
		reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				
				reset();
			}

		});

		login = new Button("Login");
		// login.disable();
		login.addSelectionListener(new SelectionListener<ButtonEvent>() {
			public void componentSelected(ButtonEvent ce) {
				
//				onSubmit();
				status.show();
				getButtonBar().disable();
				
				UserWeb user = new UserWeb();
				user.setUsername(userName.getValue());
				user.setPassword(password.getValue());
				controller.handleEvent(new AppEvent(AppEvents.UserAuthenticate, user));	
			}
		});

		addButton(reset);
		addButton(login);

	}

	public void authenticateSuccess() {        	  

		loginInfoLabel.setText("");
		
		onSubmit();
	}

	public void authenticateFailure(String msg) {

		getButtonBar().enable();		
		status.hide();
		
//		loginInfoLabel.setText("Invalid Login");
		loginInfoLabel.setText(msg);
	}
	
	public void relogin() {

		reset();		
		show();
	}
	
	protected void onSubmit() {
//		status.show();
//		getButtonBar().disable();
		Timer t = new Timer() {

			@Override
			public void run() {
				
				getButtonBar().enable();		
				status.hide();
				
				LoginDialog.this.hide();				
			}
		};
		t.schedule(1000);
	}

	protected boolean hasValue(TextField<String> field) {
		return field.getValue() != null && field.getValue().length() > 0;
	}

	protected void validate() {
		login.setEnabled(hasValue(userName) && hasValue(password)
				&& password.getValue().length() > 3);
	}

}
