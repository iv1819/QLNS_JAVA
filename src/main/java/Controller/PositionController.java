/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.Position;
import Database.Position_Connect;
import View.PositionM;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author MSI GF63
 */
public class PositionController {
    private PositionM view;
    private Position_Connect db;

    public PositionController(PositionM view) {
        this.view = view;
        this.db = new Position_Connect();
    }

    public void loadAllPositions() {
        ArrayList<Position> list = db.getAllPositions();
        DefaultTableModel model = (DefaultTableModel) view.getTblChucVu().getModel();
        model.setRowCount(0);
        for (Position p : list) {
            model.addRow(new Object[]{p.getMaCV(), p.getTenCV()});
        }
    }

    public void addPosition() {
        String maCV = view.getTxtMaCV().getText().trim();
        String tenCV = view.getTxtTenCV().getText().trim();
        if (maCV.isEmpty() || tenCV.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        Position p = new Position(maCV, tenCV);
        if (db.addPosition(p)) {
            view.showMessage("Thêm chức vụ thành công!");
            loadAllPositions();
            view.clearInputFields();
        } else {
            view.showMessage("Thêm chức vụ thất bại!");
        }
    }

    public void updatePosition() {
        String maCV = view.getTxtMaCV().getText().trim();
        String tenCV = view.getTxtTenCV().getText().trim();
        if (maCV.isEmpty() || tenCV.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        Position p = new Position(maCV, tenCV);
        if (db.updatePosition(p)) {
            view.showMessage("Cập nhật chức vụ thành công!");
            loadAllPositions();
            view.clearInputFields();
        } else {
            view.showMessage("Cập nhật chức vụ thất bại!");
        }
    }

    public void deletePosition() {
        int row = view.getTblChucVu().getSelectedRow();
        if (row == -1) {
            view.showMessage("Vui lòng chọn chức vụ để xóa!");
            return;
        }
        String maCV = view.getTblChucVu().getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (db.deletePosition(maCV)) {
                view.showMessage("Xóa chức vụ thành công!");
                loadAllPositions();
                view.clearInputFields();
            } else {
                view.showMessage("Xóa chức vụ thất bại!");
            }
        }
    }

    public void searchPosition() {
        String keyword = view.getTxtTimKiem().getText().trim().toLowerCase();
        ArrayList<Position> list = db.getAllPositions();
        DefaultTableModel model = (DefaultTableModel) view.getTblChucVu().getModel();
        model.setRowCount(0);
        for (Position p : list) {
            if (p.getMaCV().toLowerCase().contains(keyword) || p.getTenCV().toLowerCase().contains(keyword)) {
                model.addRow(new Object[]{p.getMaCV(), p.getTenCV()});
            }
        }
    }
}
