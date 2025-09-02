package com.pubcrawl.group;

import com.pubcrawl.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "group_members")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private MemberRole role;

    @Column(name = "highlight_color")
    private String highlightColor;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "group = " + (group != null ? group.getId() : null) + ", " +
                "user = " + (user != null ? user.getId() : null) + ", " +
                "role = " + role + ", " +
                "highlightColor = " + highlightColor + ", " +
                "joinedAt = " + joinedAt + ")";
    }
}
