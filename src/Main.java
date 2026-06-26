import controller.CampanhaController;
import controller.DraftController;
import integration.JsonLocalFacade;
import integration.PokemonDadosFacade;
import domain.models.Treinador;
import view.CLIView;


void main() {
    CLIView view = new CLIView();
    PokemonDadosFacade facade = new JsonLocalFacade();
    Treinador jogador = DraftController.iniciarDraft(facade, view);
    new CampanhaController().iniciarCampanha(jogador, facade, view);
}