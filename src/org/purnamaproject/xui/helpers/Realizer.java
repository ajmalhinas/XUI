package org.purnamaproject.xui.helpers;

/**
 * @(#)Realizer.java    0.5 18/08/2003
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

import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import org.purnamaproject.xui.component.atomic.XUIButton;
import org.purnamaproject.xui.component.atomic.XUICheckBox;
import org.purnamaproject.xui.component.atomic.XUIComboBox;
import org.purnamaproject.xui.component.atomic.XUIImage;
import org.purnamaproject.xui.component.atomic.XUILabel;
import org.purnamaproject.xui.component.atomic.XUIList;
import org.purnamaproject.xui.component.atomic.XUIPasswordField;
import org.purnamaproject.xui.component.atomic.XUIProgressBar;
import org.purnamaproject.xui.component.atomic.XUIRadioButton;
import org.purnamaproject.xui.component.atomic.XUISliderBar;
import org.purnamaproject.xui.component.atomic.XUITable;
import org.purnamaproject.xui.component.atomic.XUITextArea;
import org.purnamaproject.xui.component.atomic.XUITextField;
import org.purnamaproject.xui.component.atomic.XUITree;
import org.purnamaproject.xui.component.composite.XUICalendar;
import org.purnamaproject.xui.component.composite.XUIHypertextPane;
import org.purnamaproject.xui.component.container.XUIContainer;
import org.purnamaproject.xui.component.container.intermediate.XUIPanel;
import org.purnamaproject.xui.component.container.intermediate.XUISplitPanel;
import org.purnamaproject.xui.component.container.intermediate.XUITabbedPanel;
import org.purnamaproject.xui.component.container.toplevel.XUIBasicDialog;
import org.purnamaproject.xui.component.container.toplevel.XUICustomDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIOpenFileDialog;
import org.purnamaproject.xui.component.container.toplevel.XUISaveFileDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.menu.XUIMenu;
import org.purnamaproject.xui.component.menu.XUIMenuBar;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.impl.XUIBasicDialogImpl;
import org.purnamaproject.xui.impl.XUIButtonImpl;
import org.purnamaproject.xui.impl.XUICalendarImpl;
import org.purnamaproject.xui.impl.XUICheckBoxImpl;
import org.purnamaproject.xui.impl.XUIComboBoxImpl;
import org.purnamaproject.xui.impl.XUICustomDialogImpl;
import org.purnamaproject.xui.impl.XUIHypertextPaneImpl;
import org.purnamaproject.xui.impl.XUIImageImpl;
import org.purnamaproject.xui.impl.XUILabelImpl;
import org.purnamaproject.xui.impl.XUIListImpl;
import org.purnamaproject.xui.impl.XUIMenuBarImpl;
import org.purnamaproject.xui.impl.XUIMenuImpl;
import org.purnamaproject.xui.impl.XUIMenuItemImpl;
import org.purnamaproject.xui.impl.XUINodeImpl;
import org.purnamaproject.xui.impl.XUIOpenFileDialogImpl;
import org.purnamaproject.xui.impl.XUIPanelImpl;
import org.purnamaproject.xui.impl.XUIPasswordFieldImpl;
import org.purnamaproject.xui.impl.XUIProgressBarImpl;
import org.purnamaproject.xui.impl.XUIRadioButtonImpl;
import org.purnamaproject.xui.impl.XUISaveFileDialogImpl;
import org.purnamaproject.xui.impl.XUISliderBarImpl;
import org.purnamaproject.xui.impl.XUISplitPanelImpl;
import org.purnamaproject.xui.impl.XUITabbedPanelImpl;
import org.purnamaproject.xui.impl.XUITableImpl;
import org.purnamaproject.xui.impl.XUITextAreaImpl;
import org.purnamaproject.xui.impl.XUITextFieldImpl;
import org.purnamaproject.xui.impl.XUITreeImpl;
import org.purnamaproject.xui.impl.XUIWindowImpl;
import org.purnamaproject.xui.XUIDisplayException;
import org.purnamaproject.xui.XUINode;
import org.purnamaproject.xui.XUI;


/**
 * Realizer is a class that, using a XUI document, builds the user interface based on
 * the nodes of that xml document.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class Realizer
{
    /**
     * The self-reference.
     */
    private static Realizer realizer = null;

    /**
     * Reference to the XUI document.
     */
    private static XUI document;

    /**
     * Private constructor.
     */
    private Realizer()
    {
        ;
    }

    /**
     * Builds a user interface based on the XUI document that is passed to it.
     *
     * @param xui the XUI document that is used to build a user interface from.
     */
    private Realizer(XUI xui)
    {
        if(xui == document)
            ;
        else
            document = xui;
    }

    /**
     * Returns the sole instance of this class Realizer.
     */
    public static Realizer getInstance(XUI xui)
    {
        if(realizer == null)
            realizer = new Realizer(xui);
        return realizer;
    }

    /**
     * Based on the xui document, build the user interface ("realize it").
     */
    public void realize() throws XUIDisplayException
    {
        // build the GUI if not null
        if(document != null)
        {
            // get any or all windows
            List windows = document.getNodesByName("Window");
            // cycle through the windows
            for(int i = 0; i < windows.size(); i++)
            {
                XUINode windowNode = (XUINode)windows.get(i);
                // build node's native peer
                XUIWindow window = new XUIWindowImpl(windowNode);

                // get children and realize them as well
                List containers = windowNode.getDirectChildren();
                for(int z = 0; z < containers.size(); z++)
                {
                    XUINode container = (XUINode)containers.get(z);
                    doContainer(container, window);
                }

                // show the window now
                if(window.isVisible())
                    window.visualize();
            }

        } else
            throw new XUIDisplayException("XUI document must not be null.");
    }

    /**
     * Takes a node and creates a GUI component based on its type. This method deals with
     * containers and may be called recursively if a top level or intermediate container
     * has another container within it.
     *
     * @param containerNode the node which represents a type of container.
     * @param window the XUIWindow which is the top-level container owning this container.
     */
    private void doContainer(XUINode containerNode, XUIWindow window)
    {
        String type = containerNode.getName();

        if(type.equals("Panel"))
        {
            XUIPanel panel = new XUIPanelImpl(containerNode);
            // add the panel to the window
            window.addComponent(panel);

            // go through the list for atomics and realize them
            List atomics = containerNode.getDirectChildren();
            for(int z = 0; z < atomics.size(); z++)
            {
                XUINode atomic = (XUINode)atomics.get(z);
                doAtomic(atomic, panel);
            }

        } else if(type.equals("BasicDialog"))
        {
            XUIBasicDialog dialog = new XUIBasicDialogImpl(containerNode);

            // add the dialog to the window
            window.addComponent(dialog);

            // show the dialog
            dialog.visualize();

        } else if(type.equals("OpenFileDialog"))
        {
            XUIOpenFileDialog dialog = new XUIOpenFileDialogImpl(containerNode);

            // add the dialog to the window
            window.addComponent(dialog);

            // show the dialog
            dialog.visualize();

        } else if(type.equals("SaveFileDialog"))
        {
            XUISaveFileDialog dialog = new XUISaveFileDialogImpl(containerNode);

            // add the dialog to the window
            window.addComponent(dialog);

            // show the dialog
            dialog.visualize();

        } else if(type.equals("CustomDialog"))
        {
            XUICustomDialog dialog = new XUICustomDialogImpl(containerNode);

            // add the dialog to the window
            window.addComponent(dialog);

            // add panels to it
            List panels = containerNode.getChildNodesByName("Panel");
            for(int j = 0; j < panels.size(); j++)
            {

                XUINode panelNode = (XUINode)panels.get(j);
                XUIPanel panel = new XUIPanelImpl(panelNode);
                dialog.addComponent(panel);
                List panelAtomics = panelNode.getDirectChildren();
                for(int i = 0; i < panelAtomics.size(); i++)
                {
                    XUINode atomic = (XUINode)panelAtomics.get(i);
                    doAtomic(atomic, panel);
                }
            }
            // show the dialog
            dialog.visualize();


        } else if(type.equals("MenuBar"))
        {
            XUIMenuBar menuBar = new XUIMenuBarImpl(containerNode);
            // add the menubar to the window
            window.addComponent(menuBar);

            // go through the list of menus and submenus and realize them
            List menuComponents = containerNode.getDirectChildren();
            for(int z = 0; z < menuComponents.size(); z++)
            {
                XUINode menuComponentNode = (XUINode)menuComponents.get(z);
                doMenus(menuComponentNode, menuBar);
            }

        } else if(type.equals("SplitPanel"))
        {
            XUISplitPanel split = new XUISplitPanelImpl(containerNode);
            // add the split panel to the window
            window.addComponent(split);

            // add panels to it
            List panels = containerNode.getChildNodesByName("Panel");
            int size = panels.size();
            if(size == 0)
                ; // nothing to add
            else if(size == 1)
            {
                // add the first and only panel to the split panel
                XUIPanel panelOne = new XUIPanelImpl((XUINode)panels.get(0));
                split.addComponent(panelOne, XUIUtils.PLACEMENT_LEFT_OR_TOP);

                // have to add the panel's components now ...
                List atomics = ((XUINode)panels.get(0)).getDirectChildren();
                for(int z = 0; z < atomics.size(); z++)
                {
                    XUINode atomic = (XUINode)atomics.get(z);
                    doAtomic(atomic, panelOne);
                }


            } else if(size == 2)
            {
                // add both one top/left, the other right/bottom
                XUIPanel panelOne = new XUIPanelImpl((XUINode)panels.get(0));
                split.addComponent(panelOne, XUIUtils.PLACEMENT_LEFT_OR_TOP);
                XUIPanel panelTwo = new XUIPanelImpl((XUINode)panels.get(1));
                split.addComponent(panelTwo, XUIUtils.PLACEMENT_RIGHT_OR_BOTTOM);

                // panel one's children
                List atomics1 = ((XUINode)panels.get(0)).getDirectChildren();
                for(int z = 0; z < atomics1.size(); z++)
                {
                    XUINode atomic = (XUINode)atomics1.get(z);
                    doAtomic(atomic, panelOne);
                }

                // panel two's children
                List atomics2 = ((XUINode)panels.get(1)).getDirectChildren();
                for(int z = 0; z < atomics2.size(); z++)
                {
                    XUINode atomic = (XUINode)atomics2.get(z);
                    doAtomic(atomic, panelTwo);
                }


            }

        } else if(type.equals("TabbedPanel"))
        {
            XUITabbedPanel tabbed = new XUITabbedPanelImpl(containerNode);
            // add the split panel to the window
            window.addComponent(tabbed);

            // add panels to it
            List panels = containerNode.getChildNodesByName("Panel");
            for(int j = 0; j < panels.size(); j++)
            {

                XUINode panelNode = (XUINode)panels.get(j);
                XUIPanel panel = new XUIPanelImpl(panelNode);
                tabbed.addComponent(panel);
                List panelAtomics = panelNode.getDirectChildren();
                for(int i = 0; i < panelAtomics.size(); i++)
                {

                    XUINode atomic = (XUINode)panelAtomics.get(i);
                    doAtomic(atomic, panel);
                }
            }

        }
    }

    /**
     * Builds menus and menu items based on the menubar that is the start of the hierarchy.
     *
     * @param menuComponentNode the node representing a child component (i.e. XUIMenu, XUIMenuItem)
     * that is to be added to the parent (menuBar).
     * @param menuBar either the XUIMenuBar or XUIMenu that is a parent of the child component.
     */
    private void doMenus(XUINode menuComponentNode, XUIMenuBar menuBar)
    {

        String type = menuComponentNode.getName();
        if(type.equals("Menu"))
        {
            XUIMenu menu = new XUIMenuImpl(menuComponentNode);

            if(menuBar instanceof XUIMenu)
                ((XUIMenu)menuBar).addMenuForRealizer(menu);
            else
                menuBar.addMenuForRealizer(menu);

            // go through the list of menus and submenus and realize them
            List menuComponents = menuComponentNode.getDirectChildren();
            for(int z = 0; z < menuComponents.size(); z++)
            {

                XUINode childNode = (XUINode)menuComponents.get(z);
                doMenus(childNode, menu);
            }

        } else if(type.equals("MenuItem"))
        {
            if(menuBar instanceof XUIMenu)
            {
                XUIMenuItem menuItem = new XUIMenuItemImpl(menuComponentNode);
                // add menu item to the menubar
                ((XUIMenu)menuBar).addMenuItemForRealizer(menuItem);
            }
        }

    }

    /**
     * Builds atomic components based on the container that is given.
     *
     * @param atomicNode the node representing an atomic component.
     * @param container the container that owns this atomic component.
     */
    private void doAtomic(XUINode atomicNode, XUIContainer container)
    {
        // this is required since the pipeline is different from
        // this class (Realizer) than from creating the XUI from
        // scratch within the API calls. We need to differentiate.
        atomicNode.setFromRealizer(true);

        String type = atomicNode.getName();
        if(type.equals("Button"))
        {
            XUIButton button = new XUIButtonImpl(atomicNode);
            // add the button to the container
            container.addComponent(button);

        } else if(type.equals("Image"))
        {
            XUIImage image = new XUIImageImpl(atomicNode);
            // add the image to the container
            container.addComponent(image);

        } else if(type.equals("Calendar"))
        {
            XUICalendar calendar = new XUICalendarImpl(atomicNode);
            // add the Calendar to the container
            container.addComponent(calendar);

        } else if(type.equals("CheckBox"))
        {
            XUICheckBox checkbox = new XUICheckBoxImpl(atomicNode);
            // add the checkbox to the container
            container.addComponent(checkbox);

        } else if(type.equals("ComboBox"))
        {
            XUIComboBox combobox = new XUIComboBoxImpl(atomicNode);
            // add the image to the container
            container.addComponent(combobox);

        } else if(type.equals("HypertextPane"))
        {
            XUIHypertextPane hyper = new XUIHypertextPaneImpl(atomicNode);
            // add the image to the container
            container.addComponent(hyper);

        } else if(type.equals("Label"))
        {
            XUILabel label = new XUILabelImpl(atomicNode);
            // add the image to the container
            container.addComponent(label);

        } else if(type.equals("List"))
        {
            XUIList list = new XUIListImpl(atomicNode);
            // add the image to the container
            container.addComponent(list);

        } else if(type.equals("PasswordField"))
        {
            XUIPasswordField pwdfield = new XUIPasswordFieldImpl(atomicNode);
            // add the image to the container
            container.addComponent(pwdfield);

        } else if(type.equals("ProgressBar"))
        {
            XUIProgressBar progress = new XUIProgressBarImpl(atomicNode);
            // add the image to the container
            container.addComponent(progress);

        } else if(type.equals("RadioButton"))
        {
            XUIRadioButton radioButton = new XUIRadioButtonImpl(atomicNode);
            // add the image to the container
            container.addComponent(radioButton);

        } else if(type.equals("SliderBar"))
        {
            XUISliderBar slider = new XUISliderBarImpl(atomicNode);
            // add the image to the container
            container.addComponent(slider);

        } else if(type.equals("Table"))
        {
            XUITable table = new XUITableImpl(atomicNode);
            // add the image to the container
            container.addComponent(table);

        } else if(type.equals("TextArea"))
        {
            XUITextArea textArea = new XUITextAreaImpl(atomicNode);
            // add the image to the container
            container.addComponent(textArea);

        } else if(type.equals("TextField"))
        {
            XUITextField textField = new XUITextFieldImpl(atomicNode);
            // add the image to the container
            container.addComponent(textField);

        } else if(type.equals("Tree"))
        {
            XUITree tree = new XUITreeImpl(atomicNode);
            // add the image to the container
            container.addComponent(tree);

        }
    }

}
