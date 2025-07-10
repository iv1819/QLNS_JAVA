/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Employee;
import View.EmployeeM;
import Database.Employee_Connect;
import Model.Book;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Date;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
        // Kiểm tra mã nhân viên
        if (employee.getMaNV() == null || employee.getMaNV().trim().isEmpty()) {
            view.showErrorMessage("Mã nhân viên không được để trống!");
            return;
        }
        
        // Kiểm tra mã nhân viên trùng lặp
        if (employeeConnect.tonTaiMaNV(employee.getMaNV())) {
            view.showErrorMessage("Mã nhân viên đã tồn tại!");
            return;
        }
        
        // Kiểm tra tên nhân viên
        if (employee.getTenNV() == null || employee.getTenNV().trim().isEmpty()) {
            view.showErrorMessage("Tên nhân viên không được để trống!");
            return;
        }
        
        // Kiểm tra ngày sinh
        if (employee.getNgaySinh() == null) {
            view.showErrorMessage("Ngày sinh không được để trống!");
            return;
        }
        
        // Kiểm tra ngày vào làm
        if (employee.getNgayVaoLam() == null) {
            view.showErrorMessage("Ngày vào làm không được để trống!");
            return;
        }
        
        // Kiểm tra chức vụ
        if (employee.getMaCV() == null || employee.getMaCV().trim().isEmpty()) {
            view.showErrorMessage("Chức vụ không được để trống!");
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!isValidPhoneNumber(employee.getSdt())) {
            return; // Thông báo lỗi đã được hiển thị trong isValidPhoneNumber
        }
        
        // Kiểm tra số điện thoại trùng lặp
        if (employeeConnect.tonTaiSdt(employee.getSdt())) {
            view.showErrorMessage("Số điện thoại đã tồn tại!");
            return;
        }
        
        // Kiểm tra lương
        if (employee.getLuong() <= 0) {
            view.showErrorMessage("Lương phải lớn hơn 0!");
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
        // Kiểm tra mã nhân viên
        if (employee.getMaNV() == null || employee.getMaNV().trim().isEmpty()) {
            view.showErrorMessage("Mã nhân viên không được để trống!");
            return;
        }
        
        // Kiểm tra tên nhân viên
        if (employee.getTenNV() == null || employee.getTenNV().trim().isEmpty()) {
            view.showErrorMessage("Tên nhân viên không được để trống!");
            return;
        }
        
        // Kiểm tra ngày sinh
        if (employee.getNgaySinh() == null) {
            view.showErrorMessage("Ngày sinh không được để trống!");
            return;
        }
        
        // Kiểm tra ngày vào làm
        if (employee.getNgayVaoLam() == null) {
            view.showErrorMessage("Ngày vào làm không được để trống!");
            return;
        }
        
        // Kiểm tra chức vụ
        if (employee.getMaCV() == null || employee.getMaCV().trim().isEmpty()) {
            view.showErrorMessage("Chức vụ không được để trống!");
            return;
        }
        
        // Kiểm tra định dạng số điện thoại
        if (!isValidPhoneNumber(employee.getSdt())) {
            return; // Thông báo lỗi đã được hiển thị trong isValidPhoneNumber
        }
        
        // Kiểm tra lương
        if (employee.getLuong() <= 0) {
            view.showErrorMessage("Lương phải lớn hơn 0!");
            return;
        }
        
        // Tạm thời bỏ qua kiểm tra trùng lặp số điện thoại cho update 
        // vì cần phương thức tonTaiSdtKhac tương tự như CustomerController
        
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
    public void refreshData() {
        loadAllEmployees();
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
    
    public void exportEmployeeToExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Book");
        int rowNum = 0;
        List<Employee> Employee = employeeConnect.layToanBoNhanVien();
        // Header
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mã Nhân viên");
        headerRow.createCell(1).setCellValue("Tên Nhân viên");
        headerRow.createCell(2).setCellValue("Ngày sinh");
        headerRow.createCell(3).setCellValue("Ngày vào làm");
        headerRow.createCell(4).setCellValue("Tên công việc");
        headerRow.createCell(5).setCellValue("Số điện thoại");
        headerRow.createCell(6).setCellValue("Lương");
        

        for (Employee e : Employee) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(e.getMaNV());
            row.createCell(1).setCellValue(e.getTenNV());
            row.createCell(2).setCellValue(e.getNgaySinh());
            row.createCell(3).setCellValue(e.getNgayVaoLam());
            row.createCell(4).setCellValue(e.getTenCV());
            row.createCell(5).setCellValue(e.getSdt());
            row.createCell(6).setCellValue(e.getLuong());
            
        }

        // Lưu vào C:\aadmin
        try {
            File dir = new File("C:\\Users\\LAPTOP\\Documents\\exportJAVA");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(dir, "nhanvien.xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();

            System.out.println("✅ Xuất file thành công tại: C:\\Users\\LAPTOP\\Documents\\exportJAVA\\nhanvien.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi xuất file Excel.");
        }
    }
}
