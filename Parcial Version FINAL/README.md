# LabLibre - Variante 2

## Requisitos
- JDK 17 o superior.
- MySQL 8.0 o superior.
- MySQL Connector/J (archivo `.jar`).

## Preparar la base de datos
1. Abre MySQL Workbench o tu cliente MySQL.
2. Ejecuta el archivo `BaseDatos.sql`.
3. Si tu usuario o contraseña de MySQL son diferentes, modifica `USUARIO` y `CONTRASENA` en `src/Conexion.java`.

## Configurar Visual Studio Code
1. Abre la carpeta `LabLibre_Completo`.
2. Copia el conector JDBC dentro de `lib/`.
3. Crea `.vscode/settings.json` con:

```json
{
  "java.project.referencedLibraries": [
    "lib/**/*.jar"
  ]
}
```

4. Ejecuta `Main.java`.

## Funciones
- Registrar préstamo de Libro, Portátil o Kit.
- Consultar disponibilidad para impedir préstamos simultáneos del mismo material.
- Mostrar, editar, registrar devolución y eliminar préstamos.
- Consultar préstamos vencidos a una fecha dada.
- Consultar multas pagadas agrupadas por tipo de material.
- Calcular seguros por polimorfismo.
- Aplicar Strategy: Normal, Docente y Semana de exámenes.

## Organización
Para evitar el problema previo con imports de paquetes, esta versión mantiene las clases directamente en `src`. El proyecto conserva separación lógica mediante nombres de clases DAO, modelo, estrategia y formularios, pero no usa declaraciones `package`.
