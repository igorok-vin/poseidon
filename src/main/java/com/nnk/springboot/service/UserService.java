package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.UserNameExistInDataBaseException;
import com.nnk.springboot.exception.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get a list of all users
     * @return list of User containing all users
     */
    public List<User> findAllUsers() {
        logger.info("Service: display all users");
        return userRepository.findAll();
    }

    /**
     * Get user by id.
     * @param id Integer that containing id of the User
     * @return Instance of User
     */
    public User getUserById(int id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            logger.info("Service: User with Id "+id+" was found");
            return user.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Save a new User
     * @param user
     * @return User saved
     */
    public User saveUser(User user) {
        List<User> usersList = findAllUsers();
        for(User userToSave: usersList){
            if(userToSave.getUsername().equals(user.getUsername())){
                throw new UserNameExistInDataBaseException
                        ("This user name "+"'"+user.getUsername() +"'"+" already exist. User name must be unique");
            }
        }
        logger.info("Service: save new user to DB");
        return userRepository.save(user);
    }

    /**
     * Update existing user
     * @param user instance
     * @return the User updated
     */
    public User updateUser (User user) {
        User userForUpdate = userRepository.getById(user.getId());

        List<User>listWithOneUserForUpdate = userRepository.findAllById(Collections.singleton(user.getId()));

        List<User> listWithAllUsers = findAllUsers();

        List<User> usersListWithoutUserForUpdate = listWithAllUsers.stream().filter(
                user1 -> !listWithOneUserForUpdate.contains(user1)
        ).collect(Collectors.toList());

        userForUpdate.setFullname(user.getFullname());
        userForUpdate.setUsername(user.getUsername());
        userForUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        for (User user2: usersListWithoutUserForUpdate) {
            if (user2.getUsername().equals(user.getUsername())) {
                throw new UserNameExistInDataBaseException
                        ("This user name " + "'" + user.getUsername() + "'" + " already exist. User name must be unique");
            }
        }
        logger.info("Service: User updated with ID: " + user.getId());
        return userRepository.save(userForUpdate);
    }

    /**
     * Delete a User by id
     * @param id Integer that containing id of the User
     */
    public void deleteUser(Integer id) {
        logger.info("Service: User deleted with ID: " + id);
        userRepository.deleteById(id);
        }
}
