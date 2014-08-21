/*
 * XML Type:  mpi-component-type
 * Namespace: http://configuration.openempi.openhie.org/mpicomponent
 * Java type: org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.mpicomponent.impl;
/**
 * An XML mpi-component-type(@http://configuration.openempi.openhie.org/mpicomponent).
 *
 * This is a complex type.
 */
public class MpiComponentTypeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType
{
    private static final long serialVersionUID = 1L;
    
    public MpiComponentTypeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName NAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "name");
    private static final javax.xml.namespace.QName DESCRIPTION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "description");
    private static final javax.xml.namespace.QName EXTENSIONS$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "extensions");
    private static final javax.xml.namespace.QName COMPONENTTYPE$6 = 
        new javax.xml.namespace.QName("", "component-type");
    
    
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
     * Gets the "description" element
     */
    public java.lang.String getDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRIPTION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "description" element
     */
    public org.apache.xmlbeans.XmlString xgetDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRIPTION$2, 0);
            return target;
        }
    }
    
    /**
     * True if has "description" element
     */
    public boolean isSetDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DESCRIPTION$2) != 0;
        }
    }
    
    /**
     * Sets the "description" element
     */
    public void setDescription(java.lang.String description)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DESCRIPTION$2);
            }
            target.setStringValue(description);
        }
    }
    
    /**
     * Sets (as xml) the "description" element
     */
    public void xsetDescription(org.apache.xmlbeans.XmlString description)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(DESCRIPTION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(DESCRIPTION$2);
            }
            target.set(description);
        }
    }
    
    /**
     * Unsets the "description" element
     */
    public void unsetDescription()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DESCRIPTION$2, 0);
        }
    }
    
    /**
     * Gets the "extensions" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType getExtensions()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType)get_store().find_element_user(EXTENSIONS$4, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "extensions" element
     */
    public void setExtensions(org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType extensions)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType)get_store().find_element_user(EXTENSIONS$4, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType)get_store().add_element_user(EXTENSIONS$4);
            }
            target.set(extensions);
        }
    }
    
    /**
     * Appends and returns a new empty "extensions" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType addNewExtensions()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType)get_store().add_element_user(EXTENSIONS$4);
            return target;
        }
    }
    
    /**
     * Gets the "component-type" attribute
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType.Enum getComponentType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COMPONENTTYPE$6);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "component-type" attribute
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType xgetComponentType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType)get_store().find_attribute_user(COMPONENTTYPE$6);
            return target;
        }
    }
    
    /**
     * Sets the "component-type" attribute
     */
    public void setComponentType(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType.Enum componentType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(COMPONENTTYPE$6);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(COMPONENTTYPE$6);
            }
            target.setEnumValue(componentType);
        }
    }
    
    /**
     * Sets (as xml) the "component-type" attribute
     */
    public void xsetComponentType(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType componentType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType)get_store().find_attribute_user(COMPONENTTYPE$6);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType)get_store().add_attribute_user(COMPONENTTYPE$6);
            }
            target.set(componentType);
        }
    }
    /**
     * An XML component-type(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType$ComponentType.
     */
    public static class ComponentTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType
    {
        private static final long serialVersionUID = 1L;
        
        public ComponentTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ComponentTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
