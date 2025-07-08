/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Book;
import Database.Book_Connect;
import Database.Customer_Connect;
import Database.OD_Connect;
import Database.Order_Connect;
import Database.VPP_Connect;
import Model.Category;
import Model.OD;
import Model.Order;
import Model.VPP;
import View.DataChangeListener;
import View.DataChangeType;
import View.MainMenu; // Import MainMenu (View)
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.concurrent.ThreadLocalRandom;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.awt.Desktop;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingWorker;
public class MainMenuController implements DataChangeListener {
    @Override
    public void onDataChanged(DataChangeType type) {
        switch (type) {
            case BOOK:
                loadAndDisplayBooksByCategories();
                break;
            case VPP:
                loadAndDisplayBooksByCategories();
                break;
        }
    }
    private MainMenu view; 
    private Book_Connect bookConnect;
    private OD_Connect odConnect; 
    private Order_Connect orderConnect; 
    private VPP_Connect vppConnect;
    private Customer_Connect cusConnect;
    private Book currentSelectedBookForReceipt;
    private VPP currentSelectedVPPForReceipt;

    public MainMenuController(MainMenu mainMenuView) {
        this.view = mainMenuView;
        this.bookConnect = new Book_Connect();
        this.odConnect = new OD_Connect();
        this.orderConnect = new Order_Connect();
        this.vppConnect = new VPP_Connect();
        this.cusConnect = new Customer_Connect();

    }
    public void onBookItemSelected(Book book) {
        this.currentSelectedBookForReceipt = book;
        view.updateSelectedBook(book);
    }
    public void onVPPItemSelected(VPP vpp) {
        this.currentSelectedVPPForReceipt = vpp;
        view.updateSelectedVPP(vpp);
    }
    public void loadAndDisplayBooksByCategories() {
            // ---- 1. Chuẩn bị Book theo danh mục ----
        LinkedHashMap<String, ArrayList<Book>> booksByCat = new LinkedHashMap<>();
        booksByCat.put("Tất cả Sách", bookConnect.layToanBoSach());

        for (Category dm : bookConnect.layTatCaDanhMucInfo()) {
            booksByCat.put(dm.getTenDanhMuc(),
                           bookConnect.laySachTheoMaDanhMuc(dm.getMaDanhMuc()));
        }

        // ---- 2. Lấy toàn bộ VPP ----
        ArrayList<VPP> allVPP = vppConnect.layToanBoVPP();

        // ---- 3. Đẩy sang View (1 Map + 1 List) ----
        view.populateCategoryTabs(booksByCat, allVPP);
    }
        /* Giữ trạng thái */
    private String currentCustomerName = null;

public String onCustomerSelected(String sel) {
    currentCustomerName = null;
    // Check if sel is null, empty, or not a number (only digits)
    if(sel == null|| sel.trim().isEmpty() ||!sel.matches("\\d+")){
        System.out.println("trong " + currentCustomerName);
    }
    else{
        String tenKH = cusConnect.timKhachHangSDT(sel.trim());
        currentCustomerName = tenKH;
    }
    
    recalcTotal(); // Tính lại tổng ngay
    return currentCustomerName; // Return the customer name or null
}

    /* Bất cứ khi nào bảng hóa đơn thay đổi, gọi hàm này */
    private void recalcTotal() {

        view.updateReceiptTotal(currentCustomerName != null);  // truyền cờ giảm giá
    }

      public void onAddReceiptItemClicked(int quantity) {

            // ==== VALIDATE NHƯ CŨ ====
            if (currentSelectedBookForReceipt == null && currentSelectedVPPForReceipt== null) {
                view.showMessage("Vui lòng chọn một sản phẩm trước.");
                return;
            }
            if (quantity <= 0) {
                view.showMessage("Số lượng phải lớn hơn 0.");
                return;
            }
            if(currentSelectedBookForReceipt == null){
                VPP v = currentSelectedVPPForReceipt;
                DefaultTableModel model = view.getReceiptTableModel();

                // ==== TÌM XEM ĐÃ CÓ SÁCH NÀY TRONG BẢNG CHƯA ====
                int existedRow = -1;
                int existedQty = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(v.getTenVPP())) {
                        existedRow = i;
                        existedQty = (Integer) model.getValueAt(i, 1);
                        break;
                    }
                }

                int totalQtyAfterAdd = existedQty + quantity;

                // ==== KIỂM TRA TỒN KHO VỚI TỔNG SỐ LƯỢNG ====
                if (totalQtyAfterAdd > v.getSoLuong()) {
                    view.showMessage(
                        "Số lượng yêu cầu vượt quá tồn kho ("
                        + v.getSoLuong() + "). Hiện đang có "
                        + existedQty + "cái trong hóa đơn."
                    );
                    return;
                }

                double donGia = v.getGiaBan();
                double tongGiaRow = totalQtyAfterAdd * donGia;

                if (existedRow >= 0) {
                    // ---> ĐÃ CÓ: cập nhật số lượng & tổng giá
                    model.setValueAt(totalQtyAfterAdd, existedRow, 1);               // cột 1: Số lượng
                    model.setValueAt(String.format("%.0f $", tongGiaRow), existedRow, 3); // cột 3: Tổng giá
                } else {
                    // ---> CHƯA CÓ: thêm mới
                    Object[] row = {
                        v.getTenVPP(),
                        quantity,
                        String.format("%.0f $", donGia),
                        String.format("%.0f $", quantity * donGia)
                    };
                    model.addRow(row);
                }

                // ==== CẬP NHẬT TỔNG TIỀN & TỔNG SỐ LƯỢNG ====
                recalcTotal();

                // ==== RESET CHỌN SÁCH ====
                currentSelectedVPPForReceipt = null;
                view.updateSelectedVPP(null);
            }
            else{
                Book book = currentSelectedBookForReceipt;
                DefaultTableModel model = view.getReceiptTableModel();

                // ==== TÌM XEM ĐÃ CÓ SÁCH NÀY TRONG BẢNG CHƯA ====
                int existedRow = -1;
                int existedQty = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 0).equals(book.getTenSach())) {
                        existedRow = i;
                        existedQty = (Integer) model.getValueAt(i, 1);
                        break;
                    }
                }

                int totalQtyAfterAdd = existedQty + quantity;

                // ==== KIỂM TRA TỒN KHO VỚI TỔNG SỐ LƯỢNG ====
                if (totalQtyAfterAdd > book.getSoLuong()) {
                    view.showMessage(
                        "Số lượng yêu cầu vượt quá tồn kho ("
                        + book.getSoLuong() + "). Hiện đang có "
                        + existedQty + " quyển trong hóa đơn."
                    );
                    return;
                }

                double donGia = book.getGiaBan();
                double tongGiaRow = totalQtyAfterAdd * donGia;

                if (existedRow >= 0) {
                    // ---> ĐÃ CÓ: cập nhật số lượng & tổng giá
                    model.setValueAt(totalQtyAfterAdd, existedRow, 1);               // cột 1: Số lượng
                    model.setValueAt(String.format("%.0f $", tongGiaRow), existedRow, 3); // cột 3: Tổng giá
                } else {
                    // ---> CHƯA CÓ: thêm mới
                    Object[] row = {
                        book.getTenSach(),
                        quantity,
                        String.format("%.0f $", donGia),
                        String.format("%.0f $", quantity * donGia)
                    };
                    model.addRow(row);
                }

                // ==== CẬP NHẬT TỔNG TIỀN & TỔNG SỐ LƯỢNG ====
                recalcTotal();

                // ==== RESET CHỌN SÁCH ====
                currentSelectedBookForReceipt = null;
                view.updateSelectedBook(null);
            }
        }
            private String generateOrderId() {
            int rand = ThreadLocalRandom.current().nextInt(100000, 999999);
            return "dh_" + rand;
        }
      public void onCheckoutClicked(String tenkh, String tenNV) {

            DefaultTableModel model = view.getReceiptTableModel();

            if (model.getRowCount() == 0) {
                view.showMessage("Hoá đơn đang trống.");
                return;
            }

            String maDH = generateOrderId();           // dh_123456789
            LocalDateTime ngayBan = LocalDateTime.now();
java.sql.Date ngayBanOnlyDate = java.sql.Date.valueOf(ngayBan.toLocalDate());
            double tongTien = view.LayTongTien();

            Order newOrder = new Order(maDH, tenkh, ngayBanOnlyDate, tongTien, tenNV);
            orderConnect.themDH(newOrder);
            List<OD> ctdhs = new ArrayList<>();
            /* 2) Thêm từng chi tiết qua hàm themCTDH() */
            for (int row = 0; row < model.getRowCount(); row++) {

                OD detail = new OD();
                detail.setMaDH(maDH);
                detail.setTenSP(model.getValueAt(row, 0).toString());
                detail.setSoLuong((Integer) model.getValueAt(row, 1));
                detail.setDonGia(parseMoney(model.getValueAt(row, 2).toString()));
                detail.setTongTien(parseMoney(model.getValueAt(row, 3).toString()));

                
                if (!odConnect.themCTDH(detail)) {
                    view.showErrorMessage("Them CTDH that bai");
                }
                else{
                    String tenSP = detail.getTenSP();
                    int soLuongMua = detail.getSoLuong();

                    int soLuongHienTaiSach = bookConnect.laySoLuongSach(tenSP);
                    int soLuongConLai = soLuongHienTaiSach - soLuongMua;

                    if (soLuongHienTaiSach == 0) {
                        int soLuongHienTaiVPP = vppConnect.laySoLuongVPP(tenSP);
                        soLuongConLai = soLuongHienTaiVPP - soLuongMua;
                        vppConnect.capNhatSoLuongVPP(tenSP, soLuongConLai);
                    } else {
                        bookConnect.capNhatSoLuongSach(tenSP, soLuongConLai);
                    }
                }
                ctdhs.add(detail);
            }
            String filePath = "C:/Users/LAPTOP/Documents/exportJAVA/hoadon.pdf";
            exportInvoiceToPDF(filePath, newOrder, ctdhs);
            view.clearReceiptTable(); 
        }

public ArrayList<String> getAllTenKH(){
        return orderConnect.getAllTenKH();
    }

private double parseMoney(String str) {
    return Double.parseDouble(
        str.replace("$", "").replace(",", "").trim()
    );
}
    public static void exportInvoiceToPDF(String filePath, Order dh, List<OD> ctdhs) {
        Rectangle pageSize = new Rectangle(298, 420); // A6 in points (1 point = 1/72 inch)
        Document document = new Document(pageSize, 20, 20, 20, 20); // A6 paper

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // ============ CHÈN LOGO =============
            try {
                Image logo = Image.getInstance("images/logo.jpg");
                logo.scaleAbsolute(50f, 50f);
                logo.setAlignment(Image.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("Không tìm thấy hoặc không thể chèn logo: " + e.getMessage());
            }

            // ============ TIÊU ĐỀ ============
            BaseFont bf = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bf, 14, Font.BOLD);

            Paragraph title = new Paragraph("HÓA ĐƠN BÁN HÀNG", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            Font fonthg = new Font(bf, 12);
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Mã ĐH: " + dh.getMaDH(),fonthg));
            document.add(new Paragraph("Ngày: " + dh.getNgayBan(),fonthg));
            document.add(new Paragraph("Khách: " + (dh.getTenKH() != null ? dh.getTenKH() : "Khách lẻ"),fonthg));
            document.add(new Paragraph("Nhân viên: " + dh.getTenNV(),fonthg));

            document.add(new Paragraph(" "));

            // ============ BẢNG SẢN PHẨM ============
            PdfPTable table = new PdfPTable(4);
            table.setWidths(new float[]{3, 1, 2, 2});
            table.setWidthPercentage(100);
            table.setSpacingBefore(5f);
            table.setSpacingAfter(5f);

            table.addCell("SP");
            table.addCell("SL");
            table.addCell(new Phrase("Đ. Giá", fonthg));
            table.addCell(new Phrase("Th. Tiền", fonthg));
            for (OD ct : ctdhs) {
                table.addCell(new Phrase(ct.getTenSP(), fonthg));
                table.addCell(String.valueOf(ct.getSoLuong()));
                table.addCell(String.format("%,.0f", ct.getDonGia()));
                table.addCell(String.format("%,.0f", ct.getSoLuong() * ct.getDonGia()));
            }

            document.add(table);

            // ============ TỔNG TIỀN ============
            Paragraph total = new Paragraph("Tổng: " + String.format("%,.0f", dh.getTongTien()) + " $",
                    new Font(bf, 12, Font.BOLD));
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            // ============ CẢM ƠN ============
            document.add(new Paragraph(" "));
            Paragraph thanks = new Paragraph("Cảm ơn quý khách!", new Font(bf, 12));
            thanks.setAlignment(Element.ALIGN_CENTER);
            document.add(thanks);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(new File(filePath)); // Mở file bằng phần mềm mặc định trên máy
                } else {
                    System.out.println("Desktop không được hỗ trợ trên hệ điều hành này.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Không thể mở hóa đơn: " + e.getMessage());
            }
        }
    }
    public void onBookDataChanged() {
        System.out.println("DEBUG (MainMenuPresenter): Nhận được thông báo dữ liệu sách đã thay đổi. Đang tải lại tab.");
        loadAndDisplayBooksByCategories(); // Tải lại dữ liệu khi có thay đổi
    }

}