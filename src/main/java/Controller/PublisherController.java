/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.io.FileOutputStream;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JTable;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Database.Publisher_Connect;
import Model.Publisher;
import View.PublisherM;

/**
 *
 * @author nam11
 */
public class PublisherController {
    private PublisherM view;
    private Publisher_Connect model;
    public PublisherController(PublisherM view) {
        this.view = view;
        this.model = new Publisher_Connect();
    }
    public void loadAllPublishers() {
        try {
            view.updatePublisherTable(model.getAllPublishers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addPublisher(Model.Publisher publisher) {
        try {
            model.addPublisher(publisher);
            loadAllPublishers();
            javax.swing.JOptionPane.showMessageDialog(view, "Thêm thành công!");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    public void updatePublisher(Model.Publisher publisher) {
        try {
            model.updatePublisher(publisher);
            loadAllPublishers();
            javax.swing.JOptionPane.showMessageDialog(view, "Cập nhật thành công!");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    public void deletePublisher(String maNXB) {
        try {
            boolean deleted = model.deletePublisher(maNXB);
            if (deleted) {
                loadAllPublishers();
                javax.swing.JOptionPane.showMessageDialog(view, "Xóa thành công!");
            } else {
                javax.swing.JOptionPane.showMessageDialog(view, "Không thể xóa vì có sách đang liên kết với nhà xuất bản này.", "Thông báo", javax.swing.JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    public void exportToExcel() {
    JTable table = view.getPublisherTable(); // Lấy dữ liệu từ View
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
            XSSFSheet sheet = workbook.createSheet("Nhà xuất bản");

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
