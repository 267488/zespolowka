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


import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
/**
 *
 * @author radon
 */

@RestController

public class UserController{
    
    private final UserRepository userRepository;

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    
    
    
    @Autowired
    public UserController(UserRepository userRepository) {
        
        this.userRepository = userRepository;
    }
    
    @CrossOrigin
    @RequestMapping(value="/reg", method = RequestMethod.POST)
    public void userReg(@RequestBody UserTest usertest) throws AddressException, MessagingException{
        
        
        System.out.println("login: "+usertest.getLogin()+", mail: "+ usertest.getEmail()+", password: "+usertest.getPassword()+", role: "+usertest.getRole());    
    
        User user = new User(usertest.getLogin(),usertest.getEmail(),usertest.getPassword(),"UCZEN","UNACTIVE","UNBANNED");
        userRepository.save(user);
        
        
        
        System.out.println("\n 1st ===> setup Mail Server Properties..");
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		System.out.println("RegCtrl: Mail Server Properties have been setup successfully..");
 
		// Step2
		System.out.println("\n\nRegCtrl:  2nd ===> get Mail Session..");
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("radek.rzymowski@gmail.com"));
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("radek.rzymowski@gmail.com"));
		generateMailMessage.setSubject("Wiadomosc testowa z twoja_szkola");
		String emailBody = "to jest wiadomosc testowa nie odpowiadaj na nia."
                        + "\n user id: "+user.getId()
                        + "\n user login: "+user.getLogin()
                        + "\n user password: "+user.getPassword()                       
                        + " \n\n http://localhost:8090/active/"+user.getId();
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("RegCtrl:  Mail Session has been created successfully..");
 
		// Step3
		System.out.println("\n\nRegCtrl:  3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "twoja.szkola.test", "twojaszkola123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
    }   
    
    @RequestMapping(value="/getAllUsers", method = RequestMethod.GET)
    public List<User> getAll()
    {
        List<User> user = userRepository.findAll();
        
        return user;
    }
    @RequestMapping(value="/currentID", method = RequestMethod.GET)
    public int getCurrentId()
    {
        CurrentUser currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        currentUser = (CurrentUser) auth.getPrincipal();
        return currentUser.getId();
    }
    
    @RequestMapping(value="/active/{id:\\d+}", method = RequestMethod.GET)
    public String test(final @PathVariable Integer id)
    {
        System.out.println("strona aktywacyjna");
        return "strona aktywacyjna | user id to activation: "+id;
    }
}
