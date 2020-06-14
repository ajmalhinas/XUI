package org.purnamaproject.xui.peer;

/**
 * @(#)XUILinkListener.java    0.5 18/08/2003
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

import java.awt.Component;
import java.awt.Cursor;
import java.io.IOException;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * <p>Helper class that handles links from the XUIHypertextPane.</p>
 *
 * <p><b>Code revised from Marc Loy. in his article in Javaworld</b></p>
 *
 * @see <a href="http://www.javaworld.com/javaworld/jw-01-1999/jw-01-swing.html">Java World</a>
 */
public class XUILinkListener implements HyperlinkListener
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
     * Creates the listener.
     *
     * @param pane the hypertext pane that will display HTML.
     * @param bar the status bar.
     * @param enter the text field to enter in new URLs.
     */
    public XUILinkListener(JEditorPane pane, JLabel bar, JTextField enter)
    {
        hyperPane = pane;
        statusBar = bar;
        enterField = enter;
    }

    /**
     * Handle the hyperlink event based on whether it's entering, exiting or
     * activating a link (selecting it to go to).
     */
    public void hyperlinkUpdate(HyperlinkEvent e)
    {
        HyperlinkEvent.EventType type = e.getEventType();

        if(type == HyperlinkEvent.EventType.ENTERED)
        {
            hyperPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            statusBar.setText(e.getURL().toString());

        } else if(type == HyperlinkEvent.EventType.EXITED)
        {
            hyperPane.setCursor(Cursor.getDefaultCursor());
            statusBar.setText(" ");

        } else
        {
            try
            {
                hyperPane.setPage(e.getURL());
                enterField.setText(e.getURL().toString());
                enterField.postActionEvent();

            } catch (IOException ioe)
            {
                statusBar.setText(ioe.getMessage());
            }
        }
    }

}
