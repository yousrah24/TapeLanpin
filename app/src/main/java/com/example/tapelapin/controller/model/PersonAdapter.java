package com.example.tapelapin.controller.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.tapelapin.R;

import java.util.List;

public class PersonAdapter extends ArrayAdapter<Person> {
    private Context context;
    private List<Person> persons;

    public PersonAdapter(Context context, List<Person> persons) {
        super(context, 0, persons);
        this.context = context;
        this.persons = persons;
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public Person getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.persons_item, parent, false);
        }
        TextView numTextView = convertView.findViewById(R.id.numTextView);
        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        TextView scoreTextView = convertView.findViewById(R.id.scoreTextView);

        Person person = persons.get(position);
        //rang du joueur
        numTextView.setText(String.valueOf(position + 1));
        //nom du joueur
        nameTextView.setText(person.getName());
        //score du joueur
        scoreTextView.setText(String.valueOf(person.getScore()));

        return convertView;
    }
}

