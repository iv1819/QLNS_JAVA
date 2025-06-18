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

    private MainMenu mainMenuView; 
    private Book_Connect bookConnect; 

    public MainMenuController(MainMenu mainMenuView) {
        this.mainMenuView = mainMenuView;
        this.bookConnect = new Book_Connect();
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

        mainMenuView.populateCategoryTabs(categorizedBooks); // Gọi phương thức trong View để cập nhật UI
    }

}

