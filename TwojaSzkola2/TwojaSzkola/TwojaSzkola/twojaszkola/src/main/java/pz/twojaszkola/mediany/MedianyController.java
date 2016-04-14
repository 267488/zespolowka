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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pz.twojaszkola.uczen.UczenEntity;
import pz.twojaszkola.uczen.UczenRepository;
import pz.twojaszkola.zainteresowania.zainteresowaniaRepository;

/**
 *
 * @author andrew
 */
@RestController
public class MedianyController {
    
    private final MedianyRepository medianyRepository;
    private final UczenRepository uczenRepository;
    private final zainteresowaniaRepository zainteresowaniaRepository;
    
    @Autowired
        public MedianyController(final MedianyRepository medianyRepository, final UczenRepository uczenRepository, final zainteresowaniaRepository zainteresowaniaRepository) {
            this.medianyRepository = medianyRepository;
            this.uczenRepository = uczenRepository;
            this.zainteresowaniaRepository = zainteresowaniaRepository;
        }
    
        /*public MedianyEntity getMediana(final @RequestBody UczenCmd Uczen) {
            MedianyEntity rv;
            rv = medianyRepository.findUczenById(Integer.SIZE)
            return rv;
        }*/
        
        public static double med(List<Integer> tablica) {
        double mediana = 0.0; // zmienna, która będzie przechowywać medianę
        double srednia = 0.0; // zmienna do trzymania średniej wartości dwóch zmiennych
        Collections.sort(tablica); // mediana jest wartością środkową w danym zbiorze
        // aby uzyskać tę wartość, należy w pierwszym kroku
        // posortować elementy tablicy (dowolną metodą)
        // tutaj użyliśmy wbudowanej metody Javy sort.
        // jeżeli tablica zawiera parzystą liczbę elementów, to mediana jest 
        // średnią wartością dwóch środkowych elementów
        if (tablica.size() % 2 == 0) {
            srednia = tablica.get(tablica.size() / 2) + tablica.get((tablica.size() / 2) - 1);
            // w zmiennej średnia trzymamy sumę dwóch środkowych elementów tablicy
            mediana = srednia / 2.0; //obliczamy średnią wartość dwóch elementów
        } else // jeżeli tablica zawiera nieparzystą liczbę elementów, to mediana
        {    // jest dokładnie wartością środkową
            mediana = tablica.get(tablica.size() / 2);
        }
        return mediana;
    }

    public void createOrUpdateMediana(UczenEntity uczen) {

        List<Integer> lz;
        lz = zainteresowaniaRepository.findByUczenId(uczen.getId());
        final MedianyEntity mediana = new MedianyEntity(uczen, med(lz));
        this.medianyRepository.save(mediana);
    }
        
        
}
