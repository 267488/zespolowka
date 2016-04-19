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
import pz.twojaszkola.szkola.SzkolaRepository;

/**
 *
 * @author radon
 */
@RestController
@RequestMapping("/api")
public class Profile_nazwaController {
 
        private final Profil_nazwaRepository profil_nazwaRepository;

        @Autowired
        public Profile_nazwaController(final Profil_nazwaRepository profil_nazwaRepository) {            
            this.profil_nazwaRepository = profil_nazwaRepository;
        }
        
        @RequestMapping(value = "/profile_nazwa", method = GET)
        public List<Profil_nazwaEntity> getProfil_nazwa(final @RequestParam(required = false, defaultValue = "false") boolean all) {
            List<Profil_nazwaEntity> rv;
            rv = profil_nazwaRepository.findAll(new Sort(Sort.Direction.ASC, "nazwa"));
            return rv;
        }
        
        @RequestMapping(value = "/profile_nazwa", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public Profil_nazwaEntity createProfil_nazwa(final @RequestBody @Valid Profil_nazwaCmd newProfil_nazwa, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
            final Profil_nazwaEntity profil_nazwa = new Profil_nazwaEntity(newProfil_nazwa.getNazwa());
            
            return this.profil_nazwaRepository.save(profil_nazwa);	
        }
        
        @RequestMapping(value = "/profile_nazwa{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public Profil_nazwaEntity updateProfil_nazwa(final @PathVariable Integer id, final @RequestBody @Valid Profil_nazwaCmd updatedprofil, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final Profil_nazwaEntity profil_nazwa = profil_nazwaRepository.findOne(id);
		
            if(profil_nazwa == null) {
                throw new ResourceNotFoundException();
            } 
            
            return profil_nazwa;
        }
}
