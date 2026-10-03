package view;

import controller.CadastroAlunos;
import java.util.Scanner;

public class Menu {

    private CadastroAlunos cadastro;
    private Scanner scanner;

    public Menu() {
        this.cadastro = new CadastroAlunos();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine(); // limpa o buffer do Enter

            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> cadastro.listarAlunos();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU CADASTRO DE ALUNOS ---");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Listar alunos");
        System.out.println("3 - Atualizar aluno");
        System.out.println("4 - Deletar aluno");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void cadastrar() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        System.out.print("Curso: ");
        String curso = scanner.nextLine();

        System.out.print("Nota: ");
        double nota = scanner.nextDouble();
        scanner.nextLine();

        cadastro.cadastrarAluno(nome, matricula, curso, nota);
        System.out.println("Aluno cadastrado com sucesso!");
    }

    private void atualizar() {
        System.out.print("Digite o ID do aluno que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();

        System.out.print("Nova matrícula: ");
        String matricula = scanner.nextLine();

        System.out.print("Novo curso: ");
        String curso = scanner.nextLine();

        System.out.print("Nova nota: ");
        double nota = scanner.nextDouble();
        scanner.nextLine();

        if (cadastro.atualizarAluno(id, nome, matricula, curso, nota)) {
            System.out.println("Aluno atualizado com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private void deletar() {
        System.out.print("Digite o ID do aluno que deseja deletar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if (cadastro.deletarAluno(id)) {
            System.out.println("Aluno deletado com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }
}
