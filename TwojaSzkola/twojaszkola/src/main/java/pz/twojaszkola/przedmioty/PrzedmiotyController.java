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
package pz.twojaszkola.przedmioty;

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
public class PrzedmiotyController {
        private final PrzedmiotyRepository przedmiotyRepository;

        @Autowired
        public PrzedmiotyController(final PrzedmiotyRepository przedmiotyRepository) {
            this.przedmiotyRepository = przedmiotyRepository;
        }
        
        @RequestMapping(value = "/przedmioty", method = GET)
        public List<PrzedmiotyEntity> getPrzedmioty(final @RequestParam(required = false, defaultValue = "false") boolean all) {
            List<PrzedmiotyEntity> rv;
            rv = przedmiotyRepository.findAll(new Sort(Sort.Direction.ASC, "name", "kategoria"));
             //rv = przedmiotyRepository.findByName("matematyka");
            return rv;
        }
        
        @RequestMapping(value = "/przedmioty", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public PrzedmiotyEntity createPrzedmioty(final @RequestBody @Valid PrzedmiotyCmd newPrzedmiot, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final PrzedmiotyEntity przedmiot = new PrzedmiotyEntity(newPrzedmiot.getName(), newPrzedmiot.getKategoria());
            return this.przedmiotyRepository.save(przedmiot);	
        }
        
        @RequestMapping(value = "/przedmioty/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public PrzedmiotyEntity updatePrzedmiot(final @PathVariable Integer id, final @RequestBody @Valid PrzedmiotyCmd updatedPrzedmiot, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final PrzedmiotyEntity przedmiot = przedmiotyRepository.findOne(id);
		
            if(przedmiot == null) {
                throw new ResourceNotFoundException();
            } 
            
            return przedmiot;
        }
}
