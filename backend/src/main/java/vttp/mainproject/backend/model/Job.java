package vttp.mainproject.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    private Long id;
    private String title;
    private String company_name;
    private String category;
    private String job_type;
    private List<String> candidate_required_location;
    private String description;
    private String company_logo; 
    private LocalDateTime publication_date;
    private Boolean promoted = false;
    private LocalDateTime promoted_time;
    private List<String> tags;

}