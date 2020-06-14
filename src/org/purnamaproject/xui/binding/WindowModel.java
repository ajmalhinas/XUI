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
 * <p>The type used to handle event generation from a XUIWindow. This type of event is quite
 * simple (like the ActionModel's event) in that it is only a one-state event: the window
 * is closing. This gives the implementor the option of doing something at that time which
 * could be to offer a dialog box and ask the user if they wish to continue with the exiting
 * operation, go to another (possibly main window) or simply exit.</p>
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public interface WindowModel extends XUIModel
{
    /**
     * This method is called whenever a XUIWindow or XUICustomDialog are closed.
     *
     * @param component the component that generated the event.
     */
    public void windowAction(XUIComponent component);
}
