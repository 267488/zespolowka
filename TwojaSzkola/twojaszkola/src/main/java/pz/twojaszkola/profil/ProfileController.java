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
import java.util.Iterator;
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
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyCmd;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyEntity;
import pz.twojaszkola.rozszerzonePrzedmioty.RozszerzonePrzedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.user.CurrentUser;
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

    @Autowired
    public ProfileController(final ProfilRepository profilRepository,           
            final SzkolaRepository szkolaRepository,
            final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepo,
            final PrzedmiotyRepository przedmiotyRepo,
            final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepo) {
        this.profilRepository = profilRepository;
        this.szkolaRepository = szkolaRepository;
        this.rozszerzonePrzedmiotyRepo = rozszerzonePrzedmiotyRepo;
        this.przedmiotyRepo = przedmiotyRepo;
        this.ocenaPrzedmiotuRepo = ocenaPrzedmiotuRepo;
    }

    @RequestMapping(value = "/profile", method = GET)
    public List<Profil> getProfil(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        
        List<Profil> pro = new ArrayList();
        Profil p_tmp = new Profil();
        List<ProfilEntity> profile;
        List<RozszerzonePrzedmiotyEntity> przedmiotyRoz;
        PrzedmiotyEntity przedmioty;
        profile = profilRepository.findBySzkolaId(szkolaRepository.findByUserId(currentUser.getId()).getId());
        if(!profile.isEmpty()){
            System.out.println("PROFILE NIE SA PUSTE");
            
        for(ProfilEntity p : profile)
        {
        p_tmp.setNazwa(p.getNazwa()); 
        System.out.println("PIERWSZY FOR");
        System.out.println("PIERWSZY FOR p: "+p.getNazwa());
        
        System.out.println("PIERWSZY FOR: test");
        przedmiotyRoz = rozszerzonePrzedmiotyRepo.findByProfilId(p.getId());
        System.out.println("PIERWSZY FOR: test2");
        if(!przedmiotyRoz.isEmpty())
        {
            List<Integer> int_tmp = new ArrayList();
            List<String> string_tmp = new ArrayList();
            System.out.println("DRUGI IF");
        for(RozszerzonePrzedmiotyEntity r : przedmiotyRoz)
        {
            
        System.out.println("DRUGI FOR");
        przedmioty = przedmiotyRepo.findById(r.getPrzedmiotId().getId());
        System.out.println("DRUGI FOR: test"+przedmioty.getId()+" "+przedmioty.getName());
        int_tmp.add(przedmioty.getId());
        string_tmp.add(przedmioty.getName());
        System.out.println("DRUGI FOR: test2");
        }
        p_tmp.setPrzedmiotIds(int_tmp);
        p_tmp.setPrzedmiotNazwy(string_tmp);
        }
        
        pro.add(p_tmp);
        }
        System.out.println("AFTER FORS");
        return pro;
        }
        else {System.out.println("AFTER FORS");return null;}
    }
        
    

    @RequestMapping(value = "/setProfil", method = POST)
    public boolean createProfil(
            final @RequestBody Profil profil) {        
        
        System.out.println("SET PROFIL CONTROLLER");
        
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
  
        boolean dodawanie = false;
        final SzkolaEntity szkola = szkolaRepository.findByUserId(currentUser.getId());

        List<ProfilEntity> profile = profilRepository.findAll();
        
        System.out.println("dodawanie: "+dodawanie);
        System.out.println(profile);
        if(!profile.isEmpty())
        {
        for(ProfilEntity p : profile)
        {
            System.out.println("SET PROFIL for profile");
            if(p.getNazwa().equals(profil.getNazwa())){System.out.println("SET PROFIL jest juz taki");dodawanie=false;break;}
            else {System.out.println("SET PROFIL nie ma takiego");dodawanie=true;}
            System.out.println("dodawanie: "+dodawanie);
        }
        }
        else{dodawanie=true;}
        System.out.println("dodawanie: "+dodawanie);
        if(dodawanie)
        {
        ProfilEntity newProfil = new ProfilEntity(profil.getNazwa(),szkola);
        
        profilRepository.save(newProfil);
        
        for(int i : profil.getPrzedmiotIds())
        {   
            PrzedmiotyEntity przedmiot = przedmiotyRepo.findById(i);
            rozszerzonePrzedmiotyRepo.save(new RozszerzonePrzedmiotyEntity(newProfil, przedmiot));
            ocenaPrzedmiotuRepo.save(new OcenaPrzedmiotuEntity(newProfil,przedmiot,5));
        }
        System.out.println("SET PROFIL return true");
        return true;
        }
        else{ System.out.println("SET PROFIL return false");return false;}
        
        
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

        
    }

    @RequestMapping(value = "/profile{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ProfilEntity updateProfil(final @PathVariable Integer id, final @RequestBody @Valid ProfilCmd updatedprofil, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final ProfilEntity profil = profilRepository.findOne(id);

        if (profil == null) {
            throw new ResourceNotFoundException();
        }

        return profil;
    }
}
