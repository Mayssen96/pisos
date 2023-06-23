/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.esprit.entities.Blog;
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
public class ServiceBlog implements IService <Blog> {
    
    private Connection cnx = DataSource.getInstance().getCnx();
    
    @Override
  
        public void ajouter1(Blog b) {
        try {
            String req = "INSERT INTO Blog(idUtilisateur,titre,description)VALUES(?,?,?);";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(2, b.getTitre());
            pst.setString(3, b.getDescription());
            pst.setInt(1, b.getIdUtilisateur());
            pst.executeUpdate();
            System.out.println("Blog ajouté !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
        
    public void modifier(Blog b) {
        try {
            String req = "UPDATE Blog SET idUtilisateur=?, titre=? ,description=? WHERE idBlog=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setString(2, b.getTitre() );
            pst.setString(3, b.getDescription());
            pst.setInt(1,b.getIdUtilisateur());
            pst.setInt(4,b.getIdBlog());
            pst.executeUpdate();
            System.out.println("Blog modifié !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public ObservableList<Blog> afficher(){
        ObservableList <Blog> bloglist = FXCollections.observableArrayList();
       // List<Blog> list = new ArrayList<>();
        String req = "SELECT idBlog,idUtilisateur,titre, Nom,Prenom,description FROM blog JOIN utilisateur on idUtilisateur=id; ";
        try {
            PreparedStatement pst = cnx.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                //list.add(new Blog (rs.getInt("idUtilisateur"), rs.getString("titre"), rs.getString("description")));
                bloglist.add(new Blog (rs.getInt("idBlog"),rs.getInt("idUtilisateur"), rs.getString("titre"),rs.getString("Prenom")+' '+rs.getString("Nom"), rs.getString("description")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         //return list;
         return bloglist ;
    }
    

    public ObservableList afficherOnly1(int id) {
        ObservableList<Blog> bloglist1 = FXCollections.observableArrayList();
        String req = "select * from Blog WHERE idBlog=?";
        try {

            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                bloglist1.add(new Blog(rs.getInt("idBlog"), rs.getInt("idUtilisateur"), rs.getString("titre"), rs.getString("description")));
            }

            System.out.println("Blog sélectionné !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return bloglist1;

    }
      public void supprimer(Blog b) {
        try {
            String req = "DELETE from Blog WHERE idblog=?";
            PreparedStatement pst = cnx.prepareStatement(req);
            pst.setInt(1, b.getIdBlog() );
            pst.executeUpdate();
            System.out.println("Blog supprimé !");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    } 
     
          
   public boolean isTitreExists(String titre) {
    String query = "SELECT COUNT(*) FROM Blog WHERE titre = ?";
    try (PreparedStatement statement = cnx.prepareStatement(query)) {
        statement.setString(1, titre);
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error checking if titre exists: " + e.getMessage());
    }
    return false;
}
   
}