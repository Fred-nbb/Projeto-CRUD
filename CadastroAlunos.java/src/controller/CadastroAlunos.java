package controller;

import exception.AlunoNaoEncontradoException;
import exception.DadosInvalidosException;
import exception.MatriculaDuplicadaException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.Aluno;
import persistence.ArquivoAlunos;

public class CadastroAlunos {

    public static final double NOTA_MINIMA = 0.0;
    public static final double NOTA_MAXIMA = 10.0;

    // Map: acha o aluno pelo id direto, sem precisar percorrer uma lista.
    // LinkedHashMap mantém a ordem em que os alunos foram cadastrados.
    private final Map<Integer, Aluno> alunosPorId = new LinkedHashMap<>();

    // Set: não aceita elementos repetidos, então garante que cada matrícula aparece uma vez só.
    private final Set<String> matriculas = new HashSet<>();

    private int proximoId = 1;

    // CREATE
    public Aluno cadastrarAluno(String nome, String matricula, String curso, double nota)
            throws DadosInvalidosException, MatriculaDuplicadaException {
        validarDados(nome, matricula, curso, nota);

        String novaMatricula = matricula.trim();
        if (matriculas.contains(novaMatricula)) {
            throw new MatriculaDuplicadaException(novaMatricula);
        }

        Aluno aluno = new Aluno(proximoId, nome.trim(), novaMatricula, curso.trim(), nota);
        adicionar(aluno);
        return aluno;
    }

    // READ: devolve uma cópia, assim quem recebe a lista não mexe no cadastro por fora
    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunosPorId.values());
    }

    public Aluno buscarPorId(int id) throws AlunoNaoEncontradoException {
        Aluno aluno = alunosPorId.get(id);

        if (aluno == null) {
            throw new AlunoNaoEncontradoException(id);
        }
        return aluno;
    }

    // UPDATE
    public void atualizarAluno(int id, String nome, String matricula, String curso, double nota)
            throws AlunoNaoEncontradoException, DadosInvalidosException, MatriculaDuplicadaException {
        Aluno aluno = buscarPorId(id);
        validarDados(nome, matricula, curso, nota);

        String novaMatricula = matricula.trim();
        boolean mudouMatricula = !novaMatricula.equals(aluno.getMatricula());

        if (mudouMatricula) {
            if (matriculas.contains(novaMatricula)) {
                throw new MatriculaDuplicadaException(novaMatricula);
            }
            matriculas.remove(aluno.getMatricula());
            matriculas.add(novaMatricula);
        }

        aluno.setNome(nome.trim());
        aluno.setMatricula(novaMatricula);
        aluno.setCurso(curso.trim());
        aluno.setNota(nota);
    }

    // DELETE
    public void deletarAluno(int id) throws AlunoNaoEncontradoException {
        Aluno aluno = buscarPorId(id);
        alunosPorId.remove(id);
        matriculas.remove(aluno.getMatricula());
    }

    // Usado ao abrir o sistema, com os alunos lidos do arquivo.
    // Primeiro confere todos e só depois adiciona, para o cadastro não ficar pela metade se der erro.
    public void carregarAlunos(List<Aluno> alunos) throws DadosInvalidosException, MatriculaDuplicadaException {
        Set<Integer> ids = new HashSet<>(alunosPorId.keySet());
        Set<String> matriculasLidas = new HashSet<>(matriculas);

        for (Aluno aluno : alunos) {
            validarDados(aluno.getNome(), aluno.getMatricula(), aluno.getCurso(), aluno.getNota());

            // add() do Set devolve false quando o elemento já estava lá
            if (!ids.add(aluno.getId())) {
                throw new DadosInvalidosException("O ID " + aluno.getId() + " aparece mais de uma vez.");
            }
            if (!matriculasLidas.add(aluno.getMatricula())) {
                throw new MatriculaDuplicadaException(aluno.getMatricula());
            }
        }

        for (Aluno aluno : alunos) {
            adicionar(aluno);
        }
    }

    // único lugar que coloca um aluno nas coleções (usado no cadastro e no carregamento)
    private void adicionar(Aluno aluno) {
        alunosPorId.put(aluno.getId(), aluno);
        matriculas.add(aluno.getMatricula());
        proximoId = Math.max(proximoId, aluno.getId() + 1);
    }

    private void validarDados(String nome, String matricula, String curso, double nota)
            throws DadosInvalidosException {
        validarTexto(nome, "nome");
        validarTexto(matricula, "matrícula");
        validarTexto(curso, "curso");

        if (Double.isNaN(nota) || nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new DadosInvalidosException("A nota deve estar entre 0 e 10.");
        }
    }

    private void validarTexto(String valor, String campo) throws DadosInvalidosException {
        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("O campo " + campo + " não pode ficar vazio.");
        }
        // o ';' separa as colunas no arquivo, então não pode aparecer dentro dos dados
        if (valor.contains(ArquivoAlunos.SEPARADOR)) {
            throw new DadosInvalidosException("O campo " + campo + " não pode ter o caractere ';'.");
        }
    }
}
