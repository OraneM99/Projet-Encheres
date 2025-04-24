document.addEventListener('DOMContentLoaded', function() {
    const enchereForm = document.querySelector('form[action^="/encherir/"]');

    if (enchereForm) {
        enchereForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const actionUrl = this.getAttribute('action');

            fetch(actionUrl, {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Erreur réseau');
                    }
                    return response.text();
                })
                .then(data => {
                    const parser = new DOMParser();
                    const htmlDoc = parser.parseFromString(data, 'text/html');

                    const meilleureOffre = htmlDoc.querySelector('.auction-info-row:nth-child(3) .auction-info-value span:first-child');
                    const encherisseur = htmlDoc.querySelector('.auction-info-row:nth-child(3) .auction-info-value span:last-child');

                    if (meilleureOffre) {
                        document.querySelector('.auction-info-row:nth-child(3) .auction-info-value span:first-child').textContent = meilleureOffre.textContent;
                    }

                    if (encherisseur) {
                        document.querySelector('.auction-info-row:nth-child(3) .auction-info-value span:last-child').textContent = encherisseur.textContent;
                    }

                    alert('Votre enchère a été placée avec succès !');
                })
                .catch(error => {
                    console.error('Erreur:', error);
                    alert('Une erreur est survenue lors de la soumission de votre enchère.');
                });
        });
    }
});