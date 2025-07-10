/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.CustomerController;
import Controller.MainMenuController;
import Controller.MainMenuManagerController;
import Model.Customer;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author MSI GF63
 */
public class CustomerM extends javax.swing.JFrame {
    private CustomerController customerController;
    private Customer currentSelectedCustomer;
    private MainMenu parentMainMenu; // Thêm tham chiếu đến MainMenu
    private MainMenuController mainMenuController; // Thêm controller
    private MainMenuManagerController mainMenuManagerController;
    private boolean isManager; // Thêm thông tin về quyền

    /**
     * Creates new form CustomerM
     */
    public CustomerM() {
        this(null, null, false, null);
    }
    
    /**
     * Constructor với tham số đầy đủ
     */
    public CustomerM(MainMenuController mainMenuController, MainMenu parentMainMenu, boolean isManager) {
        this(mainMenuController, parentMainMenu, isManager, null);
    }
    
    /**
     * Constructor với MainMenuManagerController
     */
    public CustomerM(MainMenuController mainMenuController, MainMenu parentMainMenu, boolean isManager, MainMenuManagerController mainMenuManagerController) {
        this.mainMenuController = mainMenuController;
        this.mainMenuManagerController = mainMenuManagerController;
        this.parentMainMenu = parentMainMenu;
        this.isManager = isManager;
        
        initComponents();
        customerController = new CustomerController(this);
        setupEventListeners();
        loadCustomers();
        generateCustomerCode();
        // Luôn disable nút Sửa và Xóa khi khởi tạo
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }
    
    private void setupEventListeners() {
        // Thêm event listener cho table selection
        tblKH.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblKH.getSelectedRow() != -1) {
                    displaySelectedCustomerInfo();
                }
            }
        });
        
        // Thêm event listeners cho các button
        btnThem.addActionListener(e -> handleAddCustomer());
        btnSua.addActionListener(e -> handleUpdateCustomer());
        btnXoa.addActionListener(e -> handleDeleteCustomer());
        btnTimKiem.addActionListener(e -> handleSearchCustomer());
        btnRefresh.addActionListener(e -> handleRefresh());
        btnBack.addActionListener(e -> handleBack());
    }
    
    private void loadCustomers() {
        customerController.loadAllCustomers();
    }
    
    private void generateCustomerCode() {
        String newCode = customerController.generateCustomerCode();
        txtMaKH.setText(newCode);
        txtMaKH.setEnabled(false); // Disable mã khách hàng khi thêm mới
    }
    
    private void handleAddCustomer() {
        Customer customer = getCustomerFromInput();
        if (customer != null) {
            customerController.addCustomer(customer);
        }
    }
    
    private void handleUpdateCustomer() {
        if (currentSelectedCustomer == null) {
            showErrorMessage("Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        Customer customer = getCustomerFromInput();
        if (customer != null) {
            customer.setMaKH(currentSelectedCustomer.getMaKH()); // Giữ nguyên mã cũ
            customerController.updateCustomer(customer);
        }
    }
    
    private void handleDeleteCustomer() {
        if (currentSelectedCustomer == null) {
            showErrorMessage("Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        customerController.deleteCustomer(currentSelectedCustomer.getMaKH());
    }
    
    private void handleSearchCustomer() {
        String keyword = txtTimKiem.getText().trim();
        customerController.searchCustomers(keyword);
    }
    
    private void handleRefresh() {
        clearInputFields();
        loadCustomers();
    }
    
    private void handleBack() {
        // Quay về giao diện MainMenu_Manager2
        if (mainMenuManagerController != null && parentMainMenu != null) {
            // Sử dụng MainMenuManagerController để quay lại
            MainMenu_Manager2 managerFrame = new MainMenu_Manager2(parentMainMenu, mainMenuController, isManager);
            managerFrame.setVisible(true);
        } else if (parentMainMenu != null) {
            // Tạo lại MainMenu_Manager2 với thông tin đúng
            MainMenu_Manager2 managerFrame = new MainMenu_Manager2(parentMainMenu, mainMenuController, isManager);
            managerFrame.setVisible(true);
        } else {
            // Fallback nếu không có tham chiếu
            MainMenu_Manager2 managerFrame = new MainMenu_Manager2();
            managerFrame.setVisible(true);
        }
        dispose();
    }
    
    private Customer getCustomerFromInput() {
        String maKH = txtMaKH.getText().trim();
        String tenKH = txtTenKH.getText().trim();
        String sdt = txtSdt.getText().trim();
        
        if (tenKH.isEmpty()) {
            showErrorMessage("Vui lòng nhập tên khách hàng!");
            return null;
        }
        
        if (sdt.isEmpty()) {
            showErrorMessage("Vui lòng nhập số điện thoại!");
            return null;
        }
        
        return new Customer(maKH, tenKH, sdt);
    }
    
    public void displayCustomers(ArrayList<Customer> customers) {
        DefaultTableModel dtm = (DefaultTableModel) tblKH.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);
        dtm.addColumn("Mã KH");
        dtm.addColumn("Tên KH");
        dtm.addColumn("SDT");
        
        for (Customer customer : customers) {
            Vector<Object> row = new Vector<>();
            row.add(customer.getMaKH());
            row.add(customer.getTenKH());
            row.add(customer.getSdt());
            dtm.addRow(row);
        }
    }
    
    private void displaySelectedCustomerInfo() {
        int selectedRow = tblKH.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) tblKH.getModel();
            String maKH = model.getValueAt(selectedRow, 0).toString();
            String tenKH = model.getValueAt(selectedRow, 1).toString();
            String sdt = model.getValueAt(selectedRow, 2).toString();
            
            currentSelectedCustomer = new Customer(maKH, tenKH, sdt);
            
            txtMaKH.setText(maKH);
            txtTenKH.setText(tenKH);
            txtSdt.setText(sdt);
            
            // Luôn disabled trường mã khách hàng khi chọn để sửa/xóa
            txtMaKH.setEnabled(false);
            // Enable nút Sửa và Xóa khi chọn dòng
            btnSua.setEnabled(true);
            btnXoa.setEnabled(true);
        }
    }
    
    public void clearInputFields() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSdt.setText("");
        currentSelectedCustomer = null;
        generateCustomerCode(); // Tạo mã mới cho lần thêm tiếp theo
        // Luôn disabled trường mã khách hàng khi thêm mới
        txtMaKH.setEnabled(false);
        // Disable nút Sửa và Xóa khi clear input
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    
    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Xác nhận", JOptionPane.YES_NO_OPTION);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtMaKH = new javax.swing.JTextField();
        txtTenKH = new javax.swing.JTextField();
        txtSdt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKH = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));

        btnBack.setText("Quay lại");

        btnRefresh.setText("Làm mới");

        btnThem.setText("Thêm");

        btnSua.setText("Sửa");

        btnXoa.setText("Xóa");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quản lý khách hàng");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh)
                .addGap(36, 36, 36)
                .addComponent(btnThem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSua)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnRefresh)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(jLabel1))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jLabel2.setText("Mã KH");

        jLabel3.setText("Tên KH");

        jLabel4.setText("SDT");

        btnTimKiem.setText("Tìm kiếm");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(99, 99, 99)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(55, 55, 55)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtTenKH, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(txtSdt))
                        .addGap(87, 87, 87)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTimKiem)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(58, 58, 58)
                        .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(517, 517, 517))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        tblKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã KH", "Tên KH", "SDT"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblKH);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(CustomerM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CustomerM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblKH;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTenKH;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
