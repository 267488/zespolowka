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
package pz.twojaszkola.proponowaneSzkoly;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
public class proponowaneSzkolyEntity implements Serializable {
     private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "uczen_id", referencedColumnName = "id")
    private UczenEntity uczen_id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "szkola_id", referencedColumnName = "id")
    private SzkolaEntity szkola_id;

    protected proponowaneSzkolyEntity() {
    }

    public proponowaneSzkolyEntity(UczenEntity uczen_id, SzkolaEntity szkola_id) {
        this.uczen_id = uczen_id;
        this.szkola_id = szkola_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UczenEntity getUczen_id() {
        return uczen_id;
    }

    public void setUczen_id(UczenEntity uczen_id) {
        this.uczen_id = uczen_id;
    }

    public SzkolaEntity getSzkola_id() {
        return szkola_id;
    }

    public void setSzkola_id(SzkolaEntity szkola_id) {
        this.szkola_id = szkola_id;
    }
    
    
}
