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
package pz.twojaszkola.szkola;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    @NotBlank //niepusty
    @Size(max = 255)
    private String name;

    @Column(name = "mail", length = 255, nullable = false)
    @NotBlank //niepusty
    @Size(max = 255)
    private String mail;

    @Column(name = "password", length = 255, nullable = false)
    @NotBlank //niepusty
    @Size(max = 255)
    private String password;

    @Column(name = "adres", length = 255, nullable = false)
    @NotBlank //niepusty
    @Size(max = 255)
    private String adres;

    @Column(name = "kodpocztowy", length = 6, nullable = false)
    @NotBlank //niepusty
    @Size(max = 6)
    private String kodpocztowy;
    
    protected SzkolaEntity()
    {
        
    }
    
    public SzkolaEntity(String name, String mail, String password, String adres, String kodpocztowy) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.adres = adres;
        this.kodpocztowy = kodpocztowy;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
