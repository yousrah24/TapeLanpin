package com.example.tapelapin.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tapelapin.R;
import com.example.tapelapin.databinding.ActivityGameBinding;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    List<Button> buttons = new ArrayList<>();
    int score = 0;
    int taupe = 0;
    int manche = 1;
    //position du lapin
    int pos;
    ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.image.setImageDrawable(getDrawable(R.drawable.tape));

        buttons.add(binding.b1);
        buttons.add(binding.b2);
        buttons.add(binding.b3);
        buttons.add(binding.b4);
        buttons.add(binding.b5);
        buttons.add(binding.b6);
        buttons.add(binding.b7);
        buttons.add(binding.b8);
        buttons.add(binding.b9);

        bougeLeLapin();
        for (Button b: buttons) {
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reagirClic(view);
                }
            });
        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setTitle("Confirmation de l'abandon")
                        .setMessage("Etat de la partie :\n" + score + " lapin trouvé sur 5\nScore à " + score *50 + " points\n\n Voulez vous vraiment andandonner cette partie ? ")
                        .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    MainActivity.getUser().setScoreBD(score * 50);
                                    // Créer un Intent pour lancer AccueilActivity
                                    Intent intent = new Intent(GameActivity.this, AccueilActivity.class);
                                    // Lancer l'activité AccueilActivity

                                    startActivity(intent);
                                    //Fermer l'activité en cours
                                    finish();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    /**
     * @brief verfie si un bouton clique est le lapin
     * @param view
     */
    private void reagirClic(View view){
        if(view.getId() == buttons.get(pos).getId()){
            score++;
            binding.image.setImageDrawable(getDrawable(R.drawable.win));
            Log.i("TAPELAPIN","Bravo!! tape sur le lapin");
            bougeLeLapin();
        }else{
            taupe++;
            Log.i("TAPELAPIN","Ouch!! tape sur une taupe");
            binding.image.setImageDrawable(getDrawable(R.drawable.taupe));

        }
        //mise à jour de l'etat du jeux
        binding.tape.setText("Nombre de clics réussie : " +score);
        binding.rate.setText("Nombre de clics ratés : " +taupe);
        binding.manche.setText("Nombre de manche : " + manche + " sur 5");

        if(manche > 5){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Fin de jeu")
                    .setMessage("Au totale vous avez trouvé  "+ score + " lapin sur 5" + "\nVotre score est de " + score *50 + " points")
                    .setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {
                                //ajout du score de la partie joué
                                MainActivity.getUser().setScoreBD(score * 50);
                                // Créer un Intent pour lancer AccueilActivity
                                Intent intent = new Intent(GameActivity.this, AccueilActivity.class);
                                // Lancer l'activité AccueilActivity
                                startActivity(intent);
                                //Fermer l'activité en cours
                                finish();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }
        manche++;
    }

    /**
     * @brief choisie  au hasard le bouton qui sera le lapin
     */
    private void bougeLeLapin(){
        for (Button b: buttons) {
            b.setText("Attraper");
        }
        Random rand = new Random();
        int pos = rand.nextInt(buttons.size()-1);
    }

}