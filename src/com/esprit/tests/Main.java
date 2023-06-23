package com.esprit.tests;

import com.esprit.entities.Blog;
import com.esprit.services.ServiceBlog;
// /* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//  Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
public class Main {  
//     // @param args the command line arguments  
    public static void main(String[] args) {
         
        ServiceBlog sp2 = new ServiceBlog();
        sp2.ajouter1(new Blog(1,"xxxxx","xxxxxx"));
        System.out.println(sp2.afficher());
        
    }
    
}
