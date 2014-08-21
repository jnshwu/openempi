/*
 * XML Type:  extension-type
 * Namespace: http://configuration.openempi.openhie.org/mpicomponent
 * Java type: org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.mpicomponent.impl;
/**
 * An XML extension-type(@http://configuration.openempi.openhie.org/mpicomponent).
 *
 * This is a complex type.
 */
public class ExtensionTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType
{
    private static final long serialVersionUID = 1L;
    
    public ExtensionTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "name");
    private static final javax.xml.namespace.QName IMPLEMENTATION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "implementation");
    private static final javax.xml.namespace.QName INTERFACE$4 = 
        new javax.xml.namespace.QName("", "interface");
    
    
    /**
     * Gets the "name" element
     */
    public java.lang.String getName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "name" element
     */
    public org.apache.xmlbeans.XmlString xgetName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "name" element
     */
    public void setName(java.lang.String name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(NAME$0);
            }
            target.setStringValue(name);
        }
    }
    
    /**
     * Sets (as xml) the "name" element
     */
    public void xsetName(org.apache.xmlbeans.XmlString name)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(NAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(NAME$0);
            }
            target.set(name);
        }
    }
    
    /**
     * Gets the "implementation" element
     */
    public java.lang.String getImplementation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "implementation" element
     */
    public org.apache.xmlbeans.XmlString xgetImplementation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "implementation" element
     */
    public void setImplementation(java.lang.String implementation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMPLEMENTATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IMPLEMENTATION$2);
            }
            target.setStringValue(implementation);
        }
    }
    
    /**
     * Sets (as xml) the "implementation" element
     */
    public void xsetImplementation(org.apache.xmlbeans.XmlString implementation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMPLEMENTATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IMPLEMENTATION$2);
            }
            target.set(implementation);
        }
    }
    
    /**
     * Gets the "interface" attribute
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface.Enum getInterface()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(INTERFACE$4);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "interface" attribute
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface xgetInterface()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface)get_store().find_attribute_user(INTERFACE$4);
            return target;
        }
    }
    
    /**
     * Sets the "interface" attribute
     */
    public void setInterface(org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface.Enum xinterface)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(INTERFACE$4);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(INTERFACE$4);
            }
            target.setEnumValue(xinterface);
        }
    }
    
    /**
     * Sets (as xml) the "interface" attribute
     */
    public void xsetInterface(org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface xinterface)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface)get_store().find_attribute_user(INTERFACE$4);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface)get_store().add_attribute_user(INTERFACE$4);
            }
            target.set(xinterface);
        }
    }
    /**
     * An XML interface(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType$Interface.
     */
    public static class InterfaceImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.openhie.openempi.configuration.xml.mpicomponent.ExtensionType.Interface
    {
        private static final long serialVersionUID = 1L;
        
        public InterfaceImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected InterfaceImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
