package com.example.tapelapin.controller.model;

import com.example.tapelapin.controller.MainActivity;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String nom;
    private String email;
    private Date dateInscr;
    private List<Integer> scores = new ArrayList<>();


    public User(int id, String nom, String email, Date dateInsr) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.dateInscr = dateInsr;
    }

    /**
     * @brief renvoie le nom du joueur
     * @return le nom
     */
    public String getNom(){return nom;}

    /**
     * @brief ajoute le score de la dernière partie joué
     * @param score
     */
    public void setScore(int score){
        if(score > 0)
            scores.add(score);
    }

    /**
     * @brief ajoute le score de la dernière partie joué et l'ajoute dans la base de donnée
     * @param score
     * @throws SQLException
     */
    public void setScoreBD(int score) throws SQLException {
        if(score > 0){
            scores.add(score);
            //recuperation de la base de donnée et ajout de score
            MainActivity.getDatabaseConnection().insert(id, score);
        }

    }

    /**
     * @brief renvoie le score totale d'un joueur
     * @return le score totale
     */
    public int getScore() {
        int n = 0;
        for (int d: scores) {
            n+= d;
        }
        return n;
    }

    /**
     * @breif renvoie l'indentifiant d'un joueur
     * @return l'identifiant
     */
    public int getId() {
        return id;
    }

    /**
     * @brief renvoie la date d'inscription du joueur
     * @return la date
     */
    public String getDateIncr() {
        return dateInscr.toString();
    }
}
