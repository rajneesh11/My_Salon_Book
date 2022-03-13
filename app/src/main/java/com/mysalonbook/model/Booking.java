package com.mysalonbook.model;

public class Booking {
    private int id;
    private String bookedDate;
    private String job;
    private int slot;
    private String bookedOn;
    private String user;
    private int cancelled;
    private int jobServed;

    public Booking(int id, String bookedDate, String job, int slot, String bookedOn, String user, int cancelled, int jobServed) {
        this.id = id;
        this.bookedDate = bookedDate;
        this.job = job;
        this.slot = slot;
        this.bookedOn = bookedOn;
        this.user = user;
        this.cancelled = cancelled;
        this.jobServed = jobServed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(String bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(String bookedOn) {
        this.bookedOn = bookedOn;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getJobServed() {
        return jobServed;
    }

    public void setJobServed(int jobServed) {
        this.jobServed = jobServed;
    }
}
