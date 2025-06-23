/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Order_Connect;
import Model.Order;
import View.MainMenu;
import View.OrderM;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class OrderController {
    private OrderM view; // Tham chiếu đến View
    private Order_Connect orderConnect;
    private MainMenu mainmenu;
       public OrderController(OrderM view, MainMenu main) {
        this.view = view;
        this.orderConnect = new Order_Connect();
        this.mainmenu = main;
    }
     public void loadAllOrders() {
        ArrayList<Order> orders = orderConnect.layToanBoDH();
        view.displayOrder(orders);
    }

    public void addOrder(Order order) {
        if (orderConnect.themDH(order)) {
            mainmenu.showMessage("Thêm đơn hàng thành công!");
            loadAllOrders();
        } else {
            mainmenu.showErrorMessage("Thêm đơn hàng thất bại. Vui lòng kiểm tra lại thông tin.");
        }
    }

    public void deleteOrder(String maDH) {
        if (orderConnect.xoaDH(maDH)) {
            view.showMessage("Xóa đơn hàng thành công!");
            loadAllOrders(); 
        } else {
            view.showErrorMessage("Xóa đơn hàng thất bại. Vui lòng kiểm tra lại mã đơn hàng.");
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
}
