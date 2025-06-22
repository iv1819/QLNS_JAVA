/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.OD_Connect;
import Model.OD;
import View.OrderM;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class ODController {
    private OrderM view; // Tham chiếu đến View
    private OD_Connect odConnect;
       public ODController(OrderM view) {
        this.view = view;
        this.odConnect = new OD_Connect();
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
}
