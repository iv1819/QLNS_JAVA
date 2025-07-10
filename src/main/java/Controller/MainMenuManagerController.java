/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import View.AccountM;
import View.AuthorM;
import View.BookM;
import View.CategoryM;
import View.CustomerM;
import View.EmployeeM;
import View.MainMenu;
import View.MainMenu_Manager2;
import View.OrderM;
import View.PositionM;
import View.ProviderM;
import View.PublisherM;
import View.ThongKeM;
import View.VppM;

/**
 *
 * @author Admin
 */
public class MainMenuManagerController {
    
    private MainMenu_Manager2 view;
    private MainMenuController main;
    public MainMenuManagerController(MainMenuController mainMenu) {
        this.main = mainMenu;
    }
    
    /**
     * Mở form quản lý sách
     */
    public void Back(){
      
        MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
                  
    }
    public void openDM(){
        CategoryM categoryM = new CategoryM();
                categoryM.setVisible(true);
    }
    public void openNCC(){
        ProviderM providerM = new ProviderM();
                providerM.setVisible(true);
    }
    public void openAccount(){
         AccountM accountM = new AccountM();
                accountM.setVisible(true);
    }
    public void openBookManagement() {
        BookM bookManagementFrame = new BookM(main);
        bookManagementFrame.setVisible(true);
    }
     public void openVPP() {
        VppM vppM = new VppM(main);
        vppM.setVisible(true);
    }
    public void openNXB() {
        PublisherM publisherM = new PublisherM();
                publisherM.setVisible(true);
    }
    public void openAuthor(MainMenuController controller) {
        AuthorM author = new AuthorM(controller);
                author.setVisible(true);
    }
    public void openTK(){
        ThongKeM tk = new ThongKeM();
                tk.setVisible(true);
    }
    public void openCV(MainMenu parent, MainMenuController controller, boolean IsManager){
         PositionM positionM = new PositionM(controller, parent, IsManager, this);
                positionM.setVisible(true);
    }
    public void openOrder() {
        OrderM orderM = new OrderM();
        orderM.setVisible(true);
    }
    public void openCustomer(MainMenu parent, MainMenuController controller, boolean IsManager) {
        CustomerM customerManagementFrame = new CustomerM(controller, parent, IsManager, this);
        customerManagementFrame.setVisible(true);
    }
    /**
     * Mở form quản lý nhân viên
     */
    public void openEmployeeManagement(MainMenu parent, MainMenuController controller, boolean IsManager) {
        EmployeeM employeeManagementFrame = new EmployeeM(controller, parent, IsManager, this);
        employeeManagementFrame.setVisible(true);
    }
    
    /**
     * Quay lại MainMenuManager (được gọi từ các form con)
     */
    public void showMainMenuManager() {
        view.setVisible(true);
    }
} 
