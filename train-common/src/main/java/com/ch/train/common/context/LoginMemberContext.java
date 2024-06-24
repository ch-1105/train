package com.ch.train.common.context;

import com.ch.train.common.response.MemberLoginResponce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程本地变量
 */
public class LoginMemberContext {
    private static final Logger LOG = LoggerFactory.getLogger(LoginMemberContext.class);

    private static ThreadLocal<MemberLoginResponce> member = new ThreadLocal<>();

    public static MemberLoginResponce getMember() {
        return member.get();
    }

    public static void setMember(MemberLoginResponce member) {
        LoginMemberContext.member.set(member);
    }

    public static String getId() {
        try {
            return member.get().getId();
        } catch (Exception e) {
            LOG.error("获取登录会员信息异常", e);
            throw e;
        }
    }

}
