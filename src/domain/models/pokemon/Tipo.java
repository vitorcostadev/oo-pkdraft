package domain.models.pokemon;

/**
 * Define os tipos e gerencia a matriz de efetividades, fraquezas e imunidades.
 */
public enum Tipo {
    NORMAL, FOGO, AGUA, GRAMA, ELETRICO, GELO, LUTADOR, VENENOSO, TERRA, VOADOR, PSIQUICO, INSETO, PEDRA, FANTASMA, DRAGAO, NOTURNO, ACO, FADA;

    public double calcularEfetividade(Tipo t1, Tipo t2) {
        double e1 = obterEfetividadeBase(this, t1);
        double e2 = (t2 != null) ? obterEfetividadeBase(this, t2) : 1.0;
        return e1 * e2;
    }

    public double obterEfetividadeBase(Tipo atacante, Tipo defensor) {
        if (defensor == null) {
            return 1.0;
        }

        if(atacante == null) atacante = this;

        switch (atacante) {
            case NORMAL:
                if (defensor == PEDRA || defensor == ACO) return 0.5;
                if (defensor == FANTASMA) return 0.0;
                break;
            case FOGO:
                if (defensor == GRAMA || defensor == GELO || defensor == INSETO || defensor == ACO) return 2.0;
                if (defensor == FOGO || defensor == AGUA || defensor == PEDRA || defensor == DRAGAO) return 0.5;
                break;
            case AGUA:
                if (defensor == FOGO || defensor == TERRA || defensor == PEDRA) return 2.0;
                if (defensor == AGUA || defensor == GRAMA || defensor == DRAGAO) return 0.5;
                break;
            case GRAMA:
                if (defensor == AGUA || defensor == TERRA || defensor == PEDRA) return 2.0;
                if (defensor == FOGO || defensor == GRAMA || defensor == VENENOSO || defensor == VOADOR || defensor == INSETO || defensor == DRAGAO || defensor == ACO) return 0.5;
                break;
            case ELETRICO:
                if (defensor == AGUA || defensor == VOADOR) return 2.0;
                if (defensor == ELETRICO || defensor == GRAMA || defensor == DRAGAO) return 0.5;
                if (defensor == TERRA) return 0.0;
                break;
            case GELO:
                if (defensor == GRAMA || defensor == TERRA || defensor == VOADOR || defensor == DRAGAO) return 2.0;
                if (defensor == FOGO || defensor == AGUA || defensor == GELO || defensor == ACO) return 0.5;
                break;
            case LUTADOR:
                if (defensor == NORMAL || defensor == GELO || defensor == PEDRA || defensor == NOTURNO || defensor == ACO) return 2.0;
                if (defensor == VENENOSO || defensor == VOADOR || defensor == PSIQUICO || defensor == INSETO || defensor == FADA) return 0.5;
                if (defensor == FANTASMA) return 0.0;
                break;
            case VENENOSO:
                if (defensor == GRAMA || defensor == FADA) return 2.0;
                if (defensor == VENENOSO || defensor == TERRA || defensor == PEDRA || defensor == FANTASMA) return 0.5;
                if (defensor == ACO) return 0.0;
                break;
            case TERRA:
                if (defensor == FOGO || defensor == ELETRICO || defensor == VENENOSO || defensor == PEDRA || defensor == ACO) return 2.0;
                if (defensor == GRAMA || defensor == INSETO) return 0.5;
                if (defensor == VOADOR) return 0.0;
                break;
            case VOADOR:
                if (defensor == GRAMA || defensor == LUTADOR || defensor == INSETO) return 2.0;
                if (defensor == ELETRICO || defensor == PEDRA || defensor == ACO) return 0.5;
                break;
            case PSIQUICO:
                if (defensor == LUTADOR || defensor == VENENOSO) return 2.0;
                if (defensor == PSIQUICO || defensor == ACO) return 0.5;
                if (defensor == NOTURNO) return 0.0;
                break;
            case INSETO:
                if (defensor == GRAMA || defensor == PSIQUICO || defensor == NOTURNO) return 2.0;
                if (defensor == FOGO || defensor == LUTADOR || defensor == VENENOSO || defensor == VOADOR || defensor == FANTASMA || defensor == ACO || defensor == FADA) return 0.5;
                break;
            case PEDRA:
                if (defensor == FOGO || defensor == GELO || defensor == VOADOR || defensor == INSETO) return 2.0;
                if (defensor == LUTADOR || defensor == TERRA || defensor == ACO) return 0.5;
                break;
            case FANTASMA:
                if (defensor == PSIQUICO || defensor == FANTASMA) return 2.0;
                if (defensor == NOTURNO) return 0.5;
                if (defensor == NORMAL) return 0.0;
                break;
            case DRAGAO:
                if (defensor == DRAGAO) return 2.0;
                if (defensor == ACO) return 0.5;
                if (defensor == FADA) return 0.0;
                break;
            case NOTURNO:
                if (defensor == PSIQUICO || defensor == FANTASMA) return 2.0;
                if (defensor == LUTADOR || defensor == NOTURNO || defensor == FADA) return 0.5;
                break;
            case ACO:
                if (defensor == GELO || defensor == PEDRA || defensor == FADA) return 2.0;
                if (defensor == FOGO || defensor == AGUA || defensor == ELETRICO || defensor == ACO) return 0.5;
                break;
            case FADA:
                if (defensor == LUTADOR || defensor == DRAGAO || defensor == NOTURNO) return 2.0;
                if (defensor == FOGO || defensor == VENENOSO || defensor == ACO) return 0.5;
                break;
            default:
                break;
        }
        return 1.0;
    }
}