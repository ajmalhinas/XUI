package org.purnamaproject.xui.binding;

/**
 * @(#)TableModel.java  0.1 18/08/2003
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
 * <p>Handles events generated from a XUITable dealing with cells. There are 3 types of
 * event types:</p>
 * <ul>
 *   <li>a cell has been selected</li>
 *   <li>a cell is being edited (i.e. the operation is currently being done)</li>
 *   <li>a cell has finished being edited (i.e. the operation has been completed)</li>
 *</ul>
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public interface XUITableModel extends XUIModel
{

    /**
     * This method is called when a user clicks on the cell two times (i.e. double-click).
     * This event signifies that the user has gone into edit mode within the cell.
     *
     * @param component the component that generated the event.
     * @param row the row where cell is currently being editing in.
     * @param column the column where cell is currently being editing in.
     */
    public void tableCellEditingAction(XUIComponent component, int row, int column);

    /**
     * This method is called when the user selects a cell (i.e. single-click).
     *
     * @param component the component that generated the event.
     * @param row the row where the selected cell is situated.
     * @param column the column where the selected cell is situated.
     */
    public void tableCellSelectedAction(XUIComponent component, int row, int column);

    /**
     * Called when the user has finished editing a cell.
     *
     * @param component the component that generated the event.
     * @param row the row where cell was editing in.
     * @param column    the column where cell was editing in.
     */
    public void tableCellEditedAction(XUIComponent component, int row, int column);
}
