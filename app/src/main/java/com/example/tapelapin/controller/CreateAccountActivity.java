package com.example.tapelapin.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tapelapin.R;

import java.sql.SQLException;

import com.example.tapelapin.controller.model.DatabaseConnection;

public class CreateAccountActivity extends AppCompatActivity {
    private static DatabaseConnection bd;

    private EditText nom;
    private EditText email;
    private EditText password;
    private TextView login;
    private TextView msg;

    private Button  button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        nom = findViewById(R.id.nom);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        msg = findViewById(R.id.msg);
        button = findViewById(R.id.button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Créer un Intent pour lancer GameActivity
                Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                // Lancer l'activité GameActivity
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().isEmpty() || email.getText().toString().isEmpty() || nom.getText().toString().isEmpty()){
                    msg.setText("Vous devez remplir tous les champs");
                    msg.setVisibility(View.VISIBLE);
                }
                else {
                    //fenetre de chargement
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                    builder.setMessage("Tentative d'inscription...");
                    builder.setCancelable(false);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    try {
                        //creation du compte
                        MainActivity.getDatabaseConnection().createUser(
                                nom.getText().toString(),
                                email.getText().toString(),
                                password.getText().toString()
                        );
                        if(MainActivity.getUser() !=null){
                            // Créer un Intent pour lancer GameActivity
                            Intent intent = new Intent(CreateAccountActivity.this, AccueilActivity.class);
                            // Lancer l'activité GameActivity
                            startActivity(intent);
                            //ferme l'activité en cours
                            finish();
                        }
                    } catch (SQLException e) {
                        //si il y a une erreur sql c'est que on violé la contrainte unique du email alors on affiche un message d'erreur
                        msg.setText("Email déjà enregistré, connectez vous !");
                        msg.setVisibility(View.VISIBLE);
                        email.setFocusable(true);
                        dialog.hide();
                    }
                }

            }
        });
    }

}