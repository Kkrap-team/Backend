package com.Kkrap.Repository;

import com.Kkrap.Entity.Follows;
import com.Kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {
    boolean existsByFollowerAndFollowingId(Users follower, Long followingId);

    Optional<Follows> findByFollowerAndFollowingId(Users follower, Long followingId);

    List<Follows> findByFollower(Users follower);

    Long countByFollowingId(Long followingId);
}