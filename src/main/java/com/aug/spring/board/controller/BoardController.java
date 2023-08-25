package com.aug.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.aug.spring.board.domain.Board;
import com.aug.spring.board.domain.PageInfo;
import com.aug.spring.board.domain.Reply;
import com.aug.spring.board.service.BoardService;
import com.aug.spring.board.service.ReplyService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;
	@Autowired
	private ReplyService rService;
	
	@RequestMapping(value="/board/detail.kh", method=RequestMethod.GET)
	public ModelAndView showBoardDetail(ModelAndView mv,
			@RequestParam("boardNo") Integer boardNo) {
		try {
			Board boardOne = bService.selectBoardByNo(boardNo);
			if(boardOne != null) {
				List<Reply> replyList = rService.selectReplyList(boardNo); 
				if(replyList.size() > 0) {
					mv.addObject("rList", replyList);
				}
				mv.addObject("board", boardOne);
				mv.setViewName("board/detail");
			} else {
				mv.addObject("msg", "게시글 조회가 완료되지 않았습니다.");
				mv.addObject("error", "게시글 상세 조회 실패");
				mv.addObject("url", "/board/list.kh");
				mv.setViewName("common/errorPage");
			}
			
		} catch (Exception e) {
			mv.addObject("msg", "게시글 조회가 완료되지 않았습니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/list.kh");
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	@RequestMapping(value="/board/list.kh", method=RequestMethod.GET)
	public ModelAndView showBoardList(
			@RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
			ModelAndView mv) {
		try {
			Integer totalCount = bService.getListCount();
			PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
			List<Board> bList = bService.selectBoardList(pInfo);
			if(!bList.isEmpty()) {
				mv.addObject("bList", bList).addObject("pInfo", pInfo).setViewName("board/list");
			} else {
				mv.addObject("msg", "게시글 조회가 완료되지 않았습니다.");
				mv.addObject("error", "게시글 전체 조회 실패");
				mv.addObject("url", "/index.jsp");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "게시글 조회가 완료되지 않았습니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/index.jsp");
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	public PageInfo getPageInfo(Integer currentPage, Integer totalCount) {
		
		int recordCountPerPage = 10;
		int naviCountPerPage = 10;
		int naviTotalCount;
		
		naviTotalCount = (int)Math.ceil((double)totalCount/recordCountPerPage);
		int startNavi = ((int)((double)currentPage/naviCountPerPage+0.9)-1)*naviCountPerPage+1;
		int endNavi = startNavi + naviCountPerPage - 1;
		if(endNavi > naviTotalCount) {
			endNavi = naviTotalCount;
		}
		PageInfo pInfo = new PageInfo(currentPage, totalCount, naviTotalCount, recordCountPerPage, naviCountPerPage, startNavi, endNavi);;
		
		return pInfo;
	}
	
	@RequestMapping(value="/board/write.kh", method=RequestMethod.GET)
	public ModelAndView showWriteForm(ModelAndView mv) {
		mv.setViewName("board/write");
		return mv;
	}
	
	@RequestMapping(value="/board/write.kh", method=RequestMethod.POST)
	public ModelAndView boardRegister(
			ModelAndView mv,
			@ModelAttribute Board board,
			@RequestParam(value="uploadFile", required=false) MultipartFile uploadFile,
			HttpServletRequest request) {
		try {
			if(uploadFile != null && !uploadFile.getOriginalFilename().equals("")) {
				// 파일 정보 및 저장
				Map<String, Object> bMap = this.saveFile(request, uploadFile);
				board.setBoardFilename((String)bMap.get("fileName"));
				board.setBoardFileRename((String)bMap.get("fileRename"));
				board.setBoardFilepath((String)bMap.get("filePath"));
				board.setBoardFilelength((long)bMap.get("fileLength"));
			} else {
				board.setBoardFilename("");
				board.setBoardFileRename("");
				board.setBoardFilepath("");
			}
			int result = bService.insertBoard(board);
			mv.setViewName("redirect:/board/list.kh");
		} catch (Exception e) {
			mv.addObject("msg", "게시글 등록이 완료되지 않았습니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/write.kh");
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	public Map<String, Object> saveFile(HttpServletRequest request, MultipartFile uploadFile) throws Exception, IOException{
		Map<String, Object> fileMap = new HashMap<String, Object>();
		// resources 경로 구하기
		String root = request.getSession().getServletContext().getRealPath("resources");
		// 파일 저장 경로 구하기
		String savePath = root + "\\buploadFiles";
		// 파일 이름 구하기
		String fileName = uploadFile.getOriginalFilename();
		// 파일 확장자 구하기
		String extension = fileName.substring(fileName.lastIndexOf(".")+1);
		// 시간으로 파일 리네임하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileRename = sdf.format(new Date(System.currentTimeMillis())) + "." + extension;
		// 파일 저장 전 폴더 만들기
		File saveFolder = new File(savePath);
		if(!saveFolder.exists()) {
			saveFolder.mkdir();
		}
		// 파일 저장
		File saveFile = new File(savePath+"\\"+fileRename);
		uploadFile.transferTo(saveFile);
		// 파일 크기 구하기
		long fileLength = uploadFile.getSize();
		// 파일 정보 리턴
		fileMap.put("fileName", fileName);
		fileMap.put("fileRename", fileRename);
		fileMap.put("filePath", "../resources/buploadFiles/"+fileRename);
		fileMap.put("fileLength", fileLength);
		return fileMap;
	}
}
