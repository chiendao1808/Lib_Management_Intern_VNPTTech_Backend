package com.example.intern_vnpttech_libmanagement.serviceimpls;

import com.example.intern_vnpttech_libmanagement.dto.entity_dto.BookRecordDTO;
import com.example.intern_vnpttech_libmanagement.dto.info.ABook;
import com.example.intern_vnpttech_libmanagement.entities.Book;
import com.example.intern_vnpttech_libmanagement.repositories.BookRepo;
import com.example.intern_vnpttech_libmanagement.services.BookService;
import com.example.intern_vnpttech_libmanagement.utilities.StringProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepo bookRepo;


    @Override
    public Page<Book> findByCriteria(long bookId, String bookName, String bookAuthor, String bookCode, long publisherId,Pageable pageable) {
        try{
           // System.out.println(bookRepo.findByCriteria(bookId, bookName, bookAuthor, bookCode, publisherId,pageable).getContent().size());
            return bookRepo.findByCriteria(bookId, bookName, bookAuthor, bookCode, publisherId,pageable);
        } catch (Exception ex)
        {
            log.error("Get books error at "+this.getClass().getName()+".get()",ex);
            return Page.empty(pageable);
        }
    }

    // find all book's records
    @Override
    public Page<Book> findAll(Pageable pageable) {
        try{
            return bookRepo.findAll(pageable);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            log.error("Find All book error",ex);
            return Page.empty();
        }
    }

    // Find all books
    @Override
    public List<ABook> findAllBooks(){
       try{
           List<ABook> books = new ArrayList<>();
           List<String> listBookCode = bookRepo.getAllBookCode();
           books = listBookCode.stream().map(code -> {
               return new ABook(bookRepo.findByBookCode(code).get(0),bookRepo.countAbookAvailableAmount(code));
           }).collect(Collectors.toList());
           return books;
       }catch (Exception ex)
       {
           log.error("Get all books error", ex);
           return Collections.emptyList();
       }
    } // find all books

    @Override
    public Optional<Book> findByBookId(long bookId) {
        try{
            return bookRepo.findByBookId(bookId);
        } catch (Exception ex)
        {
            log.error("Find book by id error");
            return Optional.empty();
        }
    }

    @Override
    public Page<Book> findByBookName(String bookName, Pageable pageable) {
        try{
            return bookRepo.findByBookName(bookName,pageable);
        } catch (Exception ex)
        {
            log.error("Find by book name error",ex);
            return Page.empty();
        }
    }

    @Override
    public Page<Book> findByExactName(String bookName,Pageable pageable) {
        try{
            return bookRepo.findByExactBookName(bookName,pageable);
        } catch (Exception ex)
        {
            log.error("Find by book's exact name error",ex);
            return Page.empty();
        }
    }

    @Override
    public Page<Book> findByAuthor(String authorName, Pageable pageable) {
        try{
           // String authorNamed = authorName.toLowerCase();
            return bookRepo.findByBookAuthor(authorName,pageable);
        } catch (Exception ex)
        {
            log.error("Find by author error",ex);
            return Page.empty();
        }
    }

    @Override
    public List<Book> findByBookCode(String bookCode) {
        try{
            return bookRepo.findByBookCode(bookCode);
        } catch (Exception ex)
        {
            log.error("Find book by id error");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Book> add(Book book) {
        try{
            book.setAddedAt(new Timestamp(System.currentTimeMillis()));
            book.setBookAuthor(book.getBookAuthor().toUpperCase());
            book.setAvailable(true);
            book.setBookCode(StringProcessUtils.bookCodeGenerator(book.getBookType().getBookTypeName()
                    ,book.getBookName(),book.getBookAuthor(),book.getBookPublishYear()));
            return Optional.ofNullable(bookRepo.save(book));
        } catch (Exception ex)
        {
            log.error("Add book error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> updateAll(BookRecordDTO bookRecordDTO) {
        try{
                List<Book> listBooks = findByBookCode(bookRecordDTO.getBookCode());
                for (Book book : listBooks) {
                    bookRecordDTO.setBookId(book.getBookId());
                    Optional<Book> bookOptional = update(bookRecordDTO);
                    if (!bookOptional.isPresent())
                        return Optional.empty();
                }
                return Optional.ofNullable(listBooks.get(0));
        } catch (Exception ex)
        {
            log.error("Update the books error",ex);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> update(BookRecordDTO bookRecordDTO) {
        try{
            Book bookToUpdate = bookRepo.findByBookId(bookRecordDTO.getBookId()).get();
            bookToUpdate.setBookName(bookRecordDTO.getBookName() != null ? bookRecordDTO.getBookName() : bookToUpdate.getBookName());
            bookToUpdate.setBookAuthor(bookRecordDTO.getBookAuthor() != null ? bookRecordDTO.getBookAuthor() : bookToUpdate.getBookAuthor());
           // bookToUpdate.setBookType(bookRecordDTO.getBookType() != null ? bookRecordDTO.getBookType() : bookToUpdate.getBookType());
            bookToUpdate.setBookState(bookRecordDTO.getBookState() != null ? bookRecordDTO.getBookState() : bookToUpdate.getBookState());
            bookToUpdate.setBookImage(bookRecordDTO.getBookImageURL()!=null? bookRecordDTO.getBookImageURL(): bookToUpdate.getBookImage());
            bookToUpdate.setBookPublishYear(bookRecordDTO.getBookPublishYear() != null ? bookRecordDTO.getBookPublishYear() : bookToUpdate.getBookPublishYear());
            bookToUpdate.setAvailable(bookRecordDTO.getAvailable()!=null? bookRecordDTO.getAvailable():bookToUpdate.isAvailable());
            Optional<Book> bookOptional = Optional.ofNullable(bookRepo.save(bookToUpdate));
            if (!bookOptional.isPresent())
                return Optional.empty();
            return bookOptional;
        }catch (Exception ex)
        {
            log.error("Update a book error",ex);
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteById(long bookId) {
        try{
            return bookRepo.deleteById(bookId)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete Book by Id error",ex);
            return false;
        }
    }

    @Override
    public boolean deleteByBookCode(String bookCode) {
        try{
            return bookRepo.deleteByBookCode(bookCode)>0?true:false;
        } catch (Exception ex)
        {
            log.error("Delete Book by bookCode error",ex);
            return false;
        }
    }
}
