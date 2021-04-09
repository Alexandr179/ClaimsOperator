package ru.claims_operator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.claims_operator.models.State;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClaimDto {

    private Long id;

    private String text;

    private State state;

    private LocalDateTime createdTime;

    private Long userId;
}
