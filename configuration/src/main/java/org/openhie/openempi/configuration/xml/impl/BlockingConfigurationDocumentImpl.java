/*
 * An XML document type.
 * Localname: blocking-configuration
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.BlockingConfigurationDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * A document containing one blocking-configuration(@http://configuration.openempi.openhie.org/mpiconfig) element.
 *
 * This is a complex type.
 */
public class BlockingConfigurationDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.BlockingConfigurationDocument
{
    private static final long serialVersionUID = 1L;
    
    public BlockingConfigurationDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName BLOCKINGCONFIGURATION$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "blocking-configuration");
    private static final org.apache.xmlbeans.QNameSet BLOCKINGCONFIGURATION$1 = org.apache.xmlbeans.QNameSet.forArray( new javax.xml.namespace.QName[] { 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "blocking-configuration"),
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/suffix-array-blocking", "suffix-array-blocking"),
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking-hp", "basic-blocking"),
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/basic-blocking", "basic-blocking"),
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/sorted-neighborhood-blocking", "sorted-neighborhood-blocking"),
    });
    
    
    /**
     * Gets the "blocking-configuration" element
     */
    public org.openhie.openempi.configuration.xml.BlockingConfigurationType getBlockingConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
            target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().find_element_user(BLOCKINGCONFIGURATION$1, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "blocking-configuration" element
     */
    public void setBlockingConfiguration(org.openhie.openempi.configuration.xml.BlockingConfigurationType blockingConfiguration)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
            target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().find_element_user(BLOCKINGCONFIGURATION$1, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().add_element_user(BLOCKINGCONFIGURATION$0);
            }
            target.set(blockingConfiguration);
        }
    }
    
    /**
     * Appends and returns a new empty "blocking-configuration" element
     */
    public org.openhie.openempi.configuration.xml.BlockingConfigurationType addNewBlockingConfiguration()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.BlockingConfigurationType target = null;
            target = (org.openhie.openempi.configuration.xml.BlockingConfigurationType)get_store().add_element_user(BLOCKINGCONFIGURATION$0);
            return target;
        }
    }
}
