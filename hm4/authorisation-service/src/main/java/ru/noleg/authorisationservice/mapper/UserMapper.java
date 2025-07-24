package ru.noleg.authorisationservice.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.noleg.authorisationservice.dto.SignUp;
import ru.noleg.authorisationservice.dto.UpdateUserDto;
import ru.noleg.authorisationservice.dto.UserDto;
import ru.noleg.authorisationservice.dto.UserProfileDto;
import ru.noleg.authorisationservice.entity.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User mapToRegisterEntityFromSignUp(SignUp signUp);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User entity);

    UserDto mapToDto(User user);

    UserProfileDto mapToProfileDto(User user);

    List<UserDto> mapToDtos(List<User> entities);
}
