# RAG Knowledge Management System

A full-stack Retrieval-Augmented Generation (RAG) application built with **Java**, **Spring Boot**, and **local Large Language Models (LLMs)**. The application enables users to upload documents, organize them into sections, and perform context-aware semantic search over selected knowledge sources.

Unlike basic RAG demos, this project implements the complete pipeline—from document ingestion and indexing to retrieval and answer generation—while allowing users to manually choose which documents or sections should be used as the knowledge source.

---

## Features

- Secure user authentication
- Upload multiple documents
- Organize documents into multiple sections
- Apache Tika-based document text extraction
- Manual document chunking
- Local embedding generation using Gemma Embedding Model
- Vector storage with Qdrant
- Metadata storage with PostgreSQL
- Query refinement using Qwen 2.5 3B (4-bit Quantized)
- Semantic retrieval over selected knowledge sources
- Context-aware answer generation
- Delete individual documents
- Delete selected documents
- Delete complete sections
- Manual knowledge source selection
   - Entire section
   - Selected documents

---

# Architecture

```
                           +----------------+
                           |     User       |
                           +-------+--------+
                                   |
                                   |
                          Upload Documents
                                   |
                                   v
                     +-------------------------+
                     |      Apache Tika        |
                     |  Document -> Plain Text |
                     +------------+------------+
                                  |
                                  |
                           Manual Chunking
                                  |
                                  |
                                  v
                     +-------------------------+
                     | Gemma Embedding Model   |
                     +------------+------------+
                                  |
                     Embeddings    | Metadata
                                  |
                 +----------------+----------------+
                 |                                 |
                 v                                 v
          +-------------+                  +---------------+
          |   Qdrant    |                  | PostgreSQL    |
          | Vector DB   |                  | Metadata DB   |
          +-------------+                  +---------------+

============================================================

                 Query Processing Pipeline

User Query
     |
     v
Qwen Query Refinement
     |
     v
Gemma Embedding
     |
     v
Qdrant Similarity Search
     |
     v
Relevant Chunks
     |
     v
Qwen Answer Generation
     |
     v
Final Response
```

---

# Project Workflow

## Document Ingestion

1. User logs in.
2. User creates sections.
3. User uploads one or more documents.
4. Apache Tika extracts text from the uploaded documents.
5. Documents are manually split into chunks.
6. Each chunk is embedded using the Gemma Embedding Model.
7. Embeddings are stored in Qdrant.
8. Metadata such as users, sections, documents, document IDs, and chunk IDs are stored in PostgreSQL.

---

## Query Workflow

1. User selects the knowledge source.
   - Entire section
   - Selected documents
2. User enters a query.
3. The query is refined using Qwen 2.5.
4. The refined query is embedded.
5. Qdrant retrieves the most relevant chunks.
6. Retrieved chunks are provided to Qwen.
7. Qwen generates a context-aware answer.
8. The response is displayed to the user.

---

# Tech Stack

## Backend

- Java
- Spring Boot
- Spring Data JPA
- Maven

## Frontend

- Thymeleaf
- HTML
- CSS
- JavaScript

## AI & RAG

- Apache Tika
- Manual Chunking
- Gemma Embedding Model
- Qwen 2.5 3B (4-bit Quantized)
- Qdrant Vector Database

---

# Database

## PostgreSQL

Stores application metadata including:

- User credentials
- Sections
- Documents
- Document IDs
- Chunk IDs
- Relationships between sections and documents

## Qdrant

Stores:

- Chunk embeddings
- Payload metadata used during semantic retrieval

---

# Knowledge Source Selection

Instead of searching through every indexed document, users can explicitly choose where retrieval should occur.

Supported scopes include:

- Entire section
- One or more selected documents

This improves retrieval precision by limiting semantic search to only the relevant documents.

---

# Folder Structure

```
project
│
├── src
│   ├── main
│   │   ├── java
│   │   ├── resources
│   │   │   ├── templates
│   │   │   ├── static
│   │   │   └── application.properties
│   │
│   └── test
│
├── pom.xml
└── README.md
```

---

# Prerequisites

Before running the project, ensure the following are installed:

- Java 26+
- Maven 4.0.0+
- PostgreSQL
- Qdrant
- Running Gemma Embedding Model 300M
- Running Qwen 2.5 3B (4-bit Quantized)

---

# Installation

## Clone the Repository

```bash
git clone https://github.com/anonymous6291/talk_to_document.git
cd ./talk_to_document
```

---

## Configure PostgreSQL

Create a PostgreSQL database and update the connection details in:

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.username=postgres
spring.datasource.password=password
```

---

## Add user

Inside `src/main/java/talktodocuments/talk_to_documents/login/Login.java` modify the line:

```
userManager.addUser("1@gmail.com", "12345");
```

---

## Start Qdrant

Example using Docker:

```bash
docker run -p 6333:6333 \
-v $(pwd)/qdrant_storage:/qdrant/storage \
qdrant/qdrant
```

---

## Start Local Models

Start:

- Gemma Embedding Model
- Qwen 2.5 3B (4-bit Quantized)

Using your preferred inference server (such as llama.cpp or Ollama), and configure the corresponding endpoints in `application.properties`.

---

# Building the Project

Clean and compile the project:

```bash
mvn clean compile
```

Run all tests:

```bash
mvn test
```

Package the application:

```bash
mvn clean package
```

This generates the executable JAR inside:

```
target/
```

---

# Running the Application

Run directly with Maven:

```bash
mvn spring-boot:run
```

Or run the packaged JAR:

```bash
java -jar target/<your-jar-name>.jar
```

The application will be available at:

```
http://localhost:8080
```

---

# Usage

1. Register or log in.
2. Create one or more sections.
3. Upload documents to a section.
4. Wait for indexing to complete.
5. Select the desired knowledge source:
   - Entire section
   - Selected documents
6. Enter your query.
7. Review the generated answer.
8. Manage your documents by deleting individual files, selected files, or entire sections as needed.

---

# Future Improvements

- Streaming responses
- Hybrid search (Keyword + Vector Search)
- OCR support for scanned PDFs
- Conversation history
- Citation generation
- Multi-user collaboration
- Role-based access control
- Document versioning
- REST API support
- Admin dashboard

---

# License

This project is licensed under the Apache License 2.0.

See the `LICENSE` file for details.