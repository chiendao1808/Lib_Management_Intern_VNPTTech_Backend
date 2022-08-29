package com.example.intern_vnpttech_libmanagement.entities;

import com.example.intern_vnpttech_libmanagement.constants.DatabaseConstants;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = DatabaseConstants.databaseName, name = "book")
// Book's records
public class Book implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Column(name = "book_code",nullable = false)
    private String bookCode;

    @ManyToOne
    @JoinColumn(name = "book_type_id")
    private BookType bookType;

    @Column(name = "book_name", nullable = false)
    @NotNull
    private String bookName;

    @Column(name = "book_author",nullable = false)
    @NotNull
    private String bookAuthor;

    @Column(name = "book_publish_year")
    private int bookPublishYear;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @Column(name = "book_image")
    private String bookImage;

    @Column(name = "book_state")
    private String bookState;

    @Column(name = "added_at")
    private Timestamp addedAt;

    @Column(name = "available",nullable = false)
    private boolean available;

    @Column(name = "deleted")
    private boolean deleted;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
