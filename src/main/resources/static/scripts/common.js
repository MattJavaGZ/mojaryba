document.addEventListener("DOMContentLoaded", () => {
    // ----------------- MENU TOGGLE -----------------
    const toggle = document.querySelector(".menu-toggle");
    const menu = document.querySelector(".menu");

    if (toggle && menu) {
        toggle.addEventListener("click", () => {
            menu.classList.toggle("expanded");
        });
    }

    // ----------------- DODAWANIE ZDJĘĆ -----------------
    const photosAddDiv = document.getElementById("photos-add-container");
    const addButton = document.getElementById("add-photo-button");

    function createNextInputs() {
        if (!photosAddDiv) return;

        const fileInput = document.createElement("input");
        fileInput.type = "file";
        fileInput.accept = "image/jpeg, image/png";
        fileInput.name = "photos";
        photosAddDiv.append(fileInput);
    }

    if (addButton) {
        addButton.addEventListener("click", (e) => {
            e.preventDefault();
            createNextInputs();
        });
    }

    // ----------------- GALERIA -----------------
    const links = Array.from(document.querySelectorAll(".fish-gallery-container a"));
    const galleryModal = document.getElementById("galleryModal");
    const galleryImage = document.getElementById("galleryImage");
    const galleryCounter = document.getElementById("galleryCounter");
    const closeBtn = document.querySelector(".gallery-close");
    const prevBtn = document.querySelector(".gallery-prev");
    const nextBtn = document.querySelector(".gallery-next");

    let currentIndex = 0;
    let hideArrowsTimeout;

    function updateCounter() {
        if (galleryCounter) {
            galleryCounter.textContent = `${currentIndex + 1} / ${links.length}`;
        }
    }

    function showArrows() {
        if (!prevBtn || !nextBtn) return;

        prevBtn.classList.remove("hidden");
        nextBtn.classList.remove("hidden");

        if (hideArrowsTimeout) clearTimeout(hideArrowsTimeout);

        hideArrowsTimeout = setTimeout(() => {
            prevBtn.classList.add("hidden");
            nextBtn.classList.add("hidden");
        }, 2000);
    }

    function openGallery(event, link) {
        event.preventDefault();
        currentIndex = parseInt(link.dataset.index);
        galleryImage.src = link.dataset.full;
        updateCounter();
        galleryModal.classList.add("open");
        showArrows();
    }

    function closeGallery() {
        galleryModal.classList.remove("open");
        if (prevBtn && nextBtn) {
            prevBtn.classList.add("hidden");
            nextBtn.classList.add("hidden");
        }
        if (hideArrowsTimeout) clearTimeout(hideArrowsTimeout);
    }

    function nextImage() {
        currentIndex = (currentIndex + 1) % links.length;
        galleryImage.src = links[currentIndex].dataset.full;
        updateCounter();
        showArrows();
    }

    function prevImage() {
        currentIndex = (currentIndex - 1 + links.length) % links.length;
        galleryImage.src = links[currentIndex].dataset.full;
        updateCounter();
        showArrows();
    }

    // ----------------- LINKI GALERII -----------------
    links.forEach(link => {
        link.addEventListener("click", (e) => openGallery(e, link));
    });

    // ----------------- PRZYCISKI MODALU -----------------
    if (closeBtn) closeBtn.addEventListener("click", closeGallery);
    if (prevBtn) prevBtn.addEventListener("click", prevImage);
    if (nextBtn) nextBtn.addEventListener("click", nextImage);

    // ----------------- KLAVIATURA -----------------
    document.addEventListener("keydown", (e) => {
        if (!galleryModal.classList.contains("open")) return;
        if (e.key === "ArrowRight") nextImage();
        if (e.key === "ArrowLeft") prevImage();
        if (e.key === "Escape") closeGallery();
    });

    // ----------------- SWIPE MOBILE -----------------
    let touchStartX = 0;
    let touchEndX = 0;

    galleryModal.addEventListener("touchstart", e => {
        touchStartX = e.changedTouches[0].screenX;
        showArrows();
    });

    galleryModal.addEventListener("touchend", e => {
        touchEndX = e.changedTouches[0].screenX;
        const threshold = 50;

        if (touchStartX - touchEndX > threshold) nextImage();
        if (touchEndX - touchStartX > threshold) prevImage();
    });

    // ----------------- AUTO-UKRYWANIE STRZAŁEK -----------------
    ["mouseenter", "mousemove"].forEach(evt => {
        galleryModal.addEventListener(evt, showArrows);
    });
});