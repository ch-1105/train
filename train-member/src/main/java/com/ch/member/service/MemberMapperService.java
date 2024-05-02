package com.ch.member.service;

import com.ch.member.mapper.MemberMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * author: ch
 * create: 2024--0218:18
 * Description:
 */
@Service
public class MemberMapperService {
    @Resource
    private MemberMapper memberMapper;
    public int getMember() {
        return memberMapper.getMember();
    }
}
