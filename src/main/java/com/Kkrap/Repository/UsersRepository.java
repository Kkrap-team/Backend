package com.Kkrap.Repository;

import com.Kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
    @Query("SELECT kakao_id from Users Where kakao_id = :kakao_id")
    Optional<Users> findByKaKaoId(@Param("kakao_id") Long kakaoId);
}
