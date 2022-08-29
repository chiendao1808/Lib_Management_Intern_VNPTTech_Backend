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

    @Query(value = "from ReaderBook  rb where rb.deleted =false")
    List<ReaderBook> findAll();

    @Query(value = "from ReaderBook  rb where rb.id =?1 and rb.deleted=false ")
    Optional<ReaderBook> findById(long id);

    @Query(value = "from ReaderBook rb where rb.reader.readerId =?1 and rb.deleted = false " +
            "order by rb.reader.readerId asc")
    List<ReaderBook> findByReader(long readerId);

    @Query(value = "from ReaderBook rb where rb.book.bookId =?1 and rb.deleted =false ")
    List<ReaderBook> findByBook(long bookId);

    @Query(value = "from ReaderBook rb where rb.book.bookId=?1 and rb.reader.readerId=?2 " +
            " and rb.returnedAt is null and rb.deleted=false")
    Optional<ReaderBook> findByNotReturnBook(long bookId,long readerId);

    @Query(value = "from ReaderBook rb where rb.reader.readerId =?1 and " +
            "rb.book.bookId=?2 and rb.deleted =false")
    Optional<ReaderBook> findByReaderAndBook(long readerId, long bookId);

    @Query(value = "select count(rb) from ReaderBook rb where rb.reader.readerId=?1 and rb.returnedAt is null " +
            "and rb.deleted =false ")
    int countBorrowingBook(long readerId);

    @Modifying
    @Transactional
    @Query(value = "update ReaderBook rb set rb.deleted= true where rb.id =?1 and rb.deleted =false ")
    int delete(long id);

    @Modifying
    @Transactional
    @Query(value = "update ReaderBook rb set rb.deleted = true where rb.book.bookId =?1 and rb.deleted =false")
    int deleteByBook(long bookId);
}
