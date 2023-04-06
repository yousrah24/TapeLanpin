package com.example.tapelapin.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tapelapin.databinding.ActivityAccueilBinding;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import com.example.tapelapin.controller.model.Person;
import com.example.tapelapin.controller.model.PersonAdapter;
import com.example.tapelapin.controller.model.User;

public class AccueilActivity extends AppCompatActivity {

    private ActivityAccueilBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccueilBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        TextView nom = binding.nom;
        User loggedUser = MainActivity.getUser();
        nom.setText(loggedUser.getNom());

        ListView list = binding.liste;

        try {
            //recupération de la liste des joueurs
            List<Person> persons = MainActivity.getDatabaseConnection().getScores();
            //trie des jouerus dans l'ordre croissant
            Collections.sort(persons, Collections.reverseOrder());
            //ajout des joueurs dans la ListView avec le adptater
            list.setAdapter(new PersonAdapter(this, persons));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //affichage du score totale du joueur connecté
        binding.score.setText(String.valueOf(loggedUser.getScore()) + " POINTS");

        //affichage de la date d'incription du joueur
        binding.dateInsr.setText("Membre depuis le "+ MainActivity.getUser().getDateIncr());

        binding.button.setOnClickListener(view -> {
            // Créer un Intent pour lancer GameActivity
            Intent intent = new Intent(AccueilActivity.this, GameActivity.class);
            // Lancer l'activité GameActivity
            startActivity(intent);
            //Fermer l'activité en cours
            finish();
        } );

        binding.button2.setOnClickListener(view -> {
            try {
                //lors de la deconnexion du joueur, la connexion à la base de donnée est fermer
                MainActivity.getDatabaseConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Créer un Intent pour lancer MainActivity
            Intent intent = new Intent(AccueilActivity.this, MainActivity.class);
            // Lancer l'activité MainActivity
            startActivity(intent);
            //Fermer l'activité en cours
            finish();
        } );
    }
}