package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUIComboBox.java    0.5 18/08/2003
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
import org.purnamaproject.xui.binding.XUIEventSource;

/**
 * This atomic component represents a list of text items. Where this component differs from a XUIList
 * is that a XUIList displays at least several items in its list with scrollbars. With the XUIComboBox,
 * it only shows one item. The user clicks on an arror (usually found on the right side of the one
 * visible item). This expands or "pops up" a larger selection of the list. If any listeners are registered,
 * they will be informed of the item selected.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIComboBox extends XUIAtomic, XUIEventSource
{
    /**
     * Sets the selected item. Any string value is allowed including empty strings. A null string will
     * result in an exception.
     *
     * @param text the textual data to put in the item. If this value is null, an exception is generated.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the value is null.
     */
    public void addNewItem(String text) throws XUITypeFormatException;

    /**
     * Returns the index of the selected item.
     *
     * @return the index of the selected item.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @see #setSelectedIndex(int)
     */
    public int getSelectedIndex();

    /**
     * Sets the index of the selected item. The value selected must fall within the range specified within the
     * XUI specification and within the range of items within the combo box. Otherwise, and exception is thrown.
     *
     * @param index the new index to set to. If the index does not fall within the range of XML Schema's unsignedshort
     * or if the index value falls outside of the range of items that are found in the combo box, thena an exception
     * is thrown.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>

     * @throws org.purnamaproject.xui.XUITypeFormatException if the value does not conform to the XUI specification.
     */
    public void setSelectedIndex(int index) throws XUITypeFormatException;

    /**
     * Sets whether or not the items within the combo box are editable. If they are, then a field that is chosen,
     * can be edited by the user clicking in that field and entering new character data. If the user choses to
     * edit when the edit area is blank, then a new entry is added (both to the native GUI component as well as
     * to the XUI node.)
     *
     * @param editable whether or not the field is editable.
     */
    public void setEditable(boolean editable);

    /**
     * Returns the selected item as a string.
     *
     * @return the string value of the selected item.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     */
    public String getSelectedItem();

    /**
     * Deletes and item based on its index. If the index does not fall within the range of XML Schema's unsignedshort
     * or if the index value falls outside of the range of items that are found in the list, thena an exception
     * is thrown.
     *
     * @param index the index of the item to delete.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>

     * @throws org.purnamaproject.xui.XUITypeFormatException if the value does not conform to the XUI specification.
     */
    public void deleteItem(int index) throws XUITypeFormatException;

}
