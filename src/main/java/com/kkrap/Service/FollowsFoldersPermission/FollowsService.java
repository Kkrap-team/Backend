package com.kkrap.Service.FollowsFoldersPermission;

import com.kkrap.Entity.Follows;
import com.kkrap.Entity.Users;
import com.kkrap.Exception.AlreadyFollowingException;
import com.kkrap.Exception.FollowNotFoundException;
import com.kkrap.Repository.FollowsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowsService {
    private  final FollowsRepository followsRepository;

    public FollowsService(FollowsRepository followsRepository){
        this.followsRepository = followsRepository;
    }

    public Follows save(Users follower, Users following){
        if (followsRepository.existsByFollowerAndFollowingId(follower, following.getUserId())) {
            throw new AlreadyFollowingException("이미 팔로우 중입니다.");
        }
        return followsRepository.save(Follows.of(follower, following));
    }

    public Follows delete(Users follower, Users following){
        Follows follows = followsRepository.findByFollowerAndFollowingId(follower, following.getUserId())
                .orElseThrow(() -> new FollowNotFoundException("팔로우 관계가 존재하지 않습니다."));
        followsRepository.delete(follows);
        return follows;
    }

    public List<Follows> findByFollower(Users follower){
        return followsRepository.findByFollower(follower);
    }

    public Long countFollower(Long userId) {
        return followsRepository.countByFollowingId(userId);
    }

    public List<Long> findFollowingIdsByFollowerId(Long followerId){
        return followsRepository.findFollowingIdsByFollowerId(followerId);
    }
}
