package com.aug.spring.board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.aug.spring.board.domain.Reply;
import com.aug.spring.board.service.ReplyService;

@Controller
@RequestMapping("/reply")
public class ReplyController {
	@Autowired
	private ReplyService rService;
	
	@RequestMapping(value="/add.kh", method=RequestMethod.POST)
	public ModelAndView insertReply(ModelAndView mv,
			@ModelAttribute Reply reply,
			HttpSession session) {
		String url = "";
		try {
			String replyWriter = (String)session.getAttribute("memberId");
			if(replyWriter != null && !replyWriter.equals("")) {
				reply.setReplyWriter(replyWriter);
				int result = rService.insertReply(reply);
				url = "/board/detail.kh?boardNo="+reply.getRefBoardNo();
				if(result > 0) {
					mv.setViewName("redirect:"+url);
				} else {
					mv.addObject("msg", "댓글 등록이 완료되지 않았습니다.");
					mv.addObject("error", "댓글 등록 실패");
					mv.addObject("url", url);
					mv.setViewName("common/errorPage");
				}
			} else {
				mv.addObject("msg", "로그인이 되지 않았습니다.");
				mv.addObject("error", "로그인 정보확인 실패");
				mv.addObject("url", "/index.jsp");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의바랍니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", url);
			mv.setViewName("common/errorPage");
		}
		
		return mv;
	}
	
	@RequestMapping(value="/update.kh", method=RequestMethod.POST)
	public ModelAndView updateReply(ModelAndView mv,
			@ModelAttribute Reply reply,
			HttpSession session) {
		String url = "";
		try {
			String replyWriter = (String)session.getAttribute("memberId");
			if(replyWriter != null && !replyWriter.equals("")) {
				reply.setReplyWriter(replyWriter);
				url = "/board/detail.kh?boardNo="+reply.getRefBoardNo();
				int result = rService.updateReply(reply);
				if(result > 0) {
					mv.setViewName("redirect:"+url);
				} else {
					mv.addObject("msg", "댓글 수정이 완료되지 않았습니다.");
					mv.addObject("error", "댓글 수정 실패");
					mv.addObject("url", url);
					mv.setViewName("common/errorPage");
				}
			} else {
				mv.addObject("msg", "로그인이 되지 않았습니다.");
				mv.addObject("error", "로그인 정보확인 실패");
				mv.addObject("url", "/index.jsp");
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의바랍니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", url);
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
	
	@RequestMapping(value="/delete.kh", method=RequestMethod.GET)
	public ModelAndView deleteReply(ModelAndView mv,
			@ModelAttribute Reply reply,
			HttpSession session) {
		try {
			String memberId = (String)session.getAttribute("memberId");
			String replyWriter = reply.getReplyWriter();
			if(replyWriter != null && replyWriter.equals(memberId)) {
				int result = rService.deleteReply(reply);
				if(result > 0) {
					mv.setViewName("redirect:/board/detail.kh?boardNo="+ reply.getRefBoardNo());
				} else {
					mv.addObject("msg", "댓글 삭제가 완료되지 않았습니다.");
					mv.addObject("error", "댓글 삭제 실패");
					mv.addObject("url", "/board/detail.kh?boardNo="+ reply.getRefBoardNo());
					mv.setViewName("common/errorPage");
				}
			} else {
				mv.addObject("msg", "자신의 댓글만 삭제할 수 있습니다.");
				mv.addObject("error", "댓글 삭제 불가");
				mv.addObject("url", "/board/detail.kh?boardNo="+ reply.getRefBoardNo());
				mv.setViewName("common/errorPage");
			}
		} catch (Exception e) {
			mv.addObject("msg", "관리자에게 문의바랍니다.");
			mv.addObject("error", e.getMessage());
			mv.addObject("url", "/board/detail.kh?boardNo="+ reply.getRefBoardNo());
			mv.setViewName("common/errorPage");
		}
		return mv;
	}
}
