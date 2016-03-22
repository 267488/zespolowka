package com.radon.helloworld.pojo;
// Generated 2016-03-08 10:50:28 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;




/**
 * Uczen generated by hbm2java
 */
@Entity
public class Uczen {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
     private int id;
     private String imie;
     private String nazwisko;

    public Uczen() {
    }
    
    public Uczen(String imie, String nazwisko) {
       this.imie = imie;
       this.nazwisko = nazwisko;
    }
   
    @Column(name="id")
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="imie")
    public String getImie() {
        return imie;
    }
    
    public void setImie(String imie) {
        this.imie = imie;
    }
    
    @Column(name="nazwisko")
    public String getNazwisko() {
        return nazwisko;
    }
    
    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }
}
