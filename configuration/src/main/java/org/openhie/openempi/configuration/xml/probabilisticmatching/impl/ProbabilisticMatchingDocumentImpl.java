/*
 * An XML document type.
 * Localname: probabilistic-matching
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * A document containing one probabilistic-matching(@http://configuration.openempi.openhie.org/probabilistic-matching) element.
 *
 * This is a complex type.
 */
public class ProbabilisticMatchingDocumentImpl extends org.openhie.openempi.configuration.xml.impl.MatchingConfigurationDocumentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingDocument
{
    private static final long serialVersionUID = 1L;
    
    public ProbabilisticMatchingDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName PROBABILISTICMATCHING$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "probabilistic-matching");
    
    
    /**
     * Gets the "probabilistic-matching" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType getProbabilisticMatching()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType)get_store().find_element_user(PROBABILISTICMATCHING$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "probabilistic-matching" element
     */
    public void setProbabilisticMatching(org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType probabilisticMatching)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType)get_store().find_element_user(PROBABILISTICMATCHING$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType)get_store().add_element_user(PROBABILISTICMATCHING$0);
            }
            target.set(probabilisticMatching);
        }
    }
    
    /**
     * Appends and returns a new empty "probabilistic-matching" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType addNewProbabilisticMatching()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.ProbabilisticMatchingType)get_store().add_element_user(PROBABILISTICMATCHING$0);
            return target;
        }
    }
}
