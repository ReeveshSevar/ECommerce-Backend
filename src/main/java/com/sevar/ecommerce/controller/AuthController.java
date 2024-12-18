package com.sevar.ecommerce.controller;
import com.sevar.ecommerce.exception.UserException;
import com.sevar.ecommerce.model.Users;
import com.sevar.ecommerce.repository.UserRepository;
import com.sevar.ecommerce.request.LoginRequest;
import com.sevar.ecommerce.response.JwtResponse;
import com.sevar.ecommerce.security.jwt.JwtProvider;
import com.sevar.ecommerce.security.user.CustomUserServiceImpl;
import com.sevar.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomUserServiceImpl customUserService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register-user")
    public ResponseEntity<JwtResponse> createUser(@RequestBody Users users) throws UserException
    {
        String email = users.getEmail();
        String password = users.getPassword();
        String firstName = users.getFirstName();
        String lastName = users.getLastName();
        String mobile = users.getMobile();
        Users isEmailExist = repo.findByEmail(email);
        if (isEmailExist != null){
            throw new UserException("Email Already Registered With Another Account");
        }
        Users createdUser = new Users();
        createdUser.setEmail(email);
        createdUser.setPassword(encoder.encode(password));
        createdUser.setFirstName(firstName);
        createdUser.setLastName(lastName);
        createdUser.setMobile(mobile);
        createdUser.setCreatedAt(LocalDateTime.now());

        if (email.equalsIgnoreCase("reeveshsevar@gmail.com"))
            createdUser.setRoles("ROLE_ADMIN");
        else
            createdUser.setRoles("ROLE_USER");
        repo.save(createdUser);
        cartService.createCart(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        JwtResponse jwtResponse = new JwtResponse(token,"Signup Success");
        return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest login)
    {
        String username = login.getEmail();
        String password = login.getPassword();
        Authentication authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        JwtResponse jwtResponse = new JwtResponse(token,"SignIn Success");
        return new ResponseEntity<JwtResponse>(jwtResponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if (userDetails == null)
            throw new BadCredentialsException("Invalid User");

        if (!encoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}

