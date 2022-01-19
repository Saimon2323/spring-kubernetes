package com.saimon.controllers;

import com.saimon.exception.ResourceNotFoundException;
import com.saimon.models.UserApi;
import com.saimon.repository.UserApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Muhammad Saimon
 * @since Apr 4/18/21 2:10 AM
 */

// https://dzone.com/articles/how-to-create-rest-api-with-spring-boot
// https://github.com/givanthak/spring-boot-rest-api-tutorial

@RestController
@RequestMapping("/api/v1")
public class UserApiController {

    @Autowired
    private UserApiRepository userApiRepository;

    @GetMapping("/users")
    public List<UserApi> getAllUsers() {
        return userApiRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserApi> getUsersById(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        UserApi userApi = userApiRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        return ResponseEntity.ok().body(userApi);
    }

    @PostMapping("/users")
    public UserApi createUser(@Valid @RequestBody UserApi userApi) {
        return userApiRepository.save(userApi);
    }

    @PutMapping("users/{id}")
    public ResponseEntity<UserApi> updateUser(@PathVariable(value = "id") Long userId, @Valid @RequestBody UserApi userApi)
            throws ResourceNotFoundException {
        UserApi user = userApiRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));


        user.setEmail(userApi.getEmail());
        user.setFirstName(userApi.getFirstName());
        user.setLastName(userApi.getLastName());
        user.setUpdatedAt(new Date());

        UserApi savedUser = userApiRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("users/{id}")
    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
        UserApi userApi = userApiRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + userId));

        userApiRepository.delete(userApi);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);

        return response;
    }


}
