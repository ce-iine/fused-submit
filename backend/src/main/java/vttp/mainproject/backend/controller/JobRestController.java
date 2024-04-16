package vttp.mainproject.backend.controller;

import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.micrometer.core.annotation.Timed;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.mainproject.backend.exceptions.UpdatingException;
import vttp.mainproject.backend.model.Applicant;
import vttp.mainproject.backend.model.AppliedJob;
import vttp.mainproject.backend.model.Job;
import vttp.mainproject.backend.model.JobStat;
import vttp.mainproject.backend.model.Skill;
import vttp.mainproject.backend.service.EmailService;
import vttp.mainproject.backend.service.JobService;

@RestController
@CrossOrigin
@RequestMapping("api/repo")
public class JobRestController {

    @Autowired
    JobService jobService;

    @Autowired 
    private EmailService emailService;

    @GetMapping("/applications/{userid}")
    public ResponseEntity<List<AppliedJob>> getAppliedJobs(@PathVariable("userid") String userId) {
        List<AppliedJob> appliedJobs = jobService.getAppliedJobs(userId);

        return ResponseEntity.ok(appliedJobs);
    }

    @Timed (value = "submitting.time", description = "Time taken to insert new application")
    @PostMapping(path = "/{userid}/application", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> insertNewApplication(@PathVariable("userid") String userid,
            @RequestPart("jobid") String jobid,
            @RequestPart(name = "resume", required = false) MultipartFile resume) throws UpdatingException, IOException {

        Boolean isUpdated = false;

        Applicant app = emailService.getUserById(userid); 
        JobStat js = emailService.getJob(jobid);
        String resp = emailService.sendEmail(app, js);
        System.out.println("RESP>>>>" + resp); 

        try {
            isUpdated = jobService.insertNewApplication(resume.getInputStream(), resume.getContentType(),
                    resume.getSize(), userid, jobid); 

            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated)
                    .build()
                    .toString());
        } catch (IOException ex) {
            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated)
                    .build()
                    .toString());
        }
    }

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveJob(@RequestBody String received) throws UpdatingException {

        JsonReader reader = Json.createReader(new StringReader(received));
        JsonObject receivedObj = reader.readObject();
        String userid = receivedObj.getString("userid");
        String jobid = receivedObj.getJsonNumber("jobid").toString();

        Boolean isAdded = false;
        isAdded = jobService.insertNewBookmark(userid, jobid);

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/saved/{userid}")
    public ResponseEntity<List<Job>> getSavedJob(@PathVariable("userid") String userId) {
        List<Job> appliedJobs = jobService.getSavedJobs(userId);
        return ResponseEntity.ok(appliedJobs);
    }


    @DeleteMapping(path = "/remove/{userid}/{jobid}")
    public ResponseEntity<String> removeSavedJob(@PathVariable("userid") String userid, @PathVariable("jobid") String jobid) throws UpdatingException {

        Boolean isRemoved = false;
        isRemoved = jobService.removeSavedJob(userid, jobid);
        JsonObject resp = Json.createObjectBuilder()
                .add("isRemoved", isRemoved)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }


    @GetMapping("/all-skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> appliedJobs = jobService.getSkills();
        return ResponseEntity.ok(appliedJobs);
    }

    @GetMapping("/updatelastseen/{jobid}")
    public ResponseEntity<String> updateLastSeen(@PathVariable("jobid") String jobid) {

        Boolean isUpdated = false;

        if (jobService.updateLastSeen(jobid)){
            JsonObject resp = Json.createObjectBuilder()
                .add("isUpdated", true)
                .add("status", 200)
                .build();

                return ResponseEntity.ok(resp.toString());
        };

        return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated)
                    .build()
                    .toString());
    }





    

}
