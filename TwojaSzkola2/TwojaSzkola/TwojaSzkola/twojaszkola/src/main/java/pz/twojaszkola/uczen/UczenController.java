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
import pz.twojaszkola.user.UserTest;

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
            rv = uczenRepository.findAll(new Sort(Sort.Direction.ASC, "pesel", "name", "lastname", "mail", "password", "kodpocztowy"));
            return rv;
        }
        
        /*@RequestMapping(value = "/uczen", method = POST) 
        @PreAuthorize("isAuthenticated()")
        public UczenEntity createUczen(final @RequestBody @Valid UczenCmd newUczen, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
            User user = null;
            final UczenEntity uczen = new UczenEntity(newUczen.getPesel(), newUczen.getName(), newUczen.getLastname(), newUczen.getPassword(), newUczen.getKodpocztowy(), user);
            return this.uczenRepository.save(uczen);	
        }*/
        
        @RequestMapping(value = "/uczen/{id:\\d+}", method = PUT)
        @PreAuthorize("isAuthenticated()")
        @Transactional
        public UczenEntity updateUczen(final @PathVariable Integer id, final @RequestBody @Valid UczenCmd updatedUczen, final BindingResult bindingResult) {
            if(bindingResult.hasErrors()) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
	
            final UczenEntity uczen = uczenRepository.findOne(id);
		
            if(uczen == null) {
                throw new ResourceNotFoundException();
            } 
            
            return uczen;
        }
        
        @RequestMapping(value = "/currentUser", method = GET)
        public SuperUser getUczen() {
            
            CurrentUser currentUser = null;
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            currentUser = (CurrentUser) auth.getPrincipal();
            
            User user = userRepository.findById(currentUser.getId());
            UczenEntity uczen = uczenRepository.findByUser(user);
            SuperUser superUser = new SuperUser(uczen.getName(), uczen.getLastname(), user.getEmail(), user.getLogin(), uczen.getId(), user.getId(), uczen.getKodpocztowy(), uczen.getPesel());
            return superUser;
        }
}
