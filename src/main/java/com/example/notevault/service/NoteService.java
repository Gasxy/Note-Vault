package com.example.notevault.service;

import com.example.notevault.model.Note;
import com.example.notevault.repository.NoteRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public List<Note> getNotesForUser(String username, boolean isAdmin) {
        if (isAdmin) {
            return noteRepository.findAll();
        }
        return noteRepository.findByOwnerUsername(username);
    }

    public Note createNote(Note note, String username) {
        note.setId(null);
        note.setOwnerUsername(username);
        return noteRepository.save(note);
    }

    public void deleteNote(Long id, String username, boolean isAdmin) {
        Optional<Note> noteOpt = noteRepository.findById(id);
        if (noteOpt.isEmpty()) {
            throw new IllegalArgumentException("Note not found with id: " + id);
        }

        Note note = noteOpt.get();
        if (!isAdmin && !note.getOwnerUsername().equals(username)) {
            throw new AccessDeniedException("You are not allowed to delete this note.");
        }

        noteRepository.delete(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }
}
