package com.litmus7.streakify.service;

import com.litmus7.streakify.entity.User;
import com.litmus7.streakify.exception.ResourceNotFoundException;
import com.litmus7.streakify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private  UserRepository userRepository ;

    public User registerUser(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw  new RuntimeException("Email is already registerd!!! ");
        }
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll(); //
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(("User not found with id: "+ id)));
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Cannot delete. User not found with id: "+ id);
        }
        userRepository.deleteById(id);
    }
}
