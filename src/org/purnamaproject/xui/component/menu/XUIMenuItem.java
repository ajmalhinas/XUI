package org.purnamaproject.xui.component.menu;

/**
 * @(#)XUIMenuItem.java    0.5 18/08/2003
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

import javax.swing.ImageIcon;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The menu item represents a GUI menu item that sits on top of a Menu. A menu item can
 * have sub menus hanging off of it. Menu items can also have check marks and images. Menu
 * items can be disabled.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIMenuItem extends XUIComponent, XUIEventSource
{
    /**
     * Sets this component to being enabled. By being enabled, it will respond to user operations.
     *
     * @param enabled true means this component is enabled and will respond to user operations and false
     * means that it will not.
     */
    public void setEnabled(boolean enabled);

    /**
     * Returns the label of this component.
     *
     * @return the label of this component as a string.
      * @see #setLabel(String)
     */
    public String getLabel();

    /**
     * Sets the label of this component. The string values allowed can contain
     * both alphabetic and numeric characters. Real-time validation is performed and
     * therefore if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newLabel the new label to assign to this component.
     */
    public void setLabel(String newLabel) throws XUITypeFormatException;

    /**
     * Returns the whether or not this menu item has a check mark on it (similar to a check box.
     *
     * @return true if checked, false otherwise.
     * @see #setIsChecked(boolean)
     */
    public boolean getIsChecked();

    /**
     * Sets whether or not this menu item is checked. If true, then this menu item is checked. Otherwise, false.
     * The Java boolean type is mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param isChecked if true then this menu item is checked. False means it is not.
     */
    public void setIsChecked(boolean isChecked);

    /**
     * Adds a new image to this button. The XUIButton class will handle the encoding and decoding
     * of binary format data to Base64 data that will be later stored in the XML file.
     *
     * @param newImage the new image icon (which contains the binary image data).
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#base64Binary">W3C XML Schema, section 3.2.16</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema ID
     * type, or if the image is null, an exception occurs.
     */
    public void addImage(XUIImage newImage) throws XUITypeFormatException;

    /**
     * Removes the image that is situated in this component. If no image exists, no action is taken.
     *
     * @see #addImage(XUIImage)
     */
    public void removeImage();

    /**
     * Sets the keyboard shortcut to this particular menu component. This allows for advanced users to
     * use the keyboard to quickly call up menu commands rather than use the mouse pointer to select each
     * menu component.
     *
     * @param key1 the first key value of the shortcut. These values are strings and must conform to the
     * string values found in the Purnama Project XUI Schema.
     * @param key2 the second key value of the shortcut. These values are strings and must conform to the
     * string values found in the Purnama Project XUI Schema.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the key values do not map to the XUI constants found in XUIUtils.
     * <a href="http://xml.bcit.ca/xml/PurnamaProject/xui/xui.xsd">Purnama Project XUI Schema</a> - Shortcut
     */
    public void setShortCut(String key1, String key2) throws XUITypeFormatException;

    /**
     * Returns the shortcut from this menu component. If no shortcut exists, then nothing is returned.
     *
     * The returned string contains the keycode and any modifiers that are with it.
     */
    public String getShortcut();

    /**
     * Returns the XUI id reference of this component.
     *
     * @return the id reference of this component as a string.
      * @see #setIDRef(String)
     */
    public String getIDRef();

    /**
     * Sets the XUI id reference of this component. This value is a reference to parent container that
     * this component is a child of. The value of the reference must be the value of an existing key
     * within the XUI document.     The string values allowed can contain both alphabetic and numeric
     * characters. The first character must start with an alphabetic character. Real-time validation
     * is performed and therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#IDREF">W3C XML Schema, section 3.3.9</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema IDREF
     * type, or if the string is null, an exception occurs.
     * @param newIDRef the new ID reference value to assign to this component.
     */
    public void setIDRef(String newIDRef) throws XUITypeFormatException;
}
