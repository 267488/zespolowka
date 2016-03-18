package com.radon.helloworld.dao;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.radon.helloworld.util.HibernateUtil;
import java.util.List;
import com.radon.helloworld.pojo.Uczen;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Agata
 */
public class UczenDAO {
    public static List<Uczen> layDS(){
        List<Uczen> ls = null;
        try{
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            String hgl = "from Uczen";
            Query query = session.createQuery(hgl);
            ls=query.list();
            session.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return ls;
    }
}
