package org.purnamaproject.xui.component.container.toplevel;

/**
 * @(#)XUICustomDialog.java    0.5 18/08/2003
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

import javax.swing.JDialog;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.GridMapping;
import org.purnamaproject.xui.component.container.toplevel.XUITopLevelContainer;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * The abstraction of a XUI CustomDialog. The implementing class will instantiate
 * a Java Swing custom dialog box. A custom dialog acts very similar to a Window
 * with the exception that a custom dialog must have a parent (a Window) and must
 * choose a modality.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUICustomDialog extends XUIContainer, XUITopLevelContainer, GridMapping
{
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

    /**
     * Returns the modality state of this dialog.
     *
     * @return the modality state of this component.
     * @see #setModal(boolean)
     */
    public boolean getModal();

    /**
     * Sets the modality of this dialog. If the modality value is set to true, then the dialog box must
     * be responded to before the user may interact with the underlying Windows of this application.
     * Otherwise, the user can bypass this dialog and interact with the underlying parent window that
     * called this dialog. The value allowed for this must be a boolean value. The Java boolean type is
     * mapped to the XML Schema boolean type.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#boolean">W3C XML Schema, section 3.2.2</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @param modal the modality of this dialog.
     */
    public void setModal(boolean modal);

    /**
     * Returns the dialog peer. This is required since all other (non dialog) intermediate containers
     * return containers.
     *
     * @return a dialog peer.
     */
    public JDialog getDialogPeer();
}
