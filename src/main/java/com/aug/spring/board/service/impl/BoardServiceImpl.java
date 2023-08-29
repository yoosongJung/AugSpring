package com.aug.spring.board.service.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aug.spring.board.domain.Board;
import com.aug.spring.board.domain.PageInfo;
import com.aug.spring.board.service.BoardService;
import com.aug.spring.board.store.BoardStore;

@Service
public class BoardServiceImpl implements BoardService{

	@Autowired
	private BoardStore bStore;
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public int insertBoard(Board board) {
		int result = bStore.insertBoard(sqlSession, board);
		return result;
	}

	@Override
	public int getListCount() {
		int result = bStore.selectListCount(sqlSession);
		return result;
	}

	@Override
	public List<Board> selectBoardList(PageInfo pInfo) {
		List<Board> bList = bStore.selectBoardList(sqlSession, pInfo);
		return bList;
	}

	@Override
	public Board selectBoardByNo(Integer boardNo) {
		Board boardOne = bStore.selectBoardByNo(sqlSession, boardNo);
		return boardOne;
	}

	@Override
	public int deleteBoard(Board board) {
		int result = bStore.deleteBoard(sqlSession, board);
		return result;
	}

	@Override
	public int updateBoard(Board board) {
		int result = bStore.updateBoard(sqlSession, board);
		return result;
	}

}
