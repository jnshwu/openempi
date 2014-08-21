/*
 * XML Type:  basic-blocking-type
 * Namespace: http://configuration.openempi.openhie.org/basic-blocking
 * Java type: org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.basicblocking.impl;
/**
 * An XML basic-blocking-type(@http://configuration.openempi.openhie.org/basic-blocking).
 *
 * This is a complex type.
 */
public class BasicBlockingTypeImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationTypeImpl implements org.openhie.openempi.configuration.xml.basicblocking.BasicBlockingType
{
    private static final long serialVersionUID = 1L;
    
    public BasicBlockingTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BLOCKINGROUNDS$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking", "blocking-rounds");
    
    
    /**
     * Gets the "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds getBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "blocking-rounds" element
     */
    public void setBlockingRounds(org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds blockingRounds)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$0);
            }
            target.set(blockingRounds);
        }
    }
    
    /**
     * Appends and returns a new empty "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds addNewBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.basicblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$0);
            return target;
        }
    }
}
