/*
 * XML Type:  file-loader-type
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.FileLoaderType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML file-loader-type(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class FileLoaderTypeImpl extends org.openhie.openempi.configuration.xml.impl.FileLoaderConfigurationTypeImpl implements org.openhie.openempi.configuration.xml.fileloader.FileLoaderType
{
    private static final long serialVersionUID = 1L;
    
    public FileLoaderTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILELOADERCONFIG$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "file-loader-config");
    
    
    /**
     * Gets the "file-loader-config" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig getFileLoaderConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig)get_store().find_element_user(FILELOADERCONFIG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "file-loader-config" element
     */
    public void setFileLoaderConfig(org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig fileLoaderConfig)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig)get_store().find_element_user(FILELOADERCONFIG$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig)get_store().add_element_user(FILELOADERCONFIG$0);
            }
            target.set(fileLoaderConfig);
        }
    }
    
    /**
     * Appends and returns a new empty "file-loader-config" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig addNewFileLoaderConfig()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderConfig)get_store().add_element_user(FILELOADERCONFIG$0);
            return target;
        }
    }
}
