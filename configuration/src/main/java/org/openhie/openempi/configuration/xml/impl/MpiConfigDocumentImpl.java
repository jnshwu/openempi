/*
 * An XML document type.
 * Localname: mpi-config
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.MpiConfigDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * A document containing one mpi-config(@http://configuration.openempi.openhie.org/mpiconfig) element.
 *
 * This is a complex type.
 */
public class MpiConfigDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.MpiConfigDocument
{
    private static final long serialVersionUID = 1L;
    
    public MpiConfigDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MPICONFIG$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "mpi-config");
    
    
    /**
     * Gets the "mpi-config" element
     */
    public org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig getMpiConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig target = null;
            target = (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig)get_store().find_element_user(MPICONFIG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "mpi-config" element
     */
    public void setMpiConfig(org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig mpiConfig)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig target = null;
            target = (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig)get_store().find_element_user(MPICONFIG$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig)get_store().add_element_user(MPICONFIG$0);
            }
            target.set(mpiConfig);
        }
    }
    
    /**
     * Appends and returns a new empty "mpi-config" element
     */
    public org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig addNewMpiConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig target = null;
            target = (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig)get_store().add_element_user(MPICONFIG$0);
            return target;
        }
    }
    /**
     * An XML mpi-config(@http://configuration.openempi.openhie.org/mpiconfig).
     *
     * This is a complex type.
     */
    public static class MpiConfigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig
    {
        private static final long serialVersionUID = 1L;
        
        public MpiConfigImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName GLOBALIDENTIFIER$0 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "global-identifier");
        private static final javax.xml.namespace.QName SCHEDULEDTASKS$2 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "scheduled-tasks");
        private static final javax.xml.namespace.QName FILELOADERCONFIGURATION$4 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "file-loader-configuration");
        private static final org.apache.xmlbeans.QNameSet FILELOADERCONFIGURATION$5 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "file-loader"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "file-loader-configuration"),
        });
        private static final javax.xml.namespace.QName CUSTOMFIELDS$6 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "custom-fields");
        private static final javax.xml.namespace.QName BLOCKINGCONFIGURATION$8 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "blocking-configuration");
        private static final org.apache.xmlbeans.QNameSet BLOCKINGCONFIGURATION$9 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "blocking-configuration"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "suffix-array-blocking"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking-hp", "basic-blocking"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking", "basic-blocking"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/sorted-neighborhood-blocking", "sorted-neighborhood-blocking"),
        });
        private static final javax.xml.namespace.QName MATCHINGCONFIGURATION$10 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "matching-configuration");
        private static final org.apache.xmlbeans.QNameSet MATCHINGCONFIGURATION$11 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "probabilistic-matching"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/exact-matching", "exact-matching"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "matching-configuration"),
        });
        private static final javax.xml.namespace.QName SINGLEBESTRECORD$12 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "single-best-record");
        private static final org.apache.xmlbeans.QNameSet SINGLEBESTRECORD$13 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "single-best-record"),
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "single-best-record"),
        });
        private static final javax.xml.namespace.QName ADMINCONFIGURATION$14 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "admin-configuration");
        
        
        /**
         * Gets the "global-identifier" element
         */
        public org.openhie.openempi.configuration.xml.GlobalIdentifier getGlobalIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.GlobalIdentifier target = null;
                target = (org.openhie.openempi.configuration.xml.GlobalIdentifier)get_store().find_element_user(GLOBALIDENTIFIER$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "global-identifier" element
         */
        public boolean isSetGlobalIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(GLOBALIDENTIFIER$0) != 0;
            }
        }
        
        /**
         * Sets the "global-identifier" element
         */
        public void setGlobalIdentifier(org.openhie.openempi.configuration.xml.GlobalIdentifier globalIdentifier)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.GlobalIdentifier target = null;
                target = (org.openhie.openempi.configuration.xml.GlobalIdentifier)get_store().find_element_user(GLOBALIDENTIFIER$0, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.GlobalIdentifier)get_store().add_element_user(GLOBALIDENTIFIER$0);
                }
                target.set(globalIdentifier);
            }
        }
        
        /**
         * Appends and returns a new empty "global-identifier" element
         */
        public org.openhie.openempi.configuration.xml.GlobalIdentifier addNewGlobalIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.GlobalIdentifier target = null;
                target = (org.openhie.openempi.configuration.xml.GlobalIdentifier)get_store().add_element_user(GLOBALIDENTIFIER$0);
                return target;
            }
        }
        
        /**
         * Unsets the "global-identifier" element
         */
        public void unsetGlobalIdentifier()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(GLOBALIDENTIFIER$0, 0);
            }
        }
        
        /**
         * Gets the "scheduled-tasks" element
         */
        public org.openhie.openempi.configuration.xml.ScheduledTasks getScheduledTasks()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.ScheduledTasks target = null;
                target = (org.openhie.openempi.configuration.xml.ScheduledTasks)get_store().find_element_user(SCHEDULEDTASKS$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "scheduled-tasks" element
         */
        public boolean isSetScheduledTasks()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SCHEDULEDTASKS$2) != 0;
            }
        }
        
        /**
         * Sets the "scheduled-tasks" element
         */
        public void setScheduledTasks(org.openhie.openempi.configuration.xml.ScheduledTasks scheduledTasks)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.ScheduledTasks target = null;
                target = (org.openhie.openempi.configuration.xml.ScheduledTasks)get_store().find_element_user(SCHEDULEDTASKS$2, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.ScheduledTasks)get_store().add_element_user(SCHEDULEDTASKS$2);
                }
                target.set(scheduledTasks);
            }
        }
        
        /**
         * Appends and returns a new empty "scheduled-tasks" element
         */
        public org.openhie.openempi.configuration.xml.ScheduledTasks addNewScheduledTasks()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.ScheduledTasks target = null;
                target = (org.openhie.openempi.configuration.xml.ScheduledTasks)get_store().add_element_user(SCHEDULEDTASKS$2);
                return target;
            }
        }
        
        /**
         * Unsets the "scheduled-tasks" element
         */
        public void unsetScheduledTasks()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SCHEDULEDTASKS$2, 0);
            }
        }
        
        /**
         * Gets the "file-loader-configuration" element
         */
        public org.openhie.openempi.configuration.xml.FileLoaderConfigurationType getFileLoaderConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.FileLoaderConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().find_element_user(FILELOADERCONFIGURATION$5, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "file-loader-configuration" element
         */
        public void setFileLoaderConfiguration(org.openhie.openempi.configuration.xml.FileLoaderConfigurationType fileLoaderConfiguration)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.FileLoaderConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().find_element_user(FILELOADERCONFIGURATION$5, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().add_element_user(FILELOADERCONFIGURATION$4);
                }
                target.set(fileLoaderConfiguration);
            }
        }
        
        /**
         * Appends and returns a new empty "file-loader-configuration" element
         */
        public org.openhie.openempi.configuration.xml.FileLoaderConfigurationType addNewFileLoaderConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.FileLoaderConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().add_element_user(FILELOADERCONFIGURATION$4);
                return target;
            }
        }
        
        /**
         * Gets the "custom-fields" element
         */
        public org.openhie.openempi.configuration.xml.CustomFields getCustomFields()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.CustomFields target = null;
                target = (org.openhie.openempi.configuration.xml.CustomFields)get_store().find_element_user(CUSTOMFIELDS$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "custom-fields" element
         */
        public boolean isSetCustomFields()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(CUSTOMFIELDS$6) != 0;
            }
        }
        
        /**
         * Sets the "custom-fields" element
         */
        public void setCustomFields(org.openhie.openempi.configuration.xml.CustomFields customFields)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.CustomFields target = null;
                target = (org.openhie.openempi.configuration.xml.CustomFields)get_store().find_element_user(CUSTOMFIELDS$6, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.CustomFields)get_store().add_element_user(CUSTOMFIELDS$6);
                }
                target.set(customFields);
            }
        }
        
        /**
         * Appends and returns a new empty "custom-fields" element
         */
        public org.openhie.openempi.configuration.xml.CustomFields addNewCustomFields()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.CustomFields target = null;
                target = (org.openhie.openempi.configuration.xml.CustomFields)get_store().add_element_user(CUSTOMFIELDS$6);
                return target;
            }
        }
        
        /**
         * Unsets the "custom-fields" element
         */
        public void unsetCustomFields()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(CUSTOMFIELDS$6, 0);
            }
        }
        
        /**
         * Gets the "blocking-configuration" element
         */
        public org.openhie.openempi.configuration.xml.BlockingConfigurationType getBlockingConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().find_element_user(BLOCKINGCONFIGURATION$9, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "blocking-configuration" element
         */
        public void setBlockingConfiguration(org.openhie.openempi.configuration.xml.BlockingConfigurationType blockingConfiguration)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().find_element_user(BLOCKINGCONFIGURATION$9, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().add_element_user(BLOCKINGCONFIGURATION$8);
                }
                target.set(blockingConfiguration);
            }
        }
        
        /**
         * Appends and returns a new empty "blocking-configuration" element
         */
        public org.openhie.openempi.configuration.xml.BlockingConfigurationType addNewBlockingConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().add_element_user(BLOCKINGCONFIGURATION$8);
                return target;
            }
        }
        
        /**
         * Gets the "matching-configuration" element
         */
        public org.openhie.openempi.configuration.xml.MatchingConfigurationType getMatchingConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.MatchingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.MatchingConfigurationType)get_store().find_element_user(MATCHINGCONFIGURATION$11, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "matching-configuration" element
         */
        public void setMatchingConfiguration(org.openhie.openempi.configuration.xml.MatchingConfigurationType matchingConfiguration)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.MatchingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.MatchingConfigurationType)get_store().find_element_user(MATCHINGCONFIGURATION$11, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.MatchingConfigurationType)get_store().add_element_user(MATCHINGCONFIGURATION$10);
                }
                target.set(matchingConfiguration);
            }
        }
        
        /**
         * Appends and returns a new empty "matching-configuration" element
         */
        public org.openhie.openempi.configuration.xml.MatchingConfigurationType addNewMatchingConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.MatchingConfigurationType target = null;
                target = (org.openhie.openempi.configuration.xml.MatchingConfigurationType)get_store().add_element_user(MATCHINGCONFIGURATION$10);
                return target;
            }
        }
        
        /**
         * Gets the "single-best-record" element
         */
        public org.openhie.openempi.configuration.xml.SingleBestRecordType getSingleBestRecord()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.SingleBestRecordType target = null;
                target = (org.openhie.openempi.configuration.xml.SingleBestRecordType)get_store().find_element_user(SINGLEBESTRECORD$13, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * True if has "single-best-record" element
         */
        public boolean isSetSingleBestRecord()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(SINGLEBESTRECORD$13) != 0;
            }
        }
        
        /**
         * Sets the "single-best-record" element
         */
        public void setSingleBestRecord(org.openhie.openempi.configuration.xml.SingleBestRecordType singleBestRecord)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.SingleBestRecordType target = null;
                target = (org.openhie.openempi.configuration.xml.SingleBestRecordType)get_store().find_element_user(SINGLEBESTRECORD$13, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.SingleBestRecordType)get_store().add_element_user(SINGLEBESTRECORD$12);
                }
                target.set(singleBestRecord);
            }
        }
        
        /**
         * Appends and returns a new empty "single-best-record" element
         */
        public org.openhie.openempi.configuration.xml.SingleBestRecordType addNewSingleBestRecord()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.SingleBestRecordType target = null;
                target = (org.openhie.openempi.configuration.xml.SingleBestRecordType)get_store().add_element_user(SINGLEBESTRECORD$12);
                return target;
            }
        }
        
        /**
         * Unsets the "single-best-record" element
         */
        public void unsetSingleBestRecord()
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(SINGLEBESTRECORD$13, 0);
            }
        }
        
        /**
         * Gets the "admin-configuration" element
         */
        public org.openhie.openempi.configuration.xml.AdminConfiguration getAdminConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.AdminConfiguration target = null;
                target = (org.openhie.openempi.configuration.xml.AdminConfiguration)get_store().find_element_user(ADMINCONFIGURATION$14, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "admin-configuration" element
         */
        public void setAdminConfiguration(org.openhie.openempi.configuration.xml.AdminConfiguration adminConfiguration)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.AdminConfiguration target = null;
                target = (org.openhie.openempi.configuration.xml.AdminConfiguration)get_store().find_element_user(ADMINCONFIGURATION$14, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.AdminConfiguration)get_store().add_element_user(ADMINCONFIGURATION$14);
                }
                target.set(adminConfiguration);
            }
        }
        
        /**
         * Appends and returns a new empty "admin-configuration" element
         */
        public org.openhie.openempi.configuration.xml.AdminConfiguration addNewAdminConfiguration()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.AdminConfiguration target = null;
                target = (org.openhie.openempi.configuration.xml.AdminConfiguration)get_store().add_element_user(ADMINCONFIGURATION$14);
                return target;
            }
        }
    }
}
