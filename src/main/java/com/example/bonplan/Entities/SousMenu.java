package com.example.bonplan.Entities;

public class SousMenu {
    private long id;
    private String nom;
    private float prix;
    private long menuId;
    private long bonPlanId;

    public SousMenu(String nom, float prix, long menuId, long bonPlanId) {
        this.nom = nom;
        this.prix = prix;
        this.menuId = menuId;
        this.bonPlanId = bonPlanId;
    }
    public SousMenu() {
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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public long getMenuId() {
        return menuId;
    }

    public void setMenuId(long menuId) {
        this.menuId = menuId;
    }

    public long getBonPlanId() {
        return bonPlanId;
    }

    public void setBonPlanId(long bonPlanId) {
        this.bonPlanId = bonPlanId;
    }
}
