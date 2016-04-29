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
public class ProfileController {

    private final ProfilRepository profilRepository;
    private final SzkolaRepository szkolaRepository;
    private final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepo;
    private final przedmiotyRepository przedmiotyRepo;
    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo;

    @Autowired
    public ProfileController(final ProfilRepository profilRepository,           
            final SzkolaRepository szkolaRepository,
            final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepo,
            final przedmiotyRepository przedmiotyRepo,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo) {
        this.profilRepository = profilRepository;
        this.szkolaRepository = szkolaRepository;
        this.rozszerzonePrzedmiotyRepo = rozszerzonePrzedmiotyRepo;
        this.przedmiotyRepo = przedmiotyRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
    }

    @RequestMapping(value = "/profile", method = GET)
    public List<ProfilEntity> getProfil(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ProfilEntity> rv;
        rv = profilRepository.findAll(new Sort(Sort.Direction.ASC, "profilNazwa", "szkola"));
        return rv;
    }
    
    @RequestMapping(value = "/profile/{id}/przedmioty", method = GET)
    public List<RozszerzonePrzedmiotyEntity> getProfilRozszerzone(final @PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {
        final ProfilEntity profil = profilRepository.findOne(id);
        
        return profil.getRozszerzonePrzedmioty();
    }

    @RequestMapping(value = "/profile", method = POST)
    @PreAuthorize("isAuthenticated()")
    public ProfilEntity createProfil(
            final @RequestBody @Valid RozszerzonePrzedmiotyCmd[] newRozszerzone,
            final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        

        Integer idSzkoly = 1; //////////////////////////////ID SZKOLY////////////////////////////////////////

        final SzkolaEntity szkola = szkolaRepository.findByUserId(currentUser.getId());

        
        
        /*List<ProfilEntity> rv;
        Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG1 ID PROFIL_NAZWA : " + id);
        //final przedmiotyEntity przedmiot = przedmiotyRepo.findById(newRozszerzone.getPrzedmiotId());

        List<przedmiotyEntity> przedmioty = new ArrayList<przedmiotyEntity>();
        for (int i = 0; i < newRozszerzone.length; i++) {
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG4 ID " + i + "  przedmiot: " + newRozszerzone[i].getPrzedmiotId());
            if (newRozszerzone[i].getPrzedmiotId() != null) {
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG4 ID " + i + "  przedmiot: " + przedmiotyRepo.findById(idSzkoly).getName());
                przedmioty.add(przedmiotyRepo.findById(newRozszerzone[i].getPrzedmiotId()));
            }
        }

        rv = profilRepository.findByPrzedmiotNazwaIdAndSzkola(id, szkola.getId());
        boolean dodawanie = true;
        for (ProfilEntity prof : rv) {
            if (prof.getProfil_nazwa().getId() == id) {
                dodawanie = false;
            }
        }

        if (dodawanie) {
            final ProfilEntity profil = new ProfilEntity(profil_nazwa, szkola);
            final ProfilEntity e = this.profilRepository.save(profil);
            for (przedmiotyEntity przedmiot : przedmioty) {
                RozszerzonePrzedmiotyEntity rozsz = new RozszerzonePrzedmiotyEntity(profil, przedmiot);
                Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG5 name przedmiotu: " + przedmiot.getName());

                this.rozszerzonePrzedmiotyRepo.save(rozsz);
                if (ocenaPrzedmiotuRepo.getOcenaByPrzedmiotAndProfil(przedmiot.getId(), profil.getId()) == null) {
                    OcenaPrzedmiotuEntity ocPrzedmiotu = new OcenaPrzedmiotuEntity(profil, przedmiot, 5);
                    this.ocenaPrzedmiotuRepo.save(ocPrzedmiotu);
                }
            }

            return e;
        }*/

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
        profil.setNazwa(updatedprofil.getNazwa());        

        if (profil == null) {
            throw new ResourceNotFoundException();
        }

        return profilRepository.save(profil);
    }
    
    @RequestMapping(value = "/profile/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteProfil(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final ProfilEntity profil = profilRepository.findById(id);
        profilRepository.delete(profil);

        //return uczen;
    }
}
