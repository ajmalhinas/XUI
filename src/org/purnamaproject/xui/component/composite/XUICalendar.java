package org.purnamaproject.xui.component.composite;

/**
 * @(#)XUICalendar.java    0.5 18/08/2003
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
 * This component represents a calendar. It allows the viewer to display the date within
 * any month within any year.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUICalendar extends XUIComponent
{
    /**
     * Returns the x coordinate of this component. For a top level container, this coordinate is the actual
     * x coordinate value (in pixels) that appears on the desktop. For an intermediate component, this x
     * coordinate is the position within the layout grid that is assigned to the top level container.
     *
     * @return the x coordinate of this component.
     */
    public int getX();

    /**
     * Returns the y coordinate of this component. For a top level container, this coordinate is the actual
     * y coordinate value (in pixels) that appears on the desktop. For an intermediate component, this y
     * coordinate is the position within the layout grid that is assigned to the top level container.
     *
     * @return the y coordinate of this component.
     */
    public int getY();

    /**
     * Returns the width of this component. For a top level container, this value is the actual width
     * (in pixels) that appears on the desktop. For an intermediate component, this width value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells wide.
     *
     * @return the width of this component.
     */
    public int getWidth();

    /**
     * Returns the height of this component. For a top level container, this value is the actual height
     * (in pixels) that appears on the desktop. For an intermediate component, this height value is the
     * position within the layout grid that is assigned to the top level container (e.g. 2 would be two
     * cells high.
     *
     * @return the height of this component.
     */
    public int getHeight();

    /**
     * Sets this component to being enabled. By being enabled, it will respond to user operations.
     *
     * @param enabled true means this component is enabled and will respond to user operations and false
     * means that it will not.
     */
    public void setEnabled(boolean enabled);

    /**
     * <p>Passes the new coordinates and width and height for the dimensions.</p>
     *
     * <p><b>Note:</b> <i>This method call should never be called by any class other than
     * the <code>org.purnamaproject.xui.helpers.ComponentFactory</code> as it is assumed that
     * the XUI document will be created via the API calls rather than through unmarshalling
     * of the XUI document. These two pipelines are treated separately in their handling of
     * Only implementors of the ComponentFactory class should call this method.</i><p>
     *
     * @param x the x coordinate of this component and where to place it within the container
     * that it sits within.
     * @param y the y coordinate of this component and where to place it within the container
     * that it sits within.
     * @param width the width dimension of this component and the size to make it within the container
     * that it sits within.
     * @param height the height dimension of this component and the size to make it within the container
     * that it sits within.
     */
    public void factoryCoordinates(int x, int y, int width, int height);
}
