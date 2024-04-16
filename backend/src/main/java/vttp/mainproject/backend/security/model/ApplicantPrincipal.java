package vttp.mainproject.backend.security.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import vttp.mainproject.backend.model.Applicant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantPrincipal implements UserDetails {

    private Applicant applicant;
    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(applicant.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return applicant.getPassword();
    }

    @Override
    public String getUsername() {
        return applicant.getId();
    }


    @Override
    public boolean isAccountNonExpired() {
        return applicant != null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return applicant != null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return applicant != null;
    }

    @Override
    public boolean isEnabled() {
        return applicant != null;
    }
}
