package com.yaniv.luz.service;

import java.util.List;
import com.yaniv.luz.model.Service;

@org.springframework.stereotype.Service
public class ServiceService {

    public List<Service> getServices(){
        return List.of (
            new Service(1L, "Haircut", 30, 80),
            new Service(2L, "Haircut + Beard", 45, 110),
            new Service(3L, "Long Haircut", 60, 140)
        );
    }
}