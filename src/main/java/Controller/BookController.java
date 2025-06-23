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
        if (bookConnect.addBook(book)) {
            view.showMessage("Thêm sách thành công!");
            loadAllBooks(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Thêm sách thất bại. Vui lòng kiểm tra lại mã sách hoặc thông tin.");
        }
    }

    public void updateBook(Book book) {
        if (bookConnect.updateBook(book)) {
            view.showMessage("Cập nhật sách thành công!");
            loadAllBooks(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Cập nhật sách thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteBook(String maSach) {
        if (bookConnect.deleteBook(maSach)) {
            view.showMessage("Xóa sách thành công!");
            loadAllBooks(); // Cập nhật lại JTable
            refreshMainMenuTabs();
        } else {
            view.showErrorMessage("Xóa sách thất bại. Vui lòng kiểm tra lại mã sách.");
        }
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
