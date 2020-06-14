package org.purnamaproject.xui.component.menu;

/**
 * @(#)XUIMenu.java 0.5 18/08/2003
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

/**
 * The menu represents a GUI component sits atop of a menu bar. Menus can have menu items. Menus
 * can also be popup menus as well as sub menus.
 *
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public interface XUIMenu extends XUIMenuBar
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
     * Returns the whether or not this menu is a pop-up menu.
     *
     * @return true if popup menu, false otherwise.
     * @see #setIsPopupMenu(boolean)
     */
    public boolean getIsPopupMenu();

    /**
     * Sets whether or not this menu is popup. If true, then this menu is popup. Otherwise, false. The Java
     * boolean type is mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param isPopup if true then this menu is a popup menu. False means it is not.
     */
    public void setIsPopupMenu(boolean isPopup);

    /**
     * Returns the whether or not this menu is a sub-menu. If this menu is a popup menu, then this will
     * always return false.
     *
     * @return true if sub-menu, false otherwise.

     */
    public boolean getIsSubMenu();

    /**
     * Adds a new XUI menu item to this menu. If the menu item is null, an exception is thrown.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu item is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to add to this menu.
     */
    public void addMenuItem(XUIMenuItem menuItem) throws XUITypeFormatException;

    /**
     * Adds a new XUI menu item to this menu. If the menu item is null, an exception is thrown.
     * This version of this add menu method should only be called up by the realizer as it does
     * not add any child nodes to the XUI DOM since it's assumed that the child nodes already
     * exist.
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the menu item is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to add to this menu.
     */
    public void addMenuItemForRealizer(XUIMenuItem menuItem) throws XUITypeFormatException;

    /**
     * Removes the menu item from this menu. Note: if this menu item contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories).
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param id the id of the menu item.
     */
    public void removeMenuItem(String id) throws XUITypeFormatException;

    /**
     * Removes the menu item from this menu. Note: if this menu item contains a sub menu on it, the entire
     * sub-structure will be deleted as well. (e.g. like how deleting a directory will delete its
     * sub-directories). If the menu item is not found or if it is null, no action is taken
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param menuItem the menu item to delete.
     */
    public void removeMenuItem(XUIMenuItem menuItem);
}
