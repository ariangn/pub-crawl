package com.pubcrawl.group;

import com.pubcrawl.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupMapper groupMapper;

    public Iterable<GroupDto> getAllGroups(String sortBy) {
        if (!Set.of("name", "createdAt").contains(sortBy))
            sortBy = "name";

        return groupRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    public Iterable<GroupDto> getUserGroups(UUID userId, String sortBy) {
        final String finalSortBy = Set.of("name", "createdAt").contains(sortBy) ? sortBy : "name";

        var members = groupMemberRepository.findByUserId(userId);
        return members.stream()
                .map(member -> groupMapper.toDto(member.getGroup()))
                .sorted((g1, g2) -> {
                    if (finalSortBy.equals("createdAt")) {
                        return g2.getCreatedAt().compareTo(g1.getCreatedAt());
                    }
                    return g1.getName().compareTo(g2.getName());
                })
                .toList();
    }

    public GroupDto getGroup(UUID groupId) {
        var group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return groupMapper.toDto(group);
    }

    public GroupDto getGroupByInviteCode(String inviteCode) {
        var group = groupRepository.findByInviteCode(inviteCode).orElseThrow(InvalidInviteCodeException::new);
        return groupMapper.toDto(group);
    }

    public GroupDto createGroup(CreateGroupRequest request, User owner) {
        // Check if group name already exists
        if (groupRepository.existsByName(request.getName())) {
            throw new DuplicateGroupNameException();
        }
        
        var group = groupMapper.toEntity(request);
        group.setOwner(owner);
        group.setInviteCode(generateInviteCode());
        
        groupRepository.save(group);
        
        // Add owner as admin member
        var ownerMember = GroupMember.builder()
                .group(group)
                .user(owner)
                .role(MemberRole.ADMIN)
                .highlightColor(generateRandomLightColor()) 
                .joinedAt(java.time.LocalDateTime.now())
                .build();
        groupMemberRepository.save(ownerMember);
        
        return groupMapper.toDto(group);
    }

    public List<GroupMemberDto> getGroupMembers(UUID groupId) {
        var members = groupMemberRepository.findByGroupId(groupId);
        return members.stream()
                .map(groupMapper::toDto)
                .toList();
    }

    public GroupMemberDto joinGroup(UUID groupId, JoinGroupRequest request, User user) {
        var group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        
        // Verify invite code
        if (!group.getInviteCode().equals(request.getInviteCode())) {
            throw new InvalidInviteCodeException();
        }
        
        // Check if user is already a member
        if (groupMemberRepository.existsByGroupIdAndUserId(groupId, user.getId())) {
            throw new DuplicateGroupMemberException();
        }
        
        var member = GroupMember.builder()
                .group(group)
                .user(user)
                .role(MemberRole.MEMBER)
                .highlightColor(request.getHighlightColor() != null ? request.getHighlightColor() : generateRandomLightColor())
                .joinedAt(java.time.LocalDateTime.now())
                .build();
        
        groupMemberRepository.save(member);
        return groupMapper.toDto(member);
    }

    public GroupMemberDto updateMember(UUID groupId, UUID userId, UpdateMemberRequest request) {
        var member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);
        
        groupMapper.update(request, member);
        groupMemberRepository.save(member);
        
        return groupMapper.toDto(member);
    }

    public void removeMember(UUID groupId, UUID userId, User currentUser) {
        // Check if current user is a member of the group
        var currentUserMember = groupMemberRepository.findByGroupIdAndUserId(groupId, currentUser.getId())
                .orElseThrow(UserNotGroupMemberException::new);
        
        // If removing someone else, check if current user is admin
        if (!currentUser.getId().equals(userId)) {
            if (currentUserMember.getRole() != MemberRole.ADMIN) {
                throw new UnauthorizedMemberOperationException();
            }
        }
        
        // Prevent admin from removing themselves
        if (currentUser.getId().equals(userId) && currentUserMember.getRole() == MemberRole.ADMIN) {
            throw new AdminSelfRemovalException();
        }
        
        var member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(GroupMemberNotFoundException::new);
        
        groupMemberRepository.delete(member);
    }

    private String generateInviteCode() {
        String code;
        do {
            code = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (groupRepository.existsByInviteCode(code));
        return code;
    }

    private String generateRandomLightColor() {
        Random random = new Random();
        
        int hue = random.nextInt(360); // full hue range
        int saturation = 40 + random.nextInt(31); // 40-70% saturation
        int lightness = 70 + random.nextInt(21); // 70-90% lightness
        
        // convert HSL to RGB
        return hslToHex(hue, saturation, lightness);
    }
    
    private String hslToHex(int h, int s, int l) {
        double hNorm = h / 360.0;
        double sNorm = s / 100.0;
        double lNorm = l / 100.0;
        
        double c = (1 - Math.abs(2 * lNorm - 1)) * sNorm;
        double x = c * (1 - Math.abs((hNorm * 6) % 2 - 1));
        double m = lNorm - c / 2;
        
        double r, g, b;
        if (hNorm < 1.0/6) {
            r = c; g = x; b = 0;
        } else if (hNorm < 2.0/6) {
            r = x; g = c; b = 0;
        } else if (hNorm < 3.0/6) {
            r = 0; g = c; b = x;
        } else if (hNorm < 4.0/6) {
            r = 0; g = x; b = c;
        } else if (hNorm < 5.0/6) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }
        
        r = (r + m) * 255;
        g = (g + m) * 255;
        b = (b + m) * 255;
        
        return String.format("#%02X%02X%02X", 
            Math.round(r), Math.round(g), Math.round(b));
    }

    public Iterable<GroupWithMembershipDto> getUserGroupsWithMembership(UUID userId, String sortBy) {
        final String finalSortBy = Set.of("name", "createdAt").contains(sortBy) ? sortBy : "name";

        var members = groupMemberRepository.findByUserId(userId);
        return members.stream()
                .map(member -> {
                    var group = member.getGroup();
                    return new GroupWithMembershipDto(
                        group.getId(),
                        group.getName(),
                        group.getPfpUrl(),
                        group.getOwner().getId(),
                        group.getCreatedAt(),
                        group.getInviteCode(),
                        group.getDescription(),
                        member.getRole(),
                        member.getJoinedAt()
                    );
                })
                .sorted((g1, g2) -> {
                    if (finalSortBy.equals("createdAt")) {
                        return g2.getCreatedAt().compareTo(g1.getCreatedAt());
                    }
                    return g1.getName().compareTo(g2.getName());
                })
                .toList();
    }
}
