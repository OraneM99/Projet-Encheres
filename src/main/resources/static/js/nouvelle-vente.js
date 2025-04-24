document.addEventListener('DOMContentLoaded', function() {
    function formatDate(date) {
        let d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    // Récupérer la date d'aujourd'hui
    const today = new Date();
    const todayFormatted = formatDate(today);

    // Récupérer les champs de date
    const dateDebutInput = document.getElementById('dateDebutEncheres');
    const dateFinInput = document.getElementById('dateFinEncheres');

    // Définir la date de début à aujourd'hui par défaut si elle n'est pas déjà définie
    if (!dateDebutInput.value) {
        dateDebutInput.value = todayFormatted;
    }

    // Définir la date minimale pour le champ de début à aujourd'hui
    dateDebutInput.min = todayFormatted;

    // S'assurer que la date de fin est toujours après ou égale à la date de début
    function updateDateFinMin() {
        // La date minimale pour la fin d'enchère est la date de début
        dateFinInput.min = dateDebutInput.value;

        // Si la date de fin est antérieure à la date de début, on la met à jour
        if (dateFinInput.value < dateDebutInput.value) {
            dateFinInput.value = dateDebutInput.value;
        }
    }

    updateDateFinMin();

    // Si la date de fin n'est pas définie, suggérer une date par défaut (7 jours plus tard)
    if (!dateFinInput.value) {
        const nextWeek = new Date();
        nextWeek.setDate(today.getDate() + 7);
        dateFinInput.value = formatDate(nextWeek);
    }

    dateDebutInput.addEventListener('change', updateDateFinMin);

    // Gestion des boutons calendrier
    const calendarButtons = document.querySelectorAll('.calendar-button');
    calendarButtons.forEach(button => {
        button.addEventListener('click', function() {
            const dateInput = this.parentElement.querySelector('.date-input');
            dateInput.showPicker();
        });
    });
});