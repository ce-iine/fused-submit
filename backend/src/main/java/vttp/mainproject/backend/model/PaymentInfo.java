package vttp.mainproject.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {

    private String successUrl;
    private String cancelUrl;
    
}
