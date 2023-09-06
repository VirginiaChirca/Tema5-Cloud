package com.example.payamigo.service;

import com.example.payamigo.model.User;
import com.example.payamigo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UsersService {
    private final UserRepository userRepository;

    @Autowired
    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).get();
    }

    public User getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public boolean deleteUser(Integer uniqueId)
    {
        boolean result = false;

        Optional<User> theUser = userRepository.findById(uniqueId);
        if(theUser.isPresent())
        {
            userRepository.delete(theUser.get());
            result = true;
        }

        return  result;
    }
    public long getNoOfUser()
    {
        return userRepository.count();
    }
}
