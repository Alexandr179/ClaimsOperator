package ru.claims_operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.claims_operator.dto.ClaimDto;
import ru.claims_operator.mapper.ClaimDtoMapper;
import ru.claims_operator.models.Claim;
import ru.claims_operator.models.State;
import ru.claims_operator.repositories.ClaimsRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
@RequestMapping("/api/v0.1")
public class ClaimsController {

    @Autowired
    private ClaimsRepository claimsRepository;

    @Autowired
    private ClaimDtoMapper claimDtoMapper;

    @PreAuthorize("hasAuthority('OPERATOR')")// get SENDED claims
    @GetMapping("/claims/sended")
    public ResponseEntity<List<ClaimDto>> getSendedClaims() {

        List<Claim> sendedClaims = claimsRepository.findAllByState(State.SENDED);
        if (!sendedClaims.isEmpty()) {
            return ResponseEntity.ok(sendedClaims.stream().map(claim -> claimDtoMapper.toDto(claim)).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }


    @PreAuthorize("hasAuthority('OPERATOR')")// set ASSEPT/REJECT-state Claim
    @PostMapping("/claims/{claim-id}/{state}")
    public ResponseEntity<ClaimDto> setClaimStatus(@PathVariable("claim-id") Long claimId,
                                                   @PathVariable("state") String state) {
        State stateOf = null;
        try {
            stateOf = State.valueOf(state.toUpperCase());
        } catch (Exception ignore) {
        }
        Optional<Claim> claim = claimsRepository.findById(claimId);

        if (claim.isPresent() & (stateOf == State.ACCEPTED || stateOf == State.REJECTED)) {
            claim.get().setState(stateOf);
            Claim savedClaim = claimsRepository.save(claim.get());
            return ResponseEntity.ok(claimDtoMapper.toDto(savedClaim));
        }
        return ResponseEntity.status(BAD_REQUEST).build();
    }


    @PreAuthorize("hasAuthority('USER')")// add new Claim
    @PostMapping("/claims")
    public ResponseEntity<ClaimDto> newDraftClaim(@RequestBody ClaimDto claimDto) {
        Claim claim = claimDtoMapper.toEntity(claimDto);
        Claim savedClaim = claimsRepository.save(claim);
        return ResponseEntity.ok(claimDtoMapper.toDto(savedClaim));
    }


    @PreAuthorize("hasAuthority('USER')")// get user's Claims
    @GetMapping("/claims/user/{user-id}")
    public ResponseEntity<List<ClaimDto>> getUsersClaims(@PathVariable("user-id") Long userId) {

        List<Claim> usersClaims = claimsRepository.findAllByClaimsUser_Id(userId);
        if (!usersClaims.isEmpty()) {
            return ResponseEntity.ok(usersClaims.stream().map(claim -> claimDtoMapper.toDto(claim)).collect(Collectors.toList()));
        }
        return ResponseEntity.notFound().build();
    }


    @PreAuthorize("hasAuthority('USER')")// update Claim with DRAFT-state
    @PostMapping("/claims/draft")
    public ResponseEntity<ClaimDto> updateDraftClaim(@RequestBody ClaimDto claimDto) {
        if(claimDto.getState() == State.DRAFT){
            Claim claim = claimDtoMapper.toEntity(claimDto);
            Claim savedClaim = claimsRepository.save(claim);
            return ResponseEntity.ok(claimDtoMapper.toDto(savedClaim));
        }
        return ResponseEntity.status(UNPROCESSABLE_ENTITY).build();
    }


    @PreAuthorize("hasAuthority('USER')")// set SENDED-state Claim
    @PostMapping("/claims/{claim-id}/send")
    public ResponseEntity<ClaimDto> setSendedClaimStatus(@PathVariable("claim-id") Long claimId) {
        Optional<Claim> claim = claimsRepository.findById(claimId);
        if (claim.isPresent()) {
            claim.get().setState(State.SENDED);
            Claim savedClaim = claimsRepository.save(claim.get());
            return ResponseEntity.ok(claimDtoMapper.toDto(savedClaim));
        }
        return ResponseEntity.status(BAD_REQUEST).build();
    }
}
