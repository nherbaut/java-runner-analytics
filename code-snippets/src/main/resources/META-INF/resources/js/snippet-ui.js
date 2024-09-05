



function createNewFileInput(fileName = "", fileContent = "") {

        const fileContainer = document.getElementById("files-container");
        const newFileEntry = document.createElement("div");
        newFileEntry.classList.add("file-entry", "mb-3");

        newFileEntry.innerHTML = `
           <div class="mb-3">
    <label for="fileName" class="form-label">File Name</label>
    <input type="text" name="fileName[]" value="`+fileName+`" class="form-control">
</div>
<div class="mb-3">
    <label for="fileContent" class="form-label">File Content</label>
    <textarea name="fileContent[]" class="form-control">`+fileContent+`</textarea>
</div>
<button type="button" class="btn btn-danger delete-file">Delete File</button>
        `;

        fileContainer.appendChild(newFileEntry);

        // Add delete functionality to the new file entry
        newFileEntry.querySelector(".delete-file").addEventListener("click", function () {
            newFileEntry.remove();
        });



}



export {
    createNewFileInput
}