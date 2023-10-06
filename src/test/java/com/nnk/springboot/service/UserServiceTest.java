package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.exception.UserNameExistInDataBaseException;
import com.nnk.springboot.exception.UserNotFoundException;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    public void findAllUsers() {
        User user1 = new User();
            user1.setId(1);
            user1.setFullname("fullName1");
            user1.setUsername("userName1");
            user1.setPassword("password1");
            user1.setRole("role1");
        User user2 = new User();
            user2.setId(2);
            user2.setFullname("fullName2");
            user2.setUsername("userName2");
            user2.setPassword("password2");
            user2.setRole("role2");
        List<User> userList = new ArrayList<>();
            userList.add(user1);
            userList.add(user2);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAllUsers();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(userList);
        verify(userRepository).findAll();
     }

    @Test
    public void getUserById() {
        User user = new User();
            user.setId(1);
            user.setFullname("fullName1");
            user.setUsername("userName1");
            user.setPassword("password1");
            user.setRole("role1");
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertEquals(result, user);
        assertThat(1).isEqualTo(result.getId());
        assertThat("fullName1").isEqualTo(result.getFullname());
        assertThat("userName1").isEqualTo(result.getUsername());
        assertThat("password1").isEqualTo(result.getPassword());
        assertThat("role1").isEqualTo(result.getRole());
    }

    @Test
    public void getUserByIdDoTrowUserNotFoundException() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,()->userService.getUserById(1));
        verify(userRepository).findById(isA(Integer.class));
    }

    @Test
    public void saveUser() {
        User user = new User();
            user.setId(1);
            user.setFullname("fullName1");
            user.setUsername("userName1");
            user.setPassword("password1");
            user.setRole("role1");
        List<User> userList = new ArrayList<>();
       when(userRepository.findAll()).thenReturn(userList);
       when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals(result,user);
        assertEquals(1,result.getId());
        assertEquals("fullName1",result.getFullname());
        assertEquals("userName1",result.getUsername());
        assertEquals("password1",result.getPassword());
        assertEquals("role1",result.getRole());
    }

    @Test
    public void saveUserDoThrowUserNameExistInDataBaseException() {
        User userAddingToDB = new User();
            userAddingToDB.setId(2);
            userAddingToDB.setFullname("fullName2");
            userAddingToDB.setUsername("userName1");
            userAddingToDB.setPassword("password2");
            userAddingToDB.setRole("role2");
        User userInDB = new User();
            userInDB.setId(1);
            userInDB.setFullname("fullName1");
            userInDB.setUsername("userName1");
            userInDB.setPassword("password1");
            userInDB.setRole("role1");
        List<User> userList = new ArrayList<>();
        userList.add(userInDB);

        when(userRepository.findAll()).thenReturn(userList);

        assertThrows(UserNameExistInDataBaseException.class,()->userService.saveUser(userAddingToDB));

        verify(userRepository).findAll();
    }

    @Test
    public void updateUser() {
        User userBeforeUpdate = new User();
            userBeforeUpdate.setId(1);
            userBeforeUpdate.setFullname("fullName1");
            userBeforeUpdate.setUsername("userName1");
            userBeforeUpdate.setPassword("password1");
        User userAfterUpdate = new User();
            userAfterUpdate.setId(1);
            userAfterUpdate.setFullname("fullName2");
            userAfterUpdate.setUsername("userName2");
            userAfterUpdate.setPassword("password2");
        List<User> listWithOneUserForUpdate = new ArrayList<>();
            listWithOneUserForUpdate.add(userBeforeUpdate);
        List<User>listofUsersInDB = new ArrayList<>();

        when(userRepository.getById(anyInt())).thenReturn(userBeforeUpdate);
        when(userRepository.findAllById(anyCollection())).thenReturn(listWithOneUserForUpdate);
        when(userRepository.findAll()).thenReturn(listofUsersInDB);
        when(passwordEncoder.encode(any())).thenReturn(userAfterUpdate.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(userAfterUpdate);

        User result = userService.updateUser(userBeforeUpdate);

        assertEquals("userName2",result.getUsername());
    }

    @Test
    public void updateUserDoThrowUserNameExistInDataBaseException() {
        User userBeforeUpdate = new User();
            userBeforeUpdate.setId(1);
            userBeforeUpdate.setFullname("fullName1");
            userBeforeUpdate.setUsername("userName1");
            userBeforeUpdate.setPassword("password1");
        User userAfterUpdate = new User();
            userAfterUpdate.setId(1);
            userAfterUpdate.setFullname("fullName2");
            userAfterUpdate.setUsername("userName1");
            userAfterUpdate.setPassword("password2");

        List<User> listWithOneUserForUpdate = new ArrayList<>();
            listWithOneUserForUpdate.add(userBeforeUpdate);

        List<User>listofUsersInDB = new ArrayList<>();
            listofUsersInDB.add(userAfterUpdate);

        when(userRepository.getById(anyInt())).thenReturn(userBeforeUpdate);
        when(userRepository.findAllById(anyCollection())).thenReturn(listWithOneUserForUpdate);
        when(userRepository.findAll()).thenReturn(listofUsersInDB);
        when(passwordEncoder.encode(any())).thenReturn(userAfterUpdate.getPassword());

        assertThrows(UserNameExistInDataBaseException.class,()->userService.updateUser(userBeforeUpdate));

        verify(userRepository).getById(anyInt());
        verify(userRepository).findAllById(anyCollection());
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1);

        userService.deleteUser(user.getId());

        verify(userRepository).deleteById(1);
    }
}