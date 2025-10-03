# Order Management Frontend
- Project responsible for the system’s screens and usability.


## Technical Decisions

- Use of Angular Material: Although it was requested in the challenge, it is advantageous as it is Google’s official component library, ensuring visual consistency and providing many ready-to-use components.

- Component Modularization: Helps in logically separating screen components and isolates code that is loaded on demand.

- Messages Component Modularization: This component is modularized for system-wide use, helping standardize messages and making changes easier if needed.

- Loading Component Modularization: A request-specific interceptor was created for it.

- Creation of a shared module: Centralizes components that will be reused across system modules and avoids repetitive imports.

- Creation of a core module: Responsible for services and interceptors used throughout the system and that are unique. Centralizing these behaviors also prevents cyclic dependency issues.

- State Management for the Product Module: Avoids each component having to fetch product changes individually and centralizes responsibility, ensuring any changes are transparent to the system, while improving API call performance.

- HTTP Interceptors for Requests and Responses:

- The request interceptor automatically simulates sending a token with every API call.

- The response interceptor standardizes error handling for API responses.

- The loading request interceptor is separate from the auth interceptor to maintain single responsibilities, making maintenance and reuse easier.

- Reactive Forms: Forms are reactive and use Angular Material components, which simplifies component validation.

- SCSS with @media: Ensures responsive design for both desktop and mobile screens.

## How to Run the Frontend Project Without Docker

- How to Run the Frontend Project Without Docker

- If you want to run the frontend manually without Docker, follow the steps below (to run via Docker, see the README.md in the root folder /gestao-de-pedidos):

- Assuming Node.js and Git are installed:

- Open a terminal and run:
```
git init .
```
```
git clone -b main git@github.com:wallyssonjr/gestao_de_pedidos.git
```
```
cd gestao-de-pedidos\gestao-pedidos-front
```
If Angular CLI v19.2.15+ is not installed on your machine, run:
```
npm install -g @angular/cli@19.2.15
```
In the root of gestao-de-pedidos\gestao-pedidos-front, run:
```
npm install
```
After installation, run:
```
ng serve --port 80
```
- Acessar no browser a [Página do sistema](http://localhost)

- Note: The backend must be running for the frontend to function correctly.
