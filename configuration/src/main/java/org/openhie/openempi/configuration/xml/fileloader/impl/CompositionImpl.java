/*
 * XML Type:  composition
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.Composition
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML composition(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class CompositionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.Composition
{
    private static final long serialVersionUID = 1L;
    
    public CompositionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INDEX$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "index");
    private static final javax.xml.namespace.QName SEPARATOR$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "separator");
    
    
    /**
     * Gets the "index" element
     */
    public int getIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDEX$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "index" element
     */
    public org.apache.xmlbeans.XmlInt xgetIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDEX$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "index" element
     */
    public void setIndex(int index)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INDEX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INDEX$0);
            }
            target.setIntValue(index);
        }
    }
    
    /**
     * Sets (as xml) the "index" element
     */
    public void xsetIndex(org.apache.xmlbeans.XmlInt index)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(INDEX$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(INDEX$0);
            }
            target.set(index);
        }
    }
    
    /**
     * Gets the "separator" element
     */
    public java.lang.String getSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SEPARATOR$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "separator" element
     */
    public org.apache.xmlbeans.XmlString xgetSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SEPARATOR$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "separator" element
     */
    public boolean isSetSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SEPARATOR$2) != 0;
        }
    }
    
    /**
     * Sets the "separator" element
     */
    public void setSeparator(java.lang.String separator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SEPARATOR$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SEPARATOR$2);
            }
            target.setStringValue(separator);
        }
    }
    
    /**
     * Sets (as xml) the "separator" element
     */
    public void xsetSeparator(org.apache.xmlbeans.XmlString separator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SEPARATOR$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SEPARATOR$2);
            }
            target.set(separator);
        }
    }
    
    /**
     * Unsets the "separator" element
     */
    public void unsetSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SEPARATOR$2, 0);
        }
    }
}
