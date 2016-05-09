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
package pz.twojaszkola.osiagniecia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Calendar;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.user.User;

/**
 *
 * @author Agata Kostrzewa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class OsiagnieciaCmd {

    private String nazwakonkursu;

    private Calendar termin;

    private Integer przedmiot;

    private String szczebel;

    private String nagroda;
    
    private User userId; 

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

    public Integer getPrzedmiot() {
        return przedmiot;
    }

    public void setPrzedmiot(Integer przedmiot) {
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

    public User getUserId() {
        return userId;
    }

    public void setUserId(User u) {
        this.userId = u;
    }
    
    

}
