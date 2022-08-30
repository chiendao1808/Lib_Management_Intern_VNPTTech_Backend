package com.example.intern_vnpttech_libmanagement.repositories;

import com.example.intern_vnpttech_libmanagement.entities.ReaderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderBookRepo extends JpaRepository<ReaderBook,Long> {

    @Query(value = "select * from reader_book rb where rb.deleted =false",nativeQuery = true)
    List<ReaderBook> findAll();

    @Query(value = "select * from reader_book rb where rb.id = :id and rb.deleted =false",nativeQuery = true)
    Optional<ReaderBook> findById(long id);

    @Query(value = "select * from reader_book rb where rb.reader_id = :readerId and rb.deleted =false",nativeQuery = true)
    List<ReaderBook> findByReader(long readerId);

    @Query(value = "select * from reader_book rb where rb.book_id = :bookId and rb.deleted =false",nativeQuery = true)
    List<ReaderBook> findByBook(long bookId);

    @Query(value = "select * from reader_book rb where rb.reader_id = :readerId and rb.book_id =:bookId" +
            " and rd.deleted =false",nativeQuery = true)
    Optional<ReaderBook> findByNotReturnBook(long bookId,long readerId);

    @Query(value = "select * from reader_book rb where rb.book_id = :bookId and rb.reader_id =:readerId" +
            " and rb.deleted =false ",nativeQuery = true)
    Optional<ReaderBook> findByReaderAndBook(long readerId, long bookId);

    @Query(value = "select count(rb) from reader_book rb " +
            "inner join book b on b.book_id = rb.book_id " +
            "where b.available =false and rb.deleted = false ",nativeQuery = true)
    int countBorrowingBook(long readerId);

    @Modifying
    @Transactional
    @Query(value = "update reader_book rb set rb.deleted =true where rb.id =:id and rb.deleted =false",nativeQuery = true)
    int delete(long id);

    @Modifying
    @Transactional
    @Query(value = "update reader_book rb set rb.deleted = true where rb.book_id =:bookId and rb.deleted =false",nativeQuery = true)
    int deleteByBook(long bookId);
}
