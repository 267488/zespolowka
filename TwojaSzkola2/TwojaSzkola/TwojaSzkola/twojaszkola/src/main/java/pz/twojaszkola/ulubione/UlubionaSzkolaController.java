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
package pz.twojaszkola.ulubione;

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
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class UlubionaSzkolaController {

     private final UlubionaSzkolaRepository ulubionaSzkolaRepository;
     private final UczenRepository uczenRepository;
     private final SzkolaRepository szkolaRepository;
     
    @Autowired
    public UlubionaSzkolaController(final UlubionaSzkolaRepository ulubionaSzkolaRepository, UczenRepository uczenRepository, SzkolaRepository szkolaRepository) {
        this.ulubionaSzkolaRepository = ulubionaSzkolaRepository;
        this.uczenRepository = uczenRepository;
        this.szkolaRepository = szkolaRepository;
    }
        
    @RequestMapping(value = "/ulubioneSzkoly", method = GET)
    public List<UlubionaSzkolaEntity2> getUlubionaSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<UlubionaSzkolaEntity2> rv;
        //rv = ulubionaSzkolaRepository.findAll(new Sort(Sort.Direction.ASC, "uczenId", "szkolaId"));
        rv = ulubionaSzkolaRepository.findByUczenId(2);
        return rv;
    }
        
    @RequestMapping(value = "/ulubionaSzkola/{id:\\d+}", method = POST) 
    @PreAuthorize("isAuthenticated()")
    public UlubionaSzkolaEntity2 createUlubionaSzkola(@PathVariable Integer id) {
        List<UlubionaSzkolaEntity2> rv;
        rv = ulubionaSzkolaRepository.findByUczenId(2);
        final UczenEntity uczen = uczenRepository.findById(2);
        //Logger.getLogger(UlubionaSzkolaController.class.getName()).log(Level.SEVERE, "LOG: " + newUlubionaSzkola.getId());
        final SzkolaEntity szkola = szkolaRepository.findById(id);
        boolean dodawanie=true;
        for(UlubionaSzkolaEntity2 szk : rv) {
            if(szk.getSzkolaId().getId() == id){
               dodawanie=false;
            }
        }
        if(dodawanie){
            final UlubionaSzkolaEntity2 ulubione = new UlubionaSzkolaEntity2(uczen, szkola);
            return this.ulubionaSzkolaRepository.save(ulubione);	
        }
        return null;
    }
    
    @RequestMapping(value = "/ulubionaSzkolaDelete/{id:\\d+}", method = DELETE) 
    @PreAuthorize("isAuthenticated()")
    public UlubionaSzkolaEntity2 deleteUlubionaSzkola(@PathVariable Integer id) {
        List<UlubionaSzkolaEntity2> rv;
        rv = ulubionaSzkolaRepository.findByUczenId(2);
        final UczenEntity uczen = uczenRepository.findById(2);
        //Logger.getLogger(UlubionaSzkolaController.class.getName()).log(Level.SEVERE, "LOG: " + newUlubionaSzkola.getId());
        UlubionaSzkolaEntity2 szkolaDoUsuniecia = null;
        boolean usuwanie=false;
        for(UlubionaSzkolaEntity2 szk : rv) {
            if(szk.getSzkolaId().getId() == id){
               usuwanie=true;
               szkolaDoUsuniecia = szk;
            }
        }
        if(usuwanie){
            ulubionaSzkolaRepository.delete(szkolaDoUsuniecia);	
            return szkolaDoUsuniecia;
        }
        return null;
    }
    
        @RequestMapping(value = "/ulubioneSzkoly/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public UlubionaSzkolaEntity2 updateProponowaneSzkoly(final @PathVariable Integer id, final @RequestBody @Valid UlubionaSzkolaCmd updatedUlubionaSzkola, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final UlubionaSzkolaEntity2 ulubiona = ulubionaSzkolaRepository.findOne(id);
		
            if(ulubiona == null) {
                throw new ResourceNotFoundException();
            } 
            return ulubiona;
        }
}
