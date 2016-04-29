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
package pz.twojaszkola.galleryStudent;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import pz.twojaszkola.user.User;

/**
 * @author Agata Kostrzewa
 */
@Entity
@Table(name = "gallery_student")
@NamedQueries({
   @NamedQuery(
        name = "GalleryStudentEntity.findByUserId",
        query
        = "Select b from GalleryStudentEntity b "
        + " where b.userId.id = :idUsera"
   )
})
public class GalleryStudentEntity implements Serializable {

    private static final long serialVersionUID = -5303688860568518942L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User userId;
    
    @Column(name = "filename", length = 36, unique = true, updatable = false)
    @NotNull
    @Size(max = 36)
    private String filename;

    

    protected GalleryStudentEntity() {
    }

    public GalleryStudentEntity(User userId,String filename) {
		this.userId = userId;
        this.filename = filename;
    }

    public Integer getId() {
	return id;
    }

    public String getFilename() {
	return filename;
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
	final GalleryStudentEntity other = (GalleryStudentEntity) obj;
	if (!Objects.equals(this.id, other.id)) {
	    return false;
	}
	return true;
    }
}
