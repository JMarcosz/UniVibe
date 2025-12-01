package com.example.univibe.domain.use_case.user

import javax.inject.Inject

class ValidateUserDataUseCase @Inject constructor() {

    data class ValidationResult(
        val isValid: Boolean,
        val errors: Map<String, String> = emptyMap()
    )

    operator fun invoke(
        firstName: String,
        lastName: String,
        email: String,
        phone: String
    ): ValidationResult {
        val errors = mutableMapOf<String, String>()

        // Validar nombre
        if (firstName.isBlank()) {
            errors["firstName"] = "El nombre es requerido"
        } else if (firstName.length < 2) {
            errors["firstName"] = "El nombre debe tener al menos 2 caracteres"
        }

        // Validar apellido
        if (lastName.isBlank()) {
            errors["lastName"] = "El apellido es requerido"
        } else if (lastName.length < 2) {
            errors["lastName"] = "El apellido debe tener al menos 2 caracteres"
        }

        // Validar email
        if (email.isBlank()) {
            errors["email"] = "El email es requerido"
        } else if (!isValidEmail(email)) {
            errors["email"] = "Email inválido"
        }

        // Validar teléfono (opcional pero si está debe ser válido)
        if (phone.isNotBlank() && !isValidPhone(phone)) {
            errors["phone"] = "Teléfono inválido"
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        // Formato básico: acepta números, paréntesis, guiones y espacios
        val phoneRegex = "^[\\d\\s()\\-+]+$".toRegex()
        return phone.matches(phoneRegex) && phone.replace(Regex("[^\\d]"), "").length >= 7
    }
}

