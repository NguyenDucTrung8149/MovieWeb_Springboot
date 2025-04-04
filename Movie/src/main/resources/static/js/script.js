'use strict';

/**
 * navbar variables
 */

const navOpenBtn = document.querySelector("[data-menu-open-btn]");
const navCloseBtn = document.querySelector("[data-menu-close-btn]");
const navbar = document.querySelector("[data-navbar]");
const overlay = document.querySelector("[data-overlay]");

const navElemArr = [navOpenBtn, navCloseBtn, overlay];

for (let i = 0; i < navElemArr.length; i++) {

  navElemArr[i].addEventListener("click", function () {

    navbar.classList.toggle("active");
    overlay.classList.toggle("active");
    document.body.classList.toggle("active");

  });

}



/**
 * header sticky
 */

const header = document.querySelector("[data-header]");

window.addEventListener("scroll", function () {

  window.scrollY >= 10 ? header.classList.add("active") : header.classList.remove("active");

});



/**
 * go top
 */

const goTopBtn = document.querySelector("[data-go-top]");

window.addEventListener("scroll", function () {

  window.scrollY >= 500 ? goTopBtn.classList.add("active") : goTopBtn.classList.remove("active");

});

const themeToggle = document.getElementById("theme-toggle");
const body = document.body;

// Kiểm tra chế độ đã lưu trước đó trong localStorage và thiết lập giao diện
if (localStorage.getItem("theme") === "dark") {
    body.classList.remove("light-mode"); // Đặt chế độ tối
    themeToggle.textContent = "sáng";
} else {
    body.classList.add("light-mode"); // Đặt chế độ sáng là mặc định
    themeToggle.textContent = "tối";
}

// Sự kiện click để chuyển đổi chế độ
themeToggle.addEventListener("click", () => {
    body.classList.toggle("light-mode");
    
    // Cập nhật nút và lưu chế độ trong localStorage
    if (body.classList.contains("light-mode")) {
        themeToggle.textContent = "tối";
        localStorage.setItem("theme", "light");
    } else {
        themeToggle.textContent = "sáng";
        localStorage.setItem("theme", "dark");
    }
});






