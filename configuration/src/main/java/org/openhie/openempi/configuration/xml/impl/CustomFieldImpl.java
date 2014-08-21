/*
 * XML Type:  custom-field
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.CustomField
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML custom-field(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class CustomFieldImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.CustomField
{
    private static final long serialVersionUID = 1L;
    
    public CustomFieldImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FIELDNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "field-name");
    private static final javax.xml.namespace.QName SOURCEFIELDNAME$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "source-field-name");
    private static final javax.xml.namespace.QName TRANSFORMATIONFUNCTION$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "transformation-function");
    
    
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
     * Gets the "source-field-name" element
     */
    public java.lang.String getSourceFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SOURCEFIELDNAME$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "source-field-name" element
     */
    public org.apache.xmlbeans.XmlString xgetSourceFieldName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SOURCEFIELDNAME$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "source-field-name" element
     */
    public void setSourceFieldName(java.lang.String sourceFieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(SOURCEFIELDNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(SOURCEFIELDNAME$2);
            }
            target.setStringValue(sourceFieldName);
        }
    }
    
    /**
     * Sets (as xml) the "source-field-name" element
     */
    public void xsetSourceFieldName(org.apache.xmlbeans.XmlString sourceFieldName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(SOURCEFIELDNAME$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(SOURCEFIELDNAME$2);
            }
            target.set(sourceFieldName);
        }
    }
    
    /**
     * Gets the "transformation-function" element
     */
    public org.openhie.openempi.configuration.xml.TransformationFunction getTransformationFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.TransformationFunction target = null;
            target = (org.openhie.openempi.configuration.xml.TransformationFunction)get_store().find_element_user(TRANSFORMATIONFUNCTION$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "transformation-function" element
     */
    public void setTransformationFunction(org.openhie.openempi.configuration.xml.TransformationFunction transformationFunction)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.TransformationFunction target = null;
            target = (org.openhie.openempi.configuration.xml.TransformationFunction)get_store().find_element_user(TRANSFORMATIONFUNCTION$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.TransformationFunction)get_store().add_element_user(TRANSFORMATIONFUNCTION$4);
            }
            target.set(transformationFunction);
        }
    }
    
    /**
     * Appends and returns a new empty "transformation-function" element
     */
    public org.openhie.openempi.configuration.xml.TransformationFunction addNewTransformationFunction()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.TransformationFunction target = null;
            target = (org.openhie.openempi.configuration.xml.TransformationFunction)get_store().add_element_user(TRANSFORMATIONFUNCTION$4);
            return target;
        }
    }
}
