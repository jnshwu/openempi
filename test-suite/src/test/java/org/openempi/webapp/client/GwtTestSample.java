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
package org.openempi.webapp.client;

import java.util.List;

import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;

import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GwtTestSample extends GWTTestCase {

	public String getModuleName() {
		return "org.openempi.webapp.Application";
	}
//
//	public void testSomething() {
//		PersonDataServiceAsync personDataService = (PersonDataServiceAsync) GWT
//				.create(PersonDataService.class);
//		ServiceDefTarget endpoint = (ServiceDefTarget) personDataService;
//		String moduleRelativeURL = Constants.PERSON_DATA_SERVICE;
//		endpoint.setServiceEntryPoint(moduleRelativeURL);
//		
//		PersonIdentifier pi = new PersonIdentifier();
//		pi.setIdentifier("c%");
//		personDataService.getPersonsByIdentifier(pi,
//				new AsyncCallback<List<Person>>() {
//					public void onFailure(Throwable e) {
//						assertTrue("Got an error: " + e, false);
//						finishTest();
//					}
//
//					public void onSuccess(List<Person> result) {
//						XTemplate tpl = XTemplate.create(getTemplate());
//						for (Person person : result) {
//							System.out.println(tpl.applyTemplate(Util.getJsObject(person, 4)));
//						}
//						finishTest();
//					}
//				});
//		// Set a delay period significantly longer than the
//		// event is expected to take.
//		delayTestFinish(500);
//	}
//	
//
//	private final String getTemplate() {
////		return "<p><b>Company:</b> {givenName}</p><br><p><b>Summary:</b> {familyName}</p>";
//		return "<p>Name: {givenName}</p>, <p>Company: {familyName}</p>, <p>Location: {postalCode}</p>, <p>Kids:</p>, " +
//				"<tpl for=\"personIdentifiers\"> " +
//				"<p>{#}. {identifier} {this.identifierDomain}, </p> " +
//				"{ isWorking: function() { return('yes');} }</tpl>";
//	}
}