package vttp.mainproject.backend.model;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobStat {

    private Long id;
    private String title;
    private String company_name;
    private int applied;
    private int saved;
    private LocalDateTime publication_date;
    private LocalDateTime last_updated;
    private LocalDateTime last_seen;
    private Boolean promoted;
}
