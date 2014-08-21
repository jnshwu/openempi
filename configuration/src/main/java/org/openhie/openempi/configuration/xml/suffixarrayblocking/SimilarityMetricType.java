/*
 * XML Type:  similarity-metric-type
 * Namespace: http://configuration.openempi.openhie.org/suffix-array-blocking
 * Java type: org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType
 *
 * Automatically generated - do not modify.
 */
package org.openhie.openempi.configuration.xml.suffixarrayblocking;


/**
 * An XML similarity-metric-type(@http://configuration.openempi.openhie.org/suffix-array-blocking).
 *
 * This is an atomic type that is a restriction of org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.
 */
public interface SimilarityMetricType extends org.apache.xmlbeans.XmlString
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SimilarityMetricType.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sFA072E9AE4BE4D55B7C5CC50942AC27F").resolveHandle("similaritymetrictype6dc3type");
    
    org.apache.xmlbeans.StringEnumAbstractBase enumValue();
    void set(org.apache.xmlbeans.StringEnumAbstractBase e);
    
    static final Enum EXACT = Enum.forString("Exact");
    static final Enum JARO = Enum.forString("Jaro");
    static final Enum JARO_WINKLER = Enum.forString("JaroWinkler");
    static final Enum JARO_WINKLER_V_2 = Enum.forString("JaroWinklerV2");
    static final Enum LEVENSHTEIN = Enum.forString("Levenshtein");
    static final Enum NEEDLEMAN_WUNSCH = Enum.forString("NeedlemanWunsch");
    static final Enum SMITH_WATERMAN = Enum.forString("SmithWaterman");
    static final Enum MONGE_ELKAN = Enum.forString("MongeElkan");
    static final Enum GOTOH = Enum.forString("Gotoh");
    static final Enum GOTOH_WINDOWED_AFFINE = Enum.forString("GotohWindowedAffine");
    static final Enum BLOCK = Enum.forString("Block");
    static final Enum SOUNDEX = Enum.forString("Soundex");
    static final Enum EUCLIDEAN = Enum.forString("Euclidean");
    static final Enum Q_GRAMS = Enum.forString("QGrams");
    static final Enum JACCARD_STRING = Enum.forString("JaccardString");
    static final Enum COSINE = Enum.forString("Cosine");
    static final Enum DICE_STRING = Enum.forString("DiceString");
    static final Enum MATCHING = Enum.forString("Matching");
    static final Enum OVERLAP = Enum.forString("Overlap");
    
    static final int INT_EXACT = Enum.INT_EXACT;
    static final int INT_JARO = Enum.INT_JARO;
    static final int INT_JARO_WINKLER = Enum.INT_JARO_WINKLER;
    static final int INT_JARO_WINKLER_V_2 = Enum.INT_JARO_WINKLER_V_2;
    static final int INT_LEVENSHTEIN = Enum.INT_LEVENSHTEIN;
    static final int INT_NEEDLEMAN_WUNSCH = Enum.INT_NEEDLEMAN_WUNSCH;
    static final int INT_SMITH_WATERMAN = Enum.INT_SMITH_WATERMAN;
    static final int INT_MONGE_ELKAN = Enum.INT_MONGE_ELKAN;
    static final int INT_GOTOH = Enum.INT_GOTOH;
    static final int INT_GOTOH_WINDOWED_AFFINE = Enum.INT_GOTOH_WINDOWED_AFFINE;
    static final int INT_BLOCK = Enum.INT_BLOCK;
    static final int INT_SOUNDEX = Enum.INT_SOUNDEX;
    static final int INT_EUCLIDEAN = Enum.INT_EUCLIDEAN;
    static final int INT_Q_GRAMS = Enum.INT_Q_GRAMS;
    static final int INT_JACCARD_STRING = Enum.INT_JACCARD_STRING;
    static final int INT_COSINE = Enum.INT_COSINE;
    static final int INT_DICE_STRING = Enum.INT_DICE_STRING;
    static final int INT_MATCHING = Enum.INT_MATCHING;
    static final int INT_OVERLAP = Enum.INT_OVERLAP;
    
    /**
     * Enumeration value class for org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType.
     * These enum values can be used as follows:
     * <pre>
     * enum.toString(); // returns the string value of the enum
     * enum.intValue(); // returns an int value, useful for switches
     * // e.g., case Enum.INT_EXACT
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
        
        static final int INT_EXACT = 1;
        static final int INT_JARO = 2;
        static final int INT_JARO_WINKLER = 3;
        static final int INT_JARO_WINKLER_V_2 = 4;
        static final int INT_LEVENSHTEIN = 5;
        static final int INT_NEEDLEMAN_WUNSCH = 6;
        static final int INT_SMITH_WATERMAN = 7;
        static final int INT_MONGE_ELKAN = 8;
        static final int INT_GOTOH = 9;
        static final int INT_GOTOH_WINDOWED_AFFINE = 10;
        static final int INT_BLOCK = 11;
        static final int INT_SOUNDEX = 12;
        static final int INT_EUCLIDEAN = 13;
        static final int INT_Q_GRAMS = 14;
        static final int INT_JACCARD_STRING = 15;
        static final int INT_COSINE = 16;
        static final int INT_DICE_STRING = 17;
        static final int INT_MATCHING = 18;
        static final int INT_OVERLAP = 19;
        
        public static final org.apache.xmlbeans.StringEnumAbstractBase.Table table =
            new org.apache.xmlbeans.StringEnumAbstractBase.Table
        (
            new Enum[]
            {
                new Enum("Exact", INT_EXACT),
                new Enum("Jaro", INT_JARO),
                new Enum("JaroWinkler", INT_JARO_WINKLER),
                new Enum("JaroWinklerV2", INT_JARO_WINKLER_V_2),
                new Enum("Levenshtein", INT_LEVENSHTEIN),
                new Enum("NeedlemanWunsch", INT_NEEDLEMAN_WUNSCH),
                new Enum("SmithWaterman", INT_SMITH_WATERMAN),
                new Enum("MongeElkan", INT_MONGE_ELKAN),
                new Enum("Gotoh", INT_GOTOH),
                new Enum("GotohWindowedAffine", INT_GOTOH_WINDOWED_AFFINE),
                new Enum("Block", INT_BLOCK),
                new Enum("Soundex", INT_SOUNDEX),
                new Enum("Euclidean", INT_EUCLIDEAN),
                new Enum("QGrams", INT_Q_GRAMS),
                new Enum("JaccardString", INT_JACCARD_STRING),
                new Enum("Cosine", INT_COSINE),
                new Enum("DiceString", INT_DICE_STRING),
                new Enum("Matching", INT_MATCHING),
                new Enum("Overlap", INT_OVERLAP),
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
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType newValue(java.lang.Object obj) {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) type.newValue( obj ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType newInstance() {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (org.openhie.openempi.configuration.xml.suffixarrayblocking.SimilarityMetricType) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
