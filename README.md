# BackEmili — Backend

Um backend em Spring Boot para gestão de usuários e requisições de transporte, com autenticação por sessão, controle de acesso por papel (USER/ADMIN), histórico de status de requisições, endpoints administrativos e estatísticas para dashboards.

## ✨ Visão geral

- Usuários: CRUD com validação, hashing de senha (BCrypt) e papéis (Role.USER/Role.ADMIN)
- Autenticação: login com verificação de senha e emissão de um token de sessão (header `X-Session-Id`)
- Requisições: criação ligada a usuário, sugestão de modais (AÉREO/MARÍTIMO/TERRESTRE), atualização de status com histórico
- Admin: endpoints dedicados para listar e gerenciar requisições, decidir modal (validação), e fornecer métricas de dashboard/relatórios
- Estatísticas: totais por modal e por status (pendentes, em trânsito, finalizadas, canceladas)
- CORS: habilitado para integração com frontend (ex.: Vite em `http://localhost:5173`) e uso dos cabeçalhos customizados
- Bootstrap de admin: promoção via token de setup para criar o primeiro admin usando Postman

## 🧱 Stack e requisitos

- Java 21
- Spring Boot 3.5.x
- Maven (usa `mvnw` no projeto)
- Banco: H2 (arquivo em desenvolvimento; memória em testes)

## 📁 Estrutura relevante

- `src/main/java/Emili/BackEmili/usuario/*` — usuários, DTOs, controller, service, repository
- `src/main/java/Emili/BackEmili/requisicao/*` — requisições, modais, controllers (público e admin), service, repository
- `src/main/java/Emili/BackEmili/status/*` — histórico de status e enum `StatusTipo`
- `src/main/java/Emili/BackEmili/auth/*` — autenticação, sessão e DTOs
- `src/main/java/Emili/BackEmili/estatistica/*` — serviço e controller de estatísticas admin
- `src/main/java/Emili/BackEmili/config/CorsConfig.java` — CORS global
- `src/main/resources/application.properties` — configuração principal
- `src/test/resources/application.properties` — configuração de testes (H2 em memória)

## ⚙️ Configuração

Arquivo `src/main/resources/application.properties` (trechos principais):

```properties
# Token de setup para promoção de usuário via Postman (apenas desenvolvimento)
setup.token=DEV_SETUP_TOKEN

spring.application.name=BackEmili

# Datasource (H2 em arquivo)
spring.datasource.url=jdbc:h2:file:./data/BackEmiliDb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=cadastro_bd
spring.datasource.password=cadastro_bd

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true

# Flyway (habilite quando houver migrações)
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migrations
spring.flyway.baseline-on-migrate=true
```

Arquivo `src/test/resources/application.properties` (testes usam H2 em memória):

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

spring.flyway.enabled=false
```

## 🚀 Como rodar

No Windows (PowerShell), você pode:

```powershell
# Rodar testes
./mvnw test

# Subir aplicação
./mvnw spring-boot:run
```

Ou executar a classe `Emili.BackEmili.BackEmiliApplication` pela sua IDE.

H2 Console: disponível em `/h2-console` (se habilitado), conecte-se ao JDBC configurado em `application.properties`.

## 🔐 Autenticação e Sessão

- Login: `POST /auth/login`
	- Body:
		```json
		{ "email": "admin@exemplo.com", "password": "minhaSenha" }
		```
	- Resposta:
		```json
		{ "token": "<UUID>", "userId": 1, "role": "ADMIN" }
		```
- Logout: `POST /auth/logout`
	- Header: `X-Session-Id: <token>`

Use o token retornado no login como `X-Session-Id` para acessar endpoints protegidos.

## 👑 Bootstrap de Admin (via Postman)

1. Crie um usuário (USER) em `POST /usuario/criar`:
	 ```json
	 {
		 "nome": "Admin Inicial",
		 "email": "admin@exemplo.com",
		 "idade": 30,
		 "password": "minhaSenhaSegura123"
	 }
	 ```
2. Promova para ADMIN: `PUT /usuario/promover/{id}`
	 - Header: `X-Setup-Token: DEV_SETUP_TOKEN`
	 - Body: vazio
3. Faça login em `/auth/login` e use `X-Session-Id: <token>` nas rotas admin.

> Observação: `setup.token` deve ser mantido secreto em produção; reinicie o backend se alterar o valor.

## 📡 Endpoints principais

### Usuário (`/usuario`)
- `POST /usuario/criar` — cria usuário (DTO: `UsuarioCreateDTO`)
- `GET /usuario/listar` — lista usuários
- `GET /usuario/listar/{id}` — detalha
- `PUT /usuario/alterar/{id}` — atualiza (DTO: `UsuarioUpdateDTO`)
- `DELETE /usuario/deletar/{id}` — remove
- `PUT /usuario/promover/{id}` — promoção para ADMIN (Header: `X-Setup-Token`)

### Autenticação (`/auth`)
- `POST /auth/login` — retorna `{ token, userId, role }`
- `POST /auth/logout` — invalida sessão (Header: `X-Session-Id`)

### Requisições — Admin (`/admin/requisicoes`) [Header: `X-Session-Id`]
- `GET /admin/requisicoes` — lista todas
- `GET /admin/requisicoes/{id}` — detalha
- `PUT /admin/requisicoes/{id}/status` — atualiza status (DTO: `RequisicaoUpdateDTO`)
- `POST /admin/requisicoes/{id}/decidir-modal` — valida modal escolhido (DTO: `AdminDecideModalDTO`)

### Requisições — Público (`/requisicoes`) [Header: `X-Session-Id`]
- Endpoints que garantem acesso conforme papel: USER vê suas requisições; ADMIN vê todas.
- Verifique o controller para rotas expostas.

### Estatísticas — Admin (`/admin/estatisticas`) [Header: `X-Session-Id`]
- `GET /admin/estatisticas/dashboard` — retorna `DashboardStatsDTO`:
	```json
	{
		"totalSolicitacoes": 42,
		"totalAereo": 10,
		"totalMaritimo": 8,
		"totalTerrestre": 24,
		"pendentes": 12,
		"emTransito": 7,
		"finalizadas": 20,
		"canceladas": 3
	}
	```
- `GET /admin/estatisticas/relatorio` — mesmo resumo inicial (pronto para filtros de período)

> Definições:
> - `pendentes` = `ABERTA` + `EM_ANALISE`
> - Contagem por modal considera requisições que incluem o modal no conjunto `modais`.
> - Status usa o último registro por requisição (`dataStatus` mais recente).

## 🌐 CORS

Configuração global permite:
- Origem: `http://localhost:5173` (ajuste conforme seu frontend)
- Métodos: `GET, POST, PUT, DELETE, OPTIONS`
- Headers: `Content-Type`, `X-Session-Id`, `X-Setup-Token`

No frontend (ex.: Vite), use `VITE_API_URL` apontando para o backend e envie `X-Session-Id` nas requisições protegidas.

## 🗃️ Banco de dados

- Desenvolvimento: H2 em arquivo em `./data/BackEmiliDb`
- Testes: H2 em memória (isolado, rápido)
- Flyway: habilitado e configurado para `classpath:db/migrations` — adicione suas migrations SQL lá quando necessário.

## 🧪 Testes

```powershell
./mvnw test
```

Em testes, H2 é em memória e Flyway desabilitado (até existirem migrações). Você pode mudar `ddl-auto` para `create-drop` em testes se quiser sempre um schema limpo.

## 📝 Convenções de commit

Mensagens em português com tipos padrão (em inglês), por exemplo:
- `feat(usuario): adicionar promoção via setup token`
- `fix(requisicao): corrigir validação de transição de status`
- `chore(cors): habilitar origem do frontend e cabeçalhos`

## 🛣️ Roadmap sugerido

- Persistir “modal aprovado” no modelo de requisição
- Adicionar filtros de período e agrupamentos (dia/semana/mês) no relatório
- Introduzir Spring Security/JWT no lugar de sessão in-memory
- Criar migrações Flyway iniciais para schema

## 📄 Licença

Projeto privado/sem licença definida. Ajuste conforme sua necessidade.
