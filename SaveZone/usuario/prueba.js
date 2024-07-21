// Obtener elementos del DOM
const changePasswordLink = document.getElementById('change-password-link');
const passwordForm = document.getElementById('password-form');

// Escuchar el evento clic en el enlace "Cambiar Contraseña"
changePasswordLink.addEventListener('click', function(event) {
    event.preventDefault(); // Prevenir el comportamiento por defecto del enlace
    passwordForm.style.display = 'block'; // Mostrar el formulario
    changePasswordLink.style.display = 'none'; // Ocultar el enlace
});
