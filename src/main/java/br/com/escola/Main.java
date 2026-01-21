package br.com.escola;

import br.com.escola.dao.AlunoDAO;
import br.com.escola.dao.CursoDAO;
import br.com.escola.dao.MatriculaDAO;
import br.com.escola.model.Aluno;
import br.com.escola.model.Curso;
import br.com.escola.model.Matricula;
import br.com.escola.util.JPAUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AlunoDAO alunoDAO = new AlunoDAO();
    private static final CursoDAO cursoDAO = new CursoDAO();
    private static final MatriculaDAO matriculaDAO = new MatriculaDAO();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTÃO - ESCOLA DE CURSOS LIVRES      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> menuAlunos();
                case 2 -> menuCursos();
                case 3 -> menuMatriculas();
                case 4 -> relatorioEngajamento();
                case 0 -> {
                    System.out.println("\n🎓 Obrigado por usar o sistema! Até mais!");
                    JPAUtil.close();
                }
                default -> System.out.println("\n⚠️  Opção inválida! Tente novamente.");
            }
        } while (opcao != 0);
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│           MENU PRINCIPAL             │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. 👤 Gerenciar Alunos              │");
        System.out.println("│  2. 📚 Gerenciar Cursos              │");
        System.out.println("│  3. 📋 Gerenciar Matrículas          │");
        System.out.println("│  4. 📊 Relatório de Engajamento      │");
        System.out.println("│  0. 🚪 Sair                          │");
        System.out.println("└──────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    // ==================== MENU ALUNOS ====================
    private static void menuAlunos() {
        int opcao;
        do {
            System.out.println("\n┌──────────────────────────────────────┐");
            System.out.println("│          GERENCIAR ALUNOS            │");
            System.out.println("├──────────────────────────────────────┤");
            System.out.println("│  1. Cadastrar Aluno                  │");
            System.out.println("│  2. Listar Alunos                    │");
            System.out.println("│  3. Buscar Aluno por E-mail          │");
            System.out.println("│  0. Voltar                           │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarAluno();
                case 2 -> listarAlunos();
                case 3 -> buscarAlunoPorEmail();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("\n⚠️  Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarAluno() {
        System.out.println("\n--- CADASTRAR NOVO ALUNO ---");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        System.out.print("Data de Nascimento (dd/MM/yyyy): ");
        String dataStr = scanner.nextLine();
        
        try {
            LocalDate dataNascimento = LocalDate.parse(dataStr, formatter);
            Aluno aluno = new Aluno(nome, email, dataNascimento);
            alunoDAO.salvar(aluno);
            System.out.println("\n✅ Aluno cadastrado com sucesso! ID: " + aluno.getId());
        } catch (DateTimeParseException e) {
            System.out.println("\n❌ Data inválida! Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao cadastrar aluno: " + e.getMessage());
        }
    }

    private static void listarAlunos() {
        System.out.println("\n--- LISTA DE ALUNOS ---");
        List<Aluno> alunos = alunoDAO.listarTodos();
        
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
        } else {
            System.out.println("┌────┬────────────────────────────┬──────────────────────────────┬────────────────┐");
            System.out.println("│ ID │ Nome                       │ E-mail                       │ Nascimento     │");
            System.out.println("├────┼────────────────────────────┼──────────────────────────────┼────────────────┤");
            for (Aluno a : alunos) {
                System.out.printf("│ %-2d │ %-26s │ %-28s │ %-14s │%n",
                    a.getId(),
                    limitarTexto(a.getNome(), 26),
                    limitarTexto(a.getEmail(), 28),
                    a.getDataNascimento().format(formatter));
            }
            System.out.println("└────┴────────────────────────────┴──────────────────────────────┴────────────────┘");
            System.out.println("Total: " + alunos.size() + " aluno(s)");
        }
    }

    private static void buscarAlunoPorEmail() {
        System.out.println("\n--- BUSCAR ALUNO POR E-MAIL ---");
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        
        Aluno aluno = alunoDAO.buscarPorEmail(email);
        if (aluno != null) {
            System.out.println("\n✅ Aluno encontrado:");
            System.out.println(aluno);
        } else {
            System.out.println("\n❌ Aluno não encontrado com este e-mail.");
        }
    }

    // ==================== MENU CURSOS ====================
    private static void menuCursos() {
        int opcao;
        do {
            System.out.println("\n┌──────────────────────────────────────┐");
            System.out.println("│          GERENCIAR CURSOS            │");
            System.out.println("├──────────────────────────────────────┤");
            System.out.println("│  1. Cadastrar Curso                  │");
            System.out.println("│  2. Listar Cursos                    │");
            System.out.println("│  3. Buscar Curso por Nome            │");
            System.out.println("│  0. Voltar                           │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> cadastrarCurso();
                case 2 -> listarCursos();
                case 3 -> buscarCursoPorNome();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("\n⚠️  Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void cadastrarCurso() {
        System.out.println("\n--- CADASTRAR NOVO CURSO ---");
        
        System.out.print("Nome do Curso: ");
        String nome = scanner.nextLine();
        
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        
        System.out.print("Carga Horária (horas): ");
        int cargaHoraria = lerOpcao();
        scanner.nextLine(); // Limpar buffer
        
        try {
            Curso curso = new Curso(nome, descricao, cargaHoraria);
            cursoDAO.salvar(curso);
            System.out.println("\n✅ Curso cadastrado com sucesso! ID: " + curso.getId());
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao cadastrar curso: " + e.getMessage());
        }
    }

    private static void listarCursos() {
        System.out.println("\n--- LISTA DE CURSOS ---");
        List<Curso> cursos = cursoDAO.listarTodos();
        
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
        } else {
            System.out.println("┌────┬────────────────────────────┬──────────────────────────────────────┬───────────┐");
            System.out.println("│ ID │ Nome                       │ Descrição                            │ Carga (h) │");
            System.out.println("├────┼────────────────────────────┼──────────────────────────────────────┼───────────┤");
            for (Curso c : cursos) {
                System.out.printf("│ %-2d │ %-26s │ %-36s │ %-9d │%n",
                    c.getId(),
                    limitarTexto(c.getNome(), 26),
                    limitarTexto(c.getDescricao(), 36),
                    c.getCargaHoraria());
            }
            System.out.println("└────┴────────────────────────────┴──────────────────────────────────────┴───────────┘");
            System.out.println("Total: " + cursos.size() + " curso(s)");
        }
    }

    private static void buscarCursoPorNome() {
        System.out.println("\n--- BUSCAR CURSO POR NOME ---");
        System.out.print("Nome (ou parte): ");
        String nome = scanner.nextLine();
        
        List<Curso> cursos = cursoDAO.buscarPorNome(nome);
        if (!cursos.isEmpty()) {
            System.out.println("\n✅ Curso(s) encontrado(s):");
            for (Curso c : cursos) {
                System.out.println(c);
            }
        } else {
            System.out.println("\n❌ Nenhum curso encontrado com este nome.");
        }
    }

    // ==================== MENU MATRÍCULAS ====================
    private static void menuMatriculas() {
        int opcao;
        do {
            System.out.println("\n┌──────────────────────────────────────┐");
            System.out.println("│        GERENCIAR MATRÍCULAS          │");
            System.out.println("├──────────────────────────────────────┤");
            System.out.println("│  1. Realizar Matrícula               │");
            System.out.println("│  2. Listar Todas as Matrículas       │");
            System.out.println("│  0. Voltar                           │");
            System.out.println("└──────────────────────────────────────┘");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> realizarMatricula();
                case 2 -> listarMatriculas();
                case 0 -> System.out.println("Voltando ao menu principal...");
                default -> System.out.println("\n⚠️  Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void realizarMatricula() {
        System.out.println("\n--- REALIZAR MATRÍCULA ---");
        
        // Listar alunos disponíveis
        System.out.println("\nAlunos disponíveis:");
        List<Aluno> alunos = alunoDAO.listarTodos();
        if (alunos.isEmpty()) {
            System.out.println("❌ Nenhum aluno cadastrado. Cadastre um aluno primeiro.");
            return;
        }
        for (Aluno a : alunos) {
            System.out.println("  " + a.getId() + " - " + a.getNome());
        }
        
        System.out.print("\nID do Aluno: ");
        Long alunoId = (long) lerOpcao();
        
        // Listar cursos disponíveis
        System.out.println("\nCursos disponíveis:");
        List<Curso> cursos = cursoDAO.listarTodos();
        if (cursos.isEmpty()) {
            System.out.println("❌ Nenhum curso cadastrado. Cadastre um curso primeiro.");
            return;
        }
        for (Curso c : cursos) {
            System.out.println("  " + c.getId() + " - " + c.getNome());
        }
        
        System.out.print("\nID do Curso: ");
        Long cursoId = (long) lerOpcao();
        
        try {
            Aluno aluno = alunoDAO.buscarPorId(alunoId);
            Curso curso = cursoDAO.buscarPorId(cursoId);
            
            if (aluno == null) {
                System.out.println("\n❌ Aluno não encontrado!");
                return;
            }
            if (curso == null) {
                System.out.println("\n❌ Curso não encontrado!");
                return;
            }
            
            Matricula matricula = new Matricula(aluno, curso, LocalDate.now());
            matriculaDAO.salvar(matricula);
            System.out.println("\n✅ Matrícula realizada com sucesso!");
            System.out.println("   Aluno: " + aluno.getNome());
            System.out.println("   Curso: " + curso.getNome());
            System.out.println("   Data: " + LocalDate.now().format(formatter));
        } catch (Exception e) {
            System.out.println("\n❌ Erro ao realizar matrícula: " + e.getMessage());
        }
    }

    private static void listarMatriculas() {
        System.out.println("\n--- LISTA DE MATRÍCULAS ---");
        List<Matricula> matriculas = matriculaDAO.listarTodas();
        
        if (matriculas.isEmpty()) {
            System.out.println("Nenhuma matrícula registrada.");
        } else {
            System.out.println("┌────┬────────────────────────────┬────────────────────────────┬────────────────┐");
            System.out.println("│ ID │ Aluno                      │ Curso                      │ Data Matrícula │");
            System.out.println("├────┼────────────────────────────┼────────────────────────────┼────────────────┤");
            for (Matricula m : matriculas) {
                System.out.printf("│ %-2d │ %-26s │ %-26s │ %-14s │%n",
                    m.getId(),
                    limitarTexto(m.getAluno().getNome(), 26),
                    limitarTexto(m.getCurso().getNome(), 26),
                    m.getDataMatricula().format(formatter));
            }
            System.out.println("└────┴────────────────────────────┴────────────────────────────┴────────────────┘");
            System.out.println("Total: " + matriculas.size() + " matrícula(s)");
        }
    }

    // ==================== RELATÓRIO DE ENGAJAMENTO (BÔNUS) ====================
    private static void relatorioEngajamento() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║         RELATÓRIO AVANÇADO DE ENGAJAMENTO            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        
        List<Curso> cursos = cursoDAO.listarTodos();
        
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
            return;
        }
        
        for (Curso curso : cursos) {
            Long totalAlunos = matriculaDAO.contarAlunosPorCurso(curso.getId());
            Double mediaIdade = matriculaDAO.mediaIdadeAlunosPorCurso(curso.getId());
            Long matriculasRecentes = matriculaDAO.contarMatriculasUltimos30Dias(curso.getId());
            
            System.out.println("\n┌──────────────────────────────────────────────────────┐");
            System.out.printf("│ 📚 Curso: %-41s │%n", limitarTexto(curso.getNome(), 41));
            System.out.println("├──────────────────────────────────────────────────────┤");
            System.out.printf("│   👥 Total de alunos matriculados: %-17d │%n", totalAlunos);
            System.out.printf("│   📅 Média de idade dos alunos: %-20s │%n", 
                mediaIdade != null ? String.format("%.1f anos", mediaIdade) : "N/A");
            System.out.printf("│   🆕 Matrículas nos últimos 30 dias: %-15d │%n", matriculasRecentes);
            System.out.println("└──────────────────────────────────────────────────────┘");
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String limitarTexto(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) return texto;
        return texto.substring(0, tamanho - 3) + "...";
    }
}
