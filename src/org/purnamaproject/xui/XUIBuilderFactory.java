package org.purnamaproject.xui;

/**
 * @(#)XUIBuilderFactory.java    0.1 18/08/2003
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

import java.lang.Class;
import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import org.purnamaproject.xui.impl.XUIBuilderImpl;

/**
 * This class looks after the creation of a XUIBuilder object. This Factory can call up any
 * XUIBuilder class by its class name.
 *
 * @version    0.1 18/08/2003
 * @author     Arron Ferguson
 */
public class XUIBuilderFactory
{
    /**
     * The singleton instance of this class.
     */
    private static XUIBuilderFactory xuibuilderfactory = null;

    /**
     * Default constructor. Private.
     */
    private XUIBuilderFactory()
    {
        ;
    }

    /**
     * Returns a XUIBuilderFactory.
     *
     * @return a handle to the XUIBuilderFactory.
     */
    public static XUIBuilderFactory getInstance()
    {
        if(xuibuilderfactory == null)
            xuibuilderfactory = new XUIBuilderFactory();
        return xuibuilderfactory;
    }

    /**
     * Returns a XUIBuilder. This method does not need the name of the class to use
     * for instantiating a XUIBuilder as it uses the default one provided.
     *
     * @return XUIBuilder.
     * @see org.purnamaproject.xui.XUIBuilder
     */
    public XUIBuilder getXUIBuilder()
    {
        XUIBuilder builder = new XUIBuilderImpl();
        return builder;
    }

    /**
     * Returns a XUIBuilder. This method uses a string name to instantiate an instance
     * of the class.
     *
     * @return XUIBuilder.
     * @throws ClassNotFoundException if the class given by name is not found (case sensitive).
     * @throws InstantiationException if the class given by name cannot be instantiated.
     * @throws IllegalAccessException if the class given by name cannot be accessed (for example if
     * the class loader forbids this operation.
     * @see org.purnamaproject.xui.XUIBuilder
     */
    public XUIBuilder getXUIBuilder(String className) throws ClassNotFoundException,
        InstantiationException, IllegalAccessException
    {
        Class builderClass = Class.forName(className);
        return (XUIBuilder)builderClass.newInstance();
    }

}
