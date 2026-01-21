# Sistema de Gestão - Escola de Cursos Livres

Sistema de controle de cursos, alunos e matrículas desenvolvido em Java com JPA e PostgreSQL.

## 🎯 Funcionalidades

- ✅ Cadastrar, listar e buscar **alunos**
- ✅ Cadastrar, listar e buscar **cursos**
- ✅ Realizar e listar **matrículas** (relacionando alunos com cursos)
- ✅ **Relatório de Engajamento** (bônus):
  - Total de alunos por curso
  - Média de idade dos alunos
  - Matrículas nos últimos 30 dias

## 🛠️ Tecnologias

- Java 17
- JPA (Hibernate 6.4)
- PostgreSQL
- Maven

## 📋 Pré-requisitos

- Java JDK 17+
- Maven
- PostgreSQL

## 🗄️ Configuração do Banco de Dados

1. Crie o banco de dados e usuário no PostgreSQL:

```sql
CREATE USER escola WITH PASSWORD 'escola123';
CREATE DATABASE escola_cursos OWNER escola;
GRANT ALL PRIVILEGES ON DATABASE escola_cursos TO escola;
```

2. As tabelas serão criadas automaticamente pelo Hibernate na primeira execução

## 🚀 Como Executar

```bash
# Compilar o projeto
mvn clean compile

# Executar
mvn exec:java -Dexec.mainClass="br.com.escola.Main"
```

Ou compile e execute o JAR:

```bash
mvn clean package
java -jar target/sistema-escola-1.0-SNAPSHOT.jar
```

## 📁 Estrutura do Projeto

```
src/main/java/br/com/escola/
├── Main.java              # Aplicação principal com menus
├── model/
│   ├── Aluno.java         # Entidade Aluno
│   ├── Curso.java         # Entidade Curso
│   └── Matricula.java     # Entidade Matrícula
├── dao/
│   ├── AlunoDAO.java      # Operações CRUD para Aluno
│   ├── CursoDAO.java      # Operações CRUD para Curso
│   └── MatriculaDAO.java  # Operações CRUD para Matrícula
└── util/
    └── JPAUtil.java       # Utilitário de conexão JPA
```

## 🔗 Modelo de Dados

### Aluno
- id (Long) - PK
- nome (String)
- email (String) - Único
- dataNascimento (LocalDate)

### Curso
- id (Long) - PK
- nome (String)
- descricao (String)
- cargaHoraria (int)

### Matrícula
- id (Long) - PK
- aluno (Aluno) - FK ManyToOne
- curso (Curso) - FK ManyToOne
- dataMatricula (LocalDate)