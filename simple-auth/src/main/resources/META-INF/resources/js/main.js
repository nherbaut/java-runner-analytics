//
// Place any custom JS here
//
window.addEventListener("load", function (e) {
    window.open("/auth");
    document.getElementById("test-roles").addEventListener("click", function () {

        fetch("/private",{
            headers: {
                "Authorization": "Bearer " + window.localStorage.getItem("java-runner-token")
            }
        }).then(res => res.text()).then(t => alert(t));
    });
});

