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
package org.openhie.openempi.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openhie.openempi.AuthenticationException;
import org.openhie.openempi.Constants;
import org.openhie.openempi.blocking.BlockingLifecycleObserver;
import org.openhie.openempi.blocking.BlockingService;
import org.openhie.openempi.configuration.Configuration;
import org.openhie.openempi.configuration.ScheduledTaskEntry;
import org.openhie.openempi.loader.FileLoaderConfigurationService;
import org.openhie.openempi.matching.MatchingLifecycleObserver;
import org.openhie.openempi.matching.MatchingService;
import org.openhie.openempi.model.User;
import org.openhie.openempi.notification.EntityAddObservable;
import org.openhie.openempi.notification.EntityDeleteObservable;
import org.openhie.openempi.notification.EntityLinkObservable;
import org.openhie.openempi.notification.EntityUnlinkObservable;
import org.openhie.openempi.notification.EntityUpdateObservable;
import org.openhie.openempi.notification.MatchingConfigurationUpdatedObservable;
import org.openhie.openempi.notification.NotificationService;
import org.openhie.openempi.profiling.DataProfileService;
import org.openhie.openempi.report.ReportService;
import org.openhie.openempi.service.AuditEventService;
import org.openhie.openempi.service.PersonManagerService;
import org.openhie.openempi.service.PersonQueryService;
import org.openhie.openempi.service.UserManager;
import org.openhie.openempi.service.ValidationService;
import org.openhie.openempi.singlebestrecord.SingleBestRecordService;
import org.openhie.openempi.stringcomparison.StringComparisonService;
import org.openhie.openempi.transformation.TransformationService;
import org.openhie.openempi.util.PropertiesUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Context implements ApplicationContextAware
{
	protected static final Log log = LogFactory.getLog(Context.class);
	private static final int THREAD_POOL_SIZE = 5;
	private static final int SCHEDULER_THREAD_POOL_SIZE = 5;
	
	private static final ThreadLocal<Object[] /* UserContext */> userContextHolder = new ThreadLocal<Object[] /* UserContext */>();
	private static ApplicationContext applicationContext;
	private static UserManager userManager;
	private static PersonManagerService personService;
	private static PersonQueryService personQueryService;
	private static ValidationService validationService;
	private static Configuration configuration;
	private static MatchingService matchingService;
	private static BlockingService blockingService;
	private static AuditEventService auditEventService;
	private static StringComparisonService stringComparisonService;
	private static TransformationService transformationService;
	private static FileLoaderConfigurationService fileLoaderConfigurationService;
	private static NotificationService notificationService;
	private static DataProfileService dataProfileService;
	private static ReportService reportService;
	private static SingleBestRecordService singleBestRecordService;
	private static ExecutorService threadPool;
	private static ScheduledExecutorService scheduler;
	private static boolean isInitialized = false;
	
	// Implements the Observer pattern for a lightweight mechanism to listen on entity events
	private static EntityAddObservable entityAddObservable = new EntityAddObservable();
	private static EntityDeleteObservable entityDeleteObservable = new EntityDeleteObservable();
	private static EntityLinkObservable entityLinkObservable = new EntityLinkObservable();
	private static EntityUnlinkObservable entityUnlinkObservable = new EntityUnlinkObservable();
	private static EntityUpdateObservable entityUpdateObservable = new EntityUpdateObservable();
	private static MatchingConfigurationUpdatedObservable matchingConfigurationUpdatedObservable = new MatchingConfigurationUpdatedObservable();
	
	public static void startup() {
		if (isInitialized) {
			return;
		}
		try {
			applicationContext = new ClassPathXmlApplicationContext(getConfigLocationsAsArray());
			applicationContext.getBean("context");
			configuration.init();
			threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
			scheduler = Executors.newScheduledThreadPool(SCHEDULER_THREAD_POOL_SIZE);
			startBlockingService();
			startMatchingService();
			startNotificationService();
			startScheduledTasks();
			isInitialized = true;
		} catch (Throwable t) {
			log.error("Failed while setting up the context for OpenEMPI: " + t, t);
		}
	}
	
	public static void shutdown() {
		stopMatchingService();
		stopBlockingService();
		stopScheduledTasks();
		stopThreadPool();
		stopObservers();
		isInitialized = false;		
	}
	
	public static void shutdownAll() {
		stopMatchingService();
		stopBlockingService();
		if (notificationService != null) {
			try {
				notificationService.shutdown();
			} catch (Exception e) {
				log.error("Failed while shutting down the notification service for OpenEMPI: " + e, e);
			}
		}
		stopThreadPool();
		isInitialized = false;		
	}
	
	public static void notifyObserver(ObservationEventType eventType, Object eventData) {
		if (eventType == ObservationEventType.ENTITY_ADD_EVENT) {
			entityAddObservable.setChanged();
			entityAddObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of an add event: " + eventData);
		} else 	if (eventType == ObservationEventType.ENTITY_DELETE_EVENT) {
			entityDeleteObservable.setChanged();
			entityDeleteObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of a delete event: " + eventData);
		} else 	if (eventType == ObservationEventType.ENTITY_LINK_EVENT) {
			entityLinkObservable.setChanged();
			entityLinkObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of a link event: " + eventData);
		} else 	if (eventType == ObservationEventType.ENTITY_UNLINK_EVENT) {
			entityUnlinkObservable.setChanged();
			entityUnlinkObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of an unlink event: " + eventData);
		} else 	if (eventType == ObservationEventType.ENTITY_UPDATE_EVENT) {
			entityUpdateObservable.setChanged();
			entityUpdateObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of an update event: " + eventData);			
		} else 	if (eventType == ObservationEventType.MATCHING_CONFIGURATION_UPDATED_EVENT) {
			matchingConfigurationUpdatedObservable.setChanged();
			matchingConfigurationUpdatedObservable.notifyObservers(eventData);
			log.info("Notified observers of the occurence of a matching configuration updated event: " + eventData);
		} else {
			log.warn("Received a notification event of an unknown event type: " + eventType);
		}		
	}
	
	public static void stopObservers() {
		entityAddObservable.deleteObservers();
		entityDeleteObservable.deleteObservers();
		entityLinkObservable.deleteObservers();
		entityUnlinkObservable.deleteObservers();
		entityUpdateObservable.deleteObservers();
		matchingConfigurationUpdatedObservable.deleteObservers();
	}
	
	public static void registerObserver(Observer observer, ObservationEventType eventType) {
		if (eventType == ObservationEventType.ENTITY_ADD_EVENT) {
			entityAddObservable.addObserver(observer);
			log.info("Added entity add event observer: " + observer);
		} else 	if (eventType == ObservationEventType.ENTITY_DELETE_EVENT) {
			entityDeleteObservable.addObserver(observer);
			log.info("Added entity delete event observer: " + observer);
		} else 	if (eventType == ObservationEventType.ENTITY_UPDATE_EVENT) {
			entityUpdateObservable.addObserver(observer);
			log.info("Added entity update event observer: " + observer);
		} else 	if (eventType == ObservationEventType.MATCHING_CONFIGURATION_UPDATED_EVENT) {
			matchingConfigurationUpdatedObservable.addObserver(observer);
			log.info("Added matching configuration updated event observer: " + observer);
		} else {
			log.warn("Received event observer registration request for an unknown event type: " + eventType + 
					" from observer: " + observer);
		}
	}
	
	public static void unregisterObserver(Observer observer, ObservationEventType eventType) {
		if (eventType == ObservationEventType.ENTITY_ADD_EVENT) {
			entityAddObservable.deleteObserver(observer);
			log.info("Removed entity add event observer: " + observer);
		} else 	if (eventType == ObservationEventType.ENTITY_DELETE_EVENT) {
			entityDeleteObservable.deleteObserver(observer);
			log.info("Removed entity delete event observer: " + observer);
		} else 	if (eventType == ObservationEventType.ENTITY_UPDATE_EVENT) {
			entityUpdateObservable.deleteObserver(observer);
			log.info("Removed entity update event observer: " + observer);
		} else 	if (eventType == ObservationEventType.ENTITY_UPDATE_EVENT) {
			matchingConfigurationUpdatedObservable.deleteObserver(observer);
			log.info("Removed matching configuration updated event observer: " + observer);			
		} else {
			log.warn("Received event observer unregistration request for an unknown event type: " + eventType + 
					" from observer: " + observer);
		}
	}
	
	private static void stopScheduledTasks() {
		scheduler.shutdown();
		try {
			if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
				scheduler.shutdownNow();
				if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
					log.error("Scheduler did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			scheduler.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
	
	private static void stopThreadPool() {
		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
				threadPool.shutdownNow();
				if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
					log.error("Pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			threadPool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	public static boolean isInitialized() {
		return isInitialized;
	}
	
	public static String[] getConfigLocationsAsArray() {
		ArrayList<String> configLocations = getConfigLocations();
		return (String[]) configLocations.toArray(new String[]{});
	}

	public static ArrayList<String> getConfigLocations() {
		if (getOpenEmpiHome() == null) {
			log.error("The OPENEMPI_HOME environment variable has not been configured; shutting down.");
			throw new RuntimeException("The OPENEMPI_HOME environment variable has not been configured.");
		}
		ArrayList<String> configFiles = generateConfigFileList();
		
		addExtensionContextsFromPropertiesFile(configFiles);
		addExtensionContextsFromSystemProperty(configFiles);
		return configFiles;
	}

	public static String getOpenEmpiHome() {
		String openEmpiHome = Constants.OPENEMPI_HOME_ENV_VALUE;
		if (openEmpiHome == null || openEmpiHome.length() == 0) {
			openEmpiHome = Constants.OPENEMPI_HOME_VALUE;
		} else {
			System.setProperty(Constants.OPENEMPI_HOME, openEmpiHome);
		}
		log.debug("OPENEMPI_HOME is set to " + openEmpiHome);
		return openEmpiHome;
	}

	private static ArrayList<String> generateConfigFileList() {
		ArrayList<String> configFiles = new ArrayList<String>();
		String openEmpiHome = getOpenEmpiHome();
		if (openEmpiHome != null && openEmpiHome.length() > 0) {
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-resources.xml");
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-dao.xml");
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-service.xml");
			configFiles.add("file:" + openEmpiHome + "/conf/applicationContext-resources-*.xml");
		} else {
			configFiles.add("classpath:/applicationContext-resources.xml");
			configFiles.add("classpath:/applicationContext-dao.xml");
			configFiles.add("classpath:/applicationContext-service.xml");
		}
		configFiles.add("classpath:/applicationContext-resources-*.xml");
		return configFiles;
	}

	private static void addExtensionContextsFromSystemProperty(ArrayList<String> configFiles) {
		String extensionContexts = System.getProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS);
		addExtensionContextsFromCommaSeparatedList(configFiles, extensionContexts);
	}

	private static void addExtensionContextsFromPropertiesFile(ArrayList<String> configFiles) {
		Properties props;
		try {
			String filename = getOpenEmpiHome() + "/conf/" + getExtensionsContextsPropertiesFilename();
			log.debug("Attempting to load extension contexts from " + filename);
			props = PropertiesUtils.load(new File(filename));
		} catch (Exception e) {
			log.warn("Unable to load the extension contexts properties file; will resort to System property. Error: " + e, e);
			return;
		}
		String extensionContexts = props.getProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS);
		addExtensionContextsFromCommaSeparatedList(configFiles, extensionContexts);
	}

	private static String getExtensionsContextsPropertiesFilename() {
		String filename = System.getProperty(Constants.OPENEMPI_EXTENSION_CONTEXTS_FILENAME);
		if (filename != null) {
			return filename;
		}
		return Constants.OPENEMPI_EXTENSION_CONTEXTS_PROPERTY_FILENAME;
	}

	private static void addExtensionContextsFromCommaSeparatedList(ArrayList<String> configFiles, String extensionContexts) {
		if (extensionContexts != null && extensionContexts.length() > 0) {
			String[] extContexts = extensionContexts.split(",");
			for (String extContext : extContexts) {
				log.debug("Adding extension application context from location: " + extContext);
				configFiles.add(extContext);
			}
		}
	}	

	private static void startNotificationService() {
		try {
			log.info("Starting the notification service...");
			notificationService.startup();
			log.info("Notification service was started successfuly.");
		} catch (Exception e) {
			log.error("Failed while trying to start the notification service. Error: " + e, e);
			throw new RuntimeException("Unable to start the notification service due to: " + e.getMessage());
		}
	}
	
	private static void startScheduledTasks() {
		try {
			log.info("Scheduling tasks...");
			@SuppressWarnings("unchecked")
			List<ScheduledTaskEntry> list = (List<ScheduledTaskEntry>)
					configuration.lookupConfigurationEntry(Configuration.SCHEDULED_TASK_LIST);
			if (list != null && list.size() > 0) {
				startScheduledTasks(list);
			}
			log.info("Tasks have been scheduled.");
		} catch (Exception e) {
			log.error("Failed while trying to schedule tasks. Error: " + e, e);
			throw new RuntimeException("Unable to schedule tasks: " + e.getMessage());
		}
	}

	private static void startScheduledTasks(List<ScheduledTaskEntry> list) {
		for (ScheduledTaskEntry task : list) {
			if (task.getScheduleType() == ScheduledTaskEntry.SCHEDULE_ENTRY_TYPE) {
				log.info("Scheduling the task: " + task);
				scheduler.schedule(task.getRunableTask(), task.getDelay(), task.getTimeUnit());
			} else if (task.getScheduleType() == ScheduledTaskEntry.SCHEDULE_AT_FIXED_RATE_ENTRY_TYPE) {
				log.info("Scheduling the task: " + task);
				scheduler.scheduleAtFixedRate(task.getRunableTask(), task.getInitialDelay(), task.getPeriod(), task.getTimeUnit());
			} else if (task.getScheduleType() == ScheduledTaskEntry.SCHEDULE_WITH_FIXED_DELAY_ENTRY_TYPE) {
				log.info("Scheduling the task: " + task);
				scheduler.scheduleWithFixedDelay(task.getRunableTask(), task.getInitialDelay(), task.getDelay(), task.getTimeUnit());
			}
		}
	}

	public static String authenticate(String username, String password)
			throws AuthenticationException {
		return getUserContext().authenticate(username, password);
	}
	
	public static User authenticate(String sessionKey)
			throws AuthenticationException {
		return getUserContext().authenticate(sessionKey);
	}

	public static UserContext getUserContext() {
		UserContext userContext = null;
		Object[] arr = userContextHolder.get();
		if (arr == null) {
			log.trace("userContext is null. Creating new userContext");
			userContext = new UserContext();
			userContext.setUserManager(userManager);
			setUserContext(userContext);
		}
		return (UserContext) userContextHolder.get()[0];
	}

	public static void setUserContext(UserContext ctx) {
		log.trace("Setting user context " + ctx);
		Object[] arr = new Object[] { ctx };
		userContextHolder.set(arr);
	}

	private static void startBlockingService() {
		Callable<Object> task = new ServiceStarterStopper("Starting the blocking service at startup.",
				ServiceStarterStopper.START_SERVICE, ServiceStarterStopper.BLOCKING_SERVICE);
		Future<Object> future = threadPool.submit(task);
		try {
			future.get();
		} catch (InterruptedException e) {
			log.error("Failed while starting up the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while starting the blocking service.");
		} catch (ExecutionException e) {
			log.error("Failed while starting up the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while starting the blocking service.");
		}
	}

	private static void stopBlockingService() {
		Callable<Object> task = new ServiceStarterStopper("Shutting down the blocking service before system shutdown.",
				ServiceStarterStopper.STOP_SERVICE, ServiceStarterStopper.BLOCKING_SERVICE);
		try {
			Future<Object> future = threadPool.submit(task);
			future.get();
		} catch (RejectedExecutionException e) {
			log.warn("Was unable to initiate a stop request on the matching service: " + e, e);			
		} catch (InterruptedException e) {
			log.error("Failed while shutting down the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while shutting down the blocking service.");
		} catch (ExecutionException e) {
			log.error("Failed while shutting up the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while shutting down the blocking service.");
		}		
	}

	public static Future<Object> scheduleTask(Callable<Object> task) {
		Future<Object> future = threadPool.submit(task);
//		try {
//			future.get();
//		} catch (InterruptedException e) {
//			log.error("Failed while scheduling a caller provided task: " + e, e);
//			throw new RuntimeException("Failed while scheduling a caller provided task.");
//		} catch (ExecutionException e) {
//			log.error("Failed while scheduling a caller provided task: " + e, e);
//			throw new RuntimeException("Failed while scheduling a caller provided task.");
//		}
		return future;
	}
	
	private static void startMatchingService() {
		Callable<Object> task = new ServiceStarterStopper("Starting the matching service at startup.",
				ServiceStarterStopper.START_SERVICE, ServiceStarterStopper.MATCHING_SERVICE);
		Future<Object> future = threadPool.submit(task);
		try {
			future.get();
		} catch (InterruptedException e) {
			log.error("Failed while starting up the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while starting the blocking service.");
		} catch (ExecutionException e) {
			log.error("Failed while starting up the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while starting the blocking service.");
		}		
	}

	private static void stopMatchingService() {
		Callable<Object> task = new ServiceStarterStopper("Shutting down the matching service before system shutdown.",
				ServiceStarterStopper.STOP_SERVICE, ServiceStarterStopper.MATCHING_SERVICE);
		try {
			Future<Object> future = threadPool.submit(task);
			future.get();
		} catch (RejectedExecutionException e) {
			log.warn("Was unable to initiate a stop request on the matching service: " + e, e);
		} catch (InterruptedException e) {
			log.error("Failed while shutting down the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while shutting down the blocking service.");
		} catch (ExecutionException e) {
			log.error("Failed while shutting down the blocking service: " + e, e);
			throw new RuntimeException("Initialization failed while shutting down the blocking service.");
		}		
	}

	public static PersonManagerService getPersonManagerService() {
		return personService;
	}
	
	public void setPersonManagerService(PersonManagerService personService) {
		Context.personService = personService;
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		Context.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	public static UserManager getUserManager() {
		return Context.userManager;
	}
	
	public void setUserManager(UserManager userManager) {
	    Context.userManager = userManager;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		Context.configuration = configuration;
	}

	public static ValidationService getValidationService() {
		return validationService;
	}

	public void setValidationService(ValidationService validationService) {
		Context.validationService = validationService;
	}

	public synchronized static MatchingService getMatchingService() {
		return matchingService;
	}

	public static void registerCustomMatchingService(MatchingService matchingService) {
		Context.matchingService = matchingService;
	}
	
	public void setMatchingService(MatchingService matchingService) {
		Context.matchingService = matchingService;
	}

	public static void registerCustomBlockingService(BlockingService blockingService) {
		Context.blockingService = blockingService;
	}
	
	public static BlockingService getBlockingService() {
		return blockingService;
	}

	public void setBlockingService(BlockingService blockingService) {
		Context.blockingService = blockingService;
	}
	
	public static SingleBestRecordService getSingleBestRecordService() {
		return singleBestRecordService;
	}

	public void setSingleBestRecordService(SingleBestRecordService singleBestRecordService) {
		Context.singleBestRecordService = singleBestRecordService;
	}

	public static void registerCustomSingleBestRecordService(SingleBestRecordService singleBestRecordService) {
		Context.singleBestRecordService = singleBestRecordService;
	}

	public static PersonQueryService getPersonQueryService() {
		return personQueryService;
	}

	public void setPersonQueryService(PersonQueryService personQueryService) {
		Context.personQueryService = personQueryService;
	}
	
	public static StringComparisonService getStringComparisonService() {
		return stringComparisonService;
	}
	
	public void setStringComparisonService(StringComparisonService stringComparisonService) {
		Context.stringComparisonService = stringComparisonService;
	}

	public static TransformationService getTransformationService() {
		return transformationService;
	}
	
	public void setTransformationService(TransformationService transformationService) {
		Context.transformationService = transformationService;
	}

	public static AuditEventService getAuditEventService() {
		return auditEventService;
	}

	public void setAuditEventService(AuditEventService auditEventService) {
		Context.auditEventService = auditEventService;
	}
	public static void registerCustomFileLoaderConfigurationService(FileLoaderConfigurationService fileLoaderConfigurationService) {
		Context.fileLoaderConfigurationService = fileLoaderConfigurationService;
	}
	
	public static FileLoaderConfigurationService getFileLoaderConfigurationService() {
		return fileLoaderConfigurationService;
	}

	public void setFileLoaderConfigurationService(FileLoaderConfigurationService fileLoaderConfigurationService) {
		Context.fileLoaderConfigurationService = fileLoaderConfigurationService;
	}

	public static NotificationService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(NotificationService notificationService) {
		Context.notificationService = notificationService;
	}
	
	public static ReportService getReportService() {
		return reportService;
	}

	public void setReportService(ReportService reportService) {
		Context.reportService = reportService;
	}
	
	public static DataProfileService getDataProfileService() {
		return dataProfileService;
	}

	public void setDataProfileService(DataProfileService dataProfileService) {
		Context.dataProfileService = dataProfileService;
	}
	
	public static ScheduledExecutorService getScheduler() {
		return scheduler;
	}
	
	public static enum ObservationEventType
	{
		ENTITY_ADD_EVENT,
		ENTITY_DELETE_EVENT,
		ENTITY_LINK_EVENT,
		ENTITY_UNLINK_EVENT,
		ENTITY_UPDATE_EVENT,
		MATCHING_CONFIGURATION_UPDATED_EVENT;
	}
	
	private static class ServiceStarterStopper implements Callable<Object>
	{
		private final static int START_SERVICE = 0;
		private final static int STOP_SERVICE = 1;
		private final static int BLOCKING_SERVICE = 2;
		private final static int MATCHING_SERVICE = 3;
		
		private String message;
		private int operation;
		private int serviceType;
		
		public ServiceStarterStopper(String message, int operation, int serviceType) {
			this.message = message;
			this.operation = operation;
			this.serviceType = serviceType;
		}
		
		public Object call() throws Exception {
			log.info(message);
			if (operation == START_SERVICE) {
				return startService();
			} else {
				return stopService();
			}
		}
		
		public Object startService() {
			if (serviceType == BLOCKING_SERVICE) {
				BlockingLifecycleObserver blockingService = (BlockingLifecycleObserver) Context.getBlockingService();
				if (blockingService != null) {
					blockingService.startup();
				}
				return blockingService;
			} else if (serviceType == MATCHING_SERVICE) {
				MatchingLifecycleObserver matchingService = (MatchingLifecycleObserver) Context.getMatchingService();
				if (matchingService != null) {
					matchingService.startup();
				}
				return matchingService;				
			} else {
				return null;
			}
		}
		
		public Object stopService() {
			if (serviceType == BLOCKING_SERVICE) {
				BlockingLifecycleObserver blockingService = (BlockingLifecycleObserver) Context.getBlockingService();
				if (blockingService != null) {
					blockingService.shutdown();
				}
				return blockingService;
			} else if (serviceType == MATCHING_SERVICE) {
				MatchingLifecycleObserver matchingService = (MatchingLifecycleObserver) Context.getMatchingService();
				if (matchingService != null) {
					matchingService.shutdown();
				}
				return matchingService;	
			} else {
				return null;
			}
		}
	}
}
