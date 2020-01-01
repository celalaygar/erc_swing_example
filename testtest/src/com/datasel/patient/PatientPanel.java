package com.datasel.patient;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.JobAttributes.DialogType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.datasel.patient.connection.DbHelper;
import com.datasel.patient.connection.ORACLEConnection;
import com.datasel.patient.editor.MyComboboxEditor;
import com.datasel.patient.editor.PatientAddEditor;
import com.datasel.patient.entity.Patient;

import javax.swing.JScrollPane;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PatientPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final JButton btnAdd = new JButton("Add");
	private final JButton btnDelete = new JButton("Delete");
	private final JButton btnEdit = new JButton("Edit");
	private final JTable table = new JTable();
	private final PatientTableModel tableModel = new PatientTableModel();
	private final JScrollPane scrollPane = new JScrollPane();
	private DbHelper dbhelper;
	private final JButton btnExit = new JButton("Exit");
	private JFrame frame;
	int selectedRowNumber = -1;
	private final JButton btnExport = new JButton("Export");
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private boolean detectedId = false;
	private String selectedId = "";
	private String path = "";
	private String extensionPath = "";
	private ArrayList<Integer> ids = new ArrayList<>();
	private final JButton btnImport = new JButton("Import");
	private final JButton btnUpdate = new JButton("Update");
	private final JTextField txtSearch = new JTextField();

	public PatientPanel() {
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 17));

		txtSearch.setColumns(10);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 10, 69, 18, 82, 58, 0, 0, 0, 0, 0, 10,
				0 };
		gridBagLayout.rowHeights = new int[] { 10, 0, 0, 10, 20, 10, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 3;
		gbc_btnUpdate.gridy = 1;
		btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnUpdate, gbc_btnUpdate);

		GridBagConstraints gbc_btnImport = new GridBagConstraints();
		gbc_btnImport.insets = new Insets(0, 0, 5, 5);
		gbc_btnImport.gridx = 4;
		gbc_btnImport.gridy = 1;
		btnImport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnImport, gbc_btnImport);

		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.insets = new Insets(0, 0, 5, 5);
		gbc_btnExport.gridx = 5;
		gbc_btnExport.gridy = 1;
		btnExport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnExport, gbc_btnExport);

		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 6;
		gbc_btnAdd.gridy = 1;
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnAdd, gbc_btnAdd);

		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 7;
		gbc_btnDelete.gridy = 1;
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnDelete, gbc_btnDelete);

		GridBagConstraints gbc_btnEdit = new GridBagConstraints();
		gbc_btnEdit.insets = new Insets(0, 0, 5, 5);
		gbc_btnEdit.gridx = 8;
		gbc_btnEdit.gridy = 1;
		btnEdit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnEdit, gbc_btnEdit);

		GridBagConstraints gbc_btnExit = new GridBagConstraints();
		gbc_btnExit.insets = new Insets(0, 0, 5, 5);
		gbc_btnExit.gridx = 9;
		gbc_btnExit.gridy = 1;
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		add(btnExit, gbc_btnExit);
		
		GridBagConstraints gbc_txtSearch = new GridBagConstraints();
		gbc_txtSearch.gridwidth = 3;
		gbc_txtSearch.insets = new Insets(0, 0, 5, 5);
		gbc_txtSearch.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearch.gridx = 7;
		gbc_txtSearch.gridy = 2;
		add(txtSearch, gbc_txtSearch);

		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 9;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 4;
		add(scrollPane, gbc_scrollPane);
		table.setFont(new Font("Tahoma", Font.PLAIN, 20));
		scrollPane.setViewportView(table);
		table.setModel(tableModel);
		GenerelEvent event = new GenerelEvent(this);
		btnAdd.setActionCommand("ADD");
		btnEdit.setActionCommand("EDIT");
		btnDelete.setActionCommand("DELETE");
		btnExit.setActionCommand("EXIT");
		btnExport.setActionCommand("EXPORT");
		btnImport.setActionCommand("IMPORT");
		btnUpdate.setActionCommand("UPDATE");
		btnAdd.addActionListener(event);
		btnEdit.addActionListener(event);
		btnDelete.addActionListener(event);
		btnExit.addActionListener(event);
		btnExport.addActionListener(event);
		btnImport.addActionListener(event);
		btnUpdate.addActionListener(event);
		
		fullInTable();
		formingTable();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				
				String searchKey = txtSearch.getText();
				TableRowSorter<AbstractTableModel> tableRowSorter = new TableRowSorter<AbstractTableModel>(tableModel);
				tableRowSorter.setRowFilter(RowFilter.regexFilter(searchKey));
				table.setRowSorter(tableRowSorter);
			}
		});
	}
	void fullInTable() {
		try {
			dbhelper = new DbHelper();
			tableModel.setPatients(dbhelper.getAllDetail());
			tableModel.fireTableDataChanged();
		} catch (SQLException e) {
			System.out.println("Problem: connection DBHelper : "+e);
		}
	}
	// formating table (colour, resizing)
	void formingTable() {
	    //table.setCellSelectionEnabled(true);
		// insert image
		table.setRowHeight(70);
		table.getColumnModel().getColumn(0).setPreferredWidth(0);
		table.getColumnModel().getColumn(1).setPreferredWidth(1);
		table.getColumnModel().getColumn(6).setPreferredWidth(1);
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		// to color and form checkbox 
		table.setDefaultRenderer(table.getColumnClass(0), new BooleanRenderer());
		
		// color row
		table.getColumnModel().getColumn(4).setCellEditor(new MyComboboxEditor());
		for (int i = 1; i < tableModel.getColumnCount(); i++) {
			table.setDefaultRenderer(table.getColumnClass(i),new WineCellRenderer());
		}
	}
	
	// to form checkbox's shape
	public static class BooleanRenderer extends JCheckBox implements TableCellRenderer {
        public BooleanRenderer() {
            super();
            setHorizontalAlignment(JLabel.CENTER);
            setBorderPainted(true);
            setOpaque(true);
        }
		@Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
			setBackground(new Color(245, 225, 245));
            setSelected((value != null && ((Boolean) value).booleanValue()));
            return this;
        }
    }
	
	class GenerelEvent implements ActionListener {
		private JPanel panel;

		public GenerelEvent(JPanel panel) {
			super();
			this.panel = panel;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			Patient patient = null;
			if (e.getActionCommand().equals("EXIT")) {
				System.out.println("Exiittt..");
				System.exit(0);
			}
			if (e.getActionCommand().equals("ADD")) {
				try {
					patient = openEditor(patient);
					patient = dbhelper.addPatient(patient);
					if (patient != null) {
						tableModel.getPatients().add(patient);
						tableModel.fireTableDataChanged();
					}
				} catch (SQLException e1) {
					System.out.println("Problem : add patient on DBHelper : "+e1);
				}
			}
			if (e.getActionCommand().equals("EDIT")) {
				if (table.getSelectedRow() == -1) {
					System.out.println("Not Selected....");
				} else {
					patient = tableModel.getPatients().get(table.getSelectedRow());
					patient = openEditor(patient);
					try {
						patient = dbhelper.updatePatient(patient);
					} catch (SQLException e1) {
						System.out.println("Problem is about edit " + e1);
					}
					tableModel.getPatients().set(table.getSelectedRow(),patient);
					tableModel.fireTableDataChanged();
				}
			}
			if (e.getActionCommand().equals("EXPORT")) {
				ArrayList<Patient> selectedPatients = new ArrayList<Patient>();
				tableModel.getPatients().forEach(parent -> {
					if (parent.isSelected())	selectedPatients.add(parent);
				});
				if (selectedPatients.size() > 0) {
					openFileChooser(selectedPatients);
				} else {
					if (table.getSelectedRow() == -1) {
						JOptionPane.showMessageDialog(null,"Please choose any line");
					} else {
						selectedPatients.add(tableModel.getPatients().get(table.getSelectedRow()));
						openFileChooser(selectedPatients);
					}
				}
			}
			if (e.getActionCommand().equals("IMPORT")) {
				openFileChooserToImportPatient();
				fullInTable();
				formingTable();
			}
			if (e.getActionCommand().equals("UPDATE")) {
				tableModel.getPatients().forEach(p -> {
						try {
							dbhelper.updateDirtyPatient(p);
						} catch (Exception e1) {
							System.out.println("problem : update dirty patient : "+e);
						}
				});

				JOptionPane.showMessageDialog(null,"to update dirty patient is ok");
				fullInTable();
				formingTable();
			}
			if (e.getActionCommand().equals("DELETE")) {
				int warning = JOptionPane.showConfirmDialog(null,"Silemk Ýstediðinizden Emin misiniz ? ", "UYARI",JOptionPane.YES_NO_OPTION);
				if (warning == 0) {

					try {
						dbhelper.deleteSelectedPatient(tableModel.getPatients());
					} catch (SQLException e1) {
						System.out.println("Problem deleted data " + e1);
					}
					try {
						tableModel.setPatients(dbhelper.getAllDetail());
						tableModel.fireTableDataChanged();
					} catch (SQLException e3) {
						System.out.println("Problem : connection "+ e3);
					}
				} else {
					JOptionPane.showMessageDialog(null,"Deleting is given up");
					System.out.println("Delete is given up");
				}
			}
		}
		// open FileChooser To Import Patient
		private void openFileChooserToImportPatient() {

			JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jfc.setDialogTitle("Ýmport File");
			jfc.setAcceptAllFileFilterUsed(false);
			jfc.setMultiSelectionEnabled(true);

			FileNameExtensionFilter Jo = new FileNameExtensionFilter("Jo", "jo");
			jfc.addChoosableFileFilter(Jo);
			jfc.setFileFilter(Jo);
			int returnValue = jfc.showDialog(null, "Import");
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File[] files = jfc.getSelectedFiles();
				for (File file : files) {
					path = file.getAbsolutePath();
					Patient patient = null ;
					try {
						patient = getPatientFromFile(path);
						patient = dbhelper.addPatientFromObject(patient);
						if (patient != null) {
							tableModel.getPatients().add(patient);
						}
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "This Patient is already saved in Database : "+patient.getName()+" "+patient.getSurname());
						System.out.println("Problem saved patient : " + e);
					}
				}
				tableModel.fireTableDataChanged();
			} else {
				JOptionPane.showMessageDialog(null, "You did not choose any file");
			}
			
		}
		// read Patient object
		private Patient getPatientFromFile(String absolutePath) {
			Patient patient = null;
			try {
				FileInputStream fis = new FileInputStream(absolutePath);
				ObjectInputStream ois = new ObjectInputStream(fis);
				patient = (Patient) ois.readObject();
				ois.close();
			} catch (IOException e) {
				System.out.println("problem : IOException : " + e);
			} catch (ClassNotFoundException e) {
				System.out.println("problem : ClassNotFoundException : " + e);
			}
			return patient;
		}
		// open FileChooser to write patient as this format : (.xml .json .jo)
		private void openFileChooser(ArrayList<Patient> patients) {
			JFileChooser jfc = new JFileChooser(FileSystemView
					.getFileSystemView().getHomeDirectory());
			jfc.setDialogTitle("Save File");
			jfc.setAcceptAllFileFilterUsed(false);

			FileNameExtensionFilter Json = new FileNameExtensionFilter("Json","json");
			FileNameExtensionFilter Xml = new FileNameExtensionFilter("Xml","xml");
			FileNameExtensionFilter Jo = new FileNameExtensionFilter("Jo", "jo");

			jfc.addChoosableFileFilter(Json);	jfc.setFileFilter(Json);
			jfc.addChoosableFileFilter(Xml);	jfc.setFileFilter(Xml);
			jfc.addChoosableFileFilter(Jo);		jfc.setFileFilter(Jo);
			
			int returnValue = jfc.showDialog(null, "Save");
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				boolean control = false;
				selectedId = "";
				path = jfc.getSelectedFile().getParent() + "\\Patient_";
				if (jfc.getFileFilter().getDescription().equals("Json")) {
					writePatientJson(path, patients);
					JOptionPane.showMessageDialog(null,"Saved Json Object in File");

				} else if (jfc.getFileFilter().getDescription().equals("Xml")) {
					try {
						writePatientXML(path, patients);
					} catch (JAXBException | IOException e) {
						System.out.println("Problem save xml format : " + e);
					}
					JOptionPane.showMessageDialog(null, "Saved Xml Object in File");

				} else if (jfc.getFileFilter().getDescription().equals("Jo")) {
					WritePatientObjectToFile(path, patients);
					JOptionPane.showMessageDialog(null,"Saved Jo Object in File");

				} else {
					JOptionPane.showMessageDialog(null,"Uncorrect data format to save data");
				}
			} else {
				JOptionPane.showMessageDialog(null,"saving is given up");
			}
		}
		// save data as Json
		private void writePatientJson(String path, ArrayList<Patient> patients) {
			for (Patient patient2 : patients) {
				String absolutePath = path;
				absolutePath += patient2.getId() + "_" + patient2.getName()
						+ "_" + patient2.getSurname() + ".json";
				JSONObject obj = new JSONObject();
				obj.put("Id", patient2.getId());
				obj.put("Name", patient2.getName());
				obj.put("Lastname", patient2.getSurname());
				obj.put("Age", patient2.getAge());
				obj.put("Gender", patient2.getGender());
				obj.put("City", patient2.getCity());
				try (FileWriter file = new FileWriter(absolutePath)) {
					file.write(obj.toJSONString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		// save data as Xml
		private void writePatientXML(String path, ArrayList<Patient> patients)
				throws JAXBException, IOException {
			for (Patient patient : patients) {
				String absolutePath = path;
				absolutePath += patient.getId() + "_" + patient.getName() + "_"+ patient.getSurname() + ".xml";
				JAXBContext jaxbContext = JAXBContext.newInstance(Patient.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
				jaxbMarshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Writer w = new OutputStreamWriter(out, "UTF-8");
				jaxbMarshaller.marshal(patient, w);
				w.flush();
				byte[] bytes = out.toByteArray();
				String xml = new String(bytes, "UTF-8");
				try (FileWriter file = new FileWriter(absolutePath)) {
					file.write(xml.toString());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		// save data as Object
		public void WritePatientObjectToFile(String path,
				ArrayList<Patient> patients) {

			for (Patient patient : patients) {
				String absolutePath = path;
				absolutePath += patient.getId() + "_" + patient.getName() + "_"
						+ patient.getSurname() + ".jo";
				// Object serObj;
				try {
					// write object
					FileOutputStream fileOut = new FileOutputStream(absolutePath);
					ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
					objectOut.writeObject(patient);
					objectOut.close();

					// read object
					FileInputStream fis = new FileInputStream(absolutePath);
					ObjectInputStream ois = new ObjectInputStream(fis);
					patient = (Patient) ois.readObject();
					ois.close();

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		private Patient openEditor(Patient patient) {
			JDialog dialog = new JDialog( JOptionPane.getFrameForComponent(panel), "Create patient", true);
			PatientAddEditor editor = new PatientAddEditor(patient);
			editor.setDialog(dialog);
			dialog.getContentPane().add(editor);
			dialog.setLocationByPlatform(true);
			dialog.setSize(600, 300);
			dialog.setVisible(true);
			patient = editor.getPatient();
			return patient;
		}
	}
}
