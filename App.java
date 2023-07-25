package assignment4;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

import assignment4.Validation.ValidationException;
/*
 * Student Name: Ricky Wong, Emma Zhang, Xiaoying Bian
 * Student ID: N01581738, N01587845, N01553051
 * Section: IGA
 * Logic: The class contains the main method, method of GUI components and action listeners (the logic).
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
		configureComponents();
		setFrameConfig();
		getConnection();
		configureActionHandlers();
	}

	public void getConnection() {
		// Create a new connection
		setPrompt("Database connecting ...");
		try {
			db = new DBUtils();
			setPrompt("Database connected :)");
		} catch (ClassNotFoundException e) {
			setPrompt("Error: Unable to establish database connection - Driver not found.", true);
			e.printStackTrace();
		} catch (SQLException e) {
			setPrompt("Error: Unable to establish database connection - Please check credentials.", true);
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

	}

	private void configureComponents() {
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

		// Set font and colors
		lblPrompt.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 12));
		tfId.setBackground(Color.YELLOW);

		pack();
	}

	private void setFrameConfig() {
		setTitle("");
		setSize(500, 250);
		setResizable(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void addComponents(JPanel container, JComponent... args) {
		for (JComponent comp : args) {
			container.add(comp);
		}
	}

	private void configureActionHandlers() {
		// Collect all input fields
		List<JTextField> textFields = Arrays.asList(tfId, tfLastName, tfFirstName, tfMi, tfAddress, tfCity, tfState,
				tfTelephone);

		Validation validation = new Validation(textFields);

		// Implement view functionality
		btnView.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String id = validation.getId(tfId.getText().trim());
					PreparedStatement pstmtView = db.getPreparedViewStatement();
					pstmtView.setString(1, id);
					ResultSet rs = pstmtView.executeQuery();
					//check if the record exists 
					if (rs.next()) {
						ResultSetMetaData rsmt = rs.getMetaData();
						int columnCount = rsmt.getColumnCount();
						for (int i = 1; i <= columnCount; i++) {
							textFields.get(i - 1).setText(rs.getString(i));
						}
						setPrompt("Record found!");
					} else {
						setPrompt("No record found!", true);
						clearTextFields();
					}
				} catch (ValidationException ve) {
					// If a validation error occurs, set the prompt and return
					setPrompt(ve.getMessage(), true);
					return;
				} catch (SQLException se) {
					setPrompt("Database error occurred. Please try again later.", true);
					se.printStackTrace();
					return;
				}
			}

		});

		// Implement insert functionality
		btnInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String id = validation.getId(tfId.getText().trim());
					
					//prevent duplication
					if (db.idExists(id)) {
						setPrompt("This ID has been in the table, please try again", true);
						return;
					} else {
						validation.validate();
						PreparedStatement pstmtInsert = db.getPreparedInsertStatement();
						pstmtInsert.setString(1, id);
						for (int i = 1; i < textFields.size(); i++) {
							pstmtInsert.setString(i + 1, textFields.get(i).getText().trim());
						}
						pstmtInsert.executeUpdate();
						setPrompt("Insert a new data successfully!");
					}
				} catch (ValidationException ve) {
					// If a validation error occurs, set the prompt and return
					setPrompt(ve.getMessage(), true);
					return;
				} catch (SQLException se) {
					setPrompt("Database error occurred. Please try again later.", true);
					se.printStackTrace();
					return;
				}
			}
		});

		// Implement update functionality
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String id = validation.getId(tfId.getText().trim());
					PreparedStatement pstmtView = db.getPreparedViewStatement();
					pstmtView.setString(1, id);
					ResultSet rs = pstmtView.executeQuery();
					if (!rs.next()) {
						setPrompt("This record is not included in the table, please try again", true);
						return;
					}

					// Store the old data
					String oldData = "Old Information:\n";
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int i = 1; i <= columnCount; i++) {
						oldData += rsmd.getColumnName(i) + ": " + rs.getString(i) + "\n";
					}
					
					PreparedStatement pstmtUpdate = db.getPreparedUpdateStatement();
					for (int i = 1; i < textFields.size(); i++) {
						if (!textFields.get(i).getText().isEmpty()) {
							pstmtUpdate.setString(i, textFields.get(i).getText());
						} else {
							pstmtUpdate.setString(i, rs.getString(i+1));
							textFields.get(i).setText(rs.getString(i+1));
						}
					}
					pstmtUpdate.setString(textFields.size(), id);
					validation.validate();
					// Store the new data
					String newData = "New Information:\n";
					for (int i = 1; i <= columnCount; i++) {
						if (!textFields.get(i-1).getText().isEmpty()) {
							newData += rsmd.getColumnName(i) + ": " + textFields.get(i-1).getText() + "\n";
						} else {
							newData += rsmd.getColumnName(i) + ": " + rs.getString(i) + "\n";
						}
					
					}

					// Confirm with the user
					int dialogResult = JOptionPane.showConfirmDialog(null,
							oldData + "\n" + newData + "\nDo you confirm the update?", "Confirm Update",
							JOptionPane.YES_NO_OPTION);
					if (dialogResult == JOptionPane.YES_OPTION) {
						pstmtUpdate.executeUpdate();
						setPrompt("Update a data successfully!");
					} else {
						setPrompt("Update cancelled by the user.");
					}

				} catch (ValidationException ve) {
					// If a validation error occurs, set the prompt and return
					setPrompt(ve.getMessage(), true);
					return;
				} catch (SQLException se) {
					setPrompt("Database error occurred. Please try again later.", true);
					se.printStackTrace();
					return;
				}
			}
		});

		// Implement clear functionality
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearTextFields();
				setPrompt("Fields cleared!");
			}
		});

		// Close connection when closing the window
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					db.closeConnection();
				} catch (SQLException | NullPointerException se) {
					se.printStackTrace();
				} finally {
					System.exit(0);
				}
			}
		});
	}

	// Helper methods
	public void setPrompt(String msg) {
		lblPrompt.setText(msg);
		lblPrompt.setForeground(Color.BLACK);
	}

	public void setPrompt(String msg, boolean isError) {
		lblPrompt.setText(msg);
		lblPrompt.setForeground(Color.RED);
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
