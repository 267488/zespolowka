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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.kategorie.kategorieEntity;

/**
 *
 * @author Agata
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrzedmiotyCmd {
    
    @NotBlank //niepusty
    @Size(max = 255)
    private String name;
    
    @NotBlank //niepusty
    private kategorieEntity kategoria;

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
