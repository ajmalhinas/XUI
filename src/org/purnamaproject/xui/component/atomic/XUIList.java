package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUIList.java    0.5 18/08/2003
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

import java.util.List;
import org.purnamaproject.xui.XUITypeFormatException;
import org.purnamaproject.xui.binding.XUIEventSource;

/**
 * This atomic component represents a list of text items. This list can allow for scrolling if there
 * are more items than what fit inside of the list viewport. Items can be selected in the list. Any
 * listeners registered with this list will be informed when a user selects an item.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIList extends XUIAtomic, XUIEventSource
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
     * XUI specification and within the range of items within the list. Otherwise, and exception is thrown.
     *
     * @param index the new index to set to. If the index does not fall within the range of XML Schema's unsignedshort
     * or if the index value falls outside of the range of items that are found in the list, thena an exception
     * is thrown.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>

     * @throws org.purnamaproject.xui.XUITypeFormatException if the value does not conform to the XUI specification.
     */
    public void setSelectedIndex(int index) throws XUITypeFormatException;

    /**
     * Returns the selected item as a string.
     *
     * @return the string value of the selected item.
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     */
    public String getSelectedItem();

    /**
     * Returns a collection of items that are presently found in the list.
     *
     * @return the items as a collection.
     */
    public List getItems();

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

    /**
     * Sets the scroll rules for this scroll panel. There are 4 choices concerning scrolling:
     * <ul>
     *   <li>vertical scrolling only</li>
     *   <li>horizontal scrolling only</li>
     *   <li>both horizontal and vertical scrolling</li>
     *   <li>neither horizontal nor vertical scrolling</li>
     * </ul>
     * The value given must be one of these based on the static variables listed in this class. Real-time
     * validation is performed and therefor if a wrong value is entered, an exception  is generated.
     *
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the XUI specification.
     * @param scrollChoice the choice of scrolling
     * @see org.purnamaproject.xui.helpers.XUIUtils#HORIZONTAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#VERTICAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#NO_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#BOTH_SCROLLING
     */
    public void setScrolling(int scrollChoice) throws XUITypeFormatException;
}
