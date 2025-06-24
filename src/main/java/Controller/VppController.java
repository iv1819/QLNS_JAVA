/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.VPP_Connect;
import Model.OD;
import Model.Order;
import Model.VPP;
import View.VppM;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        String error = validateVPP(v);
        if (error != null) {
            view.showErrorMessage(error);
            return;
        }

        if (vppConnect.themVPP(v)) {
            view.showMessage("Thêm VPP thành công!");
            loadAllVPP();
            refreshMainMenuTabs();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Thêm VPP thất bại. Vui lòng kiểm tra lại mã VPP hoặc thông tin.");
        }
    }

    public void updateVPP(VPP v) {
        String error = validateVPP(v);
        if (error != null) {
            view.showErrorMessage(error);
            return;
        }

        if (vppConnect.suaVPP(v)) {
            view.showMessage("Cập nhật VPP thành công!");
            loadAllVPP();
            refreshMainMenuTabs();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Cập nhật VPP thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }
        private String validateVPP(VPP vpp) {
            if (vpp.getMaVPP() == null || vpp.getMaVPP().trim().isEmpty()) {
                return "Mã VPP không được để trống.";
            }
            if (vpp.getTenVPP() == null || vpp.getTenVPP().trim().isEmpty()) {
                return "Tên VPP không được để trống.";
            }
            if (vpp.getSoLuong() < 0) {
                return "Số lượng không được âm.";
            }
            if (vpp.getGiaBan()< 0) {
                return "Giá không được âm.";
            }
            if ("--Chọn nhà cung cấp--".equals(vpp.getNhaCC())) {
                return "Tên nhà cung cấp không được để trống.";
            }
            return null; // hợp lệ
        }


    public void deleteVPP(String maVPP) {
        if (vppConnect.xoaVPP(maVPP)) {
            view.showMessage("Xóa vpp thành công!");
            loadAllVPP(); // Cập nhật lại JTable
            refreshMainMenuTabs();
            view.clearInputFields();
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
    
    public void importVPPFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Kiểm tra từng ô
                for (int col = 0; col <= 5; col++) {
                    Cell cell = row.getCell(col);
                    if (cell == null || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                        view.showErrorMessage("❌ Dữ liệu bị thiếu tại dòng " + (i + 1) + ", cột " + (col + 1));
                        return;
                    }
                }

                // Nếu không có cột nào null thì đọc dữ liệu
                String maVPP = row.getCell(0).getStringCellValue().trim();
                if (vppConnect.kiemTraTonTai(maVPP)) {
                    view.showErrorMessage("⚠ Mã VPP đã tồn tại ở dòng " + (i + 1) + ": " + maVPP);
                    continue; // bỏ qua dòng này, hoặc return nếu muốn dừng toàn bộ
                }
                String tenVPP = row.getCell(1).getStringCellValue().trim();
                int soLuong = (int) row.getCell(2).getNumericCellValue();
                double giaBan = row.getCell(3).getNumericCellValue();
                String nhacc = row.getCell(4).getStringCellValue().trim();
                String anh = row.getCell(5).getStringCellValue().trim();

                VPP v = new VPP(maVPP, tenVPP, soLuong, giaBan, nhacc, anh);
                vppConnect.themVPP(v);
            }
            loadAllVPP(); 
            view.showMessage("✅ Nhập VPP từ Excel thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("❌ Lỗi khi nhập VPP từ Excel: " + e.getMessage());
        }
    }

    
    public void exportVPPToExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("VPP");
        int rowNum = 0;
        List<VPP> vpps = vppConnect.layToanBoVPP();
        // Heade
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mã VPP");
        headerRow.createCell(1).setCellValue("Tên VPP");
        headerRow.createCell(2).setCellValue("Số lượng");
        headerRow.createCell(3).setCellValue("Giá");
        headerRow.createCell(4).setCellValue("Tên nhà cung cấp");
        headerRow.createCell(5).setCellValue("Ảnh");

        for (VPP v : vpps) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v.getMaVPP());
            row.createCell(1).setCellValue(v.getTenVPP());
            row.createCell(2).setCellValue(v.getSoLuong());
            row.createCell(3).setCellValue(v.getGiaBan());
            row.createCell(4).setCellValue(v.getNhaCC());
            row.createCell(5).setCellValue(v.getDuongDanAnh());
        }

        // Lưu vào C:\aadmin
        try {
            File dir = new File("C:/Users/Admin");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(dir, "vpp.xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();

            System.out.println("✅ Xuất file thành công tại: C:\\aadmin\\donhang.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi xuất file Excel.");
        }
    }
    public ArrayList<String> getAllNhaCCNames(){
        return vppConnect.getAllNhaCCNames();
    }
}
