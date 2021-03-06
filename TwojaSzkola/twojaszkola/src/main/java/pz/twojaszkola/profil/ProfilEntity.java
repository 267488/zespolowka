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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author radon
 */
@Entity
@Table(name = "profil")
@NamedQueries({
    @NamedQuery(
	    name = "ProfilEntity.findBySzkolaId",
	    query
	    = "Select b from ProfilEntity b "
            + " where b.szkola.id = :idSzkoly"
    ),
    @NamedQuery(
	    name = "ProfilEntity.findByTypSzkoly",
	    query
	    = "Select b from ProfilEntity b "
	    + " where b.szkola.typSzkoly = :typSzkoly"
    ),
    @NamedQuery(
	    name = "ProfilEntity.findSzkolySrednie",
	    query
	    = "Select b from ProfilEntity b "
	    + " where b.szkola.typSzkoly = :s1 "
            + " or b.szkola.typSzkoly = :s2 "
            + " or b.szkola.typSzkoly = :s3 "
    )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ProfilEntity implements Serializable {

    private static final long serialVersionUID = 001L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;      

    @Column(name = "nazwa", length = 255)
    @Size(max = 255)
    private String nazwa;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "szkola_id")
    private SzkolaEntity szkola;   
    
    
    protected ProfilEntity() {
    }

    
    
    public ProfilEntity(String nazwa, SzkolaEntity szkola) {
        this.nazwa = nazwa;
        this.szkola = szkola;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
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
