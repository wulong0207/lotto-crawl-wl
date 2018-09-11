package com.hhly.crawlcore.persistence.ticket.dao;

import com.hhly.crawlcore.persistence.ticket.po.TicketChannelPO;

public interface TicketChannelDaoMapper {
	
    TicketChannelPO findByChannelId(String channelId);
}