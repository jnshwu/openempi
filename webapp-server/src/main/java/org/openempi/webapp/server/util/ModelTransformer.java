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
package org.openempi.webapp.server.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.openempi.webapp.client.model.AuditEventTypeWeb;
import org.openempi.webapp.client.model.AuditEventWeb;
import org.openempi.webapp.client.model.LinkSourceWeb;
import org.openempi.webapp.client.model.MessageLogEntryWeb;
import org.openempi.webapp.client.model.MessageTypeWeb;
import org.openempi.webapp.client.model.PersonIdentifierWeb;
import org.openempi.webapp.client.model.PersonWeb;
import org.openempi.webapp.client.model.ReportParameterWeb;
import org.openempi.webapp.client.model.ReportQueryParameterWeb;
import org.openempi.webapp.client.model.ReportQueryWeb;
import org.openempi.webapp.client.model.ReportRequestEntryWeb;
import org.openempi.webapp.client.model.ReportRequestParameterWeb;
import org.openempi.webapp.client.model.ReportRequestWeb;
import org.openempi.webapp.client.model.ReportWeb;
import org.openempi.webapp.client.model.ReviewRecordPairWeb;
import org.openempi.webapp.client.model.UserWeb;
import org.openempi.webapp.client.model.RoleWeb;
import org.openempi.webapp.client.model.PermissionWeb;
import org.openempi.webapp.client.model.ParameterTypeWeb;
import org.openhie.openempi.model.AuditEvent;
import org.openhie.openempi.model.AuditEventType;
import org.openhie.openempi.model.MessageLogEntry;
import org.openhie.openempi.model.MessageType;
import org.openhie.openempi.model.Gender;
import org.openhie.openempi.model.Person;
import org.openhie.openempi.model.ReviewRecordPair;
import org.openhie.openempi.model.User;
import org.openhie.openempi.model.Role;
import org.openhie.openempi.model.Permission;
import org.openhie.openempi.model.Report;
import org.openhie.openempi.model.ReportParameter;
import org.openhie.openempi.model.ReportQuery;
import org.openhie.openempi.model.ReportQueryParameter;
import org.openhie.openempi.model.ReportRequest;
import org.openhie.openempi.model.ReportRequestEntry;
import org.openhie.openempi.model.ReportRequestParameter;
import org.openhie.openempi.model.FormEntryDisplayType;
import org.openhie.openempi.model.ParameterType;

public class ModelTransformer
{
	public static Logger log = Logger.getLogger(ModelTransformer.class);
	
	public static <T> T map(Object sourceObject, Class<T> destClass) {
		Mapper mapper = new DozerBeanMapper();
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + sourceObject.getClass() + " to type " + destClass);
		}
		return mapper.map(sourceObject, destClass);
	}
	
	public static <T> Set<T> mapSet(Set<?> sourceObjects, Class<T> destClass) {
		if (sourceObjects == null || sourceObjects.size() == 0) {
			return new HashSet<T>();
		}
		if (log.isDebugEnabled()) {
			log.debug("Transforming collection of objects of type " + sourceObjects.iterator().next().getClass() + " to type " + destClass);
		}
		Mapper mapper = new DozerBeanMapper();
		Set<T> collection = new HashSet<T>(sourceObjects.size());
		for (Object o : sourceObjects) {
			T mo = mapper.map(o, destClass);
			collection.add(mo);
		}
		return collection;
	}
	
	public static <T> List<T> mapList(List<?> sourceObjects, Class<T> destClass) {
		if (sourceObjects == null || sourceObjects.size() == 0) {
			return new ArrayList<T>();
		}
		if (log.isDebugEnabled()) {
			log.debug("Transforming collection of objects of type " + sourceObjects.get(0).getClass() + " to type " + destClass);
		}
		Mapper mapper = new DozerBeanMapper();
		List<T> collection = new ArrayList<T>(sourceObjects.size());
		for (Object o : sourceObjects) {
			T mo = mapper.map(o, destClass);
			collection.add(mo);
		}
		return collection;
	}

	// Person mapping
	public static Person mapToPerson(PersonWeb personWeb, Class<Person> personClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + personWeb.getClass() + " to type " + personClass);
		}
		Person thePerson = new Person();
		thePerson.setPersonId(personWeb.getPersonId());
		thePerson.setGivenName(personWeb.getGivenName());
		thePerson.setMiddleName(personWeb.getMiddleName());
		thePerson.setFamilyName(personWeb.getFamilyName());
		thePerson.setPrefix(personWeb.getPrefix());
		thePerson.setSuffix(personWeb.getSuffix());
		thePerson.setDateOfBirth(StringToDate(personWeb.getDateOfBirth()));
		thePerson.setBirthPlace(personWeb.getBirthPlace());
		thePerson.setDeathInd(personWeb.getDeathInd());
		thePerson.setDeathTime(StringToDateTime(personWeb.getDeathTime()));
		thePerson.setMultipleBirthInd(personWeb.getMultipleBirthInd());
		thePerson.setBirthOrder(personWeb.getBirthOrder());
		thePerson.setMothersMaidenName(personWeb.getMothersMaidenName());
		thePerson.setMotherName(personWeb.getMotherName());
		thePerson.setFatherName(personWeb.getFatherName());
		thePerson.setSsn(personWeb.getSsn());
		thePerson.setMaritalStatusCode(personWeb.getMaritalStatusCode());
		thePerson.setDegree(personWeb.getDegree());
		thePerson.setEmail(personWeb.getEmail());
		thePerson.setCountry(personWeb.getCountry());
		thePerson.setCountryCode(personWeb.getCountryCode());
		thePerson.setPhoneCountryCode(personWeb.getPhoneCountryCode());
		thePerson.setPhoneAreaCode(personWeb.getPhoneAreaCode());
		thePerson.setPhoneExt(personWeb.getPhoneExt());
		thePerson.setPhoneNumber(personWeb.getPhoneNumber());
		String genderCode = personWeb.getGender();
		if (genderCode != null) {
			Gender gender = new Gender();
			gender.setGenderCode(genderCode);
			thePerson.setGender(gender);
		}
		thePerson.setAddress1(personWeb.getAddress1());
		thePerson.setAddress2(personWeb.getAddress2());
		thePerson.setCity(personWeb.getCity());
		thePerson.setState(personWeb.getState());
		thePerson.setPostalCode(personWeb.getPostalCode());
		thePerson.setVillage(personWeb.getVillage());
		thePerson.setVillageId(personWeb.getVillageId());
		thePerson.setSector(personWeb.getSector());
		thePerson.setSectorId(personWeb.getSectorId());
		thePerson.setCell(personWeb.getCell());
		thePerson.setCellId(personWeb.getCellId());
		thePerson.setDistrict(personWeb.getDistrict());
		thePerson.setDistrictId(personWeb.getDistrictId());
		thePerson.setProvince(personWeb.getProvince());
		thePerson.setDateChanged(StringToDateTime(personWeb.getDateChanged()));
		thePerson.setDateCreated(StringToDateTime(personWeb.getDateCreated()));

		if (personWeb.getPersonIdentifiers() != null && personWeb.getPersonIdentifiers().size() > 0) {
			for (PersonIdentifierWeb personIdentifier : personWeb.getPersonIdentifiers()) {
				org.openhie.openempi.model.PersonIdentifier personId = ModelTransformer.map(personIdentifier, org.openhie.openempi.model.PersonIdentifier.class);
				thePerson.addPersonIdentifier(personId);
			}
		}
		return thePerson;
	}

	public static PersonWeb mapToPerson(Person person, Class<PersonWeb> personClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + person.getClass() + " to type " + personClass);
		}
		PersonWeb thePerson = new PersonWeb();
		thePerson.setPersonId(person.getPersonId());
		thePerson.setGivenName(person.getGivenName());
		thePerson.setFamilyName(person.getFamilyName());
		thePerson.setMiddleName(person.getMiddleName());
		thePerson.setDateOfBirth(DateToString(person.getDateOfBirth()));
		thePerson.setDeathInd(person.getDeathInd());
		thePerson.setDeathTime(DateTimeToString(person.getDeathTime()));
		Gender gender = person.getGender();
		if (gender != null) {
			thePerson.setGender(gender.getGenderName());
		}
		thePerson.setAddress1(person.getAddress1());
		thePerson.setAddress2(person.getAddress2());
		thePerson.setCity(person.getCity());
		thePerson.setState(person.getState());
		thePerson.setPostalCode(person.getPostalCode());
		thePerson.setVillage(person.getVillage());
		thePerson.setVillageId(person.getVillageId());
		thePerson.setSector(person.getSector());
		thePerson.setSectorId(person.getSectorId());
		thePerson.setCell(person.getCell());
		thePerson.setCellId(person.getCellId());
		thePerson.setDistrict(person.getDistrict());
		thePerson.setDistrictId(person.getDistrictId());
		thePerson.setProvince(person.getProvince());
		thePerson.setSuffix(person.getSuffix());
		thePerson.setPrefix(person.getPrefix());
		thePerson.setEmail(person.getEmail());
		thePerson.setBirthPlace(person.getBirthPlace());
		thePerson.setMultipleBirthInd(person.getMultipleBirthInd());
		thePerson.setBirthOrder(person.getBirthOrder());
		thePerson.setMothersMaidenName(person.getMothersMaidenName());
		thePerson.setMotherName(person.getMotherName());
		thePerson.setFatherName(person.getFatherName());
		thePerson.setMaritalStatusCode(person.getMaritalStatusCode());
		thePerson.setSsn(person.getSsn());
		thePerson.setDegree(person.getDegree());
		thePerson.setCountry(person.getCountry());
		thePerson.setPhoneAreaCode(person.getPhoneAreaCode());
		thePerson.setPhoneCountryCode(person.getPhoneCountryCode());
		thePerson.setPhoneExt(person.getPhoneExt());
		thePerson.setPhoneNumber(person.getPhoneNumber());
		thePerson.setDateChanged(DateTimeToString(person.getDateChanged()));
		thePerson.setDateCreated(DateTimeToString(person.getDateCreated()));
		
		if (person.getPersonIdentifiers() != null && person.getPersonIdentifiers().size() > 0) {
			Set<PersonIdentifierWeb> identifiers = new java.util.HashSet<PersonIdentifierWeb>(person.getPersonIdentifiers().size());
			for (org.openhie.openempi.model.PersonIdentifier personIdentifier : person.getPersonIdentifiers()) {
				if (personIdentifier.getDateVoided() == null) {
					identifiers.add(ModelTransformer.map(personIdentifier, PersonIdentifierWeb.class));
				}
			}
			thePerson.setPersonIdentifiers(identifiers);
		}
		return thePerson;
	}

	//------------------------------------------------------------------------------------------------
	
		// Role mapping
		public static Role mapToRole(RoleWeb roleWeb, Class<Role> roleClass) {
			if (log.isDebugEnabled()) {
				log.debug("Transforming object of type " + roleWeb.getClass() + " to type " + roleClass);
			}
			Role theRole = new Role();
			theRole.setId(roleWeb.getId());
			theRole.setName(roleWeb.getName());
			theRole.setDescription(roleWeb.getDescription());
	
			if (roleWeb.getPermissions() != null && roleWeb.getPermissions().size() > 0) {				
				Set<Permission> permissions= new java.util.HashSet<Permission>(roleWeb.getPermissions().size());
				for (PermissionWeb permissionWeb : roleWeb.getPermissions()) {
					org.openhie.openempi.model.Permission  permission = ModelTransformer.map(permissionWeb, org.openhie.openempi.model.Permission.class);					
					permissions.add(permission);
				}
				theRole.setPermissions(permissions);
			}
			return theRole;
		}
		
		public static RoleWeb mapToRole(Role role, Class<RoleWeb> roleClass, boolean withPermissions) {
			if (log.isDebugEnabled()) {
				log.debug("Transforming object of type " + role.getClass() + " to type " + roleClass);
			}
			RoleWeb theRole = new RoleWeb();
			theRole.setId(role.getId());
			theRole.setName(role.getName());
			theRole.setDescription(role.getDescription());

			if( withPermissions) {
				if (role.getPermissions() != null && role.getPermissions().size() > 0 ) {
					Set<PermissionWeb> permissionsWeb = new java.util.HashSet<PermissionWeb>(role.getPermissions().size());
					for (org.openhie.openempi.model.Permission permission: role.getPermissions()) {
						permissionsWeb.add( ModelTransformer.map(permission, PermissionWeb.class) );
					}
					theRole.setPermissions(permissionsWeb);
				}
			}

			return theRole;
		}

//------------------------------------------------------------------------------------------------
	
	// User mapping
	public static User mapToUser(UserWeb userWeb, Class<User> userClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + userWeb.getClass() + " to type " + userClass);
		}
		User theUser = new User();
		theUser.setId(userWeb.getId());
		theUser.setUsername(userWeb.getUsername());
		theUser.setPassword(userWeb.getPassword());
		theUser.setConfirmPassword(userWeb.getConfirmPassword());
		theUser.setPasswordHint(userWeb.getPasswordHint());
		theUser.setFirstName(userWeb.getFirstName());
		theUser.setLastName(userWeb.getLastName());
		theUser.setPhoneNumber(userWeb.getPhoneNumber());
		theUser.setEmail(userWeb.getEmail());
		theUser.setWebsite(userWeb.getWebsite());
		
		theUser.getAddress().setAddress(userWeb.getAddress());
		theUser.getAddress().setCity(userWeb.getCity());
		theUser.getAddress().setProvince(userWeb.getState());
		theUser.getAddress().setPostalCode(userWeb.getPostalCode());
		theUser.getAddress().setCountry(userWeb.getCountry());

		if(userWeb.getVersion() != null)
		   theUser.setVersion( Integer.parseInt(userWeb.getVersion()) );
		theUser.setEnabled(userWeb.getEnabled());
		theUser.setAccountExpired(userWeb.getAccountExpired());
		theUser.setAccountLocked(userWeb.getAccountLocked());
		theUser.setCredentialsExpired(userWeb.getCredentialsExpired());
		
		if (userWeb.getRoles() != null && userWeb.getRoles().size() > 0) {
			for (RoleWeb roleWeb : userWeb.getRoles()) {
				org.openhie.openempi.model.Role  role = ModelTransformer.mapToRole(roleWeb, org.openhie.openempi.model.Role.class);
				theUser.addRole(role);
			}
		}
		return theUser;
	}
	
	public static UserWeb mapToUser(User user, Class<UserWeb> userClass, boolean withRoles) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + user.getClass() + " to type " + userClass);
		}
		UserWeb theUser = new UserWeb();
		theUser.setId(user.getId());
		theUser.setUsername(user.getUsername());
		theUser.setPassword(user.getPassword());
		theUser.setConfirmPassword(user.getConfirmPassword());
		theUser.setPasswordHint(user.getPasswordHint());
		theUser.setFirstName(user.getFirstName());
		theUser.setLastName(user.getLastName());
		theUser.setPhoneNumber(user.getPhoneNumber());
		theUser.setEmail(user.getEmail());
		theUser.setWebsite(user.getWebsite());

		theUser.setAddress(user.getAddress().getAddress());
		theUser.setCity(user.getAddress().getCity());
		theUser.setState(user.getAddress().getProvince());
		theUser.setPostalCode(user.getAddress().getPostalCode());
		theUser.setCountry(user.getAddress().getCountry());

		theUser.setVersion(user.getVersion().toString());
		theUser.setEnabled(user.isEnabled());
		theUser.setAccountExpired(user.isAccountExpired());
		theUser.setAccountLocked(user.isAccountLocked());
		theUser.setCredentialsExpired(user.isCredentialsExpired());
		
		if( withRoles) {
			if (user.getRoles() != null && user.getRoles().size() > 0 ) {
				Set<RoleWeb> rolesWeb = new java.util.HashSet<RoleWeb>(user.getRoles().size());
				for (org.openhie.openempi.model.Role role : user.getRoles()) {
					rolesWeb.add(ModelTransformer.mapToRole(role, RoleWeb.class, false));
				}
				theUser.setRoles(rolesWeb);
			}
		}

		return theUser;
	}

	// Report mapping
	public static Report mapToReport(ReportWeb reportWeb, Class<Report> reportClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + reportWeb.getClass() + " to type " + reportClass);
		}
		Report theReport = new Report();
		theReport.setReportId(reportWeb.getReportId());
		theReport.setName(reportWeb.getName());
		theReport.setNameDisplayed(reportWeb.getNameDisplayed());
		theReport.setDescription(reportWeb.getDescription());
		theReport.setTemplateName(reportWeb.getTemplateName());
		theReport.setDataGenerator(reportWeb.getDataGenerator());
		
		java.util.Map<String,ReportParameter> reportParamByName = new java.util.HashMap<String,ReportParameter>();
		if (reportWeb.getReportParameters() != null && reportWeb.getReportParameters().size() > 0) {
			for (ReportParameterWeb reportParameterWeb : reportWeb.getReportParameters()) {
				org.openhie.openempi.model.ReportParameter reportParameter = ModelTransformer.map(reportParameterWeb, org.openhie.openempi.model.ReportParameter.class);
				theReport.addReportParameter(reportParameter);
				reportParameter.setReport(theReport);
				reportParamByName.put(reportParameter.getName(), reportParameter);
			}
		}
		
		if (reportWeb.getReportQueries() != null && reportWeb.getReportQueries().size() > 0) {
			for (ReportQueryWeb reportQueryWeb : reportWeb.getReportQueries()) {
				// org.openhie.openempi.model.ReportQuery reportQuery = ModelTransformer.map(reportQueryWeb, org.openhie.openempi.model.ReportQuery.class);
				 ReportQuery theReportQuery = new ReportQuery();				
				 theReportQuery.setReportQueryId(reportQueryWeb.getReportQueryId());
				 theReportQuery.setName(reportQueryWeb.getName());
				 theReportQuery.setQuery(reportQueryWeb.getQuery());	

				 // Query Parameters
				 if (reportQueryWeb.getReportQueryParameters() != null && reportQueryWeb.getReportQueryParameters().size() > 0) {
					Set<ReportQueryParameter> reportQueryParameters = new java.util.HashSet<ReportQueryParameter>(reportQueryWeb.getReportQueryParameters().size());
					for (ReportQueryParameterWeb reportQueryParameterWeb : reportQueryWeb.getReportQueryParameters() ) {
						org.openhie.openempi.model.ReportQueryParameter reportQueryParameter = ModelTransformer.map(reportQueryParameterWeb, ReportQueryParameter.class);
						reportQueryParameters.add( reportQueryParameter);
						reportQueryParameter.setReportQuery(theReportQuery);
						log.debug("Looking up the report parameter by name: " + reportQueryParameterWeb.getReportParameter().getName());
						ReportParameter reportParameter = reportParamByName.get(reportQueryParameterWeb.getReportParameter().getName());
						if (reportParameter == null) {
							log.warn("Found a report query parameter that points to a report parameter that is not known. Parameter name is: " +
									reportQueryParameterWeb.getReportParameter().getName());
						}
						reportQueryParameter.setReportParameter(reportParameter);
					}
					theReportQuery.setReportQueryParameters(reportQueryParameters);
				 }
				 theReport.addReportQuery(theReportQuery);
				 
				// set report back to reportQuery
				 theReportQuery.setReport(theReport);
			}
		}
		return theReport;
	}
	
	public static ReportWeb mapToReport(Report report, Class<ReportWeb> reportClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + report.getClass() + " to type " + reportClass);
		}
		
		ReportWeb theReport = new ReportWeb();
		theReport.setReportId(report.getReportId());
		theReport.setName(report.getName());
		theReport.setNameDisplayed(report.getNameDisplayed());
		theReport.setDescription(report.getDescription());
		theReport.setTemplateName(report.getTemplateName());
		theReport.setDataGenerator(report.getDataGenerator());
		
		// Parameters
		if (report.getReportParameters() != null && report.getReportParameters().size() > 0) {
			Set<ReportParameterWeb> reportParameterWebs = new java.util.HashSet<ReportParameterWeb>(report.getReportParameters().size());
			for (ReportParameter reportParameter : report.getReportParameters()) {
				reportParameterWebs.add( ModelTransformer.map(reportParameter, ReportParameterWeb.class));
			}
			theReport.setReportParameters(reportParameterWebs);
		}
		
		//Queries
		if (report.getReportQueries() != null && report.getReportQueries().size() > 0) {
			Set<ReportQueryWeb> reportQueryWebs = new java.util.HashSet<ReportQueryWeb>(report.getReportQueries().size());
			for (ReportQuery reportQuery : report.getReportQueries()) {
				
				 // reportQueryWebs.add( ModelTransformer.map(reportQuery, ReportQueryWeb.class));				
				 ReportQueryWeb theReportQuery = new ReportQueryWeb();				
				 theReportQuery.setReportQueryId(reportQuery.getReportQueryId());
				 theReportQuery.setName(reportQuery.getName());
				 theReportQuery.setQuery(reportQuery.getQuery());	

				 // Query Parameters
				 if (reportQuery.getReportQueryParameters() != null && reportQuery.getReportQueryParameters().size() > 0) {
					Set<ReportQueryParameterWeb> reportQueryParameterWebs = new java.util.HashSet<ReportQueryParameterWeb>(reportQuery.getReportQueryParameters().size());
					for (ReportQueryParameter reportQueryParameter : reportQuery.getReportQueryParameters() ) {
				
						reportQueryParameterWebs.add(ModelTransformer.map(reportQueryParameter, ReportQueryParameterWeb.class));
					}
					theReportQuery.setReportQueryParameters(reportQueryParameterWebs);
				 }
				 
				 reportQueryWebs.add(theReportQuery);
			}
			theReport.setReportQueries(reportQueryWebs);
		}
		return theReport;
	}

	// Report request mapping
	public static ReportRequest mapToReportRequest(ReportRequestWeb reportRequestWeb, Class<ReportRequest> reportRequestClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + reportRequestWeb.getClass() + " to type " + reportRequestClass);
		}
		ReportRequest theReportRequest = new ReportRequest();
		theReportRequest.setReportId(reportRequestWeb.getReportId());
		theReportRequest.setRequestDate(reportRequestWeb.getRequestDate());
		
		if (reportRequestWeb.getReportParameters() != null && reportRequestWeb.getReportParameters().size() > 0) {
			List<ReportRequestParameter> reportRequestParameters = new ArrayList<ReportRequestParameter>(reportRequestWeb.getReportParameters().size());
			
			int count = reportRequestWeb.getReportParameters().size();				
			for (int i=0; i < count; i++) {
				ReportRequestParameterWeb reportRequestParameterWeb = reportRequestWeb.getReportParameters().get(i);
		
				reportRequestParameters.add(ModelTransformer.map(reportRequestParameterWeb, ReportRequestParameter.class));
			}
			theReportRequest.setReportParameters(reportRequestParameters);
		}		
		return theReportRequest;
	}
	
	public static ReportRequestWeb mapToReportRequest(ReportRequest reportRequest, Class<ReportRequestWeb> reportRequestClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + reportRequest.getClass() + " to type " + reportRequestClass);
		}
		
		ReportRequestWeb theReportRequest = new ReportRequestWeb();
		theReportRequest.setReportId(reportRequest.getReportId());
		theReportRequest.setRequestDate(reportRequest.getRequestDate());
		
		// Parameters
		if (reportRequest.getReportParameters() != null && reportRequest.getReportParameters().size() > 0) {
			List<ReportRequestParameterWeb> reportParameterWebs = new ArrayList<ReportRequestParameterWeb>(reportRequest.getReportParameters().size());
			
			int count = reportRequest.getReportParameters().size();		
			for (int i=0; i < count; i++) {
				ReportRequestParameter reportRequestParameter = reportRequest.getReportParameters().get(i);	
				
				reportParameterWebs.add( ModelTransformer.map(reportRequestParameter, ReportRequestParameterWeb.class));				
			}
			theReportRequest.setReportParameters(reportParameterWebs);
		}		
		return theReportRequest;
	}
	
	// Report request entry mapping
	public static ReportRequestEntryWeb mapToReportRequestEntry(ReportRequestEntry reportRequestEntry, Class<ReportRequestEntryWeb> reportRequestEntryClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + reportRequestEntry.getClass() + " to type " + reportRequestEntryClass);
		}
		
		ReportRequestEntryWeb theReportRequestEntryWeb = new ReportRequestEntryWeb();
		
		theReportRequestEntryWeb.setReportRequestId(reportRequestEntry.getReportRequestId());			
//		theReportRequestEntryWeb.setReport( ModelTransformer.mapToReport( reportRequestEntry.getReport(), ReportWeb.class) );
//		theReportRequestEntryWeb.setUserRequested( ModelTransformer.map( reportRequestEntry.getUserRequested(), UserWeb.class) );
		
		theReportRequestEntryWeb.setUserName(reportRequestEntry.getUserRequested().getUsername());
		theReportRequestEntryWeb.setReportName(reportRequestEntry.getReport().getName());
		theReportRequestEntryWeb.setReportDescription(reportRequestEntry.getReport().getDescription());

		
		theReportRequestEntryWeb.setDateRequested(reportRequestEntry.getDateRequested());
		theReportRequestEntryWeb.setDateCompleted(reportRequestEntry.getDateCompleted());
		
		theReportRequestEntryWeb.setCompleted(reportRequestEntry.getCompleted());
		theReportRequestEntryWeb.setReportHandle(reportRequestEntry.getReportHandle());
		
		return theReportRequestEntryWeb;
	}
	
	// ParameterType mapping
	public static ParameterTypeWeb mapToParameterType(ParameterType parameterType, Class<ParameterTypeWeb> parameterTypeClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + parameterType.getClass() + " to type " + parameterTypeClass);
		}
		
		ParameterTypeWeb theParameterTypeWeb = new ParameterTypeWeb();
		theParameterTypeWeb.setName(parameterType.getName());
		theParameterTypeWeb.setDisplayName(parameterType.getDisplayName());
		
		if(parameterType.getDisplayType() == FormEntryDisplayType.CHECK_BOX)	 	
			theParameterTypeWeb.setDisplayType("CHECKBOX");	
		else if(parameterType.getDisplayType() == FormEntryDisplayType.TEXT_FIELD)
			theParameterTypeWeb.setDisplayType("TEXTFIELD");		
		else if(parameterType.getDisplayType() == FormEntryDisplayType.DATE_FIELD)
			theParameterTypeWeb.setDisplayType("DATEFIELD");	
		else if(parameterType.getDisplayType() == FormEntryDisplayType.DROP_DOWN)
			theParameterTypeWeb.setDisplayType("DROPDOWN");	
		else if(parameterType.getDisplayType() == FormEntryDisplayType.RADIO)
			theParameterTypeWeb.setDisplayType("RADIO");	
		return theParameterTypeWeb;
	}

	// auditEvent mapping
	public static AuditEventWeb mapAuditEvent(AuditEvent auditEvent, Class<AuditEventWeb> auditEventClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + auditEvent.getClass() + " to type " + auditEventClass);
		}
		
		AuditEventWeb auditEventWeb = new AuditEventWeb();
		
		AuditEventType type = auditEvent.getAuditEventType();		
		AuditEventTypeWeb typeWeb = new AuditEventTypeWeb();
			typeWeb.setAuditEventTypeCd(type.getAuditEventTypeCd());
			typeWeb.setAuditEventTypeCode(type.getAuditEventTypeCode());
			typeWeb.setAuditEventTypeName(type.getAuditEventTypeName());
			typeWeb.setAuditEventTypeDescription(type.getAuditEventTypeDescription());		
			
		auditEventWeb.setAuditEventType(typeWeb);
		auditEventWeb.setAuditEventDescription(auditEvent.getAuditEventDescription());
		auditEventWeb.setDateCreated( auditEvent.getDateCreated());
		
		if( auditEvent.getRefPerson() != null ) {
			PersonWeb refPerson = mapToPerson(auditEvent.getRefPerson(), PersonWeb.class);
			auditEventWeb.setRefPerson(refPerson);
		}
		if( auditEvent.getAltRefPerson() != null ) {
			PersonWeb altPerson = mapToPerson(auditEvent.getAltRefPerson(), PersonWeb.class);
			auditEventWeb.setAltRefPerson(altPerson);
		}
		
		if(auditEvent.getUserCreatedBy() != null ) {
			UserWeb user = mapToUser(auditEvent.getUserCreatedBy(), UserWeb.class, false);
			auditEventWeb.setUserCreatedBy(user);
		}
		
		return auditEventWeb;
	}

	// reviewRecordPair mapping
	public static ReviewRecordPairWeb mapReviewRecordPair(ReviewRecordPair reviewRecordPair, Class<ReviewRecordPairWeb> reviewRecordPairClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + reviewRecordPair.getClass() + " to type " + reviewRecordPairClass);
		}
		
		ReviewRecordPairWeb reviewRecordPairWeb = new ReviewRecordPairWeb();
		
		reviewRecordPairWeb.setReviewRecordPairId(reviewRecordPair.getReviewRecordPairId());
		reviewRecordPairWeb.setDateCreated(reviewRecordPair.getDateCreated());
		reviewRecordPairWeb.setWeight(reviewRecordPair.getWeight());
		reviewRecordPairWeb.setRecordsMatch(reviewRecordPair.getRecordsMatch());
		if( reviewRecordPair.getLinkSource() != null ) {
			reviewRecordPairWeb.setLinkSource( ModelTransformer.map(reviewRecordPair.getLinkSource(), LinkSourceWeb.class));
		}				
		reviewRecordPairWeb.setLeftPerson(mapToPerson(reviewRecordPair.getPersonLeft(), PersonWeb.class));
		reviewRecordPairWeb.setRightPerson(mapToPerson(reviewRecordPair.getPersonRight(), PersonWeb.class));	
		reviewRecordPairWeb.setUserCreatedBy(mapToUser(reviewRecordPair.getUserCreatedBy(), UserWeb.class, false));
		return reviewRecordPairWeb;
	}
	
	// messageLog mapping
	public static MessageLogEntryWeb mapMessage(MessageLogEntry messageLog, Class<MessageLogEntryWeb> messageLogClass) {
		if (log.isDebugEnabled()) {
			log.debug("Transforming object of type " + messageLog.getClass() + " to type " + messageLogClass);
		}
		
		MessageLogEntryWeb messageLogWeb = new MessageLogEntryWeb();
		MessageTypeWeb outgoingTypeWeb = null;
		MessageTypeWeb incomingTypeWeb = null;
		
		messageLogWeb.setMessageLogId(messageLog.getMessageLogId());
		
		MessageType type = messageLog.getOutgoingMessageType();	
		if(type != null ) {
			outgoingTypeWeb = new MessageTypeWeb();
				outgoingTypeWeb.setMessageTypeCd(type.getMessageTypeCd());
				outgoingTypeWeb.setMessageTypeCode(type.getMessageTypeCode());
				outgoingTypeWeb.setMessageTypeName(type.getMessageTypeName());
				outgoingTypeWeb.setMessageTypeDescription(type.getMessageTypeDescription());		
		}	
		type = messageLog.getIncomingMessageType();	
		if(type != null ) {
			incomingTypeWeb = new MessageTypeWeb();
				incomingTypeWeb.setMessageTypeCd(type.getMessageTypeCd());
				incomingTypeWeb.setMessageTypeCode(type.getMessageTypeCode());
				incomingTypeWeb.setMessageTypeName(type.getMessageTypeName());
				incomingTypeWeb.setMessageTypeDescription(type.getMessageTypeDescription());		
		}	
		messageLogWeb.setIncomingMessage(messageLog.getIncomingMessage());
		messageLogWeb.setOutgoingMessage(messageLog.getOutgoingMessage());
		messageLogWeb.setIncomingMessageType(incomingTypeWeb);
		messageLogWeb.setOutgoingMessageType(outgoingTypeWeb);
		messageLogWeb.setDateReceived(messageLog.getDateReceived());
				
		return messageLogWeb;
	}
	
	public static String DateToString(Date date)
	{
		if( date == null)
			return "";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = df.format(date);

		// System.out.println("Report Date: " + strDate);
		return strDate;
	}
	
	public static Date StringToDate(String strDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		if( strDate != null && !strDate.isEmpty() ) {
			try {
				date = df.parse(strDate);
				// System.out.println("Today = " + df.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
	
	public static String DateTimeToString(Date date)
	{
		if( date == null)
			return "";
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		String strDate = df.format(date);

		// System.out.println("Report Date: " + strDate);
		return strDate;
	}
	
	public static Date StringToDateTime(String strDate){
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
		Date date = null;
		if( strDate != null && !strDate.isEmpty() ) {
			try {
				date = df.parse(strDate);
				// System.out.println("Today = " + df.format(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
}
