/*
 * XML Type:  transformation-function
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.TransformationFunction
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML transformation-function(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class TransformationFunctionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.TransformationFunction
{
    private static final long serialVersionUID = 1L;
    
    public TransformationFunctionImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName FUNCTIONNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "function-name");
    private static final javax.xml.namespace.QName PARAMETERS$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "parameters");
    
    
    /**
     * Gets the "function-name" element
     */
    public java.lang.String getFunctionName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FUNCTIONNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "function-name" element
     */
    public org.apache.xmlbeans.XmlString xgetFunctionName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FUNCTIONNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "function-name" element
     */
    public void setFunctionName(java.lang.String functionName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(FUNCTIONNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(FUNCTIONNAME$0);
            }
            target.setStringValue(functionName);
        }
    }
    
    /**
     * Sets (as xml) the "function-name" element
     */
    public void xsetFunctionName(org.apache.xmlbeans.XmlString functionName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(FUNCTIONNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(FUNCTIONNAME$0);
            }
            target.set(functionName);
        }
    }
    
    /**
     * Gets the "parameters" element
     */
    public org.openhie.openempi.configuration.xml.Parameters getParameters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.Parameters target = null;
            target = (org.openhie.openempi.configuration.xml.Parameters)get_store().find_element_user(PARAMETERS$2, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * True if has "parameters" element
     */
    public boolean isSetParameters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PARAMETERS$2) != 0;
        }
    }
    
    /**
     * Sets the "parameters" element
     */
    public void setParameters(org.openhie.openempi.configuration.xml.Parameters parameters)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.Parameters target = null;
            target = (org.openhie.openempi.configuration.xml.Parameters)get_store().find_element_user(PARAMETERS$2, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.Parameters)get_store().add_element_user(PARAMETERS$2);
            }
            target.set(parameters);
        }
    }
    
    /**
     * Appends and returns a new empty "parameters" element
     */
    public org.openhie.openempi.configuration.xml.Parameters addNewParameters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.Parameters target = null;
            target = (org.openhie.openempi.configuration.xml.Parameters)get_store().add_element_user(PARAMETERS$2);
            return target;
        }
    }
    
    /**
     * Unsets the "parameters" element
     */
    public void unsetParameters()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PARAMETERS$2, 0);
        }
    }
}
