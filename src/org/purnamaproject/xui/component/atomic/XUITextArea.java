package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUITextArea.java    0.5 18/08/2003
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
 * This atomic component represents a multi-line component of editable text that the user can
 * use to enter in textual data. Any listeners that are registered with this component can, at
 * any point, ask this component for the textual data found within.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUITextArea extends XUITextField
{
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
