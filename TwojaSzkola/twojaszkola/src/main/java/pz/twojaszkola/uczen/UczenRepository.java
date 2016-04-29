package pz.twojaszkola.uczen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UczenRepository extends JpaRepository<UczenEntity, Integer> {
    
    UczenEntity findById(final int id);
    UczenEntity findByUserId(final Integer userId);
    UczenEntity findByPesel(final String pesel);
 
}

