/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Employee;
import View.EmployeeM;
import Database.Employee_Connect;
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
import java.util.Date;

/**
 *
 * @author MSI GF63
 */
public class EmployeeController {
    private EmployeeM view;
    private Employee_Connect employeeConnect;
    private MainMenuController mainMenuController;

    public EmployeeController(EmployeeM employeeMView, MainMenuController mainMenuController) {
        this.view = employeeMView;
        this.employeeConnect = new Employee_Connect();
        this.mainMenuController = mainMenuController;
    }

    public void loadAllEmployees() {
        ArrayList<Employee> employees = employeeConnect.layToanBoNhanVien();
        view.displayEmployees(employees);
    }

    public void addEmployee(Employee employee) {
        if (employeeConnect.tonTaiMaNV(employee.getMaNV())) {
            view.showErrorMessage("Mã nhân viên đã tồn tại!");
            return;
        }
        if (employeeConnect.tonTaiSdt(employee.getSdt())) {
            view.showErrorMessage("Số điện thoại đã tồn tại!");
            return;
        }
        if (employeeConnect.themNhanVien(employee)) {
            view.showMessage("Thêm nhân viên thành công!");
            loadAllEmployees();
        } else {
            view.showErrorMessage("Thêm nhân viên thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void updateEmployee(Employee employee) {
        if (employeeConnect.capNhatNhanVien(employee)) {
            view.showMessage("Cập nhật nhân viên thành công!");
            loadAllEmployees();
        } else {
            view.showErrorMessage("Cập nhật nhân viên thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteEmployee(String maNV) {
        if (employeeConnect.xoaNhanVien(maNV)) {
            view.showMessage("Xóa nhân viên thành công!");
            loadAllEmployees();
        } else {
            view.showErrorMessage("Xóa nhân viên thất bại. Vui lòng kiểm tra lại mã nhân viên.");
        }
    }

    public void searchEmployees(String keyword) {
        ArrayList<Employee> searchResults = employeeConnect.searchEmployees(keyword);
        view.displayEmployees(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy nhân viên nào phù hợp.");
        }
    }
}
