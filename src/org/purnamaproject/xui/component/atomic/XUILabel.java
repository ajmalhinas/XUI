package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUILabel.java    0.5 18/08/2003
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
 * This atomic component represents a simple label of text. It has text that can be justified.
 * XUILabel does not contain images, hypertext or rich text. It is a single line.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUILabel extends XUIAtomic
{
    /**
     * Returns the text of this component.
     *
     * @return the text of this component as a string.
      * @see #setText(String)
     */
    public String getText();

    /**
     * Sets the text of this component. The string values allowed can contain
     * both alphabetic and numeric characters. Real-time validation is performed and
     * therefore if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newText the new textual data to assign to this component.
     */
    public void setText(String newText) throws XUITypeFormatException;

    /**
     * Sets the justification for the text. Text can be left justified (i.e. flush left), right
     * justified (flush right) or centered on the line that it sits on. Use the values provided.
     * giving a value out of the range will result in no change being made.
     *
     * @param justification the justification value to set the text justification.
     * @see org.purnamaproject.xui.helpers.XUIUtils#LEFT_JUSTIFIED
     * @see org.purnamaproject.xui.helpers.XUIUtils#RIGHT_JUSTIFIED
     * @see org.purnamaproject.xui.helpers.XUIUtils#CENTER_JUSTIFIED
     */
    public void setJustification(int justification);
}
