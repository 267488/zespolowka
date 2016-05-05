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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import pz.twojaszkola.galleryUser.GalleryUserRepository;
import pz.twojaszkola.support.ResourceNotFoundException;
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.user.CurrentUser;
import pz.twojaszkola.zainteresowania.zainteresowaniaController;

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
    private final GalleryUserRepository galleryRepo;

    @Autowired
    public UlubionaSzkolaController(final UlubionaSzkolaRepository ulubionaSzkolaRepository,
            final UczenRepository uczenRepository,
            final SzkolaRepository szkolaRepository,
            final GalleryUserRepository galleryRepo) {
        this.ulubionaSzkolaRepository = ulubionaSzkolaRepository;
        this.uczenRepository = uczenRepository;
        this.szkolaRepository = szkolaRepository;
        this.galleryRepo = galleryRepo;
    }

    @RequestMapping(value = "/ulubioneSzkoly", method = GET)
    public List<ulubionaSzkola> getUlubionaSzkola(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<UlubionaSzkolaEntity2> rv;
        List<ulubionaSzkola> rv2 = new ArrayList<ulubionaSzkola>();
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        Integer idUcznia = uczenRepository.findByUserId(idUsera).getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUcznia);

        rv = ulubionaSzkolaRepository.findByUczenId(idUcznia);
        for (UlubionaSzkolaEntity2 u : rv) {
            String zdjecie = "";
            if (galleryRepo.findByUserId(u.getSzkolaId().getUserId().getId()) != null) {
                zdjecie = "/api/galleryUser/" + galleryRepo.findByUserId(u.getSzkolaId().getUserId().getId()).getId() + ".jpg";
            } else {
                zdjecie = "img/brak.jpg";
            }
            rv2.add(new ulubionaSzkola(u, zdjecie));
        }

        return rv2;
    }

    @RequestMapping(value = "/ulubionaSzkola/{id:\\d+}", method = POST)
    @PreAuthorize("isAuthenticated()")
    public UlubionaSzkolaEntity2 createUlubionaSzkola(@PathVariable Integer id) {
        List<UlubionaSzkolaEntity2> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);

        rv = ulubionaSzkolaRepository.findByUczenId(uczen.getId());
        //Logger.getLogger(UlubionaSzkolaController.class.getName()).log(Level.SEVERE, "LOG: " + newUlubionaSzkola.getId());
        final SzkolaEntity szkola = szkolaRepository.findById(id);
        boolean dodawanie = true;
        for (UlubionaSzkolaEntity2 szk : rv) {
            if (szk.getSzkolaId().getId() == id) {
                dodawanie = false;
            }
        }
        if (dodawanie) {
            final UlubionaSzkolaEntity2 ulubione = new UlubionaSzkolaEntity2(uczen, szkola);
            //szkola.setRodzajGwiazdki("glyphicon-star");
            this.szkolaRepository.save(szkola);
            return this.ulubionaSzkolaRepository.save(ulubione);
        }
        return null;
    }

    @RequestMapping(value = "/ulubionaSzkolaDelete/{id:\\d+}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    public UlubionaSzkolaEntity2 deleteUlubionaSzkola(@PathVariable Integer id) {
        List<UlubionaSzkolaEntity2> rv;
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        Integer idUsera = currentUser.getId();
        Logger.getLogger(zainteresowaniaController.class.getName()).log(Level.SEVERE, "LOG: " + idUsera);
        final UczenEntity uczen = uczenRepository.findByUserId(idUsera);
        rv = ulubionaSzkolaRepository.findByUczenId(uczen.getId());
        //Logger.getLogger(UlubionaSzkolaController.class.getName()).log(Level.SEVERE, "LOG: " + newUlubionaSzkola.getId());
        UlubionaSzkolaEntity2 szkolaDoUsuniecia = null;
        boolean usuwanie = false;
        for (UlubionaSzkolaEntity2 szk : rv) {
            if (szk.getSzkolaId().getId() == id) {
                usuwanie = true;
                szkolaDoUsuniecia = szk;
            }
        }
        if (usuwanie) {
            final SzkolaEntity szkola = szkolaRepository.findById(id);
            this.szkolaRepository.save(szkola);
            ulubionaSzkolaRepository.delete(szkolaDoUsuniecia);
            return szkolaDoUsuniecia;
        }
        return null;
    }

    @RequestMapping(value = "/ulubioneSzkoly/{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public UlubionaSzkolaEntity2 updateProponowaneSzkoly(final @PathVariable Integer id, final @RequestBody @Valid UlubionaSzkolaCmd updatedUlubionaSzkola, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final UlubionaSzkolaEntity2 ulubiona = ulubionaSzkolaRepository.findOne(id);

        if (ulubiona == null) {
            throw new ResourceNotFoundException();
        }
        return ulubiona;
    }
}
