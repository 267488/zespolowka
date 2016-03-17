/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radon.helloworld;

import com.radon.helloworld.dao.UczenDAO;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.pojo.Uczen;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Agata
 */
public class UczenController implements Controller {

    public ModelAndView handleRequest(HttpServletRequest hsr, HttpServletResponse hsr1) throws Exception {
        ModelAndView mv = new ModelAndView("uczniowie");
        
        try{
            List<Uczen> ls = UczenDAO.layDS();
            //for(int i=0;i<ls.size();i++){
            //    System.out.println("Uczen " + i + " to " + ls.get(i).getImie());
            //}
            mv.addObject("users",ls);
        } catch (Exception e){
            System.out.println("Blad w UczenController");
            e.printStackTrace();
        }
        
        return mv;
    }
    
}
