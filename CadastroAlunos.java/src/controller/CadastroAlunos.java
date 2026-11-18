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
import observer.CadastroObserver;
import validation.ValidadorAluno;

/**
 * Regras e operações do CRUD de alunos. Usa dois padrões de projeto:
 * Strategy (os dados são conferidos por um ValidadorAluno, que aplica regras que dá pra trocar)
 * e Observer (esta classe é a observada e avisa os CadastroObserver registrados depois de
 * cada cadastro, atualização ou remoção).
 */
public class CadastroAlunos {

    // Map: acha o aluno pelo id direto. LinkedHashMap mantém a ordem de cadastro.
    private final Map<Integer, Aluno> alunosPorId = new LinkedHashMap<>();

    // Set: não aceita repetidos, então garante uma matrícula por aluno.
    private final Set<String> matriculas = new HashSet<>();

    private final ValidadorAluno validador;
    private final List<CadastroObserver> observers = new ArrayList<>();
    private int proximoId = 1;

    /**
     * Cria um cadastro vazio com as regras de validação padrão do sistema.
     */
    public CadastroAlunos() {
        this(ValidadorAluno.padrao());
    }

    /**
     * Cria um cadastro vazio com um validador escolhido por quem está criando.
     *
     * @param validador o validador que confere os dados de cada aluno
     */
    public CadastroAlunos(ValidadorAluno validador) {
        this.validador = validador;
    }

    /**
     * Registra um observer, que será avisado depois de cada cadastro, atualização ou remoção.
     * Carregar alunos do arquivo (carregarAlunos) não avisa os observers.
     *
     * @param observer quem quer ser avisado quando o cadastro mudar
     */
    public void adicionarObserver(CadastroObserver observer) {
        observers.add(observer);
    }

    /**
     * Tira um observer da lista: ele deixa de ser avisado.
     *
     * @param observer o observer que não quer mais ser avisado
     */
    public void removerObserver(CadastroObserver observer) {
        observers.remove(observer);
    }

    /**
     * Cadastra um aluno novo (CREATE) com ID gerado automaticamente.
     *
     * @param nome      nome do aluno
     * @param matricula matrícula do aluno (não pode já existir)
     * @param curso     curso do aluno
     * @param nota      nota do aluno
     * @return o aluno cadastrado, já com o ID
     * @throws DadosInvalidosException      se algum dado for inválido
     * @throws MatriculaDuplicadaException se a matrícula já pertence a outro aluno
     */
    public Aluno cadastrarAluno(String nome, String matricula, String curso, double nota)
            throws DadosInvalidosException, MatriculaDuplicadaException {
        validar(proximoId, nome, matricula, curso, nota);

        String novaMatricula = matricula.trim();
        if (matriculas.contains(novaMatricula)) {
            throw new MatriculaDuplicadaException(novaMatricula);
        }

        Aluno aluno = new Aluno(proximoId, nome.trim(), novaMatricula, curso.trim(), nota);
        adicionar(aluno);
        notificarObservers();
        return aluno;
    }

    /**
     * Lista os alunos na ordem de cadastro (READ). Devolve uma cópia, para
     * quem recebe a lista não mexer no cadastro por fora.
     *
     * @return uma nova lista com todos os alunos
     */
    public List<Aluno> listarAlunos() {
        return new ArrayList<>(alunosPorId.values());
    }

    /**
     * Procura um aluno pelo ID (READ).
     *
     * @param id o ID do aluno
     * @return o aluno encontrado
     * @throws AlunoNaoEncontradoException se não existir aluno com esse ID
     */
    public Aluno buscarPorId(int id) throws AlunoNaoEncontradoException {
        Aluno aluno = alunosPorId.get(id);

        if (aluno == null) {
            throw new AlunoNaoEncontradoException(id);
        }
        return aluno;
    }

    /**
     * Atualiza todos os dados de um aluno (UPDATE). Ele pode manter a própria
     * matrícula, mas não pode usar a de outro aluno.
     *
     * @param id        o ID do aluno que será atualizado
     * @param nome      o novo nome
     * @param matricula a nova matrícula
     * @param curso     o novo curso
     * @param nota      a nova nota
     * @throws AlunoNaoEncontradoException se não existir aluno com esse ID
     * @throws DadosInvalidosException      se algum dado for inválido
     * @throws MatriculaDuplicadaException se a matrícula já pertence a outro aluno
     */
    public void atualizarAluno(int id, String nome, String matricula, String curso, double nota)
            throws AlunoNaoEncontradoException, DadosInvalidosException, MatriculaDuplicadaException {
        Aluno aluno = buscarPorId(id);
        validar(id, nome, matricula, curso, nota);

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
        notificarObservers();
    }

    /**
     * Remove um aluno pelo ID (DELETE). A matrícula dele fica livre para ser usada de novo.
     *
     * @param id o ID do aluno que será removido
     * @throws AlunoNaoEncontradoException se não existir aluno com esse ID
     */
    public void deletarAluno(int id) throws AlunoNaoEncontradoException {
        Aluno aluno = buscarPorId(id);
        alunosPorId.remove(id);
        matriculas.remove(aluno.getMatricula());
        notificarObservers();
    }

    /**
     * Coloca no cadastro os alunos lidos do arquivo ao abrir o sistema. Os próximos
     * IDs continuam a partir do maior ID lido.
     *
     * @param alunos os alunos lidos do arquivo
     * @throws DadosInvalidosException      se algum aluno tiver dado inválido ou ID repetido
     * @throws MatriculaDuplicadaException se duas matrículas forem iguais
     */
    public void carregarAlunos(List<Aluno> alunos) throws DadosInvalidosException, MatriculaDuplicadaException {
        Set<Integer> ids = new HashSet<>(alunosPorId.keySet());
        Set<String> matriculasLidas = new HashSet<>(matriculas);

        // primeiro confere todos e só depois adiciona, para o cadastro não ficar pela metade se der erro
        for (Aluno aluno : alunos) {
            validador.validar(aluno);

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

    // único lugar que coloca um aluno nas coleções
    private void adicionar(Aluno aluno) {
        alunosPorId.put(aluno.getId(), aluno);
        matriculas.add(aluno.getMatricula());
        proximoId = Math.max(proximoId, aluno.getId() + 1);
    }

    // a lista é imutável e a mesma para todos, então um observer não atrapalha os outros nem o cadastro
    private void notificarObservers() {
        List<Aluno> alunos = List.copyOf(alunosPorId.values());

        for (CadastroObserver observer : observers) {
            observer.cadastroAlterado(alunos);
        }
    }

    // monta um aluno com os dados digitados só para as regras de validação conferirem
    private void validar(int id, String nome, String matricula, String curso, double nota)
            throws DadosInvalidosException {
        validador.validar(new Aluno(id, nome, matricula, curso, nota));
    }
}
