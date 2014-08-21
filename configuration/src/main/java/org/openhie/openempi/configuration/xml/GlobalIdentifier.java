/*
 * XML Type:  global-identifier
 * Namespace: http://configuration.openempi.openhie.org/mpiconfig
 * Java type: org.openhie.openempi.configuration.xml.GlobalIdentifier
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml;


/**
 * An XML global-identifier(@http://configuration.openempi.openhie.org/mpiconfig).
 *
 * This is a complex type.
 */
public interface GlobalIdentifier extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(GlobalIdentifier.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("globalidentifier7e5atype");
    
    /**
     * Gets the "assign-global-identifier" element
     */
    boolean getAssignGlobalIdentifier();
    
    /**
     * Gets (as xml) the "assign-global-identifier" element
     */
    org.apache.xmlbeans.XmlBoolean xgetAssignGlobalIdentifier();
    
    /**
     * Sets the "assign-global-identifier" element
     */
    void setAssignGlobalIdentifier(boolean assignGlobalIdentifier);
    
    /**
     * Sets (as xml) the "assign-global-identifier" element
     */
    void xsetAssignGlobalIdentifier(org.apache.xmlbeans.XmlBoolean assignGlobalIdentifier);
    
    /**
     * Gets the "identifier-domain-name" element
     */
    java.lang.String getIdentifierDomainName();
    
    /**
     * Gets (as xml) the "identifier-domain-name" element
     */
    org.apache.xmlbeans.XmlString xgetIdentifierDomainName();
    
    /**
     * Sets the "identifier-domain-name" element
     */
    void setIdentifierDomainName(java.lang.String identifierDomainName);
    
    /**
     * Sets (as xml) the "identifier-domain-name" element
     */
    void xsetIdentifierDomainName(org.apache.xmlbeans.XmlString identifierDomainName);
    
    /**
     * Gets the "identifier-domain-description" element
     */
    java.lang.String getIdentifierDomainDescription();
    
    /**
     * Gets (as xml) the "identifier-domain-description" element
     */
    org.apache.xmlbeans.XmlString xgetIdentifierDomainDescription();
    
    /**
     * Sets the "identifier-domain-description" element
     */
    void setIdentifierDomainDescription(java.lang.String identifierDomainDescription);
    
    /**
     * Sets (as xml) the "identifier-domain-description" element
     */
    void xsetIdentifierDomainDescription(org.apache.xmlbeans.XmlString identifierDomainDescription);
    
    /**
     * Gets the "namespace-identifier" element
     */
    java.lang.String getNamespaceIdentifier();
    
    /**
     * Gets (as xml) the "namespace-identifier" element
     */
    org.apache.xmlbeans.XmlString xgetNamespaceIdentifier();
    
    /**
     * Sets the "namespace-identifier" element
     */
    void setNamespaceIdentifier(java.lang.String namespaceIdentifier);
    
    /**
     * Sets (as xml) the "namespace-identifier" element
     */
    void xsetNamespaceIdentifier(org.apache.xmlbeans.XmlString namespaceIdentifier);
    
    /**
     * Gets the "universal-identifier" element
     */
    java.lang.String getUniversalIdentifier();
    
    /**
     * Gets (as xml) the "universal-identifier" element
     */
    org.apache.xmlbeans.XmlString xgetUniversalIdentifier();
    
    /**
     * Sets the "universal-identifier" element
     */
    void setUniversalIdentifier(java.lang.String universalIdentifier);
    
    /**
     * Sets (as xml) the "universal-identifier" element
     */
    void xsetUniversalIdentifier(org.apache.xmlbeans.XmlString universalIdentifier);
    
    /**
     * Gets the "universal-identifier-type" element
     */
    java.lang.String getUniversalIdentifierType();
    
    /**
     * Gets (as xml) the "universal-identifier-type" element
     */
    org.apache.xmlbeans.XmlString xgetUniversalIdentifierType();
    
    /**
     * Sets the "universal-identifier-type" element
     */
    void setUniversalIdentifierType(java.lang.String universalIdentifierType);
    
    /**
     * Sets (as xml) the "universal-identifier-type" element
     */
    void xsetUniversalIdentifierType(org.apache.xmlbeans.XmlString universalIdentifierType);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier newInstance() {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.GlobalIdentifier parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.GlobalIdentifier) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
