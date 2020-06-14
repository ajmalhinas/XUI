package org.purnamaproject.xui.binding;

/**
 * @(#)TextModel.java    0.1 18/08/2003
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
 * <p>This specific type of event represents some sort of textual event that has taken place. The
 * textual event can be as simple as text having been entered into a field or more specialized,
 * like the XUIHypertextPane, that text represents a URL that has been visited. The following
 * types of components are capable of generating this type of event:</p>
 *
 * <ul>
 *   <li>XUITextField</li>
 *   <li>XUITextArea</li>
 *   <li>XUIPasswordField</li>
 *   <li>XUIHyperTextPane</li>
 * </ul>
 *
 * <p><b>Note: for XUITextField, XUIPasswordField and XUIHyperTextPane, model code will want to get the
 * text that is inside of it.</b></p>
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public interface TextModel extends XUIModel
{
    /**
     * This method is called whenever a component has text entered into it. This can be as simple
     * as a XUITextArea which generates this event every time a character is typed inside of the
     * component. It can also be when a XUIPasswordField or XUITextField have the enter key pressed
     * while the cursor is inside of this component. Finally, it can be when the XUIHypertextPane
     * has text in the form of a URL used to direct the pane to a Web site based on that URL.
     *
     * @param component the component that generated the event.
     */
    public void textAction(XUIComponent component);
}
