/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Account_Connect;
import Model.Account;
import Model.ChucVu;
import View.AccountM;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author trang
 */
public class AccountController {
    private AccountM view;
    private Account_Connect accountConnect;
    private MainMenuController mainMenuController;

    public AccountController(AccountM view, MainMenuController mainMenuController) {
        this.view = view;
        this.accountConnect = new Account_Connect();
        this.mainMenuController = mainMenuController;
    }
    
    public void loadAllAccount(){
        ArrayList<Account> account = accountConnect.layToanBoTaiKhoan();
        view.displayAccount(account);
    }

    public void updateAccount(Account updateAccount) {
        if (accountConnect.capNhatTaiKhoan(updateAccount)) {
            view.showMessage("Cập nhật tài khoản thành công!");
            loadAllAccount();
        } else {
            view.showErrorMessage("Cập nhật tài khoản thất bại! Vui lòng kiểm tra lại tài khoản");
        }
    }

    public void deleteAccount(String taiKhoan) {
        if (accountConnect.xoaTaiKhoan(taiKhoan)) {
            view.showMessage("Xóa tài khoản thành công!");
            loadAllAccount();
        } else {
            view.showErrorMessage("Xóa tài khoản thất bại. Vui lòng kiểm tra lại tài khoản.");
        }
    }

    public void searchAccount(String taiKhoan) {
        ArrayList<Account> searchResults = accountConnect.timTaiKhoan(taiKhoan);
        view.displayAccount(searchResults);
            if (searchResults.isEmpty()) {
                view.showMessage("Không tìm thấy tài khoản nào phù hợp.");
            }
        }

    public ArrayList<String> getAllMaCV() {
        return accountConnect.layDanhSachChucVu();
    }

    public List<String> getAllTrangThai() {
        return accountConnect.getAllTrangThai();
    }

    public String getMaCVByTenCV(String tenCV) {
    return accountConnect.getMaCVByTenCV(tenCV);
}

    public boolean themTaiKhoan(Account acc) {
        return accountConnect.themTaiKhoan(acc);
    }

    
}
