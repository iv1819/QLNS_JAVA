/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Book;
import Database.Book_Connect;
import Database.OD_Connect;
import Database.Order_Connect;
import Database.VPP_Connect;
import Model.Category;
import Model.OD;
import Model.Order;
import Model.VPP;
import View.MainMenu; // Import MainMenu (View)
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.table.DefaultTableModel;

public class MainMenuController {

    private MainMenu view; 
    private Book_Connect bookConnect;
    private OD_Connect odConnect; 
    private Order_Connect orderConnect; 
    private VPP_Connect vppConnect;
    private Book currentSelectedBookForReceipt;
    private VPP currentSelectedVPPForReceipt;

    public MainMenuController(MainMenu mainMenuView) {
        this.view = mainMenuView;
        this.bookConnect = new Book_Connect();
        this.odConnect = new OD_Connect();
        this.orderConnect = new Order_Connect();
        this.vppConnect = new VPP_Connect();

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

    /** Gọi khi combo khách hàng đổi */
    public void onCustomerSelected(Object sel) {
        currentCustomerName = (sel == null) ? null : sel.toString().trim();
        recalcTotal();                         // tính lại tổng ngay
    }

    /* Bất cứ khi nào bảng hóa đơn thay đổi, gọi hàm này */
    private void recalcTotal() {
        boolean hasCustomer =
            currentCustomerName != null &&
            !currentCustomerName.isEmpty() &&
            !currentCustomerName.equalsIgnoreCase("--Chọn khách hàng--");

        view.updateReceiptTotal(hasCustomer);  // truyền cờ giảm giá
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
      public void onCheckoutClicked(String tenkh) {

            DefaultTableModel model = view.getReceiptTableModel();

            if (model.getRowCount() == 0) {
                view.showMessage("Hoá đơn đang trống.");
                return;
            }

            String maDH = generateOrderId();           // dh_123456789
            LocalDateTime ngayBan = LocalDateTime.now();
java.sql.Date ngayBanOnlyDate = java.sql.Date.valueOf(ngayBan.toLocalDate());
            double tongTien = view.LayTongTien();

            Order newOrder = new Order(maDH, tenkh, ngayBanOnlyDate, tongTien);
            orderConnect.themDH(newOrder);
            /* 2) Thêm từng chi tiết qua hàm themCTDH() */
            for (int row = 0; row < model.getRowCount(); row++) {

                OD detail = new OD();
                detail.setMaDH(maDH);
                detail.setTenSP(model.getValueAt(row, 0).toString());
                detail.setSoLuong((Integer) model.getValueAt(row, 1));
                detail.setDonGia(parseMoney(model.getValueAt(row, 2).toString()));
                
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
            }
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


    public void onBookDataChanged() {
        System.out.println("DEBUG (MainMenuPresenter): Nhận được thông báo dữ liệu sách đã thay đổi. Đang tải lại tab.");
        loadAndDisplayBooksByCategories(); // Tải lại dữ liệu khi có thay đổi
    }

}

