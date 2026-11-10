package view;

import controller.CadastroAlunos;
import exception.CadastroException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import model.Aluno;
import persistence.ArquivoAlunos;

public class Menu {

    private final CadastroAlunos cadastro;
    private final ArquivoAlunos arquivo;
    private final Scanner scanner;

    public Menu(CadastroAlunos cadastro, ArquivoAlunos arquivo) {
        this.cadastro = cadastro;
        this.arquivo = arquivo;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        carregarDados();

        try {
            int opcao;
            do {
                exibirMenu();
                opcao = lerInteiro("Escolha uma opção: ");
                executar(opcao);
            } while (opcao != 0);
        } catch (NoSuchElementException e) {
            // acontece quando a entrada do teclado é fechada (Ctrl+Z / Ctrl+D)
            System.out.println("\nEntrada encerrada. Saindo...");
        }

        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n--- MENU CADASTRO DE ALUNOS ---");
        System.out.println("1 - Cadastrar aluno");
        System.out.println("2 - Listar alunos");
        System.out.println("3 - Atualizar aluno");
        System.out.println("4 - Deletar aluno");
        System.out.println("0 - Sair");
    }

    // Todos os erros do cadastro (aluno não encontrado, dados inválidos, matrícula repetida)
    // caem no mesmo catch e o usuário vê a mensagem da exceção
    private void executar(int opcao) {
        try {
            switch (opcao) {
                case 1 -> cadastrar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> deletar();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção inválida! Escolha um número de 0 a 4.");
            }
        } catch (CadastroException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void cadastrar() throws CadastroException {
        String nome = lerTexto("Nome: ");
        String matricula = lerTexto("Matrícula: ");
        String curso = lerTexto("Curso: ");
        double nota = lerNota("Nota: ");

        Aluno aluno = cadastro.cadastrarAluno(nome, matricula, curso, nota);
        salvarDados();
        System.out.println("Aluno cadastrado com sucesso! ID: " + aluno.getId());
    }

    private void listar() {
        List<Aluno> alunos = cadastro.listarAlunos();

        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        System.out.println("\n--- LISTA DE ALUNOS ---");
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
    }

    private void atualizar() throws CadastroException {
        int id = lerInteiro("Digite o ID do aluno que deseja atualizar: ");

        // busca antes de pedir os dados: se o id não existir, já avisa aqui
        Aluno aluno = cadastro.buscarPorId(id);
        System.out.println("Dados atuais: " + aluno);

        String nome = lerTexto("Novo nome: ");
        String matricula = lerTexto("Nova matrícula: ");
        String curso = lerTexto("Novo curso: ");
        double nota = lerNota("Nova nota: ");

        cadastro.atualizarAluno(id, nome, matricula, curso, nota);
        salvarDados();
        System.out.println("Aluno atualizado com sucesso!");
    }

    private void deletar() throws CadastroException {
        int id = lerInteiro("Digite o ID do aluno que deseja deletar: ");

        cadastro.deletarAluno(id);
        salvarDados();
        System.out.println("Aluno deletado com sucesso!");
    }

    // ---------- arquivo ----------

    private void carregarDados() {
        try {
            List<Aluno> alunos = arquivo.carregar();
            cadastro.carregarAlunos(alunos);

            if (!alunos.isEmpty()) {
                System.out.println(alunos.size() + " aluno(s) carregado(s) do arquivo "
                        + arquivo.getCaminho().getFileName() + ".");
            }
        } catch (IOException | CadastroException e) {
            System.out.println("Não foi possível carregar os alunos salvos: " + e.getMessage());
            fazerBackupDoArquivo();
            System.out.println("O sistema vai começar com a lista vazia.");
        }
    }

    private void salvarDados() {
        try {
            arquivo.salvar(cadastro.listarAlunos());
        } catch (IOException e) {
            System.out.println("Atenção: a alteração foi feita, mas não foi possível salvar no arquivo: "
                    + e.getMessage());
        }
    }

    private void fazerBackupDoArquivo() {
        try {
            Path backup = arquivo.fazerBackup();
            System.out.println("Uma cópia do arquivo com problema foi salva em " + backup.getFileName() + ".");
        } catch (IOException e) {
            System.out.println("Não foi possível fazer a cópia de segurança do arquivo: " + e.getMessage());
        }
    }

    // ---------- leitura do teclado ----------
    // Tudo é lido com nextLine(), então não tem mais o problema do Enter sobrando no buffer

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    // Repete a pergunta até a pessoa digitar um número inteiro
    private int lerInteiro(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("\"" + valor + "\" não é um número inteiro. Tente de novo.");
            }
        }
    }

    // Aceita a nota com ponto ou com vírgula (7.5 ou 7,5)
    private double lerNota(String mensagem) {
        while (true) {
            String valor = lerTexto(mensagem);
            try {
                return Double.parseDouble(valor.replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("\"" + valor + "\" não é uma nota válida. Use um número, por exemplo 7.5");
            }
        }
    }
}

