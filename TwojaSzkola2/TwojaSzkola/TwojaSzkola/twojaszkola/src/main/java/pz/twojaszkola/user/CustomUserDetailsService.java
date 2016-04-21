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
package pz.twojaszkola.user;

import java.awt.BorderLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author radon
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) throws InterruptedException {
       
        this.userService = userService;
    }      
    
    @Override
    public CurrentUser loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("customUserDetailsService login "+login);
        User user = userService.getUserByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email=%s was not found", login)));
        if(user.getBan().equals("BANNED") || user.getState().equals("UNACTIVE"))throw new UsernameNotFoundException("Użytkowni jest zablokowany lub nieaktywny");
        
        return new CurrentUser(user);
    }
    
}
