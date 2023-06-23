/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.esprit.entities.Commentaire;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author User
 */

public class ServiceCommentaire implements IService<Commentaire> {

    private Connection cnx = DataSource.getInstance().getCnx();

    public void ajouter1(Commentaire c) {
        try {
            String req = "INSERT INTO Commentaire (contenue,idUtilisateur,idBlog) VALUES (?,?,?);";
            PreparedStatement pst = cnx.prepareStatement(req);

            pst.setString(1, c.getContenue());
            pst.setInt(2, c.getIdUtilisateur());
            pst.setInt(3, c.getIdBlog());
            pst.executeUpdate();
            System.out.println("Comentaire ajouté !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void modifier(Commentaire c) {
        try {
            String req = "UPDATE Commentaire SET contenue=? WHERE idCommentaire=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(1, c.getContenue());
            pst.setInt(2, c.getIdCommentaire());
            pst.executeUpdate();
            System.out.println("Commentaire modifié !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public ObservableList<Commentaire> afficher() {
        ObservableList<Commentaire> comList = FXCollections.observableArrayList();

        String req = "SELECT * FROM Commentaire ";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs1 = pst.executeQuery();
            while (rs1.next()) {
                comList.add(new Commentaire(rs1.getInt("idCommentaire"), rs1.getString("contenue"), rs1.getInt("idUtilisateur"), rs1.getInt("idBlog")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return comList;
    }

    public ObservableList<Commentaire> afficherint(int id) {
        ObservableList<Commentaire> comList = FXCollections.observableArrayList();

        String req = "SELECT commentaire.contenue,commentaire.idCommentaire,blog.titre,blog.description,utilisateur.Nom,utilisateur.id,"
                + "blog.idBlog from commentaire join blog on commentaire.idBlog=blog.idBlog join utilisateur on blog.idUtilisateur=utilisateur.id where commentaire.idBlog = ?;";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs1 = pst.executeQuery();
            while (rs1.next()) {
                comList.add(new Commentaire(rs1.getInt("commentaire.idCommentaire"), rs1.getString("commentaire.contenue"), rs1.getInt("utilisateur.id"), rs1.getInt("blog.idBlog"), rs1.getString("utilisateur.nom"), rs1.getString("blog.description")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return comList;
    }

    public List<Commentaire> getbyidComm(int id) {

        String req = "SELECT * FROM Commentaire where idBlog=?";
        List<Commentaire> comList = new ArrayList();
        try {

            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs1 = pst.executeQuery();
            while (rs1.next()) {
                comList.add(new Commentaire(rs1.getString("contenue"), rs1.getInt("idUtilisateur"), rs1.getInt("idBlog")));
            }
            System.out.println("service com et comlist" + comList);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return comList;

    }

    @Override
    public void supprimer(Commentaire c) {
        try {
            String req = "DELETE from Commentaire  WHERE idCommentaire=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, c.getIdCommentaire());
            pst.executeUpdate();
            System.out.println("Commentaire supprimé !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
