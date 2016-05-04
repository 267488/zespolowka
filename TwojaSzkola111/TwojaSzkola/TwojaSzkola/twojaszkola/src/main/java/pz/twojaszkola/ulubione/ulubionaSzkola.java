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
package pz.twojaszkola.ulubione;

/**
 *
 * @author Agata
 */
public class ulubionaSzkola {
    private UlubionaSzkolaEntity2 ulubiona;
    private String zdjecie;

    public ulubionaSzkola(UlubionaSzkolaEntity2 ulubiona, String zdjecie) {
        this.ulubiona = ulubiona;
        this.zdjecie = zdjecie;
    }

    public UlubionaSzkolaEntity2 getUlubiona() {
        return ulubiona;
    }

    public void setUlubiona(UlubionaSzkolaEntity2 ulubiona) {
        this.ulubiona = ulubiona;
    }

    public String getZdjecie() {
        return zdjecie;
    }

    public void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }
    
    
    
}
