//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.07.29 at 11:45:22 AM CEST 
//


package com.thalesgroup.optet.analysis.checkstyle.jaxb.checkstyle;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the firstplugin.jaxb.checkstyle package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: firstplugin.jaxb.checkstyle
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Checkstyle }
     * 
     */
    public Checkstyle createCheckstyle() {
        return new Checkstyle();
    }

    /**
     * Create an instance of {@link Checkstyle.File }
     * 
     */
    public Checkstyle.File createCheckstyleFile() {
        return new Checkstyle.File();
    }

    /**
     * Create an instance of {@link Checkstyle.File.Error }
     * 
     */
    public Checkstyle.File.Error createCheckstyleFileError() {
        return new Checkstyle.File.Error();
    }

}
