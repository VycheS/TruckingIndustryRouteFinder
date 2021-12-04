package home.vs.app_java.controller;

import java.util.List;

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

import home.vs.app_java.dto.LayerGroupDTO;
import home.vs.app_java.service.LayerGroupService;

@RestController
public class LayerGroupController {
    private static final String REQUEST = "/client/{client_id}/layer_groups";
    private final LayerGroupService layerGroupService;

    @Autowired
    public LayerGroupController(LayerGroupService layerGroupService) {
        this.layerGroupService = layerGroupService;
    }
    
    @PostMapping(value = REQUEST)
    public ResponseEntity<HttpStatus> create(
            @RequestBody LayerGroupDTO layerGroup,
            @PathVariable(name = "client_id") int clientId) {
        return layerGroupService.create(layerGroup, clientId)
            ? new ResponseEntity<>(HttpStatus.CREATED)
            : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping(value = REQUEST)
    public ResponseEntity<List<LayerGroupDTO>> read(@PathVariable(name = "client_id") int clientId) {
        final List<LayerGroupDTO> layerGroups = this.layerGroupService.readAll(clientId);

        return layerGroups != null && !layerGroups.isEmpty()
            ? new ResponseEntity<>(layerGroups, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @GetMapping(value = REQUEST + "/{id}")
    public ResponseEntity<LayerGroupDTO> read(
            @PathVariable(name = "id") int id,
            @PathVariable(name = "client_id") int clientId) {
        final LayerGroupDTO layerGroup = this.layerGroupService.read(id, clientId);

        return layerGroup != null
            ? new ResponseEntity<>(layerGroup, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PutMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable(name = "id") int id,
            @RequestBody LayerGroupDTO layerGroup,
            @PathVariable(name = "client_id") int clientId) {
        final boolean updated = layerGroupService.update(layerGroup, id, clientId);

        return updated
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
 
    @DeleteMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable(name = "id") int id,
            @PathVariable(name = "client_id") int clientId) {
        final boolean deleted = layerGroupService.delete(id, clientId);

        return deleted
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    
}
