import controller.CampanhaController;
import controller.DraftController;
import domain.facade.JsonLocalFacade;
import domain.facade.PokemonDadosFacade;
import domain.models.battle.Treinador;
import view.CLIView;
import view.InterfaceJogo;

import java.io.FileNotFoundException;
import java.io.IOException;


public class Main{

    public static void main(String... args) {
        InterfaceJogo view = new CLIView();
        PokemonDadosFacade facade;
        try {
            facade = new JsonLocalFacade();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Não foi possível encontrar o arquivo 'pokemons_base.json'.");
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        DraftController draft = new DraftController();
        Treinador jogador = draft.iniciarDraft(facade, view);
        new CampanhaController().iniciarCampanha(jogador, facade, view);
    }


}