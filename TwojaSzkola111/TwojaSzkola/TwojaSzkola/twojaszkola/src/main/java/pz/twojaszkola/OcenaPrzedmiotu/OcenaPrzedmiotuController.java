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
package pz.twojaszkola.OcenaPrzedmiotu;

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
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.profil.ProfilEntity;
import pz.twojaszkola.profil.ProfilRepository;
import pz.twojaszkola.przedmioty.przedmiotyEntity;
import pz.twojaszkola.przedmioty.przedmiotyRepository;
import pz.twojaszkola.support.ResourceNotFoundException;

/**
 *
 * @author Agata
 */
@RestController
@RequestMapping("/api")
public class OcenaPrzedmiotuController {

    private final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepository;
    private final ProfilRepository profilRepo;
    private final przedmiotyRepository przedmiotRepo;

    @Autowired
    public OcenaPrzedmiotuController(final OcenaPrzedmiotuRepository ocenaPrzedmiotuRepository, final ProfilRepository profilRepo, final przedmiotyRepository przedmiotRepo) {
        this.ocenaPrzedmiotuRepository = ocenaPrzedmiotuRepository;
        this.profilRepo = profilRepo;
        this.przedmiotRepo = przedmiotRepo;
    }

    @RequestMapping(value = "/ocenaPrzedmiotu", method = GET)
    public List<OcenaPrzedmiotuEntity> getOcenaPrzedmiotu(final @RequestParam(required = false, defaultValue = "false") boolean all) {
        List<OcenaPrzedmiotuEntity> rv;
        rv = ocenaPrzedmiotuRepository.findAll(new Sort(Sort.Direction.ASC, "profilId", "przedmiotId", "ocena"));
        return rv;
    }

    @RequestMapping(value = "/ocenaPrzedmiotu", method = POST)
    @PreAuthorize("isAuthenticated()")
    public OcenaPrzedmiotuEntity createOcenaPrzedmiotu(final @RequestBody @Valid OcenaPrzedmiotuCmd newOcenaPrzedmiotu, final BindingResult bindingResult) {
        // public RozszerzonePrzedmiotyEntity createZainteresowania(){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Jakis blad z argumentami.");
        }

        //final przedmiotyEntity przedmiot = zainteresowaniaRepo.getPrzedmiotById(nazwa);
        final ProfilEntity profil = profilRepo.findById(4);
        final przedmiotyEntity przedmiot = przedmiotRepo.findById(1);
        final Integer a = 10;

        final OcenaPrzedmiotuEntity OcenaPrzedmiotu = new OcenaPrzedmiotuEntity(profil, przedmiot, a);
        return this.ocenaPrzedmiotuRepository.save(OcenaPrzedmiotu);
    }

    @RequestMapping(value = "/ocenaPrzedmiotu/{id:\\d+}", method = PUT)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public OcenaPrzedmiotuEntity updateOcenaPrzedmiotu(final @PathVariable Integer id, final @RequestBody @Valid OcenaPrzedmiotuCmd updatedOcenaPrzedmiotu, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Invalid arguments.");
        }

        final OcenaPrzedmiotuEntity ocenaPrzedmiotu = ocenaPrzedmiotuRepository.findOne(id);

        if (ocenaPrzedmiotu == null) {
            throw new ResourceNotFoundException();
        }
        ocenaPrzedmiotu.setOcena(updatedOcenaPrzedmiotu.getOcena());

        return this.ocenaPrzedmiotuRepository.save(ocenaPrzedmiotu);
    }

    @RequestMapping(value = "/ocenaPrzedmiotu/{id}", method = DELETE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteOcenaPrzedmiotu(@PathVariable Integer id, final @RequestParam(required = false, defaultValue = "false") boolean all) {

        final OcenaPrzedmiotuEntity ocenaPrzedmiotu = ocenaPrzedmiotuRepository.findById(id);
        ocenaPrzedmiotuRepository.delete(ocenaPrzedmiotu);

        //return uczen;
    }
}
