/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Controller.BookController;
import Controller.MainMenuController;
import Controller.VppController;
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
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;
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
     * @param tenNV
     */
    public MainMenu(boolean isManager, String tenNV) {
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
        jtxtTenNV.setText(tenNV);
        jtxtTenNV.setEditable(false);
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
                showErrorMessage("Hoá đơn đang trống – không thể thanh toán.");
                return;
            }

            if(jtxtSdt.getText().matches("\\d+")){
                showErrorMessage("Tên khách hàng đang là số");
                return;
            }
            // 2) Lấy tên KH (có thể để trống)
            String sel = jtxtSdt.getText();
            String tenKH = (sel == null ||
                            sel.trim().isEmpty())
                          ? null   // <‑‑ truyền null
                          : sel.trim();

            // 3) Gọi Controller
            controller.onCheckoutClicked(tenKH, tenNV);
        });
        jbtnDX.addActionListener(e -> {
            Login lg = new Login();
                lg.setVisible(true);
                this.dispose();
        });
        jbtnTimKH.addActionListener(e -> {
            jtxtSdt.setText(controller.onCustomerSelected(jtxtSdt.getText()));
        });
        jbtnTim.addActionListener(e -> {
            controller.searchSP(jtxtTK.getText());
        });
        clearReceiptTable();
    }
    public DefaultTableModel getReceiptTableModel() {
    return tblModelHD;
}

    public double LayTongTien(){
        return Double.parseDouble(jtxtTongTienHD.getText().replace("$", "").replace(",", "").trim());
    }
     /** Hiển thị các tab Sách + 1 tab VPP */
    public void populateCategoryTabs(LinkedHashMap<String, ArrayList<Book>> booksByCat,
                                 ArrayList<VPP> allVpp) {

            jMTab.removeAll();

            /* ==== 1. Tab cho sách (như cũ) ==== */
            for (Map.Entry<String, ArrayList<Book>> e : booksByCat.entrySet()) {
                addBookTab(e.getKey(), e.getValue());
            }

            /* ==== 2. Tab cho VPP ==== */
            addVppTab("Văn phòng phẩm", allVpp);
        }

        /* ------------------ helpers ------------------ */

       // Inside addBookTab
private void addBookTab(String title, ArrayList<Book> books) {
    // This panel will hold the BookItemPanels in a grid
    JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
    gridPanel.setBorder(new EmptyBorder(5, 20, 5, 20));
    if (books.isEmpty()) {
        gridPanel.add(new JLabel("Không có sách trong danh mục này."));
    } else {
        for (Book b : books) {
            if(b.getSoLuong()<=0){
                continue;
            }
            BookItemPanel p = new BookItemPanel(b, controller);
            p.setBookData(b);
            gridPanel.add(p);
        }
    }

    // Create a wrapper panel to contain the gridPanel.
    // This helps the JScrollPane understand the preferred width.
    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.add(gridPanel, BorderLayout.NORTH); // Add to NORTH to prevent stretching vertically

    // Use the wrapperPanel in the JScrollPane
    jMTab.addTab(title, new JScrollPane(wrapperPanel));
}

// Inside addVppTab (apply the same logic)
private void addVppTab(String title, ArrayList<VPP> vpps) {
    JPanel gridPanel = new JPanel(new GridLayout(0, 4, 20, 20));
    gridPanel.setBorder(new EmptyBorder(5, 20, 5, 20));

    if (vpps.isEmpty()) {
        gridPanel.add(new JLabel("Không có VPP."));
    } else {
        for (VPP v : vpps) {
            if(v.getSoLuong()<=0){
                continue;
            }
            VppItemPanel p = new VppItemPanel(v, controller);
            p.setVPPData(v);
            gridPanel.add(p);
        }
    }

    JPanel wrapperPanel = new JPanel(new BorderLayout());
    wrapperPanel.add(gridPanel, BorderLayout.NORTH);

    jMTab.addTab(title, new JScrollPane(wrapperPanel));
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
        else{
            jtxtGG.setText("0%");
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
        jbtnDX = new javax.swing.JButton();
        jMiddle = new javax.swing.JPanel();
        jMTab = new View.MaterialTabbed();
        jMiddle3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jtxtTenSpHD = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jspnSL = new javax.swing.JSpinner();
        jbtnThemHD = new javax.swing.JButton();
        jBottom = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jtxtTim = new javax.swing.JLabel();
        jtxtTK = new javax.swing.JTextField();
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
        jLabel11 = new javax.swing.JLabel();
        jtxtTenNV = new javax.swing.JTextField();
        jtxtSdt = new javax.swing.JTextField();
        jbtnTimKH = new javax.swing.JButton();

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

        jbtnDX.setBackground(new java.awt.Color(254, 255, 255));
        jbtnDX.setText("Đăng xuất");
        jbtnDX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDXActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jBannerLayout = new javax.swing.GroupLayout(jBanner);
        jBanner.setLayout(jBannerLayout);
        jBannerLayout.setHorizontalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jbtnQli)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnDX)
                .addContainerGap())
        );
        jBannerLayout.setVerticalGroup(
            jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBannerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jBannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnDX)
                    .addComponent(jbtnQli))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jMiddleLayout = new javax.swing.GroupLayout(jMiddle);
        jMiddle.setLayout(jMiddleLayout);
        jMiddleLayout.setHorizontalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMTab, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jMiddleLayout.setVerticalGroup(
            jMiddleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jMTab, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
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

        jLabel8.setText("Tên sản phẩm:");

        jtxtTim.setText("Tìm theo tên sản phẩm");

        jbtnTim.setBackground(new java.awt.Color(204, 204, 204));
        jbtnTim.setText("Tìm");
        jbtnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnTimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jBottomLayout = new javax.swing.GroupLayout(jBottom);
        jBottom.setLayout(jBottomLayout);
        jBottomLayout.setHorizontalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jBottomLayout.createSequentialGroup()
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jBottomLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel8)
                        .addGap(39, 39, 39)
                        .addComponent(jtxtTK))
                    .addGroup(jBottomLayout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(jtxtTim, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnTim)
                .addGap(85, 85, 85))
        );
        jBottomLayout.setVerticalGroup(
            jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtxtTim)
                .addGap(18, 18, 18)
                .addGroup(jBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTim))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        jLabel11.setText("Nhân viên:");

        jbtnTimKH.setBackground(new java.awt.Color(204, 204, 204));
        jbtnTimKH.setText("Tìm");

        javax.swing.GroupLayout jUnderLayout = new javax.swing.GroupLayout(jUnder);
        jUnder.setLayout(jUnderLayout);
        jUnderLayout.setHorizontalGroup(
            jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jUnderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtxtTongTienHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jUnderLayout.createSequentialGroup()
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jUnderLayout.createSequentialGroup()
                                .addComponent(jtxtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jUnderLayout.createSequentialGroup()
                                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jtxtSdt, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jUnderLayout.createSequentialGroup()
                                        .addComponent(jTotalPd, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jUnderLayout.createSequentialGroup()
                                        .addComponent(jtxtGG, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jUnderLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jbtnTimKH)))))))
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
                    .addComponent(jtxtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnTimKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtxtTenNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jUnderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnTT, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtTongTienHD))
                .addContainerGap())
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
                .addComponent(jList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jUnder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        new MainMenu_Manager2(this,controller, isManager).setVisible(true);
    }//GEN-LAST:event_jbtnQliActionPerformed

    private void jbtnTTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnTTActionPerformed

    private void jbtnDXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDXActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnDXActionPerformed

    private void jbtnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnTimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbtnTimActionPerformed

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
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jLeft;
    private javax.swing.JPanel jList;
    private View.MaterialTabbed jMTab;
    private javax.swing.JPanel jMain;
    private javax.swing.JPanel jMiddle;
    private javax.swing.JPanel jMiddle3;
    private javax.swing.JPanel jRight;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel jTotalPd;
    private javax.swing.JPanel jUnder;
    private javax.swing.JButton jbtnDX;
    private javax.swing.JButton jbtnQli;
    private javax.swing.JButton jbtnTT;
    private javax.swing.JButton jbtnThemHD;
    private javax.swing.JButton jbtnTim;
    private javax.swing.JButton jbtnTimKH;
    private javax.swing.JSpinner jspnSL;
    private javax.swing.JTable jtblHD;
    private javax.swing.JLabel jtxtGG;
    private javax.swing.JTextField jtxtSdt;
    private javax.swing.JTextField jtxtTK;
    private javax.swing.JTextField jtxtTenNV;
    private javax.swing.JTextField jtxtTenSpHD;
    private javax.swing.JLabel jtxtTim;
    private javax.swing.JLabel jtxtTongTienHD;
    // End of variables declaration//GEN-END:variables
}
