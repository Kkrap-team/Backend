package com.Kkrap.Repository;


import com.Kkrap.Entity.Links;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinksRepository extends JpaRepository <Links, Long> {

    List<Links> findByUsers_UserId(Long userId);

}
