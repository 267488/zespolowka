/*
* Copyright 2016 radon & agata
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuRepository;
import pz.twojaszkola.galleryUser.GalleryUserEntity;
import pz.twojaszkola.galleryUser.GalleryUserRepository;
import pz.twojaszkola.mediany.MedianyRepository;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.profil.ProfilRepository;
import pz.twojaszkola.proponowaneSzkoly.proponowaneSzkolyEntity;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.przedmioty.przedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.ulubione.UlubionaSzkolaEntity2;
import pz.twojaszkola.ulubione.UlubionaSzkolaRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.SuperSzkola;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;
import pz.twojaszkola.zainteresowania.zainteresowaniaController;
import pz.twojaszkola.zainteresowania.zainteresowaniaRepository;

/**
 *
 * @authors radon, agata
 */
@RestController
@RequestMapping("/api")
public class SzkolaController {

    private final SzkolaRepository szkolaRepository;
    private final MedianyRepository medianyRepository;
    private final ProfilRepository profilRepository;
    private final przedmiotyRepository przedmiotyRepo;
    private final UczenRepository uczenRepository;
    private final zainteresowaniaRepository zainteresowaniaRepo;
    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo;
    private final UlubionaSzkolaRepository ulubionaSzkolaRepo;
    private final UserRepository userRepository;
    private final GalleryUserRepository galleryUserRepo;

    @Autowired
    public SzkolaController(final SzkolaRepository szkolaRepository,
            final MedianyRepository medianyRepository,
            final ProfilRepository profilRepository,
            final przedmiotyRepository przedmiotyRepo,
            final UczenRepository uczenRepository,
            final zainteresowaniaRepository zainteresowaniaRepo,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo,
            final UlubionaSzkolaRepository ulubionaSzkolaRepo,
            final UserRepository userRepository,
            final GalleryUserRepository galleryUserRepo) {
        this.szkolaRepository = szkolaRepository;
        this.medianyRepository = medianyRepository;
        this.profilRepository = profilRepository;
        this.przedmiotyRepo = przedmiotyRepo;
        this.uczenRepository = uczenRepository;
        this.zainteresowaniaRepo = zainteresowaniaRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
        this.ulubionaSzkolaRepo = ulubionaSzkolaRepo;
        this.userRepository = userRepository;
        this.galleryUserRepo = galleryUserRepo;
    }

    @RequestMapping(value = "/szkola", method = GET)
    public List<szkola> getSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<SzkolaEntity> tmp;
        List<szkola> rv = new ArrayList<szkola>();
        tmp = szkolaRepository.findAll(new Sort(Sort.Direction.ASC, "name", "miasto", "adres", "kodpocztowy", "typSzkoly"));
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        UczenEntity uczen = uczenRepository.findOne(idUsera);
        szkola szkol = null;
        String zdjecie = "img/brak.jpg";

        for (SzkolaEntity s : tmp) {
            if (galleryUserRepo.findByUserId(s.getUserId().getId()) != null) {
                zdjecie = "/api/galleryUser/" + galleryUserRepo.findByUserId(s.getUserId().getId()).getId() + ".jpg";
            }
            if (ulubionaSzkolaRepo.findBySzkolaIdAndUczenId(s.getId(), uczen.getId()) != null) {
                szkol = new szkola(s, "glyphicon-star", zdjecie);
            } else {
                szkol = new szkola(s, "glyphicon-star-empty", zdjecie);
            }
            rv.add(szkol);
        }
        return rv;
    }

    @RequestMapping(value = "/szkola2", method = GET)
    public List<szkola> getSzkola2(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<SzkolaEntity> tmp;
        List<szkola> rv = new ArrayList<szkola>();
        tmp = szkolaRepository.findAll(new Sort(Sort.Direction.ASC, "name", "miasto", "adres", "kodpocztowy", "typSzkoly"));
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        UczenEntity uczen = uczenRepository.findOne(idUsera);
        szkola szkol = null;

        for (SzkolaEntity s : tmp) {
            String zdjecie = "img/brak.jpg";
            if (galleryUserRepo.findByUserId(s.getUserId().getId()) != null) {
                zdjecie = "/api/galleryUser/" + galleryUserRepo.findByUserId(s.getUserId().getId()).getId() + ".jpg";
            }
            szkola sz = new szkola(s, "glyphicon-star", zdjecie);
            rv.add(sz);
            Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "SZ: " + sz.getZdjecie() + ", " + sz.getSzkola().getName());

        }
        return rv;
    }

    @RequestMapping(value = "/CurrentSzkola", method = GET)
    public SuperSzkola getCurrentSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        User user = userRepository.findById(currentUser.getId());

        SzkolaEntity szkola = szkolaRepository.findByUserId(currentUser.getId());

        SuperSzkola superSzkola;
        superSzkola = new SuperSzkola(user.getLogin(),
                user.getPassword(),
                user.getEmail(),
                szkola.getName(),
                szkola.getNumer(),
                szkola.getMiasto(),
                szkola.getAdres(),
                szkola.getKodpocztowy(),
                szkola.getTypSzkoly(),
                szkola.getRodzajSzkoly(),
                user.getGalleryId());
        return superSzkola;
    }

    @RequestMapping(value = "/szkola/{id}", method = GET)
    public SzkolaEntity getSzkolaById(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {
        final SzkolaEntity szkola = szkolaRepository.findById(id);
        return szkola;
    }

    @RequestMapping(value = "/szukaneSzkoly/{nazwa}", method = GET)
    public List<szkola> getSzukaneSzkoly(@PathVariable String nazwa, final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<SzkolaEntity> tmp;
        List<szkola> rv = new ArrayList<szkola>();
        tmp = szkolaRepository.findAll(new Sort(Sort.Direction.ASC, "name", "miasto", "adres", "kodpocztowy", "typSzkoly"));
        for (SzkolaEntity s : tmp) {
            if ((s.getName().toLowerCase()).indexOf(nazwa.toLowerCase()) >= 0) {
                CurrentUser currentUser = null;
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                currentUser = (CurrentUser) auth.getPrincipal();
                Integer idUsera = currentUser.getId();
                UczenEntity uczen = uczenRepository.findOne(idUsera);
                szkola szkol = null;
                String zdjecie = "";
                if (galleryUserRepo.findByUserId(s.getUserId().getId()) != null) {
                    zdjecie = "/api/galleryUser/" + galleryUserRepo.findByUserId(s.getUserId().getId()).getId() + ".jpg";
                } else {
                    zdjecie = "img/brak.jpg";
                }
                if (ulubionaSzkolaRepo.findBySzkolaIdAndUczenId(s.getId(), uczen.getId()) != null) {
                    szkol = new szkola(s, "glyphicon-star", zdjecie);
                } else {
                    szkol = new szkola(s, "glyphicon-star-empty", zdjecie);
                }
                rv.add(szkol);
            }
        }
        return rv;
    }

    @RequestMapping(value = "/ProponowaneSzkoly", method = GET)
    public List<proponowaneSzkoly> getProponowaneSzkoly(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepository.findByUserId(idUsera).getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        List<proponowaneSzkolyEntity> proponowane = new ArrayList<proponowaneSzkolyEntity>();
        List<proponowaneSzkolyEntity> rv = new ArrayList<proponowaneSzkolyEntity>();
        List<przedmiotyEntity> przedmioty = przedmiotyRepo.findAll(new Sort(Sort.Direction.ASC, "name", "kategoria"));
        final UczenEntity uczen = uczenRepository.findById(idUcznia);
        final String typ = uczen.getCzegoSzukam();
        Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG TYP: " + typ);

        List<UlubionaSzkolaEntity2> rv2;
        List<SzkolaEntity> tmp;
        rv2 = ulubionaSzkolaRepo.findByUczenId(uczen.getId());
        tmp = szkolaRepository.findAll();

        List<ProfilEntity> profile;
        if ("Szkoła Średnia dowolnego typu".equals(typ)) {
            profile = profilRepository.findSzkolySrednie("Liceum", "Technikum", "Szkoła Zawodowa");
        } else {
            profile = profilRepository.findByTypSzkoly(typ);
        }

        for (ProfilEntity prof : profile) {
            Integer punktacja = 0;
            for (przedmiotyEntity przed : przedmioty) {
                Integer stZaint = 0;
                Integer ocenaPrzed = 0;
                Integer mediana = 0;
                if (medianyRepository.findByUczenId2(idUcznia) != null) {
                    mediana = medianyRepository.findByUczenId2(idUcznia).getMediana();
                }
                Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG: " + uczen.getName() + " - mediana " + mediana);
                if (zainteresowaniaRepo.getStopienZaintByUczenAndPrzedmiot(idUcznia, przed.getId()) != null) {
                    stZaint = zainteresowaniaRepo.getStopienZaintByUczenAndPrzedmiot(idUcznia, przed.getId());
                }
                if (ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil(przed.getId(), prof.getId()) != null) {
                    ocenaPrzed = ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil(przed.getId(), prof.getId());
                }
                if (stZaint >= mediana) {
                    punktacja = punktacja + stZaint * ocenaPrzed;
                }

            }
            proponowaneSzkolyEntity p = new proponowaneSzkolyEntity(uczen, prof, punktacja);
            proponowane.add(p);
        }

        Collections.sort(proponowane, new proponowaneSzkolyEntity());

        for (proponowaneSzkolyEntity p : proponowane) {
            Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG3: " + p.getUczenId().getName() + ", " + p.getProfilId().getProfil_nazwa() + ", " + p.getProfilId().getSzkola().getName() + "," + p.getPunktacja());
        }
        int ilosc = 5;
        if (proponowane.size() < 3) {
            ilosc = proponowane.size();
        }
        Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "ilosc " + ilosc);

        //Collections.sort(rv, new proponowaneSzkolyEntity());
        List<proponowaneSzkoly> szkoly = new ArrayList<proponowaneSzkoly>();
        int i = 0;
        for (int j = proponowane.size() - 1; j > 0; j--) {
            proponowaneSzkolyEntity s = proponowane.get(j);
            if (i < ilosc) {
                Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG4: " + s.getUczenId().getName() + ", " + s.getProfilId().getProfil_nazwa() + ", " + s.getProfilId().getSzkola().getName() + "," + s.getPunktacja());

                if (s.getPunktacja() != 0) {
                    String zdjecie = "";
                    if (galleryUserRepo.findByUserId(s.getProfilId().getSzkola().getUserId().getId()) != null) {
                        zdjecie = "/api/galleryUser/" + galleryUserRepo.findByUserId(s.getProfilId().getSzkola().getUserId().getId()).getId() + ".jpg";
                    } else {
                        zdjecie = "img/brak.jpg";
                    }
                    Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "ZDJECIE: " + zdjecie);
                    proponowaneSzkoly szk = new proponowaneSzkoly(s, "glyphicon-star-empty", zdjecie);
                    if (ulubionaSzkolaRepo.findBySzkolaIdAndUczenId(s.getProfilId().getSzkola().getId(), uczen.getId()) != null) {
                        szk = new proponowaneSzkoly(s, "glyphicon-star", zdjecie);
                    }
                    szkoly.add(szk);
                    i++;
                    Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG5: " + s.getUczenId().getName() + ", " + s.getProfilId().getProfil_nazwa() + ", " + s.getProfilId().getSzkola().getName() + "," + s.getPunktacja());
                }
            }
        }
        return szkoly;
    }

    @RequestMapping(value = "/szkola", method = POST)
    @PreAuthorize("isAuthenticated()")
    public SzkolaEntity createSzkola(final @RequestBody @Valid SzkolaCmd newSzkola, final BindingResult bindingResult) {
        // if(bindingResult.hasErrors()) {
        //     throw new IllegalArgumentException("Invalid arguments.");
        // }
        User user = userRepository.findById(2);
        Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOGG: " + newSzkola.getNumer());
        final SzkolaEntity szkola = new SzkolaEntity(newSzkola.getName(), newSzkola.getNumer(), newSzkola.getMiasto(), newSzkola.getAdres(), newSzkola.getKodpocztowy(), newSzkola.getTypSzkoly(), newSzkola.getRodzajSzkoly(), user);
        //Logger.getLogger(SzkolaController.class.getName()).log(Level.SEVERE, "LOG GALLERY: " + newSzkola.getGalleryId().getId());
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
//        if (galleryUserRepo.findByUserId(idUsera) != null) {
//            //szkola.setGalleryId(newSzkola.getGalleryId());
//            szkola.setGalleryId(galleryUserRepo.findByUserId(idUsera));
//        } else {
//            szkola.setGalleryId(galleryUserRepo.findOne(1));
//        }
        return this.szkolaRepository.save(szkola);
    }

    @RequestMapping(value = "/szkola/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteSzkola(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final SzkolaEntity szkola = szkolaRepository.findById(id);
        szkolaRepository.delete(szkola);

        //return uczen;
    }

    @RequestMapping(value = "/szkola", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public SzkolaEntity updateSzkola(final @PathVariable Integer id, final @RequestBody @Valid SzkolaCmd updatedSzkola, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        User user = userRepository.findById(idUsera);
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        SzkolaEntity szkola = szkolaRepository.findByUserId(idUsera);
        szkola.setName(updatedSzkola.getName());
        szkola.setNumer(updatedSzkola.getNumer());
        szkola.setMiasto(updatedSzkola.getMiasto());
        szkola.setAdres(updatedSzkola.getAdres());
        szkola.setKodpocztowy(updatedSzkola.getKodpocztowy());
        szkola.setTypSzkoly(updatedSzkola.getTypSzkoly());
        szkola.setRodzajSzkoly(updatedSzkola.getRodzajSzkoly());
        szkola.setUserId(user);

        if (szkola == null) {
            throw new ResourceNotFoundException();
        }

        return this.szkolaRepository.save(szkola);
    }

    @RequestMapping(value = "/editSchool", method = RequestMethod.PUT)
    public SuperSzkola editSchool(final @RequestBody @Valid SuperSzkola updatedSzkola) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        User user = userRepository.findById(currentUser.getId());

        SzkolaEntity szkola = szkolaRepository.findByUserId(currentUser.getId());

        SuperSzkola superSzkola;
        superSzkola = new SuperSzkola(user.getLogin(),
                user.getPassword(),
                user.getEmail(),
                szkola.getName(),
                szkola.getNumer(),
                szkola.getMiasto(),
                szkola.getAdres(),
                szkola.getKodpocztowy(),
                szkola.getTypSzkoly(),
                szkola.getRodzajSzkoly(),
                user.getGalleryId());
        return superSzkola;
    }
}
