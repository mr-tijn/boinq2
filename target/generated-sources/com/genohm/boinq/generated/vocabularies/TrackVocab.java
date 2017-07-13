/* CVS $Id: $ */
package com.genohm.boinq.generated.vocabularies; 
import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
 
/**
 * Vocabulary definitions from file:/Users/martijn/Documents/workspace-sts-3.8.3.RELEASE/boinq/ontologies/track.owl 
 * @author Auto-generated by schemagen on 12 jun 2017 13:42 
 */
public class TrackVocab {
    /** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.boinq.org/iri/ontologies/track#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    public static final ObjectProperty connector = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#connector" );
    
    public static final ObjectProperty endpoint = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#endpoint" );
    
    public static final ObjectProperty entry = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#entry" );
    
    public static final ObjectProperty hasAttribute = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#hasAttribute" );
    
    public static final ObjectProperty holds = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#holds" );
    
    public static final ObjectProperty originalReference = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#originalReference" );
    
    public static final ObjectProperty partOf = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#partOf" );
    
    public static final ObjectProperty provides = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#provides" );
    
    public static final ObjectProperty references = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#references" );
    
    public static final ObjectProperty supports = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#supports" );
    
    public static final ObjectProperty targetReference = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#targetReference" );
    
    public static final ObjectProperty targets = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#targets" );
    
    public static final ObjectProperty uploadedBy = m_model.createObjectProperty( "http://www.boinq.org/iri/ontologies/track#uploadedBy" );
    
    public static final DatatypeProperty addPrefix = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#addPrefix" );
    
    public static final DatatypeProperty createdOn = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#createdOn" );
    
    public static final DatatypeProperty decimalValue = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#decimalValue" );
    
    public static final DatatypeProperty endpointUrl = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#endpointUrl" );
    
    public static final DatatypeProperty graphUri = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#graphUri" );
    
    public static final DatatypeProperty identifier = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#identifier" );
    
    public static final DatatypeProperty label = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#label" );
    
    /** <p>A name representing the match operator.</p> */
    public static final DatatypeProperty matchName = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#matchName" );
    
    public static final DatatypeProperty motherTerm = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#motherTerm" );
    
    /** <p>A (SPARQL 1.1) property path expression between the feature entity and the 
     *  field entity</p>
     */
    public static final DatatypeProperty pathExpression = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#pathExpression" );
    
    public static final DatatypeProperty rootTerm = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#rootTerm" );
    
    public static final DatatypeProperty sequence_length = m_model.createDatatypeProperty( "http://www.boinq.org/iri/ontologies/track#sequence_length" );
    
    public static final OntClass Attribute = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Attribute" );
    
    /** <p>A connector is a predefined path to an entity that can be connected to another 
     *  connector by referring to the same target entity.</p>
     */
    public static final OntClass Connector = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Connector" );
    
    public static final OntClass ConversionDate = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#ConversionDate" );
    
    /** <p>A Datasource is a source of information. It is usually a SPARQL Endpoint</p> */
    public static final OntClass Datasource = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Datasource" );
    
    public static final OntClass EntryCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#EntryCount" );
    
    /** <p>A FaldoDatasource that uses FALDO for referencing an assembly</p> */
    public static final OntClass FaldoDatasource = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FaldoDatasource" );
    
    public static final OntClass FaldoTrack = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FaldoTrack" );
    
    public static final OntClass FeatureCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FeatureCount" );
    
    /** <p>A SPARQLDatasource offering features.</p> */
    public static final OntClass FeatureDatasource = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FeatureDatasource" );
    
    public static final OntClass FeatureType = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FeatureType" );
    
    public static final OntClass FeatureTypeFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FeatureTypeFilter" );
    
    /** <p>Operators matching the value of a field</p> */
    public static final OntClass FieldMatchFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FieldMatchFilter" );
    
    public static final OntClass File = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#File" );
    
    public static final OntClass FileAttribute = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FileAttribute" );
    
    public static final OntClass FileExtension = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FileExtension" );
    
    public static final OntClass FileName = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FileName" );
    
    public static final OntClass Filter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Filter" );
    
    public static final OntClass FilterCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#FilterCount" );
    
    public static final OntClass HeaderBED = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#HeaderBED" );
    
    public static final OntClass HeaderGFF = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#HeaderGFF" );
    
    public static final OntClass HeaderSAM = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#HeaderSAM" );
    
    public static final OntClass HeaderVCF = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#HeaderVCF" );
    
    /** <p>An operator that filters based on the location of a feature</p> */
    public static final OntClass LocationFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#LocationFilter" );
    
    public static final OntClass Mapping = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Mapping" );
    
    /** <p>Match a decimal value</p> */
    public static final OntClass MatchDecimalFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#MatchDecimalFilter" );
    
    /** <p>Match an integer value</p> */
    public static final OntClass MatchIntegerFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#MatchIntegerFilter" );
    
    /** <p>Match a string value</p> */
    public static final OntClass MatchStringFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#MatchStringFilter" );
    
    /** <p>Match a term from a target graph</p> */
    public static final OntClass MatchTermFilter = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#MatchTermFilter" );
    
    public static final OntClass ReadCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#ReadCount" );
    
    /** <p>A representation of a reference sequence (e.g. a chromosome) supported by 
     *  the viewer</p>
     */
    public static final OntClass Reference = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Reference" );
    
    /** <p>A referenceAssembly supported by the viewer</p> */
    public static final OntClass ReferenceAssembly = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#ReferenceAssembly" );
    
    /** <p>An entry for mapping the datasources reference sequence IRI to a reference 
     *  known by the viewer</p>
     */
    public static final OntClass ReferenceMapEntry = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#ReferenceMapEntry" );
    
    /** <p>A Datasource that you can query using a SPARQL Endpoint</p> */
    public static final OntClass SPARQLDatasource = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#SPARQLDatasource" );
    
    public static final OntClass SampleCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#SampleCount" );
    
    public static final OntClass SupportedSpecies = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#SupportedSpecies" );
    
    /** <p>A SPARQL Datasource offering terms</p> */
    public static final OntClass TermDatasource = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#TermDatasource" );
    
    /** <p>A collection of features of the same type</p> */
    public static final OntClass Track = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#Track" );
    
    public static final OntClass TripleCount = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#TripleCount" );
    
    public static final OntClass User = m_model.createClass( "http://www.boinq.org/iri/ontologies/track#User" );
    
// INDIVIDUALS
}
