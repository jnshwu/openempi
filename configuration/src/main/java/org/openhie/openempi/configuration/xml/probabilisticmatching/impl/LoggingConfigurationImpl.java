/*
 * XML Type:  logging-configuration
 * Namespace: http://configuration.openempi.openhie.org/probabilistic-matching
 * Java type: org.openhie.openempi.configuration.xml.probabilisticmatching.LoggingConfiguration
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.probabilisticmatching.impl;
/**
 * An XML logging-configuration(@http://configuration.openempi.openhie.org/probabilistic-matching).
 *
 * This is a complex type.
 */
public class LoggingConfigurationImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.probabilisticmatching.LoggingConfiguration
{
    private static final long serialVersionUID = 1L;
    
    public LoggingConfigurationImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName LOGBYVECTORS$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "log-by-vectors");
    private static final javax.xml.namespace.QName LOGBYWEIGHT$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "log-by-weight");
    private static final javax.xml.namespace.QName LOGDESTINATION$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/probabilistic-matching", "log-destination");
    
    
    /**
     * Gets the "log-by-vectors" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors getLogByVectors()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors)get_store().find_element_user(LOGBYVECTORS$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "log-by-vectors" element
     */
    public boolean isSetLogByVectors()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LOGBYVECTORS$0) != 0;
        }
    }
    
    /**
     * Sets the "log-by-vectors" element
     */
    public void setLogByVectors(org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors logByVectors)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors)get_store().find_element_user(LOGBYVECTORS$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors)get_store().add_element_user(LOGBYVECTORS$0);
            }
            target.set(logByVectors);
        }
    }
    
    /**
     * Appends and returns a new empty "log-by-vectors" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors addNewLogByVectors()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByVectors)get_store().add_element_user(LOGBYVECTORS$0);
            return target;
        }
    }
    
    /**
     * Unsets the "log-by-vectors" element
     */
    public void unsetLogByVectors()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LOGBYVECTORS$0, 0);
        }
    }
    
    /**
     * Gets the "log-by-weight" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight getLogByWeight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight)get_store().find_element_user(LOGBYWEIGHT$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "log-by-weight" element
     */
    public boolean isSetLogByWeight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LOGBYWEIGHT$2) != 0;
        }
    }
    
    /**
     * Sets the "log-by-weight" element
     */
    public void setLogByWeight(org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight logByWeight)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight)get_store().find_element_user(LOGBYWEIGHT$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight)get_store().add_element_user(LOGBYWEIGHT$2);
            }
            target.set(logByWeight);
        }
    }
    
    /**
     * Appends and returns a new empty "log-by-weight" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight addNewLogByWeight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogByWeight)get_store().add_element_user(LOGBYWEIGHT$2);
            return target;
        }
    }
    
    /**
     * Unsets the "log-by-weight" element
     */
    public void unsetLogByWeight()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LOGBYWEIGHT$2, 0);
        }
    }
    
    /**
     * Gets the "log-destination" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination.Enum getLogDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOGDESTINATION$4, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "log-destination" element
     */
    public org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination xgetLogDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination)get_store().find_element_user(LOGDESTINATION$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "log-destination" element
     */
    public boolean isSetLogDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(LOGDESTINATION$4) != 0;
        }
    }
    
    /**
     * Sets the "log-destination" element
     */
    public void setLogDestination(org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination.Enum logDestination)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOGDESTINATION$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(LOGDESTINATION$4);
            }
            target.setEnumValue(logDestination);
        }
    }
    
    /**
     * Sets (as xml) the "log-destination" element
     */
    public void xsetLogDestination(org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination logDestination)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination target = null;
            target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination)get_store().find_element_user(LOGDESTINATION$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.probabilisticmatching.LogDestination)get_store().add_element_user(LOGDESTINATION$4);
            }
            target.set(logDestination);
        }
    }
    
    /**
     * Unsets the "log-destination" element
     */
    public void unsetLogDestination()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(LOGDESTINATION$4, 0);
        }
    }
}
