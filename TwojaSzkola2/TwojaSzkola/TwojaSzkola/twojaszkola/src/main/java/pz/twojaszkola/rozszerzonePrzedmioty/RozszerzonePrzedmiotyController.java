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
package pz.twojaszkola.rozszerzonePrzedmioty;

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
import pz.twojaszkola.kategorie.kategorieEntity;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.profil.ProfilRepository;
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
public class RozszerzonePrzedmiotyController {

     private final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepository;
     private final ProfilRepository profilRepository;
     private final przedmiotyRepository przedmiotyRepo;
     
    @Autowired
    public RozszerzonePrzedmiotyController(final RozszerzonePrzedmiotyRepository rozszerzonePrzedmiotyRepository, ProfilRepository profilRepository, przedmiotyRepository przedmiotyRepo) {
        this.rozszerzonePrzedmiotyRepository = rozszerzonePrzedmiotyRepository;
        this.profilRepository = profilRepository;
        this.przedmiotyRepo = przedmiotyRepo;
    }
        
    @RequestMapping(value = "/rozszerzonePrzedmioty", method = GET)
    public List<RozszerzonePrzedmiotyEntity> getRozszerzonePrzedmioty(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<RozszerzonePrzedmiotyEntity> rv;
        rv = rozszerzonePrzedmiotyRepository.findAll(new Sort(Sort.Direction.ASC, "profilId", "przedmiotId"));
        return rv;
    }
        
    @RequestMapping(value = "/rozszerzonePrzedmioty", method = POST) 
    @PreAuthorize("isAuthenticated()")
    public RozszerzonePrzedmiotyEntity createRozszerzonePrzedmioty(final @RequestBody @Valid RozszerzonePrzedmiotyCmd newRozszerzonePrzedmioty, final BindingResult bindingResult) {
   // public zainteresowaniaEntity createZainteresowania(){
    if(bindingResult.hasErrors()) {
        throw new IllegalArgumentException("Jakis blad z argumentami.");
    }
    
    //final przedmiotyEntity przedmiot = zainteresowaniaRepo.getPrzedmiotById(nazwa);
    final przedmiotyEntity przedmiot = przedmiotyRepo.findById(3);
    final ProfilEntity profil = profilRepository.findById(1);
 
    final RozszerzonePrzedmiotyEntity rozszerzone = new RozszerzonePrzedmiotyEntity(profil, przedmiot);
            return this.rozszerzonePrzedmiotyRepository.save(rozszerzone);	
    }
        
        @RequestMapping(value = "/rozszerzonePrzedmioty/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public RozszerzonePrzedmiotyEntity updateRozszerzonePrzedmioty(final @PathVariable Integer id, final @RequestBody @Valid RozszerzonePrzedmiotyCmd updatedRozszerzonePrzedmioty, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final RozszerzonePrzedmiotyEntity rozszerzone = rozszerzonePrzedmiotyRepository.findOne(id);
		
            if(rozszerzone == null) {
                throw new ResourceNotFoundException();
            } 
            
            return rozszerzone;
        }
}
