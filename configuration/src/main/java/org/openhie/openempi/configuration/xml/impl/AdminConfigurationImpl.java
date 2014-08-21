/*
 * XML Type:  admin-configuration
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.AdminConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML admin-configuration(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class AdminConfigurationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.AdminConfiguration
{
    private static final long serialVersionUID = 1L;
    
    public AdminConfigurationImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILEREPOSITORYDIRECTORY$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "file-repository-directory");
    private static final javax.xml.namespace.QName AUTOSTARTPIXPDQ$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "autostart-pixpdq");
    private static final javax.xml.namespace.QName UPDATENOTIFICATIONENTRIES$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "update-notification-entries");
    
    
    /**
     * Gets the "file-repository-directory" element
     */
    public java.lang.String getFileRepositoryDirectory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FILEREPOSITORYDIRECTORY$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "file-repository-directory" element
     */
    public org.apache.xmlbeans.XmlString xgetFileRepositoryDirectory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FILEREPOSITORYDIRECTORY$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "file-repository-directory" element
     */
    public void setFileRepositoryDirectory(java.lang.String fileRepositoryDirectory)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FILEREPOSITORYDIRECTORY$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FILEREPOSITORYDIRECTORY$0);
            }
            target.setStringValue(fileRepositoryDirectory);
        }
    }
    
    /**
     * Sets (as xml) the "file-repository-directory" element
     */
    public void xsetFileRepositoryDirectory(org.apache.xmlbeans.XmlString fileRepositoryDirectory)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FILEREPOSITORYDIRECTORY$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FILEREPOSITORYDIRECTORY$0);
            }
            target.set(fileRepositoryDirectory);
        }
    }
    
    /**
     * Gets the "autostart-pixpdq" element
     */
    public boolean getAutostartPixpdq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AUTOSTARTPIXPDQ$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "autostart-pixpdq" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetAutostartPixpdq()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(AUTOSTARTPIXPDQ$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "autostart-pixpdq" element
     */
    public void setAutostartPixpdq(boolean autostartPixpdq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(AUTOSTARTPIXPDQ$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(AUTOSTARTPIXPDQ$2);
            }
            target.setBooleanValue(autostartPixpdq);
        }
    }
    
    /**
     * Sets (as xml) the "autostart-pixpdq" element
     */
    public void xsetAutostartPixpdq(org.apache.xmlbeans.XmlBoolean autostartPixpdq)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(AUTOSTARTPIXPDQ$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(AUTOSTARTPIXPDQ$2);
            }
            target.set(autostartPixpdq);
        }
    }
    
    /**
     * Gets the "update-notification-entries" element
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntries getUpdateNotificationEntries()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntries target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntries)get_store().find_element_user(UPDATENOTIFICATIONENTRIES$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "update-notification-entries" element
     */
    public boolean isSetUpdateNotificationEntries()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(UPDATENOTIFICATIONENTRIES$4) != 0;
        }
    }
    
    /**
     * Sets the "update-notification-entries" element
     */
    public void setUpdateNotificationEntries(org.openhie.openempi.configuration.xml.UpdateNotificationEntries updateNotificationEntries)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntries target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntries)get_store().find_element_user(UPDATENOTIFICATIONENTRIES$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntries)get_store().add_element_user(UPDATENOTIFICATIONENTRIES$4);
            }
            target.set(updateNotificationEntries);
        }
    }
    
    /**
     * Appends and returns a new empty "update-notification-entries" element
     */
    public org.openhie.openempi.configuration.xml.UpdateNotificationEntries addNewUpdateNotificationEntries()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.UpdateNotificationEntries target = null;
            target = (org.openhie.openempi.configuration.xml.UpdateNotificationEntries)get_store().add_element_user(UPDATENOTIFICATIONENTRIES$4);
            return target;
        }
    }
    
    /**
     * Unsets the "update-notification-entries" element
     */
    public void unsetUpdateNotificationEntries()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(UPDATENOTIFICATIONENTRIES$4, 0);
        }
    }
}
