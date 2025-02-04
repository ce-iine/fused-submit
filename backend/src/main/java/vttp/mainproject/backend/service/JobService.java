package vttp.mainproject.backend.service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import vttp.mainproject.backend.exceptions.UpdatingException;
import vttp.mainproject.backend.model.AppliedJob;
import vttp.mainproject.backend.model.Job;
import vttp.mainproject.backend.model.Skill;
import vttp.mainproject.backend.model.Utils;
import vttp.mainproject.backend.repo.BizJobRepo;
import vttp.mainproject.backend.repo.JobRepo;
import vttp.mainproject.backend.repo.ListingsRepo;
import vttp.mainproject.backend.repo.s3Repo;

@Service
public class JobService {

    @Autowired
    JobRepo jobRepo;

    @Autowired
    s3Repo s3Repo;

    @Autowired
    ListingsRepo listingsRepo;

    @Autowired
    BizJobRepo bizJobRepo;

    private Counter saveCounter = null;
    private Gauge memoryUsage;

    public JobService(CompositeMeterRegistry meterRegistry) {
        saveCounter = meterRegistry.counter("save.count");
        memoryUsage = Gauge.builder("myapp.memory.usage", Runtime.getRuntime(), Runtime::totalMemory)
                           .description("Memory Usage")
                           .register(meterRegistry);
    }

    @Transactional(rollbackFor = UpdatingException.class)
    public Boolean insertNewApplication(InputStream is, String contentType, long length, String userid, String jobid) throws UpdatingException {
        LocalDateTime currentDate = LocalDateTime.now().plusHours(8);
        // System.out.println("CURRENT DATE" + currentDate);
        String resumeId = userid + "/" + jobid;
        String url = s3Repo.saveToS3(resumeId, is, contentType, length);
        // String url = s3Repo.getImageUrl("resume/%s".formatted(resumeId)); 

        if ((jobRepo.insertNewApplication(resumeId, userid, jobid, url, currentDate)) &&
            (jobRepo.increaseApplicationCount(jobid, currentDate))){
                return true;
            } else {
                throw new UpdatingException("You already applied for this job :)");
            }
    }

    public Boolean updateLastSeen(String jobid) {
        LocalDateTime currentDate = LocalDateTime.now().plusHours(8);
        return jobRepo.updateLastSeen(jobid, currentDate);
    }

    public List<AppliedJob> getAppliedJobs(String userId) { 
        List<AppliedJob> jobIds = jobRepo.getAppliedByUserId(userId);

        List<Job> appliedJobs = new ArrayList<>();
        for (AppliedJob j : jobIds) {
            Document doc = listingsRepo.getJobById(j.getId()); 
            Job job = Utils.toJob(doc);

            j.setCandidate_required_location(job.getCandidate_required_location());
            j.setJob_type(job.getJob_type());
            j.setTitle(job.getTitle());
            j.setCompany_logo(job.getCompany_logo());
            j.setCompany_name(job.getCompany_name());
            j.setTags(job.getTags());
            appliedJobs.add(job);
        }

        return jobIds;
    }

    @Transactional(rollbackFor = UpdatingException.class)
    public Boolean insertNewBookmark(String userid, String jobid) throws UpdatingException {
        saveCounter.increment();
        if ((jobRepo.insertNewBookmark(userid, jobid)) && (jobRepo.increaseBookmarkCount(jobid))){
            return true;
        } else {
            throw new UpdatingException("You already saved this job");
        }
    }

    public List<Job> getSavedJobs(String userId) {
        List<Long> jobIds = jobRepo.getSavedJobs(userId);

        List<Job> savedJobs = new ArrayList<>();
        for (Long j : jobIds) {
            Document doc = listingsRepo.getJobById(j);
            Job job = Utils.toJob(doc);
            savedJobs.add(job);
        }

        return savedJobs;
    }

    @Transactional(rollbackFor = UpdatingException.class)
    public Boolean removeSavedJob(String userid, String jobid) throws UpdatingException { 
        if ((jobRepo.removeSavedJob(userid, jobid)) && (jobRepo.decreaseBookmarkCount(jobid))){
            return true;
        } else {
            throw new UpdatingException("error removing bookmark");
        }
    }

    @Transactional(rollbackFor = UpdatingException.class)
    public Boolean updatePromoted(String id) throws UpdatingException{
        if(jobRepo.updatePromoted(id) && listingsRepo.updatePromoted(id) != null){ 
            return true;
        } else {
            throw new UpdatingException("error updating promoted job status ");
        }
    } 

    public List<Skill> getSkills(){
        return jobRepo.getSkills();
    }

    public Boolean insertPostWithLogo(InputStream is, String contentType, long length, Job post) { 
        Random rand = new Random();
        Long jobid = 10000000 + rand.nextLong(90000000);
        post.setId(jobid); 

        LocalDateTime currentDate = LocalDateTime.now().plusHours(8); 
        post.setPublication_date(currentDate); 

        String url = s3Repo.saveToS3(jobid.toString(), is, contentType, length);
        post.setCompany_logo(url);

        if (bizJobRepo.hasCompany(post.getCompany_name())) {
            if ((listingsRepo.insertNewJob(post) != null) && (bizJobRepo.insertNewPost(post))) {
                return true;
            }
        }

        return false;
    }



}
