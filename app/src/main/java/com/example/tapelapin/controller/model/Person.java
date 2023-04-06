package com.example.tapelapin.controller.model;

public class Person implements Comparable<Person>{
    private String name;
    private int score;

    public Person(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * @brief donne le nom d'une personne
     * @return le nom
     */
    public String getName() {
        return name;
    }

    /**
     * @brief donne le score totale d'une personne
     * @return le score totale
     */
    public int getScore() {
        return score;
    }

    /**
     * @brief compare deux personne selon leur score
     * @param p
     * @return un nombre postive ou negative
     */
    @Override
    public int compareTo(Person p) {
        return this.score - p.getScore() == 0 ? name.compareTo(p.getName()) :  this.score - p.getScore();
    }
}

