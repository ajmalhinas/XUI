package org.purnamaproject.xui.impl;

/**
 * @(#)XUIBuilderImpl.java  0.5 18/08/2003
 *
 * The Purnama Project XUI (XML-based User Interface) API is an set of program
 * calls that utilize the XUI tagset and perform the task of creating a user
 * interface. The Purnama version of this API is specific using Java as the platform
 * but other XUI APIs may use other libraries and platforms. The Purnama XUI API
 * supports the creation of Swing components, adding, deleting and laying out
 * components. It also supports dynamic binding of business logic to the user
 * interface.
 *
 * Copyright (c) 2003 Arron Ferguson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * For more information, please contact Arron Ferguson at:
 * e-mail: arron_ferguson@bcit.ca
 * snail-mail: SW2 - 124, 3700 Willingdon Avenue, Burnaby, British Columbia, Canada, V5G 3H2
 * Web: http://xml.bcit.ca/PurnamaProject/
 *
 */


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;


import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import javax.xml.validation.Validator;
import javax.xml.transform.stream.StreamSource;

import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.XUI;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUIParseException;
import org.purnamaproject.xui.XUIBuilder;
import org.purnamaproject.xui.XUIValidationException;

/**
 * The class the implements XUIBuilder.
 *
 * @see org.purnamaproject.xui.XUIBuilder XUIBuilder
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class XUIBuilderImpl extends DefaultHandler implements XUIBuilder
{
    /**
     * The XUI object returned once the document has finished loading.
     */
    private XUI xui = null;

    /**
     * The XML Parser (SAX).
     */
    private XMLReader reader;

    /**
     * Determines whether or not the first node has been read in.
     */
    private boolean firstTime;

    /**
     * The stack used to store the nodes coming in.
     */
    private Stack nodeStack;

    /**
     * The temporary namespaces that are found as the document is parsed.
     */
    private Hashtable namespaces;

    /**
     * The level is the level within the XML document hierarchy. Levels always
     * start at 0 and go upwards. Negative values are ignored.
     */
    private int currentLevel;

    /**
     * determines whether or not the end of the document has been reached.
     */
    private boolean endOfDocumentReached = false;

    /**
     * The root node. This node will be handed off to the XUI object.
     */
    private XUINode root = null;

    /**
     * Default constructor.
     */
    public XUIBuilderImpl()
    {
        ;
    }

    /**
     * Parses the file based on the file name.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>String</code>.
     * @throws XUIParseException if the XUI document is not valid against the
     * Purnama Project XUI schema.
     * @throws IOException if the document cannot be found or if permissions
     * are not allowed.
     *
     */
    public void parse(String fileName) throws XUIParseException
    {
        try
        {
            // create a schema ... from the document
            SchemaFactory sf =
                SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema();

            // USE THIS WAY TO VALIDATE, IT'S THE ONLY WAY THAT SEEMS TO WORK
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(fileName));
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            /* Rather than use the below line of code, we are using the
             * schema to create a validator and call the validator
             * directly. Why? Because the following line of code does
             * absolutely nothing. This handy piece of useful information
             * is found at:
             * http://www.ibm.com/developerworks/xml/library/x-jaxpval.html
             * by Brett McLaughlin
             */
            //spf.setSchema(schema);
            SAXParser sp = spf.newSAXParser();
            sp.parse(fileName, this);

        } catch(Exception e)
        {
            e.printStackTrace();
            throw new XUIParseException(e.getMessage());

        }
// THE OLD WAY OF VALIDATING AGAINST A SCHEMA
/*        try
        {
            reader = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
            reader.setFeature("http://xml.org/sax/features/validation", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema", true);
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
            reader.setFeature("http://apache.org/xml/features/validation/dynamic", true);
            reader.setContentHandler(this);
            reader.parse(fileName);
        }
        catch(SAXException se)
        {
            throw new XUIParseException(se.getMessage());
        }
*/
    }

    /**
     * Parses the file based on the file name.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>File</code>.
     * @throws XUIParseException if the XUI document is not valid against the
     * Purnama Project XUI schema.
     * @throws IOException if the document cannot be found or if permissions
     * are not allowed.
     *
     */
    public void parse(File fileName) throws XUIParseException
    {
        parse(fileName.toString());
    }

    /**
     * Parses the file based on the file name.
     *
     * @param fileName the name of the file to parse (complete with path). Given as a
     * <code>URL</code>.
     * @throws XUIParseException if the XUI document is not valid against the
     * Purnama Project XUI schema.
     * @throws IOException if the document cannot be found or if permissions
     * are not allowed.
     * @throws MalformedURLException if the URL is malformed.
     *
     */
    public void parse(URL fileName) throws XUIParseException
    {
        parse(fileName.toString());
    }

    /**
     * Returns the XUI document object which represents the document itself
     * with all of its nodes. If the document has not been completely built
     * by the XUIBuilder, then the XUIBuilder will return null.
     *
     * @return XUI DOM.
     */
    public XUI getXUIDocument()
    {
        if(endOfDocumentReached)
        {
            return xui;
        } else
        {
            return null;
        }
    }

    /**
     * Receive notification of character data.
     *
     * <p>The Parser will call this method to report each chunk of
     * character data.  SAX parsers may return all contiguous character
     * data in a single chunk, or they may split it into several
     * chunks; however, all of the characters in any single event
     * must come from the same external entity so that the Locator
     * provides useful information.</p>
     *
     * <p>The application must not attempt to read from the array
     * outside of the specified range.</p>
     *
     * <p>Note that some parsers will report whitespace in element
     * content using the {@link #ignorableWhitespace ignorableWhitespace}
     * method rather than this one (validating parsers <em>must</em>
     * do so).</p>
     *
     * @param ch The characters from the XML document.
     * @param start The start position in the array.
     * @param length The number of characters to read from the array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     */
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        String data = new String(ch, start, length);
        if(!(data.matches("\\s+")))
        {
            String trimmedData = data.trim();
            XUINode currentNode = (XUINode)nodeStack.peek();
            currentNode.appendCDATA(trimmedData);
        }
    }

    /**
     * Receive notification of the end of a document.
     *
     * <p>The SAX parser will invoke this method only once, and it will
     * be the last method invoked during the parse.  The parser shall
     * not invoke this method until it has either abandoned parsing
     * (because of an unrecoverable error) or reached the end of
     * input.</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #startDocument
     */
    public void endDocument() throws SAXException
    {
        try
        {
            xui = new XUI(root);
        } catch (XUIValidationException e)
        {
            throw new SAXException(e.getMessage());
        }
        // create the implementation of XUI using Java Swing
        // XUIRealizer.realize(root);
        endOfDocumentReached = true;
    }

    /**
     * Receive notification of the end of an element.
     *
     * <p>The SAX parser will invoke this method at the end of every
     * element in the XML document; there will be a corresponding
     * {@link #startElement startElement} event for every endElement
     * event (even when the element is empty).</p>
     *
     * <p>For information on the names, see startElement.</p>
     *
     * @param namespaceURI The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified XML 1.0 name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     */
    public void endElement(String namespaceURI, String localName, String qName)
      throws SAXException
    {
        nodeStack.pop();
        currentLevel--;
    }

    /**
     * Receive notification of the beginning of a document.
     *
     * <p>The SAX parser will invoke this method only once, before any
     * other methods in this interface or in {@link org.xml.sax.DTDHandler
     * DTDHandler} (except for {@link #setDocumentLocator
     * setDocumentLocator}).</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #endDocument
     */
    public void startDocument() throws SAXException
    {
        // initialization must take place here because as soon as the
        // handler is registered with the parser, things start happening.
        // this guarantees everthing will be set up correctly since the
        // parser will call this method up.
        firstTime = true;
        nodeStack = new Stack();
        currentLevel = 0;
        endOfDocumentReached = false;
        namespaces = new Hashtable(4);
        root = null;
    }

    /**
     * Receive notification of the beginning of an element.
     *
     * <p>The Parser will invoke this method at the beginning of every
     * element in the XML document; there will be a corresponding
     * {@link #endElement endElement} event for every startElement event
     * (even when the element is empty). All of the element's content will be
     * reported, in order, before the corresponding endElement
     * event.</p>
     *
     * <p>This event allows up to three name components for each
     * element:</p>
     *
     * <ol>
     * <li>the Namespace URI;</li>
     * <li>the local name; and</li>
     * <li>the qualified (prefixed) name.</li>
     * </ol>
     *
     * <p>Any or all of these may be provided, depending on the
     * values of the <var>http://xml.org/sax/features/namespaces</var>
     * and the <var>http://xml.org/sax/features/namespace-prefixes</var>
     * properties:</p>
     *
     * <ul>
     * <li>the Namespace URI and local name are required when
     * the namespaces property is <var>true</var> (the default), and are
     * optional when the namespaces property is <var>false</var> (if one is
     * specified, both must be);</li>
     * <li>the qualified name is required when the namespace-prefixes property
     * is <var>true</var>, and is optional when the namespace-prefixes property
     * is <var>false</var> (the default).</li>
     * </ul>
     *
     * <p>Note that the attribute list provided will contain only
     * attributes with explicit values (specified or defaulted):
     * #IMPLIED attributes will be omitted.  The attribute list
     * will contain attributes used for Namespace declarations
     * (xmlns* attributes) only if the
     * <code>http://xml.org/sax/features/namespace-prefixes</code>
     * property is true (it is false by default, and support for a
     * true value is optional).</p>
     *
     * @param namespaceURI The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param atts The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see #endElement
     * @see org.xml.sax.Attributes
     */
    public void startElement(String namespaceURI, String localName, String qName,
      Attributes atts) throws SAXException
    {
        // get current namespaces
        Enumeration keys = namespaces.keys();
        XUINode e = new XUINodeImpl(localName);
        e.setLevel(currentLevel);
        e.setIsBeginOfNamespace(namespaces.containsKey(namespaceURI));

        currentLevel++;
        StringTokenizer st = new StringTokenizer(qName, ":");
        String pre = st.nextToken();
        e.addNamespace(pre, namespaceURI);

        // kind of inefficient but I figure better to blow any namespace
        // already in place with the same thing than to check.
        while(keys.hasMoreElements())
        {
            String key = (String)keys.nextElement();
            String prefixValue = (String)namespaces.get(key);
            e.addNamespace(prefixValue, key);

            namespaces.remove(key);
        }

        // give it attributes
        e.setAttributes(new AttributesImpl(atts));
        if(firstTime)
        {
            root = e;
            root.setToRoot(true);
            firstTime = false;
        } else
        {
            XUINode current = (XUINode)nodeStack.peek();
            // add the parent as well
            e.setParent(current);
            current.addChildNode(e);
        }
        nodeStack.push(e);
    }

    /**
     * Begin the scope of a prefix-URI Namespace mapping.
     *
     * <p>The information from this event is not necessary for
     * normal Namespace processing: the SAX XML reader will
     * automatically replace prefixes for element and attribute
     * names when the <code>http://xml.org/sax/features/namespaces</code>
     * feature is <var>true</var> (the default).</p>
     *
     * <p>There are cases, however, when applications need to
     * use prefixes in character data or in attribute values,
     * where they cannot safely be expanded automatically; the
     * start/endPrefixMapping event supplies the information
     * to the application to expand prefixes in those contexts
     * itself, if necessary.</p>
     *
     * <p>Note that start/endPrefixMapping events are not
     * guaranteed to be properly nested relative to each-other:
     * all startPrefixMapping events will occur before the
     * corresponding {@link #startElement startElement} event,
     * and all {@link #endPrefixMapping endPrefixMapping}
     * events will occur after the corresponding {@link #endElement
     * endElement} event, but their order is not otherwise
     * guaranteed.</p>
     *
     * <p>There should never be start/endPrefixMapping events for the
     * "xml" prefix, since it is predeclared and immutable.</p>
     *
     * @param prefix The Namespace prefix being declared.
     * @param uri The Namespace URI the prefix is mapped to.
     * @exception org.xml.sax.SAXException The client may throw
     *            an exception during processing.
     * @see #endPrefixMapping
     * @see #startElement
     */
    public void startPrefixMapping(String prefix, String uri)
    {
        namespaces.put(uri, prefix);
    }

}
