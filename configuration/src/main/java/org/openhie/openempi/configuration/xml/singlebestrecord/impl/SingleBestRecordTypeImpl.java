/*
 * XML Type:  single-best-record-type
 * Namespace: http://configuration.openempi.openhie.org/single-best-record
 * Java type: org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.singlebestrecord.impl;
/**
 * An XML single-best-record-type(@http://configuration.openempi.openhie.org/single-best-record).
 *
 * This is a complex type.
 */
public class SingleBestRecordTypeImpl extends org.openhie.openempi.configuration.xml.impl.SingleBestRecordTypeImpl implements org.openhie.openempi.configuration.xml.singlebestrecord.SingleBestRecordType
{
    private static final long serialVersionUID = 1L;
    
    public SingleBestRecordTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IMPLEMENTATIONNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "implementation-name");
    private static final javax.xml.namespace.QName IMPLEMENTATIONDESCRIPTION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "implementation-description");
    private static final javax.xml.namespace.QName RULESET$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "ruleset");
    
    
    /**
     * Gets the "implementation-name" element
     */
    public java.lang.String getImplementationName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATIONNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "implementation-name" element
     */
    public org.apache.xmlbeans.XmlString xgetImplementationName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATIONNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "implementation-name" element
     */
    public void setImplementationName(java.lang.String implementationName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATIONNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IMPLEMENTATIONNAME$0);
            }
            target.setStringValue(implementationName);
        }
    }
    
    /**
     * Sets (as xml) the "implementation-name" element
     */
    public void xsetImplementationName(org.apache.xmlbeans.XmlString implementationName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATIONNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IMPLEMENTATIONNAME$0);
            }
            target.set(implementationName);
        }
    }
    
    /**
     * Gets the "implementation-description" element
     */
    public java.lang.String getImplementationDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATIONDESCRIPTION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "implementation-description" element
     */
    public org.apache.xmlbeans.XmlString xgetImplementationDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATIONDESCRIPTION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "implementation-description" element
     */
    public void setImplementationDescription(java.lang.String implementationDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATIONDESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IMPLEMENTATIONDESCRIPTION$2);
            }
            target.setStringValue(implementationDescription);
        }
    }
    
    /**
     * Sets (as xml) the "implementation-description" element
     */
    public void xsetImplementationDescription(org.apache.xmlbeans.XmlString implementationDescription)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATIONDESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IMPLEMENTATIONDESCRIPTION$2);
            }
            target.set(implementationDescription);
        }
    }
    
    /**
     * Gets the "ruleset" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset getRuleset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset)get_store().find_element_user(RULESET$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "ruleset" element
     */
    public void setRuleset(org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset ruleset)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset)get_store().find_element_user(RULESET$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset)get_store().add_element_user(RULESET$4);
            }
            target.set(ruleset);
        }
    }
    
    /**
     * Appends and returns a new empty "ruleset" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset addNewRuleset()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset)get_store().add_element_user(RULESET$4);
            return target;
        }
    }
}
