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

import pz.twojaszkola.galleryUser.GalleryUserEntity;

/**
 *
 * @author radon
 */
public class SuperUser {

    private String name;
    private String lastname;
    private String mail;
    private String login;
    private String password;
    private Integer uczen_id;
    private String miasto;
    private String kodpocztowy;
    private String adres;
    private String czegoSzukam;
    private GalleryUserEntity galleryId;

    public SuperUser(
            int uczen_id,
            String name,
            String lastname,
            String mail,
            String login,
            String password,
            String miasto,
            String kodpocztowy,
            String adres,
            String czegoSzukam,
            GalleryUserEntity galleryId) {
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.login = login;
        this.password = password;
        this.uczen_id = uczen_id;
        this.miasto = miasto;
        this.kodpocztowy = kodpocztowy;
        this.adres = adres;
        this.czegoSzukam = czegoSzukam;
        this.galleryId = galleryId;
    }
    
    protected SuperUser(){}
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public Integer getUczen_id() {
        return uczen_id;
    }

    public void setUczen_id(Integer uczen_id) {
        this.uczen_id = uczen_id;
    }

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }

    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getMiasto() {
        return miasto;
    }

    public void setMiasto(String miasto) {
        this.miasto = miasto;
    }

    public String getCzegoSzukam() {
        return czegoSzukam;
    }

    public void setCzegoSzukam(String czegoSzukam) {
        this.czegoSzukam = czegoSzukam;
    }

    public GalleryUserEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GalleryUserEntity galleryId) {
        this.galleryId = galleryId;
    }
}
