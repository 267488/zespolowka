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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.FetchType;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;
import javax.persistence.PrePersist;

import pz.twojaszkola.szkola.SzkolaEntity;
//import pz.twojaszkola.szkola.SzkolaCmd;
/**
 *
 * @author KR
 */
@Entity
@Table(name = "aktualnosciSzkola")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class aktualnosciSzkolaEntity implements Serializable{
    
    private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "szkolaId", referencedColumnName = "id")
    private SzkolaEntity szkolaId;
    
    @Column(name = "tytul", nullable = false)
    @Size(max = 255)
    @NotBlank
    private String tytul;
    
    @Column(name = "dataPost")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonIgnore
    //@Size(max = 10)
    //@NotBlank
    private Calendar dataPost;
    
    @Column(name = "dataEdycja")
    @Temporal(TemporalType.TIMESTAMP)
    //@JsonIgnore
    //@Size(max = 10)
    //@NotBlank
    private Calendar dataEdycja;
    
    @Column(name = "tekst", nullable = false)
    @Size(max = 4096)
    @NotBlank
    private String tekst;
    
    @Column(name = "autor", nullable = false)
    @Size(max = 255)
    @NotBlank
    private String autor;
    
    protected aktualnosciSzkolaEntity(){}
    
    public aktualnosciSzkolaEntity(SzkolaEntity szkola_id, String tytul, String tekst, String autor){
        this.szkolaId = szkola_id;
        this.tytul = tytul;
        //this.dataPost = dataPost; //data zamieszczenia posta, TIMESTAMP?
        this.tekst = tekst;
        this.autor = autor;
        dataPost = Calendar.getInstance();
    }
// Pokombinować z edycją   
//    public aktualnosciSzkolaEntity(SzkolaEntity szkola_id, String tytul, String tekst, String autor) {
//        this.szkolaId = szkola_id;
//        this.tytul = tytul;
//        this.dataEdycja = dataEdycja;
//        this.tekst = tekst;
//        this.autor = autor;
//    }
    
//    @PrePersist
//    public void prePersist() {
//	if (this.dataPost == null) {
//	    this.dataPost = Calendar.getInstance();
//	}
//    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public SzkolaEntity getSzkolaId(){
        return szkolaId;
    }
    
    public void setSzkolaId(SzkolaEntity szkola_id){
        this.szkolaId = szkola_id;
    }
   
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

//    public Date getDataPost() {
//        return dataPost;
//    }

//    public void setDataPost(String dataPost) {
//        this.dataPost = dataPost;
//    }
//
//    public String getDateEdycja() {
//        return dataEdycja;
//    }

//    public void setDateEdycja(String dataEdycja) {
//        this.dataEdycja = dataEdycja;
//    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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
