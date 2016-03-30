package pz.twojaszkola.uczen;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import pz.twojaszkola.uczen.UczenEntity;

public interface UczenRepository extends JpaRepository<UczenEntity, Integer> {
    
    UczenEntity findById(final int id);
    UczenEntity findByPesel(final String pesel);
 
}

