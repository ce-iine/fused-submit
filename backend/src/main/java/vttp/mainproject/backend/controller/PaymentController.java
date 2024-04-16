package vttp.mainproject.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import vttp.mainproject.backend.model.PaymentInfo;
import vttp.mainproject.backend.service.StripeService;

@RestController
@CrossOrigin
@RequestMapping(path = "/api/checkout")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping
    public ResponseEntity<String> redirectToPayment(@RequestBody PaymentInfo payment) {
        String sessionId = stripeService.createPaymentLink(payment);
        
        return ResponseEntity.ok(Json.createObjectBuilder()
                .add("sessionId", sessionId)
                .build()
                .toString());
    }
    
}
