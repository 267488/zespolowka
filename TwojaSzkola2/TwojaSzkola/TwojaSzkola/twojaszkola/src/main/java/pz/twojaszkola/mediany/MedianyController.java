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
package pz.twojaszkola.mediany;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.zainteresowania.zainteresowaniaEntity;
import pz.twojaszkola.zainteresowania.zainteresowaniaRepository;

/**
 *
 * @author andrew
 */
@RestController
public class MedianyController {

    private final MedianyRepository medianyRepository;
    private final zainteresowaniaRepository zainteresowaniaRepository;
     private final UczenRepository uczenRepository;

    @Autowired
    public MedianyController(final MedianyRepository medianyRepository, final zainteresowaniaRepository zainteresowaniaRepository, final UczenRepository uczenRepository) {
        this.medianyRepository = medianyRepository;
        this.zainteresowaniaRepository = zainteresowaniaRepository;
        this.uczenRepository = uczenRepository;
    }

    /*public MedianyEntity getMediana(final @RequestBody UczenCmd Uczen) {
     MedianyEntity rv;
     rv = medianyRepository.findUczenById(Integer.SIZE)
     return rv;
     }*/
    public static int med(List<Integer> tablica) {
        double mediana = 0.0; 
        double srednia = 0.0; 
        Collections.sort(tablica); 
        if (tablica.size() % 2 == 0) {
            srednia = tablica.get(tablica.size() / 2) + tablica.get((tablica.size() / 2) - 1);
            mediana = srednia / 2.0; 

        } else 
        {    
            mediana = tablica.get(tablica.size() / 2);
        }

        return (int)mediana;
    }

    /*public void createOrUpdateMediana() {

        final UczenEntity uczen = uczenRepository.findById(1);
        List<Integer> lz;
        lz = zainteresowaniaRepository.findByUczenId(uczen.getId());
        Logger.getLogger(MedianyController.class.getName()).log(Level.SEVERE, "LOG: " + med(lz));
        final MedianyEntity mediana = new MedianyEntity(uczen, med(lz));
        this.medianyRepository.save(mediana);
    }*/

}
