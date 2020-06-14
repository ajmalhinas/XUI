/**
 * @(#)ManagerModel.java    0.1 28/06/2002
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

import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.binding.WindowModel;
import org.purnamaproject.xui.binding.XUITreeModel;
import org.purnamaproject.xui.component.atomic.XUIButton;
import org.purnamaproject.xui.component.atomic.XUITextArea;
import org.purnamaproject.xui.component.atomic.XUITextField;
import org.purnamaproject.xui.component.atomic.XUITree;
import org.purnamaproject.xui.component.atomic.XUITreeNode;
import org.purnamaproject.xui.component.container.toplevel.XUICustomDialog;
import org.purnamaproject.xui.component.container.toplevel.XUISaveFileDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.helpers.XUIComponentFactory;
import org.purnamaproject.xui.helpers.XUIResources;
import org.purnamaproject.xui.XUI;

/**
 * A sample model for the Tree/node example.
 */
public class ManagerModel implements WindowModel, XUITreeModel, ActionModel
{

    /**
     * The reference to the xui document.
     */
    private XUI xui;

    /**
     * The window itself.
     */
    private XUIWindow treeWindow;

    /**
     * The tree itself.
     */
    private XUITree tree;

    /**
     * The list.
     */
    private XUITextArea nodeDescription;

    /**
     * For saving out the XUI XML structure.
     */
    private XUIMenuItem saveAsMenuItem;

    /**
     * For editing the current node.
     */
    private XUIMenuItem EditNodeMenuItem;

    /**
     * For adding a new node to the current node.
     */
    private XUIMenuItem addNodeMenuItem;

    /**
     * FileDialog for opening html files.
     */
    private XUISaveFileDialog saveDialog;

    /**
     * CustomDialog for giving the user the opportunity to say no
     * to deleting the currently selected node.
     */
    private XUICustomDialog deleteDialog;

    /**
     * CustomDialog for giving the user the opportunity to edit
     * or back out of editing the name of a node.
     */
    private XUICustomDialog editDialog;

    /**
     * CustomDialog for giving the user the opportunity to add
     * a new child node to the currently selected node.
     */
    private XUICustomDialog addDialog;

    /**
     * Triggers event to delete the currently selected node.
     */
    private XUIButton deleteNodeButton;

    /**
     * Cancels the operation of deleting the currently selected
     * node.
     */
    private XUIButton noDeleteNodeButton;

    /**
     * Triggers event to edit the currently selected node.
     */
    private XUIButton editNodeButton;

    /**
     * Cancels the operation of editing the currently selected
     * node.
     */
    private XUIButton noEditNodeButton;

    /**
     * Triggers event to add a new node to the currently selected node.
     */
    private XUIButton addNodeButton;

    /**
     * Cancels the operation of adding a new node to the currently
     * selected node.
     */
    private XUIButton noAddNodeButton;

    /**
     * Popup that allows for a node to be deleted.
     */
    private XUIMenuItem deleteNodeMenuItem;

    /**
     * The current tree node used in editing, deleting and adding to.
     */
    private XUITreeNode currentTreeNode;

    /**
     * The text field used to edit a node.
     */
    private XUITextField editField;

    /**
     * The text field used to add a node (actually only add the textual value
     * but the node is created for you.
     */
    private XUITextField addField;

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

        treeWindow =         (XUIWindow)xui.getXUIComponent("window_0");
        tree =               (XUITree)xui.getXUIComponent("tree_0");
        nodeDescription =    (XUITextArea)xui.getXUIComponent("textarea_0");
        saveAsMenuItem =     (XUIMenuItem)xui.getXUIComponent("mi_1");
        deleteDialog =       (XUICustomDialog)xui.getXUIComponent("customdialog_1");
        editDialog =         (XUICustomDialog)xui.getXUIComponent("customdialog_2");
        saveDialog =         (XUISaveFileDialog)xui.getXUIComponent("savedialog_0");
        addDialog =          (XUICustomDialog)xui.getXUIComponent("customdialog_3");

        deleteNodeButton =   (XUIButton)xui.getXUIComponent("button_1");
        noDeleteNodeButton = (XUIButton)xui.getXUIComponent("button_2");
        editNodeButton =     (XUIButton)xui.getXUIComponent("button_4");
        noEditNodeButton =   (XUIButton)xui.getXUIComponent("button_5");
        addNodeButton =      (XUIButton)xui.getXUIComponent("button_6");
        noAddNodeButton =    (XUIButton)xui.getXUIComponent("button_7");


        deleteNodeMenuItem = (XUIMenuItem)xui.getXUIComponent("mi_4");
        EditNodeMenuItem =   (XUIMenuItem)xui.getXUIComponent("mi_2");
        addNodeMenuItem =    (XUIMenuItem)xui.getXUIComponent("mi_3");

        editField =          (XUITextField)xui.getXUIComponent("field_0");
        addField =           (XUITextField)xui.getXUIComponent("field_1");


        treeWindow.addEventListener(this);
        tree.addEventListener(this);
        saveDialog.addEventListener(this);
        saveAsMenuItem.addEventListener(this);
        addNodeMenuItem.addEventListener(this);
        noDeleteNodeButton.addEventListener(this);
        deleteNodeButton.addEventListener(this);
        deleteNodeMenuItem.addEventListener(this);
        EditNodeMenuItem.addEventListener(this);
        editNodeButton.addEventListener(this);
        noEditNodeButton.addEventListener(this);
        addNodeButton.addEventListener(this);
        noAddNodeButton.addEventListener(this);
    }

    /**
     * This method is called whenever a component has been selected/chosen.
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        if(component == saveDialog)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(saveDialog.getSelectedFile());
                xui.marshalXUI(fos);

            } catch (FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        } else if(component == saveAsMenuItem)
        {
            saveDialog.setVisible(true);
        } else if(component == deleteNodeButton)
        {

            if(currentTreeNode != null)
            {
                tree.deleteTreeNode(currentTreeNode);
                nodeDescription.setText("");
            }

            deleteDialog.setVisible(false);

        } else if(component == noDeleteNodeButton)
        {
            deleteDialog.setVisible(false);

        } else if(component == deleteNodeMenuItem)
        {
            deleteDialog.setVisible(true);

        } else if(component == EditNodeMenuItem)
        {
            editDialog.setVisible(true);

        } else if(component == noEditNodeButton)
        {
            editDialog.setVisible(false);

        } else if(component == editNodeButton)
        {
            if(currentTreeNode != null)
            {
                tree.editTreeNode(currentTreeNode, editField.getText());
                nodeDescription.setText(currentTreeNode.toString());
                editDialog.setVisible(false);
                editField.setText("");
            }
        } else if(component == addNodeButton)
        {
            XUITreeNode newNode = tree.addTreeNode(currentTreeNode, addField.getText());

            addDialog.setVisible(false);
            addField.setText("");

        } else if(component == noAddNodeButton)
        {
            addDialog.setVisible(false);

        } else if(component == addNodeMenuItem)
        {
            addDialog.setVisible(true);

        }
    }

    /**
     * This method is called whenever a node is selected.
     *
     * @param component the component that generated the event.
     * @param selectedTreeNode the currently selected XUITreeNode. If there is no node
     * selected, this will be null.
     */
    public void treeAction(XUIComponent component, XUITreeNode xuiTreeNode)
    {
        if(component == tree)
        {
            if(xuiTreeNode != null)
            {
                currentTreeNode = xuiTreeNode;
                nodeDescription.setText(currentTreeNode.toString());
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
        if(component == treeWindow)
        {
            System.exit(0);
        }
    }

}
