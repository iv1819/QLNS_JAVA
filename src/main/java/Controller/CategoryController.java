/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.Category;

import Database.Category_Connect;
import View.CategoryM;

/**
 *
 * @author nam11
 */
public class CategoryController {
    
    private CategoryM view;
    private Category_Connect model;
    
    public CategoryController(CategoryM view) {
        this.view = view;
        this.model = new Category_Connect();
    }
    public void loadAllCategories() {
        try {
            ArrayList<Category> categories = model.getAllCategories();
            view.updateCategoryTable(categories); // gọi view để cập nhật bảng
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Trong CategoryController.java
    public void addCategory(Category category) {
        try {
            model.addCategory(category); // Gọi phương thức void
            loadAllCategories(); // Cập nhật dữ liệu
            JOptionPane.showMessageDialog(view, "Thêm thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deleteCategory(String maDanhMuc) {
        if (model.deleteCategory(maDanhMuc)) {
            JOptionPane.showMessageDialog(view, "Xóa thành công!");
            loadAllCategories(); // Cập nhật lại danh sách
        } else {
            JOptionPane.showMessageDialog(view, 
                "Không thể xóa! Có sách đang thuộc danh mục này.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void updateCategory(Category category) {
        try {
            model.updateCategory(category); // Gọi phương thức void
            loadAllCategories(); // Cập nhật dữ liệu
            JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
      public void exportToExcel() {
        // TODO Auto-generated method stub
        JTable table = view.getCategoryTable(); // Lấy dữ liệu từ View
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu tệp Excel");
        int userSelection = fileChooser.showSaveDialog(view);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Danh sách danh muc");

                // ✅ Truyền TableModel vào Model để ghi dữ liệu
                model.exportToExcel(table.getModel(), sheet);

                // ✅ Ghi workbook ra file
                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }
                workbook.close();

                javax.swing.JOptionPane.showMessageDialog(view, "Xuất dữ liệu thành công!");
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}