/*
 * Copyright 2016 michael-simons.eu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pz.twojaszkola.user;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import pz.twojaszkola.gallerySchool.GallerySchoolEntity;

/**
 *
 * @author radon
 */
public class SuperSzkola {

    private int id;
    private String login;
    private String password;
    private String mail;
    private String name;
    private Integer numer;
    private String miasto;
    private String adres;
    private String kodpocztowy;
    private String typSzkoly;
    private String rodzajSzkoly;
    private String rodzajGwiazdki;
    private User user;
    private GallerySchoolEntity galleryId;

    public SuperSzkola(int id, String login, String password, String mail, String name, Integer numer, String miasto, String adres, String kodpocztowy, String typSzkoly, String rodzajSzkoly, String rodzajGwiazdki, User user, GallerySchoolEntity galleryId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.mail = mail;
        this.name = name;
        this.numer = numer;
        this.miasto = miasto;
        this.adres = adres;
        this.kodpocztowy = kodpocztowy;
        this.typSzkoly = typSzkoly;
        this.rodzajSzkoly = rodzajSzkoly;
        this.rodzajGwiazdki = rodzajGwiazdki;
        this.user = user;
        this.galleryId = galleryId;
    }

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumer() {
        return numer;
    }

    public void setNumer(Integer numer) {
        this.numer = numer;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }

    public String getTypSzkoly() {
        return typSzkoly;
    }

    public void setTypSzkoly(String typSzkoly) {
        this.typSzkoly = typSzkoly;
    }

    public String getRodzajSzkoly() {
        return rodzajSzkoly;
    }

    public void setRodzajSzkoly(String rodzajSzkoly) {
        this.rodzajSzkoly = rodzajSzkoly;
    }

    public String getRodzajGwiazdki() {
        return rodzajGwiazdki;
    }

    public void setRodzajGwiazdki(String rodzajGwiazdki) {
        this.rodzajGwiazdki = rodzajGwiazdki;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userId) {
        this.user = userId;
    }

    public GallerySchoolEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GallerySchoolEntity galleryId) {
        this.galleryId = galleryId;
    }

}
