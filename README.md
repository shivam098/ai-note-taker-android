# AI Note Taker — Android

Native Android app (Kotlin + Jetpack Compose) for the AI Note Taker — a
tasks + notes + nested lists app with a dark, violet-accented design.

## Tech stack
- **Kotlin** + **Jetpack Compose** (Material 3)
- **MVVM** — `AppViewModel` holds state as a `StateFlow<Snapshot>`
- **Offline-first** local persistence via `kotlinx.serialization` to a JSON file
- Min SDK 26 · Target/Compile SDK 35 · JDK 17

## Features (scaffold)
- Lists overview with open-task counts
- List detail: tasks, completed section, inline add
- Task detail: editable title + notes, **infinitely nested subtasks**, delete
- Seeded sample data; everything is free (no paywall)

## Run it
1. Open this folder in **Android Studio** (Ladybug or newer).
2. Let Gradle sync (it will download the SDK components it needs).
3. Pick an emulator or device and press **Run**.

Or from the command line (needs JDK 17 + Android SDK):
```
./gradlew assembleDebug
```

## Project layout
```
app/src/main/java/com/ainotetaker/app/
  MainActivity.kt          # Compose entry point
  model/Models.kt          # TaskList, TaskItem, Snapshot
  data/TaskRepository.kt    # JSON file persistence + seed data
  ui/AppViewModel.kt        # state + mutations
  ui/NoteTakerApp.kt        # simple Home -> List -> Task navigation
  ui/screens/               # HomeScreen, ListScreen, TaskScreen
  ui/theme/                 # dark + violet Material theme
```

## Roadmap
- Quick-add sheet (Task / List / Voice)
- Reminders & recurring tasks
- AI features (voice → tasks, summaries) — needs an AI API key
- Cloud sync & sharing
