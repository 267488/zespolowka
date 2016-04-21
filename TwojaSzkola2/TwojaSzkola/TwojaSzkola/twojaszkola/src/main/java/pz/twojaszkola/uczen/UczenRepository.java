package pz.twojaszkola.uczen;

import org.springframework.data.jpa.repository.JpaRepository;
import pz.twojaszkola.user.User;

public interface UczenRepository extends JpaRepository<UczenEntity, Integer> {
    
    UczenEntity findById(final int id);
    UczenEntity findByPesel(final String pesel);
    UczenEntity findByUser(final User user);
}

