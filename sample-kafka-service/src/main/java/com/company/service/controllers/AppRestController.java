package com.company.service.controllers;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.services.api.ProcessService;
import org.jbpm.services.api.RuntimeDataService;
import org.kie.server.springboot.jbpm.ContainerAliasResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppRestController {

    @Autowired
    ProcessService processService;

    @Autowired
    RuntimeDataService dataService;

    @Autowired
    private ContainerAliasResolver aliasResolver;

    private String containerAlias = "sample-kafka-kjar";
    private String processId = "kafkaweather";

    @RequestMapping(value = "/weather/{city}/{country}", method = RequestMethod.GET, produces = "text/plain")
    public void startWxProcess(@PathVariable("city") String city,
                               @PathVariable("country") String country) {

        startWeatherProcess(city,
                            country);
    }

    private void startWeatherProcess(String city,
                                     String country) {
        Map<String, Object> processInputs = new HashMap<>();
        processInputs.put("inCountryCode",
                          country);
        processInputs.put("inCityName",
                          city);
        processService.startProcess(aliasResolver.latest(containerAlias),
                                    processId,
                                    processInputs);
    }
}
