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

import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.ImageIcon;
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
import org.purnamaproject.xui.component.atomic.XUITreeNode;
import org.purnamaproject.xui.component.composite.XUICalendar;
import org.purnamaproject.xui.component.composite.XUIHypertextPane;
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

/**
 * This class looks after the creation of GUI components and passes them on as the
 * XUIComponent type and subtypes.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class XUIComponentFactory
{
    /**
     * Default constructor. Private.
     */
    private XUIComponentFactory()
    {
        ;
    }

    /**
     * Returns a XUIWindow that is ready to be inserted into the DOM. Default settings are used
     * until proper user values are entered. Has the default size of 200 x 200 and the default screen
     * coordinates of 0,0.
     *
     * @return a XUIWindow.
     * @see org.purnamaproject.xui.XUIBuilder
     */
    public static XUIWindow makeWindow()
    {
        // make the XUI window which will create a Swing JWindow
        XUIWindow window = new XUIWindowImpl();
        return window;
    }

    /**
     * Returns a XUIWindow that is ready to be inserted into the DOM. Accepts the title, width and
     * height of this window. Has the default coordinates 0,0.
     *
     * @return a XUIWindow.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the window.
     * @param height the height of the window.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the title is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUIWindow makeWindow(String title, int width, int height)
        throws XUIDisplayException
    {
        // make the XUI window which will create a Swing JWindow
        XUIWindow window = new XUIWindowImpl(title, width, height);
        return window;
    }

    /**
     * Returns a XUIWindow that is ready to be inserted into the DOM. Accepts the title, width,
     * x and y coordinates of this window.
     *
     * @return a XUIWindow.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the window.
     * @param height the height of the window.
     * @param xCoordinate the x coordinate to use to place this component somewhere on the screen.
     * @param yCoordinate the y coordinate to use to place this component somewhere on the screen.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the title is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIWindow makeWindow(String title, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI window which will create a Swing JWindow
        XUIWindow window = new XUIWindowImpl(title, width, height);
        window.setComponentLocation(xCoordinate, yCoordinate);
        return window;
    }

    /**
     * Returns a XUICustomDialog that is ready to be inserted into the DOM. Has default coordinates of
     * 0,0 and a default size of 280 x 220 and is by default modal.
     *
     * @return a XUICustomDialog.
     * @param window the parent of this dialog.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUICustomDialog makeCustomDialog(XUIWindow window) throws XUIDisplayException
    {
        // make the XUI dialog
        XUICustomDialog dialog = new XUICustomDialogImpl(window);
        return dialog;
    }

    /**
     * Returns a XUICustomDialog that is ready to be inserted into the DOM. Creates a dialog that has
     * a parent window, a width and a height. Has the default coordinates 0,0. Is modal by default.
     *
     * @return a XUICustomDialog.
     * @param window the parent of this dialog.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the dialog.
     * @param height the height of the dialog.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUICustomDialog makeCustomDialog(XUIWindow window, String title, int width, int height)
        throws XUIDisplayException
    {
        // make the XUI dialog
        XUICustomDialog dialog = new XUICustomDialogImpl(window, title, width, height);
        return dialog;
    }

    /**
     * Returns a XUICustomDialog that is ready to be inserted into the DOM. Creates a dialog that has
     * a parent window, a width, height and x and y coordinates.
     *
     * @return a XUICustomDialog.
     * @param title the title to be displayed in the titlebar of the dialog.
     * @param width the width of the dialog.
     * @param height the height of the dialog.
     * @param window the parent of this dialog.
     * @param xCoordinate the x coordinate to use to place this component somewhere on the screen.
     * @param yCoordinate the y coordinate to use to place this component somewhere on the screen.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUICustomDialog makeCustomDialog(XUIWindow window, String title, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI dialog
        XUICustomDialog dialog = new XUICustomDialogImpl(window, title, width, height);
        dialog.setComponentLocation(xCoordinate, yCoordinate);
        return dialog;
    }

    /**
     * Returns a XUIOpenFileDialog that is ready to be inserted into the DOM. The dialog has a parent
     * window, a title and a width and height. Has the default coordinates 0,0. Is modal by default.
     *
     * @return a XUIOpenFileDialog.
     * @param window the parent of this dialog.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the dialog.
     * @param height the height of the dialog.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUIOpenFileDialog makeOpenFileDialog(XUIWindow window, String title, int width, int height)
        throws XUIDisplayException
    {
        // make the XUI dialog
        XUIOpenFileDialog dialog = new XUIOpenFileDialogImpl(window, title);
        dialog.setComponentSize(width, height);
        return dialog;
    }

    /**
     * Returns a XUIOpenFileDialog that is ready to be inserted into the DOM. The dialog has a parent
     * window, a title, a width and height and x and y coordinates. Is modal by default.
     *
     * @return a XUIOpenFileDialog.
     * @param title the title to be displayed in the titlebar of the dialog.
     * @param width the width of the dialog.
     * @param height the height of the dialog.
     * @param window the parent of this dialog.
     * @param xCoordinate the x coordinate to use to place this component somewhere on the screen.
     * @param yCoordinate the y coordinate to use to place this component somewhere on the screen.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIOpenFileDialog makeOpenFileDialog(XUIWindow window, String title, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI dialog
        XUIOpenFileDialog dialog = new XUIOpenFileDialogImpl(window, title);
        dialog.setComponentLocation(xCoordinate, yCoordinate);
        dialog.setComponentSize(width, height);
        return dialog;
    }

    /**
     * Returns a XUIBasicDialog that is ready to be inserted into the DOM. Has a parent window, message,
     * its type, title, width and height. Has the default coordinates 0,0.
     *
     * @return a XUIBasicDialog.
     * @param window the parent of this dialog.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the dialog.
     * @param message the message to place in the dialog
     * @param type the type that this dialog should be. The 4 types are question, information, warning and error.
     * @param height the height of the dialog.
     * @see org.purnamaproject.xui.XUIBuilder
     * @see org.purnamaproject.xui.helpers.XUIUtils#QUESTION_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#INFORMATION_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#WARNING_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#ERROR_TYPE
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUIBasicDialog makeBasicDialog(XUIWindow window, String message, byte type,
        String title, int width, int height) throws XUIDisplayException
    {
        // make the XUI dialog
        XUIBasicDialog dialog = new XUIBasicDialogImpl(window, message, title, type);
        dialog.setComponentSize(width, height);
        return dialog;
    }

    /**
     * Returns a XUIBasicDialog that is ready to be inserted into the DOM. Has a parent window, message,
     * its type, title, width and height and x and y coordinates. Is modal by default.
     *
     * @return a XUIBasicDialog.
     * @param window the parent of this dialog.
     * @param title the title to be displayed in the titlebar of the window.
     * @param width the width of the dialog.
     * @param message the message to place in the dialog
     * @param type the type that this dialog should be. The 4 types are question, information, warning and error.
     * @param height the height of the dialog.
     * @param xCoordinate the x coordinate to use to place this component somewhere on the screen.
     * @param yCoordinate the y coordinate to use to place this component somewhere on the screen.
     * @see org.purnamaproject.xui.XUIBuilder
     * @see org.purnamaproject.xui.helpers.XUIUtils#QUESTION_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#INFORMATION_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#WARNING_TYPE
     * @see org.purnamaproject.xui.helpers.XUIUtils#ERROR_TYPE
     * @throws org.purnamaproject.xui.XUIDisplayException if the window is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIBasicDialog makeBasicDialog(XUIWindow window, String message, byte type,
        String title, int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI dialog
        XUIBasicDialog dialog = new XUIBasicDialogImpl(window, message, title, type);
        dialog.setComponentLocation(xCoordinate, yCoordinate);
        dialog.setComponentSize(width, height);
        return dialog;
    }

    /**
     * Returns a XUIPanel that is ready to be inserted into the DOM. Has width and height with default coordinates
     * of 0,0.
     *
     * @return a XUIPanel.
     * @param title the title to be displayed in the titlebar of the panel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the title is null or width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUIPanel makePanel(String title, int width, int height)
        throws XUIDisplayException
    {
        // make the XUI panel
        XUIPanel panel = new XUIPanelImpl(title, width, height);
        return panel;
    }

    /**
     * Returns a XUIPanel that is ready to be inserted into the DOM. Panel has a title, width, height and
     * x and y coordinates.
     *
     * @return a XUIPanel.
     * @param title the title to be displayed in the titlebar of the panel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIPanel makePanel(String title, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI panel
        XUIPanel panel = new XUIPanelImpl(title, width, height);
        panel.setComponentLocation(xCoordinate, yCoordinate);
        return panel;
    }

    /**
     * Returns a XUISplitPanel that is ready to be inserted into the DOM. Has the default coordinates 0,0
     * and a default orientation of vertical.
     *
     * @return a XUISplitPanel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUISplitPanel makeSplitPanel(int width, int height)
        throws XUIDisplayException
    {
        // make the XUI panel
        XUISplitPanel panel = new XUISplitPanelImpl(width, height);
        return panel;
    }

    /**
     * Returns a XUISplitPanel that is ready to be inserted into the DOM. Allows to set its orienation,
     * height and width. Has the default coordinates 0,0.
     *
     * @return a XUISplitPanel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @param orientation the orientation for the panes to be split (e.g. 'horizontal' or 'vertical')
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_HORIZONTAL
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_VERTICAL
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the orientation1 is is not a valid value or width or height are
     * outside of the bounds of the XUI schema type.
     */
    public static XUISplitPanel makeSplitPanel(int width, int height, int orientation)
        throws XUIDisplayException
    {
        // make the XUI panel
        XUISplitPanel panel = new XUISplitPanelImpl(width, height, orientation);
        return panel;
    }

    /**
     * Returns a XUISplitPanel that is ready to be inserted into the DOM. Accepts the width, height
     * orientation and x and y coordinates.
     *
     * @return a XUISplitPanel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @param orientation the orientation for the panes to be split (e.g. 'horizontal' or 'vertical')
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_HORIZONTAL
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_VERTICAL
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUISplitPanel makeSplitPanel(int width, int height, int xCoordinate, int yCoordinate,
        int orientation) throws XUIDisplayException
    {
        // make the XUI panel
        XUISplitPanel panel = new XUISplitPanelImpl(width, height, orientation);
        panel.setComponentLocation(xCoordinate, yCoordinate);
        return panel;
    }

    /**
     * Returns a XUITabbedPanel that is ready to be inserted into the DOM. Accepts the width and height
     * width a default coordinate set of 0,0.
     *
     * @return a XUITabbedPanel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height are outside of the bounds of
     * the XUI schema type.
     */
    public static XUITabbedPanel makeTabbedPanel(int width, int height)
        throws XUIDisplayException
    {
        // make the XUI panel
        XUITabbedPanel panel = new XUITabbedPanelImpl(width, height);
        return panel;
    }

    /**
     * Returns a XUITabbedPanel that is ready to be inserted into the DOM. Sets the width, height
     * and x and y coordinates.
     *
     * @return a XUITabbedPanel.
     * @param width the width of the panel.
     * @param height the height of the panel.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITabbedPanel makeTabbedPanel(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI button
        XUITabbedPanel tabbed = new XUITabbedPanelImpl(width, height);
        tabbed.setComponentLocation(xCoordinate, yCoordinate);
        return tabbed;
    }

    /**
     * Returns a XUIButton that is ready to be inserted into the DOM. Accepts the label to be displayed on
     * this button, the width and height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIButton.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the label is null.
     */
    public static XUIButton makeButton(String label, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI button
        XUIButton button = new XUIButtonImpl(label, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIButton that is ready to be inserted into the DOM. Creates a button with an icon,
     * sets its width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIButton.
     * @param image the image to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image is null.
     */
    public static XUIButton makeButton(XUIImage image, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI button
        XUIButton button = new XUIButtonImpl(image, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIButton that is ready to be inserted into the DOM. Creates a button with both an
     * icon and label, sets its width and height and x and y coordinates. The default orientation is
     * horizontal.
     *
     * @return a XUIButton.
     * @param image the image to give this button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image or the label are null.
     */
    public static XUIButton makeButton(XUIImage image, String label, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIButton button = new XUIButtonImpl(label, image, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIButton that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIButton.
     * @param defaultIcon the image for the icon displayed by default.
     * @param pressedIcon the image for the icon displayed when the button is pressed.
     * @param rolloverIcon the image for the icon displayed when the mouse cursor passes over top of the button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null or the label is null.
     */
    public static XUIButton makeButton(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        String label, int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIButton button = new XUIButtonImpl(label, xCoordinate, yCoordinate);
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIButton that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIButton.
     * @param defaultIcon the image displayed by default.
     * @param pressedIcon the image displayed when the button is pressed.
     * @param rolloverIcon the image displayed when the mouse cursor passes over top of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null.
     */
    public static XUIButton makeButton(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIButton button = new XUIButtonImpl();
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIRadioButton that is ready to be inserted into the DOM. Accepts the label to be displayed on
     * this button, the width and height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIRadioButton.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the label is null.
     */
    public static XUIRadioButton makeRadioButton(String label, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI button
        XUIRadioButton button = new XUIRadioButtonImpl(label, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIRadioButton that is ready to be inserted into the DOM. Creates a button with an icon,
     * sets its width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIRadioButton.
     * @param icon the image to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image is null.
     */
    public static XUIRadioButton makeRadioButton(XUIImage icon, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI button
        XUIRadioButton button = new XUIRadioButtonImpl(icon, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIRadioButton that is ready to be inserted into the DOM. Creates a button with both an
     * icon and label, sets its width and height and x and y coordinates. The default orientation is
     * horizontal.
     *
     * @return a XUIRadioButton.
     * @param icon the icon to give this button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image or the label are null.
     */
    public static XUIRadioButton makeRadioButton(XUIImage icon, String label, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIRadioButton button = new XUIRadioButtonImpl(label, icon, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIRadioButton that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIRadioButton.
     * @param defaultIcon the image displayed by default.
     * @param pressedIcon the image displayed when the button is pressed.
     * @param rolloverIcon the image displayed when the mouse cursor passes over top of the button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null or the label is null.
     */
    public static XUIRadioButton makeRadioButton(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        String label, int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIRadioButton button = new XUIRadioButtonImpl(label, xCoordinate, yCoordinate);
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIRadioButton that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUIRadioButton.
     * @param defaultIcon the icon displayed by default.
     * @param pressedIcon the icon displayed when the button is pressed.
     * @param rolloverIcon the icon displayed when the mouse cursor passes over top of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null.
     */
    public static XUIRadioButton makeRadioButton(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI button
        XUIRadioButton button = new XUIRadioButtonImpl();
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUICheckBox that is ready to be inserted into the DOM. Accepts the label to be displayed on
     * this button, the width and height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUICheckBox.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the label is null.
     */
    public static XUICheckBox makeCheckBox(String label, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI check box
        XUICheckBox button = new XUICheckBoxImpl(label, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUICheckBox that is ready to be inserted into the DOM. Creates a button with an icon,
     * sets its width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUICheckBox.
     * @param icon the icon to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image is null.
     */
    public static XUICheckBox makeCheckBox(XUIImage icon, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI check box
        XUICheckBox button = new XUICheckBoxImpl(icon, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUICheckBox that is ready to be inserted into the DOM. Creates a button with both an
     * icon and label, sets its width and height and x and y coordinates. The default orientation is
     * horizontal.
     *
     * @return a XUICheckBox.
     * @param icon the icon to give this button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the image or the label are null.
     */
    public static XUICheckBox makeCheckBox(XUIImage icon, String label, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI check box
        XUICheckBox button = new XUICheckBoxImpl(label, icon, xCoordinate, yCoordinate);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUICheckBox that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUICheckBox.
     * @param defaultIcon the icon displayed by default.
     * @param pressedIcon the icon displayed when the button is pressed.
     * @param rolloverIcon the icon displayed when the mouse cursor passes over top of the button.
     * @param label the label to give this button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null or the label is null.
     */
    public static XUICheckBox makeCheckBox(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        String label, int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI check box
        XUICheckBox button = new XUICheckBoxImpl(label, xCoordinate, yCoordinate);
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUICheckBox that is ready to be inserted into the DOM. Creates a button with all three
     * images - first icon is the default, second one is the button displayed when the icon has the
     * mouse 'rollover' it, the third icon is the image to display when the button is pressed. Also
     * sets the label, width, height and x and y coordinates. The default orientation is horizontal.
     *
     * @return a XUICheckBox.
     * @param defaultIcon the icon displayed by default.
     * @param pressedIcon the icon displayed when the button is pressed.
     * @param rolloverIcon the icon displayed when the mouse cursor passes over top of the button.
     * @param width the width of the button.
     * @param height the height of the button.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if any of the images are null.
     */
    public static XUICheckBox makeCheckBox(XUIImage defaultIcon, XUIImage rolloverIcon, XUIImage pressedIcon,
        int width, int height, int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI check box
        XUICheckBox button = new XUICheckBoxImpl();
        button.addImages(defaultIcon, rolloverIcon, pressedIcon);
        button.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return button;
    }

    /**
     * Returns a XUIComboBox that is ready to be inserted into the DOM. Creates a combo box and
     * sets its width, height and x and y coordinates. The default is that it is not editable.
     *
     * @return a XUIComboBox.
     * @param width the width of the combo box.
     * @param height the height of the combo box.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIComboBox makeComboBox(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI combo box
        XUIComboBox box = new XUIComboBoxImpl(xCoordinate, yCoordinate);
        box.factoryCoordinates(xCoordinate, yCoordinate, width, height);

        return box;
    }

    /**
     * Returns a XUIComboBox that is ready to be inserted into the DOM. Creates a combo box and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUIComboBox.
     * @param isEditable whether or not this combo box is editable or not.
     * @param width the width of the combo box.
     * @param height the height of the combo box.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIComboBox makeComboBox(boolean isEditable, int width, int height, int xCoordinate,
        int yCoordinate) throws XUIDisplayException
    {
        // make the XUI combo box
        XUIComboBox box = new XUIComboBoxImpl(xCoordinate, yCoordinate);
        box.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        box.setEditable(isEditable);
        return box;
    }

    /**
     * Returns a XUICalendar that is ready to be inserted into the DOM. Creates a calendar and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUICalendar.
     * @param width the width of the calendar.
     * @param height the height of the calendar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUICalendar makeCalendar(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI calendar
        XUICalendar calendar = new XUICalendarImpl(xCoordinate, yCoordinate);
        calendar.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return calendar;
    }

    /**
     * Returns a XUITable that is ready to be inserted into the DOM. Creates a tree and
     * sets its width, height and x and y coordinates. Is given a default root node with the value
     * 'node'.
     *
     * @return a XUITable.
     * @param width the width of the table.
     * @param height the height of the table.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITable makeTable(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI tree
        XUITable table = new XUITableImpl(xCoordinate, yCoordinate);
        table.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return table;
    }

    /**
     * Returns a XUITable that is ready to be inserted into the DOM. Creates a tree and
     * sets its width, height and x and y coordinates. Is given a default root node with the value
     * 'node'.
     *
     * @return a XUITable.
     * @param width the width of the table.
     * @param height the height of the table.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @param isEditable whether or not the table cells are editable.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITable makeTable(boolean isEditable, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI tree
        XUITable table = new XUITableImpl(isEditable, xCoordinate, yCoordinate);
        table.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return table;
    }

    /**
     * Returns a XUITree that is ready to be inserted into the DOM. Creates a tree and
     * sets its width, height and x and y coordinates. Is given a default root node with the value
     * 'node'.
     *
     * @return a XUITree.
     * @param width the width of the tree.
     * @param height the height of the tree.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITree makeTree(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI tree
        XUITree tree = new XUITreeImpl(xCoordinate, yCoordinate);
        tree.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return tree;
    }

    /**
     * Returns a XUITree that is ready to be inserted into the DOM. Creates a tree and
     * sets its width, height and x and y coordinates. Is given a default root node with the value
     * 'node'.
     *
     * @return a XUITree.
     * @param width the width of the tree.
     * @param root the root node to set of this tree.
     * @param height the height of the tree.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the tree node is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITree makeTree(XUITreeNode root, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI tree
        XUITree tree = new XUITreeImpl(xCoordinate, yCoordinate, root);
        tree.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return tree;
    }

    /**
     * <p>Returns a XUIImage that is ready to be inserted into the DOM. Creates a image and
     * sets its width, height and x and y coordinates.</p>
     *
     * <p><b>Note: if the image is too large to fit inside of the component, it will be cropped
     * based on the top left corner being 0,0. This is preferred rather than rescaling the image
     * since the image quality will be degraded as well, not all platforms will be capable of
     * performing advanced sampling techniques.</b></p>
     *
     * @return a XUIImage.
     * @param fileName the URL to find the image at.
     * @param width the width of the image.
     * @param height the height of the image.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the image is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIImage makeImage(URL fileName, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI image

        XUIImage image = new XUIImageImpl(fileName, xCoordinate, yCoordinate);
        image.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return image;
    }

    /**
     * Returns a XUILabel that is ready to be inserted into the DOM. Creates a label and
     * sets its width, height and x and y coordinates and sets the text for the label.
     *
     * @return a XUILabel.
     * @param width the width of the label.
     * @param height the height of the label.
     * @param text the text to be placed within this label.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the text is null or width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the text string is null.
     */
    public static XUILabel makeLabel(String text, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI label
        XUILabel label = new XUILabelImpl(xCoordinate, yCoordinate, text);
        label.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return label;
    }

    /**
     * Returns a XUIList that is ready to be inserted into the DOM. Creates a list and
     * sets its width, height and x and y coordinates and sets the text for the list. No items
     * are in this list.
     *
     * @return a XUIList.
     * @param width the width of the list.
     * @param height the height of the list.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIList makeList(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI list
        XUIList list = new XUIListImpl(xCoordinate, yCoordinate);
        list.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return list;
    }

    /**
     * Returns a XUIList that is ready to be inserted into the DOM. Creates a list and
     * sets its width, height and x and y coordinates and sets the text for the list. No items
     * are in this list.
     *
     * @return a XUIList.
     * @param width the width of the list.
     * @param scrollChoice what type of scrolling is desired ('horizontal', 'vertical', 'both' or 'none').
     * @param height the height of the list.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @see org.purnamaproject.xui.helpers.XUIUtils#HORIZONTAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#VERTICAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#NO_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#BOTH_SCROLLING
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the scrollChoice is not a valid value
     */
    public static XUIList makeList(int scrollChoice, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI list
        XUIList list = new XUIListImpl(xCoordinate, yCoordinate, scrollChoice);
        list.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return list;
    }

    /**
     * Returns a XUIHypertextPane that is ready to be inserted into the DOM. Creates a hypertext pane and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUIHypertextPane.
     * @param width the width of the hypertext pane.
     * @param height the height of the hypertext pane.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIHypertextPane makeHypertextPane(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI hyper text pane
        XUIHypertextPane list = new XUIHypertextPaneImpl(xCoordinate, yCoordinate);
        list.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return list;
    }

    /**
     * Returns a XUIHypertextPane that is ready to be inserted into the DOM. Creates a hypertext pane and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUIHypertextPane.
     * @param width the width of the hypertext pane.
     * @param height the height of the hypertext pane.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the url is null.
     * @throws MalformedURLException if the url is malformed.
     */
    public static XUIHypertextPane makeHypertextPane(URL url, int width, int height, int xCoordinate,
        int yCoordinate) throws XUIDisplayException, MalformedURLException
    {
        // make the XUI hyper text pane
        XUIHypertextPane hypertext = new XUIHypertextPaneImpl(url, xCoordinate, yCoordinate);
        hypertext.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return hypertext;
    }

    /**
     * Returns a XUISliderBar that is ready to be inserted into the DOM. Creates a slider bar and
     * sets its width, height and x and y coordinates. Default slider value is 0.
     *
     * @return a XUISliderBar.
     * @param width the width of the slider bar.
     * @param height the height of the slider bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUISliderBar makeSliderBar(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI slider bar
        XUISliderBar slider = new XUISliderBarImpl(xCoordinate, yCoordinate);
        slider.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return slider;
    }

    /**
     * Returns a XUISliderBar that is ready to be inserted into the DOM. Creates a slider bar and
     * sets its width, height and x and y coordinates. Also sets the position.
     *
     * @return a XUISliderBar.
     * @param width the width of the slider bar.
     * @param position the position value on the slider.
     * @param height the height of the slider bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the position is not within the 0 to 100 range.
     */
    public static XUISliderBar makeSliderBar(int width, int height, int xCoordinate, int yCoordinate,
        int position) throws XUIDisplayException
    {
        // make the XUI slider bar
        XUISliderBar slider = new XUISliderBarImpl(xCoordinate, yCoordinate);
        slider.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        slider.setCurrentPosition(position);
        return slider;
    }

    /**
     * Returns a XUIProgressBar that is ready to be inserted into the DOM. Creates a progress bar and
     * sets its width, height and x and y coordinates. Default progress value is 0. No label is given
     * by default.
     *
     * @return a XUIProgressBar.
     * @param width the width of the progress bar.
     * @param height the height of the progress bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIProgressBar makeProgressBar(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI progress bar
        XUIProgressBar progress = new XUIProgressBarImpl(xCoordinate, yCoordinate);
        progress.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return progress;
    }

    /**
     * Returns a XUIProgressBar that is ready to be inserted into the DOM. Creates a progress bar and
     * sets its width, height and x and y coordinates. Default progress value is 0.
     *
     * @return a XUIProgressBar.
     * @param label the label that is displayed within the progress bar (e.g. 'Download Completed').
     * @param width the width of the progress bar.
     * @param height the height of the progress bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the label is null.
     */
    public static XUIProgressBar makeProgressBar(String label, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI progress bar
        XUIProgressBar progress = new XUIProgressBarImpl(xCoordinate, yCoordinate, label);
        progress.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return progress;
    }

    /**
     * Returns a XUIProgressBar that is ready to be inserted into the DOM. Creates a progress bar and
     * sets its width, height and x and y coordinates. No default label given.
     *
     * @return a XUIProgressBar.
     * @param statusValue the progress status. Must be from 0 to 100.
     * @param width the width of the progress bar.
     * @param height the height of the progress bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the progress value is < 0 > 100.
     */
    public static XUIProgressBar makeProgressBar(int statusValue, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI progress bar
        XUIProgressBar progress = new XUIProgressBarImpl(xCoordinate, yCoordinate, statusValue);
        progress.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return progress;
    }

    /**
     * Returns a XUIProgressBar that is ready to be inserted into the DOM. Creates a progress bar and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUIProgressBar.
     * @param statusValue the progress status. Must be from 0 to 100.
     * @param label the label that is displayed within the progress bar (e.g. 'Download Completed').
     * @param width the width of the progress bar.
     * @param height the height of the progress bar.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the progress value is < 0 > 100 or if
     * the label is null.
     */
    public static XUIProgressBar makeProgressBar(int statusValue, String label, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI progress bar
        XUIProgressBar progress = new XUIProgressBarImpl(xCoordinate, yCoordinate, statusValue, label);
        progress.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return progress;
    }

    /**
     * Returns a XUITextField that is ready to be inserted into the DOM. Creates a text field and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUITextField.
     * @param width the width of the text field.
     * @param height the height of the text field.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITextField makeTextField(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI text field
        XUITextField textField = new XUITextFieldImpl(xCoordinate, yCoordinate);
        textField.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return textField;
    }

    /**
     * Returns a XUITextField that is ready to be inserted into the DOM. Creates a text field and
     * sets its width, height and x and y coordinates and its text value.
     *
     * @return a XUITextField.
     * @param width the width of the text field.
     * @param height the height of the text field.
     * @param text the text value to go into this field.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the text is null.
     */
    public static XUITextField makeTextField(String text, int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI text field which
        XUITextField textField = new XUITextFieldImpl(xCoordinate, yCoordinate);
        textField.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        textField.setText(text);
        return textField;
    }

    /**
     * Returns a XUIPasswordField that is ready to be inserted into the DOM. Creates a password text field and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUIPasswordField.
     * @param width the width of the password text field.
     * @param height the height of the password text field.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUIPasswordField makePasswordField(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI password text field
        XUIPasswordField password = new XUIPasswordFieldImpl(xCoordinate, yCoordinate);
        password.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return password;
    }

    /**
     * Returns a XUIPasswordField that is ready to be inserted into the DOM. Creates a password text field and
     * sets its width, height and x and y coordinates and its text value.
     *
     * @return a XUIPasswordField.
     * @param width the width of the password text field.
     * @param height the height of the password text field.
     * @param text the text value to go into this field.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the text is null.
     */
    public static XUIPasswordField makePasswordField(String text, int width, int height, int xCoordinate,
        int yCoordinate) throws XUIDisplayException
    {
        // make the XUI password text field
        XUIPasswordField password = new XUIPasswordFieldImpl(xCoordinate, yCoordinate);
        password.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        password.setText(text);
        return password;
    }

    /**
     * Returns a XUITextArea that is ready to be inserted into the DOM. Creates a password text area and
     * sets its width, height and x and y coordinates.
     *
     * @return a XUITextArea.
     * @param width the width of the password text area.
     * @param height the height of the password text area.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type.
     */
    public static XUITextArea makeTextArea(int width, int height, int xCoordinate, int yCoordinate)
        throws XUIDisplayException
    {
        // make the XUI text area
        XUITextArea area = new XUITextAreaImpl(xCoordinate, yCoordinate);
        area.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        return area;
    }

    /**
     * Returns a XUITextArea that is ready to be inserted into the DOM. Creates a password text area and
     * sets its width, height and x and y coordinates and its text value.
     *
     * @return a XUITextArea.
     * @param width the width of the password text area.
     * @param height the height of the password text area.
     * @param text the text value to go into this field.
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the text is null.
     */
    public static XUITextArea makeTextArea(String text, int width, int height, int xCoordinate,
        int yCoordinate) throws XUIDisplayException
    {
        // make the XUI text area
        XUITextArea area = new XUITextAreaImpl(xCoordinate, yCoordinate);
        area.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        area.setText(text);
        return area;
    }

    /**
     * Returns a XUITextArea that is ready to be inserted into the DOM. Creates a password text area and
     * sets its width, height and x and y coordinates and its text value.
     *
     * @return a XUITextArea.
     * @param width the width of the password text area.
     * @param height the height of the password text area.
     * @param text the text value to go into this field.
     * @param scrollChoice what type of scrolling is desired ('horizontal', 'vertical', 'both' or 'none').
     * @see org.purnamaproject.xui.helpers.XUIUtils#HORIZONTAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#VERTICAL_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#NO_SCROLLING
     * @see org.purnamaproject.xui.helpers.XUIUtils#BOTH_SCROLLING
     * @param xCoordinate the x coordinate to use to place this component within the parent's grid.
     * @param yCoordinate the y coordinate to use to place this component within the parent's grid.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the width or height or x or y coordinates are
     * outside of the bounds of the XUI schema type. Also if the text is null.
     */
    public static XUITextArea makeTextArea(int scrollChoice, String text, int width, int height,
        int xCoordinate, int yCoordinate) throws XUIDisplayException
    {
        // make the XUI text area
        XUITextArea area = new XUITextAreaImpl(xCoordinate, yCoordinate, scrollChoice);
        area.factoryCoordinates(xCoordinate, yCoordinate, width, height);
        area.setText(text);
        return area;
    }

    /**
     * Returns a XUIMenuBar that is ready to be inserted into the DOM.
     *
     * @return a XUIMenuBar.
     * @see org.purnamaproject.xui.XUIBuilder
     */
    public static XUIMenuBar makeMenuBar()
    {
        // make the XUI menu bar
        XUIMenuBar bar = new XUIMenuBarImpl();
        return bar;
    }

    /**
     * Returns a XUIMenu that is ready to be inserted into the DOM.
     *
     * @return a XUIMenu.
     * @see org.purnamaproject.xui.XUIBuilder
     */
    public static XUIMenu makeMenu()
    {
        // make the XUI menu
        XUIMenu menu = new XUIMenuImpl();
        return menu;
    }

    /**
     * Returns a XUIMenu that is ready to be inserted into the DOM. Adds a label to it.
     *
     * @return a XUIMenu.
     * @param label the label displayed in this menu.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the label is null.
     */
    public static XUIMenu makeMenu(String label) throws XUIDisplayException
    {
        // make the XUI menu
        XUIMenu menu = new XUIMenuImpl(label);
        return menu;
    }

    /**
     * Returns a XUIMenuItem that is ready to be inserted into the DOM. Adds a label to it.
     *
     * @return a XUIMenuItem.
     * @param label the label displayed in this menu item.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the label is null.
     */
    public static XUIMenuItem makeMenuItem(String label) throws XUIDisplayException
    {
        // make the XUI menu item
        XUIMenuItem menuItem = new XUIMenuItemImpl(label);
        return menuItem;
    }

    /**
     * Returns a XUIMenuItem that is ready to be inserted into the DOM. Adds an image to it.
     *
     * @return a XUIMenuItem.
     * @see org.purnamaproject.xui.XUIBuilder
     * @param image the image displayed in this menu item.
     * @throws org.purnamaproject.xui.XUIDisplayException if the image is null.
     */
    public static XUIMenuItem makeMenuItem(XUIImage image) throws XUIDisplayException
    {
        // make the XUI menu item
        XUIMenuItem menuItem = new XUIMenuItemImpl(image);
        return menuItem;
    }

    /**
     * Returns a XUIMenuItem that is ready to be inserted into the DOM. Adds an image and a label.
     *
     * @return a XUIMenuItem.
     * @param label the label displayed in this menu item.
     * @param image the image displayed in this menu item.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the image is null or the label is null.
     */
    public static XUIMenuItem makeMenuItem(XUIImage image, String label) throws XUIDisplayException
    {
        // make the XUI menu item
        XUIMenuItem menuItem = new XUIMenuItemImpl(image);
        menuItem.setLabel(label);
        return menuItem;
    }

    /**
     * Returns a XUIMenuItem that is ready to be inserted into the DOM. Adds an image, a label and
     * allows for the setting of whether or not the menu item has a check box on it.
     *
     * @return a XUIMenuItem.
     * @param label the label displayed in this menu item.
     * @param image the image displayed in this menu item.
     * @param hasCheckBox the boolean value that determines if this menu item will display a checkbox.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the image or the label are null.
     */
    public static XUIMenuItem makeMenuItem(XUIImage image, String label, boolean hasCheckBox)
        throws XUIDisplayException
    {
        // make the XUI menu item
        XUIMenuItem menuItem = new XUIMenuItemImpl(label, hasCheckBox);
        menuItem.addImage(image);
        return menuItem;
    }

    /**
     * Returns a XUIMenuItem that is ready to be inserted into the DOM. Adds a label and
     * allows for the setting of whether or not the menu item has a check box on it.
     *
     * @return a XUIMenuItem.
     * @param label the label displayed in this menu item.
     * @param hasCheckBox the boolean value that determines if this menu item will display a checkbox.
     * @see org.purnamaproject.xui.XUIBuilder
     * @throws org.purnamaproject.xui.XUIDisplayException if the label is null.
     */
    public static XUIMenuItem makeMenuItem(String label, boolean hasCheckBox)
        throws XUIDisplayException
    {
        // make the XUI menu item
        XUIMenuItem menuItem = new XUIMenuItemImpl(label, hasCheckBox);
        return menuItem;
    }

}
