/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.esprit.gui;

import com.esprit.entities.Rate;
import com.esprit.services.ServiceRate;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class TableRatesController implements Initializable {

    @FXML
    private TableView<Rate> tableviewRate;
    @FXML
    private TableColumn<Rate, String> colBlogName;
    @FXML
    private TableColumn<Rate, String> colNomUtilisateur;
    @FXML
    private TableColumn<Rate, Integer> colRate;
    @FXML
    private Button retourBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       Showtherates();
    }    
      public void Showtherates() {

        ServiceRate r = new ServiceRate();
        
        
        colBlogName.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colNomUtilisateur.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("rating"));

    }

    public TableView<Rate> getTableviewRate() {
        return tableviewRate;
    }

    public void setTableviewRate(TableView<Rate> tableviewRate) {
        this.tableviewRate = tableviewRate;
    }

    public TableColumn<Rate, String> getColBlogName() {
        return colBlogName;
    }
    
    
    
    

    public void setColBlogName(TableColumn<Rate, String> colBlogName) {
        this.colBlogName = colBlogName;
    }

    public TableColumn<Rate, String> getColNomUtilisateur() {
        return colNomUtilisateur;
    }

    public void setColNomUtilisateur(TableColumn<Rate, String> colNomUtilisateur) {
        this.colNomUtilisateur = colNomUtilisateur;
    }

    public TableColumn<Rate, Integer> getColRate() {
        return colRate;
    }

    public void setColRate(TableColumn<Rate, Integer> colRate) {
        this.colRate = colRate;
    }


    @FXML
    private void backToB(ActionEvent event) {
         
         try {
            
            Parent blogRoot = FXMLLoader.load(getClass().getResource("Blog.fxml"));
            Scene blogScene = new Scene(blogRoot);
            Stage currentStage = (Stage) retourBtn.getScene().getWindow();
            currentStage.setScene(blogScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
