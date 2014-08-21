/*
 * XML Type:  file-loader-config
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML file-loader-config(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class FileLoaderConfigImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig
{
    private static final long serialVersionUID = 1L;
    
    public FileLoaderConfigImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName HEADERLINEPRESENT$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "header-line-present");
    private static final javax.xml.namespace.QName DATAFIELDS$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "data-fields");
    
    
    /**
     * Gets the "header-line-present" element
     */
    public boolean getHeaderLinePresent()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(HEADERLINEPRESENT$0, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "header-line-present" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetHeaderLinePresent()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(HEADERLINEPRESENT$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "header-line-present" element
     */
    public void setHeaderLinePresent(boolean headerLinePresent)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(HEADERLINEPRESENT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(HEADERLINEPRESENT$0);
            }
            target.setBooleanValue(headerLinePresent);
        }
    }
    
    /**
     * Sets (as xml) the "header-line-present" element
     */
    public void xsetHeaderLinePresent(org.apache.xmlbeans.XmlBoolean headerLinePresent)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(HEADERLINEPRESENT$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(HEADERLINEPRESENT$0);
            }
            target.set(headerLinePresent);
        }
    }
    
    /**
     * Gets the "data-fields" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataFields getDataFields()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataFields target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataFields)get_store().find_element_user(DATAFIELDS$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "data-fields" element
     */
    public void setDataFields(org.openhie.openempi.configuration.xml.fileloader.DataFields dataFields)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataFields target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataFields)get_store().find_element_user(DATAFIELDS$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.fileloader.DataFields)get_store().add_element_user(DATAFIELDS$2);
            }
            target.set(dataFields);
        }
    }
    
    /**
     * Appends and returns a new empty "data-fields" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataFields addNewDataFields()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataFields target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataFields)get_store().add_element_user(DATAFIELDS$2);
            return target;
        }
    }
}
