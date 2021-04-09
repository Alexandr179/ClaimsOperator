package ru.claims_operator.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.claims_operator.dto.UserDto;
import ru.claims_operator.models.User;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class UserDtoMapper {

    @Autowired
    private ModelMapper mapper;


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(User.class, UserDto.class);
//                .addMappings(m -> m.skip(UserDto::setId)).setPostConverter(toDtoConverter());
    }

    public UserDto toDto(User entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, UserDto.class);
    }




    private Converter<User, UserDto> toDtoConverter() {
        return context -> {
            User source = context.getSource();
            UserDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(User source, UserDto destination) {
        // business logic
    }
}