package com.mineco.api.controller;

import com.mineco.api.entities.Sat;
import com.mineco.api.service.ServiceConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SatController {

    private final ServiceConsumer serviceConsumer;

    public SatController(ServiceConsumer serviceConsumer) {
        this.serviceConsumer = serviceConsumer;
    }

    @GetMapping("login")
    public ResponseEntity<Sat> login(){
        Sat satmodel = serviceConsumer.logginSat();
        return new ResponseEntity<>(satmodel, HttpStatus.OK);
    }
}
