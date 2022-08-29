package com.example.intern_vnpttech_libmanagement.services.statistics;

import com.example.intern_vnpttech_libmanagement.dto.info.CardStatistics;
import com.example.intern_vnpttech_libmanagement.dto.info.CardStatisticsDetails;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderBookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.ReaderCardTypeRepo;
import com.example.intern_vnpttech_libmanagement.services.ReaderCardRepo;
import com.example.intern_vnpttech_libmanagement.utilities.DateAndTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticService {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private ReaderBookRepo readerBookRepo;

    @Autowired
    private ReaderCardRepo readerCardRepo;

    @Autowired
    private ReaderCardTypeRepo cardTypeRepo;


    public CardStatistics cardStatistic(int month, int year)
    {
        List<ReaderCard> readerCardList = readerCardRepo.findAll();
        CardStatistics cardStatistics = new CardStatistics();
        long numberPublishedCardInMonth = readerCardList
                .stream().filter(readerCard -> DateAndTimeUtils.inMonthCheck(readerCard.getPublishedAt(),month,year)).count();
        Map<String, CardStatisticsDetails> detailNumbers= cardStatistics.getDetailNumbers();
        cardStatistics.setMonth(month);
        cardStatistics.setYear(year);
        List<ReaderCardType> cardTypeList = cardTypeRepo.getAllCardType();
        cardTypeList.stream().forEach(readerCardType -> {
            long numberPublishCard = readerCardList.stream().filter(readerCard -> {
                return DateAndTimeUtils.inMonthCheck(readerCard.getPublishedAt(),month,year) && readerCard.getCardId()==readerCardType.getCardTypeId();
            }).count();
            detailNumbers.put(readerCardType.getCardTypeName(),new CardStatisticsDetails(numberPublishCard,numberPublishCard/numberPublishedCardInMonth));
        });
        return cardStatistics;
    }





}
