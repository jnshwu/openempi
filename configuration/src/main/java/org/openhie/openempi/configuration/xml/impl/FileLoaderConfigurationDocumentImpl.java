/*
 * An XML document type.
 * Localname: file-loader-configuration
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.FileLoaderConfigurationDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * A document containing one file-loader-configuration(@http://configuration.openempi.openhie.org/mpiconfig) element.
 *
 * This is a complex type.
 */
public class FileLoaderConfigurationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.FileLoaderConfigurationDocument
{
    private static final long serialVersionUID = 1L;
    
    public FileLoaderConfigurationDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILELOADERCONFIGURATION$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "file-loader-configuration");
    private static final org.apache.xmlbeans.QNameSet FILELOADERCONFIGURATION$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "file-loader"),
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "file-loader-configuration"),
    });
    
    
    /**
     * Gets the "file-loader-configuration" element
     */
    public org.openhie.openempi.configuration.xml.FileLoaderConfigurationType getFileLoaderConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.FileLoaderConfigurationType target = null;
            target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().find_element_user(FILELOADERCONFIGURATION$1, 0);
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
            target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().find_element_user(FILELOADERCONFIGURATION$1, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().add_element_user(FILELOADERCONFIGURATION$0);
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
            target = (org.openhie.openempi.configuration.xml.FileLoaderConfigurationType)get_store().add_element_user(FILELOADERCONFIGURATION$0);
            return target;
        }
    }
}
