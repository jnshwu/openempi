/*
 * XML Type:  ruleset
 * Namespace: http://configuration.openempi.openhie.org/single-best-record
 * Java type: org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.singlebestrecord.impl;
/**
 * An XML ruleset(@http://configuration.openempi.openhie.org/single-best-record).
 *
 * This is a complex type.
 */
public class RulesetImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.singlebestrecord.Ruleset
{
    private static final long serialVersionUID = 1L;
    
    public RulesetImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RULE$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/single-best-record", "rule");
    
    
    /**
     * Gets array of all "rule" elements
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Rule[] getRuleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(RULE$0, targetList);
            org.openhie.openempi.configuration.xml.singlebestrecord.Rule[] result = new org.openhie.openempi.configuration.xml.singlebestrecord.Rule[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "rule" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Rule getRuleArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Rule target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Rule)get_store().find_element_user(RULE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "rule" element
     */
    public int sizeOfRuleArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(RULE$0);
        }
    }
    
    /**
     * Sets array of all "rule" element
     */
    public void setRuleArray(org.openhie.openempi.configuration.xml.singlebestrecord.Rule[] ruleArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(ruleArray, RULE$0);
        }
    }
    
    /**
     * Sets ith "rule" element
     */
    public void setRuleArray(int i, org.openhie.openempi.configuration.xml.singlebestrecord.Rule rule)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Rule target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Rule)get_store().find_element_user(RULE$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(rule);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "rule" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Rule insertNewRule(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Rule target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Rule)get_store().insert_element_user(RULE$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "rule" element
     */
    public org.openhie.openempi.configuration.xml.singlebestrecord.Rule addNewRule()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.singlebestrecord.Rule target = null;
            target = (org.openhie.openempi.configuration.xml.singlebestrecord.Rule)get_store().add_element_user(RULE$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "rule" element
     */
    public void removeRule(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(RULE$0, i);
        }
    }
}
