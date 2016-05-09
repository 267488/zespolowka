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
package pz.twojaszkola.szkola;

import pz.twojaszkola.galleryUser.GalleryUserEntity;
import pz.twojaszkola.proponowaneSzkoly.proponowaneSzkolyEntity;

/**
 *
 * @author Agata
 */
public class proponowaneSzkoly {
    private proponowaneSzkolyEntity szkola;   
    
    private String rodzajGwiazdki;
    
    private String zdjecie;

    public proponowaneSzkoly(proponowaneSzkolyEntity szkola, String rodzajGwiazdki, String zdjecie) {
        this.szkola = szkola;
        this.rodzajGwiazdki = rodzajGwiazdki;
        this.zdjecie = zdjecie;
    }

    public String getZdjecie() {
        return zdjecie;
    }

    public void setZdjecie(String zdjecie) {
        this.zdjecie = zdjecie;
    }

    public proponowaneSzkolyEntity getSzkola() {
        return szkola;
    }

    public void setSzkola(proponowaneSzkolyEntity szkola) {
        this.szkola = szkola;
    }

    public String getRodzajGwiazdki() {
        return rodzajGwiazdki;
    }

    public void setRodzajGwiazdki(String rodzajGwiazdki) {
        this.rodzajGwiazdki = rodzajGwiazdki;
    }

    
}
