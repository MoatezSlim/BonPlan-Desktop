package com.example.bonplan.Entities;

public class Menu {
    private long id;
    private String nom;
    private long bonPlanId;

    public Menu(String nom, long bonPlanId) {
        this.nom = nom;
        this.bonPlanId = bonPlanId;
    }
    public Menu() {
    }

    // Getters et setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getBonPlanId() {
        return bonPlanId;
    }

    public void setBonPlanId(long bonPlanId) {
        this.bonPlanId = bonPlanId;
    }
}
