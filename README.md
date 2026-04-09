# 🎬 CineBook — Movie Booking Android App

A fully functional **Android Movie Booking Application** built with Java in Android Studio. This project demonstrates core Android development concepts including multi-screen navigation, local data persistence, UI components, and event handling.

---

## 📱 App Flow & Screenshots

```
Login Screen ➝ Home (Movies) ➝ Movie Details ➝ Theatre Selection ➝ Seat Selection ➝ Payment ➝ Confirmation
```

---

## ✨ Features

- 🔐 **Login with Validation** — Email, Password, CheckBox, and Spinner (City Selector)
- 🎥 **Movie Listing** — Grid-based movie display using RecyclerView
- 🏛️ **Theatre & Showtime Selection** — Dynamic show time buttons per theatre
- 💺 **Seat Selection** — Interactive seat grid (Available / Selected / Booked)
- 💳 **Payment Screen** — Booking confirmation with AlertDialog
- 🧾 **Booking Confirmation** — Receipt with Share option via Implicit Intent
- 📊 **SQLite Database** — Permanently stores all bookings in a local database
- 📁 **Internal Storage** — Saves plain-text receipt files on device storage
- 🔔 **Notifications** — Push notification on booking confirmation
- 💾 **SharedPreferences** — Remembers login state and selected city

---

## 🗂️ Project Structure

```
MovieBookingApp/
│
├── app/src/main/
│   ├── java/com/example/moviebooking/
│   │   ├── LoginActivity.java           # Entry point, validation, auto-login
│   │   ├── MainActivity.java            # Home screen, movie list, lifecycle
│   │   ├── MovieDetailActivity.java     # Movie synopsis and booking trigger
│   │   ├── TheatreActivity.java         # Theatre & showtime selection
│   │   ├── SeatSelectionActivity.java   # Interactive seat grid, price calc
│   │   ├── PaymentActivity.java         # Payment gateway simulation
│   │   ├── ConfirmationActivity.java    # Receipt, DB save, share intent
│   │   │
│   │   ├── adapters/
│   │   │   ├── MovieAdapter.java        # RecyclerView adapter for movies
│   │   │   ├── TheatreAdapter.java      # RecyclerView adapter for theatres
│   │   │   └── SeatAdapter.java        # RecyclerView adapter for seats
│   │   │
│   │   ├── models/
│   │   │   ├── MovieModel.java          # Movie data class
│   │   │   ├── TheatreModel.java        # Theatre data class
│   │   │   └── SeatModel.java          # Seat status model (AVAILABLE/SELECTED/BOOKED)
│   │   │
│   │   ├── database/
│   │   │   └── BookingDatabaseHelper.java  # SQLite: Create, Insert, Read, Delete
│   │   │
│   │   └── storage/
│   │       └── InternalStorageHelper.java  # File I/O: Write, Read, Count, Clear
│   │
│   ├── res/
│   │   ├── layout/       # XML screen & item layouts
│   │   ├── drawable/     # Shapes, gradients, seat icons, posters
│   │   └── values/       # Colors, Strings, Themes
│   │
│   └── AndroidManifest.xml  # Activity registration + permissions
```

---

## 🧱 Android Concepts Demonstrated

| Concept | Where Used |
|--------|-----------|
| `ConstraintLayout` | `activity_login.xml` |
| `RecyclerView` + `Adapter` | Movies, Theatres, Seats |
| `GridLayoutManager` | Movie grid (2 col), Seat grid (8 col) |
| `SharedPreferences` | Auto-login, city, last booking |
| `SQLite Database` | Full CRUD on bookings table |
| `Internal Storage` | Receipt text file (MODE_APPEND) |
| `Explicit Intent` | All screen-to-screen navigation |
| `Implicit Intent` | Share receipt via WhatsApp/Email |
| `AlertDialog` | Exit confirm, payment confirm |
| `Notification + PendingIntent` | Booking confirmation notification |
| `CheckBox` | Terms & Conditions in Login |
| `Spinner` | City selection dropdown |
| `Input Validation` | Email/Password validation with `setError()` |
| `Activity Lifecycle` | `onStart`, `onResume`, `onPause`, `onStop`, `onDestroy` |
| `OnBackPressedCallback` | Modern back navigation (Android 13+) |
| `Toast` | User feedback messages |

---

## 🛠️ Tech Stack

- **Language:** Java
- **IDE:** Android Studio
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Database:** SQLite (via `SQLiteOpenHelper`)
- **Build System:** Gradle

---

## 🚀 How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/pranav30r/movie-booking-system.git
   ```
2. Open the project in **Android Studio**
3. Let Gradle sync (click **"Sync Now"** if prompted)
4. Click the **Run ▶** button or press `Shift + F10`
5. Select an emulator or a connected physical device

---

## 📦 Dependencies (`app/build.gradle`)

```gradle
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.2'
implementation 'androidx.activity:activity:1.8.2'
```

---

## 👨‍💻 Author

**Pranav** — [@pranav30r](https://github.com/pranav30r)

---

## 📄 License

This project is built for educational purposes as part of an Android Development practical examination.
