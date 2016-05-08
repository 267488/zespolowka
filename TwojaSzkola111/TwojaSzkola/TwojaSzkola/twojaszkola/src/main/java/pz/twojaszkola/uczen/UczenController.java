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

import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestMethod;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.user.SuperUser;
import pz.twojaszkola.user.User;
import pz.twojaszkola.user.UserRepository;
import pz.twojaszkola.zainteresowania.ZainteresowaniaController;
import pz.twojaszkola.zainteresowania.ZainteresowaniaEntity;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class UczenController {

    private final UczenRepository uczenRepository;
    private final UserRepository userRepository;

    @Autowired
    public UczenController(final UczenRepository uczenRepository,
            final UserRepository userRepository) {
        this.uczenRepository = uczenRepository;
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/uczen", method = GET)
    public List<UczenEntity> getUczen(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<UczenEntity> rv;
        rv = uczenRepository.findAll(new Sort(Sort.Direction.ASC, "pesel", "name", "lastname", "mail", "czegoSzukam", "kodpocztowy"));
        return rv;
    }

    @RequestMapping(value = "/CurrentUczen", method = GET)
    public SuperUser getCurrentUczen() {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();

        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + currentUser.getId());
        final User user = userRepository.findById(currentUser.getId());
        final UczenEntity uczen = uczenRepository.findByUserId(user.getId());
        
        System.out.println("CurrentUczenController test"+currentUser.getId());
        System.out.println("CurrentUczenController test"+uczen.getId());
        
        return new SuperUser(uczen.getId(),
                uczen.getName(),
                uczen.getLastname(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                uczen.getMiasto(),
                uczen.getKodpocztowy(),
                uczen.getAdres(),
                uczen.getCzegoSzukam(),
                user.getGalleryId());
        

    }

    @RequestMapping(value = "/CurrentUczen/zainteresowania", method = GET)
    public List<ZainteresowaniaEntity> getZainteresowaniaUczniaById(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);

        return uczen.getZainteresowania();
    }

    class zainteresowaniaStopienComparator implements Comparator<ZainteresowaniaEntity> {

        @Override
        public int compare(ZainteresowaniaEntity z1, ZainteresowaniaEntity z2) {
            return z2.getStopienZainteresowania() - z1.getStopienZainteresowania();
        }
    }

    @RequestMapping(value = "/CurrentUczen/zainteresowaniamax", method = GET)
    public ZainteresowaniaEntity getMaxZainteresowaniaUczniaById(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);
        List<ZainteresowaniaEntity> lz = uczen.getZainteresowania();
        Collections.sort(lz, new zainteresowaniaStopienComparator());

        return lz.get(0);
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
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);

        User user = userRepository.findById(idUsera);
        final UczenEntity uczen = new UczenEntity(newUczen.getName(), newUczen.getLastname(), newUczen.getCzegoSzukam(), newUczen.getKodpocztowy(), newUczen.getMiasto(), newUczen.getAdres(), user);
//        if (galleryStudentRepo.findOne(2) != null) {
//            //szkola.setGalleryId(newSzkola.getGalleryId());
//            uczen.setGalleryId(galleryStudentRepo.findOne(2));
//        } else {
//            uczen.setGalleryId(galleryStudentRepo.findOne(1));
//        }
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


    @RequestMapping(value="/editUczen", method = RequestMethod.POST)
    public boolean editSchool(@RequestBody SuperUser editUczen)
    {
        
        System.out.println("EDIT UCZEN: ");
        

        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepository.findByUserId(idUsera).getId();
        Logger.getLogger(ZainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        User user = userRepository.findById(currentUser.getId());
        UczenEntity uczen = uczenRepository.findByUserId(currentUser.getId());
        
        user.setEmail(editUczen.getMail());
        uczen.setName(editUczen.getName());
        uczen.setLastname(editUczen.getLastname());
        uczen.setMiasto(editUczen.getMiasto());
        uczen.setKodpocztowy(editUczen.getKodpocztowy());
        uczen.setAdres(editUczen.getAdres());
        uczen.setCzegoSzukam(editUczen.getCzegoSzukam());
        uczenRepository.save(uczen);
        userRepository.save(user);
        return true;

    }
}
