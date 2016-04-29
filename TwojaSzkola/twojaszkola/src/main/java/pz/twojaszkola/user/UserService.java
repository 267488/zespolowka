package pz.twojaszkola.user;



import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(int id);

    Optional<User> getUserByLogin(String login);

    Collection<User> getAllUsers();

    User create(UserCmd form);

}
