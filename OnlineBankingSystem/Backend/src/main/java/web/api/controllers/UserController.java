package web.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import web.api.models.Prospect;
import web.api.models.User;
import web.api.services.ProspectService;
import web.api.services.UserService;
import web.api.utilities.JWTUtil;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final ProspectService prospectService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;


    @Autowired
    public UserController(UserService userService, ProspectService prospectService, AuthenticationManager authenticationManager, JWTUtil jwtUtil){
        this.userService=userService;
        this.prospectService = prospectService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> userLogin(@RequestBody User user) throws BadCredentialsException{

        try{
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getEmailId(),user.getPassword()));
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Email_Id or password is incorrect", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(user.getEmailId());
        final String jwt = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Prospect> getUsers(@RequestBody Prospect newProspect) {
        Prospect object = prospectService.addProspect(newProspect);
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }
}