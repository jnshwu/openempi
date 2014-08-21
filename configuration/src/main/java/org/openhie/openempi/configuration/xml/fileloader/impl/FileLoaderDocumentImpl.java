/*
 * An XML document type.
 * Localname: file-loader
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.FileLoaderDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * A document containing one file-loader(@http://configuration.openempi.openhie.org/file-loader) element.
 *
 * This is a complex type.
 */
public class FileLoaderDocumentImpl extends org.openhie.openempi.configuration.xml.impl.FileLoaderConfigurationDocumentImpl implements org.openhie.openempi.configuration.xml.fileloader.FileLoaderDocument
{
    private static final long serialVersionUID = 1L;
    
    public FileLoaderDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FILELOADER$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "file-loader");
    
    
    /**
     * Gets the "file-loader" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.FileLoaderType getFileLoader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderType target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderType)get_store().find_element_user(FILELOADER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "file-loader" element
     */
    public void setFileLoader(org.openhie.openempi.configuration.xml.fileloader.FileLoaderType fileLoader)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderType target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderType)get_store().find_element_user(FILELOADER$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderType)get_store().add_element_user(FILELOADER$0);
            }
            target.set(fileLoader);
        }
    }
    
    /**
     * Appends and returns a new empty "file-loader" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.FileLoaderType addNewFileLoader()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.FileLoaderType target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.FileLoaderType)get_store().add_element_user(FILELOADER$0);
            return target;
        }
    }
}
