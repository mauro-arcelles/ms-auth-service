//package com.project1.ms_auth_service;
//
//import com.project1.ms_auth_service.config.auth.jwt.JwtService;
//import com.project1.ms_auth_service.model.entity.User;
//import io.reactivex.rxjava3.core.Single;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//    @Autowired
//    private JwtService jwtService;
//
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/login")
//    public Single<String> login(@RequestBody AuthRequest request) {
//        return authService.authenticate(request)
//            .flatMap(jwtService::generateToken);
//    }
//
//    @PostMapping("/register")
//    public Single<User> register(@RequestBody RegisterRequest request) {
//        return authService.register(request);
//    }
//}
