package ru.claims_operator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.claims_operator.models.Claim;
import ru.claims_operator.models.State;

import java.util.List;

public interface ClaimsRepository extends JpaRepository<Claim, Long> {

    List<Claim> findAllByState(State state);

    List<Claim> findAllByClaimsUser_Id(Long claimsUser_id);
}
