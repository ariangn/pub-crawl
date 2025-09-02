package com.pubcrawl.group;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupMapper {
    @Mapping(target = "ownerId", source = "owner.id")
    GroupDto toDto(Group group);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "inviteCode", ignore = true)
    Group toEntity(CreateGroupRequest request);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "pfpUrl", source = "user.pfpUrl")
    GroupMemberDto toDto(GroupMember groupMember);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "highlightColor", source = "highlightColor")
    @Mapping(target = "joinedAt", expression = "java(java.time.LocalDateTime.now())")
    GroupMember toEntity(JoinGroupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "joinedAt", ignore = true)
    void update(UpdateMemberRequest request, @MappingTarget GroupMember groupMember);
}
