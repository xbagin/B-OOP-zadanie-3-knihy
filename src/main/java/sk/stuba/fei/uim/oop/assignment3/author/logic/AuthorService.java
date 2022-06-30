package sk.stuba.fei.uim.oop.assignment3.author.logic;

import sk.stuba.fei.uim.oop.assignment3.book.data.Book;
import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.stuba.fei.uim.oop.assignment3.author.data.Author;
import sk.stuba.fei.uim.oop.assignment3.author.data.AuthorRepository;
import sk.stuba.fei.uim.oop.assignment3.author.web.bodies.AuthorRequest;
import sk.stuba.fei.uim.oop.assignment3.list.data.LendingList;

import java.util.List;

@Service
public class AuthorService implements IAuthorService {
    private final AuthorRepository repository;

    @Autowired
    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Author> getAll() {
        return this.repository.findAll();
    }

    @Override
    public Author create(AuthorRequest request) throws IllegalOperationException {
        if (request.getName() == null || request.getSurname() == null) {
            throw new IllegalOperationException();
        }
        return this.repository.save(new Author(request));
    }

    @Override
    public Author getById(Long id) throws NotFoundException {
        Author author = this.repository.findAuthorById(id);
        if (author == null) {
            throw new NotFoundException();
        }
        return author;
    }

    @Override
    public Author update(Long id, AuthorRequest request) throws NotFoundException {
        Author author = this.getById(id);
        if (request.getName() != null) {
            author.setName(request.getName());
        }
        if (request.getSurname() != null) {
            author.setSurname(request.getSurname());
        }
        return this.repository.save(author);
    }

    @Override
    public void delete(Long id) throws NotFoundException, IllegalOperationException {
        Author author = this.getById(id);
        for (Book book : author.getBooks()) {
            for (LendingList list : book.getLendingLists()) {
                if (list.getLended()) {
                    throw new IllegalOperationException();
                }
            }
        }
        this.repository.delete(author);
    }
}
