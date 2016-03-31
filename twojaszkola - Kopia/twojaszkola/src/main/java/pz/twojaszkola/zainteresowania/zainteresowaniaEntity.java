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
package pz.twojaszkola.zainteresowania;

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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
@Entity
@Table(name = "zainteresowania")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class zainteresowaniaEntity implements Serializable {
    
     private static final long serialVersionUID = 1249824815158908981L;
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "uczen_id", referencedColumnName = "id")
    private UczenEntity uczen_id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "przedmiot_id", referencedColumnName = "id")
    private przedmiotyEntity przedmiot_id;
    
    @Column(name = "stopien_zainteresowania", nullable = false)
    @NotBlank
    private Integer stopien_zainteresowania;

    protected zainteresowaniaEntity() {
    }

    public zainteresowaniaEntity(UczenEntity uczen_id, przedmiotyEntity przedmiot_id, Integer stopien_zainteresowania) {
        this.uczen_id = uczen_id;
        this.przedmiot_id = przedmiot_id;
        this.stopien_zainteresowania = stopien_zainteresowania;
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

    public przedmiotyEntity getPrzedmiot_id() {
        return przedmiot_id;
    }

    public void setPrzedmiot_id(przedmiotyEntity przedmiot_id) {
        this.przedmiot_id = przedmiot_id;
    }

    public Integer getStopien_zainteresowania() {
        return stopien_zainteresowania;
    }

    public void setStopien_zainteresowania(Integer stopien_zainteresowania) {
        this.stopien_zainteresowania = stopien_zainteresowania;
    }

    
}
