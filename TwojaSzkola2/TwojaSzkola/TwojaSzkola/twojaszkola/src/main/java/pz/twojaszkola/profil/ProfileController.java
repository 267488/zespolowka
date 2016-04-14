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
package pz.twojaszkola.profil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
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
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;

/**
 *
 * @author radon
 */

@RestController
@RequestMapping("/api")
public class ProfileController {
 
        private final ProfilRepository profilRepository;
        private final Profil_nazwaRepository profil_nazwaRepository;
        private final SzkolaRepository szkolaRepository;
        @Autowired
        public ProfileController(final ProfilRepository profilRepository,
                                 final Profil_nazwaRepository profil_nazwaRepository,
                                 final SzkolaRepository szkolaRepository) {            
            this.profilRepository = profilRepository;
            this.profil_nazwaRepository = profil_nazwaRepository;
            this.szkolaRepository = szkolaRepository;
        }
        
        @RequestMapping(value = "/profile", method = GET)
        public List<ProfilEntity> getProfil(final @RequestParam(required = false, defaultValue = "false") boolean all) {
            List<ProfilEntity> rv;
            rv = profilRepository.findAll(new Sort(Sort.Direction.ASC, "profilNazwa", "szkola"));
            //System.out.println("\n\nProfileController: metoda GET\n\n");
            return rv;
        }
        
        @RequestMapping(value = "/profile/{id:\\d+}", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public ProfilEntity createProfil(final @PathVariable Integer id,
                final @RequestBody @Valid ProfilCmd newProfil, 
                final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
            final Profil_nazwaEntity profil_nazwa = profil_nazwaRepository.findById(id);
            final SzkolaEntity szkola = szkolaRepository.findById(1);
          
            List<ProfilEntity> rv;
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG1 ID PRZEDMIOTU : " + id);
            Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, "LOG2 ID SZKOLY: " + szkola.getId());
            rv=profilRepository.findByPrzedmiotNazwaIdAndSzkola(id, szkola.getId());
            boolean dodawanie=true;
            for(ProfilEntity prof : rv) {
                if(prof.getProfil_nazwa().getId() == id){
                    dodawanie=false;
                }
            }
        
            if(dodawanie){
                final ProfilEntity profil = new ProfilEntity(profil_nazwa, szkola);
            
                return this.profilRepository.save(profil);	
            }
            
            return null;
        }
        
        @RequestMapping(value = "/profile{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public ProfilEntity updateProfil(final @PathVariable Integer id, final @RequestBody @Valid ProfilCmd updatedprofil, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final ProfilEntity profil = profilRepository.findOne(id);
		
            if(profil == null) {
                throw new ResourceNotFoundException();
            } 
            
            return profil;
        }
}
