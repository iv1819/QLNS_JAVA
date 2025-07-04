/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Book_Connect;
import Database.Customer_Connect;
import Database.OD_Connect;
import Database.Order_Connect;
import Database.VPP_Connect;
import Model.Customer;
import Model.OD;
import Model.Order;
import View.MainMenu;
import View.OrderM;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Admin
 */
public class OrderController {
    private OrderM view; // Tham chiếu đến View
    private Order_Connect orderConnect;
    private Customer_Connect cusConnect;
    private MainMenu mainmenu;
    private OD_Connect odConnect;
    private Book_Connect bookConnect;
    private VPP_Connect vppConnect;
       public OrderController(OrderM view, MainMenu main) {
        this.view = view;
        this.orderConnect = new Order_Connect();
        this.odConnect = new OD_Connect();
        this.bookConnect = new Book_Connect();
        this.vppConnect = new VPP_Connect();
        this.cusConnect = new Customer_Connect();
        this.mainmenu = main;
    }
        public void loadAllOD(String maDH) {
        ArrayList<OD> ods = odConnect.layCTDHtheoMaDH(maDH);
        view.displayOD(ods);
    }

    public void addOD(OD ctdh) {
        odConnect.themCTDH(ctdh);
    }

    public void deleteOD(String maDH) {
        if (odConnect.xoaCTDH(maDH)) {
            view.showMessage("Xóa CTDH thành công!");
            loadAllOD(maDH); // Cập nhật lại JTable
        } else {
            view.showErrorMessage("Xóa CTDH thất bại. Vui lòng kiểm tra lại mã CTDH.");
        }
    }
     public void loadAllOrders() {
        ArrayList<Order> orders = orderConnect.layToanBoDH();
        view.displayOrder(orders);
    }

    public void addOrder(Order order) {
        if (orderConnect.themDH(order)) {
            mainmenu.showMessage("Thêm đơn hàng thành công!");
            loadAllOrders();
            view.clearInputFields();
        } else {
            mainmenu.showErrorMessage("Thêm đơn hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteOrder(String maDH) {
        if(odConnect.xoaCTDH(maDH)){
             if (orderConnect.xoaDH(maDH)) {
            view.showMessage("Xóa đơn hàng thành công!");
            loadAllOrders();
            loadAllOD(maDH);
            view.clearInputFields();
        } else {
            view.showErrorMessage("Xóa đơn hàng thất bại. Vui lòng kiểm tra lại mã đơn hàng.");
        }
        }
        else{
            view.showErrorMessage("Xóa đơn hàng thất bại vì ko xóa dc ctdh. Vui lòng kiểm tra lại mã đơn hàng.");
        }
    }

    public void searchOrder(String maDH) {
        ArrayList<Order> searchResults;
        if (!maDH.isEmpty()) {
            // Giả sử có phương thức tìm kiếm theo cả tên sách và tác giả
            searchResults = orderConnect.layDonHangTheoMaDH(maDH);
        } else {
            loadAllOrders(); // Nếu không nhập gì thì hiển thị toàn bộ
            return;
        }
        view.displayOrder(searchResults);
        if (searchResults.isEmpty()) {
            view.showMessage("Không tìm thấy đơn hàng nào phù hợp.");
        }
    }
    private String generateOrderId() {
            int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
            return "dh_" + rand;
        }
    String maDH = "0";
   public void nhapExcel(File file) {
    try (FileInputStream fis = new FileInputStream(file);
         XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

        XSSFSheet sheet = workbook.getSheetAt(0);
        int rowCount = sheet.getPhysicalNumberOfRows();
        Map<String, Order> existingOrders = new HashMap<>();

        for (int i = 1; i < rowCount; i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            // 1. Mã đơn hàng (bỏ qua cột nhập, tự sinh)
            Cell cellMaDH = row.getCell(0);
            if (cellMaDH != null && cellMaDH.getCellType() != CellType.BLANK) {
                view.showErrorMessage("❌ Mã đơn hàng phải để trống ở dòng " + (i + 1));
                return;
            }
            

            // 2. Ngày lập
            Cell cell = row.getCell(1);
            java.sql.Date sqlDate = null;

            if (cell != null) {
                if (cell.getCellType() == CellType.STRING) {
                    String dateStr = cell.getStringCellValue().trim(); // "6/22/2025"
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
                        java.util.Date utilDate = sdf.parse(dateStr);
                        sqlDate = new java.sql.Date(utilDate.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date utilDate = cell.getDateCellValue();
                    sqlDate = new java.sql.Date(utilDate.getTime());
                }
            }

            if (sqlDate == null) {
                System.out.println("⚠️ Không đọc được ngày lập ở dòng " + (i + 1));
            }

            // 3. Tên khách hàng
            Cell cellTenKH = row.getCell(2);
            String tenKH = (cellTenKH == null || cellTenKH.getCellType() == CellType.BLANK)
                    ? null : cellTenKH.getStringCellValue().trim();
            String maKH = null;

            if ((tenKH != null && !tenKH.isEmpty())) {
                maKH = orderConnect.getMaKH(tenKH);
                if (maKH == null) {
                    maKH = generateKHId();
                    Customer kh = new Customer(maKH, tenKH, "");
                    cusConnect.themKhachHang(kh);
                    System.out.println("Đã thêm KH mới: " + maKH + " - " + tenKH);
                }
            }

            // 4. Tên sản phẩm
            String tenSp = row.getCell(3).getStringCellValue().trim();
            String maSP = odConnect.timMaSP(tenSp);
            if (maSP == null) {
                view.showErrorMessage("❌ Không tồn tại sản phẩm: " + tenSp + " (dòng " + (i + 1) + ")");
                return;
            }

            // 5. Số lượng, đơn giá, tổng tiền
            int soLuong = (int) row.getCell(4).getNumericCellValue();
            double donGia = row.getCell(5).getNumericCellValue();
            double tongtiensp = row.getCell(6).getNumericCellValue();
            double tongtien = row.getCell(7).getNumericCellValue();
            String tenNV = row.getCell(8).getStringCellValue().trim();
            // 6. Tạo đơn hàng (nếu chưa có)
            if (sqlDate != null) {
                maDH = generateOrderId();
                Order dh = new Order(maDH, tenKH, sqlDate, tongtien, tenNV);
                orderConnect.themDH(dh);
                existingOrders.put(maDH, dh);
                System.out.println("✅ Đã thêm đơn hàng: " + maDH + ", KH: " + maKH);
                
            }

            // 7. Thêm chi tiết đơn hàng
            OD ctdh = new OD();
            ctdh.setMaDH(maDH);
            ctdh.setTenSP(tenSp);
            ctdh.setSoLuong(soLuong);
            ctdh.setDonGia(donGia);
            ctdh.setTongTien(tongtiensp);
            if(odConnect.laySoLuongSP(tenSp) < soLuong){
                System.out.println("san pham ko du de ban " + maDH + ": " + tenSp);
                continue;
            }
            if(odConnect.themCTDH(ctdh)){
                // Giảm số lượng sách còn lại
                    String tenSP = ctdh.getTenSP();
                    int soLuongMua = ctdh.getSoLuong();

                    int soLuongHienTaiSach = bookConnect.laySoLuongSach(tenSP);
                    int soLuongConLai = soLuongHienTaiSach - soLuongMua;

                    if (soLuongHienTaiSach == 0) {
                        int soLuongHienTaiVPP = vppConnect.laySoLuongVPP(tenSP);
                        soLuongConLai = soLuongHienTaiVPP - soLuongMua;
                        vppConnect.capNhatSoLuongVPP(tenSP, soLuongConLai);
                    } else {
                        bookConnect.capNhatSoLuongSach(tenSP, soLuongConLai);
                    }
                System.out.println("Đã thêm CTDH cho " + maDH + ": " + tenSp);
            }else{
                System.out.println("that bai " + maDH + ": " + tenSp);
            }
            
        }

        loadAllOrders(); // Load lại lên bảng
        view.showMessage("Nhập dữ liệu từ Excel thành công!");

    } catch (Exception e) {
        e.printStackTrace();
        view.showErrorMessage("Lỗi khi nhập Excel: " + e.getMessage());
    }
}


        public void exportDonHangToExcel() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("DonHang_ChiTiet");
        int rowNum = 0;
        List<Order> donHangList = orderConnect.layToanBoDH();
        // Header
        XSSFRow headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("Mã Đơn Hàng");
        headerRow.createCell(1).setCellValue("Ngày Lập");
        headerRow.createCell(2).setCellValue("Tên KH");
        headerRow.createCell(3).setCellValue("Tên sản phẩm");
        headerRow.createCell(4).setCellValue("Số lượng");
        headerRow.createCell(5).setCellValue("Đơn giá");
                headerRow.createCell(6).setCellValue("Tổng tiền sản phẩm");

        headerRow.createCell(7).setCellValue("Tổng tiền");
        headerRow.createCell(8).setCellValue("Tên nhân viên");
        for (Order dh : donHangList) {
            List<OD> ctdhs = odConnect.layCTDHtheoMaDH(dh.getMaDH());
            maDH = dh.getMaDH();
            if (ctdhs != null && !ctdhs.isEmpty()) {
                boolean isFirstCT = true;
                for (OD ct : ctdhs) {
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dh.getMaDH());
                    if(isFirstCT){
                        row.createCell(1).setCellValue(dh.getNgayBan().toString());
                        isFirstCT = false;
                    }
                    else{
                        row.createCell(1).setCellValue("");
                    }
                    
                    row.createCell(2).setCellValue(dh.getTenKH() != null ? dh.getTenKH() : ""); // Cho phép null
                    row.createCell(3).setCellValue(ct.getTenSP());
                    row.createCell(4).setCellValue(ct.getSoLuong());
                    row.createCell(5).setCellValue(ct.getDonGia());
                    row.createCell(6).setCellValue(ct.getTongTien());

                    row.createCell(7).setCellValue(dh.getTongTien());
                    row.createCell(8).setCellValue(dh.getTenNV());

                }
            } else {
                // Không có chi tiết đơn hàng
                XSSFRow row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dh.getMaDH());
                row.createCell(1).setCellValue(dh.getNgayBan().toString());
                row.createCell(2).setCellValue(dh.getTenKH() != null ? dh.getTenKH() : "");
                row.createCell(3).setCellValue(""); // các cột CTDH để trống
                row.createCell(4).setCellValue("");
                row.createCell(5).setCellValue("");
                row.createCell(6).setCellValue("");
                row.createCell(7).setCellValue("");
                row.createCell(8).setCellValue(dh.getTenNV());
            }
        }

        // Lưu vào C:\aadmin
        try {
            File dir = new File("C:/Users/Admin");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream out = new FileOutputStream(new File(dir, "donhang.xlsx"));
            workbook.write(out);
            out.close();
            workbook.close();

            System.out.println("Xuất file thành công tại: C:\\aadmin\\donhang.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Lỗi khi xuất file Excel.");
        }
    }

    private String generateKHId() {
            int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
            return "kh_" + rand;
        }
}
