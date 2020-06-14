package org.purnamaproject.xui.helpers;

/**
 * @(#)AbstractActionModel.java    0.5 18/08/2003
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

import java.io.IOException;
import java.net.MalformedURLException;
import org.purnamaproject.xui.binding.XUIBindingException;
import org.purnamaproject.xui.XUIParseException;
import org.purnamaproject.xui.XUIBuilder;
import org.purnamaproject.xui.XUIBuilderFactory;
import org.purnamaproject.xui.XUI;
import org.purnamaproject.xui.XUIValidationException;


/**
 * This class is simply for testing purposes. It loads, parses, realizes and binds the UI to any code
 * that it is referred to in the XUI document.
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class XUITest
{

    /**
     * tests the XUI document by loading it as an application.
     */
    public XUITest(String fileName)
    {
        try
        {
            // from a file, parse, build, serialize.
            XUIBuilder builder = XUIBuilderFactory.getInstance().getXUIBuilder();
            builder.parse(fileName);
            XUI xui = builder.getXUIDocument();
            // display it
            xui.visualize();
            // bind it
            xui.bind();

            // get root to save out to file if one so chooses
//            XUINode root = xui.getRoot();
            // finally save out to file system
//            xui.marshalXUI("parsed_" + fileName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /**
     * Creates an instance of the XUI document as a GUI.
     */
    public static void main(String[] args)
    {
        if(args != null)
        {
            if(args.length == 1)
                new XUITest(args[0]);
            else
            {
                System.out.println("Usage: java XUITest <filename>");
                System.exit(0);
            }
        }
        else
        {
            System.out.println("Usage: java XUITest <filename>");
            System.exit(0);
        }
    }
}
