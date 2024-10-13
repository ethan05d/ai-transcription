package com.example.transcription.Repositories;

import com.example.transcription.Models.Transcription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranscriptionRepository extends JpaRepository<Transcription, Long> {

}
