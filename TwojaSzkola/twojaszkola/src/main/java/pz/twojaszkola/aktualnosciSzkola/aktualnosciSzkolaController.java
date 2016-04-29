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
package pz.twojaszkola.aktualnosciSzkola;

import java.util.List;
import javax.validation.Valid;
import java.util.Date;
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
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.support.ResourceNotFoundException;

/**
 *
 * @author KR
 */

@RestController
@RequestMapping("/api")
public class aktualnosciSzkolaController {
 
    private final aktualnosciSzkolaRepository aktualnosciSzkolaRepository;
    private final SzkolaRepository szkolaRepository;
    
    @Autowired
    public aktualnosciSzkolaController(final aktualnosciSzkolaRepository aktualnosciSzkolaRepository, final SzkolaRepository szkolaRepository){
        this.aktualnosciSzkolaRepository = aktualnosciSzkolaRepository;
        this.szkolaRepository = szkolaRepository;
    }
    
    @RequestMapping(value = "/aktualnosciSzkola", method = GET)
    public List<aktualnosciSzkolaEntity> getAktualnosciSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all){
        List<aktualnosciSzkolaEntity> rv;
        rv = aktualnosciSzkolaRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
        return rv;
    }
    
//    @RequestMapping(value = "/uczen", method = POST) 
//        @PreAuthorize("isAuthenticated()")
//        public UczenEntity createUczen(final @RequestBody @Valid UczenCmd newUczen, final BindingResult bindingResult) {
//            if(bindingResult.hasErrors()) {
//                throw new IllegalArgumentException("Invalid arguments.");
//            }
//	
//            final UczenEntity uczen = new UczenEntity(newUczen.getPesel(), newUczen.getName(), newUczen.getLastname(), newUczen.getMail(), newUczen.getPassword(), newUczen.getKodpocztowy());
//            return this.uczenRepository.save(uczen);	
//        }
    
    @RequestMapping(value = "/aktualnosciSzkola", method = POST) 
    @PreAuthorize("isAuthenticated()")
    public aktualnosciSzkolaEntity createAktualnosciSzkola(final @RequestBody @Valid aktualnosciSzkolaCmd newAktualnosciSzkola, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments KR");
        }
    
        final SzkolaEntity szkola = szkolaRepository.findById(1);
    
        final aktualnosciSzkolaEntity aktualnosciSzkola = new aktualnosciSzkolaEntity(szkola, newAktualnosciSzkola.getTytul(), newAktualnosciSzkola.getTekst(), newAktualnosciSzkola.getAutor());
        return this.aktualnosciSzkolaRepository.save(aktualnosciSzkola);
    }
    
    @RequestMapping(value = "/aktualnosciSzkola/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteAktualnosciSzkola(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final aktualnosciSzkolaEntity aktualnosciSzkola = aktualnosciSzkolaRepository.findById(id);
        aktualnosciSzkolaRepository.delete(aktualnosciSzkola);

        //return uczen;
    }
//POTEM DODAC UPDATE
    @RequestMapping(value = "/aktualnosciSzkola/{id}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public aktualnosciSzkolaEntity updateAktualnosciSzkola(final @PathVariable Integer id, final @RequestBody @Valid aktualnosciSzkolaCmd updatedAktualnosciSzkola, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
        final aktualnosciSzkolaEntity aktualnosciSzkola = aktualnosciSzkolaRepository.findById(id);
        aktualnosciSzkola.setTytul(updatedAktualnosciSzkola.getTytul());
        aktualnosciSzkola.setTekst(updatedAktualnosciSzkola.getTekst());

        return this.aktualnosciSzkolaRepository.save(aktualnosciSzkola);
    }
}
