# FINCOSTOS - MVP Funcional

## ✅ Completado

### 3️⃣ Activities Implementadas

#### 1. **MainActivity** (Pantalla Principal)
- 3 botones grandes: Registrar gasto, Registrar venta, Resumen del mes
- Navegación entre pantallas

#### 2. **GastoActivity** (Registrar Gasto)
- Campos: Fecha (hoy por defecto), Tipo de gasto, Concepto, Valor
- Validación de campos
- Guarda en memoria (AppRepository)
- Toast de confirmación
- Vuelve automáticamente a main

#### 3. **VentaActivity** (Registrar Venta)
- Campos: Fecha (hoy por defecto), Cajas, Precio por caja, Total (automático)
- Cálculo automático del total
- Validación de campos
- Guarda en memoria (AppRepository)
- Toast de confirmación
- Vuelve automáticamente a main

#### 4. **ResumenActivity** (Resumen del Mes)
- Muestra 5 métricas clave:
  - 💰 Ingresos (verde)
  - 💸 Gastos (rojo)
  - 📈 Resultado (verde si gana, rojo si pierde)
  - 📦 Cajas vendidas (azul)
  - 📉 Costo por caja (naranja)
- Botón "Volver" para regreso a main

### 📦 Capas de Datos

#### **AppRepository** (Singleton)
- Almacena Gastos y Ventas en listas
- Calcula totales del mes
- Calcula utilidad, costo por caja

#### **Data Models**
- `Gasto`: id, fecha, tipoGasto, concepto, valor, observaciones
- `Venta`: id, fecha, cajas, precioPorCaja, cliente (calcula total automático)

#### **DateUtils**
- Convierte fechas a formato dd/MM/yyyy
- Obtiene hoy automáticamente

### 🎨 Layouts
- activity_main.xml
- activity_gasto.xml
- activity_venta.xml
- activity_resumen.xml
- card_background.xml (estilos para tarjetas)

### 🔗 Manifest
- Todas las Activities registradas
- MainActivity como punto de entrada

---

## 🚀 Funcionamiento Actual

**Flujo:**
1. Usuario abre app → MainActivity
2. Toca "Registrar gasto" → GastoActivity
   - Completa datos → Guardar → vuelve a main
3. Toca "Registrar venta" → VentaActivity
   - Completa datos → Guardar → vuelve a main
4. Toca "Resumen" → ResumenActivity
   - Ve todas las métricas del mes actual

**Los datos se guardan en memoria** (AppRepository.gastos y AppRepository.ventas)
→ Se pierden cuando cierras la app (es temporal, luego irá a BD)

---

## 📋 Próximos Pasos

### Fase 2: Base de Datos
- Implementar Room Database
- Tablas: Configuracion, Gastos, Ventas
- Reemplazar AppRepository con DAO

### Fase 3: Mejoras UI
- Agregar iconos
- Mejorar colores y temas
- Agregar animaciones

### Fase 4: Validaciones
- Límites de valores
- Prevenir duplicados
- Alertas

---

## 🛠️ Tecnologías

- **Language**: Kotlin
- **Framework**: Android API 24+
- **UI**: Material Design 3
- **Storage**: En memoria (temporal)
- **Data**: Models + Singleton Repository

