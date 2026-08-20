package com.example.notevault.controller;

import com.example.notevault.model.Note;
import com.example.notevault.service.NoteService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NoteMvcController {

    private final NoteService noteService;

    public NoteMvcController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/notes";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/notes")
    public String listNotes(Model model, Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        List<Note> notes = noteService.getNotesForUser(username, isAdmin);

        model.addAttribute("notes", notes);
        model.addAttribute("newNote", new Note());
        model.addAttribute("username", username);
        model.addAttribute("isAdmin", isAdmin);
        return "notes";
    }

    @PostMapping("/notes")
    public String createNote(@ModelAttribute("newNote") Note newNote,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        noteService.createNote(newNote, username);
        redirectAttributes.addFlashAttribute("success", "Note created successfully.");
        return "redirect:/notes";
    }

    @PostMapping("/notes/delete/{id}")
    public String deleteNote(@PathVariable Long id,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        boolean isAdmin = isAdmin(authentication);
        try {
            noteService.deleteNote(id, username, isAdmin);
            redirectAttributes.addFlashAttribute("success", "Note deleted.");
        } catch (AccessDeniedException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/notes";
    }

    @GetMapping("/admin")
    public String adminPanel(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "admin";
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(a -> a.equals("ROLE_ADMIN"));
    }
}
