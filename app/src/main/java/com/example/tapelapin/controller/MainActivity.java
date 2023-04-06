package com.example.tapelapin.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tapelapin.R;

import java.sql.SQLException;

import com.example.tapelapin.controller.model.DatabaseConnection;

import com.example.tapelapin.controller.model.User;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button button;

    private TextView msg;

    private TextView register;
    private static DatabaseConnection bd;
    private static User loggedUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.button);

        register = findViewById(R.id.register);
        msg = findViewById(R.id.msg);

        //permission d'execution de thread en paralléle
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        //au clic sur le textView de "creer compte", on lance l'activite de creation de compte
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Créer un Intent pour lancer CreateAccountActivity
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                // Lancer l'activité CreateAccountActivity
                startActivity(intent);
            }
        });

        setDatabaseConnection();
        // action à faire au clic du bouton de connexion
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //verfication que tous les champs input sont remplis
                if(password.getText().toString().isEmpty() || email.getText().toString().isEmpty()){
                    msg.setText("Vous devez remplir tous les champs");
                    msg.setVisibility(View.VISIBLE);
                }
                else{
                    try {
                        //affichage de la fenetre de chargement
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Tentative de Connexion...");
                        builder.setCancelable(false);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //verfication si l'email et mot de passe sont correct dans la base de donnée
                        loggedUser = bd.verif(email.getText().toString(), password.getText().toString());
                        if(loggedUser != null){
                            // Créer un Intent pour lancer GameActivity
                            Intent intent = new Intent(MainActivity.this, AccueilActivity.class);
                            // Lancer l'activité GameActivity
                            startActivity(intent);
                            //cacher la fentre de chargement
                            dialog.hide();
                        }
                        else {
                            //si l'email ou le mot de passe ne sont pas dans la base de donné
                            //affichage du message d'erreur
                            password.setText("");
                            msg.setText("ERREUR : email ou mot de passe incorrect");
                            msg.setVisibility(View.VISIBLE);
                            //met le curseur sur l'input de mot de passe
                            password.setFocusable(true);
                            //cacher la fentre de chargement
                            dialog.hide();
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
    }

    /**
     * @brief connexion à la base de donnée MySQL
     */
    private void setDatabaseConnection() {
        try {
            String url = "jdbc:mysql://192.168.193.172:3306/taplapin", user = "android2", mdp = "root";
            //connexion à la base de donnee MySQL
            bd = new DatabaseConnection(url, user, mdp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @brief renvoie la connexion de base de donnée
     * @return la connexion de base de donnée
     */
    public static DatabaseConnection getDatabaseConnection() {
        return bd;
    }

    /**
     * @brief renvoie le joueur connecté
     * @return le joueur connecté
     */
    public static User getUser() {
        return loggedUser;
    }

    /**
     * @brief initialise le joueur connecté
     * @param user
     */
    public static void setUser(User user) {
        MainActivity.loggedUser = user;
    }


}
