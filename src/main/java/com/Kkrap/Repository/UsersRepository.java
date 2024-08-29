package com.Kkrap.Repository;

import com.Kkrap.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{
    @Query("SELECT kakaoId from Users Where kakaoId = :kakaoId")
    Optional<Users> findByKaKaoId(@Param("kakaoId") Long kakaoId);
}
