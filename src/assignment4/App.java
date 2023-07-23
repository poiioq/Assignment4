package assignment4;


import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/*
 * Student Name: Ricky Wong
 * Student ID: N01581738
 * Section: IGA
 * Logic: The class contains the View (GUI) of the program.
 */

/*
 *  ITC5201 – Assignment 4
 *  I declare that this assignment is my own work in accordance with Humber Academic Policy.
 *  No part of this assignment has been copied manually or electronically from any other source
 *  (including web sites) or distributed to other students/social media.
 *  Name: _Ricky Lok Ting Wong__ Student ID: ____N01581738_____ Date: ____2023-07-19______
 */

public class App extends JFrame {

	// Declare GUI components
	private JTextField tfId;
	private JTextField tfLastName;
	private JTextField tfFirstName;
	private JTextField tfMi;
	private JTextField tfAddress;
	private JTextField tfCity;
	private JTextField tfState;
	private JTextField tfTelephone;

	private JLabel lblId;
	private JLabel lblLastName;
	private JLabel lblFirstName;
	private JLabel lblMi;
	private JLabel lblAddress;
	private JLabel lblCity;
	private JLabel lblState;
	private JLabel lblTelephone;

	private JButton btnView;
	private JButton btnInsert;
	private JButton btnUpdate;
	private JButton btnClear;

	private JLabel lblPrompt;

	private JPanel panelData;
	private JPanel panelFirstRow;
	private JPanel panelSecondRow;
	private JPanel panelThirdRow;
	private JPanel panelForthRow;
	private JPanel panelFifthRow;

	private JPanel panelBtn;

	private DBUtils db;

	App() {
		createComponents();
		addComponentsToLayout();
		setFrameConfig();
		getConnection();
		configureActionHandlers();
	}

	public void getConnection() {
		// Create a new connection
		setPrompt("Database connecting ...",false);
		try {
			db = new DBUtils();
			setPrompt("Database connected :)",false);
		} catch (ClassNotFoundException e) {
			setPrompt("Error: Unable to establish database connection - Driver not found.",true);
			e.printStackTrace();
		} catch (SQLException e) {
			setPrompt("Error: Unable to establish database connection - Please check credentials.",true);
			e.printStackTrace();
		}
	}

	private void createComponents() {

		// Create labels and text fields
		lblId = new JLabel("ID");
		tfId = new JTextField(12);
		lblLastName = new JLabel("Last Name");
		tfLastName = new JTextField(10);
		lblFirstName = new JLabel("First Name");
		tfFirstName = new JTextField(10);
		lblMi = new JLabel("mi");
		tfMi = new JTextField(3);
		lblAddress = new JLabel("Address");
		tfAddress = new JTextField(15);
		lblCity = new JLabel("City");
		tfCity = new JTextField(15);
		lblState = new JLabel("State");
		tfState = new JTextField(3);
		lblTelephone = new JLabel("Telephone");
		tfTelephone = new JTextField(10);
		lblPrompt = new JLabel("");

		// Create buttons
		btnView = new JButton("View");
		btnInsert = new JButton("Insert");
		btnUpdate = new JButton("Update");
		btnClear = new JButton("Clear");

		FlowLayout deLayout = new FlowLayout(FlowLayout.LEFT);
		// Create panels
		panelData = new JPanel(deLayout);
		panelFirstRow = new JPanel(deLayout);
		panelSecondRow = new JPanel(deLayout);
		panelThirdRow = new JPanel(deLayout);
		panelForthRow = new JPanel(deLayout);
		panelFifthRow = new JPanel(deLayout);
		panelBtn = new JPanel(new FlowLayout(FlowLayout.CENTER));

		return;
	}

	private void addComponentsToLayout() {
		// Use grid bag layout
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		panelData.setLayout(new GridLayout(5, 1));

		// Use a helper method to add all components to their corresponding containers
		addComponents(panelFirstRow, lblId, tfId);
		addComponents(panelSecondRow, lblLastName, tfLastName, lblFirstName, tfFirstName, lblMi, tfMi);
		addComponents(panelThirdRow, lblAddress, tfAddress);
		addComponents(panelForthRow, lblCity, tfCity, lblState, tfState);
		addComponents(panelFifthRow, lblTelephone, tfTelephone);
		addComponents(panelData, panelFirstRow, panelSecondRow, panelThirdRow, panelForthRow, panelFifthRow);
		panelData.setBorder(new TitledBorder(new EtchedBorder(), "Staff Information"));

		gbc.fill = GridBagConstraints.BOTH;
		// set panelData to take 80% of the container height
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		add(panelData, gbc);

		// set panelBtn to take 20% of the container height
		addComponents(panelBtn, btnView, btnInsert, btnUpdate, btnClear);
		gbc.gridy = 1;
		gbc.weighty = 0.2;
		add(panelBtn, gbc);

		// set lblPrompt to be displayed at the bottom left
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		gbc.gridy = 2;
		gbc.weighty = 0;
		add(lblPrompt, gbc);

		pack();
		return;
	}

	private void setFrameConfig() {
		setTitle("");
		setSize(420, 250);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		return;
	}

	private void addComponents(JPanel container, JComponent... args) {
		for (JComponent comp : args) {
			container.add(comp);
		}
		return;
	}

	private void configureActionHandlers() {
		// Collect all input fields
		List<JTextField> textFields = Arrays.asList(tfId, tfLastName, tfFirstName, tfMi, tfAddress, tfCity, tfState,
				tfTelephone);

		// Implement view functionality
		btnView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Validation validation = new Validation(textFields);
					String Id=validation.getId(tfId.getText().trim());
					if (Id.length()>9) {
						setPrompt(Id,true);
						return;
					}else {
						
						PreparedStatement pstmtView = db.getPreparedViemStatement();
						pstmtView.setString(1, Id);
						ResultSet rs = pstmtView.executeQuery();
						if (rs.next()) {
							ResultSetMetaData rsmt = rs.getMetaData();
							int columnCount = rsmt.getColumnCount();
							for (int i = 1; i <= columnCount; i++) {
								textFields.get(i - 1).setText(rs.getString(i));
							}
							setPrompt("Record found!",false);
						} else {
							setPrompt("No record found!",true);
							clearTextFields();
						}
					}
				} catch (SQLException se) {
					se.printStackTrace();
				}
				}
			
		});

		// Implement insert functionality
		btnInsert.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            Validation validation = new Validation(textFields);
		            if (validation.returnValidateLength() != null) {
		                setPrompt(validation.returnValidateLength(),true);
		                return;
		            }

		            if (validation.returnValidateRequiredFields() != null) {
		                setPrompt(validation.returnValidateRequiredFields(),true);
		                return;
		            }
		            String Id=validation.getId(tfId.getText().trim());
					if (Id.length()>9) {
						setPrompt(Id,true);
						return;
					}else {
						PreparedStatement pstmtInsert = db.getPreparedInsertStatment();
						if (db.idExsits(Id)) {
							setPrompt("This ID has been in the table, please try again",true);
							return;
						} else {
							pstmtInsert.setString(1, Id);
							for (int i = 1; i < textFields.size(); i++) {
								pstmtInsert.setString(i + 1, textFields.get(i).getText().trim());
							}
							pstmtInsert.executeUpdate();
							setPrompt("Insert a new data successfully!",false);
						}
					}
		        } catch (SQLException se) {
		            se.printStackTrace();
		        }
		    }
		});



		btnUpdate.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		            Validation validation = new Validation(textFields);
		            if (validation.returnValidateLength() != null) {
		                setPrompt(validation.returnValidateLength(),true);
		                return;
		            }

		            if (validation.returnValidateRequiredFields() != null) {
		                setPrompt(validation.returnValidateRequiredFields(),true);
		                return;
		            }
		            
		            String Id = validation.getId(tfId.getText().trim());
		            if (Id.length() > 9) {
		                setPrompt(Id,true);
		                return;
		            } else {
		                PreparedStatement pstmtView = db.getPreparedViemStatement();
		                pstmtView.setString(1, Id);
		                ResultSet rs = pstmtView.executeQuery();
		                if (!rs.next()) {
		                    setPrompt("This record is not included in the table, please try again",true);
		                    return;
		                }

		                // Store the old data
		                String oldData = "Old Information:\n";
		                ResultSetMetaData rsmd = rs.getMetaData();
		                int columnCount = rsmd.getColumnCount();
		                for (int i = 1; i <= columnCount; i++) {
		                    oldData += rsmd.getColumnName(i) + ": " + rs.getString(i) + "\n";
		                }

		                // Store the new data
		                String newData = "New Information:\n";
		                for (int i = 1; i < textFields.size(); i++) {
		                    newData += rsmd.getColumnName(i + 1) + ": " + textFields.get(i).getText() + "\n";
		                }

		                // Confirm with the user
		                int dialogResult = JOptionPane.showConfirmDialog(null, oldData + "\n" + newData + "\nDo you confirm the update?", "Confirm Update", JOptionPane.YES_NO_OPTION);
		                if (dialogResult == JOptionPane.YES_OPTION) {
		                    PreparedStatement pstmtUpdate = db.getPreparedUpdateStatement();
		                    for (int i = 1; i < textFields.size(); i++) {
		                        if (!textFields.get(i).getText().isEmpty()) {
		                            pstmtUpdate.setString(i, textFields.get(i).getText());
		                        } else {
		                            pstmtUpdate.setString(i, rs.getString(i + 1));
		                        }
		                    }
		                    pstmtUpdate.setString(textFields.size(), Id);
		                    pstmtUpdate.executeUpdate();
		                    setPrompt("Update a data successfully!",false);
		                } else {
		                    setPrompt("Update cancelled by the user.",false);
		                }
		            }

		        } catch (SQLException se) {
		            se.printStackTrace();
		        }
		    }
		});


		// Implement clear functionality
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPrompt("",false);
				clearTextFields();
			}
		});
	}
	
	

	// Getters and setters
	public JTextField getTfId() {
		return tfId;
	}

	public void setTfId(JTextField tfId) {
		this.tfId = tfId;
	}

	public JTextField getTfLastName() {
		return tfLastName;
	}

	public void setTfLastName(JTextField tfLastName) {
		this.tfLastName = tfLastName;
	}

	public JTextField getTfFirstName() {
		return tfFirstName;
	}

	public void setTfFirstName(JTextField tfFirstName) {
		this.tfFirstName = tfFirstName;
	}

	public JTextField getTfMi() {
		return tfMi;
	}

	public void setTfMi(JTextField tfMi) {
		this.tfMi = tfMi;
	}

	public JTextField getTfAddress() {
		return tfAddress;
	}

	public void setTfAddress(JTextField tfAddress) {
		this.tfAddress = tfAddress;
	}

	public JTextField getTfCity() {
		return tfCity;
	}

	public void setTfCity(JTextField tfCity) {
		this.tfCity = tfCity;
	}

	public JTextField getTfState() {
		return tfState;
	}

	public void setTfState(JTextField tfState) {
		this.tfState = tfState;
	}

	public JTextField getTfTelephone() {
		return tfTelephone;
	}

	public void setTfTelephone(JTextField tfTelephone) {
		this.tfTelephone = tfTelephone;
	}

	public JButton getBtnView() {
		return btnView;
	}

	public void setBtnView(JButton btnView) {
		this.btnView = btnView;
	}

	public JButton getBtnInsert() {
		return btnInsert;
	}

	public void setBtnInsert(JButton btnInsert) {
		this.btnInsert = btnInsert;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public void setBtnUpdate(JButton btnUpdate) {
		this.btnUpdate = btnUpdate;
	}

	public JButton getBtnClear() {
		return btnClear;
	}

	public void setBtnClear(JButton btnClear) {
		this.btnClear = btnClear;
	}

	public JLabel getLblPrompt() {
		return lblPrompt;
	}

	public void setLblPrompt(JLabel lblPrompt) {
		this.lblPrompt = lblPrompt;
	}

	// Helper methods
	public void setPrompt(String msg,boolean isError) {
		if (isError) {
			this.lblPrompt.setText("<html><font color='red'><b>"+msg+"</b></font></html>");
		}else
		{
			this.lblPrompt.setText("<html><font color='blue'><b>"+msg+"</font></html>");
		}
	}

	public void clearTextFields() {
		tfId.setText("");
		tfLastName.setText("");
		tfFirstName.setText("");
		tfMi.setText("");
		tfCity.setText("");
		tfState.setText("");
		tfAddress.setText("");
		tfTelephone.setText("");
	}

	// main method
	public static void main(String[] args) {

		App app = new App();

		// Make the app visible
		app.setVisible(true);

	}

}

