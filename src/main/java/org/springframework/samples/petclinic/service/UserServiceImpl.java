package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@SuppressWarnings("unused")
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveUser(User user) throws Exception {
        if (isNull(user.getRoles()) || user.getRoles().isEmpty()) {
            throw new Exception("User must have at least a role set!");
        }
        for (Role role : user.getRoles()) {
            if (!role.getName().startsWith("ROLE_")) {
                role.setName("ROLE_" + role.getName());
            }
            if (isNull(role.getUser())) {
                role.setUser(user);
            }
        }
        userRepository.save(user);
    }
}
