package com.vue_spring.demo.service;

import com.vue_spring.demo.DTO.MemberResponseDto;

public interface MemberService {

    public MemberResponseDto getMemberInfo(String email);

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    public MemberResponseDto getMyInfo() ;
}
