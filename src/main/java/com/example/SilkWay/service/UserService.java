package com.example.SilkWay.service;

import com.example.SilkWay.model.Role;
import com.example.SilkWay.model.User;
import com.example.SilkWay.repository.RoleRepository;
import com.example.SilkWay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;


@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User findUserById(int id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user,String role) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole(role);
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }


    public ArrayList<User> getAllByStatus(String status){
        ArrayList<User> list=(ArrayList<User>)userRepository.getAllByStatus(status);
        ArrayList<User> finalist=new ArrayList<User>();
        if(!list.isEmpty() && list.size()>2){
        finalist.add(list.get(0));
        finalist.add(list.get(1));
        finalist.add(list.get(2));
        return finalist;
        }

        return list;
    }

    public ArrayList<User> getAll()
    {
        return (ArrayList) userRepository.findAll();
    }

    public ArrayList<User> findByFirstName(String name){
        return (ArrayList<User>) userRepository.findAllByFirstName(name);
    }

    public User findByToken(String token){
        return userRepository.findByToken(token);
    }

    public User save(User user){
        return userRepository.save(user);
    }

}