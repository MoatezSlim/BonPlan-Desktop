package com.example.bonplan.Entities;

public class Offre {
    private long id;
    private String title;
    private String content;
    private long bon_plan_id; // Modification du nom de la propriété

    // Constructeurs
    public Offre() {
    }

    public Offre(long id, String title, String content, long bon_plan_id) { // Modification du nom de la propriété
        this.id = id;
        this.title = title;
        this.content = content;
        this.bon_plan_id = bon_plan_id; // Modification du nom de la propriété
    }

    // Getters et setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getBon_plan_id() { // Modification du nom de la méthode
        return bon_plan_id;
    }

    public void setBon_plan_id(long bon_plan_id) {
        this.bon_plan_id = bon_plan_id;
    }

}
