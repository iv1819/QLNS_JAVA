/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Author_Connect;
import Model.Author;
import Model.VPP;
import View.AuthorM;
import View.DataChangeListener;
import View.DataChangeType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author trang
 */
public class AuthorController {
    private AuthorM view;
    private Author_Connect authorConnect;
    private MainMenuController mainMenuController;
    private List<DataChangeListener> listeners = new ArrayList<>();

   public void addDataChangeListener(DataChangeListener listener) {
       listeners.add(listener);
   }

   private void notifyListeners(DataChangeType type) {
       for (DataChangeListener listener : listeners) {
           listener.onDataChanged(type);
       }
   }
    public AuthorController(AuthorM view,  MainMenuController mainMenuController) {
        this.view = view;
        this.authorConnect = new Author_Connect();
        this.mainMenuController = mainMenuController;
    }
    private boolean validateInput(Author a) {

            if (a.getMaTG().isEmpty() || a.getTenTG().isEmpty()) {
                view.showErrorMessage("Vui lòng nhập đầy đủ thông tin mã và tên tác giả.");
                return false;
            }

            if (!a.getMaTG().startsWith("tg_")){
                view.showErrorMessage("Mã tác giả phải bắt đầu bằng tg_");
                return false;
            }

            if (!a.getTenTG().matches("^[\\p{L}\\s]+$")) {
                view.showErrorMessage("Tên tác giả không được chứa số hoặc ký tự đặc biệt.");
                return false;
            }
            return true;
        }
    public void loadAllAuthors() {
        ArrayList<Author> authors = authorConnect.layToanBoTacGia();
        view.displayAuthors(authors);
    }

    public void addAuthor(Author newAuthor) {
        if(!validateInput(newAuthor)){
            return;
        }
        if (authorConnect.tonTaiMaTG(newAuthor.getMaTG())) {
            view.showErrorMessage("Mã tác giả đã tồn tại!");
            return;
        }
        if (authorConnect.themTacGia(newAuthor)) {
            view.showMessage("Thêm tác giả thành công!");
            loadAllAuthors();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Thêm tác giả thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void updateAuthor(Author updatedAuthor) {
        if(!validateInput(updatedAuthor)){
            return;
        }
        if (authorConnect.capNhatTacGia(updatedAuthor)) {
            view.showMessage("Cập nhật tác giả thành công!");
            loadAllAuthors();
            view.clearInputFields();
        } else {
            view.showErrorMessage("Cập nhật tác giả thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteAuthor(String maTG) {
    ArrayList<String> dsSach = authorConnect.laySachTheoMaTG(maTG);

    StringBuilder message = new StringBuilder();
    if (!dsSach.isEmpty()) {
        message.append("⚠️ Khi xóa tác giả này, các sách sau cũng sẽ bị xóa:\n");
        for (String tenSach : dsSach) {
            message.append("- ").append(tenSach).append("\n");
        }
    }
    message.append("Bạn có chắc chắn muốn xóa tác giả này không?");

    int choice = javax.swing.JOptionPane.showConfirmDialog(null, message.toString(), "Xác nhận xóa", javax.swing.JOptionPane.YES_NO_OPTION);
    if (choice == javax.swing.JOptionPane.YES_OPTION) {
        if (authorConnect.xoaTacGia(maTG)) {
            view.showMessage("Đã xóa tác giả.");
            loadAllAuthors();
            notifyListeners(DataChangeType.BOOK);
            view.clearInputFields();
        } else {
            view.showErrorMessage("Xóa thất bại.");
        }
    }
}


    public void searchAuthors(String tenTG) {
        ArrayList<Author> searchResults = authorConnect.timTacGiaTheoTen(tenTG);
        view.displayAuthors(searchResults);
            if (searchResults.isEmpty()) {
                view.showMessage("Không tìm thấy tác giả nào phù hợp.");
            }
        }
    public void importAuthorFromExcel(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowCount = sheet.getPhysicalNumberOfRows();

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Kiểm tra từng ô
                for (int col = 0; col <= 1; col++) {
                    Cell cell = row.getCell(col);
                    if (cell == null || (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty())) {
                        view.showErrorMessage("Dữ liệu bị thiếu tại dòng " + (i + 1) + ", cột " + (col + 1));
                        return;
                    }
                }

                // Nếu không có cột nào null thì đọc dữ liệu
                String matg = row.getCell(0).getStringCellValue().trim();
                if (authorConnect.tonTaiMaTG(matg)) {
                    view.showErrorMessage("Mã tác giả đã tồn tại ở dòng " + (i + 1) + ": " + matg);
                    continue; // bỏ qua dòng này, hoặc return nếu muốn dừng toàn bộ
                }
                String tentg = row.getCell(1).getStringCellValue().trim();

                Author a = new Author(matg, tentg);
                authorConnect.themTacGia(a);
            }
            loadAllAuthors(); 
            view.showMessage("Nhập tác giả từ Excel thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            view.showErrorMessage("❌ Lỗi khi nhập tác giả từ Excel: " + e.getMessage());
        }
    }

    
    public void exportAuthorToExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Tác giả");
        int rowNum = 0;
        List<Author> tgs = authorConnect.layToanBoTacGia();
        // Heade
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mã tác giả");
        headerRow.createCell(1).setCellValue("Tên tác giả");

        for (Author tg : tgs) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(tg.getMaTG());
            row.createCell(1).setCellValue(tg.getTenTG());
        }

        // Lưu vào C:\aadmin
        try {
            File dir = new File("C:/Users/Admin");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(dir, "author.xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();

            System.out.println("✅ Xuất file thành công tại: C:\\aadmin\\donhang.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("❌ Lỗi khi xuất file Excel.");
        }
    }
}

    

   

    

    

    
    
    
