package com.example.SilkWay.service;

import com.example.SilkWay.model.Role;
import com.example.SilkWay.model.User;
import com.example.SilkWay.repository.RoleRepository;
import com.example.SilkWay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;


@Service("userService")
public class UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private EmailService emailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository,
                       @Qualifier("roleRepository") RoleRepository roleRepository,
                       EmailService emailService,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user,String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);
        Role userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    public void sendTokenToConfirm(User user, HttpServletRequest request){
        user.setToken(UUID.randomUUID().toString());

        String appUrl = request.getScheme() + "://" + request.getServerName();

        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(user.getEmail());
        registrationEmail.setSubject("Confirmation to OpenTravel site");
        registrationEmail.setText("Hello "+ user.getFirstName()+"! \n" +
                "Welcome to OpenTravel site, We so happy to see you as a "+ user.getStatus()+" in our big tourism family!"+
                "\n Please confirm your gmail on opentravel site to finish registration,click the link below:\n\n"
                + appUrl + ":8099/confirm?token=" + user.getToken()+"\n\n" +
                "best regards,\n" +
                "OpenTravel team")
        ;
        registrationEmail.setFrom("noreply@domain.com");
        emailService.sendEmail(registrationEmail);
    }

    public ArrayList<User> getAll()
    {
        return (ArrayList) userRepository.findAll();
    }

    public User findByToken(String token){
        return userRepository.findByToken(token);
    }

    public User save(User user){
        return userRepository.save(user);
    }

    public void updateUser(User user){
        User user1=findUserById(user.getId());
        user1.setFirstName(user.getFirstName());
        user1.setLastName(user.getLastName());
        user1.setContacts(user.getContacts());
        save(user1);
    }

    public void deleteUser(User user){
        userRepository.delete(user);
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }
}