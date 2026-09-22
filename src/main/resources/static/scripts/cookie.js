document.addEventListener("DOMContentLoaded", function () {

    const banner = document.getElementById("cookie-banner");
    const acceptButton = document.getElementById("cookie-accept");
    const rejectButton = document.getElementById("cookie-reject");

    const cookieConsent = localStorage.getItem("cookieConsent");

    if (cookieConsent === "accepted") {
        loadGoogleAnalytics();
    }

    if (!banner) {
        return;
    }

    if (cookieConsent) {
        banner.style.display = "none";
        return;
    }

    banner.style.display = "block";

    if (acceptButton) {
        acceptButton.addEventListener("click", function () {
            localStorage.setItem("cookieConsent", "accepted");
            banner.style.display = "none";
            loadGoogleAnalytics();
        });
    }

    if (rejectButton) {
        rejectButton.addEventListener("click", function () {
            localStorage.setItem("cookieConsent", "rejected");
            banner.style.display = "none";
        });
    }
});


function loadGoogleAnalytics() {

    if (document.getElementById("google-analytics-script")) {
        return;
    }

    const script = document.createElement("script");

    script.id = "google-analytics-script";
    script.async = true;
    script.src = "https://www.googletagmanager.com/gtag/js?id=G-81LM89MXC5";

    document.head.appendChild(script);

    window.dataLayer = window.dataLayer || [];

    function gtag() {
        dataLayer.push(arguments);
    }

    window.gtag = gtag;

    gtag("js", new Date());

    gtag("config", "G-81LM89MXC5");
}