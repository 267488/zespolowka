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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author radon
 */
@Entity
@Table(name = "Szkola")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class SzkolaEntity implements Serializable {

    private static final long serialVersionUID = 0001L;
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @Size(max = 255)
    @NotBlank
    private String name;

    @Column(name = "numer")
    //@NotBlank
    private Integer numer;
    
    @Column(name = "mail")
    @Size(max = 255)
    @NotBlank
    private String mail;

    @Column(name = "miasto")
    @Size(max = 255)
    @NotBlank
    private String miasto;

    @Column(name = "adres")
    @Size(max = 255)
    @NotBlank
    private String adres;

    @Column(name = "kodpocztowy", length = 6)
    @Size(max = 6)
    @NotBlank
    private String kodpocztowy;
    
    @Column(name = "typSzkoly")
    @NotBlank
    private String typSzkoly;
    
    @Column(name = "rodzajSzkoly")
    @NotBlank
    private String rodzajSzkoly;
    
    @Column(name = "rodzajGwiazdki", nullable = false)
    @NotBlank
    private String rodzajGwiazdki;
    
    protected SzkolaEntity()
    {
        
    }
    
    public SzkolaEntity(String name, Integer numer, String mail, String miasto, String adres, String kodpocztowy, String typSzkoly, String rodzajSzkoly, String rodzajGwiazdki) {
        this.name = name;
        this.numer = numer;
        this.mail = mail;
        this.miasto = miasto;
        this.adres = adres;
        this.kodpocztowy = kodpocztowy;
        this.typSzkoly = typSzkoly;
        this.rodzajSzkoly = rodzajSzkoly;
        this.rodzajGwiazdki = rodzajGwiazdki;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getRodzajGwiazdki() {
        return rodzajGwiazdki;
    }

    public void setRodzajGwiazdki(String rodzajGwiazdki) {
        this.rodzajGwiazdki = rodzajGwiazdki;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SzkolaEntity)) {
            return false;
        }
        SzkolaEntity other = (SzkolaEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
