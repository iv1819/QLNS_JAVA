/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.MainMenuController;
import Controller.MainMenuManagerController;

/**
 *
 * @author Admin
 */
public class MainMenu_Manager2 extends javax.swing.JFrame {
    private MainMenu parent;
    private MainMenuController controller;
    private boolean isManager;
    private MainMenuManagerController thisControl;
    /**
     * Creates new form MainMenu_Manager
     */
    public MainMenu_Manager2(MainMenu parent, MainMenuController controller, boolean IsManager) {
        this.parent = parent;
        this.controller = controller;
        this.isManager = IsManager;
        this.thisControl = new MainMenuManagerController(controller);
    initComponents();
setLocationRelativeTo(null); 
    if(IsManager){
        btnBookM.addActionListener(evt -> {
            thisControl.openBookManagement();
                            this.dispose();

        });
        jbtnVPP.addActionListener(evt -> {
                thisControl.openVPP();
                this.dispose();

            });
        // Xử lý nút Quay lại
            btnBack.addActionListener(evt -> {
                    if (parent != null) {
                        parent.setVisible(true);
                    } else {
                        thisControl.Back();
                    }
                    this.dispose();
            });

             jbtnOrderM.addActionListener(evt ->{
                   thisControl.openOrder();
                    this.dispose();

            });

            // Xử lý nút Quản lý nhân viên
            btnEmployeeM.addActionListener(evt ->{
                    thisControl.openEmployeeManagement(parent, controller, IsManager);
                    this.dispose();
            });

             // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(evt -> {
                thisControl.openCustomer(parent, controller, IsManager);
                this.dispose();
        });        
        // Xử lý nút Quản lý chức vụ
        btnPositionM.addActionListener(evt -> {
               thisControl.openCV(parent, controller, IsManager);
                this.dispose();
        });
        btnPublisher.addActionListener(evt -> {
               thisControl.openNCC();
               this.dispose();
        });
        btnCategoryM.addActionListener(evt -> {
                thisControl.openDM();
                this.dispose();
        });
        btnProviderM.addActionListener(evt -> {
                thisControl.openNCC();
                this.dispose();
            
        });
        btnAccount.addActionListener(evt -> {
                thisControl.openAccount();
              
               this.dispose();
        });
        jbtnAuthor.addActionListener(evt ->  {
                thisControl.openAuthor(controller);
                this.dispose();
        });
        jbtnTK.addActionListener(evt ->{
                thisControl.openTK();
                this.dispose();
        });
    }
    else{
        btnEmployeeM.setEnabled(false);
        btnPositionM.setEnabled(false);

        btnAccount.setEnabled(false);
        btnBookM.addActionListener(evt -> {
            thisControl.openBookManagement();
                            this.dispose();

        });
        jbtnVPP.addActionListener(evt -> {
                thisControl.openVPP();
                this.dispose();

            });
        // Xử lý nút Quay lại
            btnBack.addActionListener(evt -> {
                    if (parent != null) {
                        parent.setVisible(true);
                    } else {
                        thisControl.Back();
                    }
                    this.dispose();
            });

             jbtnOrderM.addActionListener(evt ->{
                   thisControl.openOrder();
                    this.dispose();

            });


             // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(evt -> {
                thisControl.openCustomer(parent, controller, IsManager);
                this.dispose();
        });   
        btnPublisher.addActionListener(evt -> {
               thisControl.openNCC();
               this.dispose();
        });
        btnCategoryM.addActionListener(evt -> {
                thisControl.openDM();
                this.dispose();
        });
        btnProviderM.addActionListener(evt -> {
                thisControl.openNCC();
                this.dispose();
            
        });
        jbtnAuthor.addActionListener(evt ->  {
                thisControl.openAuthor(controller);
                this.dispose();
        });
        jbtnTK.addActionListener(evt ->{
                thisControl.openTK();
                this.dispose();
        });
    }
}
    public MainMenu_Manager2() {
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnBookM = new javax.swing.JButton();
        jbtnVPP = new javax.swing.JButton();
        btnProviderM = new javax.swing.JButton();
        btnPublisher = new javax.swing.JButton();
        jbtnOrderM = new javax.swing.JButton();
        btnCategoryM = new javax.swing.JButton();
        btnPositionM = new javax.swing.JButton();
        btnEmployeeM = new javax.swing.JButton();
        btnCustomerM = new javax.swing.JButton();
        jbtnAuthor = new javax.swing.JButton();
        btnAccount = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jbtnTK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnBack.setBackground(new java.awt.Color(0, 51, 102));
        btnBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Quay lại");
        btnBack.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnBookM.setBackground(new java.awt.Color(0, 51, 102));
        btnBookM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBookM.setForeground(new java.awt.Color(255, 255, 255));
        btnBookM.setText("Quản lí sách");
        btnBookM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnBookM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookMActionPerformed(evt);
            }
        });

        jbtnVPP.setBackground(new java.awt.Color(0, 51, 102));
        jbtnVPP.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtnVPP.setForeground(new java.awt.Color(255, 255, 255));
        jbtnVPP.setText("Quản lí VPP");
        jbtnVPP.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnProviderM.setBackground(new java.awt.Color(0, 51, 102));
        btnProviderM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnProviderM.setForeground(new java.awt.Color(255, 255, 255));
        btnProviderM.setText("Quản lí nhà cung cấp");
        btnProviderM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnPublisher.setBackground(new java.awt.Color(0, 51, 102));
        btnPublisher.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPublisher.setForeground(new java.awt.Color(255, 255, 255));
        btnPublisher.setText("Quản lí nhà xuất bản");
        btnPublisher.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jbtnOrderM.setBackground(new java.awt.Color(0, 51, 102));
        jbtnOrderM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtnOrderM.setForeground(new java.awt.Color(255, 255, 255));
        jbtnOrderM.setText("Quản lí đơn hàng");
        jbtnOrderM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnCategoryM.setBackground(new java.awt.Color(0, 51, 102));
        btnCategoryM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCategoryM.setForeground(new java.awt.Color(255, 255, 255));
        btnCategoryM.setText("Quản lí danh mục");
        btnCategoryM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnPositionM.setBackground(new java.awt.Color(0, 51, 102));
        btnPositionM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnPositionM.setForeground(new java.awt.Color(255, 255, 255));
        btnPositionM.setText("Quản lí chức vụ");
        btnPositionM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnEmployeeM.setBackground(new java.awt.Color(0, 51, 102));
        btnEmployeeM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnEmployeeM.setForeground(new java.awt.Color(255, 255, 255));
        btnEmployeeM.setText("Quản lí nhân viên");
        btnEmployeeM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnCustomerM.setBackground(new java.awt.Color(0, 51, 102));
        btnCustomerM.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCustomerM.setForeground(new java.awt.Color(255, 255, 255));
        btnCustomerM.setText("Quản lí khách hàng");
        btnCustomerM.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jbtnAuthor.setBackground(new java.awt.Color(0, 51, 102));
        jbtnAuthor.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtnAuthor.setForeground(new java.awt.Color(255, 255, 255));
        jbtnAuthor.setText("Quản lí tác giả");
        jbtnAuthor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnAccount.setBackground(new java.awt.Color(0, 51, 102));
        btnAccount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAccount.setForeground(new java.awt.Color(255, 255, 255));
        btnAccount.setText("Quản lí tài khoản");
        btnAccount.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Các mục quản lí");

        jbtnTK.setBackground(new java.awt.Color(0, 51, 102));
        jbtnTK.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jbtnTK.setForeground(new java.awt.Color(255, 255, 255));
        jbtnTK.setText("Thống kê");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(163, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnTK, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(245, 245, 245))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(75, 75, 75)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnBookM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnVPP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnProviderM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jbtnAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jbtnOrderM, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(btnCategoryM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnPositionM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEmployeeM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnCustomerM, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(84, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
                .addComponent(jbtnTK, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(29, 29, 29)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jbtnAuthor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnCategoryM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBookM, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(30, 30, 30)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPositionM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnVPP, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnEmployeeM, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnProviderM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jbtnOrderM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnCustomerM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(64, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBookMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookMActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBookMActionPerformed

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
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu_Manager2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu_Manager2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccount;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBookM;
    private javax.swing.JButton btnCategoryM;
    private javax.swing.JButton btnCustomerM;
    private javax.swing.JButton btnEmployeeM;
    private javax.swing.JButton btnPositionM;
    private javax.swing.JButton btnProviderM;
    private javax.swing.JButton btnPublisher;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbtnAuthor;
    private javax.swing.JButton jbtnOrderM;
    private javax.swing.JButton jbtnTK;
    private javax.swing.JButton jbtnVPP;
    // End of variables declaration//GEN-END:variables
}
