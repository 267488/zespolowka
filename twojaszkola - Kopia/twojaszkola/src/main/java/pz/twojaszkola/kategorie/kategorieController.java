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
package pz.twojaszkola.kategorie;

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

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class kategorieController {
    
     private final kategorieRepository kategorieRepository;

        @Autowired
        public kategorieController(final kategorieRepository kategorieRepository) {
            this.kategorieRepository = kategorieRepository;
        }
        
        @RequestMapping(value = "/kategorie", method = GET)
        public List<kategorieEntity> getUczen(final @RequestParam(required = false, defaultValue = "false") boolean all) {
            List<kategorieEntity> rv;
            rv = kategorieRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
            return rv;
        }
        
        @RequestMapping(value = "/kategorie", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public kategorieEntity createUczen(final @RequestBody @Valid kategorieCmd newKategorie, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final kategorieEntity kategorie = new kategorieEntity(newKategorie.getName());
            return this.kategorieRepository.save(kategorie);	
        }
        
        @RequestMapping(value = "/kategorie/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public kategorieEntity updateKategorie(final @PathVariable Integer id, final @RequestBody @Valid kategorieCmd updatedKategorie, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final kategorieEntity kategorie = kategorieRepository.findOne(id);
		
            if(kategorie == null) {
                throw new ResourceNotFoundException();
            } 
            
            return kategorie;
        }
    
}
