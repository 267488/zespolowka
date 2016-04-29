package pz.twojaszkola.user;

import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
        System.out.println("current user: "+user.getEmail()+" "+user.getPassword()+" "+user.getRole().toString());
    }

    public User getUser() {
        return user;
    }

    public Integer getId() {
        return user.getId();
    }

    public String getRole() {
        return user.getRole();
    }
    
    public String getState()
    {
        return user.getState();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
