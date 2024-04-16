package vttp.mainproject.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.mainproject.backend.model.Applicant;
import vttp.mainproject.backend.model.EmpHistory;
import vttp.mainproject.backend.model.Skill;
import vttp.mainproject.backend.service.UserService;

@RestController
@CrossOrigin
@RequestMapping(path = "/user")
public class UserProfileRestController {

    @Autowired
    UserService userService;

    @PostMapping(path = "/profile-pic/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAppProfilePic(@PathVariable("userid") String userid,
            @RequestPart(name = "profile_pic", required = false) MultipartFile profile) {

        Boolean isUpdated = false; 
        String url;

        try {
            url = userService.updatePicFile(profile.getInputStream(), profile.getContentType(),profile.getSize(), userid);

            isUpdated = true;

            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated)
                    .add("url", url)
                    .build().toString());
        } catch (IOException ex) {
            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated).build().toString());
        }

    }

    @PostMapping(path = "/resume/{userid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateAppProfileFile(@PathVariable("userid") String userid,
            @RequestPart(name = "resume", required = false) MultipartFile resume) {

        Boolean isUpdated = false;
        String url;
        try {
            url = userService.updateResFile(resume.getInputStream(), resume.getContentType(),
                    resume.getSize(), userid);
            isUpdated = true;

            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated)
                    .add("url", url)
                    .build().toString());
        } catch (IOException ex) {
            return ResponseEntity.ok(Json.createObjectBuilder()
                    .add("isUpdated", isUpdated).build().toString());
        }
    }

    @PostMapping(path = "/applicant/update/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAppProfile(@PathVariable("userid") String userid,
            @RequestBody Applicant applicant) {

        Boolean isAdded = false;
        isAdded = userService.updateAppProfile(applicant);

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/applicant/{userid}")
    public ResponseEntity<Applicant> updateAppProfile(@PathVariable("userid") String userid) {
        Applicant app = userService.getAppProfile(userid);

        return ResponseEntity.ok(app);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Applicant>> getAll() {
        List<Applicant> app = userService.getAllAppUsers();

        return ResponseEntity.ok(app);
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Applicant>> process(@RequestParam("search") String search) {
        List<Applicant> app = userService.getAllAppByTerm(search);
        return ResponseEntity.ok(app);
    }

    @PostMapping(path = "/employment/update/{userid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateEmployment (@PathVariable("userid") String userid,
            @RequestBody EmpHistory values) {
        Boolean isAdded = false;
        isAdded = userService.updateEmployment(userid, values); 

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/employment-history/{userid}")
    public ResponseEntity<List<EmpHistory>> getEmploymentHistory(@PathVariable("userid") String userid) {
        List<EmpHistory> all = userService.getEmpHistories(userid);

        return ResponseEntity.ok(all);
    }


    @GetMapping("/gethistory/{histid}")
    public ResponseEntity<EmpHistory> getHistory(@PathVariable("histid") String histid) {
        EmpHistory all = userService.getEmpHistory(histid);

        return ResponseEntity.ok(all);
    }

    @PostMapping(path = "/updatehist/{histid}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateHistory(@PathVariable("histid") String histid, @RequestBody EmpHistory values) {

        Boolean isAdded = false;
        isAdded = userService.updateEmpHistory(values); 

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }


    @GetMapping("remove-history/{histid}")
    public ResponseEntity<String> removeHistory(@PathVariable("histid") String histid) {

        Boolean isAdded = false;
        isAdded = userService.removeHistory(histid);

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }

    @PostMapping(path = "/updateskills/{userid}" , consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSkills(@PathVariable("userid") String userid, @RequestBody List<Skill> values) {
        Boolean isAdded = false;
        isAdded = userService.updateSkills(userid, values); 

        JsonObject resp = Json.createObjectBuilder()
                .add("isAdded", isAdded)
                .add("status", 200)
                .build();

        return ResponseEntity.ok(resp.toString());
    }

    @GetMapping("/allskills/{userid}")
    public ResponseEntity<List<Skill>> getUserSkills(@PathVariable("userid") String userid) {
        List<Skill> all = userService.getUserSkills(userid);

        return ResponseEntity.ok(all);
    }


}
