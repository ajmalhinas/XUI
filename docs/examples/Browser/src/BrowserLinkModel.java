
/**
 * @(#)BrowserLinkModel.java    0.1 28/06/2002
 *
 * The Purnama Project XUI (XML-based User Interface) API is an set of program
 * calls that utilize the XUI tagset and perform the task of creating a user
 * interface. The Purnama Project XUI API is specific using Java as the platform
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
 * snail-mail: 480-555 Seymour Street, Vancouver, B.C., V6B 3H6
 * Web: http://xml.bcit.ca/PurnamaProject/
 *
 */

import java.util.LinkedList;

// for Purnama Project XUI
import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.component.XUIComponent;


/**
 * Handles the bookmarks that are kept and saved as menu items.
 */
public class BrowserLinkModel implements ActionModel
{
    /**
     * The model that handles all of the functionality for browsing.
     */
    private BrowserModel parentModel;

    /**
     * The sources (XUIMenuItems) that will be selected by the user
     * and which need event handling to take the user to a new URL.
     */
    LinkedList sources;

    /**
     * Accepts the model.
     */
    public BrowserLinkModel(BrowserModel model)
    {
        parentModel = model;
        sources = new LinkedList();
    }

    /**
     * This method is called whenever a component has been selected/chosen.
     *
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        parentModel.getHypertextPane().setURL(((XUIMenuItem)component).getLabel());
        parentModel.doLink();
    }

    /**
     * Removes a source (XUIMenuItem) from the list. Returns the removed object.
     *
     * @param index the index of the event source to remove.
     * @return the event source.
     */
    public Object removeSource(int index)
    {
        return sources.remove(index);
    }

    /**
     * Adds an event source (XUIComponent).
     *
     * @param component the XUIComponent that generates events.
     */
    public void addSource(XUIComponent component)
    {
        sources.add(component);
    }
}
