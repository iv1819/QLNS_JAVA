/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Author_Connect;
import Model.Author;
import View.AuthorM;
import java.util.ArrayList;

/**
 *
 * @author trang
 */
public class AuthorController {
    private AuthorM view;
    private Author_Connect authorConnect;
    private MainMenuController mainMenuController;

    public AuthorController(AuthorM view,  MainMenuController mainMenuController) {
        this.view = view;
        this.authorConnect = new Author_Connect();
        this.mainMenuController = mainMenuController;
    }

    public void loadAllAuthors() {
        ArrayList<Author> authors = authorConnect.layToanBoTacGia();
        view.displayAuthors(authors);
    }

    public void addAuthor(Author newAuthor) {
        if (authorConnect.tonTaiMaTG(newAuthor.getMaTG())) {
            view.showErrorMessage("Mã tác giả đã tồn tại!");
            return;
        }
        if (authorConnect.themTacGia(newAuthor)) {
            view.showMessage("Thêm tác giả thành công!");
            loadAllAuthors();
        } else {
            view.showErrorMessage("Thêm tác giả thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void updateAuthor(Author updatedAuthor) {
        if (authorConnect.capNhatTacGia(updatedAuthor)) {
            view.showMessage("Cập nhật tác giả thành công!");
            loadAllAuthors();
        } else {
            view.showErrorMessage("Cập nhật tác giả thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteAuthor(String maTG) {
        if (authorConnect.xoaTacGia(maTG)) {
            view.showMessage("Xóa tác giả thành công!");
            loadAllAuthors();
        } else {
            view.showErrorMessage("Xóa tác giả thất bại. Vui lòng kiểm tra lại mã tác giả.");
        }
    }

    public void searchAuthors(String tenTG) {
        ArrayList<Author> searchResults = authorConnect.timTacGiaTheoTen(tenTG);
        view.displayAuthors(searchResults);
            if (searchResults.isEmpty()) {
                view.showMessage("Không tìm thấy tác giả nào phù hợp.");
            }
        }
    }

    

   

    

    

    
    
    
