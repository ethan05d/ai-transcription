# AI Transcription App Demo

This project is a full-stack application that leverages **OpenAI's Whisper** for video transcription. It is built using **Spring Boot** for the backend and **Vite + React** for the frontend. This app allows users to upload video files for transcription and manages the video files and transcriptions using AWS S3.

## Features

- Upload multiple video files.
- Transcribe videos using OpenAI Whisper.
- Manage video files and transcriptions with AWS S3 storage.
- Authentication using OAuth2.

## Tech Stack

- **Backend**: Spring Boot (Java), OpenAI Whisper
- **Frontend**: Vite + React (TypeScript)
- **Database**: PostgreSQL
- **Storage**: AWS S3
- **OAuth**: Google OAuth2

---

## Prerequisites

Before setting up the project, ensure that you have the following installed:

- **Docker** and **Docker Compose**
- **Java 17+** (for Spring Boot)
- **Node.js** (for Vite + React)
- **AWS S3** credentials
- **OpenAI API Key**

---

## Project Structure

```
├── backend/
│   └── transcription/
│       ├── Dockerfile (Spring Boot Dockerfile)
│       ├── .env (Spring Boot environment variables)
│       ├── src/ (Spring Boot source code)
├── frontend/
│   ├── Dockerfile (Vite + React Dockerfile)
│   ├── .env (Vite environment variables)
│   ├── src/ (React source code)
├── docker-compose.yml (Docker Compose setup for the project)
```

---

## Environment Variables

You will need to create two `.env` files for the backend and frontend.

### 1. Backend `.env` (located in `backend/transcription/.env`):

```env
# PostgreSQL Settings
POSTGRES_DB=your_db_name
POSTGRES_USER=your_db_user
POSTGRES_PASSWORD=your_db_password

# AWS S3 Settings
S3_ACCESS_KEY=your_aws_access_key
S3_SECRET_KEY=your_aws_secret_key
S3_BUCKET=your_s3_bucket_name

# OpenAI Settings
OPENAI_API_KEY=your_openai_api_key

# OAuth2 (Google)
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### 2. Frontend `.env` (located in `frontend/.env`):

```env
VITE_API_URL=http://localhost:8080/api
VITE_GOOGLE_CLIENT_ID=your_google_client_id

# !! Do this again in both .env files
POSTGRES_DB=your_db_name
POSTGRES_USER=your_db_user
POSTGRES_PASSWORD=your_db_password
```

---

## Docker Compose Setup

The project is containerized using **Docker Compose**. It sets up the backend, frontend, and PostgreSQL database services.

### Steps to Run the Application:

1. **Clone the repository**:

   ```bash
   git clone https://github.com/yourusername/ai-transcription-app.git
   cd ai-transcription-app
   ```

2. **Create the `.env` files** as described above.

3. **Run Docker Compose**:

   ```bash
   docker-compose up --build
   ```

4. **Access the application**:
   - Frontend: Open your browser and go to `http://localhost:3000`
   - Backend (API): Accessible at `http://localhost:8080`

### Docker Compose Services

- **`springboot-app`**: The backend service (Spring Boot app). It runs on port `8080` and handles video uploads, transcriptions, and OAuth authentication.
- **`vite-app`**: The frontend service (Vite + React). It runs on port `3000` and provides the user interface.
- **`postgres`**: The PostgreSQL database service. It stores user data, video information, and transcription details.

### Example `docker-compose.yml`:

```yaml
version: "3.9"

services:
  springboot-app:
    build:
      context: ./backend/transcription
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    env_file:
      - ./backend/transcription/.env
    depends_on:
      - postgres

  postgres:
    image: postgres:14-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_USER=${POSTGRES_USER}
      - POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
      - POSTGRES_DB=${POSTGRES_DB}
    volumes:
      - ./postgres-data:/var/lib/postgresql/data

  vite-app:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:3000"
    env_file:
      - ./frontend/.env
    depends_on:
      - springboot-app
```

---

## Running the App Locally without Docker

### Backend Setup (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd backend/transcription
   ```
2. Create the `.env` file as mentioned above.
3. Build and run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup (Vite + React)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Create the `.env` file as mentioned above.
3. Install dependencies and start the Vite development server:
   ```bash
   npm install
   npm run dev
   ```

---

## License

This project is licensed under the MIT License.

---

## Contributing

Feel free to open issues or pull requests if you find bugs or have suggestions for improvements.

---

## Contact

For any questions or support, contact ethan05dy@gmail.com.
