/*
 * An XML document type.
 * Localname: single-best-record
 * Namespace: http://configuration.openempi.openhie.org/single-best-record
 * Java type: org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.singlebestrecord.impl;
/**
 * A document containing one single-best-record(@http://configuration.openempi.openhie.org/single-best-record) element.
 *
 * This is a complex type.
 */
public class SingleBestRecordDocumentImpl extends org.openhie.openempi.configuration.xml.impl.SingleBestRecordDocumentImpl implements org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordDocument
{
    private static final long serialVersionUID = 1L;
    
    public SingleBestRecordDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SINGLEBESTRECORD2$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "single-best-record");
    
    
    /**
     * Gets the "single-best-record" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType getSingleBestRecord2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType)get_store().find_element_user(SINGLEBESTRECORD2$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "single-best-record" element
     */
    public void setSingleBestRecord2(org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType singleBestRecord2)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType)get_store().find_element_user(SINGLEBESTRECORD2$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType)get_store().add_element_user(SINGLEBESTRECORD2$0);
            }
            target.set(singleBestRecord2);
        }
    }
    
    /**
     * Appends and returns a new empty "single-best-record" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType addNewSingleBestRecord2()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType)get_store().add_element_user(SINGLEBESTRECORD2$0);
            return target;
        }
    }
}
