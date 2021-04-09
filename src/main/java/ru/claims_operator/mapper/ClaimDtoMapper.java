package ru.claims_operator.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.claims_operator.dto.ClaimDto;
import ru.claims_operator.models.Claim;
import ru.claims_operator.repositories.UsersRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class ClaimDtoMapper {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UsersRepository usersRepository;


    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Claim.class, ClaimDto.class);
//                .addMappings(m -> m.skip(ClaimDto::setId)).setPostConverter(toDtoConverter());

        mapper.createTypeMap(ClaimDto.class, Claim.class)
                .addMappings(m -> m.skip(Claim::setClaimsUser)).setPostConverter(toEntityConverter())
                .addMappings(m -> m.skip(Claim::setCreatedTime)).setPostConverter(toEntityConverter());
    }

    public ClaimDto toDto(Claim entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, ClaimDto.class);
    }

    private Converter<Claim, ClaimDto> toDtoConverter() {
        return context -> {
            Claim source = context.getSource();
            ClaimDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(Claim source, ClaimDto destination) {
        // business logic
    }


    public Claim toEntity(ClaimDto dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, Claim.class);
    }

    private Converter<ClaimDto, Claim> toEntityConverter() {
        return context -> {
            ClaimDto source = context.getSource();
            Claim destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    // business logic
    void mapSpecificFields(ClaimDto source, Claim destination) {
        destination.setClaimsUser(usersRepository.findById(source.getUserId()).orElse(null));
        destination.setCreatedTime(LocalDateTime.now());
    }
}