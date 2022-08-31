package com.example.intern_vnpttech_libmanagement.services.statistics;

import com.example.intern_vnpttech_libmanagement.dto.info.BookStatistics;
import com.example.intern_vnpttech_libmanagement.dto.info.BookStatisticsDetails;
import com.example.intern_vnpttech_libmanagement.dto.info.CardStatistics;
import com.example.intern_vnpttech_libmanagement.dto.info.CardStatisticsDetails;
import com.example.intern_vnpttech_libmanagement.entities.BookType;
import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCard;
import com.example.intern_vnpttech_libmanagement.entities.ReaderCardType;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.repositories.BookTypeRepo;
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

    @Autowired
    private BookTypeRepo bookTypeRepo;


    // option = 1 -> montly statistics , option = 2 -> yearly statistics

    public CardStatistics getCardStatistic(int month, int year,int option)
    {
        try{
            List<ReaderCard> readerCardList = readerCardRepo.findAll();
            CardStatistics cardStatistics = new CardStatistics();
            long numberPublishedCardInMonth = readerCardList
                    .stream().filter(readerCard -> DateAndTimeUtils.inTimeCheck(readerCard.getPublishedAt(),month,year,option)).count();
            Map<String, CardStatisticsDetails> detailNumbers= cardStatistics.getDetailNumbers();
            cardStatistics.setMonth(month);
            cardStatistics.setYear(year);
            List<ReaderCardType> cardTypeList = cardTypeRepo.getAllCardType();
            cardTypeList.stream().forEach(readerCardType -> {
                long numberPublishCard = readerCardList.stream()
                                                        .filter(readerCard -> DateAndTimeUtils.inTimeCheck(readerCard.getPublishedAt(),month,year,option)
                                                        && readerCard.getCardId()==readerCardType.getCardTypeId())
                                                        .count();
                detailNumbers.put(readerCardType.getCardTypeName(),new CardStatisticsDetails(numberPublishCard,(float) numberPublishCard/numberPublishedCardInMonth));
            });
            cardStatistics.setDetailNumbers(detailNumbers);
            cardStatistics.setTotalNumberPublishedCard(numberPublishedCardInMonth);
            return cardStatistics;
        } catch (Exception ex)
        {
            log.error("Card statistic error",ex);
            return null;
        }
    }

    // make a statistic of book's types based on the number of borrowed turns
    public BookStatistics getBookTypeStatistic(int month, int year,int option)
    {
        try{
          List<ReaderBook> readerBookList = readerBookRepo.findAll();
          BookStatistics bookStatistics = new BookStatistics();
          bookStatistics.setMonth(month);
          bookStatistics.setYear(year);
          long totalBorrowedTurns = readerBookList.stream().filter(readerBook -> {
              return DateAndTimeUtils.inTimeCheck(readerBook.getBorrowedAt(),month,year,option);
          }).count();
          Map<String, BookStatisticsDetails> details = bookStatistics.getDetails();
          List<BookType> bookTypeList = bookTypeRepo.getAllBookType();
          bookTypeList.stream().forEach(bookType -> {
              long numberBorrowedTurns = readerBookList
                                          .stream()
                                          .filter(readerBook -> DateAndTimeUtils.inTimeCheck(readerBook.getBorrowedAt(),month,year,option)
                                                  && readerBook.getBook().getBookType().getBookTypeId()==bookType.getBookTypeId())
                                          .count();
              details.put(bookType.getBookTypeName(),
                            new BookStatisticsDetails(numberBorrowedTurns,(float)numberBorrowedTurns/totalBorrowedTurns));
          });
          bookStatistics.setDetails(details);
          bookStatistics.setTotalBorrowedTurns(totalBorrowedTurns);
          return bookStatistics;
        } catch (Exception ex)
        {
            log.error("Book type statistic error",ex);
            return null;
        }
    }


}
