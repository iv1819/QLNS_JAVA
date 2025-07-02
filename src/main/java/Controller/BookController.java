/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author Admin
 */
import View.BookM; // Import BookM (View)
import Model.Book; // Import Book (Model)
import Database.Book_Connect;
import Model.VPP;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class BookController { 
    private BookM view; // Tham chiếu đến View
    private Book_Connect bookConnect; // Tham chiếu đến lớp kết nối CSDL
    private JLabel imagePreviewLabel; // JLabel để hiển thị ảnh preview
    private String selectedImagePath; // Để lưu đường dẫn ảnh đã chọn từ JFileChooser
 private MainMenuController mainMenuController;
       public BookController(BookM bookMView, JLabel imagePreviewLabel, MainMenuController mainMenuController) {
        this.view = bookMView;
        this.imagePreviewLabel = imagePreviewLabel;
        this.bookConnect = new Book_Connect();
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
                            imagePreviewLabel.getWidth(), imagePreviewLabel.getHeight(), Image.SCALE_SMOOTH);
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
            imagePreviewLabel.setText("Ảnh sách"); // Đặt lại text mặc định
        }
    }

    // Các phương thức xử lý logic nghiệp vụ
    
    public void loadAllBooks() {
        ArrayList<Book> books = bookConnect.layToanBoSach();
        view.displayBooks(books);
    }
    

    public void addBook(Book book) {
         String validationMsg = validateBook(book);
        if (validationMsg != null) {
            view.showErrorMessage("Lỗi: " + validationMsg);
            return;
        }

        if (bookConnect.addBook(book)) {
            loadAllBooks();
            refreshMainMenuTabs();
            view.showMessage("Thêm sách thành công!");
            
            view.clearInputFields();
        } else {
            view.showErrorMessage("Thêm sách thất bại. Có thể do mã sách bị trùng hoặc lỗi hệ thống.");
        }
    }

    public void updateBook(Book book) {
          String validationMsg = validateBook(book);
            if (validationMsg != null) {
                view.showErrorMessage("Lỗi: " + validationMsg);
                return;
            }

            if (bookConnect.updateBook(book)) {
                loadAllBooks();
                refreshMainMenuTabs();
                view.showMessage("Cập nhật sách thành công!");
                
                
                view.clearInputFields();
            } else {
                view.showErrorMessage("Cập nhật sách thất bại. Vui lòng kiểm tra lại thông tin.");
            }
    }

    public void deleteBook(String maSach) {
        if (bookConnect.deleteBook(maSach)) {
            loadAllBooks();
            refreshMainMenuTabs();
            view.showMessage("Xóa sách thành công!");
            view.clearInputFields();
        } else {
            view.showErrorMessage("Xóa sách thất bại. Vui lòng kiểm tra lại mã sách.");
        }
    }
        private String validateBook(Book book) {
            if (book.getMaSach() == null || book.getMaSach().trim().isEmpty()) {
                return "Mã sách không được để trống.";
            }

            if(!book.getMaSach().startsWith("s_")){
                return "Mã sách phải bắt đầu bằng s_";
            }
            if (book.getTenSach() == null || book.getTenSach().trim().isEmpty()) {
                return "Tên sách không được để trống.";
            }

            if (book.getSoLuong() < 0) {
                return "Số lượng không hợp lệ (phải >= 0).";
            }

            if (book.getGiaBan()< 0) {
                return "Giá sách không hợp lệ (phải >= 0).";
            }

            if ("--Chọn tác giả--".equals(book.getTacGia())) {
                return "Tên tác giả không được để trống.";
            }
            if ("--Chọn nhà xuất bản--".equals(book.getNhaXB())) {
                return "Tên nxb không được để trống.";
            }
            if ("--Chọn danh mục--".equals(book.getNhaXB())) {
                return "Tên danh mục không được để trống.";
            }
            if (book.getNamXB()< 1900 || book.getNamXB()> 2100) {
                return "Năm xuất bản không hợp lệ.";
            }

            // Có thể thêm các kiểm tra khác như: định dạng ảnh, tên NXB, danh mục...
            return null; // Nếu hợp lệ
        }

    public void searchBooks(String tenSach, String tenTacGia) {
        ArrayList<Book> searchResults;
        if (!tenSach.isEmpty() && !tenTacGia.isEmpty()) {
            // Giả sử có phương thức tìm kiếm theo cả tên sách và tác giả
            searchResults = bookConnect.laySachTheoTenSachVaTenTacGia(tenSach, tenTacGia); 
        } else if (!tenSach.isEmpty()) {
            searchResults = bookConnect.laySachTheoMaTen(tenSach);
        } else if (!tenTacGia.isEmpty()) {
            searchResults = bookConnect.laySachTheoTenTacGia(tenTacGia); // Cần thêm phương thức này vào Book_Connect
        } else {
            loadAllBooks(); // Nếu không nhập gì thì hiển thị toàn bộ
            return;
        }
        view.displayBooks(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy sách nào phù hợp.");
        }
    }
    public void importBookFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Kiểm tra từng ô
                for (int col = 0; col <= 8; col++) {
                    Cell cell = row.getCell(col);
                    if (cell == null || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                        view.showErrorMessage("❌ Dữ liệu bị thiếu tại dòng " + (i + 1) + ", cột " + (col + 1));
                        return;
                    }
                }

                // Nếu không có cột nào null thì đọc dữ liệu
                String maSach = row.getCell(0).getStringCellValue().trim();
                if (bookConnect.kiemTraTonTai(maSach)) {
                    view.showErrorMessage("⚠ Mã Sach đã tồn tại ở dòng " + (i + 1) + ": " + maSach);
                    continue; // bỏ qua dòng này, hoặc return nếu muốn dừng toàn bộ
                }
                String tenSach = row.getCell(1).getStringCellValue().trim();
                int soLuong = (int) row.getCell(2).getNumericCellValue();
                double giaBan = row.getCell(3).getNumericCellValue();
                String tacgia = row.getCell(4).getStringCellValue().trim();
                String nhaxb = row.getCell(5).getStringCellValue().trim();

                String anh = row.getCell(6).getStringCellValue().trim();
                int namxb = (int) row.getCell(7).getNumericCellValue();
                String danhmuc = row.getCell(8).getStringCellValue().trim();
                

                Book b= new Book(maSach, tenSach, soLuong, giaBan, tacgia, nhaxb, anh, namxb, danhmuc);
                bookConnect.addBook(b);
            }
            loadAllBooks(); 
            view.showMessage("Nhập Sach từ Excel thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("Lỗi khi nhập Sach từ Excel: " + e.getMessage());
        }
    }

    
    public void exportBookToExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Book");
        int rowNum = 0;
        List<Book> books = bookConnect.layToanBoSach();
        // Heade
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mã Sách");
        headerRow.createCell(1).setCellValue("Tên Sách");
        headerRow.createCell(2).setCellValue("Số lượng");
        headerRow.createCell(3).setCellValue("Giá");
        headerRow.createCell(4).setCellValue("Tên tác giả");
        headerRow.createCell(5).setCellValue("Tên nhà xuất bản");
        headerRow.createCell(6).setCellValue("Ảnh");
        headerRow.createCell(7).setCellValue("Năm xuất bản");
        headerRow.createCell(8).setCellValue("Danh mục");

        for (Book b : books) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(b.getMaSach());
            row.createCell(1).setCellValue(b.getTenSach());
            row.createCell(2).setCellValue(b.getSoLuong());
            row.createCell(3).setCellValue(b.getGiaBan());
            row.createCell(4).setCellValue(b.getTacGia());
            row.createCell(5).setCellValue(b.getNhaXB());
            row.createCell(6).setCellValue(b.getDuongDanAnh());
            row.createCell(7).setCellValue(b.getNamXB());
            row.createCell(8).setCellValue(b.getDanhMuc());
        }

        // Lưu vào C:\aadmin
        try {
            File dir = new File("C:/Users/Admin");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(dir, "sach.xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();

            System.out.println("Xuất file thành công tại: C:\\aadmin\\donhang.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi xuất file Excel.");
        }
    }
    
    public ArrayList<String> getAllNhaXBNames(){
        return bookConnect.getAllNhaXBNames();
    }
     public ArrayList<String> getAllTacGiaNames(){
        return bookConnect.getAllTacGiaNames();
    }
     public ArrayList<String> getAllDanhMuc(){
        return bookConnect.getAllDanhMuc();
    }
}
