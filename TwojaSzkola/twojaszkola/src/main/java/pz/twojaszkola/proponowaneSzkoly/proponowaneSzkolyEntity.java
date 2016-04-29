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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
//@Entity
////@Table(name = "proponowaneSzkoly")

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class proponowaneSzkolyEntity implements Serializable, Comparator<proponowaneSzkolyEntity>, Comparable<proponowaneSzkolyEntity> {
     private static final long serialVersionUID = 1249824815158908981L;
    
    //@Id
    //@Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Integer id;
    
    //@ManyToOne(optional = false, fetch = FetchType.EAGER)
    //@JoinColumn(name = "uczenId", referencedColumnName = "id")
    private UczenEntity uczenId;

    //@ManyToOne(optional = false, fetch = FetchType.EAGER)
    //@JoinColumn(name = "profilId", referencedColumnName = "id")
    private ProfilEntity profilId;
    
    //@Column(name = "punktacja")
    //@NotBlank
    private Integer punktacja;
    
    public proponowaneSzkolyEntity() {
    }

    public proponowaneSzkolyEntity(UczenEntity uczenId, ProfilEntity profilId, Integer punktacja) {
        this.uczenId = uczenId;
        this.profilId = profilId;
        this.punktacja = punktacja;
    }

    public UczenEntity getUczenId() {
        return uczenId;
    }

    public ProfilEntity getProfilId() {
        return profilId;
    }

    public Integer getPunktacja() {
        return punktacja;
    }

    public void setPunktacja(Integer punktacja) {
        this.punktacja = punktacja;
    }
    
    public int compare(proponowaneSzkolyEntity s1, proponowaneSzkolyEntity s2) {
                if (s1.punktacja > s2.punktacja) {
                return -1;
        }
                else if (s1.punktacja < s2.punktacja) {
               return 1;
        }
            return 0;
    }

    @Override
    public int compareTo(proponowaneSzkolyEntity s) {
        return (this.punktacja).compareTo(s.punktacja);
    }
    
}
