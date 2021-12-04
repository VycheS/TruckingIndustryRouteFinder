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

import home.vs.app_java.dto.ClientDTO;
import home.vs.app_java.service.ClientService;

@RestController
public class ClientController {
    private static final String REQUEST = "/clients";
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }
    
    @PostMapping(value = REQUEST)
    public ResponseEntity<HttpStatus> create(@RequestBody ClientDTO client) {
        
        final boolean created = clientService.create(client);

        return created 
            ? new ResponseEntity<>(HttpStatus.CREATED)
            : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @GetMapping(value = REQUEST)
    public ResponseEntity<List<ClientDTO>> read() {
        
        final List<ClientDTO> clients = this.clientService.readAll();

        return clients != null && !clients.isEmpty()
            ? new ResponseEntity<>(clients, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
    }

    @GetMapping(value = REQUEST + "/{id}")
    public ResponseEntity<ClientDTO> read(@PathVariable(name = "id") int id) {
        
        final ClientDTO client = this.clientService.read(id);

        return client != null
            ? new ResponseEntity<>(client, HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @PutMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> update(@PathVariable(name = "id") int id, @RequestBody ClientDTO client) {
        
        final boolean updated = clientService.update(client, id);

        return updated
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
 
    @DeleteMapping(value = REQUEST + "/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable(name = "id") int id) {
        
        final boolean deleted = clientService.delete(id);

        return deleted
            ? new ResponseEntity<>(HttpStatus.OK)
            : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }
    
}
