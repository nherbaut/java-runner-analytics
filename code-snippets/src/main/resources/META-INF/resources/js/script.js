import {
    createMeta
} from "/js/snippet.js"


function createNewMetaForm(metaContainer, key, value) {
    let metaDiv = document.createElement("div");
    let metaKeyInput = document.createElement("input");
    let metaValueInput = document.createElement("input");
    let metaKeyLabel = document.createElement("label");
    let metaValueLabel = document.createElement("label");
    let metadataDeleteInput = document.createElement("input");


    metaDiv.classList.add("meta-card");
    metaKeyLabel.innerText = "Key";
    metaValueLabel.innerText = "Value";
    metaKeyInput.type = "text";
    metaKeyInput.classList.add("form-control");
    metaKeyLabel.classList.add("form-label");
    metaValueInput.classList.add("form-control");
    metaValueLabel.classList.add("form-label");
    metaDiv.classList.add("card", "col-sm-6");
    metadataDeleteInput.classList.add("btn", "btn-danger");
    metadataDeleteInput.value = "delete";
    metaDiv.innerHTML = '<h5 class="card-title">Metadata</h5>';
    metaDiv.appendChild(metaKeyLabel);
    metaDiv.appendChild(metaKeyInput);
    metaDiv.appendChild(metaValueLabel);
    metaDiv.appendChild(metaValueInput);
    metaDiv.appendChild(metadataDeleteInput);
    metaContainer.appendChild(metaDiv);
    metadataDeleteInput.addEventListener("click", function () {
        metaDiv.remove();
    });
    if (key !== undefined) {
        metaKeyInput.value = key;
    }
    if (value !== undefined) {
        metaValueInput.value = value;
    }

    fetch("http://localhost:8081/meta/keys").then(r => r.json()).then(suggestions =>
        new Awesomplete(metaKeyInput, {
            list: suggestions
        }));

    let keyAutocomplete = new Awesomplete(metaValueInput, {
        list: []
    });

    metaKeyInput.addEventListener("focusout", function () {
        console.log("focusout");
        fetch("http://localhost:8081/meta/values?key=" + metaKeyInput.value).then(r => r.json()).then(suggestions => {
            keyAutocomplete.list = suggestions;
        })
    });


}

function extractMetas(metacontainer) {
    const metas = [];
    const metaCards = metacontainer.querySelectorAll('.meta-card');

// Loop through each meta-card and select the nested inputs
    metaCards.forEach((metaCard) => {
        const inputForThisMeta = metaCard.querySelectorAll('input.form-control');  // This will select all inputs inside the meta-card
        let key = inputForThisMeta[0].value;
        let value = inputForThisMeta[1].value;
        metas.push(createMeta(key, value));
    });


    return metas;
}


export {createNewMetaForm, extractMetas}