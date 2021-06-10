import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    private JPanel MainPanel;
    private JPanel butPanel;
    private JPanel tablePanel;

    private JButton addBut;
    private JButton delBut;
    private JComboBox tableBox;

    private JTable carsTable;
    private JTable clientsTable;
    private JTable rentalTable;

    private JScrollPane scrollPane;

    String[] tableNames = {"Cars", "Clients", "Actual rentals"};
    Connection connection = null;
    PreparedStatement prep = null;
    ResultSet resultSet = null;
    Statement statement = null;

    int carID, clientID, rentID;

    public Main(String title){
        super(title);

//-----------------------------SQL-SITE-------------------------------------------------------------------------------------------------------------------

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Jakub/Documents/STUDIAJS/IntroductionToJava/sqlite-tools-win32-x86-3350500/introductiontojava.db");
            System.out.println("INFO: Connection was established.");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }


        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DefaultTableModel carData = new DefaultTableModel(new String[]{"Model", "Year", "Cost", "Available"}, 0);
        DefaultTableModel clientsData = new DefaultTableModel(new String[]{"Name", "Birth"}, 0);
        DefaultTableModel rentalData = new DefaultTableModel(new String[]{"Client Name", "Car Model", "Due"}, 0);


        try {
            resultSet = statement.executeQuery("select * from cars;");
            while (resultSet.next()) {
                String model = resultSet.getString("Model");
                String year = resultSet.getString("Year");
                String cost = resultSet.getString("Cost");
                String avail = resultSet.getString("Available");
                carID = Integer.parseInt(resultSet.getString("id"));
                carData.addRow(new Object[]{model,year,cost, avail});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try{
            resultSet = statement.executeQuery("select * from clients;");
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String birth = resultSet.getString("Birth");
                clientID = Integer.parseInt(resultSet.getString("id"));
                clientsData.addRow(new Object[]{name, birth});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            resultSet = statement.executeQuery("select * from rental;");
            while (resultSet.next()) {
                String client_name = resultSet.getString("Client_name");
                String car_model = resultSet.getString("Car_model");
                String due = resultSet.getString("Due");
                rentID = Integer.parseInt(resultSet.getString("Rent_id"));
                rentalData.addRow(new Object[]{client_name, car_model, due});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


//------------------------------------GUI-SITE------------------------------------------------------------------------------------------------------------


        addBut = new JButton("ADD");
        delBut = new JButton("DELETE");
        tableBox = new JComboBox(tableNames);

        carsTable = new JTable();
        carsTable.setModel(carData);
        clientsTable = new JTable();
        clientsTable.setModel(clientsData);
        rentalTable = new JTable();
        rentalTable.setModel(rentalData);

        scrollPane = new JScrollPane(carsTable);

        tableBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = tableBox.getSelectedItem();
                if ("Cars".equals(selectedItem)) {
                    scrollPane.setViewportView(carsTable);
                } else if ("Clients".equals(selectedItem)) {
                    scrollPane.setViewportView(clientsTable);
                } else if ("Actual rentals".equals(selectedItem)) {
                    scrollPane.setViewportView(rentalTable);
                }

            }
        });
        addBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = tableBox.getSelectedItem();

                    if ("Cars".equals(selectedItem)) {
                        try{
                            JFrame addFrame = new JFrame();
                            addFrame.setTitle("Add Car");
                            addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            addFrame.setLocationRelativeTo(null);
                            JTextField addModelField = new JTextField("");
                            JTextField addYearField = new JTextField("");
                            JTextField addCostField = new JTextField("");
                            addModelField.setBorder(new TitledBorder("Model"));
                            addYearField.setBorder(new TitledBorder("Year"));
                            addCostField.setBorder(new TitledBorder("Cost"));
                            addFrame.setLayout(new GridLayout(1,4));
                            addFrame.add(addModelField);
                            addFrame.add(addYearField);
                            addFrame.add(addCostField);
                            JButton tmpAdd = new JButton("ADD");
                            tmpAdd.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    try{
                                        prep = connection.prepareStatement("insert into cars values (?, ?, ?, ?, ?);");
                                        carID++;
                                        prep.setString(1, String.valueOf(carID));
                                        prep.setString(2, addModelField.getText());
                                        prep.setString(3, addYearField.getText());
                                        prep.setString(4, addCostField.getText());
                                        prep.setString(5, "Yes");
                                        prep.addBatch();
                                        prep.executeBatch();
                                        carData.addRow(new Object[]{addModelField.getText(),addYearField.getText(),Double.valueOf(addCostField.getText()), "Yes"});
                                        addFrame.dispose();
                                    } catch (Exception er){
                                        er.printStackTrace();
                                    }
                                }
                            });
                            addFrame.add(tmpAdd);
                            addFrame.setSize(500,100);
                            addFrame.setVisible(true);
                        } catch (Exception er){
                            er.printStackTrace();
                        }
                    } else if ("Clients".equals(selectedItem)) {
                        try{
                            JFrame addFrame = new JFrame();
                            addFrame.setTitle("Add Client");
                            addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            addFrame.setLocationRelativeTo(null);
                            JTextField addNameField = new JTextField("");
                            JTextField addBirthField = new JTextField("");
                            addNameField.setBorder(new TitledBorder("Name"));
                            addBirthField.setBorder(new TitledBorder("Birth"));
                            addFrame.setLayout(new GridLayout(1,3));
                            addFrame.add(addNameField);
                            addFrame.add(addBirthField);
                            JButton tmpAdd = new JButton("ADD");
                            tmpAdd.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    try{
                                        prep = connection.prepareStatement("insert into clients values (?, ?, ?);");
                                        clientID++;
                                        prep.setString(1, String.valueOf(clientID));
                                        prep.setString(2, addNameField.getText());
                                        prep.setString(3, addBirthField.getText());
                                        prep.addBatch();
                                        prep.executeBatch();
                                        clientsData.addRow(new Object[]{addNameField.getText(),addBirthField.getText()});
                                        addFrame.dispose();
                                    } catch (Exception er){
                                        er.printStackTrace();
                                    }
                                }
                            });
                            addFrame.add(tmpAdd);
                            addFrame.setSize(500,100);
                            addFrame.setVisible(true);
                        } catch (Exception er){
                            er.printStackTrace();
                        }
                    } else if ("Actual rentals".equals(selectedItem)) {
                        try{
                            JFrame addFrame = new JFrame();
                            addFrame.setTitle("Add Car");
                            addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            addFrame.setLocationRelativeTo(null);
                            addFrame.setLayout(new GridLayout(1,3));
                            JScrollPane clientPane = new JScrollPane();
                            JScrollPane carPane = new JScrollPane();
                            clientPane.setViewportView(clientsTable);
                            carPane.setViewportView(carsTable);
                            addFrame.add(clientPane);
                            addFrame.add(carPane);
                            JButton tmpRent = new JButton("RENT");
                            tmpRent.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    try{
                                        if(String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),3)).equals("Yes")){
                                            String nameTmp = String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(),0));
                                            String birthTmp = String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(),1));
                                            String modelTmp = String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),0));
                                            String yearTmp = String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),1));
                                            resultSet = statement.executeQuery("select * from clients where Name = '" + nameTmp + "' and Birth = '" + birthTmp + "';");
                                            int clientIDtmp = resultSet.getInt("id");
                                            resultSet = statement.executeQuery("select * from cars where Model = '" + modelTmp + "' and Year = '" + yearTmp + "';");
                                            int carsIDtmp = resultSet.getInt("id");
                                            System.out.println(clientIDtmp);
                                            prep = connection.prepareStatement("insert into rental values (?, ?, ?, ?, ?, ?);");
                                            rentID++;
                                            prep.setString(1, String.valueOf(rentID));
                                            prep.setString(2, String.valueOf(clientIDtmp));
                                            prep.setString(3, String.valueOf(carsIDtmp));
                                            prep.setString(4, String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(),0)));
                                            prep.setString(5, String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),0)));
                                            prep.setString(6, "2022-12-12");
                                            prep.addBatch();
                                            prep.executeBatch();
                                            prep = connection.prepareStatement("update cars set Available = 'No' where Model = '" + modelTmp + "' and Year = '" + yearTmp + "';");
                                            prep.addBatch();
                                            prep.executeBatch();
                                            for(int i=0;i<carsTable.getRowCount();i++){
                                                if(carsTable.getValueAt(i,0).equals(String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),0)))){
                                                    carsTable.setValueAt("No",i,3);
                                                    break;
                                                }
                                            }
                                            rentalData.addRow(new Object[]{String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(),0)),String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(),0)), "2022-12-12"});
                                            addFrame.dispose();
                                        } else {
                                            JOptionPane optionPane = new JOptionPane("This car isn't available, please select another", JOptionPane.ERROR_MESSAGE);
                                            JDialog dialog = optionPane.createDialog("ERROR");
                                            dialog.setAlwaysOnTop(true);
                                            dialog.setVisible(true);
                                        }

                                    } catch (Exception er){
                                        er.printStackTrace();
                                    }

                                }
                            });
                            addFrame.add(tmpRent);
                            addFrame.setSize(500,500);
                            addFrame.setVisible(true);
                        } catch (Exception er){
                            er.printStackTrace();
                        }

                }

            }
        });
        delBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = tableBox.getSelectedItem();

                    if ("Cars".equals(selectedItem)) {
                        if(carsTable.getSelectedRow()!=-1){
                            JFrame delFrame = new JFrame();
                            delFrame.setTitle("Remove Car");
                            delFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            delFrame.setLocationRelativeTo(null);
                            JButton yesTmp = new JButton("YES");
                            JButton noTmp = new JButton("NO");
                            JLabel labelTmp = new JLabel("Are You sure to delete selected row?");
                            JLabel emptyLabel = new JLabel("");
                            JLabel emptyLabel2 = new JLabel("");
                            JLabel emptyLabel3 = new JLabel("");
                            delFrame.setLayout(new GridLayout(2, 3));
                            delFrame.add(emptyLabel);
                            delFrame.add(labelTmp);
                            delFrame.add(emptyLabel2);
                            delFrame.add(yesTmp);
                            delFrame.add(emptyLabel3);
                            delFrame.add(noTmp);
                            yesTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    try {
                                        prep = connection.prepareStatement("delete from cars where Model=(?) and Year=(?) and Cost=(?);");
                                        carID--;
                                        prep.setString(1, String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(), 0)));
                                        prep.setString(2, String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(), 1)));
                                        prep.setString(3, String.valueOf(carsTable.getValueAt(carsTable.getSelectedRow(), 2)));
                                        prep.addBatch();
                                        prep.executeBatch();
                                        for(int i=0;i<rentalTable.getRowCount();i++){
                                            if(rentalTable.getValueAt(i,1).equals(carsTable.getValueAt(carsTable.getSelectedRow(), 0))){
                                                prep = connection.prepareStatement("delete from rental where Client_name=(?) and Car_model = (?);");
                                                prep.setString(1, String.valueOf(rentalTable.getValueAt(i, 0)));
                                                prep.setString(2, String.valueOf(rentalTable.getValueAt(i, 1)));
                                                prep.addBatch();
                                                prep.executeBatch();
                                                ((DefaultTableModel) rentalTable.getModel()).removeRow(i);
                                                break;
                                            }
                                        }
                                        ((DefaultTableModel) carsTable.getModel()).removeRow(carsTable.getSelectedRow());
                                        delFrame.dispose();
                                    } catch (Exception er) {
                                        er.printStackTrace();
                                    }
                                }
                            });
                            noTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    delFrame.dispose();
                                }
                            });
                            delFrame.setSize(700, 200);
                            delFrame.setVisible(true);
                        } else{
                            JOptionPane optionPane = new JOptionPane("You must select row first", JOptionPane.ERROR_MESSAGE);
                            JDialog dialog = optionPane.createDialog("ERROR");
                            dialog.setAlwaysOnTop(true);
                            dialog.setVisible(true);
                        }

                    } else if ("Clients".equals(selectedItem)) {
                        if(clientsTable.getSelectedRow()!=-1){
                            JFrame delFrame = new JFrame();
                            delFrame.setTitle("Remove Client");
                            delFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            delFrame.setLocationRelativeTo(null);
                            JButton yesTmp = new JButton("YES");
                            JButton noTmp = new JButton("NO");
                            JLabel labelTmp = new JLabel("Are You sure to delete selected row?");
                            JLabel emptyLabel = new JLabel("");
                            JLabel emptyLabel2 = new JLabel("");
                            JLabel emptyLabel3 = new JLabel("");
                            delFrame.setLayout(new GridLayout(2, 3));
                            delFrame.add(emptyLabel);
                            delFrame.add(labelTmp);
                            delFrame.add(emptyLabel2);
                            delFrame.add(yesTmp);
                            delFrame.add(emptyLabel3);
                            delFrame.add(noTmp);
                            yesTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    try {
                                        prep = connection.prepareStatement("delete from clients where Name=(?) and Birth=(?);");
                                        clientID--;
                                        prep.setString(1, String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(), 0)));
                                        prep.setString(2, String.valueOf(clientsTable.getValueAt(clientsTable.getSelectedRow(), 1)));
                                        prep.addBatch();
                                        prep.executeBatch();
                                        for(int i=0;i<rentalTable.getRowCount();i++){
                                            if(rentalTable.getValueAt(i,0).equals(clientsTable.getValueAt(clientsTable.getSelectedRow(), 0))){
                                                prep = connection.prepareStatement("delete from rental where Client_name=(?) and Car_model = (?);");
                                                prep.setString(1, String.valueOf(rentalTable.getValueAt(i, 0)));
                                                prep.setString(2, String.valueOf(rentalTable.getValueAt(i, 1)));
                                                prep.addBatch();
                                                prep.executeBatch();
                                                ((DefaultTableModel) rentalTable.getModel()).removeRow(i);
                                                break;
                                            }
                                        }
                                        ((DefaultTableModel) clientsTable.getModel()).removeRow(clientsTable.getSelectedRow());
                                        delFrame.dispose();
                                    } catch (Exception er) {
                                        er.printStackTrace();
                                    }
                                }
                            });
                            noTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    delFrame.dispose();
                                }
                            });
                            delFrame.setSize(700, 200);
                            delFrame.setVisible(true);
                        } else {
                            JOptionPane optionPane = new JOptionPane("You must select row first", JOptionPane.ERROR_MESSAGE);
                            JDialog dialog = optionPane.createDialog("ERROR");
                            dialog.setAlwaysOnTop(true);
                            dialog.setVisible(true);
                        }

                    } else if ("Actual rentals".equals(selectedItem)) {
                        if(rentalTable.getSelectedRow()!=-1){
                            JFrame delFrame = new JFrame();
                            delFrame.setTitle("Remove Rental");
                            delFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            delFrame.setLocationRelativeTo(null);
                            JButton yesTmp = new JButton("YES");
                            JButton noTmp = new JButton("NO");
                            JLabel labelTmp = new JLabel("Are You sure to delete selected row?");
                            JLabel emptyLabel = new JLabel("");
                            JLabel emptyLabel2 = new JLabel("");
                            JLabel emptyLabel3 = new JLabel("");
                            delFrame.setLayout(new GridLayout(2, 3));
                            delFrame.add(emptyLabel);
                            delFrame.add(labelTmp);
                            delFrame.add(emptyLabel2);
                            delFrame.add(yesTmp);
                            delFrame.add(emptyLabel3);
                            delFrame.add(noTmp);
                            yesTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    try {
                                        String modelTmp = String.valueOf(rentalTable.getValueAt(rentalTable.getSelectedRow(), 1));
                                        prep = connection.prepareStatement("update cars set Available = 'Yes' where Model='" + modelTmp + "';");
                                        prep.addBatch();
                                        prep.executeBatch();
                                        prep = connection.prepareStatement("delete from rental where Client_name=(?) and Car_model=(?);");
                                        rentID--;
                                        prep.setString(1, String.valueOf(rentalTable.getValueAt(rentalTable.getSelectedRow(), 0)));
                                        prep.setString(2, String.valueOf(rentalTable.getValueAt(rentalTable.getSelectedRow(), 1)));
                                        prep.addBatch();
                                        prep.executeBatch();
                                        for(int i=0;i<carsTable.getRowCount();i++){
                                            if(carsTable.getValueAt(i,0).equals(String.valueOf(rentalTable.getValueAt(rentalTable.getSelectedRow(), 1)))){
                                                carsTable.setValueAt("Yes",i,3);
                                                break;
                                            }
                                        }
                                        ((DefaultTableModel) rentalTable.getModel()).removeRow(rentalTable.getSelectedRow());
                                        delFrame.dispose();
                                    } catch (Exception er) {
                                        er.printStackTrace();
                                    }
                                }
                            });
                            noTmp.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent eTmp) {
                                    delFrame.dispose();
                                }
                            });
                            delFrame.setSize(700, 200);
                            delFrame.setVisible(true);
                        } else {
                            JOptionPane optionPane = new JOptionPane("You must select row first", JOptionPane.ERROR_MESSAGE);
                            JDialog dialog = optionPane.createDialog("ERROR");
                            dialog.setAlwaysOnTop(true);
                            dialog.setVisible(true);
                        }
                    }
            }
        });

        MainPanel = new JPanel();
        butPanel = new JPanel();
        tablePanel = new JPanel();
        butPanel.add(addBut);
        butPanel.add(delBut);
        butPanel.add(tableBox);
        tablePanel.add(scrollPane);


        MainPanel.setLayout(new BorderLayout());
        MainPanel.add(BorderLayout.SOUTH, butPanel);
        MainPanel.add(BorderLayout.NORTH, tablePanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(MainPanel);

        this.setSize(new Dimension(800, 600));
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args){

        Main mw = new Main("Introduction to Java");

        mw.setVisible(true);


    }

}
