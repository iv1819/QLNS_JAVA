/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Controller.MainMenuController;
import Model.Book;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;

/**
 *
 * @author Admin
 */
public class BookItemPanel extends javax.swing.JPanel {
private Book book;
    private MainMenuController controller;
    /**
     * Creates new form BookItemPanel
     * @param book
     * @param controller
     */
    public BookItemPanel(Book book, MainMenuController controller) {
         this.book = book;
        this.controller = controller;
        initComponents();
        setPreferredSize(new Dimension(96, 146));
        jtxtPriceBI.setPreferredSize(new Dimension(70,25));
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        setBookData(book); // Đổi tên phương thức để tránh nhầm lẫn với setBook trong Model

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller != null) {
                    // Thông báo cho Presenter khi sách được click
                    controller.onBookItemSelected(BookItemPanel.this.book);
                    System.out.println("DEBUG (BookItemPanel): Đã click vào sách: " + BookItemPanel.this.book.getTenSach());
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // Hiệu ứng khi di chuột vào
                setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Hiệu ứng khi di chuột ra
                setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Viền xám nhạt
            }
        });
    }
     public void setBookData(Book book) {

        if (book.getDuongDanAnh() != null && !book.getDuongDanAnh().isEmpty()) {
            try {
                File file = new File(book.getDuongDanAnh());
                if (file.exists()) {
                    BufferedImage originalImage = ImageIO.read(file);
                    Image scaledImage = originalImage.getScaledInstance(
                    58, 70, Image.SCALE_SMOOTH);
                    jtxtAnhBI.setIcon(new ImageIcon(scaledImage));
                    jtxtAnhBI.setText(""); // Xóa text nếu có ảnh
                } else {
                    jtxtAnhBI.setIcon(null);
                    jtxtAnhBI.setText("Không tìm thấy ảnh");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                jtxtAnhBI.setIcon(null); // Xóa ảnh nếu có lỗi
                jtxtAnhBI.setText("Lỗi tải ảnh");
            }
        } else {
            jtxtAnhBI.setIcon(null);
            jtxtAnhBI.setText("Ảnh sách"); // Đặt lại text mặc định
        }
        jtxtNameBI.setText(truncateText(book.getTenSach(), 15)); 
        jtxtPriceBI.setText(String.format("%.0f $", book.getGiaBan())); 
    }
 private String truncateText(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "..."; // Cắt và thêm "..."
        }
        return text;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtxtAnhBI = new javax.swing.JLabel();
        jtxtNameBI = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jtxtPriceBI = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(100, 150));

        jtxtAnhBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtAnhBI.setText("Anh");
        jtxtAnhBI.setPreferredSize(new java.awt.Dimension(58, 70));

        jtxtNameBI.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        jtxtNameBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtNameBI.setText("Name");
        jtxtNameBI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));

        jtxtPriceBI.setBackground(new java.awt.Color(51, 153, 255));
        jtxtPriceBI.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jtxtPriceBI.setForeground(new java.awt.Color(255, 255, 255));
        jtxtPriceBI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jtxtPriceBI.setText("$");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 96, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jtxtPriceBI, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jtxtPriceBI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtxtNameBI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jtxtAnhBI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jtxtAnhBI, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jtxtNameBI, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jtxtAnhBI;
    private javax.swing.JLabel jtxtNameBI;
    private javax.swing.JLabel jtxtPriceBI;
    // End of variables declaration//GEN-END:variables
}
