/*
 * XML Type:  scheduled-task
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.ScheduledTask
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml;


/**
 * An XML scheduled-task(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public interface ScheduledTask extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScheduledTask.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("scheduledtask27detype");
    
    /**
     * Gets the "task-name" element
     */
    java.lang.String getTaskName();
    
    /**
     * Gets (as xml) the "task-name" element
     */
    org.apache.xmlbeans.XmlString xgetTaskName();
    
    /**
     * Sets the "task-name" element
     */
    void setTaskName(java.lang.String taskName);
    
    /**
     * Sets (as xml) the "task-name" element
     */
    void xsetTaskName(org.apache.xmlbeans.XmlString taskName);
    
    /**
     * Gets the "task-implementation" element
     */
    java.lang.String getTaskImplementation();
    
    /**
     * Gets (as xml) the "task-implementation" element
     */
    org.apache.xmlbeans.XmlString xgetTaskImplementation();
    
    /**
     * Sets the "task-implementation" element
     */
    void setTaskImplementation(java.lang.String taskImplementation);
    
    /**
     * Sets (as xml) the "task-implementation" element
     */
    void xsetTaskImplementation(org.apache.xmlbeans.XmlString taskImplementation);
    
    /**
     * Gets the "delay" element
     */
    long getDelay();
    
    /**
     * Gets (as xml) the "delay" element
     */
    org.apache.xmlbeans.XmlLong xgetDelay();
    
    /**
     * True if has "delay" element
     */
    boolean isSetDelay();
    
    /**
     * Sets the "delay" element
     */
    void setDelay(long delay);
    
    /**
     * Sets (as xml) the "delay" element
     */
    void xsetDelay(org.apache.xmlbeans.XmlLong delay);
    
    /**
     * Unsets the "delay" element
     */
    void unsetDelay();
    
    /**
     * Gets the "initial-delay" element
     */
    long getInitialDelay();
    
    /**
     * Gets (as xml) the "initial-delay" element
     */
    org.apache.xmlbeans.XmlLong xgetInitialDelay();
    
    /**
     * True if has "initial-delay" element
     */
    boolean isSetInitialDelay();
    
    /**
     * Sets the "initial-delay" element
     */
    void setInitialDelay(long initialDelay);
    
    /**
     * Sets (as xml) the "initial-delay" element
     */
    void xsetInitialDelay(org.apache.xmlbeans.XmlLong initialDelay);
    
    /**
     * Unsets the "initial-delay" element
     */
    void unsetInitialDelay();
    
    /**
     * Gets the "period" element
     */
    long getPeriod();
    
    /**
     * Gets (as xml) the "period" element
     */
    org.apache.xmlbeans.XmlLong xgetPeriod();
    
    /**
     * True if has "period" element
     */
    boolean isSetPeriod();
    
    /**
     * Sets the "period" element
     */
    void setPeriod(long period);
    
    /**
     * Sets (as xml) the "period" element
     */
    void xsetPeriod(org.apache.xmlbeans.XmlLong period);
    
    /**
     * Unsets the "period" element
     */
    void unsetPeriod();
    
    /**
     * Gets the "schedule-type" attribute
     */
    org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType.Enum getScheduleType();
    
    /**
     * Gets (as xml) the "schedule-type" attribute
     */
    org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType xgetScheduleType();
    
    /**
     * Sets the "schedule-type" attribute
     */
    void setScheduleType(org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType.Enum scheduleType);
    
    /**
     * Sets (as xml) the "schedule-type" attribute
     */
    void xsetScheduleType(org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType scheduleType);
    
    /**
     * Gets the "time-unit" attribute
     */
    org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit.Enum getTimeUnit();
    
    /**
     * Gets (as xml) the "time-unit" attribute
     */
    org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit xgetTimeUnit();
    
    /**
     * Sets the "time-unit" attribute
     */
    void setTimeUnit(org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit.Enum timeUnit);
    
    /**
     * Sets (as xml) the "time-unit" attribute
     */
    void xsetTimeUnit(org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit timeUnit);
    
    /**
     * An XML schedule-type(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.ScheduledTask$ScheduleType.
     */
    public interface ScheduleType extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ScheduleType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("scheduletype4cc6attrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum SCHEDULE = Enum.forString("schedule");
        static final Enum SCHEDULE_AT_FIXED_RATE = Enum.forString("schedule-at-fixed-rate");
        static final Enum SCHEDULE_WITH_FIXED_DELAY = Enum.forString("schedule-with-fixed-delay");
        
        static final int INT_SCHEDULE = Enum.INT_SCHEDULE;
        static final int INT_SCHEDULE_AT_FIXED_RATE = Enum.INT_SCHEDULE_AT_FIXED_RATE;
        static final int INT_SCHEDULE_WITH_FIXED_DELAY = Enum.INT_SCHEDULE_WITH_FIXED_DELAY;
        
        /**
         * Enumeration value class for org.openhie.openempi.configuration.xml.ScheduledTask$ScheduleType.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_SCHEDULE
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
        {
            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s)
                { return (Enum)table.forString(s); }
            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i)
                { return (Enum)table.forInt(i); }
            
            private Enum(java.lang.String s, int i)
                { super(s, i); }
            
            static final int INT_SCHEDULE = 1;
            static final int INT_SCHEDULE_AT_FIXED_RATE = 2;
            static final int INT_SCHEDULE_WITH_FIXED_DELAY = 3;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("schedule", INT_SCHEDULE),
                    new Enum("schedule-at-fixed-rate", INT_SCHEDULE_AT_FIXED_RATE),
                    new Enum("schedule-with-fixed-delay", INT_SCHEDULE_WITH_FIXED_DELAY),
                }
            );
            private static final long serialVersionUID = 1L;
            private java.lang.Object readResolve() { return forInt(intValue()); } 
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType newValue(java.lang.Object obj) {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType) type.newValue( obj ); }
            
            public static org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType newInstance() {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.ScheduleType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * An XML time-unit(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.ScheduledTask$TimeUnit.
     */
    public interface TimeUnit extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(TimeUnit.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("timeunite372attrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum DAYS = Enum.forString("days");
        static final Enum HOURS = Enum.forString("hours");
        static final Enum MICROSECONDS = Enum.forString("microseconds");
        static final Enum MILLISECONDS = Enum.forString("milliseconds");
        static final Enum MINUTES = Enum.forString("minutes");
        static final Enum SECONDS = Enum.forString("seconds");
        static final Enum NANOSECONDS = Enum.forString("nanoseconds");
        
        static final int INT_DAYS = Enum.INT_DAYS;
        static final int INT_HOURS = Enum.INT_HOURS;
        static final int INT_MICROSECONDS = Enum.INT_MICROSECONDS;
        static final int INT_MILLISECONDS = Enum.INT_MILLISECONDS;
        static final int INT_MINUTES = Enum.INT_MINUTES;
        static final int INT_SECONDS = Enum.INT_SECONDS;
        static final int INT_NANOSECONDS = Enum.INT_NANOSECONDS;
        
        /**
         * Enumeration value class for org.openhie.openempi.configuration.xml.ScheduledTask$TimeUnit.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_DAYS
         * Enum.forString(s); // returns the enum value for a string
         * Enum.forInt(i); // returns the enum value for an int
         * </pre>
         * Enumeration objects are immutable singleton objects that
         * can be compared using == object equality. They have no
         * public constructor. See the constants defined within this
         * class for all the valid values.
         */
        static final class Enum extends org.apache.xmlbeans.StringEnumAbstractBase
        {
            /**
             * Returns the enum value for a string, or null if none.
             */
            public static Enum forString(java.lang.String s)
                { return (Enum)table.forString(s); }
            /**
             * Returns the enum value corresponding to an int, or null if none.
             */
            public static Enum forInt(int i)
                { return (Enum)table.forInt(i); }
            
            private Enum(java.lang.String s, int i)
                { super(s, i); }
            
            static final int INT_DAYS = 1;
            static final int INT_HOURS = 2;
            static final int INT_MICROSECONDS = 3;
            static final int INT_MILLISECONDS = 4;
            static final int INT_MINUTES = 5;
            static final int INT_SECONDS = 6;
            static final int INT_NANOSECONDS = 7;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("days", INT_DAYS),
                    new Enum("hours", INT_HOURS),
                    new Enum("microseconds", INT_MICROSECONDS),
                    new Enum("milliseconds", INT_MILLISECONDS),
                    new Enum("minutes", INT_MINUTES),
                    new Enum("seconds", INT_SECONDS),
                    new Enum("nanoseconds", INT_NANOSECONDS),
                }
            );
            private static final long serialVersionUID = 1L;
            private java.lang.Object readResolve() { return forInt(intValue()); } 
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit newValue(java.lang.Object obj) {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit) type.newValue( obj ); }
            
            public static org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit newInstance() {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.openhie.openempi.configuration.xml.ScheduledTask.TimeUnit) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openhie.openempi.configuration.xml.ScheduledTask newInstance() {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.ScheduledTask parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.ScheduledTask) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
