package com.example.bonplan.Entities;

import java.sql.Timestamp;

public class Ratings {
    private long id;
    private float rate_bp;
    private String comment_bp;
    private long userId;
    private long bon_plan_id;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Ratings(){

    }

    // Constructeur avec les paramètres nécessaires
    public Ratings(long id, float rate_bp, String comment_bp, long userId, long bon_plan_id, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.rate_bp = rate_bp;
        this.comment_bp = comment_bp;
        this.userId = userId;
        this.bon_plan_id = bon_plan_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    // Constructeur alternatif sans les horodatages
    public Ratings(long id, float rate_bp, String comment_bp, long userId, long bon_plan_id) {
        this(id, rate_bp, comment_bp, userId, bon_plan_id, null, null);
    }

    // Constructeur sans ID ni horodatages
    public Ratings(float rate_bp, String comment_bp, long userId, long bon_plan_id) {
        this(0, rate_bp, comment_bp, userId, bon_plan_id);
    }

    // Getters et setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getRate_bp() {
        return rate_bp;
    }

    public void setRate_bp(float rate_bp) {
        this.rate_bp = rate_bp;
    }

    public String getComment_bp() {
        return comment_bp;
    }

    public void setComment_bp(String comment_bp) {
        this.comment_bp = comment_bp;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBonPlanId() {
        return bon_plan_id;
    }

    public void setBonPlanId(long bonPlanId) {
        this.bon_plan_id = bonPlanId;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}