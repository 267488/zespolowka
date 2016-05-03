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
package pz.twojaszkola.osiagniecia;

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
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuEntity;
import pz.twojaszkola.OcenaPrzedmiotu.OcenaPrzedmiotuRepository;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.profil.ProfilRepository;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.przedmioty.przedmiotyRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;

/**
 *
 * @author radon
 */
@RestController
@RequestMapping("/api")
public class OsiagnieciaController {

    private final OsiagnieciaRepository osiagnieciaRepository;
    private final UserRepository userRepository;
    private final SzkolaRepository szkolaRepository;
    private final przedmiotyRepository przedmiotyRepo;
    private final ProfilRepository profilRepository;
    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo;

    @Autowired
    public OsiagnieciaController(final OsiagnieciaRepository osiagnieciaRepository,
            final UserRepository userRepository,
            final SzkolaRepository szkolaRepository,
            final przedmiotyRepository przedmiotyRepo,
            final ProfilRepository profilRepository,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo) {
        this.osiagnieciaRepository = osiagnieciaRepository;
        this.userRepository = userRepository;
        this.szkolaRepository = szkolaRepository;
        this.przedmiotyRepo = przedmiotyRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
        this.profilRepository = profilRepository;
    }

    @RequestMapping(value = "/osiagniecia", method = GET)
    public List<OsiagnieciaEntity> getOsiagniecia(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<OsiagnieciaEntity> rv;
        rv = osiagnieciaRepository.findAll(new Sort(Sort.Direction.ASC, "nazwakonkursu", "przedmiot", "userId"));
        return rv;
    }

    @RequestMapping(value = "/osiagnieciaCurrentUser", method = GET)
    public List<OsiagnieciaEntity> getOsiagnieciaCurrentUser(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<OsiagnieciaEntity> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        rv = osiagnieciaRepository.findByUserId(idUsera);
        for (OsiagnieciaEntity o: rv ) {
            Logger.getLogger(OsiagnieciaController.class.getName()).log(Level.SEVERE, " OSIAGNIECIA " +  o.getNazwakonkursu());
        }
        Logger.getLogger(OsiagnieciaController.class.getName()).log(Level.SEVERE, "OSIAGNIECIA BRAK");
        return rv;
    }

    @RequestMapping(value = "/osiagniecia/{id:\\d+}", method = POST)
    @PreAuthorize("isAuthenticated()")
    public OsiagnieciaEntity createOsiagniecia(final @PathVariable Integer id,
            final @RequestBody @Valid OsiagnieciaCmd newOsiagniecie,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();

        final User user = userRepository.findById(idUsera);

        Logger.getLogger(OsiagnieciaController.class.getName()).log(Level.SEVERE, "LOG1 ID PRZEDMIOTU : " + id);

        przedmiotyEntity przedmiot = przedmiotyRepo.findById(newOsiagniecie.getPrzedmiot());
        Logger.getLogger(OsiagnieciaController.class.getName()).log(Level.SEVERE, "PRZEDMIOT NAME" + przedmiot.getName());

        boolean dodawanie = true;

        if (dodawanie) {
            Logger.getLogger(OsiagnieciaController.class.getName()).log(Level.SEVERE, "NAZWA KONKURSU " + newOsiagniecie.getNazwakonkursu());
            final OsiagnieciaEntity osiagniecie = new OsiagnieciaEntity(newOsiagniecie.getNazwakonkursu(), newOsiagniecie.getTermin(), przedmiot, newOsiagniecie.getSzczebel(), newOsiagniecie.getNagroda(), user);
            final OsiagnieciaEntity e = this.osiagnieciaRepository.save(osiagniecie);
            if (user.getRole() == "SZKOLA") {
                Integer idSzkoly = 1;//szkolaRepository.findByUserId(idUsera).getId();
                List<ProfilEntity> profile = profilRepository.findBySzkolaId(idSzkoly);
                Integer pkt = 0;
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
                for (ProfilEntity profil : profile) {
                    OcenaPrzedmiotuEntity ocena = ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil2(przedmiot.getId(), profil.getId());
                    if (ocena == null) {
                        OcenaPrzedmiotuEntity ocPrzedmiotu = new OcenaPrzedmiotuEntity(profil, przedmiot, pkt);
                        this.ocenaPrzedmiotuRepo.save(ocPrzedmiotu);
                    } else {
                        ocena.setOcena(ocena.getOcena() + pkt);
                        //ocena.setId(ocena.getId());
                        this.ocenaPrzedmiotuRepo.save(ocena);
                    }
                }
            }
            return e;
        }

        return null;
    }

    @RequestMapping(value = "/osiagniecia{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public OsiagnieciaEntity updateOsiagniecieZainteresowan(final @PathVariable Integer id, final @RequestBody @Valid OsiagnieciaCmd updatedosiagniecie, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final OsiagnieciaEntity profil = osiagnieciaRepository.findOne(id);

        if (profil == null) {
            throw new ResourceNotFoundException();
        }

        return profil;
    }
}
