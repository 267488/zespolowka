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
package pz.twojaszkola.mediany;

/**
 *
 * @author andrew
 */
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.zainteresowania.ZainteresowaniaEntity;

@Entity
@Table(name = "mediany")
@NamedQueries({
    @NamedQuery(
        name = "MedianyEntity.findByUczenId2",
        query
        = "Select b from MedianyEntity b "
        + " where b.uczenId.id = :idUcznia"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class MedianyEntity implements Serializable {
    
     private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "uczen_id", referencedColumnName = "id")
    private UczenEntity uczenId;
    
    @Column(name = "mediana", nullable = false)
    //@NotBlank
    private Integer mediana;

    protected MedianyEntity() {
    }
    
    public MedianyEntity(UczenEntity uczen_id, Integer mediana) {
        this.uczenId = uczen_id;
        this.mediana = mediana;
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

    public Integer getMediana() {
        return mediana;
    }

    public void setMediana(Integer mediana) {
        this.mediana = mediana;
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
        MedianyEntity other = (MedianyEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
}