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
package pz.twojaszkola.aktualnosciSzkola;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import pz.twojaszkola.user.User;

/**
 *
 * @author KR
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class aktualnosciSzkolaCmd {
    
    private User userId; 
    
    @NotBlank
    @Size(max = 255)
    private String tytul;
    
    private String dataPost;
    
    private String tekst;

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }
           
    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getDataPost() {
        return dataPost;
    }

    public void setDataPost(String dataPost) {
        this.dataPost = dataPost;
    }

//    public String getDataEdycja() {
//        return dataEdycja;
//    }
//
//    public void setDataEdycja(String dataEdycja) {
//        this.dataEdycja = dataEdycja;
//    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }
    
}
