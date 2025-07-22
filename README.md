# Movements App

## Descripción

**Movements** es una aplicación Android desarrollada como parte de un challenge técnico. Permite visualizar el balance actual, ingresos, egresos y el historial de transacciones de una cuenta, todo obtenido desde una API simulada. La app está diseñada para demostrar buenas prácticas de desarrollo, arquitectura limpia y calidad de código.

### Demo
https://github.com/user-attachments/assets/eff5f52d-d08f-4791-ac67-55b7d3cb0681

## Características

- **Balance, Ingresos y Egresos:** Muestra en la parte superior el saldo total, la suma de ingresos y la suma de egresos (como valor positivo).
- **Lista de Transacciones:** Visualiza cada transacción con descripción, fecha y monto, diferenciando ingresos (verde) y egresos (rojo).
- **Manejo de Estados de UI:** Incluye estados de carga (skeleton), error y visualización de datos.
- **Pruebas Unitarias:** Cobertura de pruebas para lógica de negocio y ViewModel.
- **Arquitectura Modular:** Separación en módulos `app`, `data` y `domain` siguiendo principios de Clean Architecture.

## Arquitectura y Decisiones de Diseño

### Clean Architecture

El proyecto está estructurado en tres módulos principales:

- **domain:** Contiene modelos de negocio, interfaces de repositorios y casos de uso puros (sin dependencias de frameworks).
- **data:** Implementa los repositorios, servicios de red (API), DTOs y mapeos entre capas.
- **app:** Capa de presentación (UI) usando Jetpack Compose, ViewModel y manejo de estados.

### Principales Decisiones

- **Jetpack Compose:** Para una UI moderna, declarativa y reactiva.
- **ViewModel + State:** Manejo de estado de pantalla y lógica de presentación desacoplada de la UI.
- **DI con Hilt/Dagger:** Inyección de dependencias para facilitar testeo y escalabilidad.
- **Skeleton Loading:** Mejor experiencia de usuario durante la carga de datos.
- **Manejo de Errores:** Mensajes claros y estados diferenciados para error/carga.
- **Colores Accesibles:** Verde para ingresos, rojo para egresos, siguiendo convenciones financieras.

### Estructura de Carpetas

```
movements/
  app/      # UI, ViewModels, estados, temas
  data/     # Repositorios, API, DTOs, DI
  domain/   # Modelos, casos de uso, interfaces
```

## Ejecución

1. **Clona el repositorio:**
   ```bash
   git clone https://github.com/tuusuario/movements.git
   cd movements
   ```

2. **Abre el proyecto en Android Studio.**

3. **Sincroniza y ejecuta:**  
   Usa el botón "Run" o ejecuta en un emulador/dispositivo físico.

## Pruebas y Coverage

### Ejecutar Pruebas Unitarias

Desde Android Studio o por terminal:

```bash
./gradlew test
```

### Reporte de Coverage

Para generar el reporte de cobertura (usando Jacoco):

```bash
./gradlew jacocoTestReport
```

El reporte estará disponible en:  
`app/build/reports/jacoco/jacocoTestReport/html/index.html`

### Estado de las Pruebas
<img width="1875" height="410" alt="image" src="https://github.com/user-attachments/assets/005debc4-2464-4ec6-83cc-43209e5511b2" />

- **Ubicación de tests:**  
  - Lógica de negocio: `domain/src/test/`
  - Repositorios y casos de uso: `data/src/test/`
  - ViewModel y UI state: `app/src/test/`

## Notas y Consideraciones

- El proyecto sigue principios SOLID y separación de responsabilidades.
- El manejo de dependencias y modularidad facilita la escalabilidad y el testeo.
- El diseño visual sigue buenas prácticas de accesibilidad y claridad financiera.
