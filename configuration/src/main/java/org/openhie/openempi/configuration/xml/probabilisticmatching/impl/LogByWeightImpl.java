/*
 * XML Type:  log-by-weight
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * An XML log-by-weight(@http://configuration.openempi.openhie.org/probabilistic-matching).
 *
 * This is a complex type.
 */
public class LogByWeightImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight
{
    private static final long serialVersionUID = 1L;
    
    public LogByWeightImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName WEIGHTLOWERBOUND$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "weight-lower-bound");
    private static final javax.xml.namespace.QName WEIGHTUPPERBOUND$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "weight-upper-bound");
    private static final javax.xml.namespace.QName SAMPLESIZEPERCENTAGE$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "sample-size-percentage");
    
    
    /**
     * Gets the "weight-lower-bound" element
     */
    public double getWeightLowerBound()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WEIGHTLOWERBOUND$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "weight-lower-bound" element
     */
    public org.apache.xmlbeans.XmlDouble xgetWeightLowerBound()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(WEIGHTLOWERBOUND$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "weight-lower-bound" element
     */
    public void setWeightLowerBound(double weightLowerBound)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WEIGHTLOWERBOUND$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WEIGHTLOWERBOUND$0);
            }
            target.setDoubleValue(weightLowerBound);
        }
    }
    
    /**
     * Sets (as xml) the "weight-lower-bound" element
     */
    public void xsetWeightLowerBound(org.apache.xmlbeans.XmlDouble weightLowerBound)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(WEIGHTLOWERBOUND$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(WEIGHTLOWERBOUND$0);
            }
            target.set(weightLowerBound);
        }
    }
    
    /**
     * Gets the "weight-upper-bound" element
     */
    public double getWeightUpperBound()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WEIGHTUPPERBOUND$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "weight-upper-bound" element
     */
    public org.apache.xmlbeans.XmlDouble xgetWeightUpperBound()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(WEIGHTUPPERBOUND$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "weight-upper-bound" element
     */
    public void setWeightUpperBound(double weightUpperBound)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(WEIGHTUPPERBOUND$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(WEIGHTUPPERBOUND$2);
            }
            target.setDoubleValue(weightUpperBound);
        }
    }
    
    /**
     * Sets (as xml) the "weight-upper-bound" element
     */
    public void xsetWeightUpperBound(org.apache.xmlbeans.XmlDouble weightUpperBound)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(WEIGHTUPPERBOUND$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(WEIGHTUPPERBOUND$2);
            }
            target.set(weightUpperBound);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SAMPLESIZEPERCENTAGE$4, 0);
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
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SAMPLESIZEPERCENTAGE$4, 0);
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
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SAMPLESIZEPERCENTAGE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SAMPLESIZEPERCENTAGE$4);
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
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(SAMPLESIZEPERCENTAGE$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(SAMPLESIZEPERCENTAGE$4);
            }
            target.set(sampleSizePercentage);
        }
    }
}
