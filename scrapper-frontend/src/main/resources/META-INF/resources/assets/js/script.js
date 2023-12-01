var assignementURL = undefined;
var realURL = undefined;

function showFieldConfiguration() {

    document.getElementById("dynamicForm").hidden = false;
    document.getElementById("dynamicForm").hidden = false;

}

function siteAddressFormReset() {
    document.getElementById("webAddress").disabled = false;
    document.getElementById("webAddress-checker").disabled = false;
    document.getElementById("confirm-url-ok").hidden = true;
    document.getElementById("confirm-url-ko").hidden = true;
    document.getElementById("already-owned").hidden = true;

}

document.getElementById("webAddress-checker").addEventListener("click", function () {

    var address = document.getElementById("webAddress");
    document.getElementById("webAddress").disabled = true;
    document.getElementById("webAddress-checker").disabled = true;

    window.alert("L'adresse va être testée et affichée dans un nouvel onglet. Visitez cet onglet pour savoir si le résultat est conforme avec la page originale (le javascript est supprimé)");
    document.getElementById("pending-url-fetch").hidden = false;

    document.getElementById("confirm-url-ok").addEventListener("click", showFieldConfiguration);
    document.getElementById("confirm-url-ko").addEventListener("click", siteAddressFormReset);
    realURL = address.value;
    fetch("/url", {
        body: address.value,
        method: "POST",
        redirect: "follow"
    }).then(resp => {
        if (resp.status == 200) {
            console.log("address is ok");
            if (resp.headers.has("X-OWNER-INFO")) {
                document.getElementById("already-owned").hidden = false;
                document.getElementById("already-owned").innerHTML = `
                <p>ce site est déjà réservé par un groupe.</p>
                 <p>Si c'est vous et que vous essayez de re-configurer le projet, pas de problème. Si ce n'est pas vous, vous ne pouvez plus réserver.</p> <pre>`
                    + atob(resp.headers.get("X-OWNER-INFO")) + "</pre>";
            }
            assignementURL = resp.url;
            window.open(resp.url, "_blank");
            document.getElementById("confirm-url-ok").hidden = false;
            document.getElementById("confirm-url-ko").hidden = false;

        } else {
            siteAddressFormReset();

        }
        document.getElementById("pending-url-fetch").hidden = true;
    });
});


document.addEventListener('DOMContentLoaded', () => {
    const formSectionsItem = document.getElementById('formSections-item');
    const formSectionsItems = document.getElementById('formSections-items');
    const addSectionItemBtn = document.getElementById('addSection-item');
    const removeSectionItemBtn = document.getElementById('removeSection-item');
    const addSectionItemsBtn = document.getElementById('addSection-items');
    const removeSectionItemsBtn = document.getElementById('removeSection-items');

    function createSection(e) {
        if (e.target.classList.contains("item")) {
            createSectionWithTarget(formSectionsItem);
        } else if (e.target.classList.contains("items")) {
            createSectionWithTarget(formSectionsItems);
        }
    }

    function removeSection(e) {
        if (e.target.classList.contains("item")) {
            removeSectionWithTarget(formSectionsItem);
        } else if (e.target.classList.contains("items")) {
            removeSectionWithTarget(formSectionsItems);
        }
    }

    function createSectionWithTarget(e) {
        const sectionCount = e.children.length;
        if (sectionCount >= 15) return;

        const section = document.createElement('div');
        section.className = 'section mb-2 row';

        section.innerHTML = `
            <div class="col-sm">
                <input type="text" class="form-control user-input" name="textField${sectionCount + 1}" required minlength="3" maxlength="50" onblur="validateInput(this)">
            </div>
            <div class="col-sm">
                <select name="dropdown${sectionCount + 1}" class="form-select mt-2">
                    <option>java.lang.Integer</option>
                    <option>java.lang.Double</option>
                    <option>java.lang.Boolean</option>
                    <option>java.lang.String</option>
                    <option>java.util.ArrayList&lt;Integer&gt;</option>
                    <option>java.util.ArrayList&lt;Double&gt;</option>
                    <option>java.util.ArrayList&lt;Boolean&gt;</option>
                    <option>java.util.ArrayList&lt;String&gt;</option>
                </select>
            </div>
        `;

        e.appendChild(section);
    }

    function removeSectionWithTarget(e) {
        if (e.children.length > 7) { // 2 + 5
            e.removeChild(e.lastChild);
        }
    }

    addSectionItemsBtn.addEventListener('click', createSection);
    removeSectionItemsBtn.addEventListener('click', removeSection);

    addSectionItemBtn.addEventListener('click', createSection);
    removeSectionItemBtn.addEventListener('click', removeSection);

    // Initialize with 3 sections
    for (let i = 0; i < 5; i++) {
        createSectionWithTarget(formSectionsItem);
        createSectionWithTarget(formSectionsItems);
    }

    document.getElementById('dynamicForm').addEventListener('submit', function (event) {
        event.preventDefault();
        for(let field of document.querySelectorAll(".user-input")){
            if(field.classList.contains("invalid")){
                alert("corrigez les erreurs avant de soumettre le formulaire");
                return;
            }
        }
        var formData = new Array();
        for (let scope of ["item", "items"]) {
            for (let section of document.querySelectorAll(`#formSections-${scope} > div.section`)) {
                formData.push({
                    name: section.querySelector("input").value,
                    type: section.querySelector("select").value,
                    scope: scope
                });
            }
        }
        // Send JSON data to /home URL
        fetch('/home', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                formData: formData,
                assignementURL: assignementURL,
                groups: document.getElementById("groupTD").value,
                names: document.getElementById("nameInput").value,
            })
        }).then(res => res.blob())
            .then(blob => {
                var file = window.URL.createObjectURL(blob);
                window.location.assign(file);
            });
        ;
    });
});

function isValidString(str) {
    const regex = /^[A-Za-z]{3,50}$/;
    return regex.test(str);
}

function validateInput(input) {
    if (isValidString(input.value)) {
        input.classList.add('valid');
        input.classList.remove('invalid');
    } else {
        input.classList.add('invalid');
        input.classList.remove('valid');
    }
    for(let field of document.querySelectorAll(".user-input")){
        if(field!=input && field.value==input.value){
            input.classList.add('invalid');
            input.classList.remove('valid');
            field.classList.add('invalid');
            field.classList.remove('valid');
            break;
        }
        else{
            input.classList.add('valid');
            input.classList.remove('invalid');
        }

    }
}

