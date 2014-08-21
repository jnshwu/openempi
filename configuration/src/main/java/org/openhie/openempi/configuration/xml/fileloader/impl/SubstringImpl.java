/*
 * XML Type:  substring
 * Namespace: http://configuration.openempi.openhie.org/file-loader
 * Java type: org.openhie.openempi.configuration.xml.fileloader.Substring
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.fileloader.impl;
/**
 * An XML substring(@http://configuration.openempi.openhie.org/file-loader).
 *
 * This is a complex type.
 */
public class SubstringImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.fileloader.Substring
{
    private static final long serialVersionUID = 1L;
    
    public SubstringImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TARGETFIELDNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "target-field-name");
    private static final javax.xml.namespace.QName BEGININDEX$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "begin-index");
    private static final javax.xml.namespace.QName ENDINDEX$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/file-loader", "end-index");
    
    
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
     * Gets the "begin-index" element
     */
    public int getBeginIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BEGININDEX$2, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "begin-index" element
     */
    public org.apache.xmlbeans.XmlInt xgetBeginIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BEGININDEX$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "begin-index" element
     */
    public void setBeginIndex(int beginIndex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(BEGININDEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(BEGININDEX$2);
            }
            target.setIntValue(beginIndex);
        }
    }
    
    /**
     * Sets (as xml) the "begin-index" element
     */
    public void xsetBeginIndex(org.apache.xmlbeans.XmlInt beginIndex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(BEGININDEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(BEGININDEX$2);
            }
            target.set(beginIndex);
        }
    }
    
    /**
     * Gets the "end-index" element
     */
    public int getEndIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDINDEX$4, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "end-index" element
     */
    public org.apache.xmlbeans.XmlInt xgetEndIndex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ENDINDEX$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "end-index" element
     */
    public void setEndIndex(int endIndex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDINDEX$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ENDINDEX$4);
            }
            target.setIntValue(endIndex);
        }
    }
    
    /**
     * Sets (as xml) the "end-index" element
     */
    public void xsetEndIndex(org.apache.xmlbeans.XmlInt endIndex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(ENDINDEX$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(ENDINDEX$4);
            }
            target.set(endIndex);
        }
    }
}
