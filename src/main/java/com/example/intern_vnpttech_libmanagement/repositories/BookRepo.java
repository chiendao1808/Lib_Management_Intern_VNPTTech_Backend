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

    @Query(value = "select * from book b where b.deleted=false" +
            "order by b.book_id asc",nativeQuery = true)
    Page<Book> findAll(Pageable pageable);

    @Query(value = "select * from book b where b.book_id = :bookId and b.deleted =false",nativeQuery = true)
    Optional<Book> findByBookId(long bookId);

    @Query(value = "select * from book b where lower(b.book_name) like lower('%:bookName%') and b.deleted =false",nativeQuery = true)
    Page<Book> findByBookName(String bookName, Pageable pageable);

    @Query(value = "select * from book b where low(b.book_name) = lower(:bookName) and b.deleted =false",nativeQuery = true)
    Page<Book> findByExactBookName(String bookName, Pageable pageable);

    @Query(value = "select * from book b where lower(b.book_author) like lower('%:bookAuthor%') and b.deleted =false",nativeQuery = true)
    Page<Book> findByBookAuthor(String bookAuthor,Pageable pageable);

    @Query(value = "select * from book b where b.book_code = :bookCode and b.deleted =false",nativeQuery = true)
    Page<Book> findByBookCode(String bookCode,Pageable pageable);

    @Query(value = "select * from book b where b.book_code = :bookCode and b.deleted =false order by b.book_id asc",nativeQuery = true)
    List<Book> findByBookCode(String bookCode);

    @Query(value = "select distinct bt.book_type_name from book_type bt where bt.deleted =false",nativeQuery = true)
    List<String> getAllBookType();

    @Query(value = "select count(*) from book b where b.deleted =false", nativeQuery = true)
    int countAllBook();

    @Query(value = "select count(b) from book b where b.book_code = :bookCode and b.deleted =false",nativeQuery = true)
    int countABook(String bookCode);

    @Query(value = "select count(b) from book b where b.book_code =:bookCode and b.available =true and" +
            " b.deleted  = false",nativeQuery = true)
    int countAbookAvailableAmount(String bookCode);

    @Query(value = "select distinct book.bookCode from Book book where book.deleted =false ")
    List<String> getAllBookCode();

    @Modifying
    @Transactional
    @Query(value = "update book b " +
            "set b.available = :available where b.book_id = :bookId and b.deleted =false",nativeQuery = true)
    int setAvailable(boolean available,long bookId);

    @Query(value = "select * from book b where b.book_code = : bookCode and b.available =true " +
            "b.deleted =false order by b.book_id",nativeQuery = true)
    List<Book> getAvailableBooks(String bookCode);

    @Modifying
    @Transactional
    @Query(value = "update book b set b.deleted = true where b.book_id = :bookId and b.deleted =false",nativeQuery = true)
    int deleteById(long bookId);

    @Modifying
    @Transactional
    @Query(value = "update book b set b.deleted = true where b.book_code = :bookCode and b.deleted =false",nativeQuery = true)
    int deleteByBookCode(String bookCode);

}
