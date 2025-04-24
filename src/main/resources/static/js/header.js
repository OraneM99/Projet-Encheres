document.addEventListener('DOMContentLoaded', function() {
    const labelCheck = document.getElementById('label-check');
    const mainNav = document.getElementById('mainNav');
    const overlay = document.querySelector('.overlay');

    // Gestion du menu mobile avec la checkbox
    labelCheck.addEventListener('change', function() {
        if (this.checked) {
            mainNav.classList.add('open');
            overlay.classList.add('active');
            document.body.classList.add('menu-open');
        } else {
            mainNav.classList.remove('open');
            overlay.classList.remove('active');
            document.body.classList.remove('menu-open');
        }
    });

    // Fermer le menu quand on clique sur l'overlay
    overlay.addEventListener('click', function() {
        labelCheck.checked = false;
        mainNav.classList.remove('open');
        overlay.classList.remove('active');
        document.body.classList.remove('menu-open');
    });

    // Fermer le menu lorsqu'un lien est cliqué
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function() {
            labelCheck.checked = false;
            mainNav.classList.remove('open');
            overlay.classList.remove('active');
            document.body.classList.remove('menu-open');
        });
    });

    const currentPath = window.location.pathname;
    const navItems = document.querySelectorAll('.nav-link');

    navItems.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath ||
            (currentPath.startsWith('/admin') && href === '/admin/dashboard') ||
            (currentPath === '/' && href === '/')) {
            link.classList.add('active');
        }
    });
});