package home.vs.app_java.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import home.vs.app_java.dto.LayerDTO;
import home.vs.app_java.service.LayerService;

@RestController
public class LayerController {
    private static final String REQUEST = "/client/{client_id}/layer_group/{layer_group_id}/layers";
    private final LayerService layerService;

    @Autowired
    public LayerController(LayerService layerService) {
        this.layerService = layerService;
    }
    
    @PostMapping(value = REQUEST)
    public ResponseEntity<UUID> create(
            @RequestBody LayerDTO layer,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

            final UUID uuid = layerService.create(layer, layerGroupId, clientId);

        return uuid != null
            ? new ResponseEntity<>(uuid, HttpStatus.CREATED)
            : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }
    // @PostMapping(value = REQUEST)
    // public ResponseEntity<HttpStatus> create(
    //         @RequestBody LayerDTO layer,
    //         @PathVariable(name = "layer_group_id") int layerGroupId,
    //         @PathVariable(name = "client_id") int clientId) {

    //     return layerService.create(layer, layerGroupId, clientId)
    //         ? new ResponseEntity<>(HttpStatus.CREATED)
    //         : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    // }

    @GetMapping(value = REQUEST)
    public ResponseEntity<List<LayerDTO>> read(
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final List<LayerDTO> layers = this.layerService.readAll(layerGroupId, clientId);

        return layers != null && !layers.isEmpty()
            ? new ResponseEntity<>(layers, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @GetMapping(value = REQUEST + "/{id}")
    public ResponseEntity<LayerDTO> read(
            @PathVariable(name = "id") UUID id,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {
        final LayerDTO layer = this.layerService.read(id, layerGroupId, clientId);

        return layer != null
            ? new ResponseEntity<>(layer, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PutMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable(name = "id") UUID id,
            @RequestBody LayerDTO layer,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final boolean updated = layerService.update(layer, id, layerGroupId, clientId);

        return updated
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
 
    @DeleteMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable(name = "id") UUID id,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {
        final boolean deleted = layerService.delete(id, layerGroupId, clientId);

        return deleted
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    
}
