/*
 * XML Type:  mpi-component-type
 * Namespace: http://configuration.openempi.openhie.org/mpicomponent
 * Java type: org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.mpicomponent;


/**
 * An XML mpi-component-type(@http://configuration.openempi.openhie.org/mpicomponent).
 *
 * This is a complex type.
 */
public interface MpiComponentType extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MpiComponentType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("mpicomponenttype10f5type");
    
    /**
     * Gets the "name" element
     */
    java.lang.String getName();
    
    /**
     * Gets (as xml) the "name" element
     */
    org.apache.xmlbeans.XmlString xgetName();
    
    /**
     * Sets the "name" element
     */
    void setName(java.lang.String name);
    
    /**
     * Sets (as xml) the "name" element
     */
    void xsetName(org.apache.xmlbeans.XmlString name);
    
    /**
     * Gets the "description" element
     */
    java.lang.String getDescription();
    
    /**
     * Gets (as xml) the "description" element
     */
    org.apache.xmlbeans.XmlString xgetDescription();
    
    /**
     * True if has "description" element
     */
    boolean isSetDescription();
    
    /**
     * Sets the "description" element
     */
    void setDescription(java.lang.String description);
    
    /**
     * Sets (as xml) the "description" element
     */
    void xsetDescription(org.apache.xmlbeans.XmlString description);
    
    /**
     * Unsets the "description" element
     */
    void unsetDescription();
    
    /**
     * Gets the "extensions" element
     */
    org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType getExtensions();
    
    /**
     * Sets the "extensions" element
     */
    void setExtensions(org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType extensions);
    
    /**
     * Appends and returns a new empty "extensions" element
     */
    org.openhie.openempi.configuration.xml.mpicomponent.ExtensionsType addNewExtensions();
    
    /**
     * Gets the "component-type" attribute
     */
    org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType.Enum getComponentType();
    
    /**
     * Gets (as xml) the "component-type" attribute
     */
    org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType xgetComponentType();
    
    /**
     * Sets the "component-type" attribute
     */
    void setComponentType(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType.Enum componentType);
    
    /**
     * Sets (as xml) the "component-type" attribute
     */
    void xsetComponentType(org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType componentType);
    
    /**
     * An XML component-type(@).
     *
     * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType$ComponentType.
     */
    public interface ComponentType extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ComponentType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("componenttype231fattrtype");
        
        org.apache.xmlbeans.StringEnumAbstractBase enumValue();
        void set(org.apache.xmlbeans.StringEnumAbstractBase e);
        
        static final Enum BLOCKING = Enum.forString("blocking");
        static final Enum MATCHING = Enum.forString("matching");
        static final Enum FILELOADER = Enum.forString("fileloader");
        
        static final int INT_BLOCKING = Enum.INT_BLOCKING;
        static final int INT_MATCHING = Enum.INT_MATCHING;
        static final int INT_FILELOADER = Enum.INT_FILELOADER;
        
        /**
         * Enumeration value class for org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType$ComponentType.
         * These enum values can be used as follows:
         * <pre>
         * enum.toString(); // returns the string value of the enum
         * enum.intValue(); // returns an int value, useful for switches
         * // e.g., case Enum.INT_BLOCKING
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
            
            static final int INT_BLOCKING = 1;
            static final int INT_MATCHING = 2;
            static final int INT_FILELOADER = 3;
            
            public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
                new org.apache.xmlbeans.StringEnumAbstractBase.Table
            (
                new Enum[]
                {
                    new Enum("blocking", INT_BLOCKING),
                    new Enum("matching", INT_MATCHING),
                    new Enum("fileloader", INT_FILELOADER),
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
            public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType newValue(java.lang.Object obj) {
              return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType) type.newValue( obj ); }
            
            public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType newInstance() {
              return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType.ComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType newInstance() {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.mpicomponent.MpiComponentType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
