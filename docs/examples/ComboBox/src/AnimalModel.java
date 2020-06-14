/**
 * @(#)ComboBoxModel.java   0.1 28/06/2002
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
import org.purnamaproject.xui.component.atomic.XUIComboBox;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.helpers.XUIComponentFactory;
import org.purnamaproject.xui.XUI;
//import java.net.URL;

import org.purnamaproject.xui.helpers.XUIResources;

/**
 * A sample model for the combo box example showing images.
 */
public class AnimalModel implements WindowModel, ActionModel
{

    /**
     * The reference to the xui document.
     */
    private XUI xui;

    /**
     * The window itself.
     */
    private XUIWindow comboWindow;

    /**
     * The panel that we will later add items to.
     */
    private XUIPanel panel;

    /**
     * The combo box allowing us to choose different images.
     */
    private XUIComboBox box;

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

        ant = XUIResources.getInstance().getImage("antcol-1.thm.jpeg", 3, 3, 2, 1);

        // alternatively, you could do this, although it may take longer if you have a slow connection.
/*      try
        {
            URL url = new URL("http://www.calwestgroup.com/cartoonclips/AC_Volume_1/Animals/antcol-1.thm.jpeg");

            ant = XUIComponentFactory.makeImage(url, 3, 3, 2, 1);

        }catch(Exception e)
        {
            e.printStackTrace();
        }
*/
        currentImageID = ant.getID();

        panel = (XUIPanel)xui.getXUIComponent("panel_0");
        panel.addComponent(ant);
        box = (XUIComboBox)xui.getXUIComponent("comboBox_0");

        comboWindow = (XUIWindow)xui.getXUIComponent("window_0");
        comboWindow.addEventListener(this);
        box.addEventListener(this);

    }

    /**
     * This method is called whenever a button has been clicked on. In our application
     * specific context, we are looking for buttons.
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        if(component == box)
        {
            String item = box.getSelectedItem();
            if(item.equals("ant"))
            {
                ant = XUIResources.getInstance().getImage("antcol-1.thm.jpeg", 3, 3, 2, 1);
                panel.removeComponent(currentImageID);
                currentImageID = ant.getID();
                panel.addComponent(ant);

            }
            else if(item.equals("clam"))
            {
                clam = XUIResources.getInstance().getImage("Clam.gif", 3, 3, 2, 1);
                panel.removeComponent(currentImageID);
                currentImageID = clam.getID();
                panel.addComponent(clam);

            }
            else if(item.equals("cat"))
            {
                cat = XUIResources.getInstance().getImage("cat_standing-1.thm.jpeg", 3, 3, 2, 1);
                panel.removeComponent(currentImageID);
                currentImageID = cat.getID();
                panel.addComponent(cat);

            }
            else if(item.equals("penguin"))
            {
                penguin = XUIResources.getInstance().getImage("penguincol-1.thm.jpeg", 3, 3, 2, 1);
                panel.removeComponent(currentImageID);
                currentImageID = penguin.getID();
                panel.addComponent(penguin);

            }

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
        if(component == comboWindow)
        {
            System.exit(0);
        }
    }

}
