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


/**
 *
 * @author Agata
 */
public class szkola {
    
    private SzkolaEntity szkola;   
    
    private String rodzajGwiazdki;

    protected szkola() {
    }

    public szkola(SzkolaEntity szkola, String rodzajGwiazdki) {
        this.szkola = szkola;
        this.rodzajGwiazdki = rodzajGwiazdki;
    }

    public SzkolaEntity getSzkola() {
        return szkola;
    }

    public void setSzkola(SzkolaEntity szkola) {
        this.szkola = szkola;
    }

    public String getRodzajGwiazdki() {
        return rodzajGwiazdki;
    }

    public void setRodzajGwiazdki(String rodzajGwiazdki) {
        this.rodzajGwiazdki = rodzajGwiazdki;
    }
    
    
}
