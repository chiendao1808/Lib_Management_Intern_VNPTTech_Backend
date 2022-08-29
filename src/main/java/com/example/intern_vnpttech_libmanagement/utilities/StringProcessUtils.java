package com.example.intern_vnpttech_libmanagement.utilities;

import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

@UtilityClass
public class StringProcessUtils {

    //bookCode generator by type, name, author, published year
    public static String bookCodeGenerator(String bookType,String bookName, String author,int publicYear)
    {
        StringBuilder builder = new StringBuilder();
        String headBookCode = bookType.substring(0,4).toUpperCase();
        String mainCode = Arrays.stream((bookName +" "+ author).split(" ")).map(s -> s.substring(0,1).toUpperCase()).collect(Collectors.joining());
        builder.append(headBookCode).append(mainCode).append(publicYear);
        return builder.toString();
    }
}
