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
package pz.twojaszkola.zainteresowania;

import java.util.List;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import pz.twojaszkola.mediany.MedianyController;
import pz.twojaszkola.mediany.MedianyEntity;
import pz.twojaszkola.mediany.MedianyRepository;
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
public class zainteresowaniaController {

     private final zainteresowaniaRepository zainteresowaniaRepo;
     private final UczenRepository uczenRepo;
     private final przedmiotyRepository przedmiotRepo;
     private final MedianyRepository medianyRepo;
     
    @Autowired
    public zainteresowaniaController(final zainteresowaniaRepository zainteresowaniaRepo, final UczenRepository uczenRepo, final przedmiotyRepository przedmiotRepo, final MedianyRepository medianyRepo) {
        this.zainteresowaniaRepo = zainteresowaniaRepo;
        this.uczenRepo=uczenRepo;
        this.przedmiotRepo=przedmiotRepo;
        this.medianyRepo = medianyRepo;
    }
        
    @RequestMapping(value = "/zainteresowania", method = GET)
    public List<zainteresowaniaEntity> getZainteresowania(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<zainteresowaniaEntity> rv;
        rv = zainteresowaniaRepo.findAll(new Sort(Sort.Direction.ASC, "uczenId", "przedmiotId", "stopienZainteresowania"));
        return rv;
    }
    
    @RequestMapping(value = "/sumaZainteresowanUcznia", method = GET)
    public Integer getSuma(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<zainteresowaniaEntity> rv;
        Integer suma = 0;
        Integer idUcznia = 1; //////////////////ID UCZNIA//////////////////////////////////////
        rv = zainteresowaniaRepo.findByUczenId2(idUcznia);
        for(zainteresowaniaEntity z : rv){
            suma = suma + z.getStopienZainteresowania();
        }
        return suma;
    }
    
    @RequestMapping(value = "/zainteresowaniaUcznia", method = GET)
    public List<zainteresowaniaEntity> getZainteresowaniaUcznia(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<zainteresowaniaEntity> rv;
        Integer idUcznia = 1; //////////////////ID UCZNIA//////////////////////////////////////
        rv = zainteresowaniaRepo.findByUczenId2(idUcznia);
        return rv;
    }
    
    @RequestMapping(value = "/zainteresowania", method = POST) 
    @PreAuthorize("isAuthenticated()")
    public zainteresowaniaEntity createZainteresowania(final @RequestBody @Valid zainteresowaniaCmd newZainteresowania, final BindingResult bindingResult) {
   // public RozszerzonePrzedmiotyEntity createZainteresowania(){
    if(bindingResult.hasErrors()) {
        throw new IllegalArgumentException("Jakis blad z argumentami.");
    }
    
    //final przedmiotyEntity przedmiot = zainteresowaniaRepo.getPrzedmiotById(nazwa);
    
    Integer idUcznia = 1; ////////////////////////////////ID UCZNIA/////////////////////////////////////////////////////
    final UczenEntity uczen = uczenRepo.findById(idUcznia);
    Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + newZainteresowania.getPrzedmiotName());
     Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG1: " + newZainteresowania.getStopienZainteresowania());
    
    final przedmiotyEntity przedmiot = przedmiotRepo.findByName(newZainteresowania.getPrzedmiotName());
    
    List<zainteresowaniaEntity> rv;
    rv=zainteresowaniaRepo.findByPrzedmiotId(przedmiot.getId());
    boolean dodawanie=true;
        for(zainteresowaniaEntity zaint : rv) {
            if(zaint.getUczenId().getId() == uczen.getId()){
               dodawanie=false;
            }
        }
        
        if(dodawanie){
            final zainteresowaniaEntity zainteresowania = new zainteresowaniaEntity(uczen, przedmiot,newZainteresowania.getStopienZainteresowania());
            final zainteresowaniaEntity e = this.zainteresowaniaRepo.save(zainteresowania);
            
            List<Integer> lz;
            lz = zainteresowaniaRepo.findByUczenId(uczen.getId());
            Integer med = Integer.valueOf(MedianyController.med(lz));
            
            if (medianyRepo.findByUczenId2(uczen.getId()) == null) {
               //nie ma
                Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "Nie ma " + med);
          
               MedianyEntity mediana = new MedianyEntity(uczen, med);
               this.medianyRepo.save(mediana);
           } else {
              //jest
              Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "Jest" +  med);
              MedianyEntity mediana = medianyRepo.findByUczenId2(uczen.getId());
              mediana.setMediana(med);
              this.medianyRepo.save(mediana);
          }
            return e;	
        }
        return null;
    }
        
        @RequestMapping(value = "/zainteresowania/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public zainteresowaniaEntity updateZainteresowania(final @PathVariable Integer id, final @RequestBody @Valid zainteresowaniaCmd updatedZainteresowania, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final zainteresowaniaEntity zainteresowania = zainteresowaniaRepo.findOne(id);
		
            if(zainteresowania == null) {
                throw new ResourceNotFoundException();
            } 
            
            return zainteresowania;
        }
}
