package me.keills.repo;

import me.keills.model.Game;
import me.keills.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepo extends JpaRepository<Game,Long> {
    Optional<Game> findByName(String name);
    List<Game> findByPublisher(Publisher publisher);
    List<Game> findByReleaseDateLessThanEqual(LocalDate date);
    List<Game> findByReleaseDateGreaterThanEqual(LocalDate date);
    List<Game> findByPriceLessThanEqual(Integer price);
    List<Game> findByPriceGreaterThanEqual(Integer price);
}
