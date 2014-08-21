/*
 * XML Type:  match-field
 * Namespace: http://configuration.openempi.openhie.org/exact-matching
 * Java type: org.openhie.openempi.configuration.xml.exactmatching.MatchField
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.exactmatching.impl;
/**
 * An XML match-field(@http://configuration.openempi.openhie.org/exact-matching).
 *
 * This is a complex type.
 */
public class MatchFieldImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.exactmatching.MatchField
{
    private static final long serialVersionUID = 1L;
    
    public MatchFieldImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FIELDNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/exact-matching", "field-name");
    private static final javax.xml.namespace.QName COMPARATORFUNCTION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/exact-matching", "comparator-function");
    private static final javax.xml.namespace.QName MATCHTHRESHOLD$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/exact-matching", "match-threshold");
    
    
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
     * Gets the "comparator-function" element
     */
    public org.openhie.openempi.configuration.xml.ComparatorFunction getComparatorFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ComparatorFunction target = null;
            target = (org.openhie.openempi.configuration.xml.ComparatorFunction)get_store().find_element_user(COMPARATORFUNCTION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "comparator-function" element
     */
    public boolean isSetComparatorFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COMPARATORFUNCTION$2) != 0;
        }
    }
    
    /**
     * Sets the "comparator-function" element
     */
    public void setComparatorFunction(org.openhie.openempi.configuration.xml.ComparatorFunction comparatorFunction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ComparatorFunction target = null;
            target = (org.openhie.openempi.configuration.xml.ComparatorFunction)get_store().find_element_user(COMPARATORFUNCTION$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.ComparatorFunction)get_store().add_element_user(COMPARATORFUNCTION$2);
            }
            target.set(comparatorFunction);
        }
    }
    
    /**
     * Appends and returns a new empty "comparator-function" element
     */
    public org.openhie.openempi.configuration.xml.ComparatorFunction addNewComparatorFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ComparatorFunction target = null;
            target = (org.openhie.openempi.configuration.xml.ComparatorFunction)get_store().add_element_user(COMPARATORFUNCTION$2);
            return target;
        }
    }
    
    /**
     * Unsets the "comparator-function" element
     */
    public void unsetComparatorFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COMPARATORFUNCTION$2, 0);
        }
    }
    
    /**
     * Gets the "match-threshold" element
     */
    public float getMatchThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MATCHTHRESHOLD$4, 0);
            if (target == null)
            {
                return 0.0f;
            }
            return target.getFloatValue();
        }
    }
    
    /**
     * Gets (as xml) the "match-threshold" element
     */
    public org.apache.xmlbeans.XmlFloat xgetMatchThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MATCHTHRESHOLD$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "match-threshold" element
     */
    public boolean isSetMatchThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(MATCHTHRESHOLD$4) != 0;
        }
    }
    
    /**
     * Sets the "match-threshold" element
     */
    public void setMatchThreshold(float matchThreshold)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MATCHTHRESHOLD$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MATCHTHRESHOLD$4);
            }
            target.setFloatValue(matchThreshold);
        }
    }
    
    /**
     * Sets (as xml) the "match-threshold" element
     */
    public void xsetMatchThreshold(org.apache.xmlbeans.XmlFloat matchThreshold)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlFloat target = null;
            target = (org.apache.xmlbeans.XmlFloat)get_store().find_element_user(MATCHTHRESHOLD$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlFloat)get_store().add_element_user(MATCHTHRESHOLD$4);
            }
            target.set(matchThreshold);
        }
    }
    
    /**
     * Unsets the "match-threshold" element
     */
    public void unsetMatchThreshold()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(MATCHTHRESHOLD$4, 0);
        }
    }
}
