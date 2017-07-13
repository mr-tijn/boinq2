/* CVS $Id: $ */
package com.genohm.boinq.generated.vocabularies; 
import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
 
/**
 * Vocabulary definitions from file:/Users/martijn/Documents/workspace-sts-3.8.3.RELEASE/boinq/ontologies/format.owl 
 * @author Auto-generated by schemagen on 12 jun 2017 13:42 
 */
public class FormatVocab {
    /** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.boinq.org/iri/ontologies/format#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final ObjectProperty defines = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/format#defines" );
    
    /** <p>points to CIGAR value as defined by:</p> */
    public static final DatatypeProperty hasCIGAR = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasCIGAR" );
    
    /** <p>points to FLAG value as defined by:</p> */
    public static final DatatypeProperty hasFLAG = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasFLAG" );
    
    /** <p>points to MAPQ value as defined by:</p> */
    public static final DatatypeProperty hasMAPQ = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasMAPQ" );
    
    /** <p>points to PNEXT value as defined by:</p> */
    public static final DatatypeProperty hasPNEXT = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasPNEXT" );
    
    /** <p>points to POS value as defined by:</p> */
    public static final DatatypeProperty hasPOS = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasPOS" );
    
    /** <p>points to QNAME value as defined by:</p> */
    public static final DatatypeProperty hasQNAME = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasQNAME" );
    
    /** <p>points to QUAL as defined by:</p> */
    public static final DatatypeProperty hasQUAL = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasQUAL" );
    
    /** <p>points to RNAME value as defined by:</p> */
    public static final DatatypeProperty hasRNAME = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasRNAME" );
    
    /** <p>points to RNEXT value as defined by</p> */
    public static final DatatypeProperty hasRNEXT = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasRNEXT" );
    
    /** <p>points to SEQ as defined by:</p> */
    public static final DatatypeProperty hasSEQ = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasSEQ" );
    
    /** <p>points to TLEN value as defined by:</p> */
    public static final DatatypeProperty hasTLEN = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/format#hasTLEN" );
    
    public static final OntClass BED_Entry = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#BED_Entry" );
    
    public static final OntClass CIGAR = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#CIGAR" );
    
    public static final OntClass FLAG = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#FLAG" );
    
    public static final OntClass GFF_Entry = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#GFF_Entry" );
    
    public static final OntClass Gap = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#Gap" );
    
    public static final OntClass MAPQ = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#MAPQ" );
    
    public static final OntClass PNEXT = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#PNEXT" );
    
    public static final OntClass POS = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#POS" );
    
    public static final OntClass QNAME = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#QNAME" );
    
    public static final OntClass QUAL = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#QUAL" );
    
    public static final OntClass RGBblue = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RGBblue" );
    
    public static final OntClass RGBcolor = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RGBcolor" );
    
    public static final OntClass RGBgreen = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RGBgreen" );
    
    public static final OntClass RGBred = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RGBred" );
    
    public static final OntClass RNAME = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RNAME" );
    
    public static final OntClass RNEXT = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#RNEXT" );
    
    /** <p>One line found back in a SAM/BAM file not including the header</p> */
    public static final OntClass SAM_Entry = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#SAM_Entry" );
    
    public static final OntClass SEQ = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#SEQ" );
    
    public static final OntClass TLEN = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#TLEN" );
    
    public static final OntClass Target = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#Target" );
    
    public static final OntClass VCF_Entry = m_model.createClass( "http://www.boinq.org/iri/ontologies/format#VCF_Entry" );
    
// INDIVIDUALS
}
