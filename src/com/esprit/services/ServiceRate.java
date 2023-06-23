/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.esprit.entities.Rate;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author User
 */
public class ServiceRate implements IService <Rate> {
 
 private Connection cnx = DataSource.getInstance().getCnx();
    @Override
    public void ajouter1(Rate r) {
         try {
            String req = "INSERT INTO ratingstar (utilisateur,idBlog,rating) VALUES (?,?,?);";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(3,r.getRating());
            pst.setInt(2,r.getIdBlog());
            pst.setInt(1,r.getUtilisateur());
            pst.executeUpdate();
            System.out.println("Rate ajouté !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void modifier(Rate p) {
    }

    @Override
    public void supprimer(Rate p) {
    }

    
    public ObservableList<Rate> afficherint( int id) {
      ObservableList <Rate> rateList = FXCollections.observableArrayList();

        String req = "SELECT r.idRate,r.rating, u.Nom, b.titre FROM ratingstar r JOIN utilisateur u ON r.utilisateur = u.id JOIN blog b ON r.idBlog = b.idBlog where b.idBlog =? GROUP BY r.rating, u.Nom, b.titre;";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs1 = pst.executeQuery();
            while(rs1.next()) {
            rateList.add(new Rate (rs1.getInt("r.rating"),rs1.getString("u.Nom"), rs1.getString("b.titre")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         return rateList;
    }

    @Override
    public ObservableList<Rate> afficher() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}