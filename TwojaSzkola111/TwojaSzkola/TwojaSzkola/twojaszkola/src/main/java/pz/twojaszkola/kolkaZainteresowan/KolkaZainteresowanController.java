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
package pz.twojaszkola.kolkaZainteresowan;

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
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyCmd;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.przedmioty.przedmiotyRepository;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.user.CurrentUser;

/**
 *
 * @author radon
 */
@RestController
@RequestMapping("/api")
public class KolkaZainteresowanController {

    private final KolkaZainteresowanRepository kolkaZainteresowanRepository;
    private final SzkolaRepository szkolaRepository;
    private final przedmiotyRepository przedmiotyRepo;
    private final ProfilRepository profilRepository;
    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo;

    @Autowired
    public KolkaZainteresowanController(final KolkaZainteresowanRepository kolkaZainteresowanRepository,
            final SzkolaRepository szkolaRepository,
            final przedmiotyRepository przedmiotyRepo,
            final ProfilRepository profilRepository,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo) {
        this.kolkaZainteresowanRepository = kolkaZainteresowanRepository;
        this.szkolaRepository = szkolaRepository;
        this.przedmiotyRepo = przedmiotyRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
        this.profilRepository = profilRepository;
    }

    @RequestMapping(value = "/kolkaZainteresowan", method = GET)
    public List<KolkaZainteresowanEntity> getKolkaZainteresowan(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<KolkaZainteresowanEntity> rv;
        rv = kolkaZainteresowanRepository.findAll(new Sort(Sort.Direction.ASC, "nazwa", "przedmiot", "szkola"));
        return rv;
    }

    @RequestMapping(value = "/kolkaZainteresowanCurrentSchool", method = GET)
    public List<KolkaZainteresowanEntity> getKolkaZainteresowanCurrentSchool(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<KolkaZainteresowanEntity> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        SzkolaEntity szkola = szkolaRepository.findByUserId(idUsera);
        rv = kolkaZainteresowanRepository.findBySzkolaId(szkola.getId());
        return rv;
    }
    
    @RequestMapping(value = "/kolkaZainteresowan/{id:\\d+}", method = POST)
    @PreAuthorize("isAuthenticated()")
    public KolkaZainteresowanEntity createKolkaZainteresowan(final @PathVariable Integer id,
            final @RequestBody @Valid KolkaZainteresowanCmd newKolko,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        //final Profil_nazwaEntity profil_nazwa = profil_nazwaRepository.findById(id);

        Integer idSzkoly = 1; //////////////////////////////ID SZKOLY////////////////////////////////////////

        final SzkolaEntity szkola = szkolaRepository.findById(idSzkoly);

        Logger.getLogger(KolkaZainteresowanController.class.getName()).log(Level.SEVERE, "LOG1 ID PRZEDMIOTU : " + id);
        //final przedmiotyEntity przedmiot = przedmiotyRepo.findById(newRozszerzone.getPrzedmiotId());

        przedmiotyEntity przedmiot = przedmiotyRepo.findById(newKolko.getPrzedmiot());
        Logger.getLogger(KolkaZainteresowanController.class.getName()).log(Level.SEVERE, "PRZEDMIOT NAME" + przedmiot.getName());
        //       List<przedmiotyEntity> przedmioty = new ArrayList<przedmiotyEntity>();
//        for (int i = 0; i < newRozszerzone.length; i++) {
//            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG4 ID " + i + "  przedmiot: " + newRozszerzone[i].getPrzedmiotId());
//            if (newRozszerzone[i].getPrzedmiotId() != null) {
//                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG4 ID " + i + "  przedmiot: " + przedmiotyRepo.findById(idSzkoly).getName());
//                przedmioty.add(przedmiotyRepo.findById(newRozszerzone[i].getPrzedmiotId()));
//            }
//        }

        // rv = kolkaZainteresowanRepository.findByPrzedmiot(id);
        boolean dodawanie = true;
        //       for (KolkaZainteresowanEntity prof : rv) {
        //           if (prof.getProfil_nazwa().getId() == id) {
        //               dodawanie = false;
        //           }
        //       }
        if (dodawanie) {
            Logger.getLogger(KolkaZainteresowanController.class.getName()).log(Level.SEVERE, "NAZWA KÓŁA " + newKolko.getNazwa());
            Logger.getLogger(KolkaZainteresowanController.class.getName()).log(Level.SEVERE, "TERMIN KÓŁA " + newKolko.getTermin());
            final KolkaZainteresowanEntity kolko = new KolkaZainteresowanEntity(newKolko.getNazwa(), newKolko.getTermin(), przedmiot, szkola);
            final KolkaZainteresowanEntity e = this.kolkaZainteresowanRepository.save(kolko);
            List<ProfilEntity> profile = profilRepository.findBySzkolaId(idSzkoly);
            for (ProfilEntity profil : profile) {
                    OcenaPrzedmiotuEntity ocena = ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil2(przedmiot.getId(), profil.getId());
                if (ocena == null) {
                    OcenaPrzedmiotuEntity ocPrzedmiotu = new OcenaPrzedmiotuEntity(profil, przedmiot, 1);
                    this.ocenaPrzedmiotuRepo.save(ocPrzedmiotu);
                } else {
                    ocena.setOcena(ocena.getOcena()+1);
                    //ocena.setId(ocena.getId());
                    this.ocenaPrzedmiotuRepo.save(ocena);
                }
            }
            return e;
        }

        return null;
    }

    @RequestMapping(value = "/kolkaZainteresowan{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public KolkaZainteresowanEntity updateKolkoZainteresowan(final @PathVariable Integer id, final @RequestBody @Valid KolkaZainteresowanCmd updatedkolko, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final KolkaZainteresowanEntity profil = kolkaZainteresowanRepository.findOne(id);

        if (profil == null) {
            throw new ResourceNotFoundException();
        }

        return profil;
    }
}
