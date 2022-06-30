package sk.stuba.fei.uim.oop.assignment3.list.logic;

import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.book.logic.IBookService;
import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingList;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingListRepository;

import java.util.List;

@Service
public class LendingListService implements ILendingListService {
    private final LendingListRepository repository;
    private final IBookService bookService;

    @Autowired
    public LendingListService(LendingListRepository repository, IBookService bookService) {
        this.repository = repository;
        this.bookService = bookService;
    }

    @Override
    public List<LendingList> getAll() {
        return this.repository.findAll();
    }

    @Override
    public LendingList create() {
        return this.repository.save(new LendingList());
    }

    @Override
    public LendingList getById(Long id) throws NotFoundException {
        LendingList list = this.repository.findLendingListById(id);
        if (list == null) {
            throw new NotFoundException();
        }
        return list;
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        LendingList list = this.getById(id);
        if (list.getLended()) {
            list.getBooks().forEach(this::returnBook);
        }
        list.getBooks().forEach(book -> book.getLendingLists().remove(list));
        list.getBooks().clear();
        this.repository.delete(list);
    }

    @Override
    public LendingList addBook(Long id, Long bookId) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getById(id);
        Book book = this.bookService.getById(bookId);
        if (list.getLended() || list.getBooks().contains(book)) {
            throw new IllegalOperationException();
        }
        list.getBooks().add(book);
        book.getLendingLists().add(list);
        return this.repository.save(list);
    }

    @Override
    public void removeBook(Long id, Long bookId) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getById(id);
        if (list.getLended()) {
            throw new IllegalOperationException();
        }
        Book book = this.bookService.getById(bookId);
        list.getBooks().remove(book);
        book.getLendingLists().remove(list);
        this.repository.save(list);
    }

    @Override
    public void lend(Long id) throws NotFoundException, IllegalOperationException {
        LendingList list = this.getById(id);
        if (list.getLended() || list.getBooks().isEmpty()) {
            throw new IllegalOperationException();
        }
        for (Book book : list.getBooks()) {
            if (book.getLendCount() >= book.getAmount()) {
                throw new IllegalOperationException();
            }
        }
        list.getBooks().forEach(this::lendBook);
        list.setLended(true);
        this.repository.save(list);
    }

    private void lendBook(Book book) {
        book.setLendCount(book.getLendCount() + 1);
    }

    private void returnBook(Book book) {
        book.setLendCount(book.getLendCount() - 1);
    }
}
