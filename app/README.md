# UniVibe (App móvil)

Este README resume la estructura, cómo ejecutar el proyecto, la navegación y los aspectos importantes (Autenticación con Firebase/Google Sign-In) para el repositorio UniVibe.

## Contenido rápido
- Proyecto: Android (Compose + Hilt + Firebase)
- Carpeta principal: `app/`
- Lenguaje: Kotlin

## Resumen del proyecto
UniVibe es una aplicación para eventos universitarios. Usa:
- Jetpack Compose para UI
- Hilt para inyección de dependencias
- Firebase Authentication (email/password + Google)
- Navigation Compose con un wrapper centralizado (NavigationWrapper)

## Cómo compilar y ejecutar
En Windows (PowerShell):

```powershell
# Compilar en modo debug
.\gradlew assembleDebug

# Instalar en un dispositivo/emulador conectado
.\gradlew installDebug

# Ejecutar desde Android Studio: Run > app
```

Si cambias dependencias en `build.gradle.kts`, sincroniza/gradle sync desde Android Studio.

## Configuración de Google Sign-In (esencial)
Para que el flujo de Google funcione correctamente debes:
1. Tener `google-services.json` configurado en `app/` (ya existe en el repo).
2. En `app/src/main/res/values/strings.xml` configurar el `default_web_client_id` con el Client ID web de Firebase/Google API Console (ya hay un valor en `strings.xml`).
3. En Firebase Console > Authentication > Sign-in method: habilitar **Google**.
4. Registrar el SHA-1/SHA-256 de tu keystore (debug/producción) en Firebase Project Settings > Your Apps. Si falta el SHA la verificación de token fallará.

En el UI se fuerza el selector de cuenta (se ejecuta `googleSignInClient.signOut()` antes de abrir el intent) para que el usuario pueda elegir/añadir una cuenta.

## Navegación y comportamiento de autenticación
- `NavigationWrapper` calcula `startDestination` leyendo `AuthRepository.isAuthenticatedFlow()` (implementado con `FirebaseAuth.AuthStateListener`) — si el usuario ya está autenticado abre `home`, si no abre `auth`.
- Los ViewModels (ej. `HomeViewModel`, `AuthViewModel`) emiten navegación centralizada usando `NavigationManager.navigateTo(NavRoute.X)`; `NavigationWrapper` recoge `NavigationManager.events` (SharedFlow) y ejecuta la navegación sobre `NavHostController`.
- Ventaja: las pantallas no manejan navegación directamente, cumplen responsabilidad única.

## Rutas / ficheros clave
- Navigation:
  - `app/src/main/java/com/example/univibe/base/Navigation/NavigationWrapper.kt`
  - `app/src/main/java/com/example/univibe/base/Navigation/NavigationManager.kt`
  - `app/src/main/java/com/example/univibe/base/Navigation/NavRoute.kt`
- Autenticación:
  - `app/src/main/java/com/example/univibe/presentation/auth/AuthScreen.kt`
  - `app/src/main/java/com/example/univibe/presentation/auth/AuthViewModel.kt`
  - `app/src/main/java/com/example/univibe/domain/use_case/SignInWithGoogleUseCase.kt`
  - `app/src/main/java/com/example/univibe/domain/repository/AuthRepository.kt`
  - `app/src/main/java/com/example/univibe/data/repository/AuthRepositoryImpl.kt`
- Home / perfil:
  - `app/src/main/java/com/example/univibe/presentation/home/HomeScreen.kt`
  - `app/src/main/java/com/example/univibe/presentation/home/HomeViewModel.kt`
- Icono de app:
  - `app/src/main/res/drawable/ic_logo_univibe.xml` (ahora configurado en `AndroidManifest.xml` como `icon`/`roundIcon`).

## Recursos (drawable) usados en UI
- `person_icon.xml` — icono historial
- `settings_icon.xml` — icono configuración
- `ic_flecha.xml` — flecha para filas
- `log_out_icon.xml` — icono dentro del botón "Cerrar Sesión"
- `foto.png` — foto de perfil usada en `HomeScreen`
- `ic_logo_univibe.xml` — logo de la app (launcher aplicado en manifest)

## Comportamiento del Sign Out
- `SignOutUseCase` invoca `AuthRepository.signOut()` (en la implementación, hace `firebaseAuth.signOut()` y devuelve `AuthResult.Unauthenticated`).
- `HomeViewModel.onSignOutClick()` llama al use case y, si el resultado es `Unauthenticated`, usa `NavigationManager.navigateTo(NavRoute.Auth)` para solicitar la navegación a la pantalla de autenticación.

## Cambios recientes (resumen)
- UI: `HomeScreen` implementada (layout visual según diseño).
- Autenticación: implementado Google Sign-In en `AuthScreen` (obtención idToken y envío a `AuthViewModel.onGoogleSignIn`).
- Navegación: `NavigationManager` expone `events` (SharedFlow). `NavigationWrapper` escucha esos eventos y navega.
- Icono: `AndroidManifest.xml` actualizado para usar `@drawable/ic_logo_univibe`.

## Problemas comunes y soluciones rápidas
- idToken nulo o error al autenticar con Google:
  - Verifica `default_web_client_id` en `res/values/strings.xml` (debe ser el client ID web del proyecto), y que Google sign-in esté habilitado en Firebase.
  - Asegura el SHA-1/256 en Firebase.
- Crash relacionado con Hilt (GeneratedComponent) al iniciar Activity:
  - Asegúrate de anotar la Application con `@HiltAndroidApp` y que `MainActivity` y dependencias estén configuradas correctamente.

## Próximos pasos sugeridos
- Añadir feedback UX (snackbar/toast) para errores en `AuthScreen` y `HomeScreen`.
- Crear pruebas unitarias para ViewModels y use cases.
- Opcional: generar adaptive launcher icon (mipmap) a partir de `ic_logo_univibe` para mejor compatibilidad en todos los launchers.

## Contacto / notas del desarrollador
Si quieres que realice algunos de los próximos pasos (p. ej. agregar snackbar de error, crear unit tests o generar adaptive icon), dime cuál y lo implemento.

---
README generado automáticamente en `app/README.md`.
