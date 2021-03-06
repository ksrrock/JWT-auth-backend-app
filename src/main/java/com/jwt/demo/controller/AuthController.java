package com.jwt.demo.controller;

import com.jwt.demo.payload.*;
import com.jwt.demo.security.JwtTokenHelper;
import com.jwt.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired private JwtTokenHelper jwtTokenHelper;

  @Autowired private UserDetailsService userDetailsService;

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private UserService userService;

  @PostMapping("/login")
  public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) {
    this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
    UserDetails userDetails =
        this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
    String token = this.jwtTokenHelper.generateToken(userDetails);
    JwtAuthResponse response = JwtAuthResponse.builder().token(token).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  private void authenticate(String username, String password) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    try {
      this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    } catch (Exception e) {
      throw new BadCredentialsException("Username or password is incorrect");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(
      @RequestBody UserRequestDto userRequestDto) {
    UserResponseDto userResponseDto = userService.registerNewUser(userRequestDto);
    ApiResponse<UserResponseDto> apiResponse =
        ApiResponse.<UserResponseDto>builder().data(userResponseDto).errors(null).build();
    return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
  }
}
