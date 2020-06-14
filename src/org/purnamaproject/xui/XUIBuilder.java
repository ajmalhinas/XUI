package org.purnamaproject.xui;

/**
 * @(#)XUIBuilder.java    0.1 18/08/2003
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

/**
 * The type responsible for parsing a XUI document. Any classes that implement this interface
 * must be able to:
 * <ul>
 *  <li>parse XML based on the <a href="http://www.w3.org/TR/REC-xml">W3C XML 1.0 Recommendation</a></li>
 *  <li>create a XUI DOM based on the <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a></li>
 *  <li>validate all data types based on the <a href="http://www.w3.org/XML/Schema">W3C XML Schema Recommendation</a></li>
 * </ul>
 *
 * The type of parser used is not important. However, any underlying parsers used should be capable of
 * delivering the XUI DOM in a timely fashion. A SAX parser for example would be deemed more appropriate
 * because it does not build a DOM. The W3C DOM would not be the best choice for this as it already builds
 * a DOM which may not be best suited for building a custom DOM structure in memory.
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIBuilder
{
    /**
     * Returns the XUI document object which represents the document itself
     * with all of its nodes. If the document has not been completely built
     * by the XUIBuilder, then the XUIBuilder will return null.
     *
     * @return XUI DOM.
     */
    public XUI getXUIDocument();

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
    public void parse(String fileName) throws XUIParseException;

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
    public void parse(File fileName) throws XUIParseException;

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
    public void parse(URL fileName) throws XUIParseException;

}
