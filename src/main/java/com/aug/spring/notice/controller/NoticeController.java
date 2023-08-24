package com.aug.spring.notice.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.aug.spring.notice.domain.Notice;
import com.aug.spring.notice.domain.PageInfo;
import com.aug.spring.notice.service.NoticeService;

@Controller
public class NoticeController {
	
	@Autowired
	private NoticeService service;
	
	@RequestMapping(value="/notice/insert.kh", method=RequestMethod.GET)
	public String showInsertForm() {
		return "notice/insert";
	}
	
	@RequestMapping(value="/notice/insert.kh", method=RequestMethod.POST)
	public String insertNotice(
			@ModelAttribute Notice notice,
			@RequestParam(value="uploadFile", required=false) MultipartFile uploadFile,
			HttpServletRequest request,
			Model model) {
		try {
			if(uploadFile != null && !uploadFile.getOriginalFilename().equals("")) {
				Map<String, Object> nMap = this.saveFile(uploadFile, request);
				String fileName = (String)nMap.get("fileName");
				String fileRename = (String)nMap.get("fileRename");
				String savePath = (String)nMap.get("filePath");
				long fileLength = (long)nMap.get("fileLength");
				
				// DB에 저장하기 위해 notice에 데이터를 Set하는 부분
				notice.setNoticeFileName(fileName);
				notice.setNoticeFileRename(fileRename);
				notice.setNoticeFilePath(savePath);
				notice.setNoticeFileLength(fileLength);
			}
			int result = service.insertNotice(notice);
			if(result > 0) {
				return "redirect:/notice/list.kh";
			} else {
				model.addAttribute("msg","공지사항 등록이 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 등록 실패");
				model.addAttribute("url", "/notice/insert.kh");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg","관리자에게 문의해주세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/notice/insert.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/notice/list.kh", method=RequestMethod.GET)
	public String showNoticeList(
			@RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
			Model model) {
		try {
//			int currentPage = page != null ? page : 1;
			int totalCount = service.getListCount();
			PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
			List<Notice> nList = service.selectNoticeList(pInfo);
			// List 데이터의 유효성 체크 방법 2가지
			// 1. isEmpty()
			// 2. size()
			if(nList.size() > 0) {
				model.addAttribute("pInfo", pInfo);
				model.addAttribute("nList", nList);
				return "notice/list";
			} else {
				model.addAttribute("msg","데이터 조회가 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 목록 조회 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg","관리자에게 문의해주세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	public PageInfo getPageInfo(int currentPage, int totalCount) {
		PageInfo pi = null;
		int recordCountPerPage = 10;
		int naviCountPerPage = 10;
		int naviTotalCount;
		int startNavi;
		int endNavi;
		
		naviTotalCount = (int)((double)totalCount/recordCountPerPage + 0.9);
		startNavi = (((int)((double)currentPage/naviCountPerPage+0.9))-1)*naviCountPerPage+1;
		endNavi = startNavi + naviCountPerPage -1;
		if(endNavi > naviTotalCount) {
			endNavi = naviTotalCount;
		}
		pi = new PageInfo(currentPage, recordCountPerPage, naviCountPerPage, startNavi, endNavi, totalCount, naviTotalCount);
		return pi;
	}
	
	@RequestMapping(value="/notice/search.kh", method=RequestMethod.GET)
	public String searchNoticeList(
			@RequestParam("searchCondition") String searchCondition,
			@RequestParam("searchKeyword") String searchKeyword,
			@RequestParam(value="page", required=false, defaultValue="1") Integer currentPage,
			Model model) {
		Map<String, String> paramMap = new HashMap<String, String>();
		// put() 메소드를 사용해서 key-value 설정을 함
		paramMap.put("searchCondition", searchCondition);
		paramMap.put("searchKeyword", searchKeyword);
		int totalCount = service.getListCount(paramMap);
		PageInfo pInfo = this.getPageInfo(currentPage, totalCount);
		List<Notice> searchList = service.searchNoticeByKeyword(pInfo, paramMap);
		
//		switch(searchCondition) {
//			case "all" :
//				searchList = service.searchNoticesByAll(searchKeyword);
//				break;
//			case "writer" : 
//				searchList = service.searchNoticesByWriter(searchKeyword);
//				break;
//			case "title" :
//				searchList = service.searchNoticesByTitle(searchKeyword);
//				break;
//			case "content" : 
//				searchList = service.searchNoticesByContent(searchKeyword);
//				break;
//		}
		if(!searchList.isEmpty()) {
			model.addAttribute("sList", searchList);
			model.addAttribute("pInfo", pInfo);
			model.addAttribute("paramMap", paramMap);
			return "notice/search";
		} else {
			model.addAttribute("msg","데이터 조회가 완료되지 않았습니다.");
			model.addAttribute("error", "공지사항 제목으로 조회 실패");
			model.addAttribute("url", "/notice/list.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/notice/detail.kh", method=RequestMethod.GET)
	public String showNoticeDetail(
			@RequestParam("noticeNo") Integer noticeNo,
			Model model) {
		Notice noticeOne = service.selectNoticeByNo(noticeNo);
		if(noticeOne != null) {
			model.addAttribute("notice", noticeOne);
			return "notice/detail";
		} else {
			model.addAttribute("msg","데이터 조회가 완료되지 않았습니다.");
			model.addAttribute("error", "공지사항 제목으로 조회 실패");
			model.addAttribute("url", "/notice/list.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/notice/modify.kh", method=RequestMethod.POST)
	public String updateNotice(
			@ModelAttribute Notice notice,
			@RequestParam(value="uploadFile", required=false) MultipartFile uploadFile,
			HttpServletRequest request,
			Model model) {
		try {
			if(uploadFile != null && !uploadFile.isEmpty()) {
				// 기존 업로드된 파일 존재 여부 체크
				String fileRename = notice.getNoticeFileRename();
				if(fileRename != null) {
					// 있으면 기존 파일 삭제
					this.deleteFile(request, fileRename);
				}
				// 없으면 새로 업로드 하려는 파일 저장
				Map<String, Object> infoMap = this.saveFile(uploadFile, request);
				notice.setNoticeFileName((String)infoMap.get("fileName"));
				notice.setNoticeFileRename((String)infoMap.get("fileRename"));
				notice.setNoticeFilePath((String)infoMap.get("filePath"));
				notice.setNoticeFileLength((long)infoMap.get("fileLength"));
			}
			int result = service.updateNotice(notice);
			if(result > 0) {
				return "redirect:/notice/detail.kh?noticeNo="+ notice.getNoticeNo();
			} else {
				model.addAttribute("msg","공지사항 수정이 완료되지 않았습니다.");
				model.addAttribute("error", "공지사항 수정 실패");
				model.addAttribute("url", "/notice/modify.kh?noticeNo="+notice.getNoticeNo());
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg","관리자에게 문의해주세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/notice/modify.kh?noticeNo="+notice.getNoticeNo());
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/notice/modify.kh", method=RequestMethod.GET)
	public String showModifyForm(
			@RequestParam("noticeNo") Integer noticeNo,
			Model model) {
		Notice noticeOne = service.selectNoticeByNo(noticeNo);
		if(noticeOne != null) {
			model.addAttribute("notice", noticeOne);
			return "notice/modify";
		} else {
			model.addAttribute("msg","데이터 조회가 완료되지 않았습니다.");
			model.addAttribute("error", "공지사항 수정페이지 불러오기 실패");
			model.addAttribute("url", "/notice/list.kh");
			return "common/errorPage";
		}
	}
	
	public Map<String, Object> saveFile(MultipartFile uploadFile, HttpServletRequest request) throws Exception {
		Map<String, Object> infoMap = new HashMap<String, Object>();
		// 파일 이름
		String fileName = uploadFile.getOriginalFilename();
		// (내가 저장한 후 그 경로를 DB에 저장하도록 준비하는 것)
		String root = request.getSession().getServletContext().getRealPath("resources");
		// 폴더가 없을 경우 자동 생성(업로드한 파일을 저장할 폴더)
		String saveFolder = root + "\\nuploadFiles";
		File folder = new File(saveFolder);
		if(!folder.exists()) {
			folder.mkdir();
		}
		// 파일 경로
//		Random rand = new Random();
//		String strResult = "N";
//		for(int i = 0; i < 7; i++) {
//			int result = rand.nextInt(20)+1;
//			strResult += result + "";
//		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strResult = sdf.format(new Date(System.currentTimeMillis()));
		
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);
		String fileRename = strResult + "." + ext;
		String savePath = saveFolder + "\\" + fileRename;
		File file = new File(savePath);
		// 파일 저장
		uploadFile.transferTo(file);
		// 파일 크기
		long fileLength = uploadFile.getSize();
		infoMap.put("fileName", fileName);
		infoMap.put("fileRename", fileRename);
		infoMap.put("filePath", savePath);
		infoMap.put("fileLength", fileLength);
		
		return infoMap;
	}

	private void deleteFile(HttpServletRequest request, String fileRename) {
		String root = request.getSession().getServletContext().getRealPath("resources");
		String delFilepath = root + "\\nuploadFiles\\" + fileRename;
		File file = new File(delFilepath);
		if(file.exists()) {
			file.delete();
		}
	}
}
