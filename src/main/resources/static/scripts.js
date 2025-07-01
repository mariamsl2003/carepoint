let navlinks = document.getElementsByClassName('nav-link');
let url = window.location.href;
for (let i = 0; i < navlinks.length; i++) {
    if (navlinks[i].href === url) {
        navlinks[i].classList.add('active');
    }
}
(function ($) {

    "use strict";

    $(document).ready(function () {
        $(".toggle-password").click(function () {

            $(this).toggleClass("eye eye-closed");
            var input = $($(this).attr("toggle"));
            if (input.attr("type") == "password") {
                input.attr("type", "text");
            } else {
                input.attr("type", "password");
            }
        });
    });

})(jQuery)