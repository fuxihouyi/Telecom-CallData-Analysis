package com.abner.ct.web.bean;

public class Intimacy {

    private String contact_id1;
    private String contact_id2;
    private double intimacy;
    private int rank;
    private String name;
    private int call_count;
    private String call_duration_count;

    public int getCall_count() {
        return call_count;
    }

    public void setCall_count(int call_count) {
        this.call_count = call_count;
    }

    public String getCall_duration_count() {
        return call_duration_count;
    }

    public void setCall_duration_count(String call_duration_count) {
        this.call_duration_count = call_duration_count;
    }

    public Intimacy(String contact_id1, String contact_id2, double intimacy, int rank, String name, int call_count, String call_duration_count) {
        this.contact_id1 = contact_id1;
        this.contact_id2 = contact_id2;
        this.intimacy = intimacy;
        this.rank = rank;
        this.name = name;
        this.call_count = call_count;
        this.call_duration_count = call_duration_count;
    }

    public Intimacy() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact_id1() {
        return contact_id1;
    }

    public void setContact_id1(String contact_id1) {
        this.contact_id1 = contact_id1;
    }

    public String getContact_id2() {
        return contact_id2;
    }

    public void setContact_id2(String contact_id2) {
        this.contact_id2 = contact_id2;
    }

    public double getIntimacy() {
        return intimacy;
    }

    public void setIntimacy(double intimacy) {
        this.intimacy = intimacy;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}


