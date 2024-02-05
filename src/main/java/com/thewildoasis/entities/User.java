package com.thewildoasis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.thewildoasis.annotations.FieldsValueMatch;
import com.thewildoasis.annotations.PasswordStrength;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Users")
@JsonIgnoreProperties({"authorities", "accountNonExpired",
        "accountNonLocked", "credentialsNonExpired",
        "enabled", "tokens", "confirmationPassword"})
@FieldsValueMatch.List({@FieldsValueMatch(
        field = "password",
        fieldMatch = "confirmPassword",
        message = "Password and confirm password do not match!"
)})
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotBlank(message = "Full name must not be blank.")
    @Size(min = 3, message = "Full name must be at least 3 characters long.")
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @NotBlank(message = "Email should not be blank.")
    @Email(message = "Please provide valid email address.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @PasswordStrength
    @NotBlank(message = "Password should not be blank.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    private String password;

    @PasswordStrength
    @Size(min = 8, message = "Confirm password must be at least 8 characters long.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Transient
    private String confirmPassword;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(targetEntity = Token.class, fetch = FetchType.EAGER,
            mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    private Set<Token> tokens;

    @Column(name = "avatar")
    private String avatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" +
                "Username=" + this.email + ", " +
                "Password=[PROTECTED], " +
                "Enabled=" + this.isEnabled() + ", " +
                "AccountNonExpired=" + this.isAccountNonExpired() + ", " +
                "CredentialsNonExpired=" + this.isCredentialsNonExpired() + ", " +
                "AccountNonLocked=" + this.isAccountNonExpired() + ", " +
                "Granted Authorities=" + this.getAuthorities() + "]";
    }

}
