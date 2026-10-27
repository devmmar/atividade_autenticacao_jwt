# Atividade sobre autenticação e autorização com JWT

## 1. Qual a diferença entre autenticação e autorização? Dê um exemplo.

Autenticação é o processo de verificar quem é o usuário. Por exemplo, no login, o sistema confere se o e-mail e a senha informados correspondem a uma conta cadastrada. Autorização é a verificação do que esse usuário pode fazer depois de identificado. Por exemplo, um usuário comum pode consultar imóveis, mas apenas um administrador pode cadastrar um novo imóvel.

## 2. Qual a diferença entre stateful e stateless?

Em um sistema *stateful*, o servidor guarda o estado da interação, como uma sessão de login, e usa esse registro nas próximas requisições. Em um sistema *stateless*, cada requisição contém as informações necessárias para ser processada, sem depender de uma sessão armazenada no servidor. Com JWT, o cliente envia o token em cada requisição protegida, e o servidor verifica sua assinatura e validade.

## 3. Qual a semântica e a sintaxe de um JWT?

Um JWT (*JSON Web Token*) é uma forma compacta de transmitir declarações verificáveis entre sistemas. Sua sintaxe é `header.payload.signature`, com três partes separadas por pontos. O *header* indica o tipo de token e o algoritmo de assinatura. O *payload* contém declarações, como o identificador do usuário, o perfil e a data de expiração. A *signature* permite verificar se o token foi alterado e se foi assinado com a chave esperada. As duas primeiras partes são codificadas em Base64URL, mas não são criptografadas; por isso, não devem conter senhas ou outros dados secretos.
