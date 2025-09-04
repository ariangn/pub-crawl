package com.pubcrawl.book;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "groupId", source = "group.id")
    BookDto toDto(Book book);
}
