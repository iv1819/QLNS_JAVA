/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.MainMenuController;

/**
 *
 * @author Admin
 */
public class MainMenu_Manager2 extends javax.swing.JFrame {
    private MainMenuController controller;

    /**
     * Creates new form MainMenu_Manager
     */
    public MainMenu_Manager2(MainMenu parent, MainMenuController controller, boolean IsManager) {
    this.controller = controller;  // <-- KHÔNG còn null
    initComponents();

    if(IsManager){
        btnBookM.addActionListener(evt -> {
            BookM bookM = new BookM(this.controller);
            bookM.setVisible(true);
            dispose();
        });
        jbtnVPP.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    VppM vpp = new VppM(controller);
                    vpp.setVisible(true);
                                    dispose();

                }
            });
        // Xử lý nút Quay lại
            btnBack.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setVisible(true);
                    dispose();
                }
            });

             jbtnOrderM.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    OrderM orderM = new OrderM();
                    orderM.setVisible(true);
                    dispose();
                }
            });

            // Xử lý nút Quản lý nhân viên
            btnEmployeeM.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    EmployeeM employeeM = new EmployeeM();
                    employeeM.setVisible(true);
                    dispose();
                }
            });

             // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerM customerM = new CustomerM();
                customerM.setVisible(true);
                dispose();
            }
        });        
        // Xử lý nút Quản lý chức vụ
        btnPositionM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
        PositionM positionM = new PositionM();
                positionM.setVisible(true);
                dispose();            }
        });
        btnPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PublisherM publisherM = new PublisherM();
                publisherM.setVisible(true);
                dispose();
            }
        });
        btnCategoryM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoryM categoryM = new CategoryM();
                categoryM.setVisible(true);
                dispose();
            }
        });
        btnProviderM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProviderM providerM = new ProviderM();
                providerM.setVisible(true);
                dispose();
            }
        });
        btnAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccountM accountM = new AccountM();
                accountM.setVisible(true);
                dispose();
            }
        });
    }
    else{
        btnEmployeeM.setEnabled(false);
        btnPositionM.setEnabled(false);

        btnBookM.addActionListener(evt -> {
        BookM bookM = new BookM(this.controller);
        bookM.setVisible(true);
        dispose();
        });
    jbtnVPP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VppM vpp = new VppM(controller);
                vpp.setVisible(true);
                                dispose();

            }
        });
    // Xử lý nút Quay lại
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
                dispose();
            }
        });
        
         jbtnOrderM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OrderM orderM = new OrderM();
                orderM.setVisible(true);
                dispose();
            }
        });
        
        // Xử lý nút Quản lý khách hàng
        btnCustomerM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerM customerM = new CustomerM();
                customerM.setVisible(true);
                dispose();
            }
        });
        btnPublisher.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PublisherM publisherM = new PublisherM();
                publisherM.setVisible(true);
                dispose();
            }
        });
        btnCategoryM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CategoryM categoryM = new CategoryM();
                categoryM.setVisible(true);
                dispose();
            }
        });
        btnProviderM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProviderM providerM = new ProviderM();
                providerM.setVisible(true);
                dispose();
            }
        });
    }
}
    public MainMenu_Manager2() {
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        jButton2 = new javax.swing.JButton();
        btnAccount = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnBack.setText("Quay lại");

        btnBookM.setText("Quản lí sách");

        jbtnVPP.setText("Quản lí VPP");

        btnProviderM.setText("Quản lí nhà cung cấp");

        btnPublisher.setText("Quản lí nhà xuất bản");

        jbtnOrderM.setText("Quản lí đơn hàng");

        btnCategoryM.setText("Quản lí danh mục");

        btnPositionM.setText("Quản lí chức vụ");

        btnEmployeeM.setText("Quản lí nhân viên");

        btnCustomerM.setText("Quản lí khách hàng");

        jButton2.setText("Quản lí tác giả");

        btnAccount.setText("Quản lí tài khoản");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(81, 81, 81)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnBookM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnVPP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnProviderM, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                            .addComponent(btnPublisher, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnOrderM, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnCategoryM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnPositionM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEmployeeM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCustomerM, javax.swing.GroupLayout.DEFAULT_SIZE, 146, Short.MAX_VALUE))))
                .addContainerGap(128, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                    .addComponent(btnCategoryM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnBookM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAccount, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                    .addComponent(btnPositionM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbtnVPP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEmployeeM, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnProviderM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbtnOrderM, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCustomerM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnPublisher, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(110, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jbtnOrderM;
    private javax.swing.JButton jbtnVPP;
    // End of variables declaration//GEN-END:variables
}
