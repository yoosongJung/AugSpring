package com.aug.spring.member.service;

import com.aug.spring.member.domain.Member;

public interface MemberService {

	/**
	 * 회원가입 Service
	 * @param member
	 * @return
	 */
	int insertMember(Member member);

	/**
	 * 회원 로그인 Service
	 * @param member
	 * @return
	 */
	Member checkMemberLogin(Member member);

	/**
	 * 회원정보 조회 Service
	 * @param memberId
	 * @return
	 */
	Member getMemberById(String memberId);

	/**
	 * 회원정보 수정 Service
	 * @param member
	 * @return
	 */
	int updateMember(Member member);

	/**
	 * 회원탈퇴 Service, Update로 함.
	 * @param memberId
	 * @return
	 */
	int deleteMember(String memberId);

}
