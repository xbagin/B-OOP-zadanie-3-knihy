package sk.stuba.fei.uim.oop.assignment3.book.logic;

import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookRequest;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookEditRequest;

import java.util.List;

public interface IBookService {
    List<Book> getAll();
    Book create(BookRequest request) throws NotFoundException, IllegalOperationException;
    Book getById(Long id) throws NotFoundException;
    Book update(Long id, BookEditRequest request) throws NotFoundException;
    void delete(Long id) throws NotFoundException, IllegalOperationException;
    int getAmount(Long id) throws  NotFoundException;
    int addAmount(Long id, Integer increment) throws NotFoundException, IllegalOperationException;
    int getBookLendCount(Long id) throws NotFoundException;
}
