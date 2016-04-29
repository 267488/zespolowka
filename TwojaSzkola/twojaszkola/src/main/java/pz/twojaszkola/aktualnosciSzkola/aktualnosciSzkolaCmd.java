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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author KR
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class aktualnosciSzkolaCmd {
    
    //@NotNull
    //private Integer id;
    
    private SzkolaEntity szkolaId;
    
    @NotBlank
    @Size(max = 255)
    private String tytul;
    
    //ustawic format daty
    //@NotBlank
    //@Size(max = 10)
    private String dataPost;
    
    //ustawic format daty
    //@NotBlank
    //@Size(max = 10)
    private String dataEdycja;
    
    @NotBlank
    @Size(max = 4096)
    private String tekst;
    
    @NotBlank
    @Size(max = 255)
    private String autor;
            
//    public Integer getId() {
//        return id;
//    }
   
    public SzkolaEntity getSzkolaId() {
        return szkolaId;
    }
    
    public void setSzkolaId(SzkolaEntity szkola_id) {
        this.szkolaId = szkola_id;
    }
    
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getDataPost() {
        return dataPost;
    }

    public void setDataPost(String dataPost) {
        this.dataPost = dataPost;
    }

    public String getDataEdycja() {
        return dataEdycja;
    }

    public void setDataEdycja(String dataEdycja) {
        this.dataEdycja = dataEdycja;
    }

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
    
}
