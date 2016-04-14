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
package pz.twojaszkola.uczen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author Agata
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UczenCmd {
    
    @NotBlank
    @Size(min=11,max = 11)
    private String pesel;
    
    @NotBlank
    @Size(max = 255)
    private String name;
    
    @NotBlank
    @Size(max = 255)
    private String lastname;

    @NotBlank
    @Size(max = 255)
    private String mail;
    
    @NotBlank
    @Size(min=6, max = 255)
    private String password;
    
    @NotBlank
    @Size(min=6, max = 6)
    private String kodpocztowy;
    
    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKodpocztowy() {
        return kodpocztowy;
    }

    public void setKodpocztowy(String kodpocztowy) {
        this.kodpocztowy = kodpocztowy;
    }
    
}
