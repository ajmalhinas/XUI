package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUISliderBar.java    0.5 18/08/2003
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

import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.component.atomic.XUIAtomic;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * This atomic component represents a slider bar. A sliderbar contains a knob on the bar itself
 * allowing the user to drag this knob anywhere on the bar. When the user drags this slider,
 * any listeners who are registered with the slider bar will be informed of the position change
 * of the knob on the bar.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUISliderBar extends XUIAtomic, XUIEventSource
{
    /**
     * Sets the orientation of this button component. Orientation refers to how the image and text are
     * placed. If there is both an image and text, then this value has meaning. If vertical, then the image
     * is overtop of the text. If the orientation is set to horizontal, then the image is on the left and the
     * text is on the right. If a value other than vertical or horizontal are given, no change it made. The default
     * value is horizontal.
     *
     * @param orientation the orientation of image and text.
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_HORIZONTAL
     * @see org.purnamaproject.xui.helpers.XUIUtils#ORIENT_VERTICAL
     */
    public void setOrientation(int orientation);

    /**
     * Returns the position of the knob on this sliderbar.
     *
     * @return the position of the knob on this sliderbar.
     * @see #setCurrentPosition(int)
     */
    public int getCurrentPosition();

    /**
     * Sets the position of the knob on this progress bar. The value allowed for this must fall within the
     * range from 0 to 65,535. Real-time validation is performed and therefor if a wrong value is entered,
     * an exception is generated.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param position the position of the knob on the slider bar.
     */
    public void setCurrentPosition(int position) throws XUITypeFormatException;

    /**
     * Returns the increment value that will be used to move the component in when the user moves the knob
     * on the slider bar. e.g. 5 units per move.
     *
     * @return the visual measure increment value.
     * @see #setVisualMeasureIncrement(int)
     */
    public int getVisualMeasureIncrement();

    /**
     * Sets visual measure increment value on the knob. When the user moves the knob or clicks within the
     * bar itself (not on the arrows), the knob moves in a certain number of units. This allows for faster
     * scrolling. The value must fall within the specified range based on the XUI specification.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param increment the increment value (e.g. 2, 4, 6, 8 showing an increment of 2).
     */
    public void setVisualMeasureIncrement(int increment) throws XUITypeFormatException;

    /**
     * Returns the maximum value that is represented on the slider bar. e.g. 100.
     *
     * @return the maximum value.
     * @see #setMaxValue(int)
     */
    public int getMaxValue();

    /**
     * Sets the maximum value on the slider bar. This is the ceiling value that represents the top of the range.
     * The value must fall within the specified range based on the XUI specification.
     *
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type.
     * @param maxValue the maxValue of the knob on the slider bar.
     */
    public void setMaxValue(int maxValue) throws XUITypeFormatException;

}
