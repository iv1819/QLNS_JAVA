/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.VPP_Connect;
import Model.VPP;
import View.VppM;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Admin
 */
public class VppController {
    private VppM view; // Tham chiếu đến View
    private VPP_Connect vppConnect; // Tham chiếu đến lớp kết nối CSDL
    private JLabel imagePreviewLabel; // JLabel để hiển thị ảnh preview
    private String selectedImagePath; // Để lưu đường dẫn ảnh đã chọn từ JFileChooser
 private MainMenuController mainMenuController;
       public VppController(VppM bookMView, JLabel imagePreviewLabel, MainMenuController mainMenuController) {
        this.view = bookMView;
        this.imagePreviewLabel = imagePreviewLabel;
        this.vppConnect = new VPP_Connect();
        this.selectedImagePath = "";
        this.mainMenuController = mainMenuController; // Initialize MainMenuController reference
    }
    private void refreshMainMenuTabs() {
        if (mainMenuController != null) {
            System.out.println("DEBUG (BookController): Đang gọi refresh các tab trên MainMenu.");
            mainMenuController.loadAndDisplayBooksByCategories();
        } else {
            System.out.println("DEBUG (BookController): MainMenuController là null, không thể refresh tab.");
        }
    }
    public String getSelectedImagePath() {
        return selectedImagePath;
    }

    /**
     * Xử lý logic chọn ảnh từ hệ thống file.
     * Mở JFileChooser, cho phép người dùng chọn file ảnh,
     * sau đó hiển thị ảnh preview và đường dẫn ảnh.
     */
    public void handleImageSelection() {
        JFileChooser fileChooser = new JFileChooser();
        // Thiết lập bộ lọc chỉ cho phép chọn các file ảnh
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Tệp ảnh (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Hiển thị hộp thoại Open File
        int returnValue = fileChooser.showOpenDialog(view); // Sử dụng view làm parent

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedImagePath = selectedFile.getAbsolutePath(); // Lưu đường dẫn tuyệt đối của file

            updateImagePreview(selectedImagePath); // Cập nhật ảnh trong JLabel
        }
    }
    
    // Phương thức để cập nhật ảnh preview trên JLabel
    public void updateImagePreview(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                File file = new File(imagePath);
                if (file.exists()) {
                    BufferedImage originalImage = ImageIO.read(file);
                    Image scaledImage = originalImage.getScaledInstance(
                            78, 100, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(scaledImage));
                    imagePreviewLabel.setText(""); // Xóa text nếu có ảnh
                } else {
                    imagePreviewLabel.setIcon(null);
                    imagePreviewLabel.setText("Không tìm thấy ảnh");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                imagePreviewLabel.setIcon(null); // Xóa ảnh nếu có lỗi
                imagePreviewLabel.setText("Lỗi tải ảnh");
                view.showErrorMessage("Không thể tải ảnh: " + ex.getMessage());
            }
        } else {
            imagePreviewLabel.setIcon(null);
            imagePreviewLabel.setText("Ảnh VPP"); // Đặt lại text mặc định
        }
    }

    // Các phương thức xử lý logic nghiệp vụ
    
    public void loadAllVPP() {
        ArrayList<VPP> vpps = vppConnect.layToanBoVPP();
        view.displayVPP(vpps);
    }

    public void addVPP(VPP v) {
        if (vppConnect.themVPP(v)) {
            view.showMessage("Thêm vpp thành công!");
            loadAllVPP(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Thêm vpp thất bại. Vui lòng kiểm tra lại mã vpp hoặc thông tin.");
        }
    }

    public void updateVPP(VPP v) {
        if (vppConnect.suaVPP(v)) {
            view.showMessage("Cập nhật vpp thành công!");
            loadAllVPP(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Cập nhật vpp thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteVPP(String maVPP) {
        if (vppConnect.xoaVPP(maVPP)) {
            view.showMessage("Xóa vpp thành công!");
            loadAllVPP(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Xóa vpp thất bại. Vui lòng kiểm tra lại mã vpp.");
        }
    }

    public void searchVPP(String tenVPP, String tenNCC) {
        ArrayList<VPP> searchResults;
        if (!tenVPP.isEmpty() && !tenNCC.isEmpty()) {
            // Giả sử có phương thức tìm kiếm theo cả tên sách và tác giả
            searchResults = vppConnect.layVppTheoTenVppvaTenNCC(tenVPP, tenNCC); 
        } else if (!tenVPP.isEmpty()) {
            searchResults = vppConnect.layVppTheoTen(tenVPP);
        } else if (!tenNCC.isEmpty()) {
            searchResults = vppConnect.layVppTheoTenNCC(tenNCC); // Cần thêm phương thức này vào Book_Connect
        } else {
            loadAllVPP(); // Nếu không nhập gì thì hiển thị toàn bộ
            return;
        }
        view.displayVPP(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy vpp nào phù hợp.");
        }
    }
    
    public ArrayList<String> getAllNhaCCNames(){
        return vppConnect.getAllNhaCCNames();
    }
}
