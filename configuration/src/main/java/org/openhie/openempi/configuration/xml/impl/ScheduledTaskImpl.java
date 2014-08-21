/*
 * XML Type:  scheduled-task
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.ScheduledTask
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.impl;
/**
 * An XML scheduled-task(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public class ScheduledTaskImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements org.openhie.openempi.configuration.xml.ScheduledTask
{
    private static final long serialVersionUID = 1L;
    
    public ScheduledTaskImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TASKNAME$0 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "task-name");
    private static final javax.xml.namespace.QName TASKIMPLEMENTATION$2 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "task-implementation");
    private static final javax.xml.namespace.QName DELAY$4 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "delay");
    private static final javax.xml.namespace.QName INITIALDELAY$6 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "initial-delay");
    private static final javax.xml.namespace.QName PERIOD$8 = 
        new javax.xml.namespace.QName("http://configuration.openempi.openhie.org/mpiconfig", "period");
    private static final javax.xml.namespace.QName SCHEDULETYPE$10 = 
        new javax.xml.namespace.QName("", "schedule-type");
    private static final javax.xml.namespace.QName TIMEUNIT$12 = 
        new javax.xml.namespace.QName("", "time-unit");
    
    
    /**
     * Gets the "task-name" element
     */
    public java.lang.String getTaskName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TASKNAME$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "task-name" element
     */
    public org.apache.xmlbeans.XmlString xgetTaskName()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TASKNAME$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "task-name" element
     */
    public void setTaskName(java.lang.String taskName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TASKNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TASKNAME$0);
            }
            target.setStringValue(taskName);
        }
    }
    
    /**
     * Sets (as xml) the "task-name" element
     */
    public void xsetTaskName(org.apache.xmlbeans.XmlString taskName)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TASKNAME$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TASKNAME$0);
            }
            target.set(taskName);
        }
    }
    
    /**
     * Gets the "task-implementation" element
     */
    public java.lang.String getTaskImplementation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TASKIMPLEMENTATION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "task-implementation" element
     */
    public org.apache.xmlbeans.XmlString xgetTaskImplementation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TASKIMPLEMENTATION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "task-implementation" element
     */
    public void setTaskImplementation(java.lang.String taskImplementation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TASKIMPLEMENTATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TASKIMPLEMENTATION$2);
            }
            target.setStringValue(taskImplementation);
        }
    }
    
    /**
     * Sets (as xml) the "task-implementation" element
     */
    public void xsetTaskImplementation(org.apache.xmlbeans.XmlString taskImplementation)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TASKIMPLEMENTATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TASKIMPLEMENTATION$2);
            }
            target.set(taskImplementation);
        }
    }
    
    /**
     * Gets the "delay" element
     */
    public long getDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DELAY$4, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "delay" element
     */
    public org.apache.xmlbeans.XmlLong xgetDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(DELAY$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "delay" element
     */
    public boolean isSetDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(DELAY$4) != 0;
        }
    }
    
    /**
     * Sets the "delay" element
     */
    public void setDelay(long delay)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(DELAY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(DELAY$4);
            }
            target.setLongValue(delay);
        }
    }
    
    /**
     * Sets (as xml) the "delay" element
     */
    public void xsetDelay(org.apache.xmlbeans.XmlLong delay)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(DELAY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(DELAY$4);
            }
            target.set(delay);
        }
    }
    
    /**
     * Unsets the "delay" element
     */
    public void unsetDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(DELAY$4, 0);
        }
    }
    
    /**
     * Gets the "initial-delay" element
     */
    public long getInitialDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INITIALDELAY$6, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "initial-delay" element
     */
    public org.apache.xmlbeans.XmlLong xgetInitialDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(INITIALDELAY$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "initial-delay" element
     */
    public boolean isSetInitialDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(INITIALDELAY$6) != 0;
        }
    }
    
    /**
     * Sets the "initial-delay" element
     */
    public void setInitialDelay(long initialDelay)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(INITIALDELAY$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(INITIALDELAY$6);
            }
            target.setLongValue(initialDelay);
        }
    }
    
    /**
     * Sets (as xml) the "initial-delay" element
     */
    public void xsetInitialDelay(org.apache.xmlbeans.XmlLong initialDelay)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(INITIALDELAY$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(INITIALDELAY$6);
            }
            target.set(initialDelay);
        }
    }
    
    /**
     * Unsets the "initial-delay" element
     */
    public void unsetInitialDelay()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(INITIALDELAY$6, 0);
        }
    }
    
    /**
     * Gets the "period" element
     */
    public long getPeriod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PERIOD$8, 0);
            if (target == null)
            {
                return 0L;
            }
            return target.getLongValue();
        }
    }
    
    /**
     * Gets (as xml) the "period" element
     */
    public org.apache.xmlbeans.XmlLong xgetPeriod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(PERIOD$8, 0);
            return target;
        }
    }
    
    /**
     * True if has "period" element
     */
    public boolean isSetPeriod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PERIOD$8) != 0;
        }
    }
    
    /**
     * Sets the "period" element
     */
    public void setPeriod(long period)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PERIOD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PERIOD$8);
            }
            target.setLongValue(period);
        }
    }
    
    /**
     * Sets (as xml) the "period" element
     */
    public void xsetPeriod(org.apache.xmlbeans.XmlLong period)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlLong target = null;
            target = (org.apache.xmlbeans.XmlLong)get_store().find_element_user(PERIOD$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlLong)get_store().add_element_user(PERIOD$8);
            }
            target.set(period);
        }
    }
    
    /**
     * Unsets the "period" element
     */
    public void unsetPeriod()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PERIOD$8, 0);
        }
    }
    
    /**
     * Gets the "schedule-type" attribute
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType.Enum getScheduleType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEDULETYPE$10);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "schedule-type" attribute
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType xgetScheduleType()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType)get_store().find_attribute_user(SCHEDULETYPE$10);
            return target;
        }
    }
    
    /**
     * Sets the "schedule-type" attribute
     */
    public void setScheduleType(org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType.Enum scheduleType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(SCHEDULETYPE$10);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(SCHEDULETYPE$10);
            }
            target.setEnumValue(scheduleType);
        }
    }
    
    /**
     * Sets (as xml) the "schedule-type" attribute
     */
    public void xsetScheduleType(org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType scheduleType)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType)get_store().find_attribute_user(SCHEDULETYPE$10);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType)get_store().add_attribute_user(SCHEDULETYPE$10);
            }
            target.set(scheduleType);
        }
    }
    
    /**
     * Gets the "time-unit" attribute
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit.Enum getTimeUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIMEUNIT$12);
            if (target == null)
            {
                return null;
            }
            return (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit.Enum)target.getEnumValue();
        }
    }
    
    /**
     * Gets (as xml) the "time-unit" attribute
     */
    public org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit xgetTimeUnit()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit)get_store().find_attribute_user(TIMEUNIT$12);
            return target;
        }
    }
    
    /**
     * Sets the "time-unit" attribute
     */
    public void setTimeUnit(org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit.Enum timeUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_attribute_user(TIMEUNIT$12);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_attribute_user(TIMEUNIT$12);
            }
            target.setEnumValue(timeUnit);
        }
    }
    
    /**
     * Sets (as xml) the "time-unit" attribute
     */
    public void xsetTimeUnit(org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit timeUnit)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit target = null;
            target = (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit)get_store().find_attribute_user(TIMEUNIT$12);
            if (target == null)
            {
                target = (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit)get_store().add_attribute_user(TIMEUNIT$12);
            }
            target.set(timeUnit);
        }
    }
    /**
     * An XML schedule-type(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.ScheduledTask$ScheduleType.
     */
    public static class ScheduleTypeImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType
    {
        private static final long serialVersionUID = 1L;
        
        public ScheduleTypeImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ScheduleTypeImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
    /**
     * An XML time-unit(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.ScheduledTask$TimeUnit.
     */
    public static class TimeUnitImpl extends org.apache.xmlbeans.impl.values.JavaStringEnumerationHolderEx implements org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit
    {
        private static final long serialVersionUID = 1L;
        
        public TimeUnitImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected TimeUnitImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
