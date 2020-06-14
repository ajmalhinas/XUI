package org.purnamaproject.xui.component.container.toplevel;

/**
 * @(#)XUISaveFileDialog.java    0.5 18/08/2003
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JDialog;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.helpers.XUIFilter;
import org.purnamaproject.xui.component.container.toplevel.XUITopLevelContainer;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The abstraction of a XUI XUISaveFileDialog. Allows the user to graphically choose a directory for
 * which a file will be saved based on application data given to it. If any listeners are registered
 * with this component they will be given a reference to the file.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUISaveFileDialog extends XUIContainer, XUITopLevelContainer, XUIEventSource
{
    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
      * @see #setIDRef(String)
     */
    public String getIDRef();

    /**
     * Sets the XUI id reference of this component. This value is a reference to parent container that
     * this component is a child of. The string values allowed can contain both alphabetic and numeric
     * characters. The first character must start with an alphabetic character. Real-time validation
     * is performed and therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#IDREF">W3C XML Schema, section 3.3.9</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the string is null, an exception occurs.
     * @param newIDRef the new ID reference value to assign to this component.
     */
    public void setIDRef(String newIDRef) throws XUITypeFormatException;

    /**
     * Returns the selected file as a file object.
     *
     * @return the selected file.
     */
    public File getSelectedFile();

    /**
     * Returns the name of the file that will be created. Given as a File object.
     *
     * @param file the file to save to and given as a File.
     */
    public void setSelectedFile(File file);

    /**
     * Returns the filters used within the file dialog.
     *
     * @return a list of strings that represent the filters used with this file dialog.
      * @see #addFilter(String)
     */
    public List getFilters();

    /**
     * Removes the filters from this file dialog. If no filters are found, no action is taken.
     *
      * @see #addFilter(String)
     */
    public void removeAllFilters();

    /**
     * Adds a list of new filters to this file dialog box. Filters allow the user to choose from a list of known
     * file types which makes looking for a particular file more efficient. The types of values that are allowed
     * are any character using either numerics or alphabetic characters (both upper and lower case). There must be
     * at least 1 character, but no more than 4 characters. Real-time validation is performed and therefor if a
     * wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#token">W3C XML Schema, section 3.3.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema token
     * type, or if the string is null, an exception occurs.
     * @param newFilters a new filter to add to this file dialog box.
     */
    public void addFilters(List newFilters) throws XUITypeFormatException;

    /**
     * Adds a new filter to this file dialog box. Filters allow the user to choose from a list of known file types
     * which makes looking for a particular file more efficient. The types of values that are allowed are any
     * character using either numerics or alphabetic characters (both upper and lower case). There must be at
     * least 1 character, but no more than 4 characters. Real-time validation is performed and therefor if a
     * wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#token">W3C XML Schema, section 3.3.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema token
     * type, or if the string is null, an exception occurs.
     * @param newFilter a new filter to add to this file dialog box.
     */
    public void addFilter(String newFilter) throws XUITypeFormatException;

}
