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
package org.openhie.openempi.openpixpdq.v3.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jws.WebService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.QUQIIN000003UV01Type;
import org.hl7.v3.QUQIMT000001UV01QueryContinuation;
import org.openhealthtools.openexchange.actorconfig.IheActor;
import org.openhealthtools.openexchange.datamodel.Identifier;
import org.openhealthtools.openexchange.datamodel.Patient;
import org.openhealthtools.openexchange.datamodel.PatientIdentifier;
import org.openhealthtools.openpixpdq.api.Hl7MessageValidationException;
import org.openhealthtools.openpixpdq.api.IPdSupplierAdapter;
import org.openhealthtools.openpixpdq.api.PdSupplierException;
import org.openhealthtools.openpixpdq.api.PdqQuery;
import org.openhealthtools.openpixpdq.api.PdqResult;
import org.openhealthtools.openpixpdq.common.AssigningAuthorityUtil;
import org.openhealthtools.openpixpdq.common.ContinuationPointer;
import org.openhealthtools.openpixpdq.common.PatientBroker;
import org.openhealthtools.openpixpdq.common.PixPdqFactory;
import org.openhie.openempi.openpixpdq.v3.PDQSupplierPortType;
import org.openhie.openempi.openpixpdq.v3.util.Hl7ConversionHelper;


@WebService(endpointInterface = "org.openhie.openempi.openpixpdq.v3.PDQSupplierPortType")
public class PdqSupplierServiceImpl extends AbstractIheService implements PDQSupplierPortType
{	
	protected final Log log = LogFactory.getLog(getClass());
	private IPdSupplierAdapter pdAdapter;
	private PdqSupplierV3 pdqSupplier;
	private static HashMap<String,ContinuationPointer> cachedQueryResults = new HashMap<String,ContinuationPointer>();
	
	public void init() {
		pdAdapter = PixPdqFactory.getPdSupplierAdapter();
		log.debug("Initializing the service." + pdAdapter);
		super.init();
		String actorName = getActorName();
		log.info("Looking up the PDQ Supplier using key " + actorName);
		IheActor iheActor = PatientBroker.getInstance().getActorByName(actorName);
		log.info("Obtained a actor: " + iheActor);
		if  (iheActor instanceof PdqSupplierV3) {
			pdqSupplier = (PdqSupplierV3) iheActor;
		} else {
			log.warn("Service has not been configured properly since the ihe actor is either undefined or of incorrect type.");
		}
	}

	public PRPAIN201306UV02 pdqSupplierQUQIIN000003UV01Continue(QUQIIN000003UV01Type body) {
		log.debug("Received request: " + body);
		
		Hl7HeaderAddress addr = extractHeaderAddress(body.getReceiver(), body.getSender());
		try {
			
			validateSenderReceivingApplicationAndFacility(addr, pdqSupplier.getActorDescription());
			
		} catch (Hl7MessageValidationException e) {
			
		}
		
		PRPAIN201306UV02 response = null;
	    try {
	    	QUQIMT000001UV01QueryContinuation queryContinuation = Hl7ConversionHelper.extractQueryContinuation(body);
			String queryId = Hl7ConversionHelper.buildPointerFromQueryId(queryContinuation.getQueryId());
			
			int startResultNumber = 0;
	    	int continuationQuantity = 0;
			if( queryContinuation.getStartResultNumber() != null ) {
				startResultNumber = queryContinuation.getStartResultNumber().getValue().intValue();
			}
			if( queryContinuation.getContinuationQuantity() != null ) {
				continuationQuantity = queryContinuation.getContinuationQuantity().getValue().intValue();
			}	    	
	    
			ContinuationPointer continuation = cachedQueryResults.get(queryId);
			
			if( continuation != null ) {
			
				// build ControlActProcess with queryAck and queryByParameter	
				if( queryContinuation.getStartResultNumber() != null ) {
					
					// start number in continuation request
					startResultNumber = queryContinuation.getStartResultNumber().getValue().intValue();
					response = Hl7ConversionHelper.generateResponseMessage(startResultNumber, continuationQuantity, continuation, body);
				} else {
					
					// last query start number
					startResultNumber = continuation.getstartResultNumber();
					response = Hl7ConversionHelper.generateResponseMessage(startResultNumber, continuationQuantity, continuation, body);				
				}
				
				
				if(startResultNumber + continuationQuantity < continuation.getTotalRecords()) {
					
					// put the startResultNumber for next continuation request
					continuation.setstartResultNumber( startResultNumber + continuationQuantity );
					continuation.setLastRequestTime(new java.util.Date().getTime());
					synchronized(cachedQueryResults) {
		            	cachedQueryResults.put(queryId, continuation);
		            }					
				} else {
					
					// The continuation reaches the last patient record
		    		cachedQueryResults.remove(queryId);		    		
				}
			} else {
				
				// Cannot find the Continuation Pointer
				response = Hl7ConversionHelper.generateResponseMessage(startResultNumber, continuationQuantity, null, body);					
			}
			
	    } catch (Throwable e) {
	    	
	    	log.error("Encountered a problem while processing the query continuation request: " + e, e);
	    	response = Hl7ConversionHelper.generateResponseMessage(0, 0, null, body);;
	    }
	    				
		return response;
	}

	public MCCIIN000002UV01 pdqSupplierQUQIIN000003UV01Cancel(QUQIIN000003UV01Type body) {
		log.debug("Received request: " + body);
				
		Hl7HeaderAddress addr = extractHeaderAddress(body.getReceiver(), body.getSender());
		try {
			
			validateSenderReceivingApplicationAndFacility(addr, pdqSupplier.getActorDescription());
			
		} catch (Hl7MessageValidationException e) {
			
		}
		
		MCCIIN000002UV01 response = null;
	    try {
	    	QUQIMT000001UV01QueryContinuation queryContinuation = Hl7ConversionHelper.extractQueryContinuation(body);
	    	String statusCode = queryContinuation.getStatusCode().getCode();
	    	
	    	// Cancel the request
	    	if( statusCode.equals("aborted") ) {
	    		String queryId = Hl7ConversionHelper.buildPointerFromQueryId(queryContinuation.getQueryId());
	    		cachedQueryResults.remove(queryId);
	    		
		    	response = Hl7ConversionHelper.generateResponseMessage(body, false);
	    	}
	    	
	    } catch (Throwable e) {
	    	
	    	log.error("Encountered a problem while processing the query continuation request: " + e, e);
	    	response = Hl7ConversionHelper.generateResponseMessage(body, true);
	    }
	    				
		return response;	
	}

	public PRPAIN201306UV02 pdqSupplierPRPAIN201305UV02(PRPAIN201305UV02 message) {
		log.debug("Received request: " + message);

		Hl7HeaderAddress addr = extractHeaderAddress(message.getReceiver(), message.getSender());
		try {
			
			validateSenderReceivingApplicationAndFacility(addr, pdqSupplier.getActorDescription());
			
		} catch (Hl7MessageValidationException e) {
			
			PRPAIN201306UV02 response = Hl7ConversionHelper.generateResponseMessage(null, null, null, message, true, null);	  		
			return response;
		}

		PRPAIN201306UV02 response = null;
	    try {
			
			PdqQuery query = Hl7ConversionHelper.extractQueryPerson(pdqSupplier.getActorDescription(), message);
			List<Identifier> returnDomains = query.getReturnDomains();
			// check validation of identifiers in OtherIDsScopingOrganization
			boolean validDomains = true;
			List<Identifier> invalidDomains = new ArrayList<Identifier>();
			for (Identifier identifier : returnDomains) {
	            if(!AssigningAuthorityUtil.validateDomain( identifier, pdqSupplier.getActorDescription(), pdAdapter) ) {
	            	validDomains = false;
	            	invalidDomains.add(identifier);
	            }
		    }
			
			if( !validDomains ) {
				// IHE_ITI_TF_Vol2B: 6110
		    	response = Hl7ConversionHelper.generateResponseMessage(null, null, null, message, true, invalidDomains);
			} else {
				PdqResult result = pdAdapter.findPatients(query, null);
				List<List<Patient>> listLists = getPatientList(result, returnDomains);
				
				// This code that was commented out is incorrect. The inner list of patients are patients that are all
				// linked together whereas the outer list is a list of distinct physical
				// patients that matched the query criteria.
				//
				// The actual count of patients found is the number of entries in the outer
				// list. The way we present each inner list, is by generating one record
				// presenting the first patients demographics in the record but list all
				// the identifies from all the patients in the inner list as all belonging
				// to the first inner patient record. I implemented this functionality in
				// extractSublist but please check.
				List<Patient> subList = Hl7ConversionHelper.extractSublist(listLists);
//				for (List<Patient> list : listLists) {
//					if (list != null) {
//						for (Patient patient : list) {
//							if (patient != null) {
//								patients.add(patient);
//							}
//						}
//					}
//				}				
								
				ContinuationPointer continuation = Hl7ConversionHelper.extractContinuation(message);
				Integer initialQuantity = Hl7ConversionHelper.extractQueryInitialQuantity(message);		
				int currentQuantity = subList.size();	
				if(initialQuantity != null) {
				   currentQuantity = Math.min(subList.size(), initialQuantity);
				}

				if (continuation != null) {
					continuation.setPatients(listLists);
					continuation.setLastRequestTime(new java.util.Date().getTime());
					continuation.setQueryTag(message.getControlActProcess().getQueryByParameter());
					continuation.setReturnDomain(returnDomains);
					continuation.setTotalRecords(listLists.size());
					continuation.setstartResultNumber(currentQuantity);
					
					// If the number of records they requested is valid, is more than zero
					// and less than the total number of records, then we need to cache the query
					// results for future reference.
					//
					// In a continuation request, we can lookup the original query results using
					// the pointer which is constructed using HlConversionHelper.buildPointerFromQueryId(queryId);
					//
					if (initialQuantity != null && initialQuantity > 0 && initialQuantity < listLists.size()) {
						synchronized(cachedQueryResults) {
	                    	cachedQueryResults.put(continuation.getPointer(), continuation);
	                    }
					}
				}

		    	response = Hl7ConversionHelper.generateResponseMessage(0, currentQuantity, subList, message, false, null);
			}
	    	
	    } catch (PdSupplierException e) {
	    	log.error("Encountered a problem while processing find patients: " + e, e);
	    	response = Hl7ConversionHelper.generateResponseMessage(null, null, null, message, true, null);	
	    	
	    } catch (Throwable e) {
	    	
	    	log.error("Encountered a problem while processing the patient discovery request: " + e, e);
	    	response = Hl7ConversionHelper.generateResponseMessage(null, null, null, message, true, null);
	    }
	    
	    return response; 
	}
/*	
	private List<Patient> extractSublist(List<List<Patient>> lists) {
		List<Patient> subList = new ArrayList<Patient>();
		for (List<Patient> innerList : lists) {
			Patient superPatient = innerList.get(0);
			if (innerList.size() > 0) {
				for (int i=1; i < innerList.size(); i++) {
					superPatient.getPatientIds().addAll(innerList.get(i).getPatientIds());
				}
			}
			subList.add(superPatient);
		}
		return subList;
	}
*/
	
	/*
	 * Return the patient list for specified return domain otherwise it return 
	 * all patient list.
	 * 
	 * @param pdqresult contains all matching patients
	 * @param returnDomains the domains whose patient demographics to be 
	 *        returned to the PDQ consumer
	 * @return List<List<Patient>> a list of filtered patient by return domains. The
	 * first list for different logic patients, while the second list is for the same 
	 * logic patient in different domains.
	 */
	private List<List<Patient>> getPatientList(PdqResult pdqResult, List<Identifier> returnDomains){
		List<List<Patient>>  finalPatients = new ArrayList<List<Patient>>();
		
		  //pdqResult can never be null, otherwise exception would be thrown. 
        List<List<Patient>> allPatients = pdqResult.getPatients();
        // List<List<Patient>> finalPatients = new ArrayList<List<Patient>>();
        if (returnDomains.size() == 0) {
            //If no return domain is specified, we consider all patients
           finalPatients= allPatients;
        }
        else {
            //Find a list of final patients that have ids in the return domain.
            for (List<Patient> lpatients : allPatients) {
                List<Patient> filteredPatients = new ArrayList<Patient>();
                for (Patient patient : lpatients) {
                   List<PatientIdentifier> pids = patient.getPatientIds();
                   for (PatientIdentifier pid : pids) {
                 	  Identifier authority = pid.getAssigningAuthority();
                 	  
                       //authority might be partial (either namespaceId or universalId),
                       //so need to map to the one used in the configuration. 
                       authority = AssigningAuthorityUtil.reconcileIdentifier( authority, pdqSupplier.getActorDescription(), pdAdapter );
                       if (returnDomains.contains( authority )) {
                           filteredPatients.add( patient );
                           break;
                       }                	  
                   }
                }
                //We don't want an empty list of patient 
                if (filteredPatients.size() > 0)
                   finalPatients.add( filteredPatients );
            }
        }
		return finalPatients;
	}
}
