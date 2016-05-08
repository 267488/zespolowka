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
package pz.twojaszkola.zainteresowania;

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
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
@Entity
@Table(name = "zainteresowania")
@NamedQueries({
    @NamedQuery(
	    name = "zainteresowaniaEntity.findByPrzedmiotId",
	    query
	    = "Select b from zainteresowaniaEntity b "
	    + " where b.przedmiotId.id = :idPrzedmiotu"
    ),
    @NamedQuery(
        name = "zainteresowaniaEntity.findByUczenId",
        query
        = "Select b.stopienZainteresowania from zainteresowaniaEntity b "
        + " where b.uczenId.id = :idUcznia"
   ),
    @NamedQuery(
        name = "zainteresowaniaEntity.findByUczenId2",
        query
        = "Select b from zainteresowaniaEntity b "
        + " where b.uczenId.id = :idUcznia"
   ),
   @NamedQuery(
        name = "zainteresowaniaEntity.getStopienZaintByUczenAndPrzedmiot",
        query
        = "Select b.stopienZainteresowania from zainteresowaniaEntity b "
        + " where b.uczenId.id = :idUcznia"
        + " and b.przedmiotId.id = :idPrzedmiotu"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ZainteresowaniaEntity implements Serializable {
    
     private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "uczenId", referencedColumnName = "id")
    private UczenEntity uczenId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "przedmiotId", referencedColumnName = "id")
    private PrzedmiotyEntity przedmiotId;
    
    @Column(name = "stopienZainteresowania", nullable = false)
    //@NotBlank
    //@NotNull
    private Integer stopienZainteresowania;

    protected ZainteresowaniaEntity() {
    }

    public ZainteresowaniaEntity(UczenEntity uczen_id, PrzedmiotyEntity przedmiot_id, Integer stopien_zainteresowania) {
        this.uczenId = uczen_id;
        this.przedmiotId = przedmiot_id;
        this.stopienZainteresowania = stopien_zainteresowania;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UczenEntity getUczenId() {
        return uczenId;
    }

    public void setUczenId(UczenEntity uczen_id) {
        this.uczenId = uczen_id;
    }

    public PrzedmiotyEntity getPrzedmiotId() {
        return przedmiotId;
    }

    public void setPrzedmiotId(PrzedmiotyEntity przedmiot_id) {
        this.przedmiotId = przedmiot_id;
    }

    public Integer getStopienZainteresowania() {
        return stopienZainteresowania;
    }

    public void setStopienZainteresowania(Integer stopien_zainteresowania) {
        this.stopienZainteresowania = stopien_zainteresowania;
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
        if (!(object instanceof ZainteresowaniaEntity)) {
            return false;
        }
        ZainteresowaniaEntity other = (ZainteresowaniaEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
