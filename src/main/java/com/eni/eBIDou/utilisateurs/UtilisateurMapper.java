package com.eni.eBIDou.utilisateurs;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    UtilisateurDTO toDto(UtilisateurBO bo);
    UtilisateurBO toBo(UtilisateurDTO dto);
}
