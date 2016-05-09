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
package pz.twojaszkola.ulubione;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
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
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
@Entity
@Table(name = "ulubione_szkoly")
@NamedQueries({
    @NamedQuery(
	    name = "UlubionaSzkolaEntity2.findByUczenId",
	    query
	    = "Select b from UlubionaSzkolaEntity2 b "
	    + " where b.uczenId.id = :idUcznia"
   ),
    @NamedQuery(
	    name = "UlubionaSzkolaEntity2.findBySzkolaIdAndUczenId",
	    query
	    = "Select b from UlubionaSzkolaEntity2 b "
	    + " where b.szkolaId.id = :idSzkoly"
            + " and b.uczenId.id = :idUcznia"
   ),
    @NamedQuery(
	    name = "UlubionaSzkolaEntity2.findBySzkolaId",
	    query
	    = "Select b from UlubionaSzkolaEntity2 b "
	    + " where b.szkolaId.id = :idSzkoly"
   ),
    @NamedQuery(
	    name = "UlubionaSzkolaEntity2.rmById",
	    query
	    = "Delete from UlubionaSzkolaEntity2 b "
	    + " where b.id = :id"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UlubionaSzkolaEntity2 {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "uczenId", referencedColumnName = "id")
    private UczenEntity uczenId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "szkolaId", referencedColumnName = "id")
    private SzkolaEntity szkolaId;

    protected UlubionaSzkolaEntity2() {
    }

    
    public UlubionaSzkolaEntity2(UczenEntity uczenId, SzkolaEntity szkolaId) {
        this.uczenId = uczenId;
        this.szkolaId = szkolaId;
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

    public void setUczenId(UczenEntity uczenId) {
        this.uczenId = uczenId;
    }

    public SzkolaEntity getSzkolaId() {
        return szkolaId;
    }

    public void setSzkolaId(SzkolaEntity szkolaId) {
        this.szkolaId = szkolaId;
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
        if (!(object instanceof UlubionaSzkolaEntity2)) {
            return false;
        }
        UlubionaSzkolaEntity2 other = (UlubionaSzkolaEntity2) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}

