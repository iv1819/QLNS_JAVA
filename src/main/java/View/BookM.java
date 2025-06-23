/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Controller.BookController; // Import Controller
import Controller.MainMenuController;
import Model.Book;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JFrame;
/**
 *
 * @author Admin
 */
public class BookM extends javax.swing.JFrame {
private BookController bookController;
    private String currentImagePath = "";
    // Lưu đường dẫn ảnh hiện tại
    /**
     * Creates new form BookM
     */
 public BookM() {
        this(null); // Call the main constructor with a null controller
    }
    public BookM(MainMenuController mainMenuController) {
        initComponents();
         bookController = new BookController(this, JImage, mainMenuController);
        jTable_Books.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && jTable_Books.getSelectedRow() != -1) {
                    displaySelectedBookInfo();
                }
            }
        });
        populateComboBoxes();
        // Tải dữ liệu khi khởi tạo
        bookController.loadAllBooks();
    }
    // Phương thức mới để tải dữ liệu cho ComboBoxes
    private void populateComboBoxes() {
        // Tải dữ liệu cho ComboBox Nhà xuất bản
        List<String> nhaXBNames = bookController.getAllNhaXBNames();
        DefaultComboBoxModel<String> nxbModel = new DefaultComboBoxModel<>();
        for (String name : nhaXBNames) {
            nxbModel.addElement(name);
        }
        jcbxNhaXB.setModel(nxbModel);

        // Tải dữ liệu cho ComboBox Tác giả
        List<String> tacGiaNames = bookController.getAllTacGiaNames();
        DefaultComboBoxModel<String> tgModel = new DefaultComboBoxModel<>();
        for (String name : tacGiaNames) {
            tgModel.addElement(name);
        }
        jcbxTacGia.setModel(tgModel);
        List<String> danhmuc = bookController.getAllDanhMuc();
        DefaultComboBoxModel<String> dmModel = new DefaultComboBoxModel<>();
        for (String name : danhmuc) {
            dmModel.addElement(name);
        }
        jcmbDM.setModel(dmModel);
    }
public void displayBooks(ArrayList<Book> books) {
        DefaultTableModel dtm = (DefaultTableModel) jTable_Books.getModel();
        dtm.setRowCount(0); // Xóa dữ liệu cũ
        dtm.setColumnCount(0); // Xóa cột cũ

        // Định nghĩa cột (nếu chưa có trong initComponents hoặc muốn reset)
        dtm.addColumn("Mã sách");
        dtm.addColumn("Tên sách");
        dtm.addColumn("Số lượng");
        dtm.addColumn("Giá");
        dtm.addColumn("Tác giả");
        dtm.addColumn("Nhà XB");
        dtm.addColumn("Danh mục");

        dtm.addColumn("Năm XB");
        dtm.addColumn("Ảnh"); // Cột ảnh (sẽ hiển thị đường dẫn)

        for (Book book : books) {
            Vector<Object> row = new Vector<>();
            row.add(book.getMaSach());
            row.add(book.getTenSach());
            row.add(book.getSoLuong());
            row.add(book.getGiaBan());
            row.add(book.getTacGia());
            row.add(book.getNhaXB());
                        row.add(book.getDanhMuc());
            row.add(book.getNamXB());
            row.add(book.getDuongDanAnh()); // Hiển thị đường dẫn ảnh
            dtm.addRow(row);
        }
    }
 private void displaySelectedBookInfo() {
        int selectedRow = jTable_Books.getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) jTable_Books.getModel();
            jtxtMaSach.setText(model.getValueAt(selectedRow, 0).toString());
            jtxtTenSach.setText(model.getValueAt(selectedRow, 1).toString());
            jtxtSoLuong.setText(model.getValueAt(selectedRow, 2).toString());
            jtxtGia.setText(model.getValueAt(selectedRow, 3).toString());
            String tacGia = model.getValueAt(selectedRow, 4).toString();
            // Đảm bảo item tồn tại trong model của ComboBox trước khi set
            if (((DefaultComboBoxModel<String>)jcbxTacGia.getModel()).getIndexOf(tacGia) != -1) {
                jcbxTacGia.setSelectedItem(tacGia);
            } else {
                jcbxTacGia.setSelectedIndex(0); // hoặc hiển thị thông báo
            }

            String nhaXB = model.getValueAt(selectedRow, 5).toString();
            if (((DefaultComboBoxModel<String>)jcbxNhaXB.getModel()).getIndexOf(nhaXB) != -1) {
                jcbxNhaXB.setSelectedItem(nhaXB);
            } else {
               
                jcbxNhaXB.setSelectedIndex(0); // hoặc hiển thị thông báo
            }
            String danhmuc = model.getValueAt(selectedRow, 6).toString();
            if (((DefaultComboBoxModel<String>)jcmbDM.getModel()).getIndexOf(danhmuc) != -1) {
                jcmbDM.setSelectedItem(danhmuc);
            } else {
                // Xử lý nếu giá trị không có trong danh sách
                jcmbDM.setSelectedIndex(0); // hoặc hiển thị thông báo
            }            jtxtNamXB.setText(model.getValueAt(selectedRow, 7).toString());
            
            // Hiển thị ảnh
            jtxtAnh.setText(model.getValueAt(selectedRow, 8).toString());
            currentImagePath = model.getValueAt(selectedRow, 8).toString();
            bookController.updateImagePreview(currentImagePath); // Gọi controller để cập nhật ảnh
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jComboBox2 = new javax.swing.JComboBox<>();
        JPanel_Top = new javax.swing.JPanel();
        JUpper = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jbtnThem = new javax.swing.JButton();
        jbtnSua = new javax.swing.JButton();
        jbtnXoa = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        JMiddle = new javax.swing.JPanel();
        JMaSach = new javax.swing.JLabel();
        JTenSach = new javax.swing.JLabel();
        JTacGia = new javax.swing.JLabel();
        jcbxTacGia = new javax.swing.JComboBox<>();
        jtxtMaSach = new javax.swing.JTextField();
        jtxtTenSach = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jcbxNhaXB = new javax.swing.JComboBox<>();
        jtxtSoLuong = new javax.swing.JTextField();
        jtxtGia = new javax.swing.JTextField();
        jtxtNamXB = new javax.swing.JTextField();
        jbtnAnh = new javax.swing.JButton();
        jtxtAnh = new javax.swing.JTextField();
        JImage = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jcmbDM = new javax.swing.JComboBox<>();
        JBottom = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_Books = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtxtTimTenSach = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jtxtTimTenTG = new javax.swing.JTextField();
        jbtnTim = new javax.swing.JButton();

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(150, 40));

        JPanel_Top.setBackground(new java.awt.Color(255, 255, 255));
        JPanel_Top.setForeground(new java.awt.Color(242, 242, 242));
        JPanel_Top.setFocusTraversalPolicyProvider(true);

        JUpper.setBackground(new java.awt.Color(0, 0, 102));
        JUpper.setForeground(new java.awt.Color(242, 242, 242));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Quản lí Sách");

        jbtnThem.setFont(new java.awt.Font("Leelawadee UI Semilight", 0, 12)); // NOI18N
        jbtnThem.setText("Thêm");
        jbtnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnThemActionPerformed(evt);
            }
        });

        jbtnSua.setText("Sửa");
        jbtnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSuaActionPerformed(evt);
            }
        });

        jbtnXoa.setText("Xóa");
        jbtnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnXoaActionPerformed(evt);
            }
        });

        btnBack.setText("Quay lại");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JUpperLayout = new javax.swing.GroupLayout(JUpper);
        JUpper.setLayout(JUpperLayout);
        JUpperLayout.setHorizontalGroup(
            JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUpperLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnThem)
                .addGap(18, 18, 18)
                .addComponent(jbtnSua)
                .addGap(18, 18, 18)
                .addComponent(jbtnXoa)
                .addGap(10, 10, 10))
        );
        JUpperLayout.setVerticalGroup(
            JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JUpperLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbtnThem)
                        .addComponent(jbtnSua)
                        .addComponent(jbtnXoa))
                    .addGroup(JUpperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(btnBack)))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        JMiddle.setBackground(new java.awt.Color(255, 255, 255));

        JMaSach.setText("Mã sách");

        JTenSach.setText("Tên sách");

        JTacGia.setText("Tác giả");

        jcbxTacGia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setText("Nhà xuất bản");

        jLabel3.setText("Số lượng");

        jLabel4.setText("Giá");

        jLabel5.setText("Ảnh");

        jLabel6.setText("Năm xuất bản");

        jcbxNhaXB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jbtnAnh.setBackground(new java.awt.Color(204, 204, 204));
        jbtnAnh.setText("Chọn ảnh");
        jbtnAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAnhActionPerformed(evt);
            }
        });

        jtxtAnh.setEditable(false);

        JImage.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        JImage.setText("Ảnh");
        JImage.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        JImage.setPreferredSize(new java.awt.Dimension(70, 70));

        jLabel10.setText("Danh mục");

        jcmbDM.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout JMiddleLayout = new javax.swing.GroupLayout(JMiddle);
        JMiddle.setLayout(JMiddleLayout);
        JMiddleLayout.setHorizontalGroup(
            JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JMiddleLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(JMaSach, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(JTenSach, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addComponent(JTacGia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(JMiddleLayout.createSequentialGroup()
                            .addComponent(jtxtAnh)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jbtnAnh))
                        .addComponent(jcbxTacGia, 0, 209, Short.MAX_VALUE)
                        .addComponent(jtxtMaSach)
                        .addComponent(jtxtTenSach))
                    .addComponent(JImage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(104, 104, 104)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtGia)
                    .addComponent(jtxtNamXB, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jtxtSoLuong, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jcbxNhaXB, 0, 227, Short.MAX_VALUE)
                    .addComponent(jcmbDM, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(134, Short.MAX_VALUE))
        );
        JMiddleLayout.setVerticalGroup(
            JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JMiddleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JMaSach)
                    .addComponent(jtxtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jcbxNhaXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTenSach)
                    .addComponent(jtxtTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JTacGia)
                    .addComponent(jcbxTacGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jtxtNamXB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnAnh)
                    .addComponent(jtxtAnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JMiddleLayout.createSequentialGroup()
                        .addGroup(JMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(jcmbDM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(JImage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jScrollPane1.setViewportView(jTable_Books);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Tìm kiếm theo tên sách và tên tác giả");

        jLabel8.setText("Tên sách");

        jLabel9.setText("Tên tác giả");

        jbtnTim.setText("Tìm kiếm");
        jbtnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout JBottomLayout = new javax.swing.GroupLayout(JBottom);
        JBottom.setLayout(JBottomLayout);
        JBottomLayout.setHorizontalGroup(
            JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JBottomLayout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(JBottomLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTimTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTimTenTG, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                .addComponent(jbtnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addComponent(jScrollPane1)
        );
        JBottomLayout.setVerticalGroup(
            JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtTimTenSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jtxtTimTenTG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTim))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout JPanel_TopLayout = new javax.swing.GroupLayout(JPanel_Top);
        JPanel_Top.setLayout(JPanel_TopLayout);
        JPanel_TopLayout.setHorizontalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(JUpper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(JMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(JPanel_TopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(JBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JPanel_TopLayout.setVerticalGroup(
            JPanel_TopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JPanel_TopLayout.createSequentialGroup()
                .addComponent(JUpper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(JMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(JBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(JPanel_Top, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnThemActionPerformed
        // TODO add your handling code here:
        String maSach = jtxtMaSach.getText();
        String tenSach = jtxtTenSach.getText();
        int soLuong = Integer.parseInt(jtxtSoLuong.getText());
        double giaBan = Double.parseDouble(jtxtGia.getText());
        String tacGia = jcbxTacGia.getSelectedItem().toString();
        String nhaXB = jcbxNhaXB.getSelectedItem().toString();
        int namXB = Integer.parseInt(jtxtNamXB.getText());
        String danhmuc = jcmbDM.getSelectedItem().toString();
        String anh = jtxtAnh.getText();
        Book newBook = new Book(maSach, tenSach, soLuong, giaBan, tacGia, nhaXB, anh, namXB, danhmuc);
        bookController.addBook(newBook);
        clearInputFields();
    }//GEN-LAST:event_jbtnThemActionPerformed

    private void jbtnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSuaActionPerformed
        // TODO add your handling code here:
        String maSach = jtxtMaSach.getText();
        String tenSach = jtxtTenSach.getText();
        int soLuong = Integer.parseInt(jtxtSoLuong.getText());
        double giaBan = Double.parseDouble(jtxtGia.getText());
        String tacGia = jcbxTacGia.getSelectedItem().toString();
        String nhaXB = jcbxNhaXB.getSelectedItem().toString();
        int namXB = Integer.parseInt(jtxtNamXB.getText());
        String danhmuc = jcmbDM.getSelectedItem().toString();
        String anh = jtxtAnh.getText();
        Book updatedBook = new Book(maSach, tenSach, soLuong, giaBan, tacGia, nhaXB, anh, namXB, danhmuc);
        bookController.updateBook(updatedBook);
        clearInputFields();
    }//GEN-LAST:event_jbtnSuaActionPerformed

    private void jbtnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnXoaActionPerformed
        // TODO add your handling code here:
          String maSach = jtxtMaSach.getText(); // Lấy mã sách từ trường nhập
        if (maSach.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sách này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bookController.deleteBook(maSach);
            clearInputFields();
        }
    }//GEN-LAST:event_jbtnXoaActionPerformed

    private void jbtnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTimActionPerformed
        // TODO add your handling code here:
        String tenSach = jtxtTimTenSach.getText();
        String tenTacGia = jtxtTimTenTG.getText();
        bookController.searchBooks(tenSach, tenTacGia);
    }//GEN-LAST:event_jbtnTimActionPerformed

    private void jbtnAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAnhActionPerformed
        // TODO add your handling code here:
        bookController.handleImageSelection();
        currentImagePath = bookController.getSelectedImagePath();
        jtxtAnh.setText(currentImagePath);
    }//GEN-LAST:event_jbtnAnhActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        MainMenu_Manager2 managerFrame = new MainMenu_Manager2();
        managerFrame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

        private void clearInputFields() {
        jtxtMaSach.setText("");
        jtxtTenSach.setText("");
        jtxtSoLuong.setText("");
        jtxtGia.setText("");
        jtxtNamXB.setText("");
        jcbxTacGia.setSelectedIndex(0); // Đặt lại về mục đầu tiên
        jcbxNhaXB.setSelectedIndex(0); // Đặt lại về mục đầu tiên
        jtxtAnh.setText("");
        currentImagePath = ""; // Xóa đường dẫn ảnh đã chọn
        JImage.setIcon(null); // Xóa ảnh hiển thị
        JImage.setText("Ảnh sách"); // Đặt lại text mặc định
    }
    
         public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BookM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BookM().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JBottom;
    private javax.swing.JLabel JImage;
    private javax.swing.JLabel JMaSach;
    private javax.swing.JPanel JMiddle;
    private javax.swing.JPanel JPanel_Top;
    private javax.swing.JLabel JTacGia;
    private javax.swing.JLabel JTenSach;
    private javax.swing.JPanel JUpper;
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable_Books;
    private javax.swing.JButton jbtnAnh;
    private javax.swing.JButton jbtnSua;
    private javax.swing.JButton jbtnThem;
    private javax.swing.JButton jbtnTim;
    private javax.swing.JButton jbtnXoa;
    private javax.swing.JComboBox<String> jcbxNhaXB;
    private javax.swing.JComboBox<String> jcbxTacGia;
    private javax.swing.JComboBox<String> jcmbDM;
    private javax.swing.JTextField jtxtAnh;
    private javax.swing.JTextField jtxtGia;
    private javax.swing.JTextField jtxtMaSach;
    private javax.swing.JTextField jtxtNamXB;
    private javax.swing.JTextField jtxtSoLuong;
    private javax.swing.JTextField jtxtTenSach;
    private javax.swing.JTextField jtxtTimTenSach;
    private javax.swing.JTextField jtxtTimTenTG;
    // End of variables declaration//GEN-END:variables
}
