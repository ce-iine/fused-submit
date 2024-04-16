package vttp.mainproject.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.mainproject.backend.model.Job;
import vttp.mainproject.backend.service.ApiService;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ListingRestController {

    @Autowired
    ApiService jobSvc;

    @GetMapping("/getjobs")
    public ResponseEntity<List<Job>> getJobs(){
        List<Job> allJobs= jobSvc.getAllJobs();
        return ResponseEntity.ok(allJobs); 
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<Job> jobDetails(@PathVariable Long id) {
        Job job = jobSvc.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping(path = "/logo")
    public ResponseEntity<Job> jobDetails(@RequestParam("name") String name) {
        Job job = jobSvc.getJobByCompanyName(name);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<String> removeJob(@PathVariable Long id) {
        Boolean isRemoved = false;

        isRemoved = jobSvc.removeJob(id); 

        JsonObject resp = Json.createObjectBuilder()
                .add("isRemoved", isRemoved)
                .add("status", 200)
                .build(); 

        return ResponseEntity.ok(resp.toString());
    }
}


