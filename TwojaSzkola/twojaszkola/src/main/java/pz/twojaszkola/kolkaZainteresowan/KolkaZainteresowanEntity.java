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
package pz.twojaszkola.kolkaZainteresowan;

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
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author Agata Kostrzewa
 */
@Entity
@Table(name = "kolka_zainteresowan")
@NamedQueries({
    @NamedQuery(
            name = "KolkaZainteresowanEntity.findBySzkolaId",
            query
            = "Select b from KolkaZainteresowanEntity b "
            + " where b.szkola.id = :idSzkoly"
    )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class KolkaZainteresowanEntity implements Serializable {

    private static final long serialVersionUID = 001L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nazwa")
    @Size(max = 255)
    @NotBlank
    private String nazwa;

    @Column(name = "termin")
    @Size(max = 255)
    @NotBlank
    private String termin;

    @ManyToOne
    @JoinColumn(name = "przedmiot")
    private przedmiotyEntity przedmiot;

    @ManyToOne
    @JoinColumn(name = "szkola")
    private SzkolaEntity szkola;

    protected KolkaZainteresowanEntity() {
    }

    public KolkaZainteresowanEntity(String nazwa, String termin, przedmiotyEntity przedmiot, SzkolaEntity szkola) {
        this.nazwa = nazwa;
        this.termin = termin;
        this.przedmiot = przedmiot;
        this.szkola = szkola;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public przedmiotyEntity getPrzedmiot() {
        return przedmiot;
    }

    public void setSzkola(przedmiotyEntity przedmiot) {
        this.przedmiot = przedmiot;
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
        if (!(object instanceof KolkaZainteresowanEntity)) {
            return false;
        }
        KolkaZainteresowanEntity other = (KolkaZainteresowanEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
