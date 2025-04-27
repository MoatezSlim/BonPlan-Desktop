package com.example.bonplan.Entities;

public class Favoris {
    private long id;
    private long userId;
    private long bonPlanId;

    public Favoris(long userId, long bonPlanId) {
        this.userId = userId;
        this.bonPlanId = bonPlanId;
    }

    public Favoris() {
    }

    // Getters et setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBonPlanId() {
        return bonPlanId;
    }

    public void setBonPlanId(long bonPlanId) {
        this.bonPlanId = bonPlanId;
    }
}
