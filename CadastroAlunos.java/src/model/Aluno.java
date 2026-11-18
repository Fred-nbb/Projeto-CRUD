package model;

/**
 * Representa um aluno do sistema de cadastro.
 */
public class Aluno {

    private final int id;
    private String nome;
    private String matricula;
    private String curso;
    private double nota;

    /**
     * Cria um aluno com todos os dados.
     *
     * @param id        identificador único do aluno
     * @param nome      nome completo
     * @param matricula número de matrícula
     * @param curso     nome do curso
     * @param nota      nota do aluno
     */
    public Aluno(int id, String nome, String matricula, String curso, double nota) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
        this.curso = curso;
        this.nota = nota;
    }

    /**
     * Devolve o ID do aluno (não tem setter, porque o ID não muda).
     *
     * @return o ID
     */
    public int getId() {
        return id;
    }

    /**
     * Devolve o nome do aluno.
     *
     * @return o nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Troca o nome do aluno.
     *
     * @param nome o novo nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Devolve a matrícula do aluno.
     *
     * @return a matrícula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Troca a matrícula do aluno.
     *
     * @param matricula a nova matrícula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Devolve o curso do aluno.
     *
     * @return o curso
     */
    public String getCurso() {
        return curso;
    }

    /**
     * Troca o curso do aluno.
     *
     * @param curso o novo curso
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }

    /**
     * Devolve a nota do aluno.
     *
     * @return a nota
     */
    public double getNota() {
        return nota;
    }

    /**
     * Troca a nota do aluno.
     *
     * @param nota a nova nota
     */
    public void setNota(double nota) {
        this.nota = nota;
    }

    /**
     * Texto usado na listagem de alunos.
     *
     * @return os dados do aluno em uma linha
     */
    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Matrícula: " + matricula
                + " | Curso: " + curso + " | Nota: " + nota;
    }
}
