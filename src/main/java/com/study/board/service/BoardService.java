package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;
    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception{
        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        //파일을 저장할 경로를 지정
        UUID uuid = UUID.randomUUID();
        //파일 이름에 넣을 녀석을 uuid라는 녀석으로 랜덤으로 써줌
        String fileName = uuid + "_" + file.getOriginalFilename();
        //파일 이름 형식을 "uuid_~~~"로 지정
        File saveFile = new File(projectPath, fileName);
        //File이란 클래스를 이용해 껍데기를 생성
        file.transferTo(saveFile);
        board.setFilename(fileName);
        board.setFilepath("/files/" + fileName);
        boardRepository.save(board);
    }

    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable){ //Board라는 클래스가 담긴 List를 반환
        return boardRepository.findAll(pageable);
    }

    // 검색기능 추가
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable){
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board boardView(Integer id){
        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id){
        boardRepository.deleteById(id);
    }
}