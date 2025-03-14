package com.Kkrap.Repository;

import com.Kkrap.Entity.Folders;
import com.Kkrap.Entity.FoldersLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoldersLinksRepository extends JpaRepository<FoldersLinks, Long> {

    List<FoldersLinks> findByFolders(Folders folders);

}
