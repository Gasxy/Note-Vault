# Changelog

Journal of changes to Note-Vault. Each entry is tied to a push/pull session.
Format: date → What / Where / Why / How / Impact.

---

## [2026-08-20] — Initial project build: Note-Vault (1bbf62f)

- **What:**  Created the Note-Vault Spring Boot application from scratch —
            a role-based note management service with both a Thymeleaf
            MVC UI and a JSON REST API.
- **Where:** Initial commit `1bbf62f`. Files introduced:

    Build / project root
    - `pom.xml` — Maven project, parent `spring-boot-starter-parent`
      `3.3.4`, Java 17. Dependencies: `spring-boot-starter-web`,
      `spring-boot-starter-security`, `spring-boot-starter-data-jpa`,
      `spring-boot-starter-thymeleaf`,
      `thymeleaf-extras-springsecurity6`, `h2` (runtime),
      `spring-boot-starter-test` and `spring-security-test` (test).

    Application entry point
    - `src/main/java/com/example/notevault/NoteVaultApplication.java` —
      `@SpringBootApplication` with the standard `main` method.

    Security
    - `src/main/java/com/example/notevault/config/SecurityConfig.java` —
      `BCryptPasswordEncoder`, in-memory users (`user`/`password` →
      `USER`, `admin`/`admin123` → `ADMIN`), `SecurityFilterChain`
      permitting `/`, `/login`, `/h2-console/**`, `/css/**`, `/js/**`;
      restricting `/admin/**` and `/api/admin/**` to `ADMIN`;
      restricting `/notes/**` and `/api/notes/**` to `USER` or
      `ADMIN`; form login with `defaultSuccessUrl("/notes")`; HTTP
      Basic enabled; logout to `/login?logout`; CSRF disabled for
      `/h2-console/**` and `/api/**`; frame options disabled.

    Domain / persistence
    - `src/main/java/com/example/notevault/model/Note.java` — JPA
      `@Entity` mapped to table `notes` with `id` (IDENTITY),
      `title`, `content` (length 2000), `ownerUsername`; no-arg and
      full constructors plus getters/setters.
    - `src/main/java/com/example/notevault/model/Role.java` — utility
      constants `USER` and `ADMIN`.
    - `src/main/java/com/example/notevault/repository/NoteRepository.java`
      — Spring Data `JpaRepository<Note, Long>` with
      `findByOwnerUsername(String)`.

    Business logic
    - `src/main/java/com/example/notevault/service/NoteService.java` —
      `getNotesForUser(username, isAdmin)` (admins see all, users see
      their own), `createNote(note, username)` (forces `id = null`
      and stamps owner), `deleteNote(id, username, isAdmin)` with
      `AccessDeniedException` for non-owner non-admin and
      `IllegalArgumentException` for missing ids, plus `findById`.

    Controllers
    - `src/main/java/com/example/notevault/controller/NoteMvcController.java`
      — `@Controller` routes: `GET /` → redirect `/notes`,
      `GET /login` → `login`, `GET /notes` → list (filters by role),
      `POST /notes` → create, `POST /notes/delete/{id}` → delete,
      `GET /admin` → `admin` view (ADMIN-gated by SecurityConfig).
    - `src/main/java/com/example/notevault/controller/NoteRestController.java`
      — `@RestController @RequestMapping("/api/notes")`: `GET` list,
      `POST` create (201 Created), `DELETE /{id}` returning
      `{message, id}` or 403/404 JSON on error.

    Configuration / data
    - `src/main/resources/application.properties` — `spring.application.name=notevault`,
      H2 console enabled at `/h2-console`, in-memory datasource
      `jdbc:h2:mem:notevaultdb;DB_CLOSE_DELAY=-1` (user `sa`, empty
      password), JPA `ddl-auto=update`, SQL init always-on from
      `classpath:data.sql`, `defer-datasource-initialization=true`.
    - `src/main/resources/data.sql` — four seed rows: "Welcome User"
      and "Shopping List" owned by `user`, "Admin Notice" and
      "Project Ideas" owned by `admin`.

    UI templates
    - `src/main/resources/templates/login.html` — login form.
    - `src/main/resources/templates/notes.html` — notes UI with role
      flags.
    - `src/main/resources/templates/admin.html` — admin dashboard
      view.

- **Why:**   Establish the baseline Note-Vault project: a small but
            complete reference of Spring Boot + Spring Security + JPA
            + Thymeleaf with both a browser UI and a REST API, and a
            clear USER/ADMIN authorization model. Decisions made:
            in-memory H2 + seed SQL for zero-setup demos;
            `InMemoryUserDetailsManager` for the same reason; form
            login + HTTP Basic so the same auth serves UI and API;
            owner-scoped reads with admin override for oversight.
- **How:**   Scaffolded a standard Maven Spring Boot project, added
            the dependencies above, wired security with a single
            `SecurityConfig`, modeled `Note` as a JPA entity with an
            `ownerUsername` ownership field, exposed two parallel
            controllers (MVC for the UI, REST for JSON), and pre-seeded
            the database so the app is usable immediately after
            `mvn spring-boot:run`.
- **Impact:** First commit on `main`. Sets the entire codebase,
            architecture, dependencies, security model, and default
            credentials in stone. Every later change builds on this
            baseline.

---

## [2026-08-25] — Expand README with full project docs (83099b8)

- **What:**  Rewrote README from a 2-line stub to a full project reference.
- **Where:** `README.md`
- **Why:**   The original README only said "A personal vault for storing
            whatever notes you want; private, non-private anything" — no
            setup steps, no API docs, no security notes.
- **How:**   Added sections for features, tech stack, prerequisites, getting
            started, default credentials, project structure, URL map, REST
            API examples, configuration, and security notes. Pulled facts
            from `pom.xml`, controllers, `SecurityConfig`, and
            `application.properties`.
- **Impact:** Docs only. No code or config change.

---

## [2026-08-25] — Add CHANGELOG.md journal (this commit)

- **What:**  Introduced a structured change journal to record every push/pull
            session for the project.
- **Where:** `CHANGELOG.md` (new file)
- **Why:**   Need a single, human-readable record of what changed, when,
            where, why, and how — independent of `git log` — so the project
            state and history can be reconstructed at a glance.
- **How:**   Created this file with a fixed header explaining the format and
            the inaugural entry documenting the README push (83099b8).
            Going forward, a dated `What / Where / Why / How / Impact`
            section will be appended after each push/pull session when
            reminded.
- **Impact:** Docs only. Establishes a new convention — entries are
            session-scoped, not per-commit, and updated only when prompted
            at the end of a session.