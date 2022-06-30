package sk.stuba.fei.uim.oop.assignment3.list.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LendingListRepository extends JpaRepository<LendingList, Long> {
    List<LendingList> findAll();
    LendingList findLendingListById(Long id);
}
