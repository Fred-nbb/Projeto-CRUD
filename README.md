# 🎓 Sistema de Cadastro de Alunos (CRUD)

Projeto Integrador da disciplina de Programação Orientada a Objetos (MINF-0301).

Sistema feito em Java para cadastrar, listar, atualizar e deletar alunos pelo terminal. Os dados ficam salvos no arquivo `alunos.csv`, então continuam lá quando o programa é fechado e aberto de novo.

## Funcionalidades

- Cadastrar aluno (nome, matrícula, curso e nota). O ID é gerado automaticamente.
- Listar os alunos na ordem em que foram cadastrados
- Atualizar os dados de um aluno pelo ID
- Deletar um aluno pelo ID
- Salvar tudo no `alunos.csv` a cada alteração e carregar esse arquivo quando o programa abre

Regras:

- Nome, matrícula e curso não podem ficar vazios
- A nota tem que estar entre 0 e 10 (pode digitar com ponto ou vírgula, ex.: `7.5` ou `7,5`)
- Não pode ter dois alunos com a mesma matrícula
- Se a pessoa digitar letra onde era número, o programa avisa e pergunta de novo

## Estrutura do projeto

```
Projeto-CRUD/
├── README.md
├── .gitignore
└── CadastroAlunos.java/                  # pasta do projeto no VS Code
    ├── .vscode/settings.json             # diz ao VS Code onde ficam src, test e lib
    ├── lib/
    │   └── junit-platform-console-standalone.jar   # JUnit 5
    ├── src/
    │   ├── app/Main.java                 # inicia o programa
    │   ├── model/Aluno.java              # dados do aluno
    │   ├── controller/CadastroAlunos.java  # regras e operações do CRUD
    │   ├── persistence/ArquivoAlunos.java  # leitura e escrita do alunos.csv
    │   ├── exception/                    # exceções do sistema
    │   └── view/Menu.java                # menu e leitura do teclado
    └── test/
        ├── controller/CadastroAlunosTest.java
        ├── controller/CadastroComArquivoTest.java
        └── persistence/ArquivoAlunosTest.java
```

## Instalação

1. Instale o **Java JDK 17 ou mais novo** (por exemplo, o Eclipse Temurin, em https://adoptium.net). Pra conferir, abra o terminal e rode `java -version` e `javac -version`: os dois têm que mostrar a versão 17 ou maior.
2. (Opcional) Instale o **VS Code** com a extensão **Extension Pack for Java**, que já traz o executor de testes.
3. Baixe o projeto:

   ```
   git clone https://github.com/Fred-nbb/Projeto-CRUD.git
   ```

   ou, na página do repositório no GitHub, clique em **Code > Download ZIP** e extraia.

Não precisa instalar mais nada: o JUnit já vem dentro da pasta `lib`.

## Compilação e execução

**Pelo VS Code**

1. Abra a pasta `CadastroAlunos.java` (Arquivo > Abrir Pasta). Tem que ser essa pasta, e não a `Projeto-CRUD`, porque a configuração do projeto está dentro dela.
2. Abra `src/app/Main.java` e clique em **Run**, em cima do `main`.

**Pelo terminal**, dentro da pasta `CadastroAlunos.java`:

```
javac -encoding UTF-8 -d bin -sourcepath src src/app/Main.java
java -cp bin app.Main
```

O arquivo `alunos.csv` é criado na pasta de onde o programa foi executado.

## Como rodar os testes

Os testes usam JUnit 5 e ficam na pasta `test`. O jar do JUnit já vem na pasta `lib`, então não precisa baixar nada.

**Pelo VS Code**: abra o painel **Testing** (ícone de tubo de ensaio na barra lateral) e clique em **Run Tests**. Também dá pra rodar um teste só, pelo botão que aparece do lado de cada `@Test`.

**Pelo terminal**, dentro da pasta `CadastroAlunos.java`:

Windows (PowerShell):

```powershell
javac -encoding UTF-8 -d bin -cp lib/junit-platform-console-standalone.jar -sourcepath src (Get-ChildItem -Recurse test -Filter *.java).FullName
java -jar lib/junit-platform-console-standalone.jar execute -cp bin --scan-classpath
```

Linux ou macOS:

```bash
javac -encoding UTF-8 -d bin -cp lib/junit-platform-console-standalone.jar -sourcepath src $(find test -name "*.java")
java -jar lib/junit-platform-console-standalone.jar execute -cp bin --scan-classpath
```

São 30 testes:

- `CadastroAlunosTest`: cadastro, listagem, atualização, remoção e as validações (campo vazio, nota fora de 0 a 10, matrícula repetida, ID que não existe).
- `ArquivoAlunosTest`: salvar e ler o arquivo, arquivo que ainda não existe, linha com erro e cópia de segurança.
- `CadastroComArquivoTest`: testa o cadastro junto com o arquivo, como se o programa fosse fechado e aberto de novo. Confere que os alunos voltam, que a atualização e a remoção continuam valendo e que os IDs continuam de onde pararam.

## O que foi usado em cada parte

### Exceções

Ficam no pacote `exception` e todas são filhas de `CadastroException`:

| Exceção | Quando acontece | Exemplo de mensagem |
|---|---|---|
| `AlunoNaoEncontradoException` | o ID digitado não existe | Nenhum aluno encontrado com o ID 7. |
| `DadosInvalidosException` | campo vazio, nota fora de 0 a 10 ou `;` no texto | A nota deve estar entre 0 e 10. |
| `MatriculaDuplicadaException` | a matrícula já é de outro aluno | Já existe um aluno com a matrícula 2026001. |

O `Menu` trata todas com um único `catch (CadastroException e)` e mostra a mensagem pro usuário.

Se o `alunos.csv` estiver com algum problema (por exemplo, uma nota que não é número), a leitura gera uma `IOException` dizendo qual linha está errada. Nesse caso o programa salva uma cópia em `alunos.csv.bak` antes de continuar, pra não perder os dados.

### Coleções

Usadas na classe `CadastroAlunos`:

- `Map<Integer, Aluno>` (`LinkedHashMap`): acha o aluno pelo ID direto e mantém a ordem de cadastro.
- `Set<String>` (`HashSet`): guarda as matrículas e não deixa ter repetidas.
- `List<Aluno>`: é o que `listarAlunos()` devolve, e é o que vai pro arquivo e volta dele.

### Arquivo

A classe `ArquivoAlunos` usa `BufferedWriter` e `BufferedReader` (de `java.nio.file.Files`) com try-with-resources, que fecha o arquivo mesmo se der erro. O `alunos.csv` fica assim:

```
id;nome;matricula;curso;nota
1;Maria Silva;2026001;Ciência da Computação;8.5
```

### Código sem duplicação

- A leitura do teclado fica toda em `lerTexto`, `lerInteiro` e `lerNota` (no `Menu`).
- A validação dos dados fica num método só (`validarDados`), usado pelo cadastro, pela atualização e pelo carregamento do arquivo.
- Um único método (`adicionar`) coloca o aluno nas coleções.

## Integrantes

- Daniel Barros Duarte da Silva
- João Gabriel Silva Santiago
- José Bezerra Sandes Neto
- Frederico Nogueira Brasil Bezerra
