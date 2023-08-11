package com.mineco.api.service;

import com.mineco.api.entities.Sat;
import com.mineco.api.external.services.satService;
import org.springframework.stereotype.Service;

@Service
public class ServiceConsumer {

    private final satService WSConsumo;

    public ServiceConsumer(satService wsConsumo) {
        WSConsumo = wsConsumo;
    }

    public Sat logginSat(){
        Sat satModel = new Sat();
        satModel.setUsername("rgm_user");
        satModel.setPassword("pU8%1M$bt5&V8Y80A");
        return WSConsumo.logginSat(satModel);
    }
}
