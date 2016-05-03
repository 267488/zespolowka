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
package pz.twojaszkola.aktualnosciSzkola;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.galleryPictures.GalleryPictureEntity;
import pz.twojaszkola.user.User;
/**
 *
 * @author KR
 */
@Entity
@Table(name = "aktualnosci")
@NamedQueries({
   @NamedQuery(
        name = "aktualnosciSzkolaEntity.findByUserId",
        query
        = "Select b from aktualnosciSzkolaEntity b "
        + " where b.userId.id = :userId"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class aktualnosciSzkolaEntity implements Serializable{
    
    private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId; 
    
    @Column(name = "tytul")
    @Size(max = 255)
    private String tytul;
    
    @Column(name = "dataPost")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataPost;
    
    @Column(name = "tekst", nullable = false)
    @Size(max = 4096)
    private String tekst;
    
    protected aktualnosciSzkolaEntity(){}
    
    public aktualnosciSzkolaEntity(User szkola_id, String tytul, String tekst){
        this.userId = szkola_id;
        this.tytul = tytul;
        this.tekst = tekst;
        this.dataPost = Calendar.getInstance();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
    
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof aktualnosciSzkolaEntity)) {
            return false;
        }
        aktualnosciSzkolaEntity other = (aktualnosciSzkolaEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
