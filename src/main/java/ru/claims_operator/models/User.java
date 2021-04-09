package ru.claims_operator.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import javax.persistence.*;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cl_user")
// , uniqueConstraints = {@UniqueConstraint(columnNames = "email", name = "users_unique_email_idx")}
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "email")// , unique = true
    private String email;

    @Column(name = "hash_password")
    @JsonIgnore
    private String hashPassword;

    @ElementCollection
    @CollectionTable(name = "token", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "value")
    @JsonIgnore
    private List<String> tokens;

    //    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role"}, name = "user_roles_unique_idx")})
    @Column(name = "role")
    @ElementCollection(fetch = FetchType.EAGER)
//    @BatchSize(size = 200)
    private Set<Role> roles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "claimsUser", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Claim> claims;


    public void setRoles(Collection<Role> roles) {
        this.roles = CollectionUtils.isEmpty(roles) ? EnumSet.noneOf(Role.class) : EnumSet.copyOf(roles);
    }
}
