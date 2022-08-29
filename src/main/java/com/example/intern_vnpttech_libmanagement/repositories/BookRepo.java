package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book,Long> {

    @Query(value = "from Book book where  book.deleted =false ")
    Page<Book> findAll(Pageable pageable);

    @Query(value = "from Book book where book.bookId =?1 and book.deleted =false")
    Optional<Book> findByBookId(long bookId);

    @Query(value = "from Book book where book.bookName like concat('%',?1,'%') and book.deleted=false ")
    Page<Book> findByBookName(String bookName, Pageable pageable);

    @Query("from Book book where book.bookName =?1 and book.deleted =false")
    Page<Book> findByExactBookName(String bookName, Pageable pageable);

    @Query(value = "from Book book where book.bookAuthor like concat('%',?1,'%') and book.deleted =false")
    Page<Book> findByBookAuthor(String bookAuthor,Pageable pageable);

    @Query(value = "from Book book where book.bookCode =?1 and book.deleted =false ")
    Page<Book> findByBookCode(String bookCode,Pageable pageable);

    @Query(value = "from Book book where book.bookCode =?1 and book.deleted =false ")
    List<Book> findByBookCode(String bookCode);

    @Query(value = "select distinct book.bookType from Book book where book.deleted =false order by  book.bookType asc")
    List<String> getAllBookCategory();

    @Query(value = "select count (book) from Book book where book.deleted =false")
    int countAllBook();

    @Query(value = "select count(book) from Book book where book.bookCode =?1 and book.deleted=false")
    int countABook(String bookCode);

    @Query(value = "select count(book) from Book book where book.bookCode =?1 and book.available =true and book.deleted=false")
    int countAbookAvailableAmount(String bookCode);

    @Query(value = "select distinct book.bookCode from Book book where book.deleted =false ")
    List<String> getAllBookCode();

    @Modifying
    @Transactional
    @Query(value = "update Book book set book.available = ?1 where book.bookId =?2 and book.deleted =false")
    int setAvailable(boolean available,long bookId);

    @Query(value = "from Book book where book.bookCode =?1 and book.available =true and book.deleted =false " +
            "order by book.bookId asc")
    List<Book> getAvailableBooks(String bookCode);

    @Modifying
    @Transactional
    @Query(value = "update Book book set book.deleted =true where book.bookId =?1 and book.deleted =false ")
    int deleteById(long bookId);

    @Modifying
    @Transactional
    @Query("update Book book set book.deleted =true where book.bookCode =?1 and book.deleted =false ")
    int deleteByBookCode(String bookCode);

}
