
/**
 * @(#)BrowserModel.java    0.1 28/06/2002
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
import java.util.List;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

// for Purnama Project XUI
import org.purnamaproject.xui.binding.ActionModel;
import org.purnamaproject.xui.binding.ListActionModel;
import org.purnamaproject.xui.binding.TextModel;
import org.purnamaproject.xui.binding.WindowModel;
import org.purnamaproject.xui.component.atomic.XUIButton;
import org.purnamaproject.xui.component.atomic.XUIList;
import org.purnamaproject.xui.component.composite.XUIHypertextPane;
import org.purnamaproject.xui.component.container.toplevel.XUICustomDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIOpenFileDialog;
import org.purnamaproject.xui.component.container.toplevel.XUISaveFileDialog;
import org.purnamaproject.xui.component.container.toplevel.XUIWindow;
import org.purnamaproject.xui.component.menu.XUIMenu;
import org.purnamaproject.xui.component.menu.XUIMenuBar;
import org.purnamaproject.xui.component.menu.XUIMenuItem;
import org.purnamaproject.xui.component.XUIComponent;
import org.purnamaproject.xui.helpers.XUIComponentFactory;
import org.purnamaproject.xui.XUI;


/**
 * A sample model for the WebBrowser.
 */
public class BrowserModel implements ActionModel, TextModel, WindowModel, ListActionModel
{

    /**
     * The reference to the xui document.
     */
    private XUI xui;

    /**
     * Goes to the home page.
     */
    private XUIList bookmarksList;

    /**
     * Determines that the browser should be closed down and
     * the program should be stopped.
     */
    private XUIButton yesExit;

    /**
     * User changed their mind ... keep on browsing.
     */
    private XUIButton notDontExit;

    /**
     * Dialog box that allows the user to view bookmarks and delete them from the list/menu.
     */
    private XUICustomDialog bookmarksDialog;

    /**
     * FileDialog for opening html files.
     */
    private XUIOpenFileDialog fileDialog;

    /**
     * FileDialog for opening html files.
     */
    private XUISaveFileDialog saveDialog;

    /**
     * CustomDialog for giving the user the opportunity to say no
     * to exiting the Web browser. For a Web browser, this is overkill.
     * However, it helps demonstrate the API usage.
     */
    private XUICustomDialog exitDialog;

    /**
     * Exits the program.
     */
    private XUIMenuItem exitMenuItem;

    /**
     * Opens the file dialog.
     */
    private XUIMenuItem openMenuItem;

    /**
     * Saves the current page.
     */
    private XUIMenuItem saveMenuItem;

    /**
     * Bookmarks the current page.
     */
    private XUIMenuItem bookmarkMenuItem;

    /**
     * Popup that goes to the home menu.
     */
    private XUIMenuItem popuphomeMenuItem;

    /**
     * Popup that goes to the next page.
     */
    private XUIMenuItem popupprevMenuItem;

    /**
     * Popup that goes to the previous page.
     */
    private XUIMenuItem popupnextMenuItem;

    /**
     * Saves the current page.
     */
    private XUIMenuItem popupsaveasMenuItem;

    /**
     * Bookmarks the current page.
     */
    private XUIMenuItem popupbookmarkMenuItem;

    /**
     * Opens the bookmarks managing dialog.
     */
    private XUIMenuItem manageBookmarksMenuItem;

    /**
     * Goes to the home page.
     */
    private XUIButton homeButton;

    /**
     * The dialog to give the user the chance to say no to exiting. Overkill
     * in a Web browser but something that demonstrates the API call.
     */
    private XUIWindow browserWindow;

    /**
     * Goes to the previous link within the list of previously viewed sites.
     */
    private XUIButton prevButton;

    /**
     * Goes to the next link within the list of previously viewed sites.
     */
    private XUIButton nextButton;

    /**
     * Goes to the next link within the list of previously viewed sites.
     */
    private XUIHypertextPane hyperTextPane;

    /**
     * Represents a queue of previous url links.
     */
    private LinkedList queue = new LinkedList();

    /**
     * Represents a queue of previous url links.
     */
    private LinkedList bookmarks = new LinkedList();

    /**
     * The max size of the queue for previous pages.
     */
    private int max = 10;

    /**
     * Reference to the menuBar so we can add bookmarks to it.
     */
    private XUIMenuBar menuBar;

    /**
     * Bookmarks menu.
     */
    private XUIMenu bookmarksMenu;

    /**
     * The home link.
     */
    private String homeLink = "http://www.w3c.org";

    /**
     * The index of the queue for previous & next pages.
     */
    private int index = 0;

    /**
     * The class that creates menu items on the fly for each new bookmark saved.
     */
    private BrowserLinkModel linkModel;

    /**
     * Returns the XUIHypertextPane.
     *
     * @param returns a reference to the XUIHypertextPane.
     */
    public XUIHypertextPane getHypertextPane()
    {
        return hyperTextPane;
    }

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

        // instantiate the link model.
        linkModel = new BrowserLinkModel(this);

        // getting references to the components so as to bind them to the model's
        // event handling capabilities.
        menuBar =               (XUIMenuBar)xui.getXUIComponent("menuBar_0");
        bookmarksMenu =         (XUIMenu)xui.getXUIComponent("menu_1");
        openMenuItem =          (XUIMenuItem)xui.getXUIComponent("mi_1");
        homeButton =            (XUIButton)xui.getXUIComponent("button_1");
        prevButton =            (XUIButton)xui.getXUIComponent("button_0");
        nextButton =            (XUIButton)xui.getXUIComponent("button_2");
        popupbookmarkMenuItem = (XUIMenuItem)xui.getXUIComponent("mi_9");
        bookmarkMenuItem =      (XUIMenuItem)xui.getXUIComponent("mi_3");
        popuphomeMenuItem =     (XUIMenuItem)xui.getXUIComponent("mi_8");
        popupprevMenuItem =     (XUIMenuItem)xui.getXUIComponent("mi_6");
        popupnextMenuItem =     (XUIMenuItem)xui.getXUIComponent("mi_7");
        popupsaveasMenuItem =   (XUIMenuItem)xui.getXUIComponent("mi_5");
        saveMenuItem =          (XUIMenuItem)xui.getXUIComponent("mi_0");
        hyperTextPane =         (XUIHypertextPane)xui.getXUIComponent("hyper_0");
        fileDialog =            (XUIOpenFileDialog)xui.getXUIComponent("filedialog_0");
        saveDialog =            (XUISaveFileDialog)xui.getXUIComponent("savedialog_0");
        browserWindow =         (XUIWindow)xui.getXUIComponent("window_0");
        exitDialog =            (XUICustomDialog)xui.getXUIComponent("customdialog_1");
        notDontExit =           (XUIButton)xui.getXUIComponent("button_4");
        yesExit =               (XUIButton)xui.getXUIComponent("button_3");
        exitMenuItem =          (XUIMenuItem)xui.getXUIComponent("mi_2");
        bookmarksDialog =       (XUICustomDialog)xui.getXUIComponent("customdialog_0");
        manageBookmarksMenuItem=(XUIMenuItem)xui.getXUIComponent("mi_4");
        bookmarksList =         (XUIList)xui.getXUIComponent("list_0");


        List bookmarks = bookmarksList.getItems();
        for(int i = 0; i < bookmarks.size(); i++)
        {
            String url = (String)bookmarks.get(i);
            XUIMenuItem aMenuItem = XUIComponentFactory.makeMenuItem(url);
            bookmarksMenu.addMenuItem(aMenuItem);
            linkModel.addSource(aMenuItem);
            aMenuItem.addEventListener(linkModel);
        }


        // add this class as a listener to events from these components.
        openMenuItem.addEventListener(this);
        hyperTextPane.addEventListener(this);
        homeButton.addEventListener(this);
        prevButton.addEventListener(this);
        nextButton.addEventListener(this);
        fileDialog.addEventListener(this);
        saveDialog.addEventListener(this);
        popuphomeMenuItem.addEventListener(this);
        popupprevMenuItem.addEventListener(this);
        popupnextMenuItem.addEventListener(this);
        popupsaveasMenuItem.addEventListener(this);
        saveMenuItem.addEventListener(this);
        popupbookmarkMenuItem.addEventListener(this);
        bookmarkMenuItem.addEventListener(this);
        browserWindow.addEventListener(this);
        notDontExit.addEventListener(this);
        yesExit.addEventListener(this);
        exitMenuItem.addEventListener(this);
        manageBookmarksMenuItem.addEventListener(this);
        bookmarksList.addEventListener(this);

        // this is for keeping track of the last 10 URLs visited.
        queue.add(hyperTextPane.getURL());

        // by default buttons should not be available for going backward or forward
        // because no sites have been visited.
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        popupprevMenuItem.setEnabled(false);
        popupnextMenuItem.setEnabled(false);

    }

    /**
     * This method is called whenever a component (XUIList or XUIComboBox).
     *
     * @param component the component that generated the event.
     * @param index the index of the item that was selected/edited/deleted. If deleted, it is the
     * previous index since that item no longer exists. If the index is -1, then no item is selected
     * and/or no item available in the list (0 length list).
     */
    public void listAction(XUIComponent component, byte type, int index)
    {

        if(component == bookmarksList)
        {
            if(type == ListActionModel.DOUBLE_CLICK)
            {
                hyperTextPane.setURL(bookmarksList.getSelectedItem());
                doLink();
            } else if(type == ListActionModel.DELETED)
            {
                bookmarksList.deleteItem(index);
                bookmarksMenu.removeMenuItem((XUIMenuItem)linkModel.removeSource(index));

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

        if(component == browserWindow)
            exitDialog.setVisible(true);

    }

    /**
     * This method is called whenever a component has text entered into it. This can be as simple
     * as a XUITextArea which generates this event every time a character is typed inside of the
     * component. It can also be when a XUIPasswordField or XUITextField have the enter key pressed
     * while the cursor is inside of this component. Finally, it can be when the XUIHypertextPane
     * has text in the form of a URL used to direct the pane to a Web site based on that URL.
     *
     * @param component the component that generated the event.
     */
    public void textAction(XUIComponent component)
    {
        if(component == hyperTextPane)
        {
            doLink();
        }

    }

    /**
     * This method is called whenever a component has been selected/chosen. In our application
     * specific context, we are looking for buttons, menu items and even a file dialog.
     *
     * @param component the component that generated the event.
     */
    public void action(XUIComponent component)
    {
        if(component == openMenuItem)
        {
            fileDialog.setVisible(true);

        } else if(component == homeButton || component == popuphomeMenuItem)
        {
            doHome();

        } else if(component == prevButton || component == popupprevMenuItem)
        {
            doPrevious();

        } else if(component == nextButton || component == popupnextMenuItem)
        {
            doNext();

        } else if(component == fileDialog)
        {
            if(fileDialog.getSelectedFile() !=null)
                hyperTextPane.setURL(fileDialog.getSelectedFileAsURL());

            index++;
            if(index != queue.size())
            {
                nextButton.setEnabled(false);
                popupnextMenuItem.setEnabled(false);
                for(int i = index; i < queue.size(); i++)
                {
                    queue.remove(i);
                }
            }
            queue.add(hyperTextPane.getURL());

            prevButton.setEnabled(true);
            popupprevMenuItem.setEnabled(true);

        } else if(component == saveDialog)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(saveDialog.getSelectedFile());
                hyperTextPane.getDocument().writeTo(fos);

            } catch (FileNotFoundException fnfe)
            {
                fnfe.printStackTrace();
            } catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

        } else if(component == popupsaveasMenuItem || component == saveMenuItem)
        {
            saveDialog.setVisible(true);

        } else if(component == popupbookmarkMenuItem || component == bookmarkMenuItem)
        {
            doBookmark(hyperTextPane.getURL());

        } else if(component == notDontExit)
        {
            exitDialog.setVisible(false);
            browserWindow.setVisible(true);

        } else if(component == yesExit)
        {
            System.exit(0);
        } else if(component == exitMenuItem)
        {
            exitDialog.setVisible(true);

        } else if(component == manageBookmarksMenuItem)
        {
            bookmarksDialog.setVisible(true);

        }

    }

    /**
     * Handles a link (URL) and places it in the queue.
     */
    protected void doLink()
    {
        index++;
        if(index != queue.size())
        {
            nextButton.setEnabled(false);
            for(int i = index; i < queue.size(); i++)
            {
                queue.remove(i);
            }
        }
        queue.add(hyperTextPane.getURL());
        if(queue.size() > max)
        {
            queue.removeFirst();
            index = 10;
        }

        prevButton.setEnabled(true);
        popupprevMenuItem.setEnabled(true);

    }

    /**
     * Handles the home key/menu item.
     */
    private void doPrevious()
    {

        index--;
        hyperTextPane.setURL((String)queue.get(index));
        if(index == 0)
        {
            prevButton.setEnabled(false);
            popupprevMenuItem.setEnabled(false);
        }
        popupnextMenuItem.setEnabled(true);
        nextButton.setEnabled(true);

    }

    /**
     * Handles the home key/menu item.
     */
    private void doNext()
    {

        index++;
        if((index + 1) == queue.size())
        {
            nextButton.setEnabled(false);
            popupnextMenuItem.setEnabled(false);
        }

        hyperTextPane.setURL((String)queue.get(index));

        prevButton.setEnabled(true);
        popupprevMenuItem.setEnabled(true);

    }

    /**
     * Bookmarks the page.
     */
    private void doBookmark(String url)
    {

        bookmarks.add(url);
        bookmarksList.addNewItem(url);
        XUIMenuItem aMenuItem = XUIComponentFactory.makeMenuItem(url);
        bookmarksMenu.addMenuItem(aMenuItem);
        linkModel.addSource(aMenuItem);
        aMenuItem.addEventListener(linkModel);

    }

    /**
     *
     */
    private void removeBookmark(int index)
    {

        bookmarks.remove(index);
        XUIMenuItem itemToRemove = (XUIMenuItem)linkModel.removeSource(index);
        bookmarksMenu.removeMenuItem(itemToRemove);

    }

    /**
     * Handles the home key/menu item.
     */
    private void doHome()
    {

        if(((String)queue.getLast()).equals(homeLink) && index != 0)
            ; // don't add it ... no sense in keeping the same link
              // like 500 times and pressing prev, prev, prev ...
        else
        {
            hyperTextPane.setURL(homeLink);
            prevButton.setEnabled(true);
            popupprevMenuItem.setEnabled(true);
            queue.add(homeLink);
            index++;
        }

    }

}
