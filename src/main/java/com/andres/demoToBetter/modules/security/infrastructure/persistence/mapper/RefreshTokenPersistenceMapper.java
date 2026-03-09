package com.andres.demotobetter.modules.security.infrastructure.persistence.mapper;

import org.mapstruct.Mapper;
import com.andres.demotobetter.modules.security.domain.model.RefreshToken;
import com.andres.demotobetter.modules.security.infrastructure.persistence.entity.RefreshTokenEntity;

/**
 * Mapper para transformar entre la Entidad de BD y el Record de Dominio.
 * Usamos 'uses' para que sepa cómo mapear el UserSecurity internamente.
 */
@Mapper(componentModel = "spring", uses = { UserSecurityPersistenceMapper.class })
public interface RefreshTokenPersistenceMapper {

    // Convierte la Entity de base de datos al Record de Dominio
    RefreshToken toDomain(RefreshTokenEntity entity);

    // Convierte el Record de Dominio a la Entity para guardar en base de datos
    RefreshTokenEntity toEntity(RefreshToken domain);
}
