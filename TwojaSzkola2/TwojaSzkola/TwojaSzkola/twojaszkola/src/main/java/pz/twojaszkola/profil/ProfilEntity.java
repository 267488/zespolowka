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
package pz.twojaszkola.profil;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author radon
 */
@Entity
@Table(name = "profil")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ProfilEntity implements Serializable {

    private static final long serialVersionUID = 001L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "profil_nazwa_id")
    private Profil_nazwaEntity profilNazwa;        

    @ManyToOne
    @JoinColumn(name = "szkola_id")
    private SzkolaEntity szkola;   
    
    
    protected ProfilEntity() {
    }

    
    
    public ProfilEntity(Profil_nazwaEntity profil_nazwa, SzkolaEntity szkola) {
        this.profilNazwa = profil_nazwa;
        this.szkola = szkola;
    }

    
    public Profil_nazwaEntity getProfil_nazwa() {
        return profilNazwa;
    }

    public void setProfil_nazwa(Profil_nazwaEntity profil_nazwa) {
        this.profilNazwa = profil_nazwa;
    }
            
            
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SzkolaEntity getSzkola() {
        return szkola;
    }

    public void setSzkola(SzkolaEntity szkola) {
        this.szkola = szkola;
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
        if (!(object instanceof ProfilEntity)) {
            return false;
        }
        ProfilEntity other = (ProfilEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
   
}
