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
package pz.twojaszkola.proponowaneSzkoly;

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
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.przedmioty.przedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class proponowaneSzkolyController {

     private final proponowaneSzkolyRepository proponowaneSzkolyRepo;
     
    @Autowired
    public proponowaneSzkolyController(final proponowaneSzkolyRepository proponowaneSzkolyRepo) {
        this.proponowaneSzkolyRepo = proponowaneSzkolyRepo;
    }
        
    @RequestMapping(value = "/proponowaneSzkoly", method = GET)
    public List<proponowaneSzkolyEntity> getProponowaneSzkoly(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<proponowaneSzkolyEntity> rv;
        rv = proponowaneSzkolyRepo.findAll(new Sort(Sort.Direction.ASC, "uczenId", "profilId", "punktacja"));
        //Integer id =1;
        //rv=zainteresowaniaRepo.findByPrzedmiotId(id);
        return rv;
    }
        
    @RequestMapping(value = "/uczen/{id:\\\\d+}/szkola{nazwa:\\\\d+}/proponowaneSzkoly", method = POST) 
    @PreAuthorize("isAuthenticated()")
    public proponowaneSzkolyEntity createProponowaneSzkoly(@PathVariable Integer id, @PathVariable String nazwa, final @RequestBody @Valid proponowaneSzkolyCmd newProponowaneSzkoly, final BindingResult bindingResult) {
    if(bindingResult.hasErrors()) {
        throw new IllegalArgumentException("Jakis blad z argumentami.");
    }
    
    //final przedmiotyEntity przedmiot = zainteresowaniaRepo.getPrzedmiotById(nazwa);
    //final UczenEntity uczen = zainteresowaniaRepo.getUczenById(id);
    //final UczenEntity bike = UczenRepository.findById(idU);
    //final przedmiotyEntity bike = przedmiotyRepository.findById(idP);
   
    final proponowaneSzkolyEntity propozycje = new proponowaneSzkolyEntity(newProponowaneSzkoly.getUczenId(), newProponowaneSzkoly.getProfilId(), newProponowaneSzkoly.getPunktacja());
            return this.proponowaneSzkolyRepo.save(propozycje);	
    }
        
        @RequestMapping(value = "/proponowaneSzkoly/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public proponowaneSzkolyEntity updateProponowaneSzkoly(final @PathVariable Integer id, final @RequestBody @Valid proponowaneSzkolyCmd updatedZainteresowania, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final proponowaneSzkolyEntity propozycje = proponowaneSzkolyRepo.findOne(id);
		
            if(propozycje == null) {
                throw new ResourceNotFoundException();
            } 
            
            return propozycje;
        }
}
