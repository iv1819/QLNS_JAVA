/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.MainMenuController;
import Model.Book;
import Model.VPP;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import View.EmployeeM;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Admin
 */
public class MainMenu extends javax.swing.JFrame {
private MainMenuController controller;
private Book currentSelectedBook;
private VPP currentSelectedVpp;
private boolean isManager;
private DefaultTableModel tblModelHD;

    public MainMenu() {
    }
    /**
     * Creates new form MainMenu
     * @param isManager
     */
    public MainMenu(boolean isManager) {
        this.isManager = isManager;
        initComponents();
        setLocationRelativeTo(null); 
        tblModelHD = (DefaultTableModel) jtblHD.getModel();
        tblModelHD.setColumnCount(0);
        tblModelHD.addColumn("Tên sách");
        tblModelHD.addColumn("Số lượng");
        tblModelHD.addColumn("Đơn giá");
        tblModelHD.addColumn("Tổng giá");

        controller = new MainMenuController(this);
        controller.loadAndDisplayBooksByCategories();
        jbtnThemHD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Giao tiếp với Presenter khi có sự kiện
                controller.onAddReceiptItemClicked((Integer) jspnSL.getValue());
            }
        });
        jbtnTT.addActionListener(e -> {
            // 1) Kiểm tra bảng hóa đơn
            if (tblModelHD.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    MainMenu.this,
                    "Hoá đơn đang trống – không thể thanh toán.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // 2) Lấy tên KH (có thể để trống)
            Object sel = jcbxTenKH.getSelectedItem();
            String tenKH = (sel == null ||
                            sel.toString().trim().isEmpty() ||
                            sel.toString().equalsIgnoreCase("--Chọn khách hàng--"))
                          ? null   // <‑‑ truyền null
                          : sel.toString().trim();

            // 3) Gọi Controller
            controller.onCheckoutClicked(tenKH);
        });
        // Trong MainMenu constructor – sau khi initComponents()
        jcbxTenKH.addActionListener(e -> {
            controller.onCustomerSelected(
                (Object) jcbxTenKH.getSelectedItem()   // có thể null
            );
        });

        populateComboBox();
        clearReceiptTable();
    }
    public DefaultTableModel getReceiptTableModel() {
    return tblModelHD;
}

 private void populateComboBox() {
        // Tải dữ liệu cho ComboBox Nhà xuất bản
        List<String> tenKHs = controller.getAllTenKH();
        DefaultComboBoxModel<String> khModel = new DefaultComboBoxModel<>();
        khModel.addElement("--Chọn khách hàng--");
        for (String name : tenKHs) {
            
            khModel.addElement(name);
        }
        jcbxTenKH.setModel(khModel);

    }
    public double LayTongTien(){
        return Double.parseDouble(jtxtTongTienHD.getText().replace("$", "").replace(",", "").trim());
    }
     /** Hiển thị các tab Sách + 1 tab VPP */
    public void populateCategoryTabs(LinkedHashMap<String, ArrayList<Book>> booksByCat,
                                 ArrayList<VPP> allVpp) {

            jTabbedPaneBooks.removeAll();

            /* ==== 1. Tab cho sách (như cũ) ==== */
            for (Map.Entry<String, ArrayList<Book>> e : booksByCat.entrySet()) {
                addBookTab(e.getKey(), e.getValue());
            }

            /* ==== 2. Tab cho VPP ==== */
            addVppTab("Văn phòng phẩm", allVpp);
        }

        /* ------------------ helpers ------------------ */

        private void addBookTab(String title, ArrayList<Book> books) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

            if (books.isEmpty()) {
                panel.add(new JLabel("Không có sách trong danh mục này."));
            } else {
                for (Book b : books) {
                    if(b.getSoLuong()<=0){
                        continue;
                    }
                    BookItemPanel p = new BookItemPanel(b, controller);
                    p.setBookData(b);
                    panel.add(p);
                }
            }
            jTabbedPaneBooks.addTab(title, new JScrollPane(panel));
        }

        private void addVppTab(String title, ArrayList<VPP> vpps) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

            if (vpps.isEmpty()) {
                panel.add(new JLabel("Không có VPP."));
            } else {
                for (VPP v : vpps) {
                    if(v.getSoLuong()<=0){
                        continue;
                    }
                    VppItemPanel p = new VppItemPanel(v, controller); // tạo tương tự BookItemPanel
                    p.setVPPData(v);
                    panel.add(p);
                }
            }
            jTabbedPaneBooks.addTab(title, new JScrollPane(panel));
        }

    public void addReceiptItem(Object[] rowData) {
        tblModelHD.addRow(rowData);
    }
     public void updateSelectedBook(Book book) {
        this.currentSelectedBook = book;
        if (book != null) {
            jtxtTenSpHD.setText(book.getTenSach());
            jspnSL.setValue(1); 
            jbtnThemHD.setEnabled(true);
        } else {
            jtxtTenSpHD.setText("Chọn một san pham...");
            jspnSL.setValue(1);
            jbtnThemHD.setEnabled(false); 
        }
    }
      public void updateSelectedVPP(VPP vpp) {
        this.currentSelectedVpp = vpp;
        if (vpp != null) {
            jtxtTenSpHD.setText(vpp.getTenVPP());
            jspnSL.setValue(1); 
            jbtnThemHD.setEnabled(true);
        } else {
            jtxtTenSpHD.setText("Chọn một san pham...");
            jspnSL.setValue(1);
            jbtnThemHD.setEnabled(false); 
        }
    }
    public void updateReceiptTotal(boolean hasDiscount) {
        double totalAmount = 0;
        int    totalItems  = 0;

        for (int i = 0; i < tblModelHD.getRowCount(); i++) {
            String moneyStr = tblModelHD.getValueAt(i, 3).toString()
                                       .replace("$", "");
            totalAmount += Double.parseDouble(moneyStr);
            totalItems  += (Integer) tblModelHD.getValueAt(i, 1);
        }

        if (hasDiscount) {
            jtxtGG.setText("10%");
            totalAmount *= 0.9;                           // ‑10 %
        }

        jtxtTongTienHD.setText(String.format("%.0f $", totalAmount));
        jTotalPd.setText(String.valueOf(totalItems));
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
    public void clearReceiptTable() {
    // Xoá tất cả các dòng
    tblModelHD.setRowCount(0);

    // Cập nhật lại tổng số lượng & tổng tiền về 0
    jTotalPd.setText("0");
    jtxtTongTienHD.setText("0 $");

    // Nếu muốn vô hiệu hoá nút “Thêm” sau khi xoá
    updateSelectedBook(null);
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMain = new javax.swing.JPanel();
        jLeft = new javax.swing.JPanel();
        jBanner = new javax.swing.JPanel();
        jbtnQli = new javax.swing.JButton();
        jMiddle = new javax.swing.JPanel();
        jTabbedPaneBooks = new javax.swing.JTabbedPane();
        jMiddle3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtxtTenSpHD = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jspnSL = new javax.swing.JSpinner();
        jbtnThemHD = new javax.swing.JButton();
        jBottom = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtxtTenSachTK = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtxtTenTacGiaTK = new javax.swing.JTextField();
        jbtnTim = new javax.swing.JButton();
        jRight = new javax.swing.JPanel();
        jBanner2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblHD = new javax.swing.JTable();
        jUnder = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTotalPd = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtxtTongTienHD = new javax.swing.JLabel();
        jbtnTT = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtGG = new javax.swing.JLabel();
        jcbxTenKH = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jBanner.setBackground(new java.awt.Color(0, 0, 102));

        jbtnQli.setBackground(new java.awt.Color(0, 0, 102));
        jbtnQli.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jbtnQli.setForeground(new java.awt.Color(255, 255, 255));
        jbtnQli.setText("Quản lí");
        jbtnQli.setBorder(null);
        jbtnQli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnQliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jBannerLayout = new javax.swing.GroupLayout(jBanner);
        jBanner.setLayout(jBannerLayout);
        jBannerLayout.setHorizontalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnQli)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jBannerLayout.setVerticalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jbtnQli)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jMiddleLayout = new javax.swing.GroupLayout(jMiddle);
        jMiddle.setLayout(jMiddleLayout);
        jMiddleLayout.setHorizontalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneBooks)
        );
        jMiddleLayout.setVerticalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPaneBooks, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        jMiddle3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel6.setText("Tên sp:");

        jLabel7.setText("Số lượng:");

        jbtnThemHD.setText("Thêm vào hóa đơn");

        javax.swing.GroupLayout jMiddle3Layout = new javax.swing.GroupLayout(jMiddle3);
        jMiddle3.setLayout(jMiddle3Layout);
        jMiddle3Layout.setHorizontalGroup(
            jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiddle3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtTenSpHD, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jspnSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jbtnThemHD)
                .addContainerGap())
        );
        jMiddle3Layout.setVerticalGroup(
            jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMiddle3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jMiddle3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtTenSpHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jspnSL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnThemHD))
                .addContainerGap(7, Short.MAX_VALUE))
        );

        jBottom.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Tên sách:");

        jLabel9.setText("Tìm theo tên sách và tác giả");

        jLabel10.setText("Tên tác giả:");

        jbtnTim.setBackground(new java.awt.Color(204, 204, 204));
        jbtnTim.setText("Tìm");

        javax.swing.GroupLayout jBottomLayout = new javax.swing.GroupLayout(jBottom);
        jBottom.setLayout(jBottomLayout);
        jBottomLayout.setHorizontalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBottomLayout.createSequentialGroup()
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jBottomLayout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbtnTim))
                    .addGroup(jBottomLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtTenSachTK)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtTenTacGiaTK, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jBottomLayout.setVerticalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jbtnTim))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtTenSachTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jtxtTenTacGiaTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout jLeftLayout = new javax.swing.GroupLayout(jLeft);
        jLeft.setLayout(jLeftLayout);
        jLeftLayout.setHorizontalGroup(
            jLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jMiddle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jMiddle3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLeftLayout.setVerticalGroup(
            jLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLeftLayout.createSequentialGroup()
                .addComponent(jBanner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMiddle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jMiddle3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jBanner2.setBackground(new java.awt.Color(0, 0, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Hóa đơn");

        javax.swing.GroupLayout jBanner2Layout = new javax.swing.GroupLayout(jBanner2);
        jBanner2.setLayout(jBanner2Layout);
        jBanner2Layout.setHorizontalGroup(
            jBanner2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBanner2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jBanner2Layout.setVerticalGroup(
            jBanner2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBanner2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jList.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        jtblHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtblHD);

        javax.swing.GroupLayout jListLayout = new javax.swing.GroupLayout(jList);
        jList.setLayout(jListLayout);
        jListLayout.setHorizontalGroup(
            jListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jListLayout.setVerticalGroup(
            jListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jUnder.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Tổng sản phẩm:");

        jTotalPd.setText("0");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Tổng tiền:");

        jtxtTongTienHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jtxtTongTienHD.setText("$1000");

        jbtnTT.setBackground(new java.awt.Color(102, 153, 255));
        jbtnTT.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jbtnTT.setForeground(new java.awt.Color(255, 255, 255));
        jbtnTT.setText("Thanh toán");
        jbtnTT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTTActionPerformed(evt);
            }
        });

        jLabel4.setText("Khách hàng:");

        jLabel5.setText("Giảm giá:");

        jtxtGG.setText("0%");

        javax.swing.GroupLayout jUnderLayout = new javax.swing.GroupLayout(jUnder);
        jUnder.setLayout(jUnderLayout);
        jUnderLayout.setHorizontalGroup(
            jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jUnderLayout.createSequentialGroup()
                                .addComponent(jTotalPd, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jtxtGG, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jcbxTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtTongTienHD, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jUnderLayout.setVerticalGroup(
            jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jtxtGG))
                    .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTotalPd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jcbxTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jtxtTongTienHD))
                    .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jRightLayout = new javax.swing.GroupLayout(jRight);
        jRight.setLayout(jRightLayout);
        jRightLayout.setHorizontalGroup(
            jRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jBanner2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jUnder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jRightLayout.setVerticalGroup(
            jRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jRightLayout.createSequentialGroup()
                .addComponent(jBanner2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUnder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jMainLayout = new javax.swing.GroupLayout(jMain);
        jMain.setLayout(jMainLayout);
        jMainLayout.setHorizontalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jMainLayout.createSequentialGroup()
                .addComponent(jLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jMainLayout.setVerticalGroup(
            jMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jRight, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnQliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnQliActionPerformed
        new MainMenu_Manager2(this, controller, isManager).setVisible(true);
    }//GEN-LAST:event_jbtnQliActionPerformed

    private void jbtnTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnTTActionPerformed

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
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jBanner;
    private javax.swing.JPanel jBanner2;
    private javax.swing.JPanel jBottom;
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
    private javax.swing.JPanel jLeft;
    private javax.swing.JPanel jList;
    private javax.swing.JPanel jMain;
    private javax.swing.JPanel jMiddle;
    private javax.swing.JPanel jMiddle3;
    private javax.swing.JPanel jRight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPaneBooks;
    private javax.swing.JLabel jTotalPd;
    private javax.swing.JPanel jUnder;
    private javax.swing.JButton jbtnQli;
    private javax.swing.JButton jbtnTT;
    private javax.swing.JButton jbtnThemHD;
    private javax.swing.JButton jbtnTim;
    private javax.swing.JComboBox<String> jcbxTenKH;
    private javax.swing.JSpinner jspnSL;
    private javax.swing.JTable jtblHD;
    private javax.swing.JLabel jtxtGG;
    private javax.swing.JTextField jtxtTenSachTK;
    private javax.swing.JTextField jtxtTenSpHD;
    private javax.swing.JTextField jtxtTenTacGiaTK;
    private javax.swing.JLabel jtxtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
