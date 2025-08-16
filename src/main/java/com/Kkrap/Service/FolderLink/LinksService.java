package com.Kkrap.Service.FolderLink;

import com.Kkrap.Entity.FoldersLinks;
import com.Kkrap.Entity.Links;
import com.Kkrap.Exception.LinksNotFoundException;
import com.Kkrap.Repository.LinksRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LinksService {

    private LinksRepository linksRepository;

    public LinksService(LinksRepository linksRepository){
        this.linksRepository = linksRepository;
    }

    //FOldersLinks에서 link_id 추출하며 Links 조회
    public List<Links> selectLinksByCreateTime(List<FoldersLinks> folderLinksList){
        return folderLinksList.stream()
                .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null)) // orElse(null) 제거
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Links::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    public List<Links> selectTop4LinksByCreateTime(List<FoldersLinks> folderLinksList){
        return folderLinksList.stream()
                .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null)) // orElse(null) 제거
                .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                .sorted(Comparator.comparing(Links::getCreateTime).reversed()) // 최신순 정렬
                .limit(4) // 상위 4개만 추출
                .collect(Collectors.toList());
    }

    public List<Links> selectTop1LinksByCreateTime(List<FoldersLinks> folderLinksList){
        return folderLinksList.stream()
                .map(folderLink -> linksRepository.findById(folderLink.getLinks().getLinkId()).orElse(null)) // orElse(null) 제거
                .filter(Objects::nonNull) // 존재하는 Links만 리스트에 추가
                .sorted(Comparator.comparing(Links::getCreateTime).reversed()) // 최신순 정렬
                .limit(1) // 상위 1개만 추출
                .collect(Collectors.toList());
    }

    //links가 있는지 검사
    public void validateAllExistByIds(List<Long> linkIds) {
        for (Long id : linkIds) {
            findById(id);
        }
    }

    //삭제 대상만 필터링
    public List<FoldersLinks> filterFoldersLinksByLinkIds(List<FoldersLinks> foldersLinksList, List<Long> linkIdList) {
        return foldersLinksList.stream()
                .filter(fl -> linkIdList.contains(fl.getLinks().getLinkId()))
                .collect(Collectors.toList());
    }


    //링크들 삭제
    public void deleteAllByIds(List<Long> linkIds) {
        for (Long linkId : linkIds) {
            deleteById(linkId);
        }
    }



    public Links save(Links links){
        return linksRepository.save(links);
    }
    public Links findById(Long linkId){
        return linksRepository.findById(linkId).orElseThrow(() -> LinksNotFoundException.of("링크를 찾을 수 없습니다"));
    }
    public void deleteById(Long linkId){
        linksRepository.deleteById(linkId);
    }


}
