/*
 * XML Type:  rule
 * Namespace: http://configuration.openempi.openhie.org/single-best-record
 * Java type: org.openhie.openempi.configuration.xml.singlebestrecord.Rule
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.singlebestrecord.impl;
/**
 * An XML rule(@http://configuration.openempi.openhie.org/single-best-record).
 *
 * This is a complex type.
 */
public class RuleImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.singlebestrecord.Rule
{
    private static final long serialVersionUID = 1L;
    
    public RuleImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FIELDNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "field-name");
    private static final javax.xml.namespace.QName CONDITION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "condition");
    
    
    /**
     * Gets the "field-name" element
     */
    public java.lang.String getFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FIELDNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "field-name" element
     */
    public org.apache.xmlbeans.XmlString xgetFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FIELDNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "field-name" element
     */
    public void setFieldName(java.lang.String fieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FIELDNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FIELDNAME$0);
            }
            target.setStringValue(fieldName);
        }
    }
    
    /**
     * Sets (as xml) the "field-name" element
     */
    public void xsetFieldName(org.apache.xmlbeans.XmlString fieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FIELDNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FIELDNAME$0);
            }
            target.set(fieldName);
        }
    }
    
    /**
     * Gets the "condition" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Condition.Enum getCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CONDITION$2, 0);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.singlebestrecord.Condition.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "condition" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Condition xgetCondition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Condition target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Condition)get_store().find_element_user(CONDITION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "condition" element
     */
    public void setCondition(org.openhie.openempi.configuration.xml.singlebestrecord.Condition.Enum condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CONDITION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CONDITION$2);
            }
            target.setEnumValue(condition);
        }
    }
    
    /**
     * Sets (as xml) the "condition" element
     */
    public void xsetCondition(org.openhie.openempi.configuration.xml.singlebestrecord.Condition condition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Condition target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Condition)get_store().find_element_user(CONDITION$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.singlebestrecord.Condition)get_store().add_element_user(CONDITION$2);
            }
            target.set(condition);
        }
    }
}
