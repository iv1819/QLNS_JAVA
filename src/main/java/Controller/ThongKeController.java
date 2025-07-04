/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Book_Connect;
import Database.Order_Connect;
import Database.VPP_Connect;
import Model.Book;
import Model.VPP;
import View.ThongKeM;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Admin
 */
public class ThongKeController {
    private ThongKeM view; // Tham chiếu đến View
    private Order_Connect orderConnect;
    private Book_Connect bookCon;
        private VPP_Connect vppCon;

       public ThongKeController(ThongKeM view) {
        this.view = view;
        this.orderConnect = new Order_Connect();
        this.bookCon = new Book_Connect();
        this.vppCon = new VPP_Connect();

    }
       public double tinhTongTien(Date start, Date end){
           return orderConnect.getTongTien(start, end);
       }
       public int tongDH(Date start, Date end){
           return orderConnect.getTongDH(start, end);

       }
       public int tongSP(Date start, Date end){
          return orderConnect.getTongSanPham(start, end);

       }
       public String spBanChay(Date start, Date end){
           return orderConnect.getSanPhamBanChay(start, end);

       }

       public void locSachSL(int sl){
           ArrayList<Book> books = bookCon.laySachtheoSoLuong(sl);
        view.displayBooks(books);
       }
       public void locVPPSL(int sl){
           ArrayList<VPP> vpp = vppCon.layVppTheoSL(sl);
        view.displayVPP(vpp);
       }
    public ChartPanel taoBieuDoDoanhThu(Date startDate, Date endDate) {
    Map<String, Double> data = orderConnect.getDoanhThuTheoNgay(startDate, endDate);

    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    for (Map.Entry<String, Double> entry : data.entrySet()) {
 System.out.println("Ngày: " + entry.getKey() + ", Doanh thu: " + entry.getValue());
        dataset.addValue(entry.getValue(), "Doanh thu", entry.getKey());
    }

    JFreeChart chart = ChartFactory.createBarChart(
            "Biểu đồ doanh thu từ " + startDate + " đến " + endDate,
            "Ngày",
            "Doanh thu (VNĐ)",
            dataset,
            PlotOrientation.VERTICAL,       // Chiều: dọc hoặc ngang
            false,                           // Hiển thị chú thích (legend)
            true,                            // Tooltips
            false  
    );

    return new ChartPanel(chart);
}
}
