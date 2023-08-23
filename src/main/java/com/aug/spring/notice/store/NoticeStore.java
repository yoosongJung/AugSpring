package com.aug.spring.notice.store;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.aug.spring.notice.domain.Notice;
import com.aug.spring.notice.domain.PageInfo;

public interface NoticeStore {
	/**
	 * 공지사항 등록 Store
	 * @param session
	 * @param notice
	 * @return int
	 */
	int insertNotice(SqlSession session, Notice notice);

	/**
	 * 공지사항 목록 조회 Store
	 * @param session 
	 * @return List
	 */
	List<Notice> selectNoticeList(SqlSession session, PageInfo pInfo);

	/**
	 * 공지사항 전체 갯수 조회 Store
	 */
	int selectListCount(SqlSession session);

	/**
	 * 공지사항 제목으로 조회 Store
	 * @param session
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> selectNoticesByTitle(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 내용으로 조회 Store
	 * @param session
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticesByContent(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 작성자로 조회 Store
	 * @param session
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticesByWriter(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 전체로 조회 Store
	 * @param session
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticesByAll(SqlSession session, String searchKeyword);

	/**
	 * 공지사항 키워드로 조회 Store
	 * @param session
	 * @param searchCondition
	 * @param searchKeyword
	 * @return
	 */
	List<Notice> searchNoticeByKeyword(SqlSession session, PageInfo pInfo, Map<String, String> paramMap);

	/**
	 * 공지사항 검색 게시물 전체 갯수 Store
	 * @param session
	 * @param paramMap
	 * @return
	 */
	int selectListCount(SqlSession session, Map<String, String> paramMap);

	/**
	 * 공지사항 번호로 조회 Store
	 * @param session
	 * @param noticeNo
	 * @return
	 */
	Notice selectNoticeByNo(SqlSession session, Integer noticeNo);

	/**
	 * 공지사항 수정 Store
	 * @param session
	 * @param notice
	 * @return
	 */
	int updateNotice(SqlSession session, Notice notice);

}
