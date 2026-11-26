# Casos de prueba del escenario LabLibre

Ejecutar después de crear la base de datos y configurar la conexión.

| Caso | Datos / acción | Resultado esperado |
|---|---|---|
| 1 | Libro LIB-01; préstamo 2026-11-01; límite 2026-11-15; devolución 2026-11-19; política NORMAL | 4 días de retraso; multa base y final $4.000 |
| 2 | Portátil POR-07; valor $3.200.000; préstamo 2026-11-10; límite 2026-11-13; devolución 2026-11-16; política DOCENTE | Multa base $30.000; multa final $15.000; seguro $64.000 |
| 3 | Kit KIT-03; 20 componentes; préstamo 2026-11-02; límite 2026-11-09; devolución 2026-11-20; 3 faltantes; SEMANA_EXAMENES | 11 días; multa base $48.000; multa final $96.000; seguro $2.000 |
| 4 | Libro LIB-02; préstamo 2026-11-05; límite 2026-11-15; devolución 2026-11-14 | Multa base y final $0 |
| 5 | Intentar prestar un portátil por 5 días | Se muestra PlazoExcedidoException |
| 6 | Intentar prestar un material que tiene un préstamo sin devolución | Se muestra MaterialNoDisponibleException |
| 7 | Devolver un kit de 20 componentes indicando 25 faltantes | Se muestra DevolucionInvalidaException |
| 8 | Cerrar y volver a abrir la aplicación | Los préstamos, seguros, multas y estado de pago siguen almacenados en MySQL |

Las fechas de los casos 1 a 4 corresponden a los datos definidos en el enunciado. Para las pruebas actuales, se puede utilizar una fecha posterior a esas fechas si se quiere probar las consultas de vencidos.
