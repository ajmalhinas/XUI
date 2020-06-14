package org.purnamaproject.xui.peer;

/**
 * @(#)XUIHTML.java    0.5 18/08/2003
 *
 * The Purnama Project XUI (XML-based User Interface) API is an set of program
 * calls that utilize the XUI tagset and perform the task of creating a user
 * interface. The Purnama version of this API is specific using Java as the platform
 * but other XUI APIs may use other libraries and platforms. The Purnama XUI API
 * supports the creation of Swing components, adding, deleting and laying out
 * components. It also supports dynamic binding of business logic to the user
 * interface.
 *
 * Copyright (c) 2003 Marc Loy
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

import java.awt.event.MouseEvent;
import java.awt.Point;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.AttributeList;


/**
 * <p>Enabled HTML handling capability within the Java framework.</p>
 *
 * <p><b>Code revised from Marc Loy. in his article in Javaworld</b></p>
 *
 * @see <a href="http://www.javaworld.com/javaworld/jw-01-1999/jw-01-swing.html">Java World</a>
 */
public class XUIHTML extends HTMLEditorKit
{
    /**
     * Represents a jump to a different URL.
     */
    public static final int JUMP = 0;

    /**
     * Represents a move to a different URL.
     */
    public static final int MOVE = 1;

    /**
     * Handles mouse events.
     */
    LinkHandler myHandler = new LinkHandler();

    /**
     * Creates a new XUIHTML object.
     */
    public XUIHTML()
    {
        super();
    }


    public void install(JEditorPane pane)
    {
        pane.addMouseListener(myHandler);
        pane.addMouseMotionListener(myHandler);
    }

    /**
     * Inner class to handle dealing with mouse events within the hypertext pane.
     */
    public static class LinkHandler extends MouseInputAdapter implements Serializable
    {

        URL currentUrl = null;

        /**
         * Handles a mouse click.
         */
        public void mouseClicked(MouseEvent e)
        {
            JEditorPane editor = (JEditorPane) e.getSource();

            if(!editor.isEditable())
            {
                Point pt = new Point(e.getX(), e.getY());
                int pos = editor.viewToModel(pt);
                if (pos >= 0)
                    activateLink(pos, editor, JUMP);
            }
        }

        /**
         * Need to add move type to activate a link.
         */
        public void mouseMoved(MouseEvent e)
        {
            JEditorPane editor = (JEditorPane) e.getSource();

            if(!editor.isEditable())
            {
                Point pt = new Point(e.getX(), e.getY());
                int pos = editor.viewToModel(pt);
                if (pos >= 0)
                    activateLink(pos, editor, MOVE);
            }
        }

        /**
         * ActivateLink has now been updated to decide which hyperlink event
         * to generate, based on the event type and status of the currentUrl field.
         * Rather than have two handlers (one for enter/exit, one for active) we do
         * all the work here. This saves us the effort of duplicating the href location
         * code. But that's really minor point.  You could certainly provide two handlers
         * if that makes more sense to you.
         *
         * @param pos the position.
         * @param html the pane which displays the HTML.
         * @param type the type of click (jump to a link or move)
         */
        protected void activateLink(int pos, JEditorPane html, int type)
        {
            Document doc = html.getDocument();
            if(doc instanceof HTMLDocument)
            {
                HTMLDocument hdoc = (HTMLDocument) doc;
                Element e = hdoc.getCharacterElement(pos);
                AttributeSet a = e.getAttributes();
                AttributeSet anchor = (AttributeSet) a.getAttribute(HTML.Tag.A);
                String href = (anchor != null) ?
                (String) anchor.getAttribute(HTML.Attribute.HREF) : null;
                boolean shouldExit = false;

                HyperlinkEvent linkEvent = null;
                if (href != null)
                {
                    URL u;
                    try
                    {
                        u = new URL(hdoc.getBase(), href);
                    } catch (MalformedURLException m) {

                        u = null;
                    }

                    if ((type == MOVE) && (!u.equals(currentUrl)))
                    {
                        linkEvent = new HyperlinkEvent(html,
                        HyperlinkEvent.EventType.ENTERED, u, href);
                        currentUrl = u;
                    }
                    else if (type == JUMP)
                    {
                        linkEvent = new HyperlinkEvent(html,
                        HyperlinkEvent.EventType.ACTIVATED, u, href);
                        shouldExit = true;
                    }
                    else
                    {
                        return;
                    }
                    html.fireHyperlinkUpdate(linkEvent);
                } else if (currentUrl != null)
                {
                    shouldExit = true;
                }
                if (shouldExit)
                {
                    linkEvent = new HyperlinkEvent(html,
                    HyperlinkEvent.EventType.EXITED,
                    currentUrl, null);
                    html.fireHyperlinkUpdate(linkEvent);
                    currentUrl = null;
                }
            }
        }


    }

}
