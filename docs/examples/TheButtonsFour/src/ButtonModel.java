/**
 * @(#)ButtonModel.java    0.1 28/06/2002
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


import org.purnamaproject.xui.binding.WindowModel;
import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.component.atomic.XUIButton;
import org.purnamaproject.xui.XUI;

/**
 * A sample model for the button example.
 */
public class ButtonModel implements WindowModel, ActionModel
{

    /**
     * The reference to the xui document.
     */
    private XUI xui;

    /**
     * The window itself.
     */
    private XUIWindow buttonWindow;

    /**
     * Button 1.
     */
    private XUIButton b1;

    /**
     * Button 2.
     */
    private XUIButton b2;

    /**
     * Button 3.
     */
    private XUIButton b3;

    /**
     * Button 4.
     */
    private XUIButton b4;

    /**
     * The method required to be there for the binding to work.
     *
     * @param xui the XUI document link so that the XUI can be queried
     * for its components based on the XML document generated.
     */
    public void init(XUI document)
    {
        // local reference of the XUI document
        xui = document;

        buttonWindow = (XUIWindow)xui.getXUIComponent("window_0");
        b1 = (XUIButton)xui.getXUIComponent("button_0");
        b2 = (XUIButton)xui.getXUIComponent("button_1");
        b3 = (XUIButton)xui.getXUIComponent("button_2");
        b4 = (XUIButton)xui.getXUIComponent("button_3");

        buttonWindow.addEventListener(this);
        b1.addEventListener(this);
        b2.addEventListener(this);
        b3.addEventListener(this);
        b4.addEventListener(this);
    }

    /**
     * This method is called whenever a button has been clicked on. In our application
     * specific context, we are looking for buttons.
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        if(component == b1)
        {
            System.out.println("Everything is ok.");

        } else if(component == b2)
        {
            System.out.println("Domo arigato, Mr. Roboto");

        } else if(component == b3)
        {
            System.out.println("Cat in Indonesian is Kucing.");

        } else if(component == b4)
        {
            System.out.println("Meeoowww!");

        }
    }

    /**
     * This method is called whenever a XUIWindow or XUICustomDialog are closed. This happens
     * when a window has been closed. We want to give the user in our application the opportunity to
     * say no to exiting the application.
     *
     * @param component the component that generated the event.
     */
    public void windowAction(XUIComponent component)
    {
        if(component == buttonWindow)
        {
            System.exit(0);
        }
    }

}
