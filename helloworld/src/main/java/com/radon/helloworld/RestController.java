/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radon.helloworld;

import com.radon.helloworld.pojo.Uczen;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 *
 * @author Agata
 */
@RequestMapping("/api/rest")
public class RestController {


    private final UczenService uczenService;

    @Autowired
    public RestController(UczenService uczenService) {
        this.uczenService = uczenService;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, value = "/uczniowie")
    public @ResponseBody List<Uczen> findAll() {
        return uczenService.getObj();

    }

}

