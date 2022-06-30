package sk.stuba.fei.uim.oop.assignment3.list.data;

import lombok.Getter;
import lombok.Setter;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class LendingList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany
    private List<Book> books;
    private Boolean lended;

    public LendingList() {
        this.books = new ArrayList<>();
        this.lended = false;
    }
}
