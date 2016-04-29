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
package pz.twojaszkola.uczen;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
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
import pz.twojaszkola.galleryStudent.GalleryStudentEntity;
import pz.twojaszkola.galleryStudent.GalleryStudentRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;
import pz.twojaszkola.zainteresowania.zainteresowaniaController;
import pz.twojaszkola.zainteresowania.zainteresowaniaEntity;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class UczenController {

    private final UczenRepository uczenRepository;
    private final UserRepository userRepository;
    private final GalleryStudentRepository galleryStudentRepo;

    @Autowired
    public UczenController(final UczenRepository uczenRepository, final UserRepository userRepository, final GalleryStudentRepository galleryStudentRepo) {
        this.uczenRepository = uczenRepository;
        this.userRepository = userRepository;
        this.galleryStudentRepo = galleryStudentRepo;
    }

    @RequestMapping(value = "/uczen", method = GET)
    public List<UczenEntity> getUczen(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<UczenEntity> rv;
        rv = uczenRepository.findAll(new Sort(Sort.Direction.ASC, "pesel", "name", "lastname", "czegoSzukam", "kodpocztowy"));
        return rv;
    }

    @RequestMapping(value = "/CurrentUczen", method = GET)
    public UczenEntity getUczenById(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);
        
        return uczen;
    }
    
    @RequestMapping(value = "/CurrentUczen/zainteresowania", method = GET)
    public List<zainteresowaniaEntity> getZainteresowaniaUczniaById(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);
        
        return uczen.getZainteresowania();
    }

    @RequestMapping(value = "/uczen", method = POST)
    @PreAuthorize("isAuthenticated()")
    public UczenEntity createUczen(final @RequestBody @Valid UczenCmd newUczen, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
       
        User user = userRepository.findById(idUsera);
        final UczenEntity uczen = new UczenEntity(newUczen.getPesel(), newUczen.getName(), newUczen.getLastname(), newUczen.getCzegoSzukam(), newUczen.getKodpocztowy(), user);
        if (galleryStudentRepo.findOne(2) != null) {
            //szkola.setGalleryId(newSzkola.getGalleryId());
            uczen.setGalleryId(galleryStudentRepo.findOne(2));
        } else {
            uczen.setGalleryId(galleryStudentRepo.findOne(1));
        }
        return this.uczenRepository.save(uczen);
    }
    
    @RequestMapping(value = "/uczen/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteUczen(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final UczenEntity uczen = uczenRepository.findById(id);
        uczenRepository.delete(uczen);

        //return uczen;
    }

    @RequestMapping(value = "/CurrentUczen", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public UczenEntity updateUczen(final @RequestBody @Valid UczenCmd updatedUczen, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepository.findByUserId(idUsera).getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        User user = userRepository.findById(idUsera);
        GalleryStudentEntity gallery = galleryStudentRepo.findOne(idUsera);
        //final UczenEntity uczen = new UczenEntity(updatedUczen.getPesel(), updatedUczen.getName(), updatedUczen.getLastname(), updatedUczen.getCzegoSzukam(), updatedUczen.getKodpocztowy(), user);
        final UczenEntity uczen = this.uczenRepository.findById(idUcznia);
        uczen.setPesel(updatedUczen.getPesel());
        uczen.setName(updatedUczen.getName());
        uczen.setLastname(updatedUczen.getLastname());
        uczen.setCzegoSzukam(updatedUczen.getCzegoSzukam());
        uczen.setKodpocztowy(updatedUczen.getKodpocztowy());
        uczen.setUserId(user);
        uczen.setGalleryId(gallery);

        if (uczen == null) {
            throw new ResourceNotFoundException();
        }

        return this.uczenRepository.save(uczen);
    }
}
