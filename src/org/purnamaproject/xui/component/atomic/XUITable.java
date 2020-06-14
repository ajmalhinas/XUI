package org.purnamaproject.xui.component.atomic;

/**
 * @(#)XUITable.java    0.5 18/08/2003
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

import java.util.Vector;
import org.purnamaproject.xui.binding.XUIEventSource;
import org.purnamaproject.xui.component.atomic.XUIAtomic;
import org.purnamaproject.xui.XUITypeFormatException;

/**
 * This atomic component represents a table consisting of columns and rows. It allows for
 * the user to change values in each cell of the table. Listeners will be informed of any
 * changes made in each cell.
 *
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public interface XUITable extends XUIAtomic, XUIEventSource
{
    /**
     * Returns the number of rows (excluding the header).
     *
     * @return the number of rows.
     * @see #getColumnCount()
     */
    public int getRowCount();

    /**
     * Returns the number of columns.
     *
     * @return the number of columns.

     */
    public int getColumnCount();

    /**
     * <p>Sets a value at the particular cell specificed. The values for rowIndex and columnIndex must fall within the
     * specified range of 0 to 65,535 as well, the row and column indexes must fall within the range of current
     * limits. If not an exception is generated. If the string value is null, an exception is generated.</p>
     *
     * @param value the actual value to set to.
     * @param columnIndex the column index.
     * @param rowIndex the row index.
     * @see <a href="http://www.w3c.org/TR/xmlschema-2/#unsignedShort">W3C XML Schema, section 3.3.23</a>
     * @see <a href="http://geekkit.bcit.ca/xui/docs/specification/xuiSpecification0.5.0.html">Purnama Project XUI Specification</a>
     * @throws org.purnamaproject.xui.XUITypeFormatException if the type does not conform to the W3C XML Schema unsignedshort type or
     * if the string is null.
     */
    public void editCellValue(String value, int rowIndex, int columnIndex) throws XUITypeFormatException;

    /**
     * Allows a specific cell to have its alignment changed (e.g. justified left).
     *
     * @param rowIndex the row index.
     * @param columnIndex the column index.
     * @param alignment the alignment attribute. The three options are XUIUtils.LEFT_JUSTIFIED,
     * XUIUtils.RIGHT_JUSTIFIED and XUIUtils.CENTER_JUSTIFIED
     */
    public void setCellAlignment(int rowIndex, int columnIndex, int alignment);

    /**
     * Returns the textual value at a particular cell.
     *
     * @return the textual value at row, column.
     * @see #editCellValue(String, int, int)
     */
    public String getCellValue(int row, int column);

    /**
     * <p>Sets the table data as a 2 dimensional list (list of lists). The list given is a two dimensional
     * collection (i.e. a list of lists) where each list within this current list is a row of data.
     * all lists must be of the same length and must not contain null values in them. An example of this
     * would be:</p>
     * <pre><code>
     * Vector list = new Vector();
     * Vector row1 = new Vector();
     * row1.add("123");
     * row1.add("456");
     * row1.add("789");
     * Vector row2 = new Vector();
     * row2.add("abc");
     * row2.add("def");
     * row2.add("ghi");
     * list.add(row1);
     * list.add(row2);
     * </pre></code>
     *
     * <p>The above creates a table that is 2 rows and 3 columns.</p>
     *
     * @param data the 2 dimensional list (list of lists) that contain string data in it.
     * @param headers the headers that are placed at the tops of the columns.
     * @throws org.purnamaproject.xui.XUITypeFormatException if any of the lists are null, if any of the strings are null, if
     * any of the lists are not the same size as specified row and column size as what was specified
     * for this table.
     */
    public void setTableData(Vector data, Vector headers) throws XUITypeFormatException;

    /**
     * Adds a new row to the current table data. The new row is appended at the bottom.
     *
     * @param data the data vector to add. The data must have the same number of columns
     * as the current table or an exception is generated.
     * @param alignment the alignment for the row.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the number of entries in this new row is not the
     * the same number of columns found in the current table.
     */
    public void addRow(Vector data, int alignment) throws XUITypeFormatException;

    /**
     * Removes the row based on the row index. If the index is out of bounds, then an
     * out of bounds exception will be generated.
     *
     * @param rowNumber the row number to remove.
     */
    public void removeRow(int rowNumber);

    /**
     * Adds a new column to the current table data. The new column is appended at the
     * the end (right side). New data is appended at the end of the table.
     *
     * @param columnName the label or name of the new column. String cannot be null.
     * @throws org.purnamaproject.xui.XUITypeFormatException if the number of column name is null.
     */
    public void addColumn(String columnName, Vector data) throws XUITypeFormatException;

    /**
     * Removes the column based on the column index. If the index is out of bounds, then an
     * out of bounds exception will be generated.
     *
     * @param columnNumber the column number to remove.
     */
    public void removeColumn(int columnNumber);

}
