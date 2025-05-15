const toggle = document.querySelector(".menu-toggle");
const menu = document.querySelector(".menu");

function toggleMenu() {
    if (menu.classList.contains("expanded")) {
        menu.classList.remove("expanded");
        toggle.querySelector('a').innerHTML = '<i id="toggle-icon" class="fa-solid fa-bars"></i>';
    } else {
        menu.classList.add("expanded");
        toggle.querySelector('a').innerHTML = '<i id="toggle-icon" class="fa-solid fa-bars"></i>';
    }
}

toggle.addEventListener("click", toggleMenu, false);

// -----------------
function createNextInputs() {
    let photosAddDiv = document.getElementById("photos-add-container");
    // let nextInputContainer = document.createElement("div");
    let fileInput = document.createElement("input");
    fileInput.type = "file";
    fileInput.accept = "image/jpeg, image/png";
    fileInput.name = "photos"

    // nextInputContainer.append(fileInput);
    // photosAddDiv.append(nextInputContainer);
    photosAddDiv.append(fileInput);
}

function registerAddButton() {
    let addButton = document.getElementById("add-photo-button");
    addButton.onclick = () => createNextInputs();
}

registerAddButton();