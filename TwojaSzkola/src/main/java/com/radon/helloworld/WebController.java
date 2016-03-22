/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radon.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Agata
 */
@Controller
public class WebController {
    @RequestMapping("/")
    String index() {
        return "index";
    }
}
