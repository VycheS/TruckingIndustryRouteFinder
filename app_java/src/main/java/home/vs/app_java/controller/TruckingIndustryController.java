package home.vs.app_java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import home.vs.app_java.service.TruckingIndustryService;

@RestController
public class TruckingIndustryController {
    private static final String REQUEST = "/client/{client_id}/layer_group/{layer_group_id}/trucking_industry";
    private final TruckingIndustryService truckingIndustryService;

    @Autowired
    public TruckingIndustryController(TruckingIndustryService truckingIndustryService) {
        this.truckingIndustryService = truckingIndustryService;
    }
    
    @GetMapping(value = REQUEST)
    public ResponseEntity<HttpStatus> findRoutes(@PathVariable(name = "layer_group_id") int layerGroupId
        , @PathVariable(name = "client_id") int clientId) {

        return this.truckingIndustryService.findRoutes(layerGroupId, clientId)
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }
    
}
