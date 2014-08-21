/*
 * An XML document type.
 * Localname: mpi-config
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.MpiConfigDocument
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml;


/**
 * A document containing one mpi-config(@http://configuration.openempi.openhie.org/mpiconfig) element.
 *
 * This is a complex type.
 */
public interface MpiConfigDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MpiConfigDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("mpiconfigf192doctype");
    
    /**
     * Gets the "mpi-config" element
     */
    org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig getMpiConfig();
    
    /**
     * Sets the "mpi-config" element
     */
    void setMpiConfig(org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig mpiConfig);
    
    /**
     * Appends and returns a new empty "mpi-config" element
     */
    org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig addNewMpiConfig();
    
    /**
     * An XML mpi-config(@http://configuration.openempi.openhie.org/mpiconfig).
     *
     * This is a complex type.
     */
    public interface MpiConfig extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MpiConfig.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("mpiconfigabcdelemtype");
        
        /**
         * Gets the "global-identifier" element
         */
        org.openhie.openempi.configuration.xml.GlobalIdentifier getGlobalIdentifier();
        
        /**
         * True if has "global-identifier" element
         */
        boolean isSetGlobalIdentifier();
        
        /**
         * Sets the "global-identifier" element
         */
        void setGlobalIdentifier(org.openhie.openempi.configuration.xml.GlobalIdentifier globalIdentifier);
        
        /**
         * Appends and returns a new empty "global-identifier" element
         */
        org.openhie.openempi.configuration.xml.GlobalIdentifier addNewGlobalIdentifier();
        
        /**
         * Unsets the "global-identifier" element
         */
        void unsetGlobalIdentifier();
        
        /**
         * Gets the "scheduled-tasks" element
         */
        org.openhie.openempi.configuration.xml.ScheduledTasks getScheduledTasks();
        
        /**
         * True if has "scheduled-tasks" element
         */
        boolean isSetScheduledTasks();
        
        /**
         * Sets the "scheduled-tasks" element
         */
        void setScheduledTasks(org.openhie.openempi.configuration.xml.ScheduledTasks scheduledTasks);
        
        /**
         * Appends and returns a new empty "scheduled-tasks" element
         */
        org.openhie.openempi.configuration.xml.ScheduledTasks addNewScheduledTasks();
        
        /**
         * Unsets the "scheduled-tasks" element
         */
        void unsetScheduledTasks();
        
        /**
         * Gets the "file-loader-configuration" element
         */
        org.openhie.openempi.configuration.xml.FileLoaderConfigurationType getFileLoaderConfiguration();
        
        /**
         * Sets the "file-loader-configuration" element
         */
        void setFileLoaderConfiguration(org.openhie.openempi.configuration.xml.FileLoaderConfigurationType fileLoaderConfiguration);
        
        /**
         * Appends and returns a new empty "file-loader-configuration" element
         */
        org.openhie.openempi.configuration.xml.FileLoaderConfigurationType addNewFileLoaderConfiguration();
        
        /**
         * Gets the "custom-fields" element
         */
        org.openhie.openempi.configuration.xml.CustomFields getCustomFields();
        
        /**
         * True if has "custom-fields" element
         */
        boolean isSetCustomFields();
        
        /**
         * Sets the "custom-fields" element
         */
        void setCustomFields(org.openhie.openempi.configuration.xml.CustomFields customFields);
        
        /**
         * Appends and returns a new empty "custom-fields" element
         */
        org.openhie.openempi.configuration.xml.CustomFields addNewCustomFields();
        
        /**
         * Unsets the "custom-fields" element
         */
        void unsetCustomFields();
        
        /**
         * Gets the "blocking-configuration" element
         */
        org.openhie.openempi.configuration.xml.BlockingConfigurationType getBlockingConfiguration();
        
        /**
         * Sets the "blocking-configuration" element
         */
        void setBlockingConfiguration(org.openhie.openempi.configuration.xml.BlockingConfigurationType blockingConfiguration);
        
        /**
         * Appends and returns a new empty "blocking-configuration" element
         */
        org.openhie.openempi.configuration.xml.BlockingConfigurationType addNewBlockingConfiguration();
        
        /**
         * Gets the "matching-configuration" element
         */
        org.openhie.openempi.configuration.xml.MatchingConfigurationType getMatchingConfiguration();
        
        /**
         * Sets the "matching-configuration" element
         */
        void setMatchingConfiguration(org.openhie.openempi.configuration.xml.MatchingConfigurationType matchingConfiguration);
        
        /**
         * Appends and returns a new empty "matching-configuration" element
         */
        org.openhie.openempi.configuration.xml.MatchingConfigurationType addNewMatchingConfiguration();
        
        /**
         * Gets the "single-best-record" element
         */
        org.openhie.openempi.configuration.xml.SingleBestRecordType getSingleBestRecord();
        
        /**
         * True if has "single-best-record" element
         */
        boolean isSetSingleBestRecord();
        
        /**
         * Sets the "single-best-record" element
         */
        void setSingleBestRecord(org.openhie.openempi.configuration.xml.SingleBestRecordType singleBestRecord);
        
        /**
         * Appends and returns a new empty "single-best-record" element
         */
        org.openhie.openempi.configuration.xml.SingleBestRecordType addNewSingleBestRecord();
        
        /**
         * Unsets the "single-best-record" element
         */
        void unsetSingleBestRecord();
        
        /**
         * Gets the "admin-configuration" element
         */
        org.openhie.openempi.configuration.xml.AdminConfiguration getAdminConfiguration();
        
        /**
         * Sets the "admin-configuration" element
         */
        void setAdminConfiguration(org.openhie.openempi.configuration.xml.AdminConfiguration adminConfiguration);
        
        /**
         * Appends and returns a new empty "admin-configuration" element
         */
        org.openhie.openempi.configuration.xml.AdminConfiguration addNewAdminConfiguration();
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig newInstance() {
              return (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (org.openhie.openempi.configuration.xml.MpiConfigDocument.MpiConfig) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument newInstance() {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.MpiConfigDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.MpiConfigDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
