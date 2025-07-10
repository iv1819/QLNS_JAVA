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
    public void refreshData() {
        loadAllEmployees();
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
