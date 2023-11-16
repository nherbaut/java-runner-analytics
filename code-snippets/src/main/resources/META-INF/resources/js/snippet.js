let codeSnippetAPIURL = undefined;
let headers = {};

function snippet_auth(codeSnippetAPIURLParam, auth_token) {

    codeSnippetAPIURL = codeSnippetAPIURLParam;
    if (auth_token != undefined) {
        headers = {"Authorization": "Bearer " + auth_token};
    }

}

async function getMySnippets() {
    let mySnippets = undefined;

    const response = await fetch(codeSnippetAPIURL + "/snippet/mine",
        {
            method: 'GET',
            headers: headers,
            credentials: 'include',
            mode: 'cors',

        });
    if (response.ok) {
        return await response.json();
    }
    return [];

}

async function getSnippet(snippetId) {
    var postHeaders = {...headers};
    postHeaders["Accept"] = "application/json";

    return await fetch(codeSnippetAPIURL + "/snippet/" + snippetId,
        {
            method: 'GET',
            headers: postHeaders,
            credentials: 'include',
            mode: 'cors',

        });


}

async function delSnippet(snippetID) {

    const response = await fetch(codeSnippetAPIURL + "/snippet/" + snippetID,
        {
            method: 'DELETE',
            headers: headers,
            credentials: 'include',
            mode: 'cors',

        });
    if (response.ok) {
        return true;
    }
    return false;
}

function createFile(name, content) {
    return {name: name, content: content};
}

function createComment(content) {
    return {content: content};
}

function createMeta(key, value) {
    return {key: key, value: value};
}

async function updateSnippet(snippetId, title, files, comments, metas) {
    var postHeaders = {...headers};
    postHeaders["Content-type"] = "application/json";

    let response = await fetch(codeSnippetAPIURL + "/snippet/" + snippetId,
        {
            method: 'PUT',
            headers: postHeaders,

            credentials: 'include',
            mode: 'cors',
            body: JSON.stringify({
                title: title,
                files: files,
                comments: comments,
                metas: metas
            })

        });
    return response;
}

async function createSnippet(title, files, comments, metas) {

    if (metas == undefined) {
        metas = [];
    }
    if (comments == undefined) {
        comments = [];
    }
    var postHeaders = {...headers};
    postHeaders["Content-type"] = "application/json";

    let response = await fetch(codeSnippetAPIURL + "/snippet/",
        {
            method: 'POST',
            headers: postHeaders,

            credentials: 'include',
            mode: 'cors',
            body: JSON.stringify({
                title: title,
                files: files,
                comments: comments,
                metas: metas
            })

        });
    return response;
}

export {
    snippet_auth,
    getMySnippets,
    delSnippet,
    createSnippet,
    createFile,
    createComment,
    getSnippet,
    updateSnippet,
    createMeta
}