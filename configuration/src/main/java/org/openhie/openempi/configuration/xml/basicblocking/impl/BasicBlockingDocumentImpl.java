/*
 * An XML document type.
 * Localname: basic-blocking
 * Namespace: http://configuration.openempi.openhie.org/basic-blocking
 * Java type: org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.basicblocking.impl;
/**
 * A document containing one basic-blocking(@http://configuration.openempi.openhie.org/basic-blocking) element.
 *
 * This is a complex type.
 */
public class BasicBlockingDocumentImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationDocumentImpl implements org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingDocument
{
    private static final long serialVersionUID = 1L;
    
    public BasicBlockingDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BASICBLOCKING$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking", "basic-blocking");
    
    
    /**
     * Gets the "basic-blocking" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType getBasicBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType)get_store().find_element_user(BASICBLOCKING$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "basic-blocking" element
     */
    public void setBasicBlocking(org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType basicBlocking)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType)get_store().find_element_user(BASICBLOCKING$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType)get_store().add_element_user(BASICBLOCKING$0);
            }
            target.set(basicBlocking);
        }
    }
    
    /**
     * Appends and returns a new empty "basic-blocking" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType addNewBasicBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType)get_store().add_element_user(BASICBLOCKING$0);
            return target;
        }
    }
}
