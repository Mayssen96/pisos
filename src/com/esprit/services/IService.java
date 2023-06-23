/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esprit.services;

import com.esprit.entities.Blog;
import java.util.List;
import javafx.collections.ObservableList;


/**Personne
 *
 * @author User
 */
public interface IService <T>{
    public void ajouter1(T p);
    public void modifier(T p);
    public void supprimer(T p);
    public List <T> afficher();
}