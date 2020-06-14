package org.purnamaproject.xui;

/**
 * @(#)GridMapping.java    0.1 18/08/2003
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

import java.awt.Dimension;
import java.util.Hashtable;

/**
 * Represents a container's ability to map components to its grid. This grid acts much like graph paper
 * where each square represents a cell. Each cell can have a component or a portion of a component mapped
 * to it. Components can occupy several cells in both the x and the y direction but components cannot
 * be mapped to cells where another component already exists.
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public interface GridMapping
{
    /**
     * Sets the grid (rows x columns) of this component. The value allowed for this must fall within the
     * range from 0 to 65,535. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated. If this method is not called or any constructors provided that allow the
     * grid to be set, a default grid size of 1 x 1 will be given.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUIDisplayException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param rows the number of rows within the grid.
     * @param columns the number of columns within the grid.
     */
    public void setGrid(int rows, int columns) throws XUIDisplayException;

    /**
     * Returns the grid height and width.
     *
     * @return the grid height and width as a <code>java.awt.Dimension</code> object.
     */
    public Dimension getGrid();

    /**
     * Returns a hashtable which contains XUIComponents as keys and rectangles as the regions
     * occupied on the grid by child components. Used for components to determine if they are
     * attempting to take up space on the container where another component already exists.
     *
     * @return the hashtable of the components and their mapped rectangle regions.
     */
    public Hashtable getGridMapping();

}
