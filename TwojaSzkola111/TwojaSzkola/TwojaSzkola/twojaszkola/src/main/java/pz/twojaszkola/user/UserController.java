package pz.twojaszkola.user;


import java.lang.reflect.Field;
import java.util.ArrayList;
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
import pz.twojaszkola.szkola.SzkolaEntity;
import pz.twojaszkola.szkola.SzkolaRepository;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
/**
 *
 * @author radon
 */

@RestController

public class UserController{
    
    private final UserRepository userRepository;
    private final UserService userService;
    private final SzkolaRepository szkolaRepository;
    private final UczenRepository uczenRepository;
    
    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    
    
    
    @Autowired
    public UserController(UserRepository userRepository,
                          UserService userService,
                          SzkolaRepository szkolaRepository,
                          UczenRepository uczenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.szkolaRepository = szkolaRepository;
        this.uczenRepository = uczenRepository;
    }
    
    @CrossOrigin
    @RequestMapping(value="/reg", method = RequestMethod.POST)
    public void userReg(@RequestBody UserTest usertest) throws AddressException, MessagingException{
        
        System.out.println("KONTROLER REJESTRACJI");
        
        if(userRepository.findByLogin(usertest.getLogin())!=null) new Error("user found");
        
        System.out.println("login: "+usertest.getLogin()+", mail: "+ usertest.getEmail()+", password: "+usertest.getPassword()+", role: "+usertest.getRole());    
    
        User user = new User(usertest.getLogin(),usertest.getEmail(),usertest.getPassword(),usertest.getRole(),"UNACTIVE","UNBANNED");
        if(usertest.getRole().equals("UCZEN"))
        {
            
        UczenEntity uczen = new UczenEntity(null, null, null, null, null, null, user);
        
        uczenRepository.save(uczen);
        }
        if(usertest.getRole().equals("SZKOLA"))
        {
            SzkolaEntity szkola = new SzkolaEntity(null, null, null, null, null, null, null, user);
            szkolaRepository.save(szkola);
        }
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
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("radek.rzymowski@gmail.com"));
		generateMailMessage.setSubject("Wiadomosc z twoja_szkola");
		String emailBody = "to jest wiadomosc z linkiem aktywacyjnym nie odpowiadaj na nia."
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
		transport.connect("smtp.gmail.com", "twojaszkolainfo", "twojaszkola123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
        
    }   
    
    @RequestMapping(value="/getAllUsers", method = RequestMethod.GET)
    public List<User> getAllUsers()
    {
        List<User> users = userRepository.findAll();
        
        return users;
    }
    
    @RequestMapping(value="/getAllSchools", method = RequestMethod.GET)
    public List<SzkolaEntity> getAllSchools()
    {
        List<SzkolaEntity> szkoly = szkolaRepository.findAll();
        
        return szkoly;
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
        User user = userRepository.findById(id);
        user.setState("ACTIVE");
        userRepository.save(user);
        System.out.println("strona aktywacyjna");
        return "strona aktywacyjna | user id to activation: "+id+"<a href='/'>wroc do strony logowania</a>";
    }
    
    @RequestMapping(value="/setBan", method = RequestMethod.POST)
    public void setBan(@RequestBody int[] idToBan)
    {
        User user;
        
        System.out.println("######### ID TO BAN :");
        for(int i : idToBan)
        {
            System.out.print(" "+i);
            user = userRepository.findById(i);
            user.setBan("BANNED");
            userRepository.save(user);
        }
    }
    
    @RequestMapping(value="/setBanAll", method = RequestMethod.POST)
    public void setBanAll()
    {
        List<User> users = userRepository.findAll();
        for(User u : users)
        {
            if(u.getId()!=0){
                u.setBan("BANNED");
            }
        }
        userRepository.save(users);
    }
    
    @RequestMapping(value="/setUBanAll", method = RequestMethod.POST)
    public void setUBanAll()
    {
        List<User> users = userRepository.findAll();
        for(User u : users)
        {
            if(u.getId()!=0){
                u.setBan("UNBANNED");
            }
        }
        userRepository.save(users);
    }
    
    @RequestMapping(value="/setUBan", method = RequestMethod.POST)
    public void setUBan(@RequestBody int[] idToUBan)
    {
        User user;
        
        System.out.println("######### ID TO UNBAN :");
        for(int i : idToUBan)
        {
            System.out.print(" "+i);
            user = userRepository.findById(i);
            user.setBan("UNBANNED");
            userRepository.save(user);
        }
    }
    
     @RequestMapping(value="/del", method = RequestMethod.POST)
    public void del(@RequestBody int[] idToUBan)
    {
        User user;
        
        System.out.println("######### ID TO DELETE :");
        for(int i : idToUBan)
        {
            System.out.print(" "+i);          
            userRepository.delete(i);
        }
    }
    
    @RequestMapping(value="/sendAdminMail", method = RequestMethod.POST)
    public void sendAdminMail(@RequestBody AdminMail mailData) throws AddressException, MessagingException
    {
        System.out.println("IDS: ");
        for(int i : mailData.getIds())
        {
            System.out.println(" "+i);
        }
        System.out.println("MAIL: "+mailData.getTemat()+" "+mailData.getTresc());
        
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
                
                for(int i : mailData.getIds())
                {
                    
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(userRepository.findById(i).getEmail()));
                }
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("radek.rzymowski@gmail.com"));
		generateMailMessage.setSubject(mailData.getTemat());
		String emailBody = mailData.getTresc();
		generateMailMessage.setContent(emailBody, "text/html");
		System.out.println("RegCtrl:  Mail Session has been created successfully..");
 
		// Step3
		System.out.println("\n\nRegCtrl:  3rd ===> Get Session and Send mail");
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "twojaszkolainfo", "twojaszkola123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
        
    }
    
    
    @RequestMapping(value="/sendAdminMailToAll", method = RequestMethod.POST)
    public void sendAdminMailToAll(@RequestBody AdminMail mailData) throws AddressException, MessagingException
    {
        List<User> users = userRepository.findAll();
        System.out.println("SEND MAIL TO ALL #########################");
       
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
 
		// Step2
		
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
                
                for(User u : users)
                {
                    
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
                }
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("radek.rzymowski@gmail.com"));
		generateMailMessage.setSubject(mailData.getTemat());
		String emailBody = mailData.getTresc();
		generateMailMessage.setContent(emailBody, "text/html");
		
 
		// Step3
		
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "twojaszkolainfo", "twojaszkola123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
        
    }
    
    @RequestMapping(value="/changePassword", method = RequestMethod.POST)
    public void changePassword(@RequestBody NewPassword newPassword) throws AddressException, MessagingException
    {
        
        System.out.println("SEND MAIL TO ALL #########################");
        
        
       
		mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");
		
 
		// Step2
		
		getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		generateMailMessage = new MimeMessage(getMailSession);
                
                for(int i : newPassword.getIds())
                {
                User u = userRepository.findById(i);
                u.setPassword(newPassword.getNewPassword());
                userRepository.save(u);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(u.getEmail()));
                }
		//generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("radek.rzymowski@gmail.com"));
		generateMailMessage.setSubject("Zmiana hasla");
		String emailBody = "Twoje nowe haslo to: "+newPassword.getNewPassword();
		generateMailMessage.setContent(emailBody, "text/html");
		
 
		// Step3
		
		Transport transport = getMailSession.getTransport("smtp");
 
		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", "twojaszkolainfo", "twojaszkola123");
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
        
    }

}
    