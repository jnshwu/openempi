/*
 * XML Type:  sorted-neighborhood-blocking-type
 * Namespace: http://configuration.openempi.openhie.org/sorted-neighborhood-blocking
 * Java type: org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.impl;
/**
 * An XML sorted-neighborhood-blocking-type(@http://configuration.openempi.openhie.org/sorted-neighborhood-blocking).
 *
 * This is a complex type.
 */
public class SortedNeighborhoodBlockingTypeImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationTypeImpl implements org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType
{
    private static final long serialVersionUID = 1L;
    
    public SortedNeighborhoodBlockingTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WINDOWSIZE$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/sorted-neighborhood-blocking", "window-size");
    private static final javax.xml.namespace.QName BLOCKINGROUNDS$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/sorted-neighborhood-blocking", "blocking-rounds");
    
    
    /**
     * Gets the "window-size" element
     */
    public int getWindowSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WINDOWSIZE$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "window-size" element
     */
    public org.apache.xmlbeans.XmlInt xgetWindowSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(WINDOWSIZE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "window-size" element
     */
    public void setWindowSize(int windowSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WINDOWSIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WINDOWSIZE$0);
            }
            target.setIntValue(windowSize);
        }
    }
    
    /**
     * Sets (as xml) the "window-size" element
     */
    public void xsetWindowSize(org.apache.xmlbeans.XmlInt windowSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(WINDOWSIZE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(WINDOWSIZE$0);
            }
            target.set(windowSize);
        }
    }
    
    /**
     * Gets the "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds getBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$2, 0);
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
    public void setBlockingRounds(org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds blockingRounds)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$2);
            }
            target.set(blockingRounds);
        }
    }
    
    /**
     * Appends and returns a new empty "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds addNewBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$2);
            return target;
        }
    }
}
