package com.data.backend.service;

import com.data.backend.model.Creator;
import com.data.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<Creator> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Creator> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Creator createUser(Creator user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
