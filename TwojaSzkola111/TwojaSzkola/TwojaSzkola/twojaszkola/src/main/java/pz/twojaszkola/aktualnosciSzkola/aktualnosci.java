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

import java.util.List;
import pz.twojaszkola.galleryPictures.GalleryPictureEntity;

/**
 *
 * @author Agata
 */
public class aktualnosci {
    
    private aktualnosciSzkolaEntity aktualnosc;
    private String podpis;
    private List<GalleryPictureEntity> galleryId;
    private String zdjecie;
    
    protected aktualnosci() {
    }

    public aktualnosci(aktualnosciSzkolaEntity aktualnosc, String podpis, List<GalleryPictureEntity> galleryId, String zdjecie) {
        this.aktualnosc = aktualnosc;
        this.podpis = podpis;
        this.galleryId = galleryId;
        this.zdjecie = zdjecie;
    }

    public aktualnosci(aktualnosciSzkolaEntity aktualnosc, String podpis, String zdjecie) {
        this.aktualnosc = aktualnosc;
        this.podpis = podpis;
        this.zdjecie = zdjecie;
    }
    
    public aktualnosciSzkolaEntity getAktualnosc() {
        return aktualnosc;
    }

    public void setAktualnosc(aktualnosciSzkolaEntity aktualnosc) {
        this.aktualnosc = aktualnosc;
    }

    public List<GalleryPictureEntity> getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(List<GalleryPictureEntity> galleryId) {
        this.galleryId = galleryId;
    }

    public String getPodpis() {
        return podpis;
    }

    public void setPodpis(String podpis) {
        this.podpis = podpis;
    }
    
    
}
