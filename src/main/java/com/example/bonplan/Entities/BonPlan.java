package com.example.bonplan.Entities;

public class BonPlan {
    private long id;
    private String nom_bp;
    private String img;
    private String categorie_bp;
    private String tel_bp;
    private String desc_bp;
    private String location;
    private long user_id;
    private String ouverture;
    private String  fermuture;
    private float rate_moy;


    public BonPlan() {
        // Constructeur par défaut
    }

    public BonPlan(long id, String nom_bp, String img, String categorie_bp, String tel_bp, String desc_bp, String location, long user_id, String ouverture, String fermuture, float rate_moy) {
        this.id = id;
        this.nom_bp = nom_bp;
        this.img = img;
        this.categorie_bp = categorie_bp;
        this.tel_bp = tel_bp;
        this.desc_bp = desc_bp;
        this.location = location;
        this.user_id = user_id;
        this.ouverture = ouverture;
        this.fermuture = fermuture;
        this.rate_moy = rate_moy;
    }

    // Getters et setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom_bp() {
        return nom_bp;
    }

    public void setNom_bp(String nom_bp) {
        this.nom_bp = nom_bp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategorie_bp() {
        return categorie_bp;
    }

    public void setCategorie_bp(String categorie_bp) {
        this.categorie_bp = categorie_bp;
    }

    public String getTel_bp() {
        return tel_bp;
    }

    public void setTel_bp(String tel_bp) {
        this.tel_bp = tel_bp;
    }

    public String getDesc_bp() {
        return desc_bp;
    }

    public void setDesc_bp(String desc_bp) {
        this.desc_bp = desc_bp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }


    public String getOuverture() {
        return ouverture;
    }

    public void setOuverture(String ouverture) {
        this.ouverture = ouverture;
    }

    public String getFermeture() {
        return  fermuture;
    }

    public void setFermeture(String fermeture) {
        this.fermuture = fermeture;
    }

    public float getRate_moy() {
        return rate_moy;
    }

    public void setRate_moy(float rate_moy) {
        this.rate_moy = rate_moy;
    }

    @Override
    public String toString() {
        return "BonPlan{" +
                "id=" + id +
                ", nom_bp='" + nom_bp + '\'' +
                ", img='" + img + '\'' +
                ", categorie_bp='" + categorie_bp + '\'' +
                ", tel_bp='" + tel_bp + '\'' +
                ", desc_bp='" + desc_bp + '\'' +
                ", location='" + location + '\'' +
                ", user_id=" + user_id +
                ", ouverture='" + ouverture + '\'' +
                ", fermeture='" + fermuture + '\'' +
                ", rate_moy=" + rate_moy +
                '}';
    }
}
