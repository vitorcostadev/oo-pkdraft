<div align="center">
  
# Pokémon Draft

Este repositório contém o código-fonte do projeto **POKEMON DRAFT**, feito para entregar na disciplina de Programação Orientada a Objetos.

[![My Skills](https://skillicons.dev/icons?i=java)](https://skillicons.dev)

</div>

# Introdução
O **pokemon draft** é um projeto bem simples. O foco foi mais centrado em entregar algo que atendesse aos requisitos de avaliação impostos pela professora da disciplina. Nele você pode:

- Escolher pokémons para formar um time (*draft*)
- Batalhar contra adversários com o time que formou

A interação é totalmente via CLI (*Command-Line Interface*) e os pokémons são sorteados aleatoriamente a cada momento do draft.

# Documentação
O projeto foi construído em cima da arquitetura MVC (*Model-View-Controller*) e utilizou os seguintes padrões de projeto:

- *Builder*: Responsável por construir os objetos `Pokemon` via `PokemonBuilder`.
- *Command*: Para controlar os momentos onde o `Pokemon` vai atacar (`ComandoAtacar`) ou trocar (`ComandoTrocar`).
- *State*: Para fazer os controles de prioridade e resolver as ações para o turno atual.
- *Strategy*: Utilizado para construir toda a lógica de comportamento em batalha, sem precisar acoplar na batalha diretamente.
- *Facade*: Utilizado para construir uma interface que faça a leitura dos pokémons via JSON.

### Controladores e Serviços
- `DraftController`: Controla as rodadas de escolha. Ele sorteia 3 opções de Pokémons por vez com naturezas aleatórias até você completar seu time de 6.
- `CampanhaController`: Organiza a sequência de lutas contra os treinadores da Liga de Kanto. Ele cura a vida do seu time entre uma vitória e outra, e encerra o jogo se todos os seus Pokémons desmaiarem.
- `LogService`: Mostra as mensagens na tela com uma pequena pausa de 1.2 segundos para a leitura não ficar rápida demais, e salva tudo o que aconteceu em um arquivo `.pklog` dentro da pasta `logs`.

### Mecânicas do Jogo
Para deixar o simulador mais fiel aos jogos originais, foram implementados os seguintes sistemas:
- **Atributos (Stats):** Cada Pokémon possui 6 atributos principais: HP (Vida), Ataque, Defesa, Ataque Especial, Defesa Especial e Velocidade.
- **Naturezas (Natures):** Ao ser sorteado, o Pokémon recebe uma natureza aleatória que pode modificar os valores dos seus atributos.
- **Movimentos e Tipos:** Os Pokémons possuem golpes de tipos elementares diferentes. Cada golpe é classificado como *Físico* (usa Ataque/Defesa comuns) ou *Especial* (usa Ataque/Defesa Especiais).

### Sistema de Combate
- **Calculadora de Dano:** Quando um Pokémon ataca, o sistema calcula o dano misturando o poder do golpe, os atributos de quem ataca e de quem defende, e a vantagem/desvantagem de tipo do golpe contra o Pokémon alvo.
- **Ordem do Turno:** A velocidade dos Pokémons e o tipo de ação escolhida (atacar ou trocar) determinam quem joga primeiro a cada turno.

### Carregamento de Dados
Os dados dos pokémons foram armazenados no arquivo `pokemons_base.json` e são tratados pela classe `JsonLocalFacade`. Se o arquivo sumir ou der erro na leitura, o sistema usa dados de segurança salvos direto no código para o jogo não fechar:

```java
private void carregarDadosDoDisco() {
    try {
        String conteudo = new String(Files.readAllBytes(Paths.get("pokemons_base.json")));
        processarJson(conteudo);
    } catch (Exception e) {
        carregarDadosDeContingencia();
    }
}
```
### Diagrama da Arquitetura

<div align="center">
  
  <img src="logs/diagram.png" width="85%" alt="Diagrama UML de Classes do Pokémon Draft" />
  
</div>

## Como executar
Para rodar esse projeto, é bem simples, você só precisa ter o Java instalado (21+):
1. Clone este projeto ou baixe o `.zip`.
2. Na pasta raiz do projeto (`src`), compile.
```bash
javac Main.java
```
E depois, rode com:
```bash
java Main
```
<div align="center">
  
# Autores
[@vitorcostadev](https://github.com/vitorcostadev) e Fernando Nunes.

</div>
