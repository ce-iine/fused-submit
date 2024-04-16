package vttp.mainproject.backend.security.jwt;

import java.nio.CharBuffer;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vttp.mainproject.backend.exceptions.InvalidPasswordException;
import vttp.mainproject.backend.exceptions.UserNotFoundException;
import vttp.mainproject.backend.model.Applicant;
import vttp.mainproject.backend.model.Business;
import vttp.mainproject.backend.model.LoginUser;
import vttp.mainproject.backend.repo.UserRepo;
import vttp.mainproject.backend.security.model.ApplicantPrincipal;
import vttp.mainproject.backend.security.model.BusinessPrincipal;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Applicant> applicant = userRepo.getUserByEmail(email);
        Optional<Business> business = userRepo.getBusinessByEmail(email);

        if (applicant.isPresent()) {
            return new ApplicantPrincipal(applicant.get());
        } else if (business.isPresent()) {
            return new BusinessPrincipal(business.get());
        } else {
            throw new UsernameNotFoundException("Email not found!");
        }
    }

    public ApplicantPrincipal loginApplicant(LoginUser loginUser) throws UserNotFoundException, InvalidPasswordException {

        String loginEmail = loginUser.getEmail().trim().toLowerCase();

        Optional<Applicant> optVisitor = userRepo.getUserByEmail(loginEmail); 
        if (optVisitor.isEmpty()) {
            throw new UserNotFoundException("Email is not registered.");
        }
        Applicant retrievedVisitor = optVisitor.get();
        if (!passwordEncoder.matches(
                CharBuffer.wrap(loginUser.getPassword()),
                retrievedVisitor.getPassword())) {
            throw new InvalidPasswordException("Invalid password. Please try again.");
        }
        return new ApplicantPrincipal(retrievedVisitor);
    }

    public BusinessPrincipal loginBusiness(LoginUser loginUser) throws UserNotFoundException, InvalidPasswordException {

        String loginEmail = loginUser.getEmail().trim().toLowerCase();

        Optional<Business> optExhibitor = userRepo.getBusinessByEmail(loginEmail);
        if (optExhibitor.isEmpty()) {
            throw new UserNotFoundException("Business email is not registered.");
        }
        Business retrievedExhibitor = optExhibitor.get();
        if (!passwordEncoder.matches(
                CharBuffer.wrap(loginUser.getPassword()),
                retrievedExhibitor.getPassword())) {
            throw new InvalidPasswordException("Invalid password. Please try again.");
        }
        return new BusinessPrincipal(retrievedExhibitor);
    }

}
