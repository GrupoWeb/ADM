package com.mineco.api.external.services;

import com.mineco.api.config.FeignConfiguration;
import com.mineco.api.entities.Sat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "SAT-SERVICE", url = "https://wsconsumo.mineco.gob.gt", configuration = FeignConfiguration.class)
public interface satService {

    @RequestMapping(method = RequestMethod.POST, value ="api/auth/login")
    public Sat logginSat(@RequestBody Sat solicitud);
}
