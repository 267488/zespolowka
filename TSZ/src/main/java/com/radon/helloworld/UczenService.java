/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radon.helloworld;

import com.radon.helloworld.pojo.Uczen;
import com.radon.helloworld.repositories.UczenRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Agata
 */
@Service
public class UczenService {


    private final UczenRepository uczenRepository;

    @Autowired
    public UczenService(UczenRepository uczenRepository) {
        this.uczenRepository = uczenRepository;
    }


    public List<Uczen> getObj(){
        List<Uczen> uczenList = uczenRepository.findAll();
        return convertToDTOs(uczenList);
    }

    private List<Uczen> convertToDTOs(List<Uczen> models){
        return models.subList(0, 1); //tu nie wiem jak to zmodyfikowaæ, ¿eby by³o dobrze ??????
                //models.stream().map(this::convertToDTO).collect(toList());
    }

    private Uczen convertToDTO(Uczen model){

        Uczen dto = new Uczen();
        dto.setId(model.getId());
        dto.setImie(model.getImie());
        dto.setNazwisko(model.getNazwisko());
        return dto;

    }
}
