/*
 * XML Type:  log-by-vectors
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * An XML log-by-vectors(@http://configuration.openempi.openhie.org/probabilistic-matching).
 *
 * This is a complex type.
 */
public class LogByVectorsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors
{
    private static final long serialVersionUID = 1L;
    
    public LogByVectorsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VECTOR$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "vector");
    private static final javax.xml.namespace.QName SAMPLESIZEPERCENTAGE$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "sample-size-percentage");
    
    
    /**
     * Gets array of all "vector" elements
     */
    public int[] getVectorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VECTOR$0, targetList);
            int[] result = new int[targetList.size()];
            for (int i = 0, len = targetList.size() ; i < len ; i++)
                result[i] = ((org.apache.xmlbeans.SimpleValue)targetList.get(i)).getIntValue();
            return result;
        }
    }
    
    /**
     * Gets ith "vector" element
     */
    public int getVectorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) array of all "vector" elements
     */
    public org.apache.xmlbeans.XmlInt[] xgetVectorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(VECTOR$0, targetList);
            org.apache.xmlbeans.XmlInt[] result = new org.apache.xmlbeans.XmlInt[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets (as xml) ith "vector" element
     */
    public org.apache.xmlbeans.XmlInt xgetVectorArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(VECTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return (org.apache.xmlbeans.XmlInt)target;
        }
    }
    
    /**
     * Returns number of "vector" element
     */
    public int sizeOfVectorArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(VECTOR$0);
        }
    }
    
    /**
     * Sets array of all "vector" element
     */
    public void setVectorArray(int[] vectorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(vectorArray, VECTOR$0);
        }
    }
    
    /**
     * Sets ith "vector" element
     */
    public void setVectorArray(int i, int vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VECTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.setIntValue(vector);
        }
    }
    
    /**
     * Sets (as xml) array of all "vector" element
     */
    public void xsetVectorArray(org.apache.xmlbeans.XmlInt[]vectorArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(vectorArray, VECTOR$0);
        }
    }
    
    /**
     * Sets (as xml) ith "vector" element
     */
    public void xsetVectorArray(int i, org.apache.xmlbeans.XmlInt vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(VECTOR$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(vector);
        }
    }
    
    /**
     * Inserts the value as the ith "vector" element
     */
    public void insertVector(int i, int vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = 
                (org.apache.xmlbeans.SimpleValue)get_store().insert_element_user(VECTOR$0, i);
            target.setIntValue(vector);
        }
    }
    
    /**
     * Appends the value as the last "vector" element
     */
    public void addVector(int vector)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VECTOR$0);
            target.setIntValue(vector);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "vector" element
     */
    public org.apache.xmlbeans.XmlInt insertNewVector(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().insert_element_user(VECTOR$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "vector" element
     */
    public org.apache.xmlbeans.XmlInt addNewVector()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(VECTOR$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "vector" element
     */
    public void removeVector(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(VECTOR$0, i);
        }
    }
    
    /**
     * Gets the "sample-size-percentage" element
     */
    public double getSampleSizePercentage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SAMPLESIZEPERCENTAGE$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "sample-size-percentage" element
     */
    public org.apache.xmlbeans.XmlDouble xgetSampleSizePercentage()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SAMPLESIZEPERCENTAGE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "sample-size-percentage" element
     */
    public void setSampleSizePercentage(double sampleSizePercentage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SAMPLESIZEPERCENTAGE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SAMPLESIZEPERCENTAGE$2);
            }
            target.setDoubleValue(sampleSizePercentage);
        }
    }
    
    /**
     * Sets (as xml) the "sample-size-percentage" element
     */
    public void xsetSampleSizePercentage(org.apache.xmlbeans.XmlDouble sampleSizePercentage)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SAMPLESIZEPERCENTAGE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(SAMPLESIZEPERCENTAGE$2);
            }
            target.set(sampleSizePercentage);
        }
    }
}
