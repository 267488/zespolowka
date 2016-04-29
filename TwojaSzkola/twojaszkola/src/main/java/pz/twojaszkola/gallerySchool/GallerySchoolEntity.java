/*
 * Copyright 2014 Michael J. Simons.
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
package pz.twojaszkola.gallerySchool;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.user.User;

/**
 * @author Michael J. Simons, 2014-02-22
 */
@Entity
@Table(name = "gallery_school")
//@NamedQueries({
//   @NamedQuery(
//        name = "GallerySchoolEntity.findByUserId",
//        query
//        = "Select b from GallerySchoolEntity b "
//        + " where b.userId.id = :userId"
//   )
//})
public class GallerySchoolEntity implements Serializable {

    private static final long serialVersionUID = -5303688860568518942L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User userId;
    
    @Column(name = "filename", length = 36, unique = true, updatable = false)
    @NotNull
    @Size(max = 36)
    private String filename;

    @OneToMany(mappedBy = "galleryId", /*cascade = CascadeType.ALL, orphanRemoval = true,*/ fetch = FetchType.EAGER) //OtO?
    @JsonIgnore private List<SzkolaEntity> szkoly = new ArrayList<>();

    protected GallerySchoolEntity() {
    }

    public GallerySchoolEntity(User userId,String filename) {
	this.userId = userId;
        this.filename = filename;
    }

    public Integer getId() {
	return id;
    }

    public String getFilename() {
	return filename;
    }
    
    public List<SzkolaEntity> getSzkoly() {
        return szkoly;
    }
    
    public void setSzkoly(List<SzkolaEntity> szkoly) {
        this.szkoly = szkoly;
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 23 * hash + Objects.hashCode(this.filename);
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
	final GallerySchoolEntity other = (GallerySchoolEntity) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }
}
