package ru.job4j.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;

    private String status; // PENDING, ACCEPTED, REJECTED

    private Instant createdAt;
    private Instant updatedAt;
}