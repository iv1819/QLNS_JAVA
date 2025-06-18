/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.EmployeeController;
import Controller.MainMenuController;
import Model.Employee;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.lang.NumberFormatException;
import java.text.DecimalFormat;

/**
 *
 * @author MSI GF63
 */
public class EmployeeM extends javax.swing.JFrame {
    private EmployeeController employeeController;
    private MainMenuController mainMenuController;

    /**
     * Creates new form EmployeeM
     */
    public EmployeeM() {
        this(null);
    }

    public EmployeeM(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
        initComponents();
        employeeController = new EmployeeController(this, mainMenuController);
        
        jTable_Employees.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Employees.getSelectedRow() != -1) {
                    displaySelectedEmployeeInfo();
                }
            }
        });
        
        employeeController.loadAllEmployees();
    }

    public void displayEmployees(ArrayList<Employee> employees) {
        DefaultTableModel dtm = (DefaultTableModel) jTable_Employees.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);
        dtm.addColumn("Mã NV");
        dtm.addColumn("Tên NV");
        dtm.addColumn("Ngày sinh");
        dtm.addColumn("Ngày vào làm");
        dtm.addColumn("Mã CV");
        dtm.addColumn("Số ĐT");
        dtm.addColumn("Lương");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Employee employee : employees) {
            Vector<Object> row = new Vector<>();
            row.add(employee.getMaNV());
            row.add(employee.getTenNV());
            row.add(employee.getNgaySinh() != null ? sdf.format(employee.getNgaySinh()) : "");
            row.add(employee.getNgayVaoLam() != null ? sdf.format(employee.getNgayVaoLam()) : "");
            row.add(employee.getMaCV());
            row.add(employee.getSdt());
            row.add(employee.getLuong());
            dtm.addRow(row);
        }
        jTable_Employees.getColumnModel().getColumn(6).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                if (value instanceof Number) {
                    setText(String.format("%,.0f", ((Number) value).doubleValue()));
                } else {
                    setText(value != null ? value.toString() : "");
                }
            }
        });
    }

    private void displaySelectedEmployeeInfo() {
        int selectedRow = jTable_Employees.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) jTable_Employees.getModel();
            txtMaNV.setText(model.getValueAt(selectedRow, 0).toString());
            txtTenNV.setText(model.getValueAt(selectedRow, 1).toString());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date ngaySinh = sdf.parse(model.getValueAt(selectedRow, 2).toString());
                Date ngayVaoLam = sdf.parse(model.getValueAt(selectedRow, 3).toString());
                dteNgaySinh.setDate(ngaySinh);
                dteNgayVaoLam.setDate(ngayVaoLam);
            } catch (Exception e) {
                dteNgaySinh.setDate(null);
                dteNgayVaoLam.setDate(null);
            }
            txtMaCV.setText(model.getValueAt(selectedRow, 4).toString());
            txtSdt.setText(model.getValueAt(selectedRow, 5).toString());
            txtLuong.setText(model.getValueAt(selectedRow, 6).toString());
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private void clearInputFields() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        dteNgaySinh.setDate(null);
        dteNgayVaoLam.setDate(null);
        txtMaCV.setText("");
        txtSdt.setText("");
        txtLuong.setText("");
    }

    // Event handlers
    private void jbtnThemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (validateInput()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String maNV = txtMaNV.getText();
                String tenNV = txtTenNV.getText();
                java.util.Date ngaySinh = dteNgaySinh.getDate();
                java.util.Date ngayVaoLam = dteNgayVaoLam.getDate();
                String maCV = txtMaCV.getText();
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                Employee newEmployee = new Employee(maNV, tenNV, ngaySinh, ngayVaoLam, maCV, sdt, luong);
                employeeController.addEmployee(newEmployee);
                clearInputFields();
            }
        } catch (Exception e) {
            showErrorMessage("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void jbtnSuaActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (validateInput()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String maNV = txtMaNV.getText();
                String tenNV = txtTenNV.getText();
                java.util.Date ngaySinh = dteNgaySinh.getDate();
                java.util.Date ngayVaoLam = dteNgayVaoLam.getDate();
                String maCV = txtMaCV.getText();
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                Employee updatedEmployee = new Employee(maNV, tenNV, ngaySinh, ngayVaoLam, maCV, sdt, luong);
                employeeController.updateEmployee(updatedEmployee);
                clearInputFields();
            }
        } catch (Exception e) {
            showErrorMessage("Có lỗi xảy ra: " + e.getMessage());
        }
    }

    private void jbtnXoaActionPerformed(java.awt.event.ActionEvent evt) {
        String maNV = txtMaNV.getText();
        if (maNV.isEmpty()) {
            showErrorMessage("Vui lòng chọn nhân viên cần xóa.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa nhân viên này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            employeeController.deleteEmployee(maNV);
            clearInputFields();
        }
    }

    private void jbtnTimActionPerformed(java.awt.event.ActionEvent evt) {
        String tenNV = jTextField1.getText();
        if (tenNV.isEmpty()) {
            showErrorMessage("Vui lòng nhập tên nhân viên cần tìm.");
            return;
        }
        employeeController.searchEmployees(tenNV);
    }

    private boolean validateInput() {
        if (txtMaNV.getText().isEmpty() || txtTenNV.getText().isEmpty() || dteNgaySinh.getDate() == null ||
            dteNgayVaoLam.getDate() == null || txtMaCV.getText().isEmpty() || txtSdt.getText().isEmpty() || txtLuong.getText().isEmpty()) {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin.");
            return false;
        }
        try {
            Double.parseDouble(txtLuong.getText());
        } catch (NumberFormatException e) {
            showErrorMessage("Lương phải là số.");
            return false;
        }
        return true;
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        MainMenu_Manager managerFrame = new MainMenu_Manager();
        managerFrame.setVisible(true);
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        dteNgaySinh = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dteNgayVaoLam = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtMaCV = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Employees = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mã NV");

        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        jLabel2.setText("Tên NV");

        jLabel3.setText("Ngày sinh");

        jLabel4.setText("Ngày vào làm");

        jLabel5.setText("Mã CV");

        jLabel6.setText("Lương");

        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jButton3.setText("Thêm");

        jButton4.setText("Sửa");

        jButton5.setText("Xóa");

        jTable_Employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Ngày vào làm", "Mã công việc", "Số điện thoại", "Lương"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable_Employees);

        jButton6.setText("Tìm kiếm theo tên");

        jLabel7.setText("Số điện thoại");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(btnBack)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 200, Short.MAX_VALUE)
                    .addComponent(jButton3)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton4)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton5)
                    .addContainerGap())
                .addGroup(layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3))
                            .addGap(10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtMaNV, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                .addComponent(txtTenNV)
                                .addComponent(dteNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton6)))
                    .addGap(40, 40, 40)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addGap(10)
                            .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(10)
                            .addComponent(txtMaCV, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addGap(10)
                            .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addGap(10)
                            .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(30, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addContainerGap())
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBack)
                        .addComponent(jButton3)
                        .addComponent(jButton4)
                        .addComponent(jButton5))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4)
                                .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5)
                                .addComponent(txtMaCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(dteNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)
                                .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton6))))
                    .addGap(18, 18, 18)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {
        // Không cần xử lý gì ở đây
    }

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
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private com.toedter.calendar.JDateChooser dteNgaySinh;
    private com.toedter.calendar.JDateChooser dteNgayVaoLam;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Employees;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaCV;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
