package sk.stuba.fei.uim.oop.assignment3.book.logic;

import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.author.logic.IAuthorService;
import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.data.BookRepository;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookRequest;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookEditRequest;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingList;

import java.util.List;

@Service
public class BookService implements IBookService {
    private final BookRepository repository;
    private final IAuthorService authorService;

    @Autowired
    public BookService(BookRepository repository, IAuthorService authorService) {
        this.repository = repository;
        this.authorService = authorService;
    }

    @Override
    public List<Book> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Book create(BookRequest request) throws NotFoundException, IllegalOperationException {
        if (this.anyParameterMissing(request)) {
            throw new IllegalOperationException();
        }
        Author author = this.authorService.getById(request.getAuthor());
        Book book = new Book(request, author);
        author.getBooks().add(book);
        return this.repository.save(book);
    }

    @Override
    public Book getById(Long id) throws NotFoundException {
        Book book = this.repository.findBookById(id);
        if (book == null) {
            throw new NotFoundException();
        }
        return book;
    }

    @Override
    public Book update(Long id, BookEditRequest request) throws NotFoundException {
        Book book = this.getById(id);
        if (request.getName() != null) {
            book.setName(request.getName());
        }
        if (request.getDescription() != null) {
            book.setDescription(request.getDescription());
        }
        Integer pages = request.getPages();
        if (pages != null && pages > 0) {
            book.setPages(pages);
        }
        Long authorId = request.getAuthor();
        if (authorId != null && authorId != 0L) {
            Author author = this.authorService.getById(authorId);
            book.getAuthor().getBooks().remove(book);
            author.getBooks().add(book);
            book.setAuthor(author);
        }
        return this.repository.save(book);
    }

    @Override
    public void delete(Long id) throws NotFoundException, IllegalOperationException {
        Book book = this.getById(id);
        for (LendingList list : book.getLendingLists()) {
            if (list.getLended()) {
                throw new IllegalOperationException();
            }
        }
        book.getLendingLists().forEach(list -> list.getBooks().remove(book));
        book.getAuthor().getBooks().remove(book);
        this.repository.delete(book);
    }

    @Override
    public int getAmount(Long id) throws NotFoundException {
        return this.getById(id).getAmount();
    }

    @Override
    public int addAmount(Long id, Integer increment) throws NotFoundException, IllegalOperationException {
        if (increment == null) {
            throw new IllegalOperationException();
        }
        Book book = this.getById(id);
        book.setAmount(book.getAmount() + increment);
        return this.repository.save(book).getAmount();
    }

    @Override
    public int getBookLendCount(Long id) throws NotFoundException {
        return this.getById(id).getLendCount();
    }

    private boolean anyParameterMissing(BookRequest r) {
        return r.getName() == null || r.getDescription() == null || r.getAuthor() == null || r.getPages() == null || r.getAmount() == null || r.getLendCount() == null;
    }
}
