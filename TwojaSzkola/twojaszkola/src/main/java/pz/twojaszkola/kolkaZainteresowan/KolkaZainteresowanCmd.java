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
package pz.twojaszkola.kolkaZainteresowan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.szkola.SzkolaEntity;

/**
 *
 * @author Agata Kostrzewa
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KolkaZainteresowanCmd {
    
    private String nazwa; 
	
    private String termin; 
	
    private Integer przedmiot;        

    private SzkolaEntity szkola;
	
    public String getNazwa() {
        return nazwa;
    }

    public String getTermin() {
        return termin;
    }
	
    public Integer getPrzedmiot() {
        return przedmiot;
    }
	
    public SzkolaEntity getSzkola() {
        return szkola;
    }
    
    
}
