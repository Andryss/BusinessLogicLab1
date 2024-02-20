package ru.andryss.rutube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.andryss.rutube.model.Source;

public interface SourceRepository extends JpaRepository<Source, String> {
}
