package com.ch.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ch.member.domain.Ticket;
import com.ch.member.request.TicketQueryRequest;
import com.ch.member.response.TicketQueryResponse;
import com.ch.train.common.request.MemberTicketRequest;
import com.ch.train.common.response.PageResponse;

public interface TicketService extends IService<Ticket>{

    void save(MemberTicketRequest request);

    PageResponse<TicketQueryResponse> queryList(TicketQueryRequest request);

    void delete(Long id);
}