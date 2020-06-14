package org.purnamaproject.xui.helpers;

/**
 * @(#)XUIComponentFactory.java    0.5 18/08/2003
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

import java.util.Vector;

/**
 * Handles ids within a system and makes sure that all ids made are unique and managed in a central
 * store.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class IDFactory
{
    /**
     * The list of ids for all components.
     */
    private static Vector IDs;

    /**
     * Local instance of itself.
     */
    private static IDFactory idFactory = null;

    /**
     * Constructor that instantiates the collection of IDs.
     */
    private IDFactory()
    {
        IDs = new Vector();
    }

    /**
     * Returns a reference to the factory.
     *
     * @return a reference to the factory.
     */
    public static IDFactory getInstance()
    {
        if(idFactory == null)
            idFactory = new IDFactory();
        return idFactory;
    }

    /**
     * Generates an id.
     */
    public String generateID(String elementName)
    {
        int IDCounter = 0;
        String id = elementName + "_" + IDCounter;
        if(IDs.size() > 0)
        {
            while(IDs.contains(id))
            {
                IDCounter++;
                id = elementName + "_" + IDCounter;
            }
            IDs.add(id);
            return id;
        }
        else
        {
            IDs.add(id);
            IDCounter++;
            return id;
        }
    }

    /**
     * Adds an id to the collection of IDs.
     *
     * @param id a new id to add to the collection of ids.
     */
    public void addID(String id)
    {
        IDs.add(id);
    }

    /**
     * Returns true if the id is in the collection, false otherwise.
     *
     * @return true if the id is in the collection, false otherwise.
     * @param id the id to check.
     */
    public boolean containsID(String id)
    {
        return IDs.contains(id);
    }

}
