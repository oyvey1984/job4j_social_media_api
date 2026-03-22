package ru.job4j.api.service;

import org.springframework.transaction.annotation.Transactional;
import ru.job4j.api.model.FriendRequest;
import ru.job4j.api.model.Subscription;
import ru.job4j.api.model.User;
import ru.job4j.api.repository.FriendRequestRepository;
import ru.job4j.api.repository.SubscriptionRepository;
import ru.job4j.api.repository.UserRepository;

import java.time.Instant;

public class SubscribeService {

    private final SubscriptionRepository subscriptionRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    public SubscribeService(SubscriptionRepository subscriptionRepository,
                            FriendRequestRepository friendRequestRepository,
                            UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public void sendFriendRequest(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + fromUserId));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new RuntimeException("User not found: " + toUserId));

        if (fromUserId.equals(toUserId)) {
            throw new RuntimeException("You cannot send a friend request to yourself");
        }

        Subscription subscription = new Subscription();
        subscription.setFollower(fromUser);
        subscription.setFollowing(toUser);
        subscription.setCreatedAt(Instant.now());
        subscriptionRepository.save(subscription);

        FriendRequest request = new FriendRequest();
        request.setFromUser(fromUser);
        request.setToUser(toUser);
        request.setStatus("PENDING");
        request.setCreatedAt(Instant.now());
        request.setUpdatedAt(Instant.now());
        friendRequestRepository.save(request);
    }

    @Transactional
    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found: " + requestId));

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Friend request is not pending");
        }

        request.setStatus("ACCEPTED");
        request.setUpdatedAt(Instant.now());
        friendRequestRepository.save(request);

        Subscription subscription = new Subscription();
        subscription.setFollower(request.getToUser());
        subscription.setFollowing(request.getFromUser());
        subscription.setCreatedAt(Instant.now());
        subscriptionRepository.save(subscription);
    }

    @Transactional
    public void rejectFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found: " + requestId));

        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Friend request is not pending");
        }


        request.setStatus("REJECTED");
        request.setUpdatedAt(Instant.now());
        friendRequestRepository.save(request);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("User not found: " + followerId));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new RuntimeException("User not found: " + followingId));
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("User not found: " + friendId));

        subscriptionRepository.deleteByFollowerAndFollowing(user, friend);
        friendRequestRepository.deleteByFromUserAndToUser(user, friend);
        friendRequestRepository.deleteByFromUserAndToUser(friend, user);
    }
}