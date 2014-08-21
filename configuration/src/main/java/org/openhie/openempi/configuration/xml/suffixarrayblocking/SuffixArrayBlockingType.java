/*
 * XML Type:  suffix-array-blocking-type
 * Namespace: http://configuration.openempi.openhie.org/suffix-array-blocking
 * Java type: org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.suffixarrayblocking;


/**
 * An XML suffix-array-blocking-type(@http://configuration.openempi.openhie.org/suffix-array-blocking).
 *
 * This is a complex type.
 */
public interface SuffixArrayBlockingType extends org.openhie.openempi.configuration.xml.BlockingConfigurationType
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SuffixArrayBlockingType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("suffixarrayblockingtypef8d8type");
    
    /**
     * Gets the "minimum-suffix-length" element
     */
    int getMinimumSuffixLength();
    
    /**
     * Gets (as xml) the "minimum-suffix-length" element
     */
    org.apache.xmlbeans.XmlInt xgetMinimumSuffixLength();
    
    /**
     * Sets the "minimum-suffix-length" element
     */
    void setMinimumSuffixLength(int minimumSuffixLength);
    
    /**
     * Sets (as xml) the "minimum-suffix-length" element
     */
    void xsetMinimumSuffixLength(org.apache.xmlbeans.XmlInt minimumSuffixLength);
    
    /**
     * Gets the "maximum-block-size" element
     */
    int getMaximumBlockSize();
    
    /**
     * Gets (as xml) the "maximum-block-size" element
     */
    org.apache.xmlbeans.XmlInt xgetMaximumBlockSize();
    
    /**
     * Sets the "maximum-block-size" element
     */
    void setMaximumBlockSize(int maximumBlockSize);
    
    /**
     * Sets (as xml) the "maximum-block-size" element
     */
    void xsetMaximumBlockSize(org.apache.xmlbeans.XmlInt maximumBlockSize);
    
    /**
     * Gets the "blocking-rounds" element
     */
    org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds getBlockingRounds();
    
    /**
     * Sets the "blocking-rounds" element
     */
    void setBlockingRounds(org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds blockingRounds);
    
    /**
     * Appends and returns a new empty "blocking-rounds" element
     */
    org.openhie.openempi.configuration.xml.suffixarrayblocking.BlockingRounds addNewBlockingRounds();
    
    /**
     * Gets the "similarity-metric" element
     */
    org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.Enum getSimilarityMetric();
    
    /**
     * Gets (as xml) the "similarity-metric" element
     */
    org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType xgetSimilarityMetric();
    
    /**
     * Sets the "similarity-metric" element
     */
    void setSimilarityMetric(org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.Enum similarityMetric);
    
    /**
     * Sets (as xml) the "similarity-metric" element
     */
    void xsetSimilarityMetric(org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType similarityMetric);
    
    /**
     * Gets the "threshold" element
     */
    float getThreshold();
    
    /**
     * Gets (as xml) the "threshold" element
     */
    org.apache.xmlbeans.XmlFloat xgetThreshold();
    
    /**
     * Sets the "threshold" element
     */
    void setThreshold(float threshold);
    
    /**
     * Sets (as xml) the "threshold" element
     */
    void xsetThreshold(org.apache.xmlbeans.XmlFloat threshold);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType newInstance() {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SuffixArrayBlockingType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
