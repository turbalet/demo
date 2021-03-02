package kz.stud.demo.controller;

import kz.stud.demo.model.ERole;
import kz.stud.demo.model.Role;
import kz.stud.demo.model.User;
import kz.stud.demo.payload.request.AuthRequest;
import kz.stud.demo.payload.request.RefreshTokenRequest;
import kz.stud.demo.payload.request.SignUpRequest;
import kz.stud.demo.payload.response.JwtResponse;
import kz.stud.demo.repository.RoleRepository;
import kz.stud.demo.repository.UserRepository;
import kz.stud.demo.secutiry.jwt.JwtUtils;
import kz.stud.demo.secutiry.services.UserDetailsImpl;
import kz.stud.demo.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final RefreshTokenService refreshTokenService;

    private final JwtUtils jwtUtils;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthRequest request){
        Authentication authentication  = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//      List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(
                jwt, userDetails.getUsername(), refreshTokenService.generateRefreshToken().getToken()
        ));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request){
        boolean isValid = refreshTokenService.validateRefreshToken(request.getRefreshToken());
        if(isValid){
            String token = jwtUtils.generateTokenWithUsername(request.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, request.getUsername(), request.getRefreshToken()));
        }else {
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Username is already taken!");
        }

        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest request){
        refreshTokenService.deleteRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok("Refresh token deleted successfully");
    }
}
