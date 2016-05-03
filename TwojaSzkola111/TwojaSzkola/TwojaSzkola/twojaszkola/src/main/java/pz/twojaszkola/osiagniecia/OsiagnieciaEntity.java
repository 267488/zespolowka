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
package pz.twojaszkola.osiagniecia;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.io.Serializable;
import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.user.User;

/**
 *
 * @author Agata Kostrzewa
 */
@Entity
@Table(name = "osiagniecia")
@NamedQueries({
    @NamedQuery(
            name = "OsiagnieciaEntity.findByUserId",
            query
            = "Select b from OsiagnieciaEntity b "
            + " where b.userId.id = :idUsera"
    ),
    @NamedQuery(
            name = "OsiagnieciaEntity.findByPrzedmiotIdAndUserId",
            query
            = "Select b from OsiagnieciaEntity b "
            + " where b.przedmiot.id = :idPrzedmiotu"
            + " and b.userId.id = :idUsera"
    )
})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class OsiagnieciaEntity implements Serializable {

    private static final long serialVersionUID = 001L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "nazwakonkursu")
    @Size(max = 255)
    @NotBlank
    private String nazwakonkursu;

    @Column(name = "termin", nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    @NotNull
    private Calendar termin;

    @ManyToOne
    @JoinColumn(name = "przedmiot")
    private przedmiotyEntity przedmiot;

    @Column(name = "szczebel")
    @Size(max = 255)
    @NotBlank
    private String szczebel;

    @Column(name = "nagroda")
    @Size(max = 255)
    @NotBlank
    private String nagroda;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User userId; 

    protected OsiagnieciaEntity() {
    }

    public OsiagnieciaEntity(String nazwakonkursu, Calendar termin, przedmiotyEntity przedmiot, String szczebel,String nagroda, User userId) {
        this.nazwakonkursu = nazwakonkursu;
        this.termin = termin;
        this.przedmiot = przedmiot;
        this.szczebel = szczebel;
        this.userId = userId;
        this.nagroda = nagroda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazwakonkursu() {
        return nazwakonkursu;
    }

    public void setNazwakonkursu(String nazwakonkursu) {
        this.nazwakonkursu = nazwakonkursu;
    }

    public Calendar getTermin() {
        return termin;
    }

    public void setTermin(Calendar termin) {
        this.termin = termin;
    }

    public przedmiotyEntity getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(przedmiotyEntity przedmiot) {
        this.przedmiot = przedmiot;
    }

    public String getSzczebel() {
        return szczebel;
    }

    public void setSzczebel(String szczebel) {
        this.szczebel = szczebel;
    }

    public String getNagroda() {
        return nagroda;
    }

    public void setNagroda(String nagroda) {
        this.nagroda = nagroda;
    }

    public User getUser() {
        return userId;
    }

    public void setUser(User u) {
        this.userId = u;
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
        if (!(object instanceof OsiagnieciaEntity)) {
            return false;
        }
        OsiagnieciaEntity other = (OsiagnieciaEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
