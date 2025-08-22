package com.kkrap.Repository;


import com.kkrap.Entity.Links;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinksRepository extends JpaRepository <Links, Long> {

}
