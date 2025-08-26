package com.kkrap.Repository;

import com.kkrap.Entity.Follows;
import com.kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowsRepository extends JpaRepository<Follows, Long> {
    boolean existsByFollowerAndFollowingId(Users follower, Long followingId);

    Optional<Follows> findByFollowerAndFollowingId(Users follower, Long followingId);

    List<Follows> findByFollower(Users follower);

    @Query("SELECT f.followingId FROM Follows f WHERE f.follower.userId = :followerId")
    List<Long> findFollowingIdsByFollowerId(@Param("followerId") Long followerId);

    Long countByFollowingId(Long followingId);

    boolean existsByFollower_UserIdAndFollowingId(Long followerId, Long followingId);
}