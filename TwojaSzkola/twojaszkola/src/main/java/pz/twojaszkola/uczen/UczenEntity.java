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
package pz.twojaszkola.uczen;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.Objects;
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
import pz.twojaszkola.gallerySchool.GallerySchoolEntity;
import pz.twojaszkola.galleryStudent.GalleryStudentEntity;
import pz.twojaszkola.user.User;

/**
 *
 * @author Agata
 */
@Entity
@Table(name = "uczen")
@NamedQueries({
   @NamedQuery(
        name = "UczenEntity.findByUserId",
        query
        = "Select b from UczenEntity b "
        + " where b.userId.id = :userId"
   )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class UczenEntity implements Serializable {
    private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "pesel", unique = true)    
    @Size(min=11, max=11)
    private String pesel;
    
    @Column(name = "name", length = 255)
    @Size(max = 255)
    private String name;
    
    @Column(name = "lastname", length = 255)
    @Size(max = 255)
    private String lastname;
    
    @Column(name = "czegoSzukam", length = 255)
    @Size(min=6, max = 255)
    private String czegoSzukam;
    
    @Column(name = "kodpocztowy", length = 255)
    @Size(min=6, max = 6)
    private String kodpocztowy;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User userId; 
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "galleryId")
    private GalleryStudentEntity galleryId;
    
    protected UczenEntity() {
    }
    
    public UczenEntity(String pesel, String name, String lastname, String czegoSzukam, String kodpocztowy, User userId) {
        this.pesel=pesel;
        this.name = name;
        this.lastname = lastname;
        this.czegoSzukam = czegoSzukam;
        this.kodpocztowy = kodpocztowy;
        this.userId = userId;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCzegoSzukam() {
        return czegoSzukam;
    }

    public void setCzegoSzukam(String czegoSzukam) {
        this.czegoSzukam = czegoSzukam;
    }

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public GalleryStudentEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GalleryStudentEntity galleryId) {
        this.galleryId = galleryId;
    }
    
    @Override
    public int hashCode() {
	int hash = 7;
	hash = 97 * hash + Objects.hashCode(this.name);
	return hash;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	final UczenEntity other = (UczenEntity) obj;
	if (!Objects.equals(this.pesel, other.pesel)) {
	    return false;
	}
	return true;
    }
    
}
