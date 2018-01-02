package org.boinq.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;


public class SchemagenByLabel extends jena.schemagen {
	
	// adapted schemagen for having meaningful names in code
	// use with caution
	// still needs manual check
	
	public static void main( String... args ) {
		try {
			new SchemagenByLabel().go( args );
		}
		catch (SchemagenException e) {
			System.err.println( "Schemagen failed to run:" );
			System.err.println( e.getMessage() );

			if (e.getCause() != null) {
				System.err.println( "Caused by: " + e.getCause().getMessage() );
			}

			System.exit( 1 );
		}
	}
	
	@Override
	protected void go(String[] args) {
		go( new SchemagenOptionsImpl( args ) );
	}

	@Override
	protected void go(SchemagenOptions options) {
        m_options = options;


        // check for user requesting help
        if (m_options.hasHelpOption()) {
            usage();
        }


        // got the configuration, now we can begin processing
        processInput();
	}

	@Override
	protected void processInput() {
	       addIncludes();
	        determineLanguage();
	        selectInput();
	        selectOutput();
	        setGlobalReplacements();

	        processHeader();
 	       addMapIncludes();
	        writeClassDeclaration();
	        writeInitialDeclarations();
	        writeProperties();
	        writeClasses();
	        writeIndividuals();
	        writeDatatypes();
	        writeClassClose();
	        processFooter();
	        closeOutput();
	}
	
	protected void addMapIncludes() {
		writeln( 0, "import java.util.Map;" );
		writeln( 0, "import java.util.HashMap;" );	
	}

	@Override
    protected void writeDatatypeProperties() {
        String template = m_options.hasPropTemplateOption() ?  m_options.getPropTemplateOption() : DEFAULT_TEMPLATE;

        if (!m_options.hasLangRdfsOption()) {
            for (Iterator<? extends RDFNode> i = sorted( m_source.listDatatypeProperties() ); i.hasNext(); ) {
                writeValueByLabel( (Resource) i.next(), template, "DatatypeProperty", "createDatatypeProperty", "_PROP" );
            }
        }
    }

	@Override
	protected void writeAnnotationProperties() {
        String template = m_options.hasPropTemplateOption() ?  m_options.getPropTemplateOption() : DEFAULT_TEMPLATE;

        if (!m_options.hasLangRdfsOption()) {
            for (Iterator<? extends RDFNode> i = sorted( m_source.listAnnotationProperties() ); i.hasNext(); ) {
                writeValueByLabel( (Resource) i.next(), template, "AnnotationProperty", "createAnnotationProperty", "_PROP" );
            }
        }
    }

    @Override
    protected void writeRDFProperties( boolean useOntProperty ) {
        String template = m_options.hasPropTemplateOption() ?  m_options.getPropTemplateOption() : DEFAULT_TEMPLATE;
        String propType = useOntProperty ? "OntProperty" : "Property";

        // select the appropriate properties based on the language choice
        Resource[] props;
        if (m_options.hasLangOwlOption()) {
            props = new Resource[] {OWL.ObjectProperty, OWL.DatatypeProperty, RDF.Property};
        }
        else {
            props = new Resource[] {RDF.Property};
        }

        // collect the properties to be written
        List<Resource> propertyResources = new ArrayList<>();
        for ( Resource prop : props )
        {
            for ( StmtIterator i = m_source.listStatements( null, RDF.type, prop ); i.hasNext(); )
            {
                propertyResources.add( i.nextStatement().getSubject() );
            }
        }

        // now write the properties
        for (Iterator<? extends RDFNode> i = sorted( propertyResources ); i.hasNext(); ) {
            writeValueByLabel( (Resource) i.next(), template, propType, "create" + propType, "_PROP" );
        }
    }

    @Override
    protected void writeObjectProperties() {
        String template = m_options.hasPropTemplateOption() ?  m_options.getPropTemplateOption() : DEFAULT_TEMPLATE;

        if (!m_options.hasLangRdfsOption()) {
            for (Iterator<? extends RDFNode> i = sorted( m_source.listObjectProperties() ); i.hasNext(); ) {
                writeValueByLabel( (Resource) i.next(), template, "ObjectProperty", "createObjectProperty", "_PROP" );
            }
        }
    }

	@Override
	protected void writeProperties() {
        if (m_options.hasNopropertiesOption()) {
            return;
        }

        if (m_options.hasPropertySectionOption()) {
            writeln( 0, m_options.getPropertySectionOption());
        }

        if (useOntology()) {
            writeObjectProperties();
            writeDatatypeProperties();
            writeAnnotationProperties();

            // we also write out the RDF properties, to mop up any props that are not stated as
            // object, datatype or annotation properties
            writeRDFProperties( true );
        }
        else {
            writeRDFProperties( false );
        }
	}
	

	@Override
	protected void writeClasses() {
        if (m_options.hasNoclassesOption()) {
            return;
        }

        if (m_options.hasClassSectionOption()) {
            writeln( 0, m_options.getClassSectionOption());
        }

        if (useOntology()) {
            writeOntClasses();
        }
        else {
            writeRDFClasses();
        }
    }

	@Override
	protected void writeOntClasses() {
        String template = m_options.hasClassTemplateOption() ?  m_options.getClassTemplateOption() : DEFAULT_TEMPLATE;

        for (Iterator<? extends RDFNode> i = sorted( m_source.listClasses() ); i.hasNext(); ) {
            writeValueByLabel( (Resource) i.next(), template, "OntClass", "createClass", "_CLASS" );
        }
        
		writeln(1, substitute("public static Map<String, OntClass> OntClassesByLabel = new HashMap<String, OntClass>();"));
        int initializers = 1;
        int entries = 1;
        // need to split static initializer to avoid the 65 Kb limit
        writeStaticMapPre(initializers++);
        for (Iterator<? extends RDFNode> i = sorted( m_source.listClasses() ); i.hasNext(); ) {
            initializers += writeMapEntry( (Resource) i.next(), template, "OntClass", "createClass", "_CLASS", entries++, initializers );
        }
        writeStaticMapPost();
        writeInit(initializers);
	}
	
	protected void writeStaticMapPre(int i) {
		writeln(1, "protected static void init" + i + "() {");
	}
	
	protected void writeStaticMapPost() {
		writeln(1,"}");
	}

	protected void writeInit(int initializers) {
		writeln(1, "static {");
		for (int i = 1; i < initializers; i++) {
			writeln(2, "init" + i + "();");
		}
		writeln(1, "}");
	}
	
	protected int writeMapEntry(Resource r, String template, String valueClass, String creator, String disambiguator, int entrycount, int initializers) {
		if (!filter(r)) {
			String valName = asLegalJavaID(r.getProperty(RDFS.label).getObject().asLiteral().getString(), false);
	        if (valName.equals("true") || valName.equals("false")) {
	        	valName = valName+"_";
	        }
	        addReplacementPattern( "valname", valName);
	        addReplacementPattern( "uppervalname", valName.toUpperCase());
			writeln(2, substitute("OntClassesByLabel.put(\"%uppervalname%\", %valname%);"));
	        pop(2);
        	if (entrycount % 500 == 0) {
                writeStaticMapPost();
                writeStaticMapPre(initializers);
                return 1;
        	}
		}
		return 0;
	}

	protected void writeValueByLabel(Resource r, String template, String valueClass,
			String creator, String disambiguator) {
		if (r.getProperty(RDFS.label) != null) {
			writeLabel(r,template, valueClass, creator, disambiguator);
		} else {
			super.writeValue(r, template, valueClass, creator, disambiguator);
		}
	}
	
	protected void writeLabel(Resource r, String template, String valueClass,
			String creator, String disambiguator) {
        if (!filter( r )) {
            if (!noComments()  &&  hasComment( r )) {
                writeln( 1, formatComment( getComment( r ) ) );
            }

        // push the local bindings for the substitution onto the stack
        addReplacementPattern( "valuri", r.getURI() );
        addReplacementPattern( "valname", getValueName(r.getProperty(RDFS.label).getObject().asLiteral().getString(), disambiguator));
        addReplacementPattern( "valclass", valueClass );
        addReplacementPattern( "valcreator", creator );

        // write out the value
        writeln( 1, substitute( template ) );
        writeln( 1 );

        // pop the local replacements off the stack
        pop( 4 );
    }
	}
	
    protected String getValueName( String name, String disambiguator ) {

        // must be legal java
        name = asLegalJavaID( name, false );
        // quick hack to fix absence of true and false as java keywords
        if (name.equals("true") || name.equals("false")) {
        	name = name+"_";
        }

        // must not clash with an existing name
        int attempt = 0;
        String baseName = name;
        while (m_usedNames.contains( name )) {
            name = (attempt == 0) ? (name + disambiguator) : (baseName + disambiguator + attempt);
            attempt++;
        }

        // record this name so that we don't use it again (which will stop the vocabulary from compiling)
        m_usedNames.add( name );

        return name;
    }


}


