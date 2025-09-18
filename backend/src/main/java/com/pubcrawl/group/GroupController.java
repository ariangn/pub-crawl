package com.pubcrawl.group;

import com.pubcrawl.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;
    private final AuthService authService;

    @GetMapping
    public Iterable<GroupDto> getAllGroups(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return groupService.getAllGroups(sortBy);
    }

    @GetMapping("/my-groups")
    public Iterable<GroupDto> getMyGroups(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return List.of();
        }
        return groupService.getUserGroups(currentUser.getId(), sortBy);
    }

    @PostMapping
    public ResponseEntity<?> createGroup(
            @Valid @RequestBody CreateGroupRequest request,
            UriComponentsBuilder uriBuilder) {
        
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        var groupDto = groupService.createGroup(request, currentUser);
        var uri = uriBuilder.path("/groups/{id}").buildAndExpand(groupDto.getId()).toUri();
        return ResponseEntity.created(uri).body(groupDto);
    }

    @GetMapping("/{groupId}")
    public GroupDto getGroup(@PathVariable UUID groupId) {
        return groupService.getGroup(groupId);
    }

    @GetMapping("/by-invite/{inviteCode}")
    public GroupDto getGroupByInviteCode(@PathVariable String inviteCode) {
        return groupService.getGroupByInviteCode(inviteCode);
    }

    @GetMapping("/{groupId}/members")
    public List<GroupMemberDto> getGroupMembers(@PathVariable UUID groupId) {
        return groupService.getGroupMembers(groupId);
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<?> joinGroup(
            @PathVariable UUID groupId,
            @Valid @RequestBody JoinGroupRequest request,
            UriComponentsBuilder uriBuilder) {
        
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        var memberDto = groupService.joinGroup(groupId, request, currentUser);
        var uri = uriBuilder.path("/groups/{groupId}/members/{userId}")
                .buildAndExpand(groupId, memberDto.getUserId()).toUri();
        return ResponseEntity.created(uri).body(memberDto);
    }

    @PatchMapping("/{groupId}/members/{userId}")
    public GroupMemberDto updateMember(
            @PathVariable UUID groupId,
            @PathVariable UUID userId,
            @RequestBody UpdateMemberRequest request) {
        return groupService.updateMember(groupId, userId, request);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID groupId,
            @PathVariable UUID userId) {
        
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        groupService.removeMember(groupId, userId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{groupId}/members/me")
    public ResponseEntity<Void> leaveGroup(@PathVariable UUID groupId) {
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        groupService.removeMember(groupId, currentUser.getId(), currentUser);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<Void> handleGroupNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(GroupMemberNotFoundException.class)
    public ResponseEntity<Void> handleGroupMemberNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateGroupMemberException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGroupMember() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "User is already a member of this group.")
        );
    }

    @ExceptionHandler(UserNotGroupMemberException.class)
    public ResponseEntity<Map<String, String>> handleUserNotGroupMember() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "User is not a member of this group.")
        );
    }

    @ExceptionHandler(UnauthorizedMemberOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedMemberOperation() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Only group admins can remove members.")
        );
    }

    @ExceptionHandler(AdminSelfRemovalException.class)
    public ResponseEntity<Map<String, String>> handleAdminSelfRemoval() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Admin cannot remove themselves from the group.")
        );
    }

    @ExceptionHandler(InvalidInviteCodeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInviteCode() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Invalid invite code.")
        );
    }

    @ExceptionHandler(DuplicateGroupNameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGroupName() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Group name is already taken.")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
