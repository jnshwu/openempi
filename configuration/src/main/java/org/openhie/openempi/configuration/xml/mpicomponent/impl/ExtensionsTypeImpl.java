/*
 * XML Type:  extensions-type
 * Namespace: http://configuration.openempi.openhie.org/mpicomponent
 * Java type: org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.mpicomponent.impl;
/**
 * An XML extensions-type(@http://configuration.openempi.openhie.org/mpicomponent).
 *
 * This is a complex type.
 */
public class ExtensionsTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType
{
    private static final long serialVersionUID = 1L;
    
    public ExtensionsTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName EXTENSION$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "extension");
    
    
    /**
     * Gets array of all "extension" elements
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType[] getExtensionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(EXTENSION$0, targetList);
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType[] result = new org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "extension" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType getExtensionArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType)get_store().find_element_user(EXTENSION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "extension" element
     */
    public int sizeOfExtensionArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(EXTENSION$0);
        }
    }
    
    /**
     * Sets array of all "extension" element
     */
    public void setExtensionArray(org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType[] extensionArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(extensionArray, EXTENSION$0);
        }
    }
    
    /**
     * Sets ith "extension" element
     */
    public void setExtensionArray(int i, org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType extension)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType)get_store().find_element_user(EXTENSION$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(extension);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "extension" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType insertNewExtension(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType)get_store().insert_element_user(EXTENSION$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "extension" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType addNewExtension()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType)get_store().add_element_user(EXTENSION$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "extension" element
     */
    public void removeExtension(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(EXTENSION$0, i);
        }
    }
}
