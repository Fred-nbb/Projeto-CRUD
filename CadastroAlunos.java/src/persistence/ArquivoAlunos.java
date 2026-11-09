package persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import model.Aluno;

// Salva e lê os alunos de um arquivo CSV (uma linha por aluno, colunas separadas por ';').
public class ArquivoAlunos {

    public static final String SEPARADOR = ";";
    private static final String CABECALHO = "id;nome;matricula;curso;nota";
    private static final int QUANTIDADE_COLUNAS = 5;

    private final Path caminho;

    public ArquivoAlunos(Path caminho) {
        this.caminho = caminho;
    }

    public Path getCaminho() {
        return caminho;
    }

    // Reescreve o arquivo inteiro com a lista atual de alunos
    public void salvar(Collection<Aluno> alunos) throws IOException {
        Path pasta = caminho.toAbsolutePath().getParent();
        if (pasta != null) {
            Files.createDirectories(pasta);
        }

        // try-with-resources: fecha o arquivo sozinho, mesmo se der erro no meio
        try (BufferedWriter escritor = Files.newBufferedWriter(caminho, StandardCharsets.UTF_8)) {
            escritor.write(CABECALHO);
            escritor.newLine();

            for (Aluno aluno : alunos) {
                escritor.write(paraLinha(aluno));
                escritor.newLine();
            }
        }
    }

    // Lê todos os alunos do arquivo. Se o arquivo ainda não existe (primeira vez), devolve lista vazia.
    public List<Aluno> carregar() throws IOException {
        List<Aluno> alunos = new ArrayList<>();

        if (Files.notExists(caminho)) {
            return alunos;
        }

        try (BufferedReader leitor = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            String linha;
            int numeroLinha = 0;

            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;

                if (numeroLinha == 1) {
                    linha = linha.replace("﻿", ""); // tira o BOM que o Excel às vezes coloca
                    if (linha.equals(CABECALHO)) {
                        continue;
                    }
                }
                if (linha.isBlank()) {
                    continue;
                }

                alunos.add(lerLinha(linha, numeroLinha));
            }
        }
        return alunos;
    }

    // Copia o arquivo para "alunos.csv.bak", para não perder os dados se ele estiver com problema
    public Path fazerBackup() throws IOException {
        Path backup = caminho.resolveSibling(caminho.getFileName() + ".bak");
        Files.copy(caminho, backup, StandardCopyOption.REPLACE_EXISTING);
        return backup;
    }

    private String paraLinha(Aluno aluno) {
        return String.join(SEPARADOR,
                String.valueOf(aluno.getId()),
                aluno.getNome(),
                aluno.getMatricula(),
                aluno.getCurso(),
                String.valueOf(aluno.getNota()));
    }

    private Aluno lerLinha(String linha, int numeroLinha) throws IOException {
        String[] colunas = linha.split(SEPARADOR, -1);

        if (colunas.length != QUANTIDADE_COLUNAS) {
            throw new IOException("Linha " + numeroLinha + " do arquivo " + caminho.getFileName()
                    + " deveria ter " + QUANTIDADE_COLUNAS + " colunas, mas tem " + colunas.length + ".");
        }

        try {
            int id = Integer.parseInt(colunas[0].trim());
            double nota = Double.parseDouble(colunas[4].trim());
            return new Aluno(id, colunas[1].trim(), colunas[2].trim(), colunas[3].trim(), nota);
        } catch (NumberFormatException e) {
            throw new IOException("Linha " + numeroLinha + " do arquivo " + caminho.getFileName()
                    + " tem um ID ou uma nota que não é número: \"" + linha + "\".", e);
        }
    }
}
