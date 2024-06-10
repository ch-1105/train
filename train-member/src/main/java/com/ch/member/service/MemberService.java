package com.ch.member.service;

import com.ch.member.domain.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.member.request.MemberLoginRequest;
import com.ch.member.request.MemberRequest;
import com.ch.member.responce.MemberLoginResponce;

/**
* @author ch051
* @description 针对表【member(member.`member`)】的数据库操作Service
* @createDate 2024-05-04 22:23:19
*/
public interface MemberService extends IService<Member> {
    long registerMember(MemberRequest member);

    void sendCode(MemberRequest member);

    public MemberLoginResponce login(MemberLoginRequest member);

}
