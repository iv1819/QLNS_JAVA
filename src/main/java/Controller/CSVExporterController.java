/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.*;
/**
 *
 * @author MSI GF63
 */
public class CSVExporterController {
     public static void exportToCSV(JTable table, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            TableModel model = table.getModel();

            // Ghi header
            for (int i = 0; i < model.getColumnCount(); i++) {
                bw.write(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) bw.write(",");
            }
            bw.newLine();

            // Ghi dữ liệu
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    bw.write(value != null ? value.toString() : "");
                    if (col < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }

            bw.flush();
            JOptionPane.showMessageDialog(null, "Xuất file thành công!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi ghi file CSV: " + e.getMessage());
        }
    }
}
