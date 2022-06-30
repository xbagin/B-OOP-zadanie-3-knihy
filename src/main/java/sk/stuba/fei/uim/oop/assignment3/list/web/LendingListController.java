package sk.stuba.fei.uim.oop.assignment3.list.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sk.stuba.fei.uim.oop.assignment3.book.web.bodies.BookIdRequest;
import sk.stuba.fei.uim.oop.assignment3.exceptions.IllegalOperationException;
import sk.stuba.fei.uim.oop.assignment3.exceptions.NotFoundException;
import sk.stuba.fei.uim.oop.assignment3.list.logic.ILendingListService;
import sk.stuba.fei.uim.oop.assignment3.list.web.bodies.LendingListResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/list")
public class LendingListController {
    private final ILendingListService service;

    @Autowired
    public LendingListController(ILendingListService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LendingListResponse> getAllLendingLists() {
        return this.service.getAll().stream().map(LendingListResponse::new).collect(Collectors.toList());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LendingListResponse> addLendingList() {
        return new ResponseEntity<>(new LendingListResponse(this.service.create()), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public LendingListResponse getLendingList(@PathVariable("id") Long listId) throws NotFoundException {
        return new LendingListResponse(this.service.getById(listId));
    }

    @DeleteMapping(value = "/{id}")
    public void deleteLendingList(@PathVariable("id") Long listId) throws NotFoundException {
        this.service.delete(listId);
    }

    @PostMapping(value = "/{id}/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LendingListResponse addBookToLendingList(@PathVariable("id") Long listId, @RequestBody BookIdRequest body) throws NotFoundException, IllegalOperationException {
        return new LendingListResponse(this.service.addBook(listId, body.getId()));
    }

    @DeleteMapping(value = "/{id}/remove", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void removeBookFromLendingList(@PathVariable("id") Long listId, @RequestBody BookIdRequest body) throws NotFoundException, IllegalOperationException {
        this.service.removeBook(listId, body.getId());
    }

    @GetMapping(value = "/{id}/lend")
    public void lendLendingList(@PathVariable("id") Long listId) throws NotFoundException, IllegalOperationException {
        this.service.lend(listId);
    }
}
