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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class zainteresowaniaCmd {
    
    
    private Integer uczenId;
    
    
    private String przedmiotName;
    
    //@NotBlank
    //@NotNull
    private Integer stopienZainteresowania;

    public Integer getUczenId() {
        return uczenId;
    }

    public String getPrzedmiotName() {
        return przedmiotName;
    }

    public Integer getStopienZainteresowania() {
        return stopienZainteresowania;
    }
    
    
}
