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
package pz.twojaszkola.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import pz.twojaszkola.galleryUser.GalleryUserEntity;

/**
 *
 * @author radon
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserCmd {
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
    
    @NotEmpty
    private String passwordRepeated;
    
    @NotBlank   
    private String role;
    
    @NotBlank   
    private String state;
    
    private GalleryUserEntity galleryId;
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public GalleryUserEntity getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(GalleryUserEntity galleryId) {
        this.galleryId = galleryId;
    }
    
}
