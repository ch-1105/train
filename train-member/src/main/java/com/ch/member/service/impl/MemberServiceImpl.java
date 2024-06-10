package com.ch.member.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ch.common.utils.GlobalException;
import com.ch.common.utils.JwtUtil;
import com.ch.member.domain.Member;
import com.ch.member.mapper.MemberMapper;
import com.ch.member.request.MemberLoginRequest;
import com.ch.member.request.MemberRequest;
import com.ch.member.responce.MemberLoginResponce;
import com.ch.member.service.MemberService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author ch051
* @description 针对表【member(member.`member`)】的数据库操作Service实现
* @createDate 2024-05-04 22:23:19
*/
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member>
    implements MemberService {
    private static final Logger log
            = LoggerFactory.getLogger(MemberServiceImpl.class);
    @Resource
    MemberMapper memberMapper;
    @Override
    public long registerMember(MemberRequest memberRequest) {
        String mobile = memberRequest.getMobile();
        Member memberDB = selectByMobile(mobile);

        if(ObjectUtil.isNull(memberDB)){
            Member member = new Member();
            member.setId(IdUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
            return member.getId();
        }
        throw new GlobalException(400,"手机号已存在");
    }

    public void sendCode(MemberRequest memberRequest) {
        String mobile = memberRequest.getMobile();
        Member memberDB = selectByMobile(mobile);

        if(ObjectUtil.isNull(memberDB)){//用户不存在则新建
            log.info("用户不存在，添加记录");
            Member member = new Member();
            member.setId(IdUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        }else {
            log.info("用户已存在");
        }
        //生成验证码
        //String verifCode = RandomUtil.randomString(4);

        //方便测试为8888
        String verifCode = "8888";

        log.info("generate verifCode : {} ", verifCode);

        //保存数据到记录表 ： 手机号 ，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间

        //对接短信平台发送验证码
    }

    /**
     * 登录
     * @date 2024/5/23 21:13
     * @param memberRequest
     */
    public MemberLoginResponce login(MemberLoginRequest memberRequest) {
        String mobile = memberRequest.getMobile();
        String code = memberRequest.getCode();
        Member memberDB = selectByMobile(mobile);
        if(ObjectUtil.isNull(memberDB)){//用户不存在则报错
            throw new GlobalException("用户不存在");
        }
        if("8888".equals(code)){
            log.info("验证码正确");
        }else {
            throw new GlobalException("验证码错误");
        }
        MemberLoginResponce loginResponce = BeanUtil.copyProperties(memberDB, MemberLoginResponce.class);
        String token = JwtUtil.createToken(Long.valueOf(loginResponce.getId()),loginResponce.getMobile());
        loginResponce.setToken(token);
        return loginResponce;
    }

    private Member selectByMobile(String mobile) {
        QueryWrapper<Member> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq("mobile", mobile);
        List<Member> members = memberMapper.selectList(objectQueryWrapper);
        if (!members.isEmpty()) {
            return members.get(0);
        }else{
            return null;
        }
    }
}




