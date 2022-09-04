package com.example.intern_vnpttech_libmanagement.controllers;

import com.example.intern_vnpttech_libmanagement.services.statistics.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "libmng/api/statistic")
@Tag(name = "Statistic Controller")
public class StatisticController {

    @Autowired
    private StatisticService statisticService;

    @Operation(summary = "Get statistics of reader's cards published in a month")
    @Parameter(name = "option",description = "option =1 -> monthly statistics , option =2 -> yearly statistics")
    @GetMapping(path = "/card/stats/monthly")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> cardMonthlyStatistic( @RequestParam(name = "month") int month,
                                                   @RequestParam(name = "year") int year,
                                                   @RequestParam(name = "option") int option,
                                                   HttpServletRequest request)
    {
        return ResponseEntity.ok(statisticService.getCardStatistic(month,year,option));
    }

    @Operation(summary = "Get statistics of book's type borrowed in a month")
    @Parameter(name = "option",description = "option =1 -> monthly statistics , option =2 -> yearly statistics")
    @GetMapping(path = "/book-type/stats/monthly")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> bookTypeMonthlyStatistic(@RequestParam(name = "month") int month,
                                                      @RequestParam(name = "year") int year,
                                                      @RequestParam(name = "option") int option,
                                                      HttpServletRequest request)
    {
        return ResponseEntity.ok(statisticService.getBookTypeStatistic(month,year,option));
    }

    @Operation(summary = "Get statistics of reader's cards published in a year")
    @Parameter(name = "option",description = "option =1 -> monthly statistics , option =2 -> yearly statistics")
    @GetMapping(path = "/card/stats/yearly")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> cardYearlyStatistic(@RequestParam(name = "year") Integer year,
                                                 @RequestParam(name = "option") int option, // option =1 -> per month, option ==2 -> all a year
                                                  HttpServletRequest request)
    {
      if(option==2)
          return ResponseEntity.ok(statisticService.getCardStatistic(0,year,option));
      else {
          Map<String, Object> resDTO = new LinkedHashMap<>();
          resDTO.put("Title", "Card's  Yearly Statistics");
          for (int m = 1; m <= 12; m++) {
              resDTO.put(m + "/" + year, statisticService.getCardStatistic(m, year, 1));
          }
          return ResponseEntity.ok(resDTO);
      }
    }

    @Operation(summary = "Get statistics of book's type borrowed in a year")
    @Parameter(name = "option",description = "option =1 -> monthly statistics , option =2 -> yearly statistics")
    @GetMapping(path = "/book-type/stats/yearly")
    @SecurityRequirement(name = "methodAuth")
    public ResponseEntity<?> bookTypeYearlyStatistic(@RequestParam(name = "year") Integer year,
                                                     @RequestParam(name = "option") int option, // option =1 -> per month, option ==2 -> all a year
                                                      HttpServletRequest request)
    {
        if(option ==2)
            return ResponseEntity.ok(statisticService.getBookTypeStatistic(0,year,option));
        else {
            Map<String, Object> resDTO = new LinkedHashMap<>();
            resDTO.put("Title", "BookType's Yearly Statistics");
            for (int m = 1; m <= 12; m++) {
                resDTO.put(m + "/" + year, statisticService.getBookTypeStatistic(m, year, 1));
            }
            return ResponseEntity.ok(resDTO);
        }
    }
}
