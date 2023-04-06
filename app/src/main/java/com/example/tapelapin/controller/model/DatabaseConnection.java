package com.example.tapelapin.controller.model;


import com.example.tapelapin.controller.MainActivity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class DatabaseConnection {
    //url d'accés de la base de donnée
    private String url;

    // utilistaeur de la base donnée
    private String user;

    // mot de passe de la base donnée
    private String password;

    // uconnexion de la base donnée
    private Connection connection;

    public DatabaseConnection(String url,String user, String password) throws SQLException, ClassNotFoundException {
        //Drriver MySQL
        Class.forName("com.mysql.jdbc.Driver");
        //initialisation de la connexion à la base de donnée
        if(connection != null) return;
        connection = DriverManager.getConnection(url,user,password);

    }

    /**
     * @brief renvoie la connexion de la base de donnée
     * @return la connexion
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @brief verfier si un joueur existe dans la base de donnée
     * @param email
     * @param password
     * @return le joueur connecté si il a été trouvé dans la database ou null dans le cas contraire
     * @throws SQLException
     */
    public User verif(String email, String password) throws SQLException {
        //requete de recherche du joueur
        PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM joueurs WHERE email = ? AND mot_de_passe = ?");
        stmt.setString(1,email);
        stmt.setString(2,password);
        ResultSet res = stmt.executeQuery();
        User user = null;
        //si la requete renvoie une ligne alors on cree un nouvel utilisateur
        if(res.next()){
            Date dateInscr = res.getDate("date_inscription");
            user = new User(
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("email"),
                    dateInscr
            );
            //on recupere tous l'historique des score du joueur
            stmt = connection.prepareStatement("SELECT score FROM scores WHERE joueur_id = ?");
            stmt.setInt(1,user.getId());
            res = stmt.executeQuery();
            while(res.next()){
                user.setScore(res.getInt(1));
            }

        }
        return user;
    }


    /**
     * @brief deconnexion de la base de donnée
     * @throws SQLException
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = null;
    }

    /**
     * @brief donne la liste des scores enregistre dans la base de donnée -> le classement des joueurs
     * @return liste de des joueurs ayant deja joué
     * @throws SQLException
     */
    public List<Person> getScores() throws SQLException {
        List<Person> persons = new ArrayList<>();
        Statement stmt = getConnection().createStatement();
        //exectution de la procedure qui
        ResultSet res = stmt.executeQuery("CALL get_classement_joueurs()");
        //tant que il y a des lignes dans le resultat de la requete, recuperer les donnée
        while(res.next()){
            //ajout d'un joueur avec son nom et son score totale
            persons.add(new Person(res.getString(1), res.getInt(2)));
        }

        return persons;
    }

    /**
     * @brief insertion du score de la dernière partie joué dans la base de donnée
     * @param id
     * @param score
     * @throws SQLException
     */
    public void insert(int id, int score) throws SQLException {
        //requete d'insertion du score
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO scores(joueur_id,score) VALUES (?,?)");
        stmt.setInt(1,id);
        stmt.setInt(2,score);
        stmt.execute();
    }

    /**
     * @brief creer un nouveau utilisateur et le recupere dans la base de donnée
     * @param nom
     * @param email
     * @param password
     * @throws SQLException
     */
    public void createUser(String nom, String email, String password) throws SQLException {
        //requete d'insertion du nouveau joueur
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO joueurs (nom, email, mot_de_passe) VALUES (?,?,?)");
        stmt.setString(1, nom);
        stmt.setString(2, email);
        stmt.setString(3, password);
        stmt.execute();
        // requete de recuperation du joueur selon son email et mot de passe
        stmt = connection.prepareStatement("SELECT * FROM joueurs WHERE email = ? AND mot_de_passe = ?");
        stmt.setString(1,email);
        stmt.setString(2,password);
        ResultSet res = stmt.executeQuery();
        //si la requete renvoie une ligne alors on recupere les données
        if(res.next()) {
            //recuperation de la date
            Date dateInscr = res.getDate("date_inscription");
            //creation du joueur
            User user = new User(
                    res.getInt("id"),
                    res.getString("nom"),
                    res.getString("email"),
                    dateInscr
            );
            //initilisation du joueur connecte dans l'activité principale
            MainActivity.setUser(user);
        }
    }
}
