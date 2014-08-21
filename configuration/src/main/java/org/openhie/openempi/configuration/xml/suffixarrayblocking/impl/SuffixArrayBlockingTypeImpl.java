/*
 * XML Type:  suffix-array-blocking-type
 * Namespace: http://configuration.openempi.openhie.org/suffix-array-blocking
 * Java type: org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.suffixarrayblocking.impl;
/**
 * An XML suffix-array-blocking-type(@http://configuration.openempi.openhie.org/suffix-array-blocking).
 *
 * This is a complex type.
 */
public class SuffixArrayBlockingTypeImpl extends org.openhie.openempi.configuration.xml.impl.BlockingConfigurationTypeImpl implements org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType
{
    private static final long serialVersionUID = 1L;
    
    public SuffixArrayBlockingTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MINIMUMSUFFIXLENGTH$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "minimum-suffix-length");
    private static final javax.xml.namespace.QName MAXIMUMBLOCKSIZE$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "maximum-block-size");
    private static final javax.xml.namespace.QName BLOCKINGROUNDS$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "blocking-rounds");
    private static final javax.xml.namespace.QName SIMILARITYMETRIC$6 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "similarity-metric");
    private static final javax.xml.namespace.QName THRESHOLD$8 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "threshold");
    
    
    /**
     * Gets the "minimum-suffix-length" element
     */
    public int getMinimumSuffixLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINIMUMSUFFIXLENGTH$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "minimum-suffix-length" element
     */
    public org.apache.xmlbeans.XmlInt xgetMinimumSuffixLength()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MINIMUMSUFFIXLENGTH$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "minimum-suffix-length" element
     */
    public void setMinimumSuffixLength(int minimumSuffixLength)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINIMUMSUFFIXLENGTH$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MINIMUMSUFFIXLENGTH$0);
            }
            target.setIntValue(minimumSuffixLength);
        }
    }
    
    /**
     * Sets (as xml) the "minimum-suffix-length" element
     */
    public void xsetMinimumSuffixLength(org.apache.xmlbeans.XmlInt minimumSuffixLength)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MINIMUMSUFFIXLENGTH$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(MINIMUMSUFFIXLENGTH$0);
            }
            target.set(minimumSuffixLength);
        }
    }
    
    /**
     * Gets the "maximum-block-size" element
     */
    public int getMaximumBlockSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXIMUMBLOCKSIZE$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "maximum-block-size" element
     */
    public org.apache.xmlbeans.XmlInt xgetMaximumBlockSize()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MAXIMUMBLOCKSIZE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "maximum-block-size" element
     */
    public void setMaximumBlockSize(int maximumBlockSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXIMUMBLOCKSIZE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAXIMUMBLOCKSIZE$2);
            }
            target.setIntValue(maximumBlockSize);
        }
    }
    
    /**
     * Sets (as xml) the "maximum-block-size" element
     */
    public void xsetMaximumBlockSize(org.apache.xmlbeans.XmlInt maximumBlockSize)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(MAXIMUMBLOCKSIZE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(MAXIMUMBLOCKSIZE$2);
            }
            target.set(maximumBlockSize);
        }
    }
    
    /**
     * Gets the "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds getBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$4, 0);
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
    public void setBlockingRounds(org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds blockingRounds)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds)get_store().find_element_user(BLOCKINGROUNDS$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$4);
            }
            target.set(blockingRounds);
        }
    }
    
    /**
     * Appends and returns a new empty "blocking-rounds" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds addNewBlockingRounds()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds)get_store().add_element_user(BLOCKINGROUNDS$4);
            return target;
        }
    }
    
    /**
     * Gets the "similarity-metric" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.Enum getSimilarityMetric()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIMILARITYMETRIC$6, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "similarity-metric" element
     */
    public org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType xgetSimilarityMetric()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType)get_store().find_element_user(SIMILARITYMETRIC$6, 0);
            return target;
        }
    }
    
    /**
     * Sets the "similarity-metric" element
     */
    public void setSimilarityMetric(org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.Enum similarityMetric)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SIMILARITYMETRIC$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SIMILARITYMETRIC$6);
            }
            target.setEnumValue(similarityMetric);
        }
    }
    
    /**
     * Sets (as xml) the "similarity-metric" element
     */
    public void xsetSimilarityMetric(org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType similarityMetric)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType target = null;
            target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType)get_store().find_element_user(SIMILARITYMETRIC$6, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType)get_store().add_element_user(SIMILARITYMETRIC$6);
            }
            target.set(similarityMetric);
        }
    }
    
    /**
     * Gets the "threshold" element
     */
    public float getThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(THRESHOLD$8, 0);
            if (target == null)
            {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    /**
     * Gets (as xml) the "threshold" element
     */
    public org.apache.xmlbeans.XmlFloat xgetThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(THRESHOLD$8, 0);
            return target;
        }
    }
    
    /**
     * Sets the "threshold" element
     */
    public void setThreshold(float threshold)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(THRESHOLD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(THRESHOLD$8);
            }
            target.setFloatValue(threshold);
        }
    }
    
    /**
     * Sets (as xml) the "threshold" element
     */
    public void xsetThreshold(org.apache.xmlbeans.XmlFloat threshold)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(THRESHOLD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(THRESHOLD$8);
            }
            target.set(threshold);
        }
    }
}
