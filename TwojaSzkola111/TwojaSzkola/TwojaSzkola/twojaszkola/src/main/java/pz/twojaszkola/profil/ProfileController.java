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
package pz.twojaszkola.profil;

import java.util.ArrayList;
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
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuEntity;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuRepository;
import pz.twojaszkola.kolkaZainteresowan.KolkaZainteresowanEntity;
import pz.twojaszkola.kolkaZainteresowan.KolkaZainteresowanRepository;
import pz.twojaszkola.osiagniecia.OsiagnieciaEntity;
import pz.twojaszkola.osiagniecia.OsiagnieciaRepository;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyCmd;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.zainteresowania.ZainteresowaniaController;
import pz.twojaszkola.przedmioty.PrzedmiotyRepository;

/**
 *
 * @author radon
 */
@RestController
@RequestMapping("/api")
public class ProfileController {

    private final ProfilRepository profilRepository;
    private final SzkolaRepository szkolaRepository;
    private final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepo;
    private final PrzedmiotyRepository przedmiotyRepo;
    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo;
    private final KolkaZainteresowanRepository kolkaZainteresowanRepo;
    private final OsiagnieciaRepository osiagnieciaRepo;

    @Autowired
    public ProfileController(final ProfilRepository profilRepository,
            final SzkolaRepository szkolaRepository,
            final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepo,
            final PrzedmiotyRepository przedmiotyRepo,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo,
            final KolkaZainteresowanRepository kolkaZainteresowanRepo,
            final OsiagnieciaRepository osiagnieciaRepo) {
        this.profilRepository = profilRepository;
        this.szkolaRepository = szkolaRepository;
        this.rozszerzonePrzedmiotyRepo = rozszerzonePrzedmiotyRepo;
        this.przedmiotyRepo = przedmiotyRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
        this.kolkaZainteresowanRepo = kolkaZainteresowanRepo;
        this.osiagnieciaRepo = osiagnieciaRepo;
    }

    @RequestMapping(value = "/profile", method = GET)
    public List<ProfilEntity> getProfil(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ProfilEntity> tmp;
        List<profil> rv = new ArrayList<profil>();
        tmp = profilRepository.findAll(new Sort(Sort.Direction.ASC, "profilNazwa", "szkola"));
        for(ProfilEntity p : tmp){
            //List<rozszerzonePrzedmiotyEntity> roz = rozszerzonePrzedmiotyRepo.
        }
        return tmp;
    }

    @RequestMapping(value = "/profileCurrentSchool", method = GET)
    public List<profil> getProfilCurrentSchool(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ProfilEntity> tmp;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        SzkolaEntity szkola = szkolaRepository.findByUserId(idUsera);
        tmp = profilRepository.findBySzkolaId(szkola.getId());
        List<profil> rv = new ArrayList<profil>();
        for(ProfilEntity p : tmp){
            List<RozszerzonePrzedmiotyEntity> roz = rozszerzonePrzedmiotyRepo.findByProfilId(p.getId());
            profil prof = new profil(p,roz);
            rv.add(prof);
        }
        return rv;
    }

    @RequestMapping(value = "/profile/{nazwa}", method = POST)
    @PreAuthorize("isAuthenticated()")
    public ProfilEntity createProfil(final @PathVariable String nazwa,
                                     final @RequestBody @Valid RozszerzonePrzedmiotyCmd[] newRozszerzone,
                                     final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        SzkolaEntity szkola = szkolaRepository.findByUserId(idUsera);

        List<ProfilEntity> rv;
        
        List<PrzedmiotyEntity> przedmioty = new ArrayList<PrzedmiotyEntity>();
        for (int i = 0; i < newRozszerzone.length; i++) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG4 ID " + i + "  przedmiot: " + newRozszerzone[i].getPrzedmiotId());
            if (newRozszerzone[i].getPrzedmiotId() != null) {
                przedmioty.add(przedmiotyRepo.findById(newRozszerzone[i].getPrzedmiotId()));
            }
        }

        rv = profilRepository.findByPrzedmiotNazwaAndSzkola(nazwa, szkola.getId());
        boolean dodawanie = true;
        for (ProfilEntity prof : rv) {
            if (prof.getProfil_nazwa() == null ? nazwa == null : prof.getProfil_nazwa().equals(nazwa)) {
                dodawanie = false;
            }
        }

        if (dodawanie) {
            final ProfilEntity profil = new ProfilEntity(nazwa, szkola);
            final ProfilEntity e = this.profilRepository.save(profil);
            for (PrzedmiotyEntity przedmiot : przedmioty) {
                RozszerzonePrzedmiotyEntity rozsz = new RozszerzonePrzedmiotyEntity(profil, przedmiot);
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG5 name przedmiotu: " + przedmiot.getName());

                this.rozszerzonePrzedmiotyRepo.save(rozsz);
                if (ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil(przedmiot.getId(), profil.getId()) == null) {
                    List<KolkaZainteresowanEntity> kolka = kolkaZainteresowanRepo.findByPrzedmiotId(przedmiot.getId());
                    Integer pkt = 0;
                    List<OsiagnieciaEntity> osiagniecia = osiagnieciaRepo.findByPrzedmiotIdAndUserId(przedmiot.getId(),idUsera);
                    
                    for(OsiagnieciaEntity osiagniecie : osiagniecia){
                        Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG " + osiagniecie.getNazwakonkursu());
                    }
                    
                    for (OsiagnieciaEntity osiagniecie : osiagniecia ) {
                        if ("Między Szkolny".equals(osiagniecie.getSzczebel()) || "Gminny".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 1;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        } else if ("Powiatowy".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 2;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        } else if ("Regionalny".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 3;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        } else if ("Wojewódzki".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 5;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        } else if ("Ogólnokrajowy".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 10;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        } else if ("Międzynarodowy".equals(osiagniecie.getSzczebel())) {
                            pkt = pkt + 20;
                            if ("I miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 3;
                            } else if ("II miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 2;
                            } else if ("III miejsce".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 1;
                            } else if ("finalista".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 5;
                            } else if ("laureat".equals(osiagniecie.getNagroda())) {
                                pkt = pkt * 10;
                            }
                        }
                    }
                    OcenaPrzedmiotuEntity ocPrzedmiotu = new OcenaPrzedmiotuEntity(profil, przedmiot, 1 + kolka.size() + pkt);
                    this.ocenaPrzedmiotuRepo.save(ocPrzedmiotu);
                }
            }

            return e;
        }

        return null;
    }

    @RequestMapping(value = "/profile/{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ProfilEntity updateProfil(final @PathVariable Integer id, final @RequestBody @Valid ProfilCmd updatedprofil, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final ProfilEntity profil = profilRepository.findOne(id);
        profil.setProfil_nazwa(updatedprofil.getProfilNazwa());

        if (profil == null) {
            throw new ResourceNotFoundException();
        }

        return profilRepository.save(profil);
    }

    @RequestMapping(value = "/profilDelete/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteProfil(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final ProfilEntity profil = profilRepository.findById(id);
        profilRepository.delete(profil);

        //return uczen;
    }
}
