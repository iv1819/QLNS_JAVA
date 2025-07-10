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
import java.util.List;
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import java.io.FileOutputStream;

/**
 *
 * @author MSI GF63
 */
public class CustomerController {
    private CustomerM view;
    private Customer_Connect customerConnect;
    private MainMenuController mainMenuController;

    public CustomerController(CustomerM customerMView) {
        this.view = customerMView;
        this.customerConnect = new Customer_Connect();
    }
    
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
        // Kiểm tra mã khách hàng
        if (customer.getMaKH() == null || customer.getMaKH().trim().isEmpty()) {
            view.showErrorMessage("Mã khách hàng không được để trống!");
            return;
        }
        
        // Kiểm tra tên khách hàng
        if (customer.getTenKH() == null || customer.getTenKH().trim().isEmpty()) {
            view.showErrorMessage("Tên khách hàng không được để trống!");
            return;
        }
        
        // Kiểm tra số điện thoại bằng phương thức riêng
        if (!isValidPhoneNumber(customer.getSdt())) {
            return; // Thông báo lỗi đã được hiển thị trong isValidPhoneNumber
        }
        
        // Kiểm tra số điện thoại trùng lặp
        if (customerConnect.tonTaiSdt(customer.getSdt())) {
            view.showErrorMessage("Số điện thoại đã tồn tại! Vui lòng nhập số khác.");
            return;
        }
        
        if (customerConnect.themKhachHang(customer)) {
            view.showMessage("Thêm khách hàng thành công!");
            loadAllCustomers();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Thêm khách hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void updateCustomer(Customer customer) {
        // Kiểm tra tên khách hàng
        if (customer.getTenKH() == null || customer.getTenKH().trim().isEmpty()) {
            view.showErrorMessage("Tên khách hàng không được để trống!");
            return;
        }
        
        // Kiểm tra số điện thoại bằng phương thức riêng
        if (!isValidPhoneNumber(customer.getSdt())) {
            return; // Thông báo lỗi đã được hiển thị trong isValidPhoneNumber
        }
        
        // Kiểm tra số điện thoại trùng lặp (trừ chính khách hàng đang sửa)
        if (customerConnect.tonTaiSdtKhac(customer.getSdt(), customer.getMaKH())) {
            view.showErrorMessage("Số điện thoại đã tồn tại! Vui lòng nhập số khác.");
            return;
        }
        
        if (customerConnect.capNhatKhachHang(customer)) {
            view.showMessage("Cập nhật khách hàng thành công!");
            loadAllCustomers();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Cập nhật khách hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteCustomer(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) {
            view.showErrorMessage("Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        int confirm = view.showConfirmDialog("Bạn có chắc chắn muốn xóa khách hàng này?");
        if (confirm == 0) { // 0 = YES
            if (customerConnect.xoaKhachHang(maKH)) {
                view.showMessage("Xóa khách hàng thành công!");
                loadAllCustomers();
                view.clearInputFields();
            } else {
                view.showErrorMessage("Xóa khách hàng thất bại. Vui lòng kiểm tra lại.");
            }
        }
    }

    public void searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadAllCustomers();
            return;
        }
        
        ArrayList<Customer> searchResults = customerConnect.timKhachHang(keyword.trim());
        view.displayCustomers(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy khách hàng nào phù hợp.");
        }
    }
    
    /**
     * Kiểm tra định dạng số điện thoại
     * @param sdt số điện thoại cần kiểm tra
     * @return true nếu hợp lệ, false nếu không hợp lệ
     */
    private boolean isValidPhoneNumber(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            view.showErrorMessage("Số điện thoại không được để trống!");
            return false;
        }
        
        String phone = sdt.trim();
        
        // Kiểm tra độ dài
        if (phone.length() != 10) {
            view.showErrorMessage("Số điện thoại phải có đúng 10 chữ số!");
            return false;
        }
        
        // Kiểm tra chỉ chứa số
        if (!phone.matches("\\d+")) {
            view.showErrorMessage("Số điện thoại chỉ được chứa các chữ số từ 0-9!");
            return false;
        }
        
        // Kiểm tra bắt đầu bằng số hợp lệ (0)
        if (!phone.startsWith("0")) {
            view.showErrorMessage("Số điện thoại phải bắt đầu bằng số 0!");
            return false;
        }
        
        return true;
    }

    public String generateCustomerCode() {
        return customerConnect.taoMaKhachHangTuDong();
    }
}
