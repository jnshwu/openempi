/*
 * An XML document type.
 * Localname: sorted-neighborhood-blocking
 * Namespace: http://configuration.openempi.openhie.org/sorted-neighborhood-blocking
 * Java type: org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.impl;
/**
 * A document containing one sorted-neighborhood-blocking(@http://configuration.openempi.openhie.org/sorted-neighborhood-blocking) element.
 *
 * This is a complex type.
 */
public class SortedNeighborhoodBlockingDocumentImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationDocumentImpl implements org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingDocument
{
    private static final long serialVersionUID = 1L;
    
    public SortedNeighborhoodBlockingDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SORTEDNEIGHBORHOODBLOCKING$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/sorted-neighborhood-blocking", "sorted-neighborhood-blocking");
    
    
    /**
     * Gets the "sorted-neighborhood-blocking" element
     */
    public org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType getSortedNeighborhoodBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType)get_store().find_element_user(SORTEDNEIGHBORHOODBLOCKING$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sorted-neighborhood-blocking" element
     */
    public void setSortedNeighborhoodBlocking(org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType sortedNeighborhoodBlocking)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType)get_store().find_element_user(SORTEDNEIGHBORHOODBLOCKING$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType)get_store().add_element_user(SORTEDNEIGHBORHOODBLOCKING$0);
            }
            target.set(sortedNeighborhoodBlocking);
        }
    }
    
    /**
     * Appends and returns a new empty "sorted-neighborhood-blocking" element
     */
    public org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType addNewSortedNeighborhoodBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.sortedneighborhoodblocking.SortedNeighborhoodBlockingType)get_store().add_element_user(SORTEDNEIGHBORHOODBLOCKING$0);
            return target;
        }
    }
}
