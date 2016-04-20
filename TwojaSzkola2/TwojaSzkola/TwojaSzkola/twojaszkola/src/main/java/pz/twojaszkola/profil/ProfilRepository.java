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
package pz.twojaszkola.profil;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author radon
 */
public interface ProfilRepository extends JpaRepository<ProfilEntity, Integer> {
    ProfilEntity findById(final int id);
    List<ProfilEntity> findByPrzedmiotNazwaIdAndSzkola(final Integer idPrzedmiotu, final Integer idSzkoly);
    List<ProfilEntity> findByTypSzkoly(final String typSzkoly);
    List<ProfilEntity> findSzkolySrednie(final String s1, final String s2, final String s3);
}
