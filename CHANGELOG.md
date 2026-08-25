# Changelog

Journal of changes to Note-Vault. Each entry is tied to a push/pull session.
Format: date → What / Where / Why / How / Impact.

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