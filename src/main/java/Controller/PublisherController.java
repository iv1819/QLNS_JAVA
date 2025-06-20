/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Database.Publisher_Connect;
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
    
}
