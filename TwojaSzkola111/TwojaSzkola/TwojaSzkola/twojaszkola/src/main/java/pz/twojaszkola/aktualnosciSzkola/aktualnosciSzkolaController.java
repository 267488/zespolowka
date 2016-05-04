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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.validation.Valid;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.galleryPictures.GalleryPictureEntity;
import pz.twojaszkola.galleryPictures.GalleryPictureRepository;
import pz.twojaszkola.galleryUser.GalleryUserRepository;
import pz.twojaszkola.profil.ProfileController;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;

/**
 *
 * @author KR
 */
@RestController
@RequestMapping("/api")
public class aktualnosciSzkolaController {

    private final aktualnosciSzkolaRepository aktualnosciSzkolaRepository;
    private final SzkolaRepository szkolaRepository;
    private final UczenRepository uczenRepository;
    private final UserRepository userRepository;
    private final GalleryPictureRepository galleryRepository;
    private final GalleryUserRepository galleryUserRepository;

    @Autowired
    public aktualnosciSzkolaController(final aktualnosciSzkolaRepository aktualnosciSzkolaRepository,
            final SzkolaRepository szkolaRepository,
            final UczenRepository uczenRepository,
            final UserRepository userRepository,
            final GalleryUserRepository galleryUserRepository,
            final GalleryPictureRepository galleryRepository) {
        this.aktualnosciSzkolaRepository = aktualnosciSzkolaRepository;
        this.szkolaRepository = szkolaRepository;
        this.galleryRepository = galleryRepository;
        this.userRepository = userRepository;
        this.galleryUserRepository = galleryUserRepository;
        this.uczenRepository = uczenRepository;
    }

    class aktualnosciComparator implements Comparator<aktualnosci> {

        @Override
        public int compare(aktualnosci s1, aktualnosci s2) {
            if (s1.getAktualnosc().getDataPost().before(s2.getAktualnosc().getDataPost())) {
                return 1;
            } else if (s2.getAktualnosc().getDataPost().before(s1.getAktualnosc().getDataPost())) {
                return -1;
            }
            return 0;
        }
    }

    @RequestMapping(value = "/aktualnosciSzkola", method = GET)
    public List<aktualnosci> getAktualnosciSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<aktualnosci> aktual = new ArrayList<aktualnosci>();
        List<aktualnosciSzkolaEntity> rv;
        rv = aktualnosciSzkolaRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        for (aktualnosciSzkolaEntity a : rv) {
            List<GalleryPictureEntity> gallery = galleryRepository.findByAktualnosciId(a.getId());
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG0: " + gallery.size());
            String podpis = "";
            if ("UCZEN".equals(a.getUserId().getRole())) {
                UczenEntity u = uczenRepository.findByUserId(a.getUserId().getId());
                podpis = u.getName() + " " + u.getLastname();
            } else {
                SzkolaEntity s = szkolaRepository.findByUserId(a.getUserId().getId());
                podpis = s.getTypSzkoly() + " nr " + s.getNumer();
            }
            aktualnosci aktualne;
            String zdjecie = "img/brak.jpg";
                if (galleryUserRepository.findByUserId(a.getUserId().getId()) != null) {
                    zdjecie = "/api/galleryUser/" + galleryUserRepository.findByUserId(a.getUserId().getId()) + ".jpg";
                }
            if (!gallery.isEmpty()) {
                aktualne = new aktualnosci(a, podpis, gallery, zdjecie);

                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG1: " + aktualne.getAktualnosc().getTytul());
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG2: " + aktualne.getGalleryId().size());
            } else {
                aktualne = new aktualnosci(a, podpis, zdjecie);

                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG3: " + aktualne.getAktualnosc().getTytul());

            }
            aktual.add(aktualne);
        }
        Collections.sort(aktual, new aktualnosciComparator());

        for (aktualnosci a : aktual) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG OST: " + a.getAktualnosc().getTytul());

        }
        return aktual;
    }

    @RequestMapping(value = "/aktualnosciCurrentSzkola", method = GET)
    public List<aktualnosci> getAktualnosciCurrentSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<aktualnosci> aktual = new ArrayList<aktualnosci>();
        List<aktualnosciSzkolaEntity> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        rv = aktualnosciSzkolaRepository.findByUserId(idUsera);
        for (aktualnosciSzkolaEntity a : rv) {
            List<GalleryPictureEntity> gallery = galleryRepository.findByAktualnosciId(a.getId());
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG0: " + gallery.size());

            SzkolaEntity s = szkolaRepository.findByUserId(a.getUserId().getId());
            String podpis = s.getTypSzkoly() + " nr " + s.getNumer();
            aktualnosci aktualne;
            String zdjecie = "img/brak.jpg";
                if (galleryUserRepository.findByUserId(a.getUserId().getId()) != null) {
                    zdjecie = "/api/galleryUser/" + galleryUserRepository.findByUserId(a.getUserId().getId()) + ".jpg";
                }
            if (!gallery.isEmpty()) {
                aktualne = new aktualnosci(a, podpis, gallery, zdjecie);
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG1: " + aktualne.getAktualnosc().getTytul());
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG2: " + aktualne.getGalleryId().size());
            } else {
                aktualne = new aktualnosci(a, podpis, zdjecie);
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG3: " + aktualne.getAktualnosc().getTytul());

            }
            aktual.add(aktualne);
        }
        Collections.sort(aktual, new aktualnosciComparator());

        for (aktualnosci a : aktual) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG OST: " + a.getAktualnosc().getTytul());

        }
        return aktual;
    }

    @RequestMapping(value = "/aktualnosciCurrentUczen", method = GET)
    public List<aktualnosci> getCurrentUczenAktualnosci(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<aktualnosci> aktual = new ArrayList<aktualnosci>();
        List<aktualnosciSzkolaEntity> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        rv = aktualnosciSzkolaRepository.findByUserId(idUsera);
        for (aktualnosciSzkolaEntity a : rv) {
            List<GalleryPictureEntity> gallery = galleryRepository.findByAktualnosciId(a.getId());
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG0: " + gallery.size());

            UczenEntity u = uczenRepository.findByUserId(a.getUserId().getId());
            String podpis = u.getName() + " " + u.getLastname();

            aktualnosci aktualne;
            String zdjecie = "img/brak.jpg";
                if (galleryUserRepository.findByUserId(a.getUserId().getId()) != null) {
                    zdjecie = "/api/galleryUser/" + galleryUserRepository.findByUserId(a.getUserId().getId()) + ".jpg";
                }
            if (!gallery.isEmpty()) {
                aktualne = new aktualnosci(a, podpis, gallery, zdjecie);

                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG1: " + aktualne.getAktualnosc().getTytul());
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG2: " + aktualne.getGalleryId().size());
            } else {
                aktualne = new aktualnosci(a, podpis, zdjecie);

                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG3: " + aktualne.getAktualnosc().getTytul());

            }
            aktual.add(aktualne);
        }
        
        Collections.sort(aktual, new aktualnosciComparator());

        for (aktualnosci a : aktual) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG OST: " + a.getAktualnosc().getTytul());

        }
        return aktual;
    }

    @RequestMapping(value = "/CurrentUser/postcount", method = GET)
    public int getCurrentSizeAktualnosci(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        final User user = userRepository.findById(idUsera);
        List<aktualnosciSzkolaEntity> aktualnosciSzkola = user.getAktualnosciSzkola();

        return aktualnosciSzkola.size();
    }

    @RequestMapping(value = "/aktualnosciSzkola", method = POST)
    @PreAuthorize("isAuthenticated()")
    public aktualnosciSzkolaEntity createAktualnosciSzkola(final @RequestBody @Valid aktualnosciSzkolaCmd newAktualnosciSzkola, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments KR");
        }

        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();

        final User user = userRepository.findById(idUsera);

        final aktualnosciSzkolaEntity aktualnosciSzkola = new aktualnosciSzkolaEntity(user, newAktualnosciSzkola.getTytul(), newAktualnosciSzkola.getTekst());
        return this.aktualnosciSzkolaRepository.save(aktualnosciSzkola);
    }

//POTEM DODAC UPDATE
    @RequestMapping(value = "/aktualnosciSzkola/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteAktualnosciSzkola(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final aktualnosciSzkolaEntity aktualnosciSzkola = aktualnosciSzkolaRepository.findById(id);
        aktualnosciSzkolaRepository.delete(aktualnosciSzkola);

        //return uczen;
    }
//POTEM DODAC UPDATE

    @RequestMapping(value = "/aktualnosciSzkola/{id}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public aktualnosciSzkolaEntity updateAktualnosciSzkola(final @PathVariable Integer id, final @RequestBody @Valid aktualnosciSzkolaCmd updatedAktualnosciSzkola, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        final aktualnosciSzkolaEntity aktualnosciSzkola = aktualnosciSzkolaRepository.findById(id);
        aktualnosciSzkola.setTytul(updatedAktualnosciSzkola.getTytul());
        aktualnosciSzkola.setTekst(updatedAktualnosciSzkola.getTekst());

        return this.aktualnosciSzkolaRepository.save(aktualnosciSzkola);
    }
}
