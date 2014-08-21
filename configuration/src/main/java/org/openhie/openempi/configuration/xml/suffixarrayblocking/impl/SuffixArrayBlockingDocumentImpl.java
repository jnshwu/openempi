/*
 * An XML document type.
 * Localname: suffix-array-blocking
 * Namespace: http://configuration.openempi.openhie.org/suffix-array-blocking
 * Java type: org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.suffixarrayblocking.impl;
/**
 * A document containing one suffix-array-blocking(@http://configuration.openempi.openhie.org/suffix-array-blocking) element.
 *
 * This is a complex type.
 */
public class SuffixArrayBlockingDocumentImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationDocumentImpl implements org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingDocument
{
    private static final long serialVersionUID = 1L;
    
    public SuffixArrayBlockingDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SUFFIXARRAYBLOCKING$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "suffix-array-blocking");
    
    
    /**
     * Gets the "suffix-array-blocking" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType getSuffixArrayBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType)get_store().find_element_user(SUFFIXARRAYBLOCKING$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "suffix-array-blocking" element
     */
    public void setSuffixArrayBlocking(org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType suffixArrayBlocking)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType)get_store().find_element_user(SUFFIXARRAYBLOCKING$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType)get_store().add_element_user(SUFFIXARRAYBLOCKING$0);
            }
            target.set(suffixArrayBlocking);
        }
    }
    
    /**
     * Appends and returns a new empty "suffix-array-blocking" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType addNewSuffixArrayBlocking()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType)get_store().add_element_user(SUFFIXARRAYBLOCKING$0);
            return target;
        }
    }
}
