
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Greg
 */

public class view_build extends javax.swing.JFrame {

    /**
     * Creates new form view_build
     */
    UserAccount currentUser;
    
    public view_build(){
        this.setTitle("Builds View");     //Adds a title to the frame
        setLocationRelativeTo(null);
    }
    public view_build(UserAccount user){
        this.setTitle("New Build");     //Adds a title to the frame
        setLocationRelativeTo(null);
        initComponents();
        
        newBuild.setVisible(true);
        
        
        currentUser = user;
        
        TableColumn colBuild = new TableColumn();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("PC");
        columns.add("Parts");

        DefaultTableModel modelNew = (DefaultTableModel) NewBuildTable.getModel();
        
        TableColumn col1 = new TableColumn(modelNew.getColumnCount());
        for (String temp : columns) { //Adds columns to table.
            colBuild.setHeaderValue(temp);
            NewBuildTable.addColumn(colBuild);
            modelNew.addColumn(temp);
        }
        
        
        ArrayList<String> rows = new ArrayList<>();
        rows.add("CPU");
        rows.add("MotherBoard");
        rows.add("GPU");
        rows.add("PSU");
        rows.add("RAM");
        rows.add("Storage");
        rows.add("PC Case");
        rows.add("Cooler");
        rows.add("Accessory");
        for(String tempRow : rows){
            modelNew.addRow(new Object[]{tempRow,"Select Part"});
        }
        
    }
    public view_build(UserAccount user,String buildName) {
        currentUser = user;
                 Connection con = DatabaseConnection.establishConnection();

        initComponents();
        this.setTitle("View Builds");     //Adds a title to the frame
        setLocationRelativeTo(null);    //Centers the frame in the middle of ths screen
        
        lb_buildName.setText(buildName);
        
        buildPanel.setVisible(true);
        
        
        TableColumn colBuild = new TableColumn();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("PC");
        columns.add("Parts");

        
       
        jTableBuild.disable();

        DefaultTableModel model = (DefaultTableModel) jTableBuild.getModel();
        //model.setRowCount(0);//clear table
        
        TableColumn col1 = new TableColumn(model.getColumnCount());
        for (String temp : columns) { //Adds columns to table.
            colBuild.setHeaderValue(temp);
            jTableBuild.addColumn(colBuild);
            model.addColumn(temp);
        }
        
         
        try {
            Statement stmt = (Statement) con.createStatement();
           // String query = ("Select P.PartID, P.Make, P.Model, P.Price, Speed, Cores, Graphics FROM CPU JOIN Part AS P on CPU.ID=P.PartID");
            String query = "Select P.PartType,P.Model, P.Make FROM Build AS B JOIN Part AS P ON P.PartID IN(B.Motherboard,B.CPU,B.RAM,B.Storage,B.GPU,B.PSU,B.PCCase,B.Cooler,B.Accessory) where Account = '"+currentUser.getUsername()+"' AND name = '"+buildName+"';";
                       // System.out.println(query);


                stmt.executeQuery(query);
                ResultSet rs = stmt.getResultSet();
                
              
                while (rs.next()) {   
                    
                String partType = rs.getString("PartType");
                String part = (rs.getString("Model")+" - "+rs.getString("Make"));   
                model.addRow(new Object[]{partType,part});
                
                   
                }
             
        
    }catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
        }

        
       
jTableBuild.addMouseListener(new MouseAdapter() {
    public void mousePressed(MouseEvent me) {
        selectPartPanel.setVisible(true);
        
        JTable table = (JTable) me.getSource();
        Point p = me.getPoint();
        int row = table.rowAtPoint(p);
        if (me.getClickCount() == 2 && jTableBuild.isEnabled()) {
        
        String PartType = model.getValueAt(row, 0).toString();

           //System.out.println(PartType);
        
           
                SelectComponent(PartType); 
            
        }
    }
});
    }
    
   
    
    
    /**
     * Creates new form SelectComponent
     */
    int cpuID;
    int motherboardID;

    /**
     *
     * @param parent
     * @param modal
     */
    

    /**
     *
     * @param type
     */
    public void SelectComponent(String partType) {
        System.out.println(partType);
            
       
        buildPanel.setVisible(false);
        selectPartPanel.setVisible(true);
        //jTable.setVisible(true);
        
      //  initComponents();
        //this.setTitle("Select Component");     //Adds a title to the frame
        //setLocationRelativeTo(null);    //Centers the frame in the middle of ths screen
        
        TableColumn col = new TableColumn();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("Make");
        columns.add("Model");
        columns.add("Price");
        if (partType.equals("CPU")) {
            System.out.println(partType + true);
            columns.add("Speed");
            columns.add("Cores");
            columns.add("Graphics");
        } else if (partType.equals("Motherboard")) {
            columns.add("Socket");
            columns.add("Form Factor");
            columns.add("RAM Slots");
            columns.add("Max RAM");
        }

        DefaultTableModel model = (DefaultTableModel) jTableParts.getModel();
        TableColumn col1 = new TableColumn(model.getColumnCount());
        for (String temp : columns) { //Adds columns to table.
            col.setHeaderValue(temp);
            jTableParts.addColumn(col);
            model.addColumn(temp);
        }

        Connection con = DatabaseConnection.establishConnection();
        try {
            Statement stmt = (Statement) con.createStatement();
            String query;
            String make;
            String mdl;
            double price;
            if (partType.equals("CPU")) {
                query = ("Select P.PartID, P.Make, P.Model, P.Price, Speed, Cores, Graphics FROM CPU JOIN Part AS P on CPU.ID=P.PartID");

                stmt.executeQuery(query);
                ResultSet rs = stmt.getResultSet();

                while (rs.next()) {
                    make = rs.getString("Make");
                    mdl = rs.getString("Model");
                    price = rs.getDouble("Price");
                    double speed = rs.getDouble("Speed");
                    int cores = rs.getInt("Cores");
                    boolean graphics = rs.getBoolean("Graphics");

                    model.addRow(new Object[]{make, mdl, price, speed, cores, graphics});

                }
            } else if (partType.equals("Motherboard")) {
                query = ("Select P.PartID, P.Make, P.Model, P.Price, Socket, Form_Factor, RAM_Slots,MAX_RAM FROM Motherboard JOIN Part AS P on Motherboard.ID=P.PartID");

                stmt.executeQuery(query);
                ResultSet rs = stmt.getResultSet();

                while (rs.next()) {
                    make = rs.getString("Make");
                    mdl = rs.getString("Model");
                    price = rs.getDouble("Price");
                    String socket = rs.getString("Socket");
                    String size = rs.getString("Form_Factor");
                    int slots = rs.getInt("RAM_Slots");
                    int maxRAM = rs.getInt("MAX_RAM");

                    model.addRow(new Object[]{make, mdl, price, socket, size, slots, maxRAM});
                }
            }
        } catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
        }
    
    }
     public int getID() {
        int column1 = 0;
        int column2 = 1;
        int partID = 0;
        int row = jTableParts.getSelectedRow();
        String make = jTableParts.getModel().getValueAt(row, column1).toString();
        String model = jTableParts.getModel().getValueAt(row, column2).toString();
        Connection con = DatabaseConnection.establishConnection();

        try {
            Statement stmt = (Statement) con.createStatement();
            String query = "SELECT PartID FROM Part WHERE Model ='" + model + "' && Make = '" + make + "'";
            stmt.executeQuery(query);
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                partID = rs.getInt("PartID");
            }
            return partID;
        } catch (SQLException err) {
            System.out.println(err.getMessage());   //Prints out SQL error 
           
        }
        return 0;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buildPanel = new javax.swing.JPanel();
        lb_buildName = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableBuild = new javax.swing.JTable();
        btn_editBuild = new javax.swing.JButton();
        newBuild = new javax.swing.JPanel();
        BuildNmaeTxt = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        NewBuildTable = new javax.swing.JTable();
        selectPartPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableParts = new javax.swing.JTable();
<<<<<<< Updated upstream
        jLabel1 = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
=======
<<<<<<< HEAD
        goBack = new javax.swing.JButton();
=======
        jLabel1 = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
>>>>>>> origin/master
>>>>>>> Stashed changes

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("1");
        setMaximumSize(new java.awt.Dimension(900, 700));
        setMinimumSize(new java.awt.Dimension(900, 700));
        setSize(new java.awt.Dimension(900, 700));
        getContentPane().setLayout(null);

        buildPanel.setMinimumSize(new java.awt.Dimension(900, 700));
        buildPanel.setLayout(null);

        lb_buildName.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        lb_buildName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_buildName.setText("Build Name");
        buildPanel.add(lb_buildName);
        lb_buildName.setBounds(130, 140, 630, 40);

        jTableBuild.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        )
        {public boolean isCellEditable(int row, int column){return false;}}

    );
    jTableBuild.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTableBuildMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(jTableBuild);

    buildPanel.add(jScrollPane1);
    jScrollPane1.setBounds(160, 230, 560, 200);

<<<<<<< HEAD
=======
    goBack.setText("Go Back");
    goBack.setToolTipText("");
    goBack.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            goBackActionPerformed(evt);
        }
    });
    buildPanel.add(goBack);
    goBack.setBounds(20, 610, 100, 23);

>>>>>>> origin/master
    btn_editBuild.setText("Edit");
    btn_editBuild.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btn_editBuildActionPerformed(evt);
        }
    });
    buildPanel.add(btn_editBuild);
<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    btn_editBuild.setBounds(290, 270, 75, 29);

    getContentPane().add(buildPanel);
    buildPanel.setBounds(0, 0, 660, 370);
    buildPanel.setVisible(false);

    newBuild.setPreferredSize(new java.awt.Dimension(900, 500));
    newBuild.setLayout(null);

    BuildNmaeTxt.setText("Build Name ");
    BuildNmaeTxt.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            BuildNmaeTxtActionPerformed(evt);
        }
    });
    newBuild.add(BuildNmaeTxt);
    BuildNmaeTxt.setBounds(210, 20, 250, 26);

    NewBuildTable.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {

        }
    )
    {public boolean isCellEditable(int row, int column){return false;}}

    );
    NewBuildTable.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            NewBuildTableMouseClicked(evt);
        }
    });
    jScrollPane2.setViewportView(NewBuildTable);

    newBuild.add(jScrollPane2);
    jScrollPane2.setBounds(180, 60, 300, 200);

    getContentPane().add(newBuild);
    newBuild.setBounds(139, -1, 530, 320);
    newBuild.setVisible(false);
=======
>>>>>>> Stashed changes
    btn_editBuild.setBounds(420, 450, 51, 23);

    getContentPane().add(buildPanel);
    buildPanel.setBounds(0, 0, 900, 710);
<<<<<<< Updated upstream
=======
>>>>>>> origin/master
>>>>>>> Stashed changes

    selectPartPanel.setMaximumSize(new java.awt.Dimension(900, 700));
    selectPartPanel.setMinimumSize(new java.awt.Dimension(900, 700));
    selectPartPanel.setPreferredSize(new java.awt.Dimension(900, 700));

    jTableParts.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {

        },
        new String [] {

        }
    )
    {public boolean isCellEditable(int row, int column){return false;}}
    );
    jTableParts.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            jTablePartsMouseClicked(evt);
        }
    });
    jScrollPane3.setViewportView(jTableParts);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
    jLabel1.setText("Select Component");

    CancelButton.setText("Cancel");
    CancelButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            CancelButtonActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout selectPartPanelLayout = new javax.swing.GroupLayout(selectPartPanel);
    selectPartPanel.setLayout(selectPartPanelLayout);
    selectPartPanelLayout.setHorizontalGroup(
        selectPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(selectPartPanelLayout.createSequentialGroup()
            .addGap(84, 84, 84)
            .addGroup(selectPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(selectPartPanelLayout.createSequentialGroup()
                    .addComponent(CancelButton)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(301, 301, 301))
                .addGroup(selectPartPanelLayout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 729, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(87, Short.MAX_VALUE))))
    );
    selectPartPanelLayout.setVerticalGroup(
        selectPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(selectPartPanelLayout.createSequentialGroup()
            .addGap(42, 42, 42)
            .addGroup(selectPartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(CancelButton))
            .addGap(18, 18, 18)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 560, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(49, Short.MAX_VALUE))
    );

    getContentPane().add(selectPartPanel);
<<<<<<< Updated upstream
    selectPartPanel.setBounds(0, 0, 900, 710);
=======
<<<<<<< HEAD
    selectPartPanel.setBounds(0, 0, 670, 430);
    selectPartPanel.setVisible(false);

    goBack.setText("Go Back");
    goBack.setToolTipText("");
    goBack.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            goBackActionPerformed(evt);
        }
    });
    getContentPane().add(goBack);
    goBack.setBounds(10, 490, 94, 29);
=======
    selectPartPanel.setBounds(0, 0, 900, 710);
>>>>>>> origin/master
>>>>>>> Stashed changes

    pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTableBuildMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBuildMouseClicked

    }//GEN-LAST:event_jTableBuildMouseClicked
    
    private void goBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goBackActionPerformed
         user_menu   frm;
        if(currentUser.getType() == true){
             frm = new user_menu(currentUser,true); //opens admin user form
        
         
        }else{
            
            frm = new user_menu(currentUser);
        
        }
        this.dispose();
        frm.setVisible(true);
        
    }//GEN-LAST:event_goBackActionPerformed

    private void btn_editBuildActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editBuildActionPerformed
        jTableBuild.enable();
        //height light all rows 
      
    }//GEN-LAST:event_btn_editBuildActionPerformed

    private void jTablePartsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablePartsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTablePartsMouseClicked

<<<<<<< Updated upstream
=======
<<<<<<< HEAD
    private void BuildNmaeTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuildNmaeTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BuildNmaeTxtActionPerformed

    private void NewBuildTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_NewBuildTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_NewBuildTableMouseClicked
=======
>>>>>>> Stashed changes
    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        
        selectPartPanel.setVisible(false);
        buildPanel.setVisible(true);
    }//GEN-LAST:event_CancelButtonActionPerformed
<<<<<<< Updated upstream
=======
>>>>>>> origin/master
>>>>>>> Stashed changes

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(view_build.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(view_build.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(view_build.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(view_build.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new view_build().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
<<<<<<< Updated upstream
    private javax.swing.JButton CancelButton;
=======
<<<<<<< HEAD
    private javax.swing.JTextField BuildNmaeTxt;
    private javax.swing.JTable NewBuildTable;
=======
    private javax.swing.JButton CancelButton;
>>>>>>> origin/master
>>>>>>> Stashed changes
    private javax.swing.JButton btn_editBuild;
    private javax.swing.JPanel buildPanel;
    private javax.swing.JButton goBack;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableBuild;
    private javax.swing.JTable jTableParts;
    private javax.swing.JLabel lb_buildName;
    private javax.swing.JPanel newBuild;
    private javax.swing.JPanel selectPartPanel;
    // End of variables declaration//GEN-END:variables
}
