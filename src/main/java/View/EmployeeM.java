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
import Database.Position_Connect;
import java.awt.event.ActionListener;

/**
 *
 * @author MSI GF63
 */
public class EmployeeM extends javax.swing.JFrame {
    private EmployeeController employeeController;
    private MainMenuController mainMenuController;
    private Position_Connect positionConnect = new Position_Connect();

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
        loadMaCVToComboBox();
        cboMaCV.addActionListener(e -> updateTenCVByMaCV());
        txtTenCV.setEnabled(false);
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

    private void loadMaCVToComboBox() {
        cboMaCV.removeAllItems();
        for (String maCV : positionConnect.getAllMaCV()) {
            cboMaCV.addItem(maCV);
        }
        if (cboMaCV.getItemCount() > 0) {
            cboMaCV.setSelectedIndex(0);
            updateTenCVByMaCV();
        }
    }

    private void updateTenCVByMaCV() {
        String maCV = (String) cboMaCV.getSelectedItem();
        if (maCV != null) {
            txtTenCV.setText(positionConnect.getTenCVByMaCV(maCV));
        } else {
            txtTenCV.setText("");
        }
    }

    public void displayEmployees(ArrayList<Employee> employees) {
        DefaultTableModel dtm = (DefaultTableModel) jTable_Employees.getModel();
        dtm.setRowCount(0);
        dtm.setColumnCount(0);
        dtm.addColumn("Mã NV");
        dtm.addColumn("Tên NV");
        dtm.addColumn("Ngày sinh");
        dtm.addColumn("Ngày vào làm");
        dtm.addColumn("Tên công việc");
        dtm.addColumn("Số ĐT");
        dtm.addColumn("Lương");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Employee employee : employees) {
            Vector<Object> row = new Vector<>();
            row.add(employee.getMaNV());
            row.add(employee.getTenNV());
            row.add(employee.getNgaySinh() != null ? sdf.format(employee.getNgaySinh()) : "");
            row.add(employee.getNgayVaoLam() != null ? sdf.format(employee.getNgayVaoLam()) : "");
            row.add(employee.getTenCV());
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
            String tenCV = model.getValueAt(selectedRow, 4).toString();
            String maCV = null;
            for (String item : positionConnect.getAllMaCV()) {
                if (positionConnect.getTenCVByMaCV(item).equals(tenCV)) {
                    maCV = item;
                    break;
                }
            }
            if (maCV != null) {
                cboMaCV.setSelectedItem(maCV);
            }
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
                String maCV = (String) cboMaCV.getSelectedItem();
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                String tenCV = txtTenCV.getText();
                Employee newEmployee = new Employee(maNV, tenNV, ngaySinh, ngayVaoLam, maCV, sdt, luong, tenCV);
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
                String maCV = (String) cboMaCV.getSelectedItem();
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                String tenCV = txtTenCV.getText();
                Employee updatedEmployee = new Employee(maNV, tenNV, ngaySinh, ngayVaoLam, maCV, sdt, luong, tenCV);
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

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String tenNV = txtSearch.getText().trim();
        if (tenNV.isEmpty()) {
            employeeController.loadAllEmployees();
        } else {
            employeeController.searchEmployees(tenNV);
        }
    }

    private boolean validateInput() {
        if (txtMaNV.getText().isEmpty() || txtTenNV.getText().isEmpty() || dteNgaySinh.getDate() == null ||
            dteNgayVaoLam.getDate() == null || cboMaCV.getSelectedItem() == null || txtSdt.getText().isEmpty() || txtLuong.getText().isEmpty()) {
            showErrorMessage("Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        // Validate mã nhân viên
        if (!txtMaNV.getText().matches("^NV\\d{3}$")) {
            showErrorMessage("Mã nhân viên phải có định dạng NVxxx (x là số)");
            return false;
        }

        // Validate số điện thoại
        if (!txtSdt.getText().matches("^0\\d{9}$")) {
            showErrorMessage("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0");
            return false;
        }

        // Validate lương
        try {
            double luong = Double.parseDouble(txtLuong.getText());
            if (luong <= 0) {
                showErrorMessage("Lương phải lớn hơn 0");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Lương phải là số");
            return false;
        }

        // Validate ngày vào làm phải sau ngày sinh
        if (dteNgayVaoLam.getDate().before(dteNgaySinh.getDate())) {
            showErrorMessage("Ngày vào làm phải sau ngày sinh");
            return false;
        }

        return true;
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        MainMenu_Manager2 managerFrame = new MainMenu_Manager2();
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
        txtLuong = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Employees = new javax.swing.JTable();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        cboMaCV = new javax.swing.JComboBox<>();
        txtTenCV = new javax.swing.JTextField();

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

        btnThem.setText("Thêm");

        btnSua.setText("Sửa");

        btnXoa.setText("Xóa");

        jTable_Employees.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Ngày vào làm", "Tên công việc", "Số điện thoại", "Lương"
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

        btnSearch.setText("Tìm kiếm theo tên");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        jLabel7.setText("Số điện thoại");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dteNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(txtTenNV)
                            .addComponent(txtMaNV)
                            .addComponent(txtSearch))
                        .addGap(8, 8, 8)
                        .addComponent(btnSearch)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(43, 43, 43)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                                    .addComponent(txtSdt)
                                    .addComponent(txtLuong)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cboMaCV, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtTenCV, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)))))
                .addContainerGap(39, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBack)
                            .addComponent(btnThem)
                            .addComponent(btnSua)
                            .addComponent(btnXoa))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel4))
                            .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dteNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 22, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch))
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboMaCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {
        
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
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboMaCV;
    private com.toedter.calendar.JDateChooser dteNgaySinh;
    private com.toedter.calendar.JDateChooser dteNgayVaoLam;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Employees;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenCV;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
