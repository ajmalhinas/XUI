package org.purnamaproject.xui.component.container.toplevel;

/**
 * @(#)XUIWindow.java    0.5 18/08/2003
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
import org.purnamaproject.xui.component.container.toplevel.XUITopLevelContainer;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.GridMapping;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.XUITypeFormatException;


/**
 * The abstraction of a Top level window. This base abstract class represents the
 * top of the hierarchy of XUI components. The implementation GUI components are
 * then mapped to the subtypes of XUIComponent.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUIWindow extends XUIContainer, XUITopLevelContainer, GridMapping, XUIEventSource
{
    /**
     * Returns the XUI name of this component as a string.
     *
     * @return the name of this component.
      * @see #setName(String)
     */
    public String getName();

    /**
     * Sets the XUI name of this component. The string values allowed can contain
     * both alphabetic and numerica characters. Real-time validation is performed and
     * therefor if a wrong value is entered, an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#string">W3C XML Schema, section 3.2.1</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema string
     * type, or if the string is null, an exception occurs.
     * @param newName the new name to assign to this component.
     */
    public void setName(String newName) throws XUITypeFormatException;

}
