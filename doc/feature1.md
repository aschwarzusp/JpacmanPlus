# Feature 1: Portões e interruptores

## Símbolos do mapa

- `1` e `2`: interruptores dos circuitos 1 e 2.
- `a` e `b`: portões dos circuitos 1 e 2.
- `#`, espaço, `.`, `P` e `G` mantêm os significados originais.

Os dois circuitos começam fechados. Cada entrada bem-sucedida do jogador em um
interruptor alterna somente o circuito correspondente. Fantasmas atravessam os
interruptores sem acioná-los.

## Portões

Portões abertos permitem entrada e saída. O fechamento de um portão ocupado fica
pendente: os ocupantes podem sair, novas entradas são bloqueadas e o portão fecha
quando seu último ocupante sai. Uma ordem de abertura cancela o fechamento pendente.
A marca `*` identifica o fechamento pendente; o número identifica o circuito.

Mapas sem os novos símbolos continuam válidos. Se um circuito aparecer no mapa,
ele precisa ter ao menos um portão e um interruptor. Símbolos desconhecidos e
circuitos incompletos geram `PacmanConfigurationException`.

## Executar

Na raiz do projeto:

```sh
./gradlew runFeature1
```
