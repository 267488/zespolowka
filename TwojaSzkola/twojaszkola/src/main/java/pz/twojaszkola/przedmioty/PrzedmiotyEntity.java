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
package pz.twojaszkola.przedmioty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
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
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.kategorie.kategorieEntity;

/**
 *
 * @author Agata
 */

@Entity
@Table(name = "przedmioty")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class PrzedmiotyEntity implements Serializable {
    
    private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "name", unique = true, length = 255, nullable = false)
    @NotBlank //niepusty
    @Size(max = 255)
    private String name;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "kategoria", referencedColumnName = "id")
    private kategorieEntity kategoria;

    protected PrzedmiotyEntity() {
    }

    public PrzedmiotyEntity(String name, kategorieEntity kategoria) {
        this.name = name;
        this.kategoria = kategoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public kategorieEntity getKategoria() {
        return kategoria;
    }

    public void setKategoria(kategorieEntity kategoria) {
        this.kategoria = kategoria;
    }
    
}
