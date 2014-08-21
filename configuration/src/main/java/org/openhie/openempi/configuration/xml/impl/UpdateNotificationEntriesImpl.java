/*
 * XML Type:  update-notification-entries
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.UpdateNotificationEntries
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML update-notification-entries(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class UpdateNotificationEntriesImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.UpdateNotificationEntries
{
    private static final long serialVersionUID = 1L;
    
    public UpdateNotificationEntriesImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName UPDATENOTIFICATIONENTRY$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "update-notification-entry");
    
    
    /**
     * Gets array of all "update-notification-entry" elements
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntry[] getUpdateNotificationEntryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(UPDATENOTIFICATIONENTRY$0, targetList);
            org.openhie.openempi.configuration.xml.UpdateNotificationEntry[] result = new org.openhie.openempi.configuration.xml.UpdateNotificationEntry[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "update-notification-entry" element
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntry getUpdateNotificationEntryArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntry target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntry)get_store().find_element_user(UPDATENOTIFICATIONENTRY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "update-notification-entry" element
     */
    public int sizeOfUpdateNotificationEntryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(UPDATENOTIFICATIONENTRY$0);
        }
    }
    
    /**
     * Sets array of all "update-notification-entry" element
     */
    public void setUpdateNotificationEntryArray(org.openhie.openempi.configuration.xml.UpdateNotificationEntry[] updateNotificationEntryArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(updateNotificationEntryArray, UPDATENOTIFICATIONENTRY$0);
        }
    }
    
    /**
     * Sets ith "update-notification-entry" element
     */
    public void setUpdateNotificationEntryArray(int i, org.openhie.openempi.configuration.xml.UpdateNotificationEntry updateNotificationEntry)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntry target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntry)get_store().find_element_user(UPDATENOTIFICATIONENTRY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(updateNotificationEntry);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "update-notification-entry" element
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntry insertNewUpdateNotificationEntry(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntry target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntry)get_store().insert_element_user(UPDATENOTIFICATIONENTRY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "update-notification-entry" element
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntry addNewUpdateNotificationEntry()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntry target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntry)get_store().add_element_user(UPDATENOTIFICATIONENTRY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "update-notification-entry" element
     */
    public void removeUpdateNotificationEntry(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(UPDATENOTIFICATIONENTRY$0, i);
        }
    }
}
