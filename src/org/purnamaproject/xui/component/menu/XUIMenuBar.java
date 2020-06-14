package org.purnamaproject.xui.component.menu;

/**
 * @(#)XUIMenuBar.java  0.5 18/08/2003
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

import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.component.XUIComponent;

/**
 * The menu bar represents a GUI component that sits on top of a window that holds menus.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public interface XUIMenuBar extends XUIComponent
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
     * this component is a child of. The value of the reference must be the value of an existing key
     * within the XUI document.  The string values allowed can contain both alphabetic and numeric
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
     * Adds a new XUI menu to this menu bar. If the menu is null, an exception is thrown. As well, if the
     * node already exists within a XUI document hierarchy, then an exception is thrown. Normally in
     * GUI programming this is not a problem. However with XUI, each node has a unique ID and thus
     * each XUI component must be unique since the XML schema requires each element carrying attributes
     * whose type is xs:ID must be unique.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param newMenu the menu to add to this menu item.
     */
    public void addMenu(XUIMenu newMenu) throws XUITypeFormatException;

    /**
     * Adds a menu to this menu bar using the index given. If the index is < 0, no action is taken.
     * If the index is 2 or more greater than the current number of menus, the menu will simply be added
     * to the end of the menubar (last place). If the menu is null, an exception is thrown. As well, if the
     * node already exists within a XUI document hierarchy, then an exception is thrown. Normally in
     * GUI programming this is not a problem. However with XUI, each node has a unique ID and thus
     * each XUI component must be unique since the XML schema requires each element carrying attributes
     * whose type is xs:ID must be unique.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #addMenu(XUIMenu)
     * @param newMenu the menu to add to this menu item.
     * @param index the position on the menu bar where this menu should go.
     */
    public void addMenu(XUIMenu newMenu, int index) throws XUITypeFormatException;

    /**
     * Adds a new XUI menu to this menu. If the menu is null, an exception is thrown. This method should
     * only be called by the <code>Realizer</code> class since it already assumes a DOM node structure and
     * does not build any child nodes.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param newMenu the menu to add to this menu item.
     */
    public void addMenuForRealizer(XUIMenu newMenu) throws XUITypeFormatException;

    /**
     * Removes the menu from this menu item. Note: if this menu contains a menu items on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories).
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param id the id of the menu.
     */
    public void removeMenu(String id) throws XUITypeFormatException;

    /**
     * Removes the menu from this menu component. Note: if this menu contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories). If the menu is not found or if it is null, no action is taken
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menu the menu to remove.
     */
    public void removeMenu(XUIMenu menu);
}
