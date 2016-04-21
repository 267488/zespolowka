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

/**
 *
 * @author radon
 */
public class SuperUser {
    
    private String name;
    private String lastname;
    private String mail;
    private String login;
    private int uczen_id;
    private int user_id;
    private String kodpocztowy;
    private String pesel;

    public SuperUser(String name, String lastname, String mail, String login, int uczen_id, int user_id, String kodpocztowy, String pesel) {
        this.name = name;
        this.lastname = lastname;
        this.mail = mail;
        this.login = login;
        this.uczen_id = uczen_id;
        this.user_id = user_id;
        this.kodpocztowy = kodpocztowy;
        this.pesel = pesel;
    }

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

    public int getUczen_id() {
        return uczen_id;
    }

    public void setUczen_id(int uczen_id) {
        this.uczen_id = uczen_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
    
    
    
}
