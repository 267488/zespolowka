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
package pz.twojaszkola.zainteresowania;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pz.twojaszkola.uczen.UczenEntity;
//import pz.twojaszkola.przedmioty.przedmiotyEntity;
//import pz.twojaszkola.uczen.UczenEntity;

/**
 *
 * @author Agata
 */
public interface ZainteresowaniaRepository extends JpaRepository<ZainteresowaniaEntity, Integer>{
    
    List<ZainteresowaniaEntity> findByPrzedmiotId(final Integer idPrzedmiotu);
    List<Integer> findByUczenId(final Integer idUcznia);
    List<ZainteresowaniaEntity> findByUczenId2(final Integer idUcznia);
    UczenEntity getUczenById(final Integer UczenId);
    Integer getStopienZaintByUczenAndPrzedmiot(final Integer idUcznia, final Integer idPrzedmiotu);
    ZainteresowaniaEntity findById(final Integer id);
}
