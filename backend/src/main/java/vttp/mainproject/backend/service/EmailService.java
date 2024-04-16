package vttp.mainproject.backend.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import vttp.mainproject.backend.model.Applicant;
import vttp.mainproject.backend.model.JobStat;
import vttp.mainproject.backend.repo.UserRepo;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String SENDGRID_API_KEY;

    @Autowired
    UserRepo userRepo;

    public Applicant getUserById(String id) {
        return userRepo.getUserById(id).get();
    } 

    public JobStat getJob(String id) {
        return userRepo.getJob(id).get();
    }

    public String sendEmail(Applicant app, JobStat js) throws IOException {

        Email from = new Email("companyfused@gmail.com");
        String subject = "FUSED - Application for " + js.getTitle();
        Email to = new Email(app.getEmail());

        String htmlContent = "<p>Hi " + app.getFirstName() + " " + app.getLastName() + ",</p>" + "<p>Your application for " + js.getTitle() + " at " + js.getCompany_name() + " has been received!</p>" + "<p>Keep a look out for any updates on this application in your inbox.</p>"
                           + "<img src=\"https://vttpce-iine.sgp1.cdn.digitaloceanspaces.com/resume/01HV0NKXB02MDNRYWMTR4PA5X3/profile01HV8VFG8DEEC7Y7M7MKP7NGYN\" alt=\"Company Logo\" style=\"width: 200px; height: auto;\">";
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();

        try {
          request.setMethod(Method.POST);
          request.setEndpoint("mail/send");
          request.setBody(mail.build());
          Response response = sg.api(request);
          return response.getBody().toString();
        } catch (IOException ex) {
          throw ex;
        }

    }
}
