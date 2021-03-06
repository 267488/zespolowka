/*
 * Copyright 2016 radon
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
package pz.twojaszkola.szkola;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.gallerySchool.GallerySchoolEntity;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.user.User;

/**
 *
 * @author radon
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SzkolaCmd {

    @NotNull
    private Integer id;

    @NotBlank 
    @Size(max = 255)
    private String name;

    //@NotBlank
    private Integer numer;

    @NotBlank 
    @Size(max = 255)
    private String miasto;

    @NotBlank
    @Size(max = 255)
    private String adres;

    @NotBlank 
    @Size(max = 6)
    private String kodpocztowy;
    
    @NotBlank
    private String typSzkoly;
    
    @NotBlank
    private String rodzajSzkoly;
    
    @NotBlank
    private String rodzajGwiazdki;
    
    private User userId; 

    private GallerySchoolEntity galleryId;
    
    
    public Integer getId() {
        return id;
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

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public GallerySchoolEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GallerySchoolEntity galleryId) {
        this.galleryId = galleryId;
    }
    
}
