package ru.noleg.authorisationservice.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.noleg.authorisationservice.entity.Role;
import ru.noleg.authorisationservice.entity.User;
import ru.noleg.authorisationservice.exception.BusinessLogicException;
import ru.noleg.authorisationservice.exception.NotFoundException;
import ru.noleg.authorisationservice.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class UserServiceDefaultImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceDefaultImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void delete(Long id) {

        User user = this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with ID: " + id + " not found.")
        );

        if (user.getRoles().contains(Role.ROLE_ADMIN)) {
            throw new BusinessLogicException("You can't delete an administrator.");
        }

        this.userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User with id: " + id + " not found.")
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User addUserRole(Long userId, Role role) {
        User user = this.getUser(userId);
        user.getRoles().add(role);
        return this.userRepository.save(user);
    }

    @Override
    public void updateUserRole(Long userId, Role role) {
        User user = this.getUser(userId);
        user.getRoles().clear();
        user.getRoles().add(role);
        this.userRepository.save(user);
    }
}
