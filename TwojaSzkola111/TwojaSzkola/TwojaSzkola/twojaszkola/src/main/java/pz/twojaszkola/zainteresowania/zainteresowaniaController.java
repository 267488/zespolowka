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

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import pz.twojaszkola.mediany.MedianyController;
import pz.twojaszkola.mediany.MedianyEntity;
import pz.twojaszkola.mediany.MedianyRepository;
import pz.twojaszkola.przedmioty.PrzedmiotyEntity;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.przedmioty.PrzedmiotyRepository;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class ZainteresowaniaController {

    private final ZainteresowaniaRepository zainteresowaniaRepo;
    private final UczenRepository uczenRepo;
    private final PrzedmiotyRepository przedmiotRepo;
    private final MedianyRepository medianyRepo;

    @Autowired
    public ZainteresowaniaController(final ZainteresowaniaRepository zainteresowaniaRepo, final UczenRepository uczenRepo, final PrzedmiotyRepository przedmiotRepo, final MedianyRepository medianyRepo) {
        this.zainteresowaniaRepo = zainteresowaniaRepo;
        this.uczenRepo = uczenRepo;
        this.przedmiotRepo = przedmiotRepo;
        this.medianyRepo = medianyRepo;
    }

    @RequestMapping(value = "/zainteresowania", method = GET)
    public List<ZainteresowaniaEntity> getZainteresowania(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ZainteresowaniaEntity> rv;
        rv = zainteresowaniaRepo.findAll(new Sort(Sort.Direction.ASC, "uczenId", "przedmiotId", "stopienZainteresowania"));
        return rv;
    }

    
    @RequestMapping(value = "/sumaZainteresowanUcznia", method = GET)
    public Integer getSuma(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ZainteresowaniaEntity> rv;
        Integer suma = 0;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepo.findByUserId(idUsera).getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        //////////////////ID UCZNIA//////////////////////////////////////
        rv = zainteresowaniaRepo.findByUczenId2(idUcznia);
        for (ZainteresowaniaEntity z : rv) {
            suma = suma + z.getStopienZainteresowania();
        }
        return suma;
    }

    @RequestMapping(value = "/zainteresowaniaUcznia", method = GET)
    public List<Zainteresowanie> getZainteresowaniaUcznia(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ZainteresowaniaEntity> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepo.findByUserId(idUsera).getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);
        rv = zainteresowaniaRepo.findByUczenId2(idUcznia);
        
        List<Zainteresowanie> za = new ArrayList<>();
        
        for(ZainteresowaniaEntity z : rv)
        {
            Zainteresowanie tmp = new Zainteresowanie();
            
            tmp.setId(z.getPrzedmiotId().getId());
            tmp.setValue(z.getStopienZainteresowania());
            za.add(tmp);
        }
        return za;
    }
    
    @RequestMapping(value = "/StopnieZainteresowanUcznia", method = GET)
    public String getStopnieZainteresowanUcznia(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<ZainteresowaniaEntity> rv;
        List<Integer> st = new ArrayList<Integer>();
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepo.findByUserId(idUsera).getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);
        rv = zainteresowaniaRepo.findByUczenId2(idUcznia);
        for(ZainteresowaniaEntity z : rv){
            st.add(z.getStopienZainteresowania());
        }
        String string = st.toString();
        String str = string.substring(2, string.length()-2);
        
        return str;
    }
    
    @RequestMapping(value = "/zainteresowaniaUcznia/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteZainteresowaniaUcznia(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final ZainteresowaniaEntity zainteresowania = zainteresowaniaRepo.findOne(id);
        zainteresowaniaRepo.delete(zainteresowania);

    }

    @RequestMapping(value = "/zainteresowania", method = POST)
    @PreAuthorize("isAuthenticated()")
    public ZainteresowaniaEntity createZainteresowania(final @RequestBody @Valid ZainteresowaniaCmd newZainteresowania, final BindingResult bindingResult) {
        // public RozszerzonePrzedmiotyEntity createZainteresowania(){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Jakis blad z argumentami.");
        }

        //final przedmiotyEntity przedmiot = zainteresowaniaRepo.getPrzedmiotById(nazwa);
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepo.findByUserId(idUsera).getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        //Integer idUcznia = 1; ////////////////////////////////ID UCZNIA/////////////////////////////////////////////////////
        final UczenEntity uczen = uczenRepo.findById(idUcznia);
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + newZainteresowania.getPrzedmiotName());
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG1: " + newZainteresowania.getStopienZainteresowania());

        final PrzedmiotyEntity przedmiot = przedmiotRepo.findByName(newZainteresowania.getPrzedmiotName());

        List<ZainteresowaniaEntity> rv;
        rv = zainteresowaniaRepo.findByPrzedmiotId(przedmiot.getId());
        boolean dodawanie = true;
        for (ZainteresowaniaEntity zaint : rv) {
            if (zaint.getUczenId().getId() == uczen.getId()) {
                dodawanie = false;
            }
        }

        if (dodawanie) {
            final ZainteresowaniaEntity zainteresowania = new ZainteresowaniaEntity(uczen, przedmiot, newZainteresowania.getStopienZainteresowania());
            final ZainteresowaniaEntity e = this.zainteresowaniaRepo.save(zainteresowania);

            List<Integer> lz;
            lz = zainteresowaniaRepo.findByUczenId(uczen.getId());
            Integer med = Integer.valueOf(MedianyController.med(lz));

            if (medianyRepo.findByUczenId2(uczen.getId()) == null) {
                //nie ma
                Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "Nie ma " + med);

                MedianyEntity mediana = new MedianyEntity(uczen, med);
                this.medianyRepo.save(mediana);
            } else {
                //jest
                Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "Jest" + med);
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
    public ZainteresowaniaEntity updateZainteresowania(final @PathVariable Integer id, final @RequestBody @Valid ZainteresowaniaCmd updatedZainteresowania, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final ZainteresowaniaEntity zainteresowania = zainteresowaniaRepo.findOne(id);
        zainteresowania.setStopienZainteresowania(updatedZainteresowania.getStopienZainteresowania());
        final UczenEntity uczen = uczenRepo.findById(zainteresowania.getUczenId().getId());
        final ZainteresowaniaEntity e = this.zainteresowaniaRepo.save(zainteresowania);

        if (zainteresowania == null) {
            throw new ResourceNotFoundException();
        }

        List<Integer> lz;
        lz = zainteresowaniaRepo.findByUczenId(uczen.getId());
        Integer med = Integer.valueOf(MedianyController.med(lz));

        if (medianyRepo.findByUczenId2(uczen.getId()) == null) {
            //nie ma
            Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "Nie ma " + med);

            MedianyEntity mediana = new MedianyEntity(uczen, med);
            this.medianyRepo.save(mediana);
        } else {
            //jest
            Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "Jest" + med);
            MedianyEntity mediana = medianyRepo.findByUczenId2(uczen.getId());
            mediana.setMediana(med);
            this.medianyRepo.save(mediana);
        }

        return e;
    }
    
    
    @RequestMapping(value = "/setZainteresowania", method = POST)
    public void updateZainteresowania( final @RequestBody @Valid List<Zainteresowanie> newZainteresowanie) {
        System.out.println("SET ZAINERESOWANIA");
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();        
        
        List<ZainteresowaniaEntity> zainteresowania = zainteresowaniaRepo.findByUczenId2(currentUser.getId());
        
        for(Zainteresowanie z : newZainteresowanie)
        {
            for(ZainteresowaniaEntity z1 : zainteresowania)
            {
                if(z.getId()==z1.getId())z1.setStopienZainteresowania(z.getValue());
            }
        }
        
        zainteresowaniaRepo.save(zainteresowania);
    }
}
