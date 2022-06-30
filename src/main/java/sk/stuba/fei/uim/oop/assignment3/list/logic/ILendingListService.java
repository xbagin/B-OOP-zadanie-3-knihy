package sk.stuba.fei.uim.oop.assignment3.list.logic;

import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingList;

import java.util.List;

public interface ILendingListService {
    List<LendingList> getAll();
    LendingList create();
    LendingList getById(Long id) throws NotFoundException;
    void delete(Long id) throws NotFoundException;
    LendingList addBook(Long id, Long bookId) throws NotFoundException, IllegalOperationException;
    void removeBook(Long id, Long bookId) throws NotFoundException, IllegalOperationException;
    void lend(Long id) throws NotFoundException, IllegalOperationException;
}
