/*
 * An XML document type.
 * Localname: mpi-component-definition
 * Namespace: http://configuration.openempi.openhie.org/mpicomponent
 * Java type: org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.mpicomponent.impl;
/**
 * A document containing one mpi-component-definition(@http://configuration.openempi.openhie.org/mpicomponent) element.
 *
 * This is a complex type.
 */
public class MpiComponentDefinitionDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument
{
    private static final long serialVersionUID = 1L;
    
    public MpiComponentDefinitionDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MPICOMPONENTDEFINITION$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "mpi-component-definition");
    
    
    /**
     * Gets the "mpi-component-definition" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition getMpiComponentDefinition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition)get_store().find_element_user(MPICOMPONENTDEFINITION$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "mpi-component-definition" element
     */
    public void setMpiComponentDefinition(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition mpiComponentDefinition)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition)get_store().find_element_user(MPICOMPONENTDEFINITION$0, 0);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition)get_store().add_element_user(MPICOMPONENTDEFINITION$0);
            }
            target.set(mpiComponentDefinition);
        }
    }
    
    /**
     * Appends and returns a new empty "mpi-component-definition" element
     */
    public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition addNewMpiComponentDefinition()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition target = null;
            target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition)get_store().add_element_user(MPICOMPONENTDEFINITION$0);
            return target;
        }
    }
    /**
     * An XML mpi-component-definition(@http://configuration.openempi.openhie.org/mpicomponent).
     *
     * This is a complex type.
     */
    public static class MpiComponentDefinitionImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentDefinitionDocument.MpiComponentDefinition
    {
        private static final long serialVersionUID = 1L;
        
        public MpiComponentDefinitionImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName MPICOMPONENT$0 = 
            new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpicomponent", "mpi-component");
        
        
        /**
         * Gets the "mpi-component" element
         */
        public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType getMpiComponent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType target = null;
                target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType)get_store().find_element_user(MPICOMPONENT$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Sets the "mpi-component" element
         */
        public void setMpiComponent(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType mpiComponent)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType target = null;
                target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType)get_store().find_element_user(MPICOMPONENT$0, 0);
                if (target == null)
                {
                    target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType)get_store().add_element_user(MPICOMPONENT$0);
                }
                target.set(mpiComponent);
            }
        }
        
        /**
         * Appends and returns a new empty "mpi-component" element
         */
        public org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType addNewMpiComponent()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType target = null;
                target = (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType)get_store().add_element_user(MPICOMPONENT$0);
                return target;
            }
        }
    }
}
