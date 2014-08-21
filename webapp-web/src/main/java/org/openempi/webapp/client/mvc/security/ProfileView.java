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
package org.openempi.webapp.client.mvc.security;

import java.util.Map;

import org.openempi.webapp.client.AppEvents;
import org.openempi.webapp.client.Constants;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.ui.util.InputFormData;
import org.openempi.webapp.client.ui.util.InputFormat;
import org.openempi.webapp.resources.client.model.State;
import org.openempi.webapp.resources.client.model.Country;
import org.openempi.webapp.client.mvc.Controller;
import org.openempi.webapp.client.mvc.View;
import org.openhie.openempi.model.Permission;

import com.extjs.gxt.ui.client.Registry;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.mvc.AppEvent;
//import com.extjs.gxt.ui.client.mvc.Controller;
//import com.extjs.gxt.ui.client.mvc.View;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
//import com.extjs.gxt.ui.client.widget.grid.ColumnData;  
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;

import com.google.gwt.core.client.GWT;

public class ProfileView extends View
{
//	private LayoutContainer container;
	private ContentPanel container;
	private LayoutContainer formButtonContainer;	
	private LayoutContainer formContainer;
	private FormPanel leftFormPanel; 
	private FormPanel rightFormPanel; 
	private FormPanel buttonPanel; 
	
	// Username/password
	private TextField<String> userName;
	private TextField<String> password;
	private TextField<String> passwordNew;
	private TextField<String> passwordConfirm;
	private TextField<String> passwordHint;
	
	// User Info	
	private TextField<String> firstName;
	private TextField<String> lastName;
		
	// Address
	private TextField<String> address;
	private TextField<String> city;
	private ComboBox<State> state;
	private TextField<String> zip;
	private ComboBox<Country> country;
		
	// Other
	private TextField<String> phoneNumber;
	private TextField<String> email;
	private TextField<String> emailConfirm;
	private TextField<String> webSite;
	
	private Status status;
	private Button submitButton;
	private Button cancelButton;

	ListStore<State> states = new ListStore<State>();
	ListStore<Country> countries = new ListStore<Country>();

	private UserWeb updateUser = null;
	
	private Map<String,PermissionWeb> permissions = null;
	
	public ProfileView(Controller controller) {
		super(controller);
		states.add(InputFormData.getStates());
		countries.add(InputFormData.getCountry());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleEvent(AppEvent event) {
		
		if (event.getType() == AppEvents.ProfileView) {
			initUI();			
			clearFormFields(false);
			
			updateUser = Registry.get(Constants.LOGIN_USER);	    	
			displayRecords(updateUser);	
			
		} else if (event.getType() == AppEvents.ProfileUpdateComplete) {	
			
			status.hide();
			submitButton.unmask();  
			
			updateUser = (UserWeb) event.getData();		
		    Registry.register(Constants.LOGIN_USER, updateUser);
	
	        MessageBox.alert("Information", "User profile was successfully updated", listenInfoMsg);  	
	        
		} else if (event.getType() == AppEvents.ProfileVarifyPasswordSuccess) {	
			
			UserWeb user = copyUserFromGUI(updateUser);															
			controller.handleEvent(new AppEvent(AppEvents.ProfileUpdateInitiate, user));
	        
		} else if (event.getType() == AppEvents.ProfileVarifyPasswordFailure) {	
	
			status.hide();
			submitButton.unmask();  
			
	        MessageBox.alert("Information", "Incorrect current password", listenInfoMsg);  	
	        
		} else if (event.getType() == AppEvents.Error) {	
			status.hide();
			submitButton.unmask();  
			
			String message = event.getData();
	        MessageBox.alert("Information", "Failure: " + message, listenFailureMsg);  			
		}
	}

    final Listener<MessageBoxEvent> listenInfoMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   
  			  status.hide();
  			  submitButton.unmask();  
          }
        }  
    };  

    final Listener<MessageBoxEvent> listenFailureMsg = new Listener<MessageBoxEvent>() {  
        public void handleEvent(MessageBoxEvent ce) {  
          Button btn = ce.getButtonClicked();   
          if( btn.getText().equals("OK")) {   
  			  status.hide();
  			  submitButton.unmask();  
          }
        }  
    };  
	
	private FormPanel setupUserLeftForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel();
	    formPanel.setTitle(title); 
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.TOP); // default is LEFT
	    formPanel.setLabelWidth(150);

		    // Name fields
		    FieldSet namefieldSet = new FieldSet();  
		    namefieldSet.setHeading("Username");  
		    namefieldSet.setCollapsible(true);  
		    namefieldSet.setBorders(false);  
		    FormLayout namelayout = new FormLayout();  
		    namelayout.setLabelWidth(150);  
//		    namelayout.setDefaultWidth(180); // It is the real function to set the textField width. Default width is 200
		    namefieldSet.setLayout(namelayout); 

				userName = new TextField<String>();
				userName.setFieldLabel("Username");
				userName.setAllowBlank(false);
//				userName.setReadOnly(true);
				userName.disable();
				userName.setTabIndex(tabIndex++);

				namefieldSet.add(userName);

			// Password fields
			FieldSet passwordfieldSet = new FieldSet();  
			passwordfieldSet.setHeading("Password");  
			passwordfieldSet.setCollapsible(true);  
			passwordfieldSet.setBorders(false);  
			FormLayout passwordlayout = new FormLayout();  
			passwordlayout.setLabelWidth(150);  
			passwordfieldSet.setLayout(passwordlayout); 
			
			    password = new TextField<String>();
			    password.setFieldLabel("Current Password");	
			    password.setAllowBlank(false);
			    password.setPassword(true);
			    password.setTabIndex(tabIndex++);
	
				passwordNew = new TextField<String>();
				passwordNew.setFieldLabel("New Password");	
				passwordNew.setPassword(true);
				passwordNew.setTabIndex(tabIndex++);
					    
				passwordConfirm = new TextField<String>();
				passwordConfirm.setFieldLabel("Confirm Password");	
				passwordConfirm.setPassword(true);
				passwordConfirm.setTabIndex(tabIndex++);
				    
			    passwordHint= new TextField<String>();
			    passwordHint.setFieldLabel("Password Hint");	
			    passwordHint.setTabIndex(tabIndex++);
			    
			    passwordfieldSet.add(password);
			    passwordfieldSet.add(passwordNew);
			    passwordfieldSet.add(passwordConfirm);		
			    passwordfieldSet.add(passwordHint);					
			    
			FieldSet userInfofieldSet = new FieldSet();  
			userInfofieldSet.setHeading("User Info");  
			userInfofieldSet.setCollapsible(true);  
			userInfofieldSet.setBorders(false);  
		    FormLayout userInfolayout = new FormLayout();  
		    userInfolayout.setLabelWidth(150);  
		    userInfofieldSet.setLayout(userInfolayout);  
		    
				firstName = new TextField<String>();
				firstName.setFieldLabel("First Name");
				firstName.setAllowBlank(false);
				// firstName.setLabelStyle("font-size:12px; margin-left: 20px");
				firstName.setTabIndex(tabIndex++);
				
				
				lastName = new TextField<String>();
				lastName.setFieldLabel("Last Name");
				lastName.setAllowBlank(false);
				lastName.setTabIndex(tabIndex++);
				
				userInfofieldSet.add(firstName);
				userInfofieldSet.add(lastName);		
				
		formPanel.add(namefieldSet); 
		formPanel.add(passwordfieldSet); 
		formPanel.add(userInfofieldSet); 
		
	    return formPanel;
	}
	
	private FormPanel setupUserRightForm(String title, int tabIndex) {
		
	    FormPanel formPanel = new FormPanel(); 
	    formPanel.setTitle(title);  
	    formPanel.setHeaderVisible(false);
	    formPanel.setBodyBorder(false);
	    formPanel.setWidth(400);
//	    formPanel.setLabelAlign(LabelAlign.RIGHT); // default is LEFT
	    formPanel.setLabelWidth(150);

		// Address fields
		FieldSet addressfieldSet = new FieldSet();  
		addressfieldSet.setHeading("Address");  
		addressfieldSet.setCollapsible(true);  
		addressfieldSet.setBorders(false); 		
		FormLayout addresslayout = new FormLayout();  
		addresslayout.setLabelWidth(150); 
		addressfieldSet.setLayout(addresslayout);
		    		
			address = new TextField<String>();
			address.setFieldLabel("Address");
			address.setTabIndex(tabIndex++);
			
			city = new TextField<String>();
			city.setFieldLabel("City");
			city.setAllowBlank(false);
			city.setTabIndex(tabIndex++);
			
			state = new ComboBox<State>();
			state.setFieldLabel("State");
			state.setEmptyText("Select a state...");  
			state.setDisplayField("name");  
			state.setWidth(150);  
			state.setStore(states);
			state.setTypeAhead(true);  
		    state.setTriggerAction(TriggerAction.ALL);
		    state.setTabIndex(tabIndex++);
			
		    zip = new TextField<String>();
			zip.setFieldLabel("Zip Code");
			zip.setAllowBlank(false);
			zip.setTabIndex(tabIndex++);

			country = new ComboBox<Country>();
			country.setFieldLabel("Country");
			country.setEmptyText("Select a country...");
			country.setDisplayField("name");
			country.setTemplate( InputFormat.getFlagTemplate() );
			country.setWidth(100);
			country.setStore(countries);
			country.setTypeAhead(true);
			country.setTriggerAction(TriggerAction.ALL);
			country.setTabIndex(tabIndex++);
			
			addressfieldSet.add(address);
			addressfieldSet.add(city);
			addressfieldSet.add(state);
			addressfieldSet.add(zip);
			addressfieldSet.add(country);
			
		FieldSet otherfieldSet = new FieldSet();  
		otherfieldSet.setHeading("Other");  
		otherfieldSet.setCollapsible(true);  
		otherfieldSet.setBorders(false);  
		FormLayout otherlayout = new FormLayout();  
		otherlayout.setLabelWidth(150);  
		otherfieldSet.setLayout(otherlayout);  		
				
			phoneNumber = new TextField<String>();
			phoneNumber.setFieldLabel("Phone Number");	
			phoneNumber.setToolTip("xxx-xxxx");
			phoneNumber.setTabIndex(tabIndex++);
			
			email = new TextField<String>();
			email.setFieldLabel("Email");	
			email.setAllowBlank(false);
			email.setRegex(InputFormat.EMAIL_FORMATS);
		    email.getMessages().setRegexText("Invalid email format");
//		    email.setAutoValidate(true);  
		    email.setToolTip("xyz@example.com");	        
		    email.setTabIndex(tabIndex++);

			emailConfirm = new TextField<String>();
			emailConfirm.setFieldLabel("Confirm Email");	
			emailConfirm.setRegex(InputFormat.EMAIL_FORMATS);
			emailConfirm.getMessages().setRegexText("Invalid email format");
//		    email.setAutoValidate(true);  
			emailConfirm.setToolTip("xyz@example.com");	        
			emailConfirm.setTabIndex(tabIndex++);

				
			webSite = new TextField<String>();
			webSite.setFieldLabel("Website");	
			webSite.setTabIndex(tabIndex++);
				
			otherfieldSet.add(phoneNumber);
			otherfieldSet.add(email);
			otherfieldSet.add(emailConfirm);
			otherfieldSet.add(webSite);	
			
		formPanel.add(addressfieldSet); 	
		formPanel.add(otherfieldSet); 
		
	    return formPanel;
	}

	private UserWeb copyUser(UserWeb user) {
		UserWeb updatingUser = new UserWeb();
		
		updatingUser.setId(user.getId());

		// Name
		updatingUser.setUsername( user.getUsername());
		updatingUser.setFirstName(user.getFirstName());
		updatingUser.setLastName(user.getLastName());

		// Password
		updatingUser.setPassword( user.getPassword());
		updatingUser.setConfirmPassword( user.getConfirmPassword());
		updatingUser.setPasswordHint( user.getPasswordHint());
		
		// Address
		updatingUser.setAddress(user.getAddress());	
		updatingUser.setCity(user.getCity());		
		updatingUser.setState(user.getState());		
		updatingUser.setPostalCode(user.getPostalCode());
		updatingUser.setCountry(user.getCountry());

		// Phone
		updatingUser.setPhoneNumber(user.getPhoneNumber());
		
		// Other
		updatingUser.setEmail(user.getEmail());		
		updatingUser.setWebsite(user.getWebsite());			
		
		updatingUser.setVersion(user.getVersion());		
		updatingUser.setAccountExpired(user.getAccountExpired());	
		updatingUser.setAccountLocked(user.getAccountLocked());
		updatingUser.setEnabled(user.getEnabled());
		updatingUser.setCredentialsExpired(user.getCredentialsExpired());				
		
		// user roles
		updatingUser.setRoles(user.getRoles());			
		
		return updatingUser;
	}
	
	private UserWeb copyUserFromGUI(UserWeb updateUser) {
		UserWeb user = copyUser(updateUser);		
		
		// Name
		user.setUsername( userName.getValue());
		user.setFirstName( firstName.getValue());
		user.setLastName(lastName.getValue());
		
		// password
		if(passwordNew.getValue() != null && passwordConfirm.getValue() != null ) {
			user.setPassword(passwordNew.getValue());
			user.setConfirmPassword(passwordConfirm.getValue());
		}
		user.setPasswordHint(passwordHint.getValue());
				
		// Address
		user.setAddress(address.getValue());		
		user.setCity(city.getValue());
		if (state.getValue() != null) {
			user.setState(state.getValue().getAbbr());
		}		
		user.setPostalCode(zip.getValue());		
		Country countryValue = country.getValue();
		if (countryValue != null) {
			user.setCountry(countryValue.getName());
		}
		
		// Phone
		user.setPhoneNumber(phoneNumber.getValue());
		
		// Other
		user.setEmail(email.getValue());	
		user.setWebsite(webSite.getValue());		
		
		return 	user;
	}
	
	private void displayRecords(UserWeb user) {
		
		// Name
		userName.setValue(user.getUsername());
		firstName.setValue(user.getFirstName());		
		lastName.setValue(user.getLastName());
		
		passwordHint.setValue(user.getPasswordHint());

		// Address
		address.setValue(user.getAddress());
		city.setValue(user.getCity());
		for( State st : states.getModels()) {
			if(st.getAbbr().equals(user.getState()))
				state.setValue(st);
		}
		zip.setValue(user.getPostalCode());
		for( Country co : countries.getModels()) {
			if(co.getName().equals(user.getCountry()))
				country.setValue(co);
		}
		
		// Phone
		phoneNumber.setValue(user.getPhoneNumber());	
		
		// Other
		email.setValue(user.getEmail());	
		emailConfirm.setValue(user.getEmail());	
		webSite.setValue(user.getWebsite());	
		
	}
	
	private FormPanel setupButtonPanel(int tabIndex) {
		FormPanel buttonPanel = new FormPanel(); 
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setStyleAttribute("paddingRight", "10px");
		buttonPanel.setButtonAlign(HorizontalAlignment.CENTER);		
		 		
		submitButton = new Button("Save", new SelectionListener<ButtonEvent>() {
		
			@Override
			public void componentSelected(ButtonEvent ce) {
				// Info.display("test:", "save componentSelected");				
				if( !leftFormPanel.isValid() || !rightFormPanel.isValid() ) {					
					Info.display("test:", "Invalid fields");	
					return;
				}
				
				if( passwordNew.getValue() != null || passwordConfirm.getValue() != null) {
					if (passwordNew.getValue() == null || passwordConfirm.getValue() == null ||
						!passwordNew.getValue().equals(passwordConfirm.getValue())) {
				        MessageBox.alert("Information", "Confirm password is not same as new password", null);  
				        return;
					}
				}
				
				if (!email.getValue().equals(emailConfirm.getValue())) {
			        MessageBox.alert("Information", "Confirm email address is not same as email address", null);  	
			        return;
				}
				
	        	// check current password
				UserWeb user = copyUserFromGUI(updateUser);	
				user.setPassword(password.getValue());
				controller.handleEvent(new AppEvent(AppEvents.ProfileVarifyPasswordInitiate, user));
				
//				UserWeb user = copyUserFromGUI(updateUser);															
//				controller.handleEvent(new AppEvent(AppEvents.ProfileUpdateInitiate, user));
				status.show();
				submitButton.mask();
			}		
		});

		
		cancelButton = new Button("Reset", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				clearFormFields(true);
			}
		});
			
		submitButton.setTabIndex(tabIndex++);		
		cancelButton.setTabIndex(tabIndex++);	
		
		status = new Status();
		status.setBusy("please wait...");
		buttonPanel.getButtonBar().add(status);
		status.hide();
		
		buttonPanel.getButtonBar().setSpacing(15);
		buttonPanel.addButton(submitButton);
		buttonPanel.addButton(cancelButton);
		
		return buttonPanel;
	}
	
	private void checkPermissins() {
	    if( permissions != null ) {
		    PermissionWeb permission = permissions.get(Permission.USER_EDIT);
			if (permission == null) {
				
				setReadonlyFields(true);
				submitButton.disable();
				cancelButton.disable();
				
				
			}
	    }
	}
	    
	private void initUI() {
		long time = new java.util.Date().getTime();
		GWT.log("Initializing the UI ", null);
		
		permissions = Registry.get(Constants.LOGIN_USER_PERMISSIONS);   	
		
//		container = new LayoutContainer();
		container = new ContentPanel ();
		container.setLayout(new BorderLayout());
		container.setHeading("User Profile");  
		
		formButtonContainer = new LayoutContainer();
		formButtonContainer.setScrollMode(Scroll.AUTO);
		
			TableLayout identlayout = new TableLayout(2);
			identlayout.setWidth("100%");
			identlayout.setCellSpacing(5);
			identlayout.setCellVerticalAlign(VerticalAlignment.TOP);
		    
			TableLayout formlayout = new TableLayout(2);
			formlayout.setWidth("100%");
			formlayout.setCellSpacing(5);
			formlayout.setCellVerticalAlign(VerticalAlignment.TOP);
			
			formContainer = new LayoutContainer();
//			formContainer.setBorders(true);
			formContainer.setLayout(formlayout);
				leftFormPanel = setupUserLeftForm("", 1);
				rightFormPanel = setupUserRightForm("", 8);
				
			formContainer.add(leftFormPanel);	
			formContainer.add(rightFormPanel);		
			buttonPanel = setupButtonPanel(17);
			
		formButtonContainer.add(formContainer);
		formButtonContainer.add(buttonPanel);
		
	    // check permissions
		checkPermissins();
		
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(4, 2, 4, 2));
		container.add(formButtonContainer, data);		
				
		LayoutContainer wrapper = (LayoutContainer) Registry.get(Constants.CENTER_PANEL);
		wrapper.removeAll();
		wrapper.add(container);
		wrapper.layout();
		GWT.log("Done Initializing the UI in " + (new java.util.Date().getTime()-time), null);
	}
	
	private void clearFormFields(boolean reset) {
		if( !reset ) {
			userName.clear();
		}
		
		firstName.clear();
		lastName.clear();

		password.clear();
		passwordNew.clear();
		passwordConfirm.clear();
		passwordHint.clear();
		
		address.clear();
		city.clear();
		state.clear();
		zip.clear();
		country.clear();

		phoneNumber.clear();
		
		email.clear();
		emailConfirm.clear();
		webSite.clear();		
	}

	private void setReadonlyFields(boolean enable) {
		
		// Name
		userName.setReadOnly(enable);
		firstName.setReadOnly(enable);		
		lastName.setReadOnly(enable);
		
		password.setAllowBlank(enable);
		password.setReadOnly(enable);
		passwordNew.setReadOnly(enable);
		passwordConfirm.setReadOnly(enable);
		passwordHint.setReadOnly(enable);

		// Address
		address.setReadOnly(enable);
		city.setReadOnly(enable);
		zip.setReadOnly(enable);
		state.setEditable(!enable);
	    state.getListView().disableEvents(enable);
		country.setEditable(!enable);
		country.getListView().disableEvents(enable);
		
		// Phone
		phoneNumber.setReadOnly(enable);	
		
		// Other
		email.setReadOnly(enable);
		emailConfirm.setReadOnly(enable);
		webSite.setReadOnly(enable);
	}
}
