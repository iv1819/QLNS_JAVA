/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Customer;
import View.CustomerM;
import Database.Customer_Connect;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author MSI GF63
 */
public class CustomerController {
    private CustomerM view;
    private Customer_Connect customerConnect;
    private MainMenuController mainMenuController;

    public CustomerController(CustomerM customerMView, MainMenuController mainMenuController) {
        this.view = customerMView;
        this.customerConnect = new Customer_Connect();
        this.mainMenuController = mainMenuController;
    }

    public void loadAllCustomers() {
        ArrayList<Customer> customers = customerConnect.layToanBoKhachHang();
        view.displayCustomers(customers);
    }

    public void addCustomer(Customer customer) {
        if (customerConnect.themKhachHang(customer)) {
            view.showMessage("Thêm khách hàng thành công!");
            loadAllCustomers();
        } else {
            view.showErrorMessage("Thêm khách hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void updateCustomer(Customer customer) {
        if (customerConnect.capNhatKhachHang(customer)) {
            view.showMessage("Cập nhật khách hàng thành công!");
            loadAllCustomers();
        } else {
            view.showErrorMessage("Cập nhật khách hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteCustomer(String maKH) {
        if (customerConnect.xoaKhachHang(maKH)) {
            view.showMessage("Xóa khách hàng thành công!");
            loadAllCustomers();
        } else {
            view.showErrorMessage("Xóa khách hàng thất bại. Vui lòng kiểm tra lại mã khách hàng.");
        }
    }

    public void searchCustomers(String tenKH) {
        ArrayList<Customer> searchResults = customerConnect.timKhachHang(tenKH);
        view.displayCustomers(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy khách hàng nào phù hợp.");
        }
    }
}
