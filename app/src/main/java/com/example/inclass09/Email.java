package com.example.inclass09;

import java.io.Serializable;

public class Email implements Serializable {
    String subject,created_at,id,message,sender_fname,sender_lname;

    public Email() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_fname() {
        return sender_fname;
    }

    public void setSender_fname(String sender_fname) {
        this.sender_fname = sender_fname;
    }

    public String getSender_lname() {
        return sender_lname;
    }

    public void setSender_lname(String sender_lname) {
        this.sender_lname = sender_lname;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                ", created_at='" + created_at + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", sender_fname='" + sender_fname + '\'' +
                ", sender_lname='" + sender_lname + '\'' +
                '}';
    }
}
