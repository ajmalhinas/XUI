package org.purnamaproject.xui.component.container.toplevel;

/**
 * @(#)XUIBasicDialog.java    0.5 18/08/2003
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

import javax.swing.JDialog;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.container.toplevel.XUITopLevelContainer;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The abstraction of a XUI BasicDialog. The implementing class will instantiate
 * This basic dialog box will simply display a message with an icon signifying the
 * type of message being displayed.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIBasicDialog extends XUIContainer, XUITopLevelContainer
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
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema IDREF
     * type, or if the string is null, an exception occurs.
     * @param newIDRef the new ID reference value to assign to this component.
     */
    public void setIDRef(String newIDRef) throws XUITypeFormatException;

    /**
     * Returns the XUI name of this component as a string.
     *
     * @return the name of this component.
      * @see #setName(String)
     */
    public String getName();

    /**
     * Sets the XUI name of this component. The string values allowed can contain
     * both alphabetic and numerica characters. Real-time validation is performed and
     * therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newName the new name to assign to this component.
     */
    public void setName(String newName) throws XUITypeFormatException;

    /**
     * Returns the type of this dialog.
     *
     * @return the type of this component.
     * @see #setType(byte)
     */
    public byte getType();

    /**
     * Sets the type of this dialog. The following different types are allowed:
     * <ul>
     *   <li>question - (showing a question Image - value 0)</li>
     *   <li>information - (showing an information Image - value 1)</li>
     *   <li>warning - (showing a warning Image - value 2)</li>
     *   <li>error - (showing an error Image - value 3)</li>
     * </ul>
     * <p>The value allowed for this must fall within the range from 0 to 255. Real-time validation is
     * performed and therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#byte">W3C XML Schema, section 3.3.19</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema byte type.
     * @param type the type this dialog should be. Setting it to a value out of the range of known values (0 - 3)
     * will cause no image to be displayed.
     */
    public void setType(byte type) throws XUITypeFormatException;

    /**
     * Returns the message of this dialog.
     *
     * @return the message of this dialog as a string.
      * @see #setMessage(String)
     */
    public String getMessage();

    /**
     * Sets the message of this dialog box. The string values allowed can contain both alphabetic and
     * numeric characters. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the string is null, an exception occurs.
     * @param newMessage the new message to add to this dialog.
     */
    public void setMessage(String newMessage) throws XUITypeFormatException;

}
