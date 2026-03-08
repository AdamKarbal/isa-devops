package fr.univcotedazur.skimaster.admin.controllers;

import fr.univcotedazur.skimaster.admin.components.AdminService;
import fr.univcotedazur.skimaster.incident.entities.Incident;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = AdminController.BASE_URI, produces = APPLICATION_JSON_VALUE)
public class AdminController {
    private final AdminService adminService;
    public final static String BASE_URI = "/admin";

    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @PostMapping("/{code}/close")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeGate(@PathVariable String code){
        this.adminService.setStatus(code, "CLOSE");
    }

    @PostMapping("/{code}/open")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openGate(@PathVariable String code){
        this.adminService.setStatus(code, "OPEN");
    }

    @PostMapping("/openAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openAllGates(){
        this.adminService.openAllGates();
    }

    @PostMapping("/closeAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeAllGates(){
        this.adminService.closeAllGates();
    }

    @GetMapping("/alerts")
    @ResponseStatus(HttpStatus.CREATED)
    public List<String> getPendingIncidents(){
        List<String> incidents = new ArrayList<>();
        List<Incident> incidents1List = this.adminService.getPendingIncidents();
        for(Incident i : incidents1List){
            incidents.add(i.toString());
        }
        return incidents;
    }

    @PostMapping("/resolve/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resolveIncidentsForGate(@PathVariable String code){
        this.adminService.resolveIncidentsForGate(code);
    }

    //TODO: Les méthodes pour sendMessage et ClearPannel restent à faire.
}
