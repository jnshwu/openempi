/*
 * XML Type:  blocking-round
 * Namespace: http://configuration.openempi.openhie.org/basic-blocking
 * Java type: org.openhie.openempi.configuration.xml.basicblocking.BlockingRound
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.basicblocking.impl;
/**
 * An XML blocking-round(@http://configuration.openempi.openhie.org/basic-blocking).
 *
 * This is a complex type.
 */
public class BlockingRoundImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.basicblocking.BlockingRound
{
    private static final long serialVersionUID = 1L;
    
    public BlockingRoundImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BLOCKINGFIELDS$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking", "blocking-fields");
    
    
    /**
     * Gets the "blocking-fields" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BlockingFields getBlockingFields()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingFields target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingFields)get_store().find_element_user(BLOCKINGFIELDS$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "blocking-fields" element
     */
    public void setBlockingFields(org.openhie.openempi.configuration.xml.basicblocking.BlockingFields blockingFields)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingFields target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingFields)get_store().find_element_user(BLOCKINGFIELDS$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingFields)get_store().add_element_user(BLOCKINGFIELDS$0);
            }
            target.set(blockingFields);
        }
    }
    
    /**
     * Appends and returns a new empty "blocking-fields" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BlockingFields addNewBlockingFields()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingFields target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingFields)get_store().add_element_user(BLOCKINGFIELDS$0);
            return target;
        }
    }
}
