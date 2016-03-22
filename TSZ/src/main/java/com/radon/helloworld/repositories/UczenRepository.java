/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radon.helloworld.repositories;

import com.radon.helloworld.pojo.Uczen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Agata
 */
public interface UczenRepository extends JpaRepository<Uczen, String> {
}

