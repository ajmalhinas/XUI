package org.purnamaproject.xui.component.container.toplevel;

/**
 * @(#)XUITopLevelContainer.java    0.5 18/08/2003
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


/**
 * Represents a top-level container. Top level containers have the ability to display
 * themselves as well as hide themselves.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */

public interface XUITopLevelContainer
{
    /**
     * <p>Sets the component's XML element's visible attribute. This does not actually make the component visible.
     * In order to see the component visible, call the <code>visualize()</code> method.</p>
     *
     * @param visibility true means this component is visible and false means it is not.
     * @see #visualize()
     */
    public void setVisible(boolean visibility);

    /**
     * If this component has its visible attribute set to true, the component will then be visualized (i.e.
     * displayed on the screen).
     */
    public void visualize();

    /**
     * Returns whether this component is visible or not.
     *
     * @return visible state (true or false).
     */
    public boolean isVisible();
}
