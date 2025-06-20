/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Book;
import Database.Book_Connect;
import Model.Category;
import View.MainMenu; // Import MainMenu (View)
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainMenuController {

    private MainMenu view; 
    private Book_Connect bookConnect; 
    private Book currentSelectedBookForReceipt;

    public MainMenuController(MainMenu mainMenuView) {
        this.view = mainMenuView;
        this.bookConnect = new Book_Connect();
    }
    public void onBookItemSelected(Book book) {
        this.currentSelectedBookForReceipt = book;
        view.updateSelectedBook(book);
    }
    public void loadAndDisplayBooksByCategories() {
        ArrayList<Category> categories = bookConnect.layTatCaDanhMucInfo();
        LinkedHashMap<String, ArrayList<Book>> categorizedBooks = new LinkedHashMap<>();

        ArrayList<Book> allBooks = bookConnect.layToanBoSach();
        categorizedBooks.put("Tất cả Sách", allBooks);

        // Thêm các tab theo từng danh mục
        for (Category dm : categories) {
            ArrayList<Book> booksInCategory = bookConnect.laySachTheoMaDanhMuc(dm.getMaDanhMuc());
            categorizedBooks.put(dm.getTenDanhMuc(), booksInCategory); // Sử dụng TenDanhMuc làm title
        }

        view.populateCategoryTabs(categorizedBooks); // Gọi phương thức trong View để cập nhật UI
    }
      public void onAddReceiptItemClicked(int quantity) {
        if (currentSelectedBookForReceipt == null) {
            view.showMessage("Vui lòng chọn một cuốn sách trước.");
            return;
        }

        if (quantity <= 0) {
            view.showMessage("Số lượng phải lớn hơn 0.");
            return;
        }

        if (quantity > currentSelectedBookForReceipt.getSoLuong()) {
            view.showMessage("Số lượng yêu cầu vượt quá số lượng tồn kho (" + currentSelectedBookForReceipt.getSoLuong() + ").");
            return;
        }

        String tenSach = currentSelectedBookForReceipt.getTenSach();
        double donGia = currentSelectedBookForReceipt.getGiaBan();
        double tongGia = quantity * donGia;

        Object[] rowData = {tenSach, quantity, String.format("%.0f VNĐ", donGia), String.format("%.0f VNĐ", tongGia)};
        view.addReceiptItem(rowData);

        // Cập nhật tổng tiền và tổng số lượng
        updateReceiptTotals();
        
        // Reset lựa chọn sách sau khi thêm vào hóa đơn
        currentSelectedBookForReceipt = null;
        view.updateSelectedBook(null); // Xóa thông tin sách đang chọn trên UI
    }


    private void updateReceiptTotals() {
        view.updateReceiptTotalAmount(calculateTotalAmountFromView());
        view.updateReceiptTotalItems(calculateTotalItemsFromView());
    }

    private double calculateTotalAmountFromView() {
        return 0;
    }

    private int calculateTotalItemsFromView() {
        return 0;
    }

    public void onBookDataChanged() {
        System.out.println("DEBUG (MainMenuPresenter): Nhận được thông báo dữ liệu sách đã thay đổi. Đang tải lại tab.");
        loadAndDisplayBooksByCategories(); // Tải lại dữ liệu khi có thay đổi
    }

}

