package org.purnamaproject.xui.component.container.intermediate;

/**
 * @(#)XUISplitPanel.java    0.5 18/08/2003
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

import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * <p>The abstraction of a XUI split panel. A split panel is an intermediate container that contains
 * two sides: either a left and a right side or a top and a bottom side. Split panes allow for
 * components to be placed within the two sides To add more functionality, either a XUIPanel or a
 * XUIScrollPanel can be placed in either side. The bar that separates each side can be resized
 * to show more or less of a particular side.</p>
 *
 * <p>When setting the percentage for the division between the left/top and right/bottom sides
 * the underlying platform has the final say in resizing. This means that certain components
 * may reject a resize in order to maintain a specific mininum size. This rejected value
 * may stop a percentage from being used to display.</p>
 *
 * The following components can be placed into the grid of this container:
 * <ul>
 *   <li>XUIPanel</li>
 * </ul>
 *
 * Only 2 panels can be placed directly inside of a split pane (one on each side).
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUISplitPanel extends XUIContainer
{
    /**
     * Sets the orientation of the splitpane. The two options are horizontal and vertical. The value given must
     * be one of the two constants defined in the XUIUtils class or an exception is generated.
     *
     * @param orientation must be either org.purnama.xui.helpers.XUIUtils#ORIENT_HORIZONTAL or org.purnama.xui.helpers.XUIUtils#ORIENT_VERTICAL.
      * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_HORIZONTAL
      * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_VERTICAL
     */
    public void setOrientation(int orientation) throws XUITypeFormatException;


    /**
     * Divides the split pane area using a floating point number to represent percentage. The number must
     * be between 0 and 100. These are interpreted as percentage. Any other value will throw an exception.
     *
     * @param percentage the percentage representing the amout of value each side has. If the value is 25,
     * then the left/top side has 25 percent of the space and the bottom/right side has 75 percent.
     */
    public void setDivide(float percentage) throws XUITypeFormatException;

    /**
     * Adds a new component to this container in either top, bottom, left or right. If the layout chosen is
     * top and bottom, then placement values must be either PLACEMENT_LEFT or PLACEMENT_RIGHT. An exception
     * will be generated otherwise.
     *
     * <p><b>Note: This method calls up the specific class's method and uses the default position of
     * XUIUtils#PLACEMENT_LEFT or XUIUtils#PLACEMENT_TOP based on the orientation of this split panel.</b></p>
     *
     * @throws org.purnamaproject.xui.XUITypeFormatException if the orientation value is not found in the XUIUtils class.
     * <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>.
     * @param component the component to add.
     * @param orientation must be either org.purnama.xui.helpers.XUIUtils#ORIENT_HORIZONTAL or org.purnama.xui.helpers.XUIUtils#ORIENT_VERTICAL.
     * @see org.purnamaproject.xui.helpers.XUIUtils#PLACEMENT_LEFT_OR_TOP
     * @see org.purnamaproject.xui.helpers.XUIUtils#PLACEMENT_RIGHT_OR_BOTTOM
     */
    public void addComponent(XUIComponent component, int orientation) throws XUITypeFormatException;

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


}
