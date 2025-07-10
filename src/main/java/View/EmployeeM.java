/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.EmployeeController;
import Controller.MainMenuController;
import Controller.MainMenuManagerController;
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
    private MainMenuManagerController mainMenuManagerController;
    private Position_Connect positionConnect = new Position_Connect();
    private MainMenu parentMainMenu; // Thêm tham chiếu đến MainMenu
    private boolean isManager; // Thêm thông tin về quyền

    /**
     * Creates new form EmployeeM
     */
    public EmployeeM() {
        this(null, null, false, null);
    }

    public EmployeeM(MainMenuController mainMenuController) {
        this(mainMenuController, null, false, null);
    }
    
    // Constructor mới để nhận tham chiếu đến MainMenu và thông tin quyền
    public EmployeeM(MainMenuController mainMenuController, MainMenu parentMainMenu, boolean isManager) {
        this(mainMenuController, parentMainMenu, isManager, null);
    }
    
    // Constructor đầy đủ với MainMenuManagerController
    public EmployeeM(MainMenuController mainMenuController, MainMenu parentMainMenu, boolean isManager, MainMenuManagerController mainMenuManagerController) {
        this.mainMenuController = mainMenuController;
        this.mainMenuManagerController = mainMenuManagerController;
        this.parentMainMenu = parentMainMenu;
        this.isManager = isManager;
        initComponents();
        employeeController = new EmployeeController(this, mainMenuController);
        loadMaCVToComboBox();
        cboMaCV.addActionListener(e -> updateTenCVByMaCV());
        
        
        // Disabled trường mã nhân viên khi khởi tạo
        txtMaNV.setEnabled(false);
        taoMaNhanVienTuDong(); // Luôn tự động sinh mã khi mở form
        // Disable nút Sửa và Xóa khi khởi tạo
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        
        jTable_Employees.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Employees.getSelectedRow() != -1) {
                    displaySelectedEmployeeInfo();
                }
            }
        });
        employeeController.loadAllEmployees();
        
        // Test tạo mã nhân viên
        Database.Employee_Connect employeeConnect = new Database.Employee_Connect();
        employeeConnect.testTaoMaNhanVien();
    }

    private void loadMaCVToComboBox() {
        cboMaCV.removeAllItems();
        // Thay vì add mã công việc, add tên công việc
        for (String maCV : positionConnect.getAllMaCV()) {
            String tenCV = positionConnect.getTenCVByMaCV(maCV);
            cboMaCV.addItem(tenCV);
        }
        if (cboMaCV.getItemCount() > 0) {
            cboMaCV.setSelectedIndex(0);
        }
    }

    private void updateTenCVByMaCV() {
        // Không cần xử lý gì nữa vì bạn sẽ tự xóa trường này
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
                cboMaCV.setSelectedItem(tenCV); // Hiển thị tên công việc trong combobox
            }
            txtSdt.setText(model.getValueAt(selectedRow, 5).toString());
            txtLuong.setText(model.getValueAt(selectedRow, 6).toString());
            
            // Luôn disabled trường mã nhân viên, kể cả khi sửa
            txtMaNV.setEnabled(false);
            // Enable nút Sửa và Xóa khi chọn dòng
            btnSua.setEnabled(true);
            btnXoa.setEnabled(true);
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
        // Disabled trường mã nhân viên khi thêm mới
        txtMaNV.setEnabled(false);
        taoMaNhanVienTuDong(); // Luôn tự động sinh mã khi clear input
        // Disable nút Sửa và Xóa khi clear input
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
    }
    
    /**
     * Tạo mã nhân viên tự động và hiển thị lên trường mã
     */
    private void taoMaNhanVienTuDong() {
        try {
            Database.Employee_Connect employeeConnect = new Database.Employee_Connect();
            String maNVTaoTuDong = employeeConnect.taoMaNhanVienTuDong();
            txtMaNV.setText(maNVTaoTuDong);
            txtMaNV.setEnabled(false); // Đảm bảo trường mã luôn disabled
            System.out.println("Đã tạo mã nhân viên mới: " + maNVTaoTuDong);
        } catch (Exception e) {
            System.err.println("Lỗi tạo mã nhân viên: " + e.getMessage());
            showErrorMessage("Lỗi tạo mã nhân viên: " + e.getMessage());
        }
    }

    // Event handlers
    private void jbtnThemActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            // Luôn tạo mã mới khi thêm nhân viên mới
            taoMaNhanVienTuDong();
            
            if (validateInput()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String maNV = txtMaNV.getText();
                String tenNV = txtTenNV.getText();
                java.util.Date ngaySinh = dteNgaySinh.getDate();
                java.util.Date ngayVaoLam = dteNgayVaoLam.getDate();
                String maCV = positionConnect.getMaCVByTenCV((String) cboMaCV.getSelectedItem()); // Lấy mã công việc từ tên
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                String tenCV = (String) cboMaCV.getSelectedItem(); // Lấy tên công việc để lưu
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
                String maCV = positionConnect.getMaCVByTenCV((String) cboMaCV.getSelectedItem()); // Lấy mã công việc từ tên
                String sdt = txtSdt.getText();
                double luong = Double.parseDouble(txtLuong.getText());
                String tenCV = (String) cboMaCV.getSelectedItem(); // Lấy tên công việc để lưu
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

        // Validate mã nhân viên (luôn validate vì trường mã luôn disabled)
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
        this.dispose();
    }

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        // TODO add your handling code here:
        employeeController.exportEmployeeToExcel();
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // Clear tất cả các trường thông tin
        clearInputFields();
        
        // Bỏ chọn dòng trong bảng
        jTable_Employees.clearSelection();
        
        // Tải lại dữ liệu nhân viên
        employeeController.refreshData();
    }//GEN-LAST:event_btnRefreshActionPerformed

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Employees = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnExport = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btnRefresh = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        cboMaCV = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtTenNV = new javax.swing.JTextField();
        dteNgaySinh = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dteNgayVaoLam = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

        jPanel1.setBackground(new java.awt.Color(0, 0, 102));

        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnThemActionPerformed(evt);
            }
        });

        btnExport.setText("Export");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSuaActionPerformed(evt);
            }
        });

        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnXoaActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Quản lý nhân viên");
        jLabel8.setMaximumSize(new java.awt.Dimension(113, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(113, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(113, 22));

        btnRefresh.setText("Làm mới");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addGap(199, 199, 199)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnThem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSua)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnXoa)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnExport)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnBack)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel6.setText("Lương");

        jLabel1.setText("Mã NV");

        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        jLabel2.setText("Tên NV");

        jLabel3.setText("Ngày sinh");

        jLabel4.setText("Ngày vào làm");

        jLabel5.setText("Tên CV");

        jLabel7.setText("Số điện thoại");

        btnSearch.setText("Tìm kiếm");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(dteNgaySinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtTenNV)
                        .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(141, 141, 141)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSdt)
                            .addComponent(txtLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(cboMaCV, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSearch)))
                .addContainerGap(91, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txtLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dteNgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(cboMaCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSearch)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel4)
                        .addComponent(dteNgayVaoLam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnRefresh;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Employees;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtTenNV;
    // End of variables declaration//GEN-END:variables
}
