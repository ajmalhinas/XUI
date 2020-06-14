/**
 * @(#)RadioModel.java    0.1 28/06/2002
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

import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.binding.WindowModel;
import org.purnamaproject.xui.component.atomic.XUIRadioButton;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.helpers.XUIComponentFactory;
import org.purnamaproject.xui.XUI;
import org.purnamaproject.xui.helpers.XUIResources;

/**
 * A sample model for radio buttons example.
 */
public class RadioModel implements WindowModel, ActionModel
{

    /**
     * The reference to the xui document.
     */
    private XUI xui;

    /**
     * The window itself.
     */
    private XUIWindow radioWindow;

    /**
     * The panel that we will later add items to.
     */
    private XUIPanel panel;

    /**
     * XUIRadioButton that handles the click for a penguin image.
     */
    private XUIRadioButton penguinButton;

    /**
     * XUIRadioButton that handles the click for a clam image.
     */
    private XUIRadioButton clamButton;

    /**
     * XUIRadioButton that handles the click for a ant image.
     */
    private XUIRadioButton antButton;

    /**
     * XUIRadioButton that handles the click for a cat image.
     */
    private XUIRadioButton catButton;

    /**
     * Cat image.
     */
    private XUIImage cat;

    /**
     * Ant image.
     */
    private XUIImage ant;

    /**
     * Penguin image.
     */
    private XUIImage penguin;

    /**
     * Clam image.
     */
    private XUIImage clam;

    /**
     * The ID of the current image.
     */
    private String currentImageID = "";

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

        ant = XUIResources.getInstance().getImage("antcol-1.thm.jpeg", 3, 3, 1, 2);

        currentImageID = ant.getID();

        panel = (XUIPanel)xui.getXUIComponent("panel_0");
        panel.addComponent(ant);
        antButton = (XUIRadioButton)xui.getXUIComponent("radiobutton_0");
        penguinButton = (XUIRadioButton)xui.getXUIComponent("radiobutton_2");
        catButton = (XUIRadioButton)xui.getXUIComponent("radiobutton_3");
        clamButton = (XUIRadioButton)xui.getXUIComponent("radiobutton_1");

        radioWindow = (XUIWindow)xui.getXUIComponent("window_0");
        radioWindow.addEventListener(this);
        antButton.addEventListener(this);
        catButton.addEventListener(this);
        penguinButton.addEventListener(this);
        clamButton.addEventListener(this);

    }

    /**
     * This method is called whenever a button has been clicked on. In our application
     * specific context, we are looking for buttons.
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        if(component == antButton)
        {
            ant = XUIResources.getInstance().getImage("antcol-1.thm.jpeg", 3, 3, 1, 2);
            panel.removeComponent(currentImageID);
            currentImageID = ant.getID();
            panel.addComponent(ant);
            antButton.setSelected(true);
            penguinButton.setSelected(false);
            clamButton.setSelected(false);
            catButton.setSelected(false);

        } else if(component == penguinButton)
        {
            penguin = XUIResources.getInstance().getImage("penguincol-1.thm.jpeg", 3, 3, 1, 2);
            panel.removeComponent(currentImageID);
            currentImageID = penguin.getID();
            panel.addComponent(penguin);
            antButton.setSelected(false);
            penguinButton.setSelected(true);
            clamButton.setSelected(false);
            catButton.setSelected(false);

        } else if(component == clamButton)
        {
            clam = XUIResources.getInstance().getImage("Clam.gif", 3, 3, 1, 2);
            panel.removeComponent(currentImageID);
            currentImageID = clam.getID();
            panel.addComponent(clam);
            antButton.setSelected(false);
            penguinButton.setSelected(false);
            clamButton.setSelected(true);
            catButton.setSelected(false);

        } else if(component == catButton)
        {
            cat = XUIResources.getInstance().getImage("cat_standing-1.thm.jpeg", 3, 3, 1, 2);
            panel.removeComponent(currentImageID);
            currentImageID = cat.getID();
            panel.addComponent(cat);
            antButton.setSelected(false);
            penguinButton.setSelected(false);
            clamButton.setSelected(false);
            catButton.setSelected(true);

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
        if(component == radioWindow)
        {
            System.exit(0);
        }
    }

}
