package ru.noleg.authorisationservice.service.user;


import ru.noleg.authorisationservice.entity.Role;
import ru.noleg.authorisationservice.entity.User;

import java.util.List;

public interface UserService {
    User save(User user);

    User getUser(Long id);

    List<User> getAllUsers();

    User addUserRole(Long userId, Role role);

    void updateUserRole(Long userId, Role role);

    void delete(Long id);
}
