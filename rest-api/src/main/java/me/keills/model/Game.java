package me.keills.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name="games")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotNull(message = "Name can't be null")
    @Column(unique = true,nullable = false)
    private String name;

    @NonNull
    @NotNull(message = "Release date can't be null")
    @Column(nullable = false)
    private LocalDate releaseDate;

    @NonNull
    private int price;

    @ManyToOne
    @JoinColumn(name = "publishers", referencedColumnName = "id")
    private Publisher publisher;
}
