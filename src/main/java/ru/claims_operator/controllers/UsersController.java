package ru.claims_operator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.claims_operator.dto.UserDto;
import ru.claims_operator.mapper.UserDtoMapper;
import ru.claims_operator.models.Role;
import ru.claims_operator.models.User;
import ru.claims_operator.repositories.UsersRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v0.1")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserDtoMapper userDtoMapper;

    @PreAuthorize("hasAuthority('ADMIN')")// get All Users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        List<User> allUsers = usersRepository.findAll();
        return ResponseEntity.ok(allUsers.stream().map(user -> userDtoMapper.toDto(user)).collect(Collectors.toList()));
    }

    @PreAuthorize("hasAuthority('ADMIN')")// set OPERATOR-state for user
    @PostMapping("/users/{user-id}/state/operator")
    public ResponseEntity<UserDto> setUserOperatorStatus(@PathVariable("user-id") Long userId) {

        Optional<User> user = usersRepository.findById(userId);
        if(user.isPresent()){
            Set<Role> roles = user.get().getRoles();
            roles.add(Role.OPERATOR);
            user.get().setRoles(roles);
            User savedUser = usersRepository.save(user.get());
            return ResponseEntity.ok(userDtoMapper.toDto(savedUser));
        }
        return ResponseEntity.notFound().build();
    }
}
