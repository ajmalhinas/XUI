package org.purnamaproject.xui.binding;

/**
 * @(#)WindowModel.java    0.1 18/08/2003
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

import org.purnamaproject.xui.component.XUIComponent;

/**
 * <p>Handles the events generated from XUIComponents. This specific type of event
 * represents one of several things happening inside of a list type of component:</p>
 *
 * <ul>
 *   <li>An item was selected (single selection from either clicking on it or pressing the
 *     enter key).</li>
 *   <li>An item was selected (double selection by a double-click from the mouse pointer
 *     over a particular item).</li>
 *   <li>An item was chosen for deletion.</li>
 *   <li>An item was edited</li>
 * </ul>
 *
 * <p>To date, the two components that use this model are the List and the ComboBox. Both of
 * these can generate these types of events. However, only the List can generate a double-click
 * and only a ComboBox can generate an edit type of event. Once it is determined what type of
 * event has taken place, the public accessor methods can be used to retrieve data from the
 * components.</p>
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public interface ListActionModel extends XUIModel
{
    /**
     * Represents a double-selection (double-click) event.
     */
    public static final byte DOUBLE_CLICK = 0;

    /**
     * Represents a single-selection (single-click) event.
     */
    public static final byte SINGLE_CLICK = 1;

    /**
     * Represents an edit event.
     */
    public static final byte EDITED = 2;

    /**
     * Represents a delete event.
     */
    public static final byte DELETED = 3;

    /**
     * This method is called whenever a component (XUIList or XUIComboBox).
     *
     * @param component the component that generated the event.
     * @param index the index of the item that was selected/edited/deleted. If deleted, it is the
     * previous index since that item no longer exists. If the index is -1, then no item is selected
     * and/or no item available in the list (0 length list).
     * @param type the type of event.
     */
    public void listAction(XUIComponent component, byte type, int index);
}
