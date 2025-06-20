/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Provider_Connect;
import View.ProviderM;

/**
 *
 * @author nam11
 */
public class ProviderController {
    private ProviderM view;
    private Provider_Connect model;
    public ProviderController(ProviderM view) {
        this.view = view;
        this.model = new Provider_Connect();
    }
    public void loadAllProviders() {
        try {
            view.updateProviderTable(model.getAllProviders());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addProvider(String tenNCC, String sdt, String diaChi) {
        try {
            Model.Provider provider = new Model.Provider();
            provider.setTenNCC(tenNCC);
            provider.setSdt(sdt);
            provider.setDiaChi(diaChi);
            if (model.addProvider(provider)) {
                view.showMessage("Thêm nhà cung cấp thành công!");
                loadAllProviders();
            } else {
                view.showMessage("Thêm nhà cung cấp thất bại!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            view.showMessage("Lỗi khi thêm nhà cung cấp: " + e.getMessage());
        }
    }
    
}
