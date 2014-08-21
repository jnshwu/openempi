/*
 * XML Type:  substrings
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.Substrings
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML substrings(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class SubstringsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.Substrings
{
    private static final long serialVersionUID = 1L;
    
    public SubstringsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SUBSTRING$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "substring");
    
    
    /**
     * Gets array of all "substring" elements
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substring[] getSubstringArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SUBSTRING$0, targetList);
            org.openhie.openempi.configuration.xml.fileloader.Substring[] result = new org.openhie.openempi.configuration.xml.fileloader.Substring[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "substring" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substring getSubstringArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substring target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substring)get_store().find_element_user(SUBSTRING$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "substring" element
     */
    public int sizeOfSubstringArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SUBSTRING$0);
        }
    }
    
    /**
     * Sets array of all "substring" element
     */
    public void setSubstringArray(org.openhie.openempi.configuration.xml.fileloader.Substring[] substringArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(substringArray, SUBSTRING$0);
        }
    }
    
    /**
     * Sets ith "substring" element
     */
    public void setSubstringArray(int i, org.openhie.openempi.configuration.xml.fileloader.Substring substring)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substring target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substring)get_store().find_element_user(SUBSTRING$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(substring);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "substring" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substring insertNewSubstring(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substring target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substring)get_store().insert_element_user(SUBSTRING$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "substring" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substring addNewSubstring()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substring target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substring)get_store().add_element_user(SUBSTRING$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "substring" element
     */
    public void removeSubstring(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SUBSTRING$0, i);
        }
    }
}
