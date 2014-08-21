/*
 * XML Type:  vector-classification
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * An XML vector-classification(@http://configuration.openempi.openhie.org/probabilistic-matching).
 *
 * This is a complex type.
 */
public class VectorClassificationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification
{
    private static final long serialVersionUID = 1L;
    
    public VectorClassificationImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VECTOR$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "vector");
    private static final javax.xml.namespace.QName CLASSIFICATION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "classification");
    
    
    /**
     * Gets the "vector" element
     */
    public java.math.BigInteger getVector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "vector" element
     */
    public org.apache.xmlbeans.XmlNonNegativeInteger xgetVector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().find_element_user(VECTOR$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "vector" element
     */
    public void setVector(java.math.BigInteger vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VECTOR$0);
            }
            target.setBigIntegerValue(vector);
        }
    }
    
    /**
     * Sets (as xml) the "vector" element
     */
    public void xsetVector(org.apache.xmlbeans.XmlNonNegativeInteger vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlNonNegativeInteger target = null;
            target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().find_element_user(VECTOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlNonNegativeInteger)get_store().add_element_user(VECTOR$0);
            }
            target.set(vector);
        }
    }
    
    /**
     * Gets the "classification" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.Classification.Enum getClassification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLASSIFICATION$2, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.probabilisticmatching.Classification.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "classification" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.Classification xgetClassification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.Classification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.Classification)get_store().find_element_user(CLASSIFICATION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "classification" element
     */
    public void setClassification(org.openhie.openempi.configuration.xml.probabilisticmatching.Classification.Enum classification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CLASSIFICATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CLASSIFICATION$2);
            }
            target.setEnumValue(classification);
        }
    }
    
    /**
     * Sets (as xml) the "classification" element
     */
    public void xsetClassification(org.openhie.openempi.configuration.xml.probabilisticmatching.Classification classification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.Classification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.Classification)get_store().find_element_user(CLASSIFICATION$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.probabilisticmatching.Classification)get_store().add_element_user(CLASSIFICATION$2);
            }
            target.set(classification);
        }
    }
}
