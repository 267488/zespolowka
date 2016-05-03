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
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyEntity;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author radon
 */
@Entity
@Table(name = "profil")
@NamedQueries({
    @NamedQuery(
	    name = "ProfilEntity.findByPrzedmiotNazwaAndSzkola",
	    query
	    = "Select b from ProfilEntity b "
	    + " where b.nazwa = :nazwaPrzedmiotu"
            + " and b.szkola.id = :idSzkoly"
    ),
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

    @Column(name = "nazwa", unique = true, length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String nazwa;        
    
    @ManyToOne
    @JoinColumn(name = "szkola_id")
    private SzkolaEntity szkola;   
    
    @OneToMany(mappedBy = "profilId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<RozszerzonePrzedmiotyEntity> rozszerzonePrzedmioty = new ArrayList<>();
    
    @OneToMany(mappedBy = "profilId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<OcenaPrzedmiotuEntity> ocenyPrzedmiotow = new ArrayList<>();
    
    
    protected ProfilEntity() {
    }

    
    
    public ProfilEntity(String profil_nazwa, SzkolaEntity szkola) {
        this.nazwa = profil_nazwa;
        this.szkola = szkola;
    }

    
    public String getProfil_nazwa() {
        return nazwa;
    }

    public void setProfil_nazwa(String profil_nazwa) {
        this.nazwa = profil_nazwa;
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

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }
    
    public List<RozszerzonePrzedmiotyEntity> getRozszerzonePrzedmioty() {
        return rozszerzonePrzedmioty;
    }
    
    public void setRozszerzonePrzedmioty(List<RozszerzonePrzedmiotyEntity> rozszerzonePrzedmioty) {
        this.rozszerzonePrzedmioty = rozszerzonePrzedmioty;
    }
    
    public List<OcenaPrzedmiotuEntity> getOcenyPrzedmiotow() {
        return ocenyPrzedmiotow;
    }
    
    public void setOcenyPrzedmiotow(List<OcenaPrzedmiotuEntity> ocenyPrzedmiotow) {
        this.ocenyPrzedmiotow = ocenyPrzedmiotow;
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
