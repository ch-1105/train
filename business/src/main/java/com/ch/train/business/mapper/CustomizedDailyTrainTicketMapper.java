package com.ch.train.business.mapper;

import java.util.Date;

public interface CustomizedDailyTrainTicketMapper {
    void upCountBySell(
            Date date,
            String trainCode,
            String seatTypeCode,
            int minStartIndex,
            int maxStartIndex,
            int minEndIndex,
            int maxEndIndex
    );
}
