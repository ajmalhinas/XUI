package org.purnamaproject.xui.peer;

/**
 * @version 1.0 11/09/98
 */

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.ListCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Header for rows.
 */
public class RowHeaderRenderer extends JLabel implements ListCellRenderer {

  /**
   * Constructor with a reference to the table.
   *
   * @param table the table that it renders to.
   */
  public RowHeaderRenderer(JTable table) {
      super();
    JTableHeader header = table.getTableHeader();
    setOpaque(true);
    setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    setHorizontalAlignment(CENTER);
    setForeground(header.getForeground());
    setBackground(header.getBackground());
    setFont(header.getFont());
  }

 /**
  * Return a component that has been configured to display the specified value. That component's
  * paint method is then called to "render" the cell. If it is necessary to compute the dimensions
  * of a list because the list cells do not have a fixed size, this method is called to generate a
  * component on which getPreferredSize can be invoked.
  *
  * @param list - The JList we're painting.
  * @param value - The value returned by list.getModel().getElementAt(index).
  * @param index - The cells index.
  * @param isSelected - True if the specified cell was selected.
  * @param cellHasFocus - True if the specified cell has the focus.
  */
  public Component getListCellRendererComponent( JList list,
         Object value, int index, boolean isSelected, boolean cellHasFocus) {
    setText((value == null) ? "" : value.toString());
    return this;
  }
}
