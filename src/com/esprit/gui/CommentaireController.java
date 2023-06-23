/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.esprit.gui;

import com.esprit.entities.Commentaire;
import com.esprit.services.ServiceCommentaire;
import com.esprit.utils.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author User
 */
public class CommentaireController implements Initializable {

    private String[] badWordsList = {"badword1", "badword2", "badword3"};

    private TextField tfidComm;
    @FXML
    private TextField tfidContenue;
    private TextField tfidUtilisateur;
    @FXML
    private Button btnBackToBlog;

    @FXML
    private Button btnAjouterComm;
    @FXML
    private Button btnSuppComm;
    @FXML
    private Button btnModifcomm;
    @FXML
    private TableView<Commentaire> infocommentaires;

    @FXML
    private TableColumn<Commentaire, String> colContenue;
    @FXML
    private TableColumn<Commentaire, String> colidUtilisateur;
    private TableColumn<Commentaire, String> colBlogDesc;
    private Connection cnx = DataSource.getInstance().getCnx();
    ServiceCommentaire com = new ServiceCommentaire();
    private TableColumn<Commentaire, String> colidBlog;
    Map<Integer, String> mapBlogName = new HashMap();
    ObservableList list = FXCollections.observableArrayList();
    Map<Integer, String> mapUserName = new HashMap();
    ObservableList listUser = FXCollections.observableArrayList();
    @FXML
    private ComboBox<String> blogCombo;
    @FXML
    private ComboBox<String> comboUtilisateur;
    int index = -1;
    int idb = 0;
    @FXML
    private AnchorPane Rate;

    public int getIdb() {
        return idb;
    }

    public void setIdb(int idb) {
        this.idb = idb;
    }
    int idC = 0;
    int idu = 0;
    Commentaire Comn = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ShowCommentaires();

        String req = "SELECT * FROM Blog ";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                mapBlogName.put(rs.getInt("idBlog"), rs.getString("titre"));
            }
            for (Map.Entry ele : mapBlogName.entrySet()) {
                list.add(ele.getValue());
            }

            blogCombo.setItems(list);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        String req1 = "SELECT * FROM utilisateur ";
        try {
            PreparedStatement pst = cnx.prepareStatement(req1);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                mapUserName.put(rs.getInt("id"), rs.getString("Nom"));
            }
            for (Map.Entry ele : mapUserName.entrySet()) {
                listUser.add(ele.getValue());
            }

            comboUtilisateur.setItems(listUser);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private boolean containsBadWords(String contenue) {
        List<String> badWords = Arrays.asList(badWordsList);
        String[] words = contenue.toLowerCase().split("\\s+");
        for (String word : words) {
            if (badWords.contains(word)) {
                return true;
            }
        }
        return false;
    }

    @FXML
    private void AddComm(ActionEvent event) throws IOException {
        Commentaire comtest = new Commentaire(null, 0, 0);

        String contenue = tfidContenue.getText();
        if (contenue.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ de contenu !");
            return;
        }

        comtest.setContenue(contenue);

        for (Map.Entry ele : mapBlogName.entrySet()) {
            if (containsBadWords(contenue)) {
                JOptionPane.showMessageDialog(null, "Le commentaire contient des mots interdits.");
                return;
            }
            if (ele.getValue().equals(blogCombo.getValue())) {
                comtest.setIdBlog(Integer.parseInt(ele.getKey().toString()));
            }
        }

        // Mettre à jour la valeur sélectionnée du ComboBox comboUtilisateur
        comboUtilisateur.getSelectionModel().select(comboUtilisateur.getValue());

        for (Map.Entry ele : mapUserName.entrySet()) {
            if (ele.getValue().equals(comboUtilisateur.getValue())) {
                comtest.setIdUtilisateur(Integer.parseInt(ele.getKey().toString()));
            }
        }

        com.ajouter1(comtest);
        ShowCommentaires();
        JOptionPane.showMessageDialog(null, "Commentaire ajouté");
    }

    @FXML
    private void deletComm(ActionEvent event) throws IOException {

        ServiceCommentaire com = new ServiceCommentaire();
        com.supprimer(Comn);
        //com.supprimer(new Commentaire(Integer.parseInt(tfidComm.getText()), tfidContenue.getText(), Integer.parseInt(tfidUtilisateur.getText()), Integer.parseInt(blogCombo.getText())));
        JOptionPane.showMessageDialog(null, "Commentaire supprimé !");
        ShowCommentaires();
    }

    @FXML
    private void modifierComm(ActionEvent event) throws IOException {
        // Commentaire comtest = new Commentaire(0,null, 0, 0);
        if (Comn == null) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un commentaire à modifier !");
            return;
        }

        String contenue = tfidContenue.getText();
        if (contenue.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir le champ de contenu !");
            return;
        }

        if (containsBadWords(contenue)) {
            JOptionPane.showMessageDialog(null, "Le contenu du commentaire contient des mots interdits !");
            return;
        }

        Comn.setContenue(contenue);
        Comn.setIdCommentaire(idC);
        Comn.setIdBlog(idb);
        Comn.setIdUtilisateur(idu);
        com.modifier(Comn);
        JOptionPane.showMessageDialog(null, "Commentaire modifié");
        ShowCommentaires();

    }
//
//    public TableColumn<Commentaire, String> getColBlogDesc() {
//        return colBlogDesc;
//    }
//
//    public void setColBlogDesc(TableColumn<Commentaire, String> colBlogDesc) {
//        this.colBlogDesc = colBlogDesc;
//    }

    @FXML
    private void goToBlogInterface(ActionEvent event) {

        try {

            Parent blogRoot = FXMLLoader.load(getClass().getResource("Blog.fxml"));
            Scene blogScene = new Scene(blogRoot);
            Stage currentStage = (Stage) btnBackToBlog.getScene().getWindow();
            currentStage.setScene(blogScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//
//    public void updateTable() {
//
//        ObservableList<Commentaire> obsCom1 = com.afficherint(idb);
//
//        colContenue.setCellValueFactory(new PropertyValueFactory<>("contenue"));
//        colidUtilisateur.setCellValueFactory(new PropertyValueFactory<>("Nom"));
//        colidBlog.setCellValueFactory(new PropertyValueFactory<>("titre"));
//        colidBlog.setCellValueFactory(new PropertyValueFactory<>("titre"));
//        infocommentaires.setItems(obsCom1);
//
//    }

    public void ShowCommentaires() {

        ServiceCommentaire sc = new ServiceCommentaire();

        ObservableList<Commentaire> showComm = sc.afficherint(idb);

        System.out.println(idb);
        System.out.println("test " + sc.afficherint(idb));
        infocommentaires.setItems(showComm);
        colContenue.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        colidUtilisateur.setCellValueFactory(new PropertyValueFactory<>("Nom"));

    }

    @FXML
    private void renderComBlog(MouseEvent event) {
        Comn = infocommentaires.getSelectionModel().getSelectedItem();
        System.out.println(Comn);
        index = infocommentaires.getSelectionModel().getSelectedIndex();
        idb = Comn.getIdBlog();
        System.out.println("id b = " + idb);
        idC = Comn.getIdCommentaire();
        idu = Comn.getIdUtilisateur();
        System.out.println("idc est " + idC);
        System.out.println(idb);

        ObservableList<Commentaire> obsCom1 = com.afficherint(idb);
        comboUtilisateur.getSelectionModel().select(comboUtilisateur.getValue());
        comboUtilisateur.setValue(colidUtilisateur.getCellData(index));
        //blogCombo.setValue(colidBlog.getCellData(index));
        tfidContenue.setText(colContenue.getCellData(index));
        infocommentaires.setItems(obsCom1);
        colidUtilisateur.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        //colidBlog.setCellValueFactory(new PropertyValueFactory<>("titre"));
        //colBlogDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colContenue.setCellValueFactory(new PropertyValueFactory<>("contenue"));
        ShowCommentaires();

    }

    public TextField getTfidContenue() {
        return tfidContenue;
    }

    public void setTfidContenue(TextField tfidContenue) {
        this.tfidContenue = tfidContenue;
    }

    public TextField getTfidUtilisateur() {
        return tfidUtilisateur;
    }

    public void setTfidUtilisateur(TextField tfidUtilisateur) {
        this.tfidUtilisateur = tfidUtilisateur;
    }

    public ComboBox<String> getBlogCombo() {
        return blogCombo;
    }

    public void setBlogCombo(ComboBox<String> blogCombo) {
        this.blogCombo = blogCombo;
    }

    public ComboBox<String> getComboUtilisateur() {
        return comboUtilisateur;
    }

    public void setComboUtilisateur(ComboBox<String> comboUtilisateur) {
        this.comboUtilisateur = comboUtilisateur;
    }

    public TableView getInfocomm() {
        return this.infocommentaires;
    }

    public TableColumn<Commentaire, String> getColidUtilisateur() {
        return colidUtilisateur;
    }

    public void setColidUtilisateur(TableColumn<Commentaire, String> colidUtilisateur) {
        this.colidUtilisateur = colidUtilisateur;
    }

    public TableColumn<Commentaire, String> getColidBlog() {
        return colidBlog;
    }

    public void setColidBlog(TableColumn<Commentaire, String> colidBlog) {
        this.colidBlog = colidBlog;
    }

    public TableColumn<Commentaire, String> getColContenue() {
        return colContenue;
    }

    public void setColContenue(TableColumn<Commentaire, String> colContenue) {
        this.colContenue = colContenue;
    }

}
