package com.ch.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.member.domain.Ticket;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ch051
* @description 针对表【ticket(车票)】的数据库操作Mapper
* @createDate 2024-07-18 18:11:10
* @Entity com.ch.member.domain.Ticket
*/
@Mapper
public interface TicketMapper extends BaseMapper<Ticket> {

}




