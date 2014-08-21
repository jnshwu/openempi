/*
 * XML Type:  data-field
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.DataField
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML data-field(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class DataFieldImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.DataField
{
    private static final long serialVersionUID = 1L;
    
    public DataFieldImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TARGETFIELDNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "target-field-name");
    private static final javax.xml.namespace.QName COMPOSITION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "composition");
    private static final javax.xml.namespace.QName SUBSTRINGS$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "substrings");
    private static final javax.xml.namespace.QName FUNCTION$6 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "function");
    private static final javax.xml.namespace.QName FORMAT$8 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "format");
    
    
    /**
     * Gets the "target-field-name" element
     */
    public java.lang.String getTargetFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TARGETFIELDNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "target-field-name" element
     */
    public org.apache.xmlbeans.XmlString xgetTargetFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TARGETFIELDNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "target-field-name" element
     */
    public void setTargetFieldName(java.lang.String targetFieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TARGETFIELDNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TARGETFIELDNAME$0);
            }
            target.setStringValue(targetFieldName);
        }
    }
    
    /**
     * Sets (as xml) the "target-field-name" element
     */
    public void xsetTargetFieldName(org.apache.xmlbeans.XmlString targetFieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TARGETFIELDNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TARGETFIELDNAME$0);
            }
            target.set(targetFieldName);
        }
    }
    
    /**
     * Gets the "composition" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Composition getComposition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Composition target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Composition)get_store().find_element_user(COMPOSITION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "composition" element
     */
    public boolean isSetComposition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(COMPOSITION$2) != 0;
        }
    }
    
    /**
     * Sets the "composition" element
     */
    public void setComposition(org.openhie.openempi.configuration.xml.fileloader.Composition composition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Composition target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Composition)get_store().find_element_user(COMPOSITION$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.fileloader.Composition)get_store().add_element_user(COMPOSITION$2);
            }
            target.set(composition);
        }
    }
    
    /**
     * Appends and returns a new empty "composition" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Composition addNewComposition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Composition target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Composition)get_store().add_element_user(COMPOSITION$2);
            return target;
        }
    }
    
    /**
     * Unsets the "composition" element
     */
    public void unsetComposition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(COMPOSITION$2, 0);
        }
    }
    
    /**
     * Gets the "substrings" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substrings getSubstrings()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substrings target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substrings)get_store().find_element_user(SUBSTRINGS$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "substrings" element
     */
    public boolean isSetSubstrings()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SUBSTRINGS$4) != 0;
        }
    }
    
    /**
     * Sets the "substrings" element
     */
    public void setSubstrings(org.openhie.openempi.configuration.xml.fileloader.Substrings substrings)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substrings target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substrings)get_store().find_element_user(SUBSTRINGS$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.fileloader.Substrings)get_store().add_element_user(SUBSTRINGS$4);
            }
            target.set(substrings);
        }
    }
    
    /**
     * Appends and returns a new empty "substrings" element
     */
    public org.openhie.openempi.configuration.xml.fileloader.Substrings addNewSubstrings()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.fileloader.Substrings target = null;
            target = (org.openhie.openempi.configuration.xml.fileloader.Substrings)get_store().add_element_user(SUBSTRINGS$4);
            return target;
        }
    }
    
    /**
     * Unsets the "substrings" element
     */
    public void unsetSubstrings()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SUBSTRINGS$4, 0);
        }
    }
    
    /**
     * Gets the "function" element
     */
    public java.lang.String getFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FUNCTION$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "function" element
     */
    public org.apache.xmlbeans.XmlString xgetFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FUNCTION$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "function" element
     */
    public boolean isSetFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FUNCTION$6) != 0;
        }
    }
    
    /**
     * Sets the "function" element
     */
    public void setFunction(java.lang.String function)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FUNCTION$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FUNCTION$6);
            }
            target.setStringValue(function);
        }
    }
    
    /**
     * Sets (as xml) the "function" element
     */
    public void xsetFunction(org.apache.xmlbeans.XmlString function)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FUNCTION$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FUNCTION$6);
            }
            target.set(function);
        }
    }
    
    /**
     * Unsets the "function" element
     */
    public void unsetFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FUNCTION$6, 0);
        }
    }
    
    /**
     * Gets the "format" element
     */
    public java.lang.String getFormat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FORMAT$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "format" element
     */
    public org.apache.xmlbeans.XmlString xgetFormat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FORMAT$8, 0);
            return target;
        }
    }
    
    /**
     * True if has "format" element
     */
    public boolean isSetFormat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(FORMAT$8) != 0;
        }
    }
    
    /**
     * Sets the "format" element
     */
    public void setFormat(java.lang.String format)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FORMAT$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FORMAT$8);
            }
            target.setStringValue(format);
        }
    }
    
    /**
     * Sets (as xml) the "format" element
     */
    public void xsetFormat(org.apache.xmlbeans.XmlString format)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FORMAT$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FORMAT$8);
            }
            target.set(format);
        }
    }
    
    /**
     * Unsets the "format" element
     */
    public void unsetFormat()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(FORMAT$8, 0);
        }
    }
}
