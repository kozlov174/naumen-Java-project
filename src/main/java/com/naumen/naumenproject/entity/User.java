package com.naumen.naumenproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

import static com.naumen.naumenproject.entity.Role.ADMIN;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Getter
@Setter
@Entity
@Table(name = "user_table")
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(name = "firstName")
    @NotBlank(message = "Значение поля не должно быть пустым")
    private String firstName;

    @Column(name = "lastName")
    @NotBlank(message = "Значение поля не должно быть пустым")
    private String lastName;

    @Column(name = "email")
    @NotBlank(message = "Значение поля не должно быть пустым")
    @Email(message = "Неверный email")
    private String email;

    @Column(name = "password")
    @Size(min = 4, message = "Пароль должен содержать не менее 4 символов")
    private String password;

    @Column(name = "active")
    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

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
        return active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    
    public String getFullName() {
        return getFirstName().length() != 0
                && getLastName().length() != 0
                ? String.format("%s %s", getFirstName(), getLastName())
                : "<Неизвестный пользователь>";
    }

    public boolean isAdmin() {
        return getRoles().contains(ADMIN);
    }

}
