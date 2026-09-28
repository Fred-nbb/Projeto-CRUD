import java.util.ArrayList;
import java.util.Scanner;

public class CadastroAlunos {

    // Classe interna representando o Aluno
    static class Aluno {
        int id;
        String nome;
        String matricula;
        String curso;
        double nota;

        Aluno(int id, String nome, String matricula, String curso, double nota) {
            this.id = id;
            this.nome = nome;
            this.matricula = matricula;
            this.curso = curso;
            this.nota = nota;
        }
    }

    static ArrayList<Aluno> alunos = new ArrayList<>();
    static Scanner scanner = new Scanner(System.in);
    static int proximoId = 1;

    public static void main(String[] args) {
        int opcao;

        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpa o buffer do Enter

            switch (opcao) {
                case 1 -> criarAluno();
                case 2 -> listarAlunos();
                case 3 -> atualizarAluno();
                case 4 -> deletarAluno();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    static void exibirMenu() {
        System.out.println("\n--- MENU CADASTRO DE ALUNOS ---");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Listar alunos");
        System.out.println("3 - Atualizar aluno");
        System.out.println("4 - Deletar aluno");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    // CREATE
    static void criarAluno() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        System.out.print("Curso: ");
        String curso = scanner.nextLine();

        System.out.print("Nota: ");
        double nota = scanner.nextDouble();
        scanner.nextLine();

        Aluno novoAluno = new Aluno(proximoId, nome, matricula, curso, nota);
        alunos.add(novoAluno);
        proximoId++;

        System.out.println("Aluno cadastrado com sucesso!");
    }

    // READ
    static void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        System.out.println("\n--- LISTA DE ALUNOS ---");
        for (Aluno a : alunos) {
            System.out.println("ID: " + a.id + " | Nome: " + a.nome +
                    " | Matrícula: " + a.matricula + " | Curso: " + a.curso +
                    " | Nota: " + a.nota);
        }
    }

    // UPDATE
    static void atualizarAluno() {
        System.out.print("Digite o ID do aluno que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        for (Aluno a : alunos) {
            if (a.id == id) {
                System.out.print("Novo nome: ");
                a.nome = scanner.nextLine();

                System.out.print("Nova matrícula: ");
                a.matricula = scanner.nextLine();

                System.out.print("Novo curso: ");
                a.curso = scanner.nextLine();

                System.out.print("Nova nota: ");
                a.nota = scanner.nextDouble();
                scanner.nextLine();

                System.out.println("Aluno atualizado com sucesso!");
                return;
            }
        }

        System.out.println("Aluno não encontrado.");
    }

    // DELETE
    static void deletarAluno() {
        System.out.print("Digite o ID do aluno que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        boolean removido = alunos.removeIf(a -> a.id == id);

        if (removido) {
            System.out.println("Aluno deletado com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }
}