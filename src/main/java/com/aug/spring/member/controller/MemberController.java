package com.aug.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.aug.spring.member.domain.Member;
import com.aug.spring.member.service.MemberService;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService service;
	
	@RequestMapping(value="/member/register.kh", method=RequestMethod.GET)
	public String showRegisterForm() {
		return "member/register";
	}
	
	@RequestMapping(value="/member/register.kh", method=RequestMethod.POST)
	public String registerMember(
//			@RequestParam("memberId") String memberId,
			@ModelAttribute Member member,
			Model model) {
		try {
			int result = service.insertMember(member);
			if(result > 0) {
				return "redirect:/index.jsp";
			} else {
				model.addAttribute("msg","회원가입이 완료되지 않았습니다.");
				model.addAttribute("error", "회원가입 실패");
				model.addAttribute("url", "/member/register.kh");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg","관리자에게 문의해주세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/member/register.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/login.kh", method=RequestMethod.POST)
	public String memberLoginCheck(
			@ModelAttribute Member member,
			HttpSession session,
			Model model) {
		try {
			Twilio.init("AC29d1a7ab6d45d02389be44a0a8e1e10d", "c9759807f7ae09785aefcadfa3b50663");
			Message message = Message.creator(
			    new PhoneNumber("+821072887513"),
			    "MG4c413f681a8407bcd9be259ef8738286",
			    "자바에서 됩니까")
			.create();
			Member mOne = service.checkMemberLogin(member);
			if(mOne != null) {
				session.setAttribute("memberId", mOne.getMemberId());
				session.setAttribute("memberName", mOne.getMemberName());
				return "redirect:/index.jsp";
			} else {
				model.addAttribute("msg","로그인이 완료되지 않았습니다.");
				model.addAttribute("error", "로그인 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg","회원가입이 완료되지 않았습니다.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/logout.kh", method=RequestMethod.GET)
	public String memberLogout(
			HttpSession session,
			Model model) {
		if(session != null) {
			session.invalidate();
			
			return "redirect:/index.jsp";
		} else {
			model.addAttribute("msg", "로그아웃을 완료하지 못했습니다.");
			model.addAttribute("error", "로그아웃 실패");
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/myPage.kh", method=RequestMethod.POST)
	public String showMyPage(
			//@RequestParam("memberId") String memberId,
			HttpSession session,
			Model model) {
		try {
			String memberId = session.getAttribute("memberId").toString();
			Member member = null;
			if(memberId != "" && memberId != null) {
				member = service.getMemberById(memberId);
			}
			if(member != null) {
				model.addAttribute("member", member);
				return "member/myPage";
			} else {
				model.addAttribute("msg", "회원정보를 가져오지 못했습니다.");
				model.addAttribute("error", "회원정보 조회 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의하세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/update.kh", method=RequestMethod.GET)
	public String showModifyForm(
			@RequestParam("memberId") String memberId, // String memberId 만 써도 가능.
			Model model) {
		try {
			Member mOne = service.getMemberById(memberId);
			if(mOne != null) {
				model.addAttribute("member", mOne);
				return "member/modify";
			} else {
				model.addAttribute("msg", "회원정보 수정페이지 로드 실패");
				model.addAttribute("error", "회원정보 수정페이지 로드 실패");
				model.addAttribute("url", "/member/myPage.kh");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의하세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/member/myPage.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/update.kh", method=RequestMethod.POST)
	public String modifyMember(
			@ModelAttribute Member member,
			Model model) {
		try {
			int result = service.updateMember(member);
			if(result > 0) {
				return "redirect:/index.jsp";
			} else {
				model.addAttribute("msg", "회원정보 수정이 완료되지 않았습니다.");
				model.addAttribute("error", "회원정보 수정 실패");
				model.addAttribute("url", "/member/myPage.kh?memberId"+ member.getMemberId());
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의하세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/member/myPage.kh");
			return "common/errorPage";
		}
	}
	
	@RequestMapping(value="/member/delete.kh", method=RequestMethod.GET)
	public String outServiceMember(
			@RequestParam("memberId") String memberId,
			Model model) {
		try {
			int result = service.deleteMember(memberId);
			if(result > 0) {
				return "redirect:/member/logout.kh";
			} else {
				model.addAttribute("msg", "회원탈퇴가 완료되지 않았습니다.");
				model.addAttribute("error", "회원탈퇴 실패");
				model.addAttribute("url", "/index.jsp");
				return "common/errorPage";
			}
		} catch (Exception e) {
			model.addAttribute("msg", "관리자에게 문의하세요.");
			model.addAttribute("error", e.getMessage());
			model.addAttribute("url", "/index.jsp");
			return "common/errorPage";
		}
	}
}
