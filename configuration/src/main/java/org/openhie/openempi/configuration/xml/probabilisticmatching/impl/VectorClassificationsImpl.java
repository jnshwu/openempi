/*
 * XML Type:  vector-classifications
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassifications
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * An XML vector-classifications(@http://configuration.openempi.openhie.org/probabilistic-matching).
 *
 * This is a complex type.
 */
public class VectorClassificationsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassifications
{
    private static final long serialVersionUID = 1L;
    
    public VectorClassificationsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VECTORCLASSIFICATION$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "vector-classification");
    
    
    /**
     * Gets array of all "vector-classification" elements
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification[] getVectorClassificationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VECTORCLASSIFICATION$0, targetList);
            org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification[] result = new org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "vector-classification" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification getVectorClassificationArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification)get_store().find_element_user(VECTORCLASSIFICATION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "vector-classification" element
     */
    public int sizeOfVectorClassificationArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VECTORCLASSIFICATION$0);
        }
    }
    
    /**
     * Sets array of all "vector-classification" element
     */
    public void setVectorClassificationArray(org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification[] vectorClassificationArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(vectorClassificationArray, VECTORCLASSIFICATION$0);
        }
    }
    
    /**
     * Sets ith "vector-classification" element
     */
    public void setVectorClassificationArray(int i, org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification vectorClassification)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification)get_store().find_element_user(VECTORCLASSIFICATION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(vectorClassification);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "vector-classification" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification insertNewVectorClassification(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification)get_store().insert_element_user(VECTORCLASSIFICATION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "vector-classification" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification addNewVectorClassification()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.VectorClassification)get_store().add_element_user(VECTORCLASSIFICATION$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "vector-classification" element
     */
    public void removeVectorClassification(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VECTORCLASSIFICATION$0, i);
        }
    }
}
