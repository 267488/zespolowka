/*
 * Copyright 2016 Agata Kostrzewa
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
package pz.twojaszkola.OcenaPrzedmiotu;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
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
import javax.persistence.Table;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.przedmioty.przedmiotyEntity;

/**
 *
 * @author Agata
 */
@Entity
@Table(name = "ocenaPrzedmiotu")
@NamedQueries({
   @NamedQuery(
        name = "OcenaPrzedmiotuEntity.getOcenaByPrzedmiotAndProfil",
        query
        = "Select b.ocena from OcenaPrzedmiotuEntity b "
        + " where b.profilId.id = :idProfilu"
        + " and b.przedmiotId.id = :idPrzedmiotu"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class OcenaPrzedmiotuEntity implements Serializable {
    
     private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "profilId", referencedColumnName = "id")
    private ProfilEntity profilId;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "przedmiotId", referencedColumnName = "id")
    private przedmiotyEntity przedmiotId;
    
    @Column(name = "ocena", nullable = false)
    private Integer ocena;

    protected OcenaPrzedmiotuEntity() {
    }

    public OcenaPrzedmiotuEntity(ProfilEntity profil_id, przedmiotyEntity przedmiot_id, Integer stopien_zainteresowania) {
        this.profilId = profil_id;
        this.przedmiotId = przedmiot_id;
        this.ocena = stopien_zainteresowania;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProfilEntity getProfilId() {
        return profilId;
    }

    public void setProfilId(ProfilEntity profil_id) {
        this.profilId = profil_id;
    }

    public przedmiotyEntity getPrzedmiotId() {
        return przedmiotId;
    }

    public void setPrzedmiotId(przedmiotyEntity przedmiot_id) {
        this.przedmiotId = przedmiot_id;
    }

    public Integer getOcena() {
        return ocena;
    }

    public void setOcena(Integer ocena) {
        this.ocena = ocena;
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
        if (!(object instanceof OcenaPrzedmiotuEntity)) {
            return false;
        }
        OcenaPrzedmiotuEntity other = (OcenaPrzedmiotuEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
