package controller;

import java.util.ArrayList;
import model.Aluno;

public class CadastroAlunos {

    private ArrayList<Aluno> alunos;
    private int proximoId;

    public CadastroAlunos() {
        this.alunos = new ArrayList<>();
        this.proximoId = 1;
    }

    // CREATE
    public void cadastrarAluno(String nome, String matricula, String curso, double nota) {
        Aluno novoAluno = new Aluno(proximoId, nome, matricula, curso, nota);
        alunos.add(novoAluno);
        proximoId++;
    }

    // READ
    public void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }

        System.out.println("\n--- LISTA DE ALUNOS ---");
        for (Aluno a : alunos) {
            System.out.println("ID: " + a.getId() + " | Nome: " + a.getNome() +
                    " | Matrícula: " + a.getMatricula() + " | Curso: " + a.getCurso() +
                    " | Nota: " + a.getNota());
        }
    }

    // UPDATE (devolve false se o id não existir)
    public boolean atualizarAluno(int id, String nome, String matricula, String curso, double nota) {
        Aluno aluno = buscarPorId(id);

        if (aluno == null) {
            return false;
        }

        aluno.setNome(nome);
        aluno.setMatricula(matricula);
        aluno.setCurso(curso);
        aluno.setNota(nota);
        return true;
    }

    // DELETE (devolve false se o id não existir)
    public boolean deletarAluno(int id) {
        Aluno aluno = buscarPorId(id);

        if (aluno == null) {
            return false;
        }

        alunos.remove(aluno);
        return true;
    }

    // método de apoio, só é usado aqui dentro por isso é private
    private Aluno buscarPorId(int id) {
        for (Aluno a : alunos) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }
}
