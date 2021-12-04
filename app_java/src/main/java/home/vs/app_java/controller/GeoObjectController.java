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

import home.vs.app_java.dto.GeoObjectDTO;
import home.vs.app_java.service.GeoObjectService;

@RestController
public class GeoObjectController {
    private static final String REQUEST = "/client/{client_id}/layer_group/{layer_group_id}/layer/{layer_id}/geo_objects";
    private final GeoObjectService geoObjectService;

    @Autowired
    public GeoObjectController(GeoObjectService geoObjectService) {
        this.geoObjectService = geoObjectService;
    }
    
    @PostMapping(value = REQUEST)
    public ResponseEntity<HttpStatus> create(
            @RequestBody GeoObjectDTO geoObject,
            @PathVariable(name = "layer_id") UUID layerId,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final boolean created = geoObjectService.create(geoObject, layerId, layerGroupId, clientId);

        return created
            ? new ResponseEntity<>(HttpStatus.CREATED)
            : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping(value = REQUEST)
    public ResponseEntity<List<GeoObjectDTO>> read(
            @PathVariable(name = "layer_id") UUID layerId,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final List<GeoObjectDTO> geoObjects = this.geoObjectService.readAll(layerId, layerGroupId, clientId);

        return geoObjects != null && !geoObjects.isEmpty()
            ? new ResponseEntity<>(geoObjects, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @GetMapping(value = REQUEST + "/{id}")
    public ResponseEntity<GeoObjectDTO> read(
            @PathVariable(name = "id") int id,
            @PathVariable(name = "layer_id") UUID layerId,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final GeoObjectDTO geoObject = this.geoObjectService.read(id, layerId, layerGroupId, clientId);

        return geoObject != null
            ? new ResponseEntity<>(geoObject, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PutMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> update(
            @PathVariable(name = "id") int id,
            @RequestBody GeoObjectDTO geoObject,
            @PathVariable(name = "layer_id") UUID layerId,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {

        final boolean updated = geoObjectService.update(geoObject, id, layerId, layerGroupId, clientId);

        return updated
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
 
    @DeleteMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> delete(
            @PathVariable(name = "id") int id,
            @PathVariable(name = "layer_id") UUID layerId,
            @PathVariable(name = "layer_group_id") int layerGroupId,
            @PathVariable(name = "client_id") int clientId) {
                
        final boolean deleted = geoObjectService.delete(id, layerId, layerGroupId, clientId);

        return deleted
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    
}
