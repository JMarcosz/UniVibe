# UniVibe 🎉

<p align="center">
  <img src="app/src/main/res/drawable/logo_univibe.png" alt="UniVibe Logo" width="120"/>
</p>

<p align="center">
  <strong>Tu compañero para descubrir y vivir experiencias universitarias inolvidables</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Kotlin-blue.svg" alt="Language">
  <img src="https://img.shields.io/badge/Architecture-MVVM-orange.svg" alt="Architecture">
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg" alt="UI">
</p>

---

## 📖 Tabla de Contenidos

- [¿Qué es UniVibe?](#-qué-es-univibe)
- [¿Por qué usar UniVibe?](#-por-qué-usar-univibe)
- [Características Principales](#-características-principales)
- [Cómo usar UniVibe](#-cómo-usar-univibe)
  - [Para Usuarios Finales](#para-usuarios-finales)
  - [Para Desarrolladores](#para-desarrolladores)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Instalación y Configuración (clonar el proyecto)](#-instalación-y-configuración-clonar-el-proyecto)
- [Guía de Uso Detallada](#-guía-de-uso-detallada)
- [Casos de Uso](#-casos-de-uso-principales)
- [Roadmap](#-roadmap)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)

---

## 🎯 ¿Qué es UniVibe?

**UniVibe** es una aplicación móvil nativa para Android que sirve como hub central de **eventos universitarios**.

Está pensada para:

- Estudiantes que quieren descubrir, seguir y asistir a eventos académicos, culturales, deportivos y sociales.
- Centros de estudiantes y organizadores que necesitan un canal único para comunicar sus actividades.

En una frase: **UniVibe es tu agenda inteligente de eventos universitarios**.

### ¿Qué resuelve exactamente?

- Reemplaza el caos de anuncios en WhatsApp, Instagram, correos y carteles físicos.
- Te muestra **en un solo lugar** todo lo que pasa en tu universidad.
- Te permite **suscribirte** a eventos, marcarlos como favoritos y **llevar tu control** con códigos QR.

---

## 💡 ¿Por qué usar UniVibe?

### Para Estudiantes

#### 1. **Ahorra Tiempo** ⏱️
No más búsqueda en múltiples plataformas. Todo está en un solo lugar, organizado y fácil de encontrar.

#### 2. **No te pierdas nada** 📢
Sistema de suscripciones que te mantiene actualizado sobre los eventos que te interesan sin necesidad de estar pendiente constantemente.

#### 3. **Descubre nuevas experiencias** 🌟
Algoritmo de sugerencias basado en tus intereses y eventos que has marcado como favoritos.

#### 4. **Gestión simplificada** 📋
Todos tus eventos suscritos en un solo lugar con códigos QR de acceso rápido.

#### 5. **Conexión con la comunidad** 👥
Ve qué eventos son populares entre tus compañeros estudiantes.

### Para Organizadores de Eventos

#### 1. **Mayor visibilidad** 📣
Llega a todos los estudiantes de manera directa y efectiva.

#### 2. **Gestión eficiente** 📊
Sistema de códigos QR para registro y asistencia automatizada.

#### 3. **Análisis de interés** 📈
Métricas sobre "me gusta" y suscripciones para medir el interés real.

#### 4. **Comunicación directa** 💬
Canal directo con estudiantes interesados en tus eventos.

### Ventajas Técnicas

- 🚀 **Rendimiento optimizado**: Carga rápida y experiencia fluida
- 🔒 **Seguridad robusta**: Autenticación con Firebase y protección de datos
- 📱 **Diseño moderno**: Interfaz intuitiva siguiendo Material Design 3
- 🔄 **Sincronización en tiempo real**: Actualización instantánea de información
- 📴 **Funcionalidad offline**: Acceso a eventos guardados sin conexión

---

## 📱 Características Principales

### 🔐 Autenticación y Gestión de Perfil

#### Múltiples Métodos de Autenticación
- **Email y Contraseña**: Registro tradicional seguro
- **Google Sign-In**: Acceso rápido con tu cuenta institucional
- **Recuperación de contraseña**: Sistema seguro de recuperación

#### Perfil Personalizado
- **Foto de perfil inteligente**:
  - Sube tu propia foto desde la galería
  - Sincronización automática con foto de Google
  - Avatar generado con inicial si no tienes foto
- **Información personal**: Gestiona nombre, apellido, email y teléfono
- **Modo edición**: Actualiza tu información fácilmente
- **Historial de eventos**: Ve cuántos eventos tienes suscritos

### 🎉 Gestión de Eventos

#### Exploración Avanzada
- **Vista de eventos sugeridos**: Basado en tus intereses
- **Búsqueda inteligente**: Encuentra eventos por nombre o descripción
- **Filtros dinámicos**:
  - Por categoría (Deportes, Música, Arte, Tecnología, Social, etc.)
  - Por fecha (Hoy, Mañana, Esta semana, Próximo mes)
  - Por ubicación

#### Interacción con Eventos
- ⭐ **Sistema de "Me gusta"**: Marca eventos favoritos
- 📝 **Suscripción rápida**: Un toque para registrarte
- 📊 **Detalles completos**: 
  - Título y descripción
  - Categoría del evento
  - Fecha y hora
  - Ubicación exacta
  - Organizador
- 🎟️ **Código QR único**: Cada evento genera un QR para acceso rápido

#### Mis Eventos
- **Lista personalizada**: Todos tus eventos suscritos en un solo lugar
- **Acceso rápido**: Abre detalles del evento directamente
- **Gestión de suscripciones**: Suscríbete o cancela fácilmente

### 🧭 Navegación Intuitiva

La aplicación cuenta con 4 secciones principales:

1. **🏠 Home**: 
   - Eventos sugeridos para ti
   - Eventos más populares
   - Tus eventos favoritos recientes

2. **🔍 Buscar**: 
   - Buscador con filtros avanzados
   - Categorías dinámicas
   - Filtrado por fecha

3. **📋 Mis Eventos**: 
   - Eventos a los que estás suscrito
   - Códigos QR de acceso
   - Historial de participación

4. **👤 Perfil**: 
   - Información personal
   - Configuración de cuenta
   - Cierre de sesión

---

## 🚀 Cómo usar UniVibe

### Para Usuarios Finales

#### 📲 Descarga e Instalación

1. **Descarga la aplicación**
   - Desde Google Play Store (próximamente)
   - O instala el APK desde [Releases](https://github.com/tu-usuario/univibe/releases)

2. **Primer inicio**
   - Abre UniVibe
   - Verás la pantalla de bienvenida

#### 🔑 Registro y Login

**Opción 1: Registro con Email**
1. Toca "Registrate" en la pantalla de inicio
2. Completa el formulario:
   - Nombre y apellido
   - Correo electrónico institucional
   - Número de teléfono
   - Contraseña segura
3. (Opcional) Sube una foto de perfil
4. Toca "Registrarse"
5. Verifica tu email si es necesario

**Opción 2: Inicio con Google**
1. En la pantalla de login, toca el botón "Google"
2. Selecciona tu cuenta institucional de Google
3. Autoriza los permisos necesarios
4. ¡Listo! Tu perfil se crea automáticamente con tu información de Google

#### 🎯 Explorando Eventos

**Desde Home:**
1. Abre la app y estarás en la pantalla principal
2. Navega por los eventos sugeridos deslizando
3. Toca cualquier evento para ver detalles completos
4. Desde los detalles puedes:
   - Dar "Me gusta" tocando el ícono de corazón
   - Suscribirte tocando "Suscribirme"
   - Ver el código QR del evento

**Desde Buscar:**
1. Toca el ícono de lupa en la barra de navegación inferior
2. Usa el buscador para encontrar eventos específicos
3. Aplica filtros:
   - **Por fecha**: Selecciona entre las opciones de filtro temporal
   - **Por categoría**: Elige la categoría que te interese
4. Toca cualquier evento para ver más información

#### ❤️ Marcando Favoritos

1. Abre cualquier evento
2. Toca el ícono de estrella/corazón
3. El evento se marcará como favorito
4. Vuelve a tocar para quitar de favoritos

#### 📝 Suscribiéndote a Eventos

1. Abre el evento que te interesa
2. Toca el botón "Suscribirme"
3. Verás confirmación visual
4. El evento aparecerá en tu sección "Mis Eventos"
5. Se generará un código QR único para ti

#### 📋 Gestionando Mis Eventos

1. Toca el ícono de calendario en la navegación inferior
2. Verás todos los eventos a los que estás suscrito
3. Para cada evento puedes:
   - Ver detalles completos
   - Ver tu código QR de acceso
   - Cancelar suscripción si lo deseas

#### 🎟️ Usando Códigos QR

1. Abre "Mis Eventos"
2. Selecciona el evento al que asistirás
3. Toca en el evento para ver el código QR
4. Presenta el código QR al organizador en el evento
5. El organizador escaneará el código para registrar tu asistencia

#### 👤 Gestionando tu Perfil

**Ver información:**
1. Toca el ícono de perfil en la navegación inferior
2. Verás tu foto, nombre y estadísticas
3. Información personal está organizada en secciones

**Editar perfil:**
1. En tu perfil, toca el ícono de editar (lápiz)
2. Modifica la información que desees:
   - Nombre y apellido
   - Teléfono
   - Email
3. Toca "Guardar Cambios"

**Cambiar foto de perfil:**
1. En modo edición, toca el ícono de cámara en tu foto
2. Selecciona "Galería"
3. Elige una foto de tu dispositivo
4. La foto se subirá automáticamente

**Cerrar sesión:**
1. Ve a tu perfil
2. Desplázate hasta abajo
3. Toca "Cerrar Sesión"
4. Confirma la acción

#### 💡 Tips y Mejores Prácticas

✅ **Mantén tu perfil actualizado** para recibir notificaciones relevantes
✅ **Marca como favoritos eventos que te interesen** para seguimiento fácil
✅ **Suscríbete temprano** a eventos con cupos limitados
✅ **Revisa "Mis Eventos"** regularmente para no perderte ninguna actividad
✅ **Usa los filtros de búsqueda** para descubrir eventos específicos
✅ **Guarda los códigos QR offline** tomando capturas de pantalla

---

### Para Desarrolladores

#### 🛠️ Configuración del Entorno de Desarrollo

**Prerrequisitos:**
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17 o superior
- SDK Android 24+ (Android 7.0 Nougat)
- Cuenta de Firebase
- Git instalado

**Pasos de instalación:**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/tu-usuario/univibe.git
   cd univibe
   ```

2. **Configurar Firebase**
   
   a. Crear proyecto en Firebase Console:
   - Ve a [Firebase Console](https://console.firebase.google.com/)
   - Clic en "Agregar proyecto"
   - Nombre: "UniVibe"
   - Sigue el asistente de configuración

   b. Agregar aplicación Android:
   - En el proyecto Firebase, clic en el ícono de Android
   - Package name: `com.example.univibe`
   - Descarga `google-services.json`
   - Coloca el archivo en `app/google-services.json`

   c. Habilitar servicios:
   - **Authentication**: 
     - Ve a Authentication > Sign-in method
     - Habilita "Email/Password"
     - Habilita "Google"
   - **Firestore Database**:
     - Ve a Firestore Database > Crear base de datos
     - Modo: Empezar en modo de prueba
   - **Storage**:
     - Ve a Storage > Comenzar
     - Modo: Modo de prueba

3. **Configurar Google Sign-In**
   ```xml
   <!-- app/src/main/res/values/strings.xml -->
   <string name="default_web_client_id">TU_WEB_CLIENT_ID_AQUI</string>
   ```
   - Obtén el Web Client ID desde Firebase Console > Authentication > Sign-in method > Google

4. **Configurar reglas de Firestore**
   ```javascript
   rules_version = '2';
   service cloud.firestore {
     match /databases/{database}/documents {
       match /Users/{userId} {
         allow read, write: if request.auth != null && request.auth.uid == userId;
       }
       match /Events/{eventId} {
         allow read: if request.auth != null;
         allow write: if request.auth != null;
       }
     }
   }
   ```

5. **Sincronizar y compilar**
   ```bash
   # Desde terminal en el directorio del proyecto
   ./gradlew clean build
   ```
   O desde Android Studio: Build > Rebuild Project

6. **Ejecutar la aplicación**
   - Conecta un dispositivo físico o inicia un emulador
   - Clic en Run (▶️) en Android Studio
   - O desde terminal: `./gradlew installDebug`

#### 📚 Estructura del Código

**Navegación por el proyecto:**

```
app/src/main/java/com/example/univibe/
│
├── base/                          # Configuración base de la app
│   ├── MainActivity.kt            # Activity principal
│   └── navigation/                # Sistema de navegación
│       ├── NavRoute.kt            # Definición de rutas
│       └── NavigationManager.kt   # Gestor de navegación
│
├── data/                          # Capa de datos
│   └── repository/                # Implementaciones
│       ├── AuthRepositoryImpl.kt  # Autenticación
│       ├── EventRepositoryImpl.kt # Eventos
│       └── UserRepositoryImpl.kt  # Usuarios
│
├── domain/                        # Lógica de negocio
│   ├── model/                     # Modelos de dominio
│   │   ├── User.kt
│   │   ├── Event.kt
│   │   └── AuthResult.kt
│   ├── repository/                # Interfaces
│   │   ├── AuthRepository.kt
│   │   ├── EventRepository.kt
│   │   └── UserRepository.kt
│   └── usecase/                   # Casos de uso
│       ├── auth/                  # Autenticación
│       ├── event/                 # Eventos
│       └── user/                  # Usuarios
│
├── presentation/                  # Capa de presentación
│   ├── auth/                      # Pantalla de autenticación
│   │   ├── AuthScreen.kt
│   │   ├── AuthViewModel.kt
│   │   └── AuthUiState.kt
│   ├── register/                  # Pantalla de registro
│   ├── home/                      # Pantalla principal
│   ├── events/                    # Mis eventos
│   ├── find_event/                # Búsqueda de eventos
│   ├── profile/                   # Perfil de usuario
│   ├── components/                # Componentes reutilizables
│   │   ├── UserAvatar.kt
│   │   ├── PhotoSelectionModal.kt
│   │   ├── EventCard.kt
│   │   └── QrCodeSection.kt
│   ├── navigation/                # Navegación UI
│   └── theme/                     # Tema visual
│
└── di/                            # Inyección de dependencias
    ├── AppModule.kt
    └── RepositoryModule.kt
```

#### 🔧 Comandos Útiles

```bash
# Limpiar proyecto
./gradlew clean

# Compilar debug
./gradlew assembleDebug

# Compilar release
./gradlew assembleRelease

# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests de instrumentación
./gradlew connectedAndroidTest

# Ver dependencias
./gradlew dependencies

# Generar documentación
./gradlew dokkaHtml
```

#### 🧪 Testing

**Tests Unitarios:**
```kotlin
// Ejemplo de test de ViewModel
@Test
fun `when user logs in successfully, state should be Authenticated`() = runTest {
    // Given
    coEvery { signInUseCase(any(), any()) } returns AuthResult.Authenticated

    // When
    viewModel.onSignInClick()

    // Then
    assert(viewModel.uiState.value is AuthResult.Authenticated)
}
```

**Tests de Integración:**
```kotlin
// Ejemplo de test de repositorio
@Test
fun `repository should save user correctly`() = runTest {
    // Given
    val user = User(userId = "123", email = "test@example.com")

    // When
    val result = userRepository.updateUserProfile(user)

    // Then
    assert(result.isSuccess)
}
```

#### 🎨 Personalización del Tema

Edita `presentation/theme/Color.kt`:
```kotlin
val PrimaryBlue = Color(0xFF007BFF)      // Color principal
val SecondaryBlue = Color(0xFF0F4D67)    // Color secundario
val BtnPrimary = Color(0xFF055EAF)       // Botones primarios
val TextGray = Color(0xFF262626)         // Texto principal
```

#### 📝 Agregar Nuevas Funcionalidades

**Ejemplo: Agregar nueva pantalla**

1. Crear el modelo de UI State:
```kotlin
data class NuevaPantallaUiState(
    val isLoading: Boolean = false,
    val data: String = "",
    val error: String? = null
)
```

2. Crear el ViewModel:
```kotlin
@HiltViewModel
class NuevaPantallaViewModel @Inject constructor(
    private val useCase: MiUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(NuevaPantallaUiState())
    val uiState = _uiState.asStateFlow()
    
    // Lógica del ViewModel
}
```

3. Crear el Composable:
```kotlin
@Composable
fun NuevaPantallaScreen(
    viewModel: NuevaPantallaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // UI con Jetpack Compose
}
```

4. Agregar a la navegación en `NavigationWrapper.kt`

---

## 📘 Guía de Uso Detallada

### Escenarios de Uso Comunes

#### Escenario 1: Estudiante Nuevo en el Campus

**María acaba de ingresar a la universidad y quiere integrarse:**

1. **Día 1 - Instalación y Registro**
   - Descarga UniVibe desde Play Store
   - Se registra con su email institucional
   - Completa su perfil con nombre y foto

2. **Día 2 - Exploración**
   - Abre la app y explora eventos sugeridos
   - Descubre un "Tour por el Campus" programado para mañana
   - Le da "Me gusta" y se suscribe

3. **Día 3 - Asistencia al Evento**
   - Recibe recordatorio del evento
   - Va a "Mis Eventos" y genera su código QR
   - Presenta el QR en la entrada del evento
   - Conoce otros estudiantes nuevos

4. **Semana 1 - Participación Activa**
   - Filtra eventos por categoría "Social"
   - Se suscribe a 3 eventos más
   - Empieza a recibir sugerencias personalizadas

**Resultado:** María se integró rápidamente gracias a UniVibe

#### Escenario 2: Estudiante Buscando Eventos Específicos

**Carlos está interesado en tecnología y programación:**

1. **Búsqueda dirigida**
   - Abre la sección "Buscar"
   - Escribe "hackathon" en el buscador
   - Aplica filtro de categoría "Tecnología"

2. **Descubrimiento**
   - Encuentra "Hackathon Universitario 2024"
   - Lee la descripción completa y horarios
   - Ve que 45 estudiantes ya se suscribieron

3. **Decisión y acción**
   - Le da "Me gusta" para guardarlo
   - Se suscribe al evento
   - Recibe código QR de participación

4. **Seguimiento**
   - Marca como favoritos otros eventos de tecnología
   - Recibe sugerencias similares en Home

**Resultado:** Carlos encontró exactamente lo que buscaba

#### Escenario 3: Organizador de Eventos

**Laura es representante estudiantil y organiza eventos:**

1. **Publicación** (funcionalidad futura)
   - Crea un nuevo evento "Feria de Emprendimiento"
   - Añade descripción, fecha, hora y lugar
   - Sube imagen promocional

2. **Seguimiento**
   - Ve cuántos estudiantes dieron "Me gusta"
   - Monitorea suscripciones en tiempo real
   - Ajusta cupos según demanda

3. **Día del evento**
   - Usa escáner QR para registro de asistencia
   - Valida códigos QR de participantes
   - Genera lista de asistencia automática

**Resultado:** Laura gestiona el evento eficientemente

### Preguntas Frecuentes (FAQ)

#### General

**P: ¿UniVibe es gratuita?**  
R: Sí, UniVibe es completamente gratuita para todos los estudiantes.

**P: ¿Necesito conexión a internet?**  
R: Necesitas internet para explorar eventos nuevos, pero puedes acceder a "Mis Eventos" sin conexión.

**P: ¿Funciona en mi universidad?**  
R: UniVibe está diseñada para funcionar en cualquier universidad. Los eventos dependen de las publicaciones de tu institución.

#### Cuenta y Perfil

**P: ¿Puedo usar mi email personal?**  
R: Sí, aunque recomendamos usar tu email institucional para recibir eventos relevantes.

**P: ¿Qué pasa con mi foto de Google?**  
R: Si inicias sesión con Google, tu foto se sincroniza automáticamente. Puedes cambiarla más tarde subiendo una personalizada.

**P: ¿Puedo tener múltiples cuentas?**  
R: No es necesario. Una cuenta es suficiente para gestionar todos tus intereses.

**P: ¿Cómo recupero mi contraseña?**  
R: En la pantalla de login, toca "¿Olvidaste tu contraseña?" e ingresa tu email.

#### Eventos

**P: ¿Qué es el código QR?**  
R: Es un código único para cada evento suscrito que valida tu registro al presentarlo en el evento.

**P: ¿Puedo cancelar mi suscripción?**  
R: Sí, abre el evento y toca "Cancelar suscripción" o el botón de suscripción nuevamente.

**P: ¿Cuántos eventos puedo suscribir?**  
R: No hay límite. Puedes suscribirte a todos los eventos que desees.

**P: ¿Puedo ver eventos pasados?**  
R: Actualmente solo se muestran eventos actuales y futuros.

#### Técnicas

**P: ¿Por qué la app pide permisos de cámara?**  
R: Para permitirte subir fotos de perfil desde tu dispositivo.

**P: ¿Es segura mi información?**  
R: Sí. Usamos Firebase Authentication y encriptación de datos.

**P: ¿La app consume muchos datos?**  
R: No. Solo carga imágenes cuando están en pantalla y usa caché eficiente.

### Solución de Problemas

#### No puedo iniciar sesión

**Problema:** "Error al iniciar sesión"  
**Soluciones:**
1. Verifica tu conexión a internet
2. Confirma que tu email y contraseña son correctos
3. Si usas Google Sign-In, asegúrate de tener Google Play Services actualizado
4. Intenta cerrar y abrir la app

#### Los eventos no cargan

**Problema:** Pantalla en blanco o error al cargar eventos  
**Soluciones:**
1. Verifica tu conexión a internet
2. Desliza hacia abajo para refrescar (pull to refresh)
3. Cierra y abre la app
4. Borra caché: Ajustes del dispositivo > Apps > UniVibe > Borrar caché

#### No puedo subir foto de perfil

**Problema:** Error al subir imagen  
**Soluciones:**
1. Verifica permisos de almacenamiento en ajustes del dispositivo
2. Asegúrate de que la imagen no sea muy grande (máx 10MB)
3. Intenta con otra imagen
4. Reinicia la app

#### El código QR no se genera

**Problema:** No veo el código QR en mis eventos  
**Soluciones:**
1. Verifica que estés suscrito al evento
2. Toca el evento para abrir los detalles completos
3. Espera unos segundos para que se genere
4. Si persiste, desuscríbete y vuelve a suscribirte

---

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una **Arquitectura Limpia (Clean Architecture)** con el patrón **MVVM (Model-View-ViewModel)** para asegurar:

- **Separación de responsabilidades**: Cada capa tiene una función específica
- **Testabilidad**: Código fácil de probar de manera unitaria
- **Escalabilidad**: Estructura modular que facilita el crecimiento
- **Mantenibilidad**: Código limpio y organizado

### Estructura de Capas

```
app/
├── data/                    # Capa de Datos
│   ├── repository/          # Implementaciones de repositorios
│   └── ...
├── domain/                  # Capa de Dominio
│   ├── model/              # Modelos de datos del dominio
│   ├── repository/         # Interfaces de repositorios
│   └── usecase/            # Casos de uso (lógica de negocio)
└── presentation/            # Capa de Presentación
    ├── auth/               # Pantallas de autenticación
    ├── home/               # Pantalla principal
    ├── events/             # Pantalla de eventos suscritos
    ├── find_event/         # Pantalla de búsqueda de eventos
    ├── profile/            # Pantalla de perfil
    ├── register/           # Pantalla de registro
    ├── components/         # Componentes reutilizables
    ├── navigation/         # Gestión de navegación
    └── theme/              # Tema y colores de la app
```

---

## 🛠️ Tecnologías Utilizadas

### Lenguaje y Framework
- **Kotlin**: Lenguaje de programación principal
- **Jetpack Compose**: Framework moderno para UI declarativa

### Arquitectura y Patrones
- **MVVM**: Patrón de arquitectura Model-View-ViewModel
- **Clean Architecture**: Separación en capas (Data, Domain, Presentation)
- **Repository Pattern**: Abstracción de fuentes de datos
- **Use Cases**: Encapsulación de lógica de negocio

### Inyección de Dependencias
- **Hilt/Dagger**: Inyección de dependencias

### Backend y Servicios
- **Firebase Authentication**: Autenticación de usuarios
- **Firebase Firestore**: Base de datos en tiempo real
- **Firebase Storage**: Almacenamiento de imágenes

### Librerías Adicionales
- **Coil**: Carga de imágenes
- **Material Design 3**: Componentes de diseño
- **Coroutines**: Programación asíncrona
- **Flow**: Manejo reactivo de datos
- **Navigation Component**: Navegación entre pantallas

---

## 🚀 Instalación y Configuración (clonar el proyecto)

Esta sección explica **cómo levantar UniVibe en tu propio entorno** cuando clonas el repositorio.

### 1. Clonar el repo

```bash
git clone https://github.com/tu-usuario/univibe.git
cd univibe
```

### 2. Abrir el proyecto en Android Studio

1. Abrir **Android Studio**.
2. `File > Open...` y selecciona la carpeta del proyecto.
3. Espera a que Gradle sincronice.

### 3. Crear proyecto en Firebase

1. Ve a [Firebase Console](https://console.firebase.google.com/).
2. Crea un nuevo proyecto, por ejemplo: **UniVibe Dev**.
3. Dentro del proyecto, agrega una app **Android** con:
   - **Package name**: `com.example.univibe` (o el que uses en tu `app/build.gradle.kts`).
   - **App nickname**: `UniVibe Dev` (opcional).

### 4. Configurar `google-services.json`

1. Desde Firebase, descarga el archivo `google-services.json` para Android.
2. Colócalo en la ruta:
   - `app/google-services.json`

Sin este archivo, la app no podrá conectarse a Firebase (auth, Firestore, storage).

### 5. Obtener SHA-1 y SHA-256 de tu app

Firebase/Google Sign-In necesita los hashes de tu keystore.

#### Opción A: Usar keystore de debug por defecto

En la mayoría de setups, Android Studio ya genera un keystore de debug. Ejecuta en tu terminal:

```bash
cd /ruta/a/tu/android-sdk

# En Windows (PowerShell)
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

# En macOS / Linux
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Copia los valores de **SHA-1** y **SHA-256**.

#### Opción B: Usar un keystore propio

Si usas un keystore personalizado para debug/release:

```bash
keytool -list -v -keystore /ruta/a/tu/keystore.jks -alias TU_ALIAS
```

(Te pedirá la contraseña del keystore.)

### 6. Registrar hashes en Firebase (Google Sign-In)

1. En Firebase Console, ve a **Authentication > Método de acceso > Google**.
2. Asegúrate de que Google esté **habilitado**.
3. En el proyecto Firebase, sección **Configuración del proyecto > Tus apps > Android**:
   - Edita la app que creaste.
   - Agrega los hashes **SHA-1** y **SHA-256** obtenidos.
4. Guarda los cambios y, si es necesario, descarga de nuevo el `google-services.json` actualizado y reemplázalo en `app/`.

### 7. Configurar `default_web_client_id`

En Firebase, en la sección de **credenciales OAuth** del proyecto, encontrarás el **Web client ID** que usa Google Sign-In.

1. Copia el valor del **client ID OAuth 2.0 (tipo Web)** asociado a tu app.
2. En tu proyecto, abre `app/src/main/res/values/strings.xml` y asegúrate de tener:

```xml
<string name="default_web_client_id">TU_WEB_CLIENT_ID_AQUÍ</string>
```

Reemplaza `TU_WEB_CLIENT_ID_AQUÍ` por el valor real.

### 8. Activar servicios en Firebase

En Firebase Console:

- **Authentication**
  - Ve a *Authentication > Método de acceso*.
  - Habilita **Email/Password**.
  - Habilita **Google**.

- **Firestore Database**
  - Ve a *Firestore Database* y crea una base de datos.
  - Para desarrollo, puedes usar reglas de prueba como:

```js
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /Users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    match /Events/{eventId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null; // Ajustar para producción
    }
  }
}
```

- **Storage**
  - Ve a *Storage* y crea un bucket (por defecto).
  - Para desarrollo, reglas mínimas:

```js
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null; // Ajustar para producción
    }
  }
}
```

### 9. Variables locales (`local.properties`)

Revisa que tu archivo `local.properties` (no se versiona en Git) tenga al menos:

```properties
sdk.dir=C:\\Users\\TU_USUARIO\\AppData\\Local\\Android\\Sdk
```

Si tienes claves locales adicionales (por ejemplo, para Crashlytics, Maps, etc.), puedes documentarlas aquí, pero la configuración principal de UniVibe se basa en Firebase.

### 10. Compilar y ejecutar

Desde Android Studio:

1. Sincroniza Gradle (`Sync Project`).
2. Selecciona un dispositivo/emulador.
3. Run ▶️.

O desde terminal:

```bash
./gradlew assembleDebug
./gradlew installDebug
```

Si todo está bien configurado, deberías ver la **AuthScreen** y poder:
- Iniciar sesión con email/contraseña.
- Iniciar sesión con Google.
- Navegar al **Home**, **Buscar**, **Mis Eventos** y **Perfil**.

