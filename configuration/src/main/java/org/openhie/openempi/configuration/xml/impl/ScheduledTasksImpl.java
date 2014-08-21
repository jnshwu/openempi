/*
 * XML Type:  scheduled-tasks
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.ScheduledTasks
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML scheduled-tasks(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class ScheduledTasksImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.ScheduledTasks
{
    private static final long serialVersionUID = 1L;
    
    public ScheduledTasksImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SCHEDULEDTASK$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "scheduled-task");
    
    
    /**
     * Gets array of all "scheduled-task" elements
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask[] getScheduledTaskArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(SCHEDULEDTASK$0, targetList);
            org.openhie.openempi.configuration.xml.ScheduledTask[] result = new org.openhie.openempi.configuration.xml.ScheduledTask[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "scheduled-task" element
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask getScheduledTaskArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask)get_store().find_element_user(SCHEDULEDTASK$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "scheduled-task" element
     */
    public int sizeOfScheduledTaskArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(SCHEDULEDTASK$0);
        }
    }
    
    /**
     * Sets array of all "scheduled-task" element
     */
    public void setScheduledTaskArray(org.openhie.openempi.configuration.xml.ScheduledTask[] scheduledTaskArray)
    {
        synchronized (monitor())
        {
            check_orphaned();
            arraySetterHelper(scheduledTaskArray, SCHEDULEDTASK$0);
        }
    }
    
    /**
     * Sets ith "scheduled-task" element
     */
    public void setScheduledTaskArray(int i, org.openhie.openempi.configuration.xml.ScheduledTask scheduledTask)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask)get_store().find_element_user(SCHEDULEDTASK$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            target.set(scheduledTask);
        }
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "scheduled-task" element
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask insertNewScheduledTask(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask)get_store().insert_element_user(SCHEDULEDTASK$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "scheduled-task" element
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask addNewScheduledTask()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask)get_store().add_element_user(SCHEDULEDTASK$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "scheduled-task" element
     */
    public void removeScheduledTask(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(SCHEDULEDTASK$0, i);
        }
    }
}
