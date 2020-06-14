package org.purnamaproject.xui.peer;

/**
 * @(#)JWebBrowser.java 0.5 18/08/2003
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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultEditorKit;

/**
 * Encompasses a simple Web Browser that has a status bar as well as a text field for putting in a
 * valid URL to display. It supports Java's HTML 3.2 support.
 * @version 0.5 18/08/2003
 * @author  Arron Ferguson
 */
public class JWebBrowser extends JPanel
{
    /**
     * The pane for displaying the hypertext.
     */
    private JEditorPane hyperPane;

    /**
     * The status bar that gives feedback.
     */
    private JLabel statusBar;

    /**
     * The text field for entering a URL to jump to.
     */
    private JTextField enterField;

    /**
     * The scroll pane used to put the hyper text pane in to add scroll
     * capabilities.
     */
    private JScrollPane scroller;

    /**
     * The starting page.
     */
    private String startPage = "./index.html";

    /**
     * The link listener that handles links and directs the browser to a specific URL.
     */
    private XUILinkListener listener;

    /**
     * Constructs the basic Web browser panel.
     */
    public JWebBrowser()
    {
        // set GUI components up for display
        setLayout(new BorderLayout());
        statusBar = new JLabel("Ready.");
        enterField = new JTextField(startPage);

//      try
//      {
            hyperPane = new JEditorPane();
//      } catch(IOException e)
//      {
//          statusBar.setText("Cound not open that url.");
//          enterField.setText(startPage);
//      }
        hyperPane.setEditable(false);
        hyperPane.setEditorKitForContentType("text/html", new XUIHTML());
        scroller = new JScrollPane(hyperPane);
        enterField.setColumns(20);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(new JLabel("URL: "));
        panel.add(enterField);
        add(panel, BorderLayout.NORTH);
        add(statusBar, BorderLayout.SOUTH);
        add(scroller, BorderLayout.CENTER);

        listener = new XUILinkListener(hyperPane, statusBar, enterField);
        hyperPane.addHyperlinkListener(listener);

        // add event handling for the textfield ... need to get the URL from it
        enterField.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    hyperPane.setPage(e.getActionCommand());

                } catch(IOException ioe)
                {
                    statusBar.setText(ioe.getMessage());
                }
            }
        });

    }

    /**
     * Returns the text field for this component.
     *
     * @return the field that is used to enter in a valid URL.
     */
    public JTextField getField()
    {
        return enterField;
    }

    /**
     * Returns the status field for this component.
     *
     * @return the status field.
     */
    public JLabel getStatusField()
    {
        return statusBar;
    }

    /**
     * Sets the text of this field.
     *
     * @param text the text string to place in this text field.
     */
    public void setField(String text)
    {
        enterField.setText(text);
    }

    /**
     * Sets the text of this status bar.
     *
     * @param text the text string to place in this status bar.
     */
    public void setStatusField(String text)
    {
        statusBar.setText(text);
    }

    /**
     * Returns the editor pane.
     *
     * @return the editor pane.
     */
    public JEditorPane getPane()
    {
        return hyperPane;
    }

    /**
     * Returns the URL from the pane as a String.
     *
     * @return the URL to the currently pointed to page.
     */
    public String getURL()
    {
        return hyperPane.getPage().toString();
    }

    /**
     * Sets the URL of this Web browser.
     *
     * @param url the URL to set this browser to.
     */
    public void setURL(String url)
    {
        try
        {
            hyperPane.setPage(url);
            enterField.setText(url);
        } catch(IOException ioe)
        {
            statusBar.setText("Cound not open that url.");
            enterField.setText(startPage);
        }
    }

    /**
     * Accepts a URL as a string. Must be a valid URL.
     */
    public JWebBrowser(String url)
    {
        this();
        enterField.setText(url);
        try
        {
            hyperPane.setPage(url);

        } catch(IOException ioe)
        {
            statusBar.setText("Cound not open that url.");
            enterField.setText(startPage);

        }

    }

}
