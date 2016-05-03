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
package pz.twojaszkola.user;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.aktualnosciSzkola.aktualnosciSzkolaEntity;
import pz.twojaszkola.galleryUser.GalleryUserEntity;
import pz.twojaszkola.osiagniecia.OsiagnieciaEntity;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author radon
 */
@Entity
@Table(name = "user1")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "login", length = 255, nullable = false, unique=true)
    @NotBlank
    @Size(max = 255)
    private String login;
    
    @Column(name = "email", length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String email;
    
    @Column(name = "password", length = 255, nullable = false)
    @NotBlank
    @Size(max = 255)
    private String password;
    
    @Column(name = "role", nullable = false)    
    //@Enumerated(EnumType.STRING)
    private String role;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "ban", nullable = false)
    private String ban;
    
    @OneToOne(cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    @JoinColumn(name = "galleryId")
    private GalleryUserEntity galleryId;
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<UczenEntity> uczniowie = new ArrayList<>();
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<SzkolaEntity> szkola = new ArrayList<>();
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<GalleryUserEntity> galleryUser = new ArrayList<>();
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<aktualnosciSzkolaEntity> aktualnosciSzkola = new ArrayList<>();
    
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonIgnore private List<OsiagnieciaEntity> osiagniecia = new ArrayList<>();
    
    protected User() {
    }
   
    public User(String login,String email, String password, String role, String state,String ban) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.role = role;
        this.state = state;
        this.ban = ban;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
   
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }   
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }    

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }    

    public GalleryUserEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GalleryUserEntity galleryId) {
        this.galleryId = galleryId;
    }
    
    public List<UczenEntity> getUczniowie() {
        return uczniowie;
    }
    
    public void setUczniowie(List<UczenEntity> uczniowie) {
        this.uczniowie = uczniowie;
    }
    
    public List<SzkolaEntity> getSzkola() {
        return szkola;
    }
    
    public void setSzkola(List<SzkolaEntity> szkola) {
        this.szkola = szkola;
    }
    
    public List<GalleryUserEntity> getGalleryUser() {
        return galleryUser;
    }

    public void setGalleryUser(List<GalleryUserEntity> galleryUser) {
        this.galleryUser = galleryUser;
    }
    
    public List<aktualnosciSzkolaEntity> getAktualnosciSzkola() {
        return aktualnosciSzkola;
    }
    
    public void setAktualnosciSzkola(List<aktualnosciSzkolaEntity> aktualnosciSzkola) {
        this.aktualnosciSzkola = aktualnosciSzkola;
    }
    
    public List<OsiagnieciaEntity> getOsiagniecia() {
        return osiagniecia;
    }
    
    public void setOsiagniecia(List<OsiagnieciaEntity> osiagniecia) {
        this.osiagniecia = osiagniecia;
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
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pz.twojaszkola.user.User[ id=" + id + " ]";
    }
    
}
