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

package pz.twojaszkola.szkola;

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
public class SzkolaController {
 
        private final SzkolaRepository szkolaRepository;

        @Autowired
        public SzkolaController(final SzkolaRepository szkolaRepository) {
            this.szkolaRepository = szkolaRepository;
        }
        
        @RequestMapping(value = "/szkola", method = GET)
        public List<SzkolaEntity> getSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
            List<SzkolaEntity> rv;
            rv = szkolaRepository.findAll(new Sort(Sort.Direction.ASC, "name", "mail", "password", "adres", "kod_pocztowy"));
            return rv;
        }
        
        @RequestMapping(value = "/szkola", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public SzkolaEntity createSzkola(final @RequestBody @Valid SzkolaCmd newSzkola, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            
            final SzkolaEntity szkola = new SzkolaEntity(newSzkola.getName(), newSzkola.getMail(), newSzkola.getPassword(), newSzkola.getAdres(),newSzkola.getKod_pocztowy());
            return this.szkolaRepository.save(szkola);	
        }
        
        @RequestMapping(value = "/szkola/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public SzkolaEntity updateSzkola(final @PathVariable Integer id, final @RequestBody @Valid SzkolaCmd updatedSzkola, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final SzkolaEntity szkola = szkolaRepository.findOne(id);
		
            if(szkola == null) {
                throw new ResourceNotFoundException();
            } 
            
            return szkola;
        }
}

