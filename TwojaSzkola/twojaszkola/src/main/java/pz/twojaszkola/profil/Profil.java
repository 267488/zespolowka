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
package pz.twojaszkola.profil;

import java.util.List;

/**
 *
 * @author radon
 */
public class Profil {
    private String nazwa;
    private List<Integer> przedmiotIds;
    private List<String> przedmiotNazwy;

    public Profil() {
    }

    
    
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public List<Integer> getPrzedmiotIds() {
        return przedmiotIds;
    }

    public void setPrzedmiotIds(List<Integer> przedmiotIds) {
        this.przedmiotIds = przedmiotIds;
    }

    public List<String> getPrzedmiotNazwy() {
        return przedmiotNazwy;
    }

    public void setPrzedmiotNazwy(List<String> przedmiotNazwy) {
        this.przedmiotNazwy = przedmiotNazwy;
    }
    
    
}
