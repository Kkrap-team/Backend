package com.Kkrap.Repository;

import com.Kkrap.Entity.Folders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoldersRepository extends JpaRepository<Folders, Long> {

    List<Folders> findByUserUserId(Long userId);

}
