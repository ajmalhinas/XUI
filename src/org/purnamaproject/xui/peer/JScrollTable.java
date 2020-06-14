package org.purnamaproject.xui.peer;

/**
 * @(#)JScrollTable.java    0.5 18/08/2003
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
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * A custom component that allows for the JTable to have its column headers placed in it
 * and as well to allow the JTable to scroll when it needs to scroll.
 * @version    0.5 18/08/2003
 * @author     Arron Ferguson
 */
public class JScrollTable extends JPanel
{

    /**
     * the table.
     */
    private JTable table;

    /**
     * the scroll pane.
     */
    private JScrollPane scroller;

    /**
     * Model for allowing or disallowing editing of cells within the table.
     */
    public class EditModel extends DefaultTableModel
    {
        /**
         * Required since this support is not found in the JTable.
         */
        private boolean isEditable;

        /**
         * Constructor for allowing or disallowing editable cells.
         *
         * @param editState true means that cells can be edited and false
         * means they cannot be edited.
         */
        public EditModel(boolean editState)
        {
            super();
            isEditable = editState;
        }

        /**
         * Constructor for allowing or disallowing editable cells.
         *
         * @param editState true means that cells can be edited and false
         * means they cannot be edited.
         * @param rows the number of initial rows.
         * @param columns the number of initial columns.
         */
        public EditModel(boolean editState, int rows, int columns)
        {
            super(rows, columns);
            isEditable = editState;
        }

        /**
         * Returns whether or not the table will allow for editing. Required
         * since this is not built into the JTable.
         *
         * @param rowIndex the row index.
         * @param mColIndex the column index.
         */
        public boolean isCellEditable(int rowIndex, int mColIndex)
        {
            return isEditable;
        }
    };

    /**
     * Configures the scroll pane as well as the table.
     */
    public JScrollTable()
    {
        super();
        setLayout(new BorderLayout());

        // config the selection settings with the table:
        table = new JTable(new EditModel(true));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(true);
        table.getTableHeader().setReorderingAllowed(false);

        scroller = new JScrollPane(table);

        // add them to this component
        add(scroller, BorderLayout.CENTER);
    }

    /**
     * Configures the scroll pane as well as the table and sets up the table to
     * have a specific number of rows and columns.
     *
     * @param rows the number of rows.
     * @param columns the number of columns.
     */
    public JScrollTable(int rows, int columns)
    {
        super();
        setLayout(new BorderLayout());

        // config the selection settings with the table:
        table = new JTable(new EditModel(true, rows, columns));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(true);
        table.getTableHeader().setReorderingAllowed(false);

        scroller = new JScrollPane(table);

        // add them to this component
        add(scroller, BorderLayout.CENTER);

    }

    /**
     * Configures the scroll pane as well as the table and sets up the table allowing
     * editable to be turned off (false).
     *
     * @param isEditable the user can edit cells if the value is true and false means
     * that the cells are not editable.
     */
    public JScrollTable(boolean isEditable)
    {
        super();
        setLayout(new BorderLayout());

        // config the selection settings with the table:
        table = new JTable(new EditModel(isEditable));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(true);
        table.getTableHeader().setReorderingAllowed(false);

        scroller = new JScrollPane(table);

        // add them to this component
        add(scroller, BorderLayout.CENTER);

    }

    /**
     * Configures the scroll pane as well as the table and sets up the table to
     * have a specific number of rows and columns. Sets up the rules for editing.
     *
     * @param rows the number of rows.
     * @param columns the number of columns.
     * @param isEditable the user can edit cells if the value is true and false means
     * that the cells are not editable.
     */
    public JScrollTable(int rows, int columns, boolean isEditable)
    {
        super();
        setLayout(new BorderLayout());

        // config the selection settings with the table:
        table = new JTable(new EditModel(isEditable, rows, columns));
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(true);
        table.getTableHeader().setReorderingAllowed(false);

        scroller = new JScrollPane(table);

        // add them to this component
        add(scroller, BorderLayout.CENTER);

    }

    /**
     * Sets the header as well as the data within the table.
     *
     * @param rowData the rowData as a vector of vectors (like a 2D array). Within each
     * second vector is a list of strings.
     * @param headers the headers given as a vector of strings.
     */
    public JScrollTable(Vector rowData, Vector headers)
    {
        super();
        table = new JTable(rowData, headers);
        setLayout(new BorderLayout());

        // config the selection settings with the table:
        table.setCellSelectionEnabled(true);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setFocusable(true);
        table.getTableHeader().setReorderingAllowed(false);


        scroller = new JScrollPane(table);

        // add them to this component
        add(scroller, BorderLayout.CENTER);

    }

    /**
     * Returns the JTable that sits within this JScrollPane.
     *
     * @return the table.
     */
    public JTable getTable()
    {
        return table;
    }

    /**
     * Returns the JScrollPane.
     *
     * @return the scroll pane.
     */
    public JScrollPane getScrollPane()
    {
        return scroller;
    }

}
