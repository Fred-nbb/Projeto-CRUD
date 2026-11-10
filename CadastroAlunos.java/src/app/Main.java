package app;

import controller.CadastroAlunos;
import java.nio.file.Path;
import persistence.ArquivoAlunos;
import view.Menu;

public class Main {

    public static void main(String[] args) {
        CadastroAlunos cadastro = new CadastroAlunos();
        ArquivoAlunos arquivo = new ArquivoAlunos(Path.of("alunos.csv"));

        Menu menu = new Menu(cadastro, arquivo);
        menu.iniciar();
    }
}
