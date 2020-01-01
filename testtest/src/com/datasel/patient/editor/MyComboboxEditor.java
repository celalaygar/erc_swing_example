package com.datasel.patient.editor;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.datasel.patient.entity.Patient;

public class MyComboboxEditor extends AbstractCellEditor implements TableCellEditor{

	private JComboBox comboBoxGender = null;
	private String city="";
	private String gender="";

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {

		comboBoxGender = new JComboBox();
		comboBoxGender.setFont(new Font("Tahoma", Font.PLAIN, 17));
		comboBoxGender.addItem("Male");
		comboBoxGender.addItem("Female");
		return comboBoxGender;
	}

	public Object getCellEditorValue() {
		return comboBoxGender.getSelectedItem();
	}

}
