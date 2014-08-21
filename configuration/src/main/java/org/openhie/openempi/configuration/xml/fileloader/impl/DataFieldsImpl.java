/*
 * XML Type:  data-fields
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.DataFields
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML data-fields(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class DataFieldsImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.DataFields
{
    private static final long serialVersionUID = 1L;
    
    public DataFieldsImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName DATAFIELD$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "data-field");
    
    
    /**
     * Gets array of all "data-field" elements
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataField[] getDataFieldArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(DATAFIELD$0, targetList);
            org.openhie.openempi.configuration.xml.fileloader.DataField[] result = new org.openhie.openempi.configuration.xml.fileloader.DataField[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "data-field" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataField getDataFieldArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataField target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataField)get_store().find_element_user(DATAFIELD$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "data-field" element
     */
    public int sizeOfDataFieldArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DATAFIELD$0);
        }
    }
    
    /**
     * Sets array of all "data-field" element
     */
    public void setDataFieldArray(org.openhie.openempi.configuration.xml.fileloader.DataField[] dataFieldArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(dataFieldArray, DATAFIELD$0);
        }
    }
    
    /**
     * Sets ith "data-field" element
     */
    public void setDataFieldArray(int i, org.openhie.openempi.configuration.xml.fileloader.DataField dataField)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataField target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataField)get_store().find_element_user(DATAFIELD$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(dataField);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "data-field" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataField insertNewDataField(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataField target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataField)get_store().insert_element_user(DATAFIELD$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "data-field" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.DataField addNewDataField()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.DataField target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.DataField)get_store().add_element_user(DATAFIELD$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "data-field" element
     */
    public void removeDataField(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DATAFIELD$0, i);
        }
    }
}
