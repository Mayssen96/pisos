/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.esprit.gui;

import com.esprit.entities.Blog;
import com.esprit.entities.Commentaire;
import com.esprit.entities.Rate;
import com.esprit.services.ServiceBlog;
import com.esprit.services.ServiceCommentaire;
import com.esprit.services.ServiceRate;
import com.esprit.utils.DataSource;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javax.swing.JOptionPane;
//import org.controlsfx.control.Rating;
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
//import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.geometry.Insets;

import javafx.scene.layout.HBox;
import org.controlsfx.control.Rating;

/**
 * FXML Controller class
 *
 * @author User
 */
public class BlogController implements Initializable {

    ServiceRate r = new ServiceRate();
    @FXML
    private TextField tfTitre;
    @FXML
    private TextField tfDescription;
    @FXML
    private TableView<Blog> information;
    @FXML
    private TableColumn<Blog, String> colidutilisateur;
    @FXML
    private TableColumn<Blog, String> cotitre;
    @FXML
    private TableColumn<Blog, String> coldesc;
    private int IDb = 0;
    Blog bg = null;
    int index = 0;
    private Connection cnx = DataSource.getInstance().getCnx();
    Map<Integer, String> mapusers = new HashMap();
    ObservableList list = FXCollections.observableArrayList();
    ServiceCommentaire sc = new ServiceCommentaire();
    ServiceBlog sb = new ServiceBlog();
    @FXML
    private ComboBox<String> userBlogCombo;
    @FXML
    private TextField tfSearchBox;
    @FXML
    private Rating Rate;
    @FXML
    private TableColumn<Blog, Void> colAll;
    @FXML
    private AnchorPane rate;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnAjouter;
    @FXML
    private Label labelRate;

    /**
     * Initializes the controller class.
     */
    /* @Override;*/
    private void rateBlog(MouseEvent event) {

        int rating = (int) Rate.getRating();
        int idUtilisateur = bg.getIdUtilisateur();

        int idBlog = bg.getIdBlog();

        Rate r1 = new Rate(idUtilisateur, idBlog, rating);
        r.ajouter1(r1);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thank You");
        alert.setHeaderText(null);
        alert.setContentText("Thank you for rating! You gave " + rating + " stars.");
        alert.showAndWait();
        System.out.println(bg + "done");

    }

    public int getIDb() {
        return IDb;
    }

    public void setIDb(int IDb) {
        this.IDb = IDb;
    }

    public void initialize(URL url, ResourceBundle rb) {
        ShowBlogs();
        Rate.setOnMouseClicked(this::rateBlog);
        Callback<TableColumn<Blog, Void>, TableCell<Blog, Void>> cellFactory = new Callback<TableColumn<Blog, Void>, TableCell<Blog, Void>>() {
            @Override
            public TableCell<Blog, Void> call(final TableColumn<Blog, Void> param) {
                final TableCell<Blog, Void> cell = new TableCell<Blog, Void>() {

                    FontAwesomeIconView ShowRate = new FontAwesomeIconView(FontAwesomeIcon.STAR);
                    FontAwesomeIconView showBlogCom = new FontAwesomeIconView(FontAwesomeIcon.EYE);
                    FontAwesomeIconView showDelet = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                    FontAwesomeIconView edit = new FontAwesomeIconView(FontAwesomeIcon.EDIT);

                    {
                        ShowRate.setStyle(
                                "-fx-cursor: hand;"
                                + "-glyph-size: 20px;"
                                + "-fx-fill: gold;"
                        );

                        ShowRate.setOnMouseClicked((MouseEvent event) -> {
                            Blog data = getTableView().getItems().get(getIndex());

                            try {

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("tableRates.fxml"));
                                Parent root = loader.load();
                                tfTitre.getScene().setRoot(root);
                                TableRatesController trc = loader.getController();

                                ObservableList<Rate> listrate = FXCollections.observableArrayList(r.afficherint(data.getIdBlog()));

                                trc.getColBlogName().setCellValueFactory(new PropertyValueFactory<>("titreBlog"));
                                trc.getColNomUtilisateur().setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));
                                trc.getColRate().setCellValueFactory(new PropertyValueFactory<>("rating"));
                                trc.getTableviewRate().setItems(listrate);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        showBlogCom.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:20px;"
                                + "-fx-fill:#000000;;"
                        );

                        showBlogCom.setOnMouseClicked((MouseEvent event) -> {
                            Blog data = getTableView().getItems().get(getIndex());

                            try {

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("Commentaire.fxml"));
                                Parent root = loader.load();
                                tfTitre.getScene().setRoot(root);
                                CommentaireController tcr = loader.getController();

                                ObservableList<Commentaire> listBlogComm = FXCollections.observableArrayList(sc.afficherint(data.getIdBlog()));

                                tcr.getColContenue().setCellValueFactory(new PropertyValueFactory<>("contenue"));
                                //tcr.getColidUtilisateur().setCellValueFactory(new PropertyValueFactory<>("nom"));
                                tcr.getComboUtilisateur().setValue(data.getNomUtilisateur());
                                tcr.getBlogCombo().setValue(data.getTitre());
                                tcr.getInfocomm().setItems(listBlogComm);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        });

                        showDelet.setStyle(
                                " -fx-cursor: hand ;"
                                + "-glyph-size:20px;"
                                + "-fx-fill:#000000;;"
                                + "-fx-alignment:center"
                        );

                        showDelet.setOnMouseClicked((MouseEvent event) -> {

                            if (JOptionPane.showConfirmDialog(null, getTableView().getItems().get(getIndex()).getTitre(), " are you Sure to delete ?", JOptionPane.YES_NO_CANCEL_OPTION) == 0) {
                                Blog data = getTableView().getItems().get(getIndex());
                                System.out.println("selectedData: " + data);
                                sb.supprimer(data);
                                ShowBlogs();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox actionsbtn = new HBox(showBlogCom, ShowRate, showDelet);
                            actionsbtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(showDelet, new Insets(2, 2, 0, 3));
                            HBox.setMargin(ShowRate, new Insets(2, 3, 0, 2));
                            HBox.setMargin(showBlogCom, new Insets(2, 3, 0, 2));
                            setGraphic(actionsbtn);
                        }
                    }
                };
                return cell;
            }
        };

        colAll.setCellFactory(cellFactory);

        String req = "SELECT * FROM utilisateur ";

        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                mapusers.put(rs.getInt("id"), rs.getString("Prenom") + ' ' + rs.getString("Nom"));
            }
            for (Map.Entry ele : mapusers.entrySet()) {
                list.add(ele.getValue());
            }

            userBlogCombo.setItems(list);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void ShowBlogs() {
        ServiceBlog blog;
        blog = new ServiceBlog();
        ObservableList<Blog> obsBlog = blog.afficher();

        colidutilisateur.setCellValueFactory(new PropertyValueFactory<>("nomUtilisateur"));
        cotitre.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        coldesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        information.setItems(obsBlog);

        FilteredList<Blog> lbf = new FilteredList<>(obsBlog, b -> true);
        tfSearchBox.textProperty().addListener((Observable, oldValue, newValue) -> {
            lbf.setPredicate(bg -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowebg = newValue.toLowerCase();
                if (bg.getTitre().toLowerCase().contains(lowebg)) {
                    return true;
                } else {
                    return false;
                }
            });
            SortedList<Blog> sortedBlog = new SortedList<>(lbf);
            sortedBlog.comparatorProperty().bind(information.comparatorProperty());
            information.setItems(sortedBlog);
        });
    }

    @FXML
    private void modifierBlog(ActionEvent event) {
        String titre = tfTitre.getText();
        String description = tfDescription.getText();

        if (titre.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
            return;
        }

        Blog newB = new Blog(IDb, bg.getIdUtilisateur(), titre, description);
        sb.modifier(newB);
        JOptionPane.showMessageDialog(null, "Blog modifié !");
        ShowBlogs();
    }

    @FXML
    private void ajouterBlog(ActionEvent event) {
        String titre = tfTitre.getText();
        String description = tfDescription.getText();

        if (titre.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs !");
            return;
        }

        for (Map.Entry ele : mapusers.entrySet()) {
            if (ele.getValue().equals(userBlogCombo.getValue())) {
                if (sb.isTitreExists(titre)) {
                    JOptionPane.showMessageDialog(null, "Le titre du blog existe déjà !");
                    return;
                }

                Blog newB = new Blog(Integer.parseInt(ele.getKey().toString()), titre, description);
                sb.ajouter1(newB);
            }
        }

        JOptionPane.showMessageDialog(null, "Blog ajouté !");
        ShowBlogs();
    }

    @FXML
    private void getselectedblog(MouseEvent event) {

        index = information.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;
        }
        System.out.println("voilaa" + information.getSelectionModel().getSelectedItem());
        bg = information.getSelectionModel().getSelectedItem();
        IDb = bg.getIdBlog();
        System.out.println("blogctrl" + IDb);
        tfTitre.setText(cotitre.getCellData(index));
        tfDescription.setText(coldesc.getCellData(index));
    }

}
